package com.redhat.ceylon.compiler.java.language;

/**
 * Because java won't let us utter {@code Object.super} in an interface, 
 * this is present so that given 
 * <code>
 * <pre>
 * interface I {
 *     shared actual String string => super.string;
 * }
 * </pre>
 * </code>
 * we can access {@code Object.toString()} by compiling {@code I} to
 * <code>
 * <pre>
 * interface I implements com.redhat.ceylon.compiler.java.language.Object {
 *     public String toString() { return com.redhat.ceylon.compiler.java.language.Object.super.toString(); }
 * }
 * </pre>
 * </code>
 */
public interface ObjectProxy {
    // INTENTIONALLY EMPTY
    // AND WOE BETIDE YOU IF YOU IMPLEMENT 
    // toString(), hashCode() or equals(Object) here!
}
