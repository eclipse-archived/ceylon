package com.redhat.ceylon.compiler.java.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;

/**
 * <p>Thrown when a method has been declared, but not specified.</p>
 * 
 * <p>The definite assignment of methods with deferred specification is 
 * normally verified at compile time. 
 * This error can only occur if the compiler's checks are subverted, for 
 * example by using Java-level reflection to invoke a {@code private} 
 * method which was left uninitialized.</p>
 *  
 * @author tom
 */
@Ceylon(major = 6)
@Class
public class UninitializedMethodError extends Error {

    private static final long serialVersionUID = 7063698116161174567L;

	public UninitializedMethodError() {
        super();
    }

}
