package forever.software.free.com.wastingtime;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimeFormaters {
    final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH'h'mm'm'ss's'", Locale.US);
    final SimpleDateFormat hrsFormatter = new SimpleDateFormat("HH'h'", Locale.US);
    final SimpleDateFormat minFormatter = new SimpleDateFormat("mm'm'", Locale.US);
    final SimpleDateFormat secFormatter = new SimpleDateFormat("ss's'", Locale.US);

    public TimeFormaters() {
    }
}