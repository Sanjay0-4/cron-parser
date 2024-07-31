package com.example.demo.model;

import com.example.demo.exception.InvalidCronFieldException;

import static com.example.demo.util.AppUtil.formatCronField;

public class CronExpression {

    private final CronField minutes;
    private final CronField hours;
    private final CronField dayOfMonth;
    private final CronField month;
    private final CronField dayOfWeek;
    private final String command;

    public CronExpression(String expression) throws InvalidCronFieldException {
        String[] cronMembers = expression.split("\\s+");
        if (cronMembers.length != 6) {
            throw new InvalidCronFieldException("Expected [minute] [hour] [day of month] [day of week] [command] but got :" + expression);
        }

        this.minutes = new CronField(cronMembers[0], CronFieldType.MINUTES);
        this.hours = new CronField(cronMembers[1], CronFieldType.HOURS);
        this.dayOfMonth = new CronField(cronMembers[2], CronFieldType.DAY_OF_MONTH);
        this.month = new CronField(cronMembers[3], CronFieldType.MONTH);
        this.dayOfWeek = new CronField(cronMembers[4], CronFieldType.DAY_OF_WEEK);
        this.command = cronMembers[5];
    }

    public String toString() {
        return formatCronField("minute", minutes) +
                formatCronField("hour", hours) +
                formatCronField("day of month", dayOfMonth) +
                formatCronField("month", month) +
                formatCronField("day of week", dayOfWeek) +
                formatCronField("command", command);
    }

}
