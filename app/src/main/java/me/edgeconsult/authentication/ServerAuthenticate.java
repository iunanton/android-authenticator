package me.edgeconsult.authentication;

/**
 * Created by yun on 9/25/17.
 */

public interface ServerAuthenticate {
    public String userSignIn (String name, String password, String authTokenType);
    public String userSignUp (String name, String password, String authTokenType);
}
