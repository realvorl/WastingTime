package forever.software.free.com.wastingtime;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TheTimeWeLoose extends AppCompatActivity implements View.OnClickListener {

    TaskSmecher tSmech = new TaskSmecher();
    private Calendar timeKeeper = Calendar.getInstance();
    private Calendar realTime = Calendar.getInstance();


    private SimpleDateFormat timeFormater = new SimpleDateFormat("HH'h'mm'm'ss's'");
    private SimpleDateFormat hrsFormater = new SimpleDateFormat("HH'h'");
    private SimpleDateFormat minFormater = new SimpleDateFormat("mm'm'");
    private SimpleDateFormat secFormater = new SimpleDateFormat("ss's'");

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        worldTime = (TextView) findViewById(R.id.one_time);
        hrs = (TextView) findViewById(R.id.hrs_you_loose);
        min = (TextView) findViewById(R.id.min_you_loose);
        sec = (TextView) findViewById(R.id.sec_you_loose);

        resetTime();
        tSmech.execute(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_the_time_we_loose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        tSmech.cancel(true);
    }

    @Override
    public void onClick(View v) {
        TextView freeloaders = (TextView) findViewById(R.id.freeloaders);
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
        }
    }

    public class TaskSmecher extends AsyncTask {
        private final Activity act = getParent();

        private long REFRESHFREQ = 1000; /* ms */

        @Override
        protected Object doInBackground(Object[] params) {

            while(!this.isCancelled()){
                int mul = getIntFromTextView((TextView) findViewById(R.id.freeloaders));
                try {
                    long current = timeKeeper.getTime().getTime();
                    timeKeeper.setTimeInMillis(current + (REFRESHFREQ * mul));

                    long realCurrent = realTime.getTime().getTime();
                    realTime.setTimeInMillis(realCurrent + REFRESHFREQ);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateText();
                        }
                    });
                    Thread.sleep(REFRESHFREQ);
                }catch (Exception e){
                }
            }
            return true;
        }

        private void updateText(){
            worldTime.setText(timeFormater.format(realTime.getTime()));
            hrs.setText(hrsFormater.format(timeKeeper.getTime()));
            min.setText(minFormater.format(timeKeeper.getTime()));
            sec.setText(secFormater.format(timeKeeper.getTime()));
        }

        private Integer getIntFromTextView(TextView v){
            CharSequence pureText = v.getText();
            pureText = pureText.toString().replace(":","");
            return Integer.parseInt(""+pureText);
        }

    }
}
