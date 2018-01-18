package mydist.mydist.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import mydist.mydist.R;

public class AuthenticatedActivity extends AppCompatActivity
{
     CountDownTimer timer = new CountDownTimer(20 * 60 * 1000, 1000) {

        public void onTick(long millisUntilFinished) {
            //Some code
        }

        public void onFinish() {
            Context context = AuthenticatedActivity.this;
            AlertDialog mDialog  = new AlertDialog.Builder(context).
                    setMessage("Session Timeout, Please Login Again").
                    setPositiveButton(AuthenticatedActivity.this.getText(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent newIntent = new Intent(AuthenticatedActivity.this,LoginActivity.class);
                            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(newIntent);
                            //overridePendingTransition(R.anim.transition_enter, R.anim.transition_exit);
                            finish();
                        }
                    }).create();
            if(!((Activity) context).isFinishing())
            {
                mDialog.setCancelable(false);
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        timer.cancel();
        timer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }
}
