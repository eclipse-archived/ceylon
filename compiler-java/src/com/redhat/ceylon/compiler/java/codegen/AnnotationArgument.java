package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.java.codegen.Naming.Prefix;
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
    @Override
    public String toString() {
        return parameter.getName()+ " = " + term; 
    }
    @Override
    public String getFieldName() {
        return getParameter().getName();
    }
    @Override
    public Prefix getFieldNamePrefix() {
        return Prefix.$arg$;
    }
    @Override
    public Parameter getAnnotationField() {
        return getParameter();
    }
}
