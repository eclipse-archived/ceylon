package com.redhat.ceylon.compiler.java.codegen.recovery;

/**
 * The normal, error-free transformation plan.
 * Instance available from {@link Errors#GENERATE}.
 */
public class Generate extends TransformationPlan {
    Generate() {
        super(0, null, null);
    }
}