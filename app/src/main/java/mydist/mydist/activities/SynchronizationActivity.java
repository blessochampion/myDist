package mydist.mydist.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import mydist.mydist.R;
import mydist.mydist.utils.UIUtils;

public class SynchronizationActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DEFAULT_USERNAME = "Blessing";
    Button mStartSyncButton;
    EditText mUsername;
    CheckBox mCloseForTheDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronization);
        getReferencesToViews();
        setOnClickListeners();
    }

    private void setOnClickListeners()
    {
        mStartSyncButton.setOnClickListener(this);

    }

    public void getReferencesToViews() {

        setupToolBar();
        mStartSyncButton = (Button) findViewById(R.id.bt_start_sync);
        mCloseForTheDay = (CheckBox) findViewById(R.id.cb_close_for_the_day);
        mUsername = (EditText) findViewById(R.id.et_username);
        mUsername.setText(DEFAULT_USERNAME);
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = getString(R.string.synchronization);
        getSupportActionBar().setTitle(title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_synchronization, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
         return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        UIUtils.hideKeyboard(this);
        if(v.getId() == R.id.bt_start_sync){
            if(mCloseForTheDay.isChecked()){
                AlertDialog dialog = new AlertDialog.Builder(SynchronizationActivity.this).
                        setMessage(this.getString(R.string.closing_message)).
                        setPositiveButton(this.getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        }).create();
                dialog.show();
            }
        }
    }
}
