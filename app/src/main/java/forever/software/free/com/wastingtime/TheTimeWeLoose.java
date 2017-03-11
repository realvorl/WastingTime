package forever.software.free.com.wastingtime;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

public class TheTimeWeLoose extends AppCompatActivity implements View.OnClickListener {

    private final TimeFormaters timeFormaters = new TimeFormaters();

    private Calendar meetingTime = Calendar.getInstance();
    private Calendar realTime = Calendar.getInstance();

    private TextView worldTime;
    private TextView hrs;
    private TextView min;
    private TextView sec;
    private int mul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_time_we_loose);


        worldTime = (TextView) findViewById(R.id.one_time);
        hrs = (TextView) findViewById(R.id.hrs_you_loose);
        min = (TextView) findViewById(R.id.min_you_loose);
        sec = (TextView) findViewById(R.id.sec_you_loose);

        findViewById(R.id.addFreelaoders).setOnClickListener(this);
        findViewById(R.id.remFreelaoders).setOnClickListener(this);
        findViewById(R.id.freeloaders).setOnClickListener(this);

        resetTime();
        restorePreferences();
        CountingTask countingTask = new CountingTask();

        try {
            countingTask.execute(this);
        } catch (Exception e) {
            Log.d("onCreate:caught: ", e.getLocalizedMessage());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_the_time_we_loose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return (item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item));
    }

    private void resetTime() {
        meetingTime.set(Calendar.HOUR_OF_DAY, 0);
        meetingTime.set(Calendar.MINUTE, 0);
        meetingTime.set(Calendar.SECOND, 0);

        realTime.set(Calendar.HOUR_OF_DAY, 0);
        realTime.set(Calendar.MINUTE, 0);
        realTime.set(Calendar.SECOND, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savePreferences();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restorePreferences();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        savePreferences();
    }

    private void savePreferences() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("realTimeLast", realTime.getTimeInMillis());
        editor.putLong("timeKeeperLast", meetingTime.getTimeInMillis());
        editor.putInt("freeloaders", mul);
        editor.apply();   // I missed to save the data to preference here,.
    }

    private void restorePreferences() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        long realTimeLast = sharedPreferences.getLong("realTimeLast", 0);
        if (realTimeLast != 0) realTime.setTimeInMillis(realTimeLast);
        long timeKeeperLast = sharedPreferences.getLong("timeKeeperLast", 0);
        if (timeKeeperLast != 0) meetingTime.setTimeInMillis(timeKeeperLast);
        mul = sharedPreferences.getInt("freeloaders", 1);
    }

    @Override
    public void onClick(View v) {
        //TODO: this ID driven update logic has to be replaced with gesture control.
        final EditText freeloaders = (EditText) findViewById(R.id.freeloaders);
        switch (v.getId()) {
            case R.id.remFreelaoders:
                updateFreeloadersBy(freeloaders, -1);
                break;
            case R.id.addFreelaoders:
                updateFreeloadersBy(freeloaders, 1);
                break;
        }
    }

    private void updateFreeloadersBy(EditText freeloaders, int offset) {
        try {
            int currentValue = parseInt("" + freeloaders.getText());
            int updateValue = currentValue + offset;
            if ((updateValue) > 0) {
                freeloaders.setText(valueOf(updateValue));
            } else {
                throw new NumberFormatException("Unacceptable action for current value.");
            }
        } catch (Exception e) {
            freeloaders.setText("1");
        }
    }

    private class CountingTask extends AsyncTask {

        final private long REFRESH_FREQ = 1_000; /* ms */

        @Override
        protected Object doInBackground(Object[] params) {

            while (!this.isCancelled()) {
                try {
                    mul = getIntFromTextView((EditText) findViewById(R.id.freeloaders));
                } catch (Exception nPE) {
                    mul = 1;
                }
                try {
                    long current = meetingTime.getTime().getTime();
                    meetingTime.setTimeInMillis(current + (REFRESH_FREQ * mul));
                    long realCurrent = realTime.getTime().getTime();
                    realTime.setTimeInMillis(realCurrent + REFRESH_FREQ);
                    runOnUiThread(this::updateText);
                    Thread.sleep(REFRESH_FREQ);
                } catch (Exception e) {
                    Log.d("Was unable to: ", e.getLocalizedMessage());
                }
            }
            return true;
        }

        private void updateText() {
            worldTime.setText(timeFormaters.timeFormatter.format(realTime.getTime()));
            hrs.setText(timeFormaters.hrsFormatter.format(meetingTime.getTime()));
            min.setText(timeFormaters.minFormatter.format(meetingTime.getTime()));
            sec.setText(timeFormaters.secFormatter.format(meetingTime.getTime()));
        }

        private Integer getIntFromTextView(TextView v) {
            CharSequence pureText = v.getText();
            pureText = pureText.toString().trim().replaceAll("\\D", "");
            if (pureText != null && pureText.length() > 0)
                return parseInt(pureText.toString());
            return mul;
        }

    }
}
