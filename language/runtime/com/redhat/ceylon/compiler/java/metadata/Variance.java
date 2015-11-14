package com.redhat.ceylon.compiler.java.metadata;

/**
 * Enumerates possible values for {@link TypeParameter#variance @TypeParameter.variance}
 */
public enum Variance {
    /** Contravariant ({@code in}) type parameter */
    IN("in"), 
    /** Conavariant ({@code out}) type parameter */
    OUT("out"),
    /** Invariant type parameter */
    NONE("");
    
    private final String pretty;
    Variance(String pretty) {
        this.pretty = pretty;
    }
    public String getPretty() {
        return pretty;
    }
}
