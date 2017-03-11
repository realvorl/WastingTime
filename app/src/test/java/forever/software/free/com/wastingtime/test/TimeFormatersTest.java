package forever.software.free.com.wastingtime.test;

import org.junit.Test;

import java.util.Date;

import forever.software.free.com.wastingtime.TimeFormaters;

import static org.junit.Assert.assertTrue;


public class TimeFormatersTest {

    TimeFormaters classUnderTest = new TimeFormaters();

    @Test
    public void testFormatOne() {
        Date testDate = new Date(123123123123123L);
        String formatedDate = classUnderTest.hrsFormatter.format(testDate);
        assertTrue(formatedDate.equals(""));
    }
}