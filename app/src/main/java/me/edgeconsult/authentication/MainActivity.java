package me.edgeconsult.authentication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.OnAccountsUpdateListener;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnAccountsUpdateListener {
    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accountManager = AccountManager.get(getApplicationContext());
        if (accountManager.getAccounts().length > 0) {
            // we save only 1 account
            Account account = accountManager.getAccounts()[0];
            final String str = accountManager.toString();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                }
            });
            /*Account[] account = am.getAccountsByType(AuthenticatorActivity.ACCOUNT_TYPE);
            Log.i("AccountManager", am.toString());
            Log.i("AccountManager", am.getAccountsByType(AuthenticatorActivity.ACCOUNT_TYPE).toString());
            for (int i=0; i<account.length; i++) {
                Log.i("AccountManager", account[i].toString());
                String authTokenType = "";
                final AccountManagerFuture<Bundle> future = am.getAuthToken(account[i], authTokenType, null, this, null,null);
                try {
                    Bundle bnd = future.getResult();
                    final String authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                    Log.i("AccountManager", authtoken);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
        } else {
            // request from user. addAccount
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "No account yet", Toast.LENGTH_SHORT).show();
                }
            });
            // Intent i = new Intent("android.accounts.AccountAuthenticator");
            // startActivity(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        accountManager.addOnAccountsUpdatedListener(this, null, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        accountManager.removeOnAccountsUpdatedListener(this);
    }

    @Override
    public void onAccountsUpdated(Account[] accounts) {
        if (accounts.length > 0) {
            return;
        }
        Intent intent = new Intent(this, AuthenticatorActivity.class);
        startActivity(intent);
        finish();
    }
}
