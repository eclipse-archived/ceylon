package com.redhat.ceylon.common;

/**
 * Provides access to a password
 * @author tom
 */
public interface Password {
    
    /**
     * Gets the password. A {@code char} array is used rather than a 
     * {@code String} because a String would reside in memory until 
     * garbage collected, whereas the caller can zero the returned array 
     * manually once they're finished with it.
     * @return
     */
    public char[] getPassword();
}
