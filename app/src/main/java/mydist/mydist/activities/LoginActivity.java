package mydist.mydist.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import mydist.mydist.utils.FontManager;
import mydist.mydist.utils.UIUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AuthenticationListener, DownloadMastersListener {

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
        getReferencesToViews();
    }

    private void getReferencesToViews() {
        mUsername = (EditText) findViewById(R.id.login_activity_username);
        mPassword = (EditText) findViewById(R.id.login_activity_password);
        mDownload = (Button) findViewById(R.id.login_activity_download);
        mLogin = (Button) findViewById(R.id.login_activity_login);
        setButtonOnClickListeners();
        setFont();
        mLoadingIndicator = new ProgressDialog(this);
        mLoadingIndicator.setCanceledOnTouchOutside(false);

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
        if (userHasClosedSalesToday()) {
            launchDialog(getString(R.string.login_user_closed_for_the_day));
            return;
        }

        if (!NetworkUtils.isNetworkAvailable(this)) {
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
        return UserPreference.getInstance(this).isUserClosedForTheDay();
    }

    private void doDownload() {
        if (userInputIsValid()) {
            mLoadingIndicator.setMessage(getString(R.string.login_activity_download));
            mLoadingIndicator.setTitle(getString(R.string.login_dialog_title, mUsername.getText().toString().trim()));
            mLoadingIndicator.show();
            makeNetworkCallForDownload();
        }
    }


    private void makeNetworkCallForDownload() {
        DownloadMastersClient client = new DownloadMastersClient();
        client.downloadMasters(usernameValue, passwordValue, this);
    }

    private void doLogin() {
        if (userInputIsValid()) {
            makeNetworkCallForLogin();
        }
    }

    private void makeNetworkCallForLogin() {
        AuthenticationClient client = new AuthenticationClient();
        client.authenticate(usernameValue, passwordValue, this);
    }

    private void loginFailed() {
        mDialog = new AlertDialog.Builder(LoginActivity.this).
                setMessage(LoginActivity.this.getString(R.string.login_error_message)).
                setPositiveButton(LoginActivity.this.getText(R.string.ok), null).create();
        mDialog.show();
    }


    private void launchHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
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
        mLoadingIndicator.setMessage(getString(R.string.login_activity_download));
        mLoadingIndicator.setTitle(getString(R.string.login_dialog_title, mUsername.getText().toString().trim()));
        mLoadingIndicator.show();

    }

    @Override
    public void onSuccess(DownloadMastersResponse response) {
        DataUtils.saveUser(response.getUser(), UserPreference.getInstance(this));
        DataUtils.saveMasters(response, this);
        dismissDialog();
        launchHomeActivity();
    }

    @Override
    public void onFailure() {
        dismissDialog();
        loginFailed();
    }

    private void dismissDialog() {
        if (mLoadingIndicator != null) {
            mLoadingIndicator.dismiss();
        }
    }
}
