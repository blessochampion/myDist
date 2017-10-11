package mydist.mydist.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import mydist.mydist.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout mNewRetailer;
    LinearLayout mSynchronization;
    private boolean logoutRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getReferencesToViews();
        setOnClickListeners();
    }

    private void setOnClickListeners()
    {
        mNewRetailer.setOnClickListener(this);
        mSynchronization.setOnClickListener(this);
    }

    public void getReferencesToViews() {
        setupToolBar();
        mNewRetailer = (LinearLayout) findViewById(R.id.ll_new_retailer);
        mSynchronization = (LinearLayout) findViewById(R.id.ll_synchronization);

    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = getString(R.string.home);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ll_new_retailer){
            Intent intent = new Intent(HomeActivity.this, NewRetailerActivity.class);
            startActivity(intent);
            return;
        }
        if(v.getId() == R.id.ll_synchronization){
            Intent intent = new Intent(HomeActivity.this, SynchronizationActivity.class);
            startActivity(intent);
            return;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_home_settings:
                return super.onOptionsItemSelected(item);
            case R.id.menu_home_logout :
                showLogoutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void showLogoutDialog()
    {
        AlertDialog dialog = new AlertDialog.Builder(HomeActivity.this).
                setMessage(this.getString(R.string.logout_error_dialog_message)).
                setNegativeButton(this.getString(R.string.cancel), null).
                setPositiveButton(this.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logoutRequested = true;
                        onBackPressed();
                    }
                }).create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if(logoutRequested){
            super.onBackPressed();
        }else {
            this.moveTaskToBack(true);
        }

    }
}
