package mydist.mydist.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import mydist.mydist.R;
import mydist.mydist.data.UserPreference;
import mydist.mydist.listeners.AuthenticationListener;
import mydist.mydist.listeners.DownloadMastersListener;
import mydist.mydist.models.AuthenticationResponse;
import mydist.mydist.models.DownloadMastersResponse;
import mydist.mydist.network.AuthenticationClient;
import mydist.mydist.network.DownloadMastersClient;
import mydist.mydist.network.NetworkUtils;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.Days;
import mydist.mydist.utils.FontManager;
import mydist.mydist.utils.UIUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AuthenticationListener, DownloadMastersListener {

    public static final String EMPTY_STRING = "";
    UserPreference userPreference;
    EditText mUsername;
    EditText mPassword;
    Button mDownload;
    Button mLogin;
    AlertDialog mDialog;
    String usernameValue;
    String passwordValue;
    ProgressDialog mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userPreference = UserPreference.getInstance(this);
        getReferencesToViews();
    }

    private void getReferencesToViews() {
        mUsername = (EditText) findViewById(R.id.login_activity_username);
        mPassword = (EditText) findViewById(R.id.login_activity_password);
        mDownload = (Button) findViewById(R.id.login_activity_download);
        mLogin = (Button) findViewById(R.id.login_activity_login);
        setButtonOnClickListeners();
        setFont();
    }

    private void setFont() {
        Typeface ralewayFont = FontManager.getTypeface(getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(findViewById(R.id.parent_layout), ralewayFont);
    }

    private void setButtonOnClickListeners() {
        mDownload.setOnClickListener(this);
        mLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        UIUtils.hideKeyboard(this);
        if (userHasClosedSalesToday() && v.getId() == R.id.login_activity_download) {
            launchDialog(getString(R.string.login_user_closed_for_the_day));
            return;
        }
        if (userHasClosedSalesToday() && v.getId() == R.id.login_activity_login) {
            doLogin();
            return;
        }
        if (!NetworkUtils.isNetworkAvailable(this) && !(v.getId() == R.id.login_activity_login)) {
            launchDialog(getString(R.string.network_error));
        } else if (v.getId() == R.id.login_activity_download) {
            doDownload();
        } else if (v.getId() == R.id.login_activity_login) {
            doLogin();
        }
    }

    private void launchDialog(String message) {
        mDialog = new AlertDialog.Builder(LoginActivity.this).
                setMessage(message).
                setPositiveButton(LoginActivity.this.getText(R.string.ok), null).create();
        mDialog.show();
    }

    private boolean userHasClosedSalesToday() {
        String todayDate = getTodayDate();
        return todayDate.equalsIgnoreCase(userPreference.getLastUserClosedForTheDayDate());
    }

    private void doDownload() {
        if (!userHasClosedSalesToday()
                || userPreference.getLastUserClosedForTheDayDate().isEmpty()) {
            if (!mastersDownloadedToday()) {
                if (userInputIsValid()) {
                    makeNetworkCallForDownload();
                }
            } else {
                launchDialog(getString(R.string.masters_info));
            }
        } else {
            launchDialog(getString(R.string.signed_out_for_the_day));
        }
    }


    private boolean mastersDownloadedToday() {
        return userPreference.getLastMastersDownloadDate().equalsIgnoreCase(getTodayDate());
    }

    private void makeNetworkCallForDownload() {
        DownloadMastersClient client = new DownloadMastersClient();
        client.downloadMasters(usernameValue, passwordValue, this);
    }

    private void doLogin() {
        if (mastersDownloadedToday() || !Days.userHasUploadedPreviousDayCoverage(userPreference)) {
            if (userInputIsValid()) {
                String userBasicAuth = userPreference.getPassword();
                if (userBasicAuth.equalsIgnoreCase(usernameValue + ":" + passwordValue)) {
                    launchHomeActivity();
                } else {
                    loginFailed(getString(R.string.login_error_message));
                }
            }
        } else {
            launchDialog(getString(R.string.login_masters_info));
        }
    }

    private void makeNetworkCallForLogin() {
        AuthenticationClient client = new AuthenticationClient();
        client.authenticate(usernameValue, passwordValue, this);
    }

    private void loginFailed(String message) {
        mDialog = new AlertDialog.Builder(LoginActivity.this).
                setMessage(message).
                setPositiveButton(LoginActivity.this.getString(R.string.ok), null).create();
        mDialog.show();
    }

    private void launchHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissDialog();
    }

    private boolean userInputIsValid() {
        usernameValue = mUsername.getText().toString().trim();
        if (usernameValue.isEmpty()) {
            mUsername.setError(getString(R.string.input_empty_error));
            return false;
        }
        passwordValue = mPassword.getText().toString().trim();
        if (passwordValue.isEmpty()) {
            mPassword.setError(getString(R.string.input_empty_error));
            return false;
        }
        return true;
    }

    @Override
    public void onStartAuthentication() {
        mLoadingIndicator.setMessage(getString(R.string.login_activity_login));
        mLoadingIndicator.setTitle(getString(R.string.login_dialog_title, mUsername.getText().toString().trim()));
        mLoadingIndicator.show();
    }

    @Override
    public void onSuccess(AuthenticationResponse response) {
        DataUtils.saveUser(response.getUser(), UserPreference.getInstance(this));
        dismissDialog();
        launchHomeActivity();
    }

    @Override
    public void onStartDownload() {
                mLoadingIndicator = new ProgressDialog(LoginActivity.this);
                mLoadingIndicator.setCanceledOnTouchOutside(false);
                mLoadingIndicator.setMessage(getString(R.string.login_activity_download));
                mLoadingIndicator.setTitle(getString(R.string.login_dialog_title, mUsername.getText().toString().trim()));
                mLoadingIndicator.setCancelable(false);
                mLoadingIndicator.setCanceledOnTouchOutside(false);
                mLoadingIndicator.show();
    }

    @Override
    public void onSuccess(DownloadMastersResponse response) {
        new SaveMastersTask().execute(response);
    }

    @Override
    public void onFailure() {
        dismissDialog();
        loginFailed(getString(R.string.error_message_check_network));
    }

    private void dismissDialog() {
        if (mLoadingIndicator != null) {
            mLoadingIndicator.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public String getTodayDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String todayDate = dateFormat.format(new Date());
        return todayDate;
    }

    private class SaveMastersTask extends AsyncTask<DownloadMastersResponse, Void, Void> {
        @Override
        protected Void doInBackground(DownloadMastersResponse... downloadMastersResponses) {
            DownloadMastersResponse response = downloadMastersResponses[0];
            Context context = LoginActivity.this;
            userPreference.setUserCloseForTheDayDate(EMPTY_STRING);
            DataUtils.saveUser(response.getUser(), UserPreference.getInstance(context));
            DataUtils.saveNewRetailers(response.getMaster().getRetailers(), context);
            DataUtils.saveMasters(response, context);
            userPreference.savePassword(usernameValue + ":" + passwordValue);
            userPreference.setLasMastersDownloadDate(getTodayDate());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            launchHomeActivity();
        }
    }
}
