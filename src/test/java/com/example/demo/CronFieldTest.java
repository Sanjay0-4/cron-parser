package com.example.demo;

import com.example.demo.exception.InvalidCronFieldException;
import com.example.demo.model.CronField;
import com.example.demo.model.CronFieldType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CronFieldTest {

    /**
     * Test valid range expressions (E.g. 1-5, 0-3, etc. )
     */
    @Test
    public void testValidRangeExpressions() throws InvalidCronFieldException {
        CronField f = new CronField("1-5", CronFieldType.DAY_OF_MONTH);
        assertEquals("1 2 3 4 5", f.toString());
        f = new CronField("1-1", CronFieldType.DAY_OF_MONTH);
        assertEquals("1", f.toString());
        f = new CronField("1-2", CronFieldType.DAY_OF_MONTH);
        assertEquals("1 2", f.toString());
        f = new CronField("1-15", CronFieldType.DAY_OF_MONTH);
        assertEquals("1 2 3 4 5 6 7 8 9 10 11 12 13 14 15", f.toString());


        f = new CronField("0-1", CronFieldType.HOURS);
        assertEquals("0 1", f.toString());
        f = new CronField("0-3", CronFieldType.HOURS);
        assertEquals("0 1 2 3", f.toString());
        f = new CronField("0", CronFieldType.HOURS);
        assertEquals("0", f.toString());
        f = new CronField("23", CronFieldType.HOURS);
        assertEquals("23", f.toString());
    }

    /**
     * Test invalid range expressions (E.g. 1-100 for month, 0-3 for year, etc. )
     */
    @Test
    public void testInvalidRangeExpressions() {
        assertInvalidCronField("0-5", CronFieldType.DAY_OF_MONTH, "outside valid range");
        assertInvalidCronField("0-5-6", CronFieldType.DAY_OF_MONTH, "Invalid number");
        assertInvalidCronField("1-32", CronFieldType.DAY_OF_MONTH, "outside valid range");
        assertInvalidCronField("1-0", CronFieldType.DAY_OF_MONTH, "ends before it starts");
        assertInvalidCronField("1,24", CronFieldType.HOURS, "outside valid range");
    }

    private void assertInvalidCronField(String incomingText, CronFieldType fieldType, String msg) {
        try {
            new CronField(incomingText, fieldType);
            fail(incomingText + " should not be a valid " + fieldType);
        } catch (InvalidCronFieldException e) {
            assertTrue(e.getMessage().contains(msg));
            assertTrue(e.getMessage().contains(fieldType.toString()));
        }
    }


    /**
     * Test correct fixed value cron field ( e.g. 1,3,4 or 15,16 )
     */
    @Test
    public void testValidFixedValues() throws InvalidCronFieldException {
        CronField f = new CronField("1", CronFieldType.DAY_OF_MONTH);
        assertEquals("1", f.toString());
        f = new CronField("1,2,3,4,5", CronFieldType.DAY_OF_MONTH);
        assertEquals("1 2 3 4 5", f.toString());
        f = new CronField("1,1,1", CronFieldType.DAY_OF_MONTH);
        assertEquals("1", f.toString());
        f = new CronField("1,2", CronFieldType.DAY_OF_MONTH);
        assertEquals("1 2", f.toString());
        f = new CronField("2,1,3,5,6,7,4", CronFieldType.DAY_OF_MONTH);
        assertEquals("1 2 3 4 5 6 7", f.toString());


        f = new CronField("0,1", CronFieldType.HOURS);
        assertEquals("0 1", f.toString());
        f = new CronField("0,1,2,3", CronFieldType.HOURS);
        assertEquals("0 1 2 3", f.toString());
        f = new CronField("0", CronFieldType.HOURS);
        assertEquals("0", f.toString());
        f = new CronField("0,0", CronFieldType.HOURS);
        assertEquals("0", f.toString());
        f = new CronField("23,0", CronFieldType.HOURS);
        assertEquals("0 23", f.toString());
    }

    /**
     * Test incorrect fixed value cron field ( e.g. 1,3,100 for MONTH or 15,16 for DAY_OF_WEEK )
     */
    @Test
    public void testInvalidFixedValues() {
        assertInvalidCronField("0,5", CronFieldType.DAY_OF_MONTH, "outside valid range");
        assertInvalidCronField("1,32", CronFieldType.DAY_OF_MONTH, "outside valid range");
        assertInvalidCronField("0,8", CronFieldType.DAY_OF_MONTH, "outside valid range");
    }

    @Test
    public void testValidIntervals() throws InvalidCronFieldException {
        CronField f = new CronField("*/10", CronFieldType.DAY_OF_MONTH);
        assertEquals("1 11 21 31", f.toString());
        f = new CronField("*/20", CronFieldType.DAY_OF_MONTH);
        assertEquals("1 21", f.toString());
        f = new CronField("*/30", CronFieldType.DAY_OF_MONTH);
        assertEquals("1 31", f.toString());
        f = new CronField("*/40", CronFieldType.DAY_OF_MONTH);
        assertEquals("1", f.toString());


        f = new CronField("*/10", CronFieldType.HOURS);
        assertEquals("0 10 20", f.toString());
        f = new CronField("*/15", CronFieldType.HOURS);
        assertEquals("0 15", f.toString());
        f = new CronField("*/20", CronFieldType.HOURS);
        assertEquals("0 20", f.toString());
        f = new CronField("*/23", CronFieldType.HOURS);
        assertEquals("0 23", f.toString());
        f = new CronField("*/24", CronFieldType.HOURS);
        assertEquals("0", f.toString());

        f = new CronField("*", CronFieldType.DAY_OF_WEEK);
        assertEquals("1 2 3 4 5 6 7", f.toString());

        f = new CronField("*", CronFieldType.MONTH);
        assertEquals("1 2 3 4 5 6 7 8 9 10 11 12", f.toString());
    }

    @Test
    public void testInvalidIntervals()  {
        assertInvalidCronField("*/0", CronFieldType.DAY_OF_MONTH, "interval is 0");
        assertInvalidCronField("*/10/10", CronFieldType.DAY_OF_MONTH, "too many intervals");
        assertInvalidCronField("*/A", CronFieldType.DAY_OF_MONTH, "Invalid number 'A'");
        assertInvalidCronField("A/A", CronFieldType.DAY_OF_MONTH, "Invalid number 'A/A'");
        assertInvalidCronField("0/0", CronFieldType.DAY_OF_MONTH, "Invalid number '0/0'");
        assertInvalidCronField("0/15", CronFieldType.DAY_OF_MONTH, "Invalid number '0/15'");
    }
}
