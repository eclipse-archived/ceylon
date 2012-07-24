package com.redhat.ceylon.compiler.java.metadata;

/**
 * Enumerates possible values for {@link TypeParameter#variance @TypeParameter.variance}
 */
public enum Variance {
    /** Contravariant ({@code in}) type parameter */
    IN, 
    /** Conavariant ({@code out}) type parameter */
    OUT,
    /** Invariant type parameter */
    NONE;
}
