package com.redhat.ceylon.compiler.java.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;

/**
 * <p>Thrown when something is invoked which 
 * we statically know cannot be invoked. Such as an
 * abstract constructor which was never delegated-to.</p>
 *  
 * @author tom
 */
@Ceylon(major = 8)
@Class
public class UninvokableError extends Error {

    private static final long serialVersionUID = -8438042507968776L;

    public UninvokableError() {
        super();
    }

}
