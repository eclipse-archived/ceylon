package com.redhat.ceylon.compiler.java.test.issues.bug14xx.bug1469.bug1469modA;

import java.util.Properties;

public class Bug1469Java {
    private Properties p;
    
    public Bug1469Java(Properties props) {
        // Just doing something with Properties, doesn't matter what
        p = new Properties(props);
    }
    
    public Properties getProps() {
        return p;
    }
}
