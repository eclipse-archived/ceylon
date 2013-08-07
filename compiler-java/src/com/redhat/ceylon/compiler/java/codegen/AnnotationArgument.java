package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.typechecker.model.Parameter;

/**
 * An argument in an annotation invocation
 */
public class AnnotationArgument implements AnnotationFieldName {

    private AnnotationTerm term;
    private Parameter parameter;
    public AnnotationArgument() {
        
    }
    /**
     * The value of the argument
     */
    public AnnotationTerm getTerm() {
        return term;
    }
    public void setTerm(AnnotationTerm term) {
        this.term = term;
    }
    /**
     * The formal parameter corresponding to this argument.
     */
    public Parameter getParameter() {
        return parameter;
    }
    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }
    
    public String toString() {
        return parameter.getName()+ " = " + term; 
    }
    @Override
    public String getFieldNamePart() {
        return "arg$"+getParameter().getName();
    }
    @Override
    public Parameter getAnnotationField() {
        return getParameter();
    }
}
