package mydist.mydist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import mydist.mydist.R;

public class SplashActivity extends AppCompatActivity {

    private static final int DELAYED_DURATION = 2600;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestFullScreen();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startTimer();
    }

    private void startTimer()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                launchLoginActivity();
            }
        }, DELAYED_DURATION);
    }

    private void launchLoginActivity(){
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void requestFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
