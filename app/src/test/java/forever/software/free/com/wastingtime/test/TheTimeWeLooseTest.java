package forever.software.free.com.wastingtime.test;

import android.app.Application;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import forever.software.free.com.wastingtime.R;
import forever.software.free.com.wastingtime.TheTimeWeLoose;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by veo on 3/11/17.
 */
public class TheTimeWeLooseTest {


    TheTimeWeLoose classUnderTest = new TheTimeWeLoose();


    @Test
    public void onCreate() throws Exception {

    }

    @Test
    public void onCreateOptionsMenu() throws Exception {

    }

    @Test
    public void onOptionsItemSelected() throws Exception {

    }

    @Test
    public void onSaveInstanceState() throws Exception {

    }

    @Test
    public void onRestoreInstanceState() throws Exception {

    }

    @Test
    public void onBackPressed() throws Exception {

    }

    @Test
    public void onClick() throws Exception {
        View testView = Mockito.mock(View.class);
        when(testView.getId()).thenReturn(R.id.remFreelaoders);
        classUnderTest.onClick(testView);


    }

}