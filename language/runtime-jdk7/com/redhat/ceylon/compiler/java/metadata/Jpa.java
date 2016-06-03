package com.redhat.ceylon.compiler.java.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation on "JPA constructors" (i.e. constructors which are present in 
 * bytecode for the purposes of interop with Java frameworks which need 
 * nullary constructors, but for which there is not corresponding 
 * Ceylon constructor or Class parameter list).
 * 
 * @author tom
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface Jpa {

}
