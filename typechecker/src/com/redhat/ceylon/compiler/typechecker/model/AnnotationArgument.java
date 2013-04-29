package com.redhat.ceylon.compiler.typechecker.model;

/**
 * Represents an argument in the instantiation of an annotation class in 
 * an annotation constructor
 * @see AnnotationInstantiation
 */
public abstract class AnnotationArgument {
    
    Parameter targetParameter;

    /** 
     * The annotation constructor parameter corresponding to this argument 
     */
    public Parameter getTargetParameter() {
        return targetParameter;
    }

    public void setTargetParameter(Parameter parameter) {
        this.targetParameter = parameter;
    }
    
}