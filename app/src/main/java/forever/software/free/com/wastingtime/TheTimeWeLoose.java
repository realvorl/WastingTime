package forever.software.free.com.wastingtime;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;

public class TheTimeWeLoose extends AppCompatActivity implements View.OnClickListener {

    final private CountingTask taskOfMine = new CountingTask();
    final private Calendar timeKeeper = Calendar.getInstance();
    final private Calendar realTime = Calendar.getInstance();


    private final TimeFormaters timeFormaters = new TimeFormaters();

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
        try {
            taskOfMine.execute(this);
        } catch (Exception e) {
            Log.d("onCreate:caught", e.getLocalizedMessage());
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

    private void resetTime(){
        timeKeeper.set(Calendar.HOUR_OF_DAY,0);
        timeKeeper.set(Calendar.MINUTE,0);
        timeKeeper.set(Calendar.SECOND,0);

        realTime.set(Calendar.HOUR_OF_DAY,0);
        realTime.set(Calendar.MINUTE,0);
        realTime.set(Calendar.SECOND,0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        //TODO: this ID driven update logic has to be replaced with gesture control.
        final EditText freeloaders = (EditText) findViewById(R.id.freeloaders);
        switch (v.getId()) {
            case R.id.remFreelaoders: {
                int count = Integer.parseInt("" + freeloaders.getText());
                if (count > 1) {
                    --count;
                    freeloaders.setText("" + count);
                }
            }
            break;
            case R.id.addFreelaoders: {
                int count = Integer.parseInt("" + freeloaders.getText());
                count++;
                freeloaders.setText("" + count);
            }
            break;
        }
    }
    public class CountingTask extends AsyncTask {

        final private long REFRESH_FREQ = 1000; /* ms */

        @Override
        protected Object doInBackground(Object[] params) {

            while(!this.isCancelled()){
                Integer newMul = getIntFromTextView((EditText) findViewById(R.id.freeloaders));
                if (newMul == null ) mul = 1;
                else mul = newMul;

                try {
                    long current = timeKeeper.getTime().getTime();
                    timeKeeper.setTimeInMillis(current + (REFRESH_FREQ * mul));

                    long realCurrent = realTime.getTime().getTime();
                    realTime.setTimeInMillis(realCurrent + REFRESH_FREQ);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateText();
                        }
                    });
                    Thread.sleep(REFRESH_FREQ);
                }catch (Exception e){
                    Log.d("Was unable to: ", e.getLocalizedMessage());
                }
            }
            return true;
        }

        private void updateText(){
            worldTime.setText(timeFormaters.timeFormatter.format(realTime.getTime()));
            hrs.setText(timeFormaters.hrsFormatter.format(timeKeeper.getTime()));
            min.setText(timeFormaters.minFormatter.format(timeKeeper.getTime()));
            sec.setText(timeFormaters.secFormatter.format(timeKeeper.getTime()));
        }

        private Integer getIntFromTextView(TextView v){
            CharSequence pureText = v.getText();
            pureText = pureText.toString().trim().replaceAll("\\D","");
            if (pureText!=null && pureText.length() > 0) return Integer.parseInt(pureText.toString());
            return mul;
        }

    }
}
