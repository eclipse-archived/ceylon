package foo.user;

import foo.provider.Provider;

public class User {
    public void user(){
        System.err.println(Provider.provide());
    }
}
