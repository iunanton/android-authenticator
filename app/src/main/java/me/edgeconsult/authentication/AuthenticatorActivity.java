package me.edgeconsult.authentication;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    public static String ARG_ACCOUNT_TYPE = "ARG_ACCOUNT_TYPE";
    public static String ARG_AUTH_TYPE = "ARG_AUTH_TYPE";
    public static String ARG_IS_ADDING_NEW_ACCOUNT = "ARG_IS_ADDING_NEW_ACCOUNT";
    public static String ACCOUNT_TYPE = "me.edgeconsult.chat";
    public static String PARAM_USER_PASS = "PARAM_USER_PASS";

    private AccountManager mAccountManager;
    private String mAuthTokenType = "";

    private ServerAuthenticate sServerAuthenticate = new ServerAuthenticate() {
        @Override
        public String userSignIn(String name, String password, String authTokenType) {
            String authToken = "50123116-8bbc-4ddc-8df8-56fe829bd26d";
            return authToken;
        }

        @Override
        public String userSignUp(String name, String password, String authTokenType) {
            String authToken = "726e866c-9643-49e8-a26f-e26769883a5c";
            return authToken;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);
        mAccountManager = AccountManager.get(getBaseContext());
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userName = ((TextView) findViewById(R.id.accountName)).getText().toString();
                final String userPass = ((TextView) findViewById(R.id.accountPassword)).getText().toString();
                new AsyncTask<Void, Void, Intent>() {
                    @Override
                    protected Intent doInBackground(Void... params) {
                        String authtoken = sServerAuthenticate.userSignIn(userName, userPass, mAuthTokenType);
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

        });
    }
}
