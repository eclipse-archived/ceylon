package org.eclipse.ceylon.compiler.java.language;

import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Class;

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
