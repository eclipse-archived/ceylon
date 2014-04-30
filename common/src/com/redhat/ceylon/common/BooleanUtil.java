package com.redhat.ceylon.common;

/**
 * Believe it or not, but auto-boxing returns instances of Boolean which are not == Boolean.TRUE
 * so we need something smarter :(
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class BooleanUtil {
    /**
     * @return true if b is not null and is true
     */
    public static boolean isTrue(Boolean b){
        return b != null && b.booleanValue();
    }

    /**
     * @return true if b is null or is true
     */
    public static boolean isNotFalse(Boolean b){
        return b == null || b.booleanValue();
    }
}
