package forever.software.free.com.wastingtime;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TheTimeWeLoose extends AppCompatActivity implements View.OnClickListener {

    final private CountingTask taskOfMine = new CountingTask();
    final private Calendar timeKeeper = Calendar.getInstance();
    final private Calendar realTime = Calendar.getInstance();


    final private SimpleDateFormat timeFormatter = new SimpleDateFormat("HH'h'mm'm'ss's'", Locale.US);
    final private SimpleDateFormat hrsFormatter = new SimpleDateFormat("HH'h'", Locale.US);
    final private SimpleDateFormat minFormatter = new SimpleDateFormat("mm'm'", Locale.US);
    final private SimpleDateFormat secFormatter = new SimpleDateFormat("ss's'", Locale.US);

    private TextView worldTime;
    private TextView hrs;
    private TextView min;
    private TextView sec;
    private TextView freeloaders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        freeloaders = (TextView) findViewById(R.id.freeloaders);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_time_we_loose);


        worldTime = (TextView) findViewById(R.id.one_time);
        hrs = (TextView) findViewById(R.id.hrs_you_loose);
        min = (TextView) findViewById(R.id.min_you_loose);
        sec = (TextView) findViewById(R.id.sec_you_loose);

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
        //resetTime();
        taskOfMine.cancel(true);
    }

    @Override
    public void onClick(View v) {
        TextView freeloaders = (TextView) findViewById(R.id.freeloaders);
        //TODO: this ID driven update logic has to be replaced with gesture control.
       /*
         switch (v.getId()){
            case R.id.freeload_reduce:{
                int count = Integer.parseInt(""+freeloaders.getText());
                if(count>1){
                    --count;
                    freeloaders.setText(""+count);
                }
            } break;
            case R.id.freeload_more:{
                int count = Integer.parseInt(""+freeloaders.getText());
                count++;
                freeloaders.setText(""+count);
            } break;
        }*/
    }

    public class CountingTask extends AsyncTask {

        final private long REFRESH_FREQ = 1000; /* ms */

        @Override
        protected Object doInBackground(Object[] params) {

            while(!this.isCancelled()){
                int mul = getIntFromTextView((TextView) findViewById(R.id.freeloaders));
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
            worldTime.setText(timeFormatter.format(realTime.getTime()));
            hrs.setText(hrsFormatter.format(timeKeeper.getTime()));
            min.setText(minFormatter.format(timeKeeper.getTime()));
            sec.setText(secFormatter.format(timeKeeper.getTime()));
        }

        private Integer getIntFromTextView(TextView v){
            CharSequence pureText = v.getText();
            pureText = pureText.toString().replace(":","");
            return Integer.parseInt(""+pureText);
        }

    }
}
