package com.redhat.ceylon.compiler.loader;

import java.util.HashMap;

import com.redhat.ceylon.compiler.typechecker.model.Annotation;

/** 
 * Enumerates the language module annotations that can be treated as 
 * modifiers (i.e. whose annotation class is nullary)
 */
public enum ModifierAnnotation {
    ANNOTATION("annotation", 1L<<0),
    SHARED("shared", 1L<<1),
    VARIABLE("variable", 1L<<2),
    ABSTRACT("abstract", 1L<<3),
    SEALED("sealed", 1L<<4),
    FINAL("final", 1L<<5),
    ACTUAL("actual", 1L<<6),
    FORMAL("formal", 1L<<7),
    DEFAULT("default", 1L<<8),
    LATE("late", 1L<<9),
    NATIVE("native", 1L<<10),
    OPTIONAL("optional", 1L<<11),
    SERIALIZABLE("serializable", 1L<<12);
    
    public final String name;
    
    /** The mask used to address this modifier when encoded as a long. */
    public final long mask;
    
    ModifierAnnotation(String name, long mask) {
        this.name = name;
        this.mask = mask;
    }

    public Annotation makeAnnotation() {
        return new Annotation(name);
    }
}
