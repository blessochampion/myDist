package mydist.mydist.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import mydist.mydist.R;
import mydist.mydist.data.UserPreference;
import mydist.mydist.utils.FontManager;
import mydist.mydist.utils.UIUtils;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout mNewRetailer;
    LinearLayout mSynchronization;
    LinearLayout mReports;
    LinearLayout mCoverage;
    TextView mWelcomeMessage;
    private boolean logoutRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getReferencesToViews();
        setOnClickListeners();
        setFonts();

    }

    private void setFonts() {
        Typeface ralewayFont = FontManager.getTypeface(getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(findViewById(R.id.parent_layout), ralewayFont );

        //set icons
        setIcons();

    }

    private void setIcons() {
        Typeface fontAwesome = FontManager.getTypeface(getApplicationContext(), FontManager.FONT_AWESOME);
        FontManager.setFontsForView(findViewById(R.id.icon_coverage), fontAwesome );
        FontManager.setFontsForView(findViewById(R.id.icon_reports), fontAwesome );
        FontManager.setFontsForView(findViewById(R.id.icon_synchronization), fontAwesome );
        FontManager.setFontsForView(findViewById(R.id.icon_new_retailer), fontAwesome );
    }

    private void setOnClickListeners() {
        mNewRetailer.setOnClickListener(this);
        mSynchronization.setOnClickListener(this);
        mReports.setOnClickListener(this);
        mCoverage.setOnClickListener(this);
    }

    public void getReferencesToViews() {
        setupToolBar();
        mNewRetailer = (LinearLayout) findViewById(R.id.ll_new_retailer);
        mSynchronization = (LinearLayout) findViewById(R.id.ll_synchronization);
        mReports = (LinearLayout) findViewById(R.id.ll_reports);
        mCoverage = (LinearLayout) findViewById(R.id.ll_coverage);
        mWelcomeMessage = (TextView) findViewById(R.id.tv_welcome_message);
        mWelcomeMessage.setText(getString(R.string.home_welcome_message, UserPreference.getInstance(this).getUsername()));

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
        if (v.getId() == R.id.ll_new_retailer) {
            Intent intent = new Intent(HomeActivity.this, NewRetailerActivity.class);
            startActivity(intent);
            return;
        }
        if (v.getId() == R.id.ll_synchronization) {
            Intent intent = new Intent(HomeActivity.this, SynchronizationActivity.class);
            startActivity(intent);
            return;
        }
        if (v.getId() == R.id.ll_reports) {
            Intent intent = new Intent(HomeActivity.this, ReportsActivity.class);
            startActivity(intent);
            return;
        }
        if (v.getId() == R.id.ll_coverage) {
            Intent intent = new Intent(HomeActivity.this, CoverageActivity.class);
            startActivity(intent);
            return;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home_settings:
                launchSettingsActivity();
                return true;
            case R.id.menu_home_logout:
                showLogoutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void launchSettingsActivity() {
        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void showLogoutDialog() {
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
        if (logoutRequested) {
            super.onBackPressed();
        } else {
            this.moveTaskToBack(true);
        }

    }
}
