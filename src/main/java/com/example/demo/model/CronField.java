package com.example.demo.model;

import com.example.demo.exception.InvalidCronFieldException;
import lombok.Data;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Data
public class CronField {

    private final String expressionPart;
    private CronFieldType fieldType;
    private Set<Integer> parsedValues = new TreeSet<>();


    public String toString() {
        return parsedValues.stream().map(Object::toString).collect(Collectors.joining(" "));
    }

    public CronField(String expressionPart, CronFieldType fieldType) throws InvalidCronFieldException {
        this.expressionPart = expressionPart;
        this.fieldType = fieldType;

        if (!parseFixedValues() && !parseRangeOfValues() && !parseIntervals()) {
            // If no pattern matched, treat it as a single value
            int singleValue = parseNumber(expressionPart);
            populateValues(singleValue, singleValue, 1);
        }
    }

    private boolean parseFixedValues() throws InvalidCronFieldException {
        String[] fixedDates = expressionPart.split(",");
        if (fixedDates.length > 1) {
            for (String date : fixedDates) {
                int value = parseNumber(date);
                populateValues(value, value, 1);
            }
            return true;
        }
        return false;
    }

    private boolean parseRangeOfValues() throws InvalidCronFieldException {
        String[] range = expressionPart.split("-");
        if (range.length == 2) {
            int start = parseNumber(range[0]);
            int end = parseNumber(range[1]);
            populateValues(start, end, 1);
            return true;
        }
        return false;
    }

    private boolean parseIntervals() throws InvalidCronFieldException {
        if (expressionPart.startsWith("*")) {
            String[] intervals = expressionPart.split("/");
            int interval = 1;

            if (intervals.length > 2) {
                throw new InvalidCronFieldException("Invalid format: too many intervals in '" + expressionPart + "' for " + fieldType);
            }

            if (intervals.length == 2) {
                interval = parseNumber(intervals[1]);
            }

            populateValues(fieldType.min, fieldType.max, interval);
            return true;
        }
        return false;
    }


    private void populateValues(int start, int end, int increment) throws InvalidCronFieldException {
        if (increment == 0) {
            throw new InvalidCronFieldException("Number " + expressionPart + " for " + fieldType + " interval is 0");
        }
        if (end < start) {
            throw new InvalidCronFieldException("Number " + expressionPart + " for " + fieldType + " ends before it starts");
        }
        if (start < fieldType.min || end > fieldType.max) {
            throw new InvalidCronFieldException("Number " + expressionPart + " for " + fieldType + " is outside valid range (" + fieldType.min + "-" + fieldType.max + ")");
        }
        for (int i = start; i <= end; i += increment) {
            parsedValues.add(i);
        }
    }

    private Integer parseNumber(String no) throws InvalidCronFieldException {
        try {
            return Integer.parseInt(no);
        } catch (NumberFormatException nfe) {
            throw new InvalidCronFieldException("Invalid number '" + no + "' in field " + fieldType + ": " + nfe.getMessage());
        }
    }


}
