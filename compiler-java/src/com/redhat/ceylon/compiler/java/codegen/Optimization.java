package com.redhat.ceylon.compiler.java.codegen;

/**
 * Enumerates compiler optimizations, which can be disabled and required 
 * by name.
 */
public enum Optimization {

    /* If you're changing the elements of this enum please also change 
     * com.sun.tools.javac.main.OptionName.CEYLONDISABLEOPT_CUSTOM
     */
    RangeOpIteration,
    SegmentOpIteration,
    RangeIterationStatic,
    ArrayIterationStatic,
    JavaArrayIterationStatic,
    ArraySequenceIterationStatic,
    TupleIterationStatic,
    ArrayIterationDynamic,
    ArraySequenceIterationDynamic,
    
    PowerUnroll
}
