package me.edgeconsult.authentication;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    private static String TAG = "AuthenticatorActivity";
    private TextInputLayout username;
    private TextInputLayout password;
    private ProgressBar progressBar;
    private Button btn_login;

    public static String ARG_ACCOUNT_TYPE = "ARG_ACCOUNT_TYPE";
    public static String ARG_AUTH_TYPE = "ARG_AUTH_TYPE";
    public static String ARG_IS_ADDING_NEW_ACCOUNT = "ARG_IS_ADDING_NEW_ACCOUNT";
    public static String ACCOUNT_TYPE = "me.edgeconsult.chat";
    public static String PARAM_USER_PASS = "PARAM_USER_PASS";

    private AccountManager mAccountManager;
    private String mAuthTokenType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);
        mAccountManager = AccountManager.get(getBaseContext());
        username = findViewById(R.id.username_layout);
        password = findViewById(R.id.password_layout);
        progressBar = findViewById(R.id.progressBar);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        Log.d(TAG, "Invoked login function");

        /*if (!validate()) {
            onLoginFailed();
            return;
        }*/

        btn_login.setEnabled(false);
        username.setEnabled(false);
        password.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        // Create async task here
        new AsyncTask<Void, Void, Intent>() {
            @Override
            protected Intent doInBackground(Void... voids) {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(Intent intent) {
                progressBar.setVisibility(View.INVISIBLE);
                username.setEnabled(true);
                password.setEnabled(true);
                btn_login.setEnabled(true);
            }
        }.execute();
    }

    private void login2() {
        final String userName = ((TextView) findViewById(R.id.input_username)).getText().toString();
        final String userPass = ((TextView) findViewById(R.id.input_password)).getText().toString();
        new AsyncTask<Void, Void, Intent>() {
            @Override
            protected Intent doInBackground(Void... params) {
                String authtoken = "726e866c-9643-49e8-a26f-e26769883a5c";
                final Intent res = new Intent();
                res.putExtra(AccountManager.KEY_ACCOUNT_NAME, userName);
                res.putExtra(AccountManager.KEY_ACCOUNT_TYPE, ACCOUNT_TYPE);
                res.putExtra(AccountManager.KEY_AUTHTOKEN, authtoken);
                res.putExtra(PARAM_USER_PASS, userPass);
                return res;
            }
            @Override
            protected void onPostExecute(Intent intent) {
                finishLogin(intent);
            }
        }.execute();
    }

    private void finishLogin(Intent intent) {
        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
        Log.i("log",account.toString());
        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = mAuthTokenType;
            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setAuthToken(account, authtokenType, authtoken);
        } else {
            mAccountManager.setPassword(account, accountPassword);
        }
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

}
