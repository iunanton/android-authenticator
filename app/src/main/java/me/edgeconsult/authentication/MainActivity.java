package me.edgeconsult.authentication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AccountManager am = AccountManager.get(getApplicationContext());
        Account[] account = am.getAccountsByType(AuthenticatorActivity.ACCOUNT_TYPE);
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
        }
    }
}
