package forever.software.free.com.wastingtime.test;

import org.junit.Test;

import java.util.Date;

import forever.software.free.com.wastingtime.TimeFormaters;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class TimeFormatersTest {

    TimeFormaters classUnderTest = new TimeFormaters();

    @Test
    public void testFormatTime() {
        Date testDate = new Date(123123123123123L);
        String formatedDate = classUnderTest.timeFormatter.format(testDate);
        assertThat(formatedDate, is("05h32m03s"));
    }

    @Test
    public void testFormatHours(){
        Date testDate = new Date(123123123123123L);
        String formatedDate = classUnderTest.hrsFormatter.format(testDate);
        assertThat(formatedDate, is("05h"));
    }

    @Test
    public void testFormatMinute() {
        Date testDate = new Date(123123123123123L);
        String formatedDate = classUnderTest.minFormatter.format(testDate);
        assertThat(formatedDate, is("32m"));
    }

    @Test
    public void testFormatSeconds() {
        Date testDate = new Date(123123123123123L);
        String formatedDate = classUnderTest.secFormatter.format(testDate);
        assertThat(formatedDate, is("03s"));
    }
}