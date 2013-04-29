package com.redhat.ceylon.compiler.typechecker.model;

/**
 * An argument to an annotation class instantiation that is a parameter
 * of the annotation constructor.
 */
public class ParameterAnnotationArgument extends AnnotationArgument {
    private Parameter sourceParameter;
    private boolean spread;

    /**
     * The annotation constructor parameter
     */
    public Parameter getSourceParameter() {
        return sourceParameter;
    }

    public void setSourceParameter(Parameter sourceParameter) {
        this.sourceParameter = sourceParameter;
    }

    /**
     * Whether the argument is spread
     */
    public boolean isSpread() {
        return spread;
    }

    public void setSpread(boolean spread) {
        this.spread = spread;
    }
}