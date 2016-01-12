package co.jlabs.cersei_retailer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.os.Handler;

public class Splash extends Activity {
    Activity context;
    public static int splash_time = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        context=this;
        final Intent myIntent = new Intent(this, MainDashboard.class);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                startActivity(myIntent);
                context.finish();
            }
        }, splash_time);
    }


}
