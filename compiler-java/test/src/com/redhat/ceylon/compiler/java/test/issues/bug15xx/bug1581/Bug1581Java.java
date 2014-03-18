package com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1581;

import java.util.Properties;

public class Bug1581Java {
    private Properties p;

    public Bug1581Java(Properties props) {
        // Just doing something with Properties, doesn't matter what
        p = new Properties(props);
    }

    public Properties getProps() {
        return p;
    }
}
