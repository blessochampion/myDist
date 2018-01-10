package mydist.mydist.activities;

import android.content.DialogInterface;
import android.graphics.Typeface;
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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import mydist.mydist.R;
import mydist.mydist.data.UserPreference;
import mydist.mydist.utils.FontManager;
import mydist.mydist.utils.UIUtils;

public class SynchronizationActivity extends AuthenticatedActivity implements View.OnClickListener {

    Button mStartSyncButton;
    EditText mUsername;
    EditText mPassword;
    CheckBox mCloseForTheDay;
    TextView mUsernameLabel;
    TextView mPasswordLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronization);
        getReferencesToViews();
        setOnClickListeners();
        setFonts();
    }

    private void setFonts() {
        Typeface ralewayFont = FontManager.getTypeface(getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(findViewById(R.id.parent_layout), ralewayFont);
    }

    private void setOnClickListeners() {
        mStartSyncButton.setOnClickListener(this);

    }

    public void getReferencesToViews() {

        setupToolBar();
        mStartSyncButton = (Button) findViewById(R.id.bt_start_sync);
        mCloseForTheDay = (CheckBox) findViewById(R.id.cb_close_for_the_day);
        mUsername = (EditText) findViewById(R.id.et_username);
        mUsername.setText(getUsername());
        mPassword = (EditText) findViewById(R.id.password);
        mUsernameLabel = (TextView) findViewById(R.id.username_label);
        mPasswordLabel = (TextView) findViewById(R.id.password_label);
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
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        UIUtils.hideKeyboard(this);
        if (v.getId() == R.id.bt_start_sync) {
            if (mCloseForTheDay.isChecked()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String todayDate = dateFormat.format(new Date());
                UserPreference.getInstance(this).setUserCloseForTheDayDate(todayDate);
                AlertDialog dialog = new AlertDialog.Builder(SynchronizationActivity.this).
                        setMessage(this.getString(R.string.closing_message, todayDate)).
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

    public String getUsername() {
        return UserPreference.getInstance(this).getUsername();
    }
}
