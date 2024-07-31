package com.example.demo;

import com.example.demo.exception.InvalidCronFieldException;
import com.example.demo.model.CronExpression;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

import static org.apache.logging.log4j.util.Strings.isBlank;


@SpringBootApplication
public class CronParser {


    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Expected [minute] [hour] [day of month] [day of week] [command] but got :" + Arrays.toString(args));
            return;
        }
        try {
            CronExpression expr = new CronExpression(args[0]);
            System.out.println(expr);

        } catch (InvalidCronFieldException invalidCronExpression) {
            System.err.println(invalidCronExpression.getMessage());
        }
    }

}


