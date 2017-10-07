package mydist.mydist.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mydist.mydist.R;
import mydist.mydist.utils.UIUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mUsername;
    EditText mPassword;
    Button mDownload;
    Button mLogin;
    ProgressDialog  mLoadingIndicator;
    static final int INDICATOR_DELAY = 30000;
    static final String TEST_USERNAME = "Blessing";
    static final String TEST_PASSWORD = "password";

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

        mLoadingIndicator = new ProgressDialog(this);
    }

    private void setButtonOnClickListeners()
    {
        mDownload.setOnClickListener(this);
        mLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.login_activity_download){
            doDownload();
        }

        if(v.getId() == R.id.login_activity_login){
            doLogin();
        }
    }

    private void doDownload()
    {
        UIUtils.hideKeyboard(this);
        if(userInputIsValid()){
            mLoadingIndicator.setMessage(getString(R.string.login_activity_download));
            mLoadingIndicator.setTitle(getString(R.string.login_dialog_title, mUsername.getText().toString().trim()));
           mLoadingIndicator.show();
            cancelDialog();
        }

    }

    private void doLogin()
    {
        UIUtils.hideKeyboard(this);
        if(userInputIsValid()){
            mLoadingIndicator.setMessage(getString(R.string.login_activity_login));
            mLoadingIndicator.setTitle(getString(R.string.login_dialog_title, mUsername.getText().toString().trim()));
            mLoadingIndicator.show();
            cancelDialog();
        }
    }

    private void cancelDialog()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadingIndicator.dismiss();
                if(mUsername.getText().toString().toString().equals(TEST_USERNAME) &&
                        mPassword.getText().toString().trim().equals(TEST_PASSWORD)){
                    launchHomeActivity();
                }else {
                    AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this).
                            setMessage(LoginActivity.this.getString(R.string.login_error_message)).
                            setPositiveButton(LoginActivity.this.getText(R.string.ok), null).create();
                    dialog.show();
                }
            }
        }, INDICATOR_DELAY);
    }

    private void launchHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private boolean userInputIsValid()
    {
        String username = mUsername.getText().toString().trim();
        if(username.isEmpty()){
            mUsername.setError(getString(R.string.input_empty_error));
            return false;
        }

        String password = mPassword.getText().toString().trim();
        if(password.isEmpty()){
            mPassword.setError(getString(R.string.input_empty_error));
            return false;
        }

        return  true;
    }
}
