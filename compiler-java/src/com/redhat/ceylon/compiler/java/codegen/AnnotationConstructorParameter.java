package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.typechecker.model.Parameter;

/**
 * A parameter to an annotation constructor, 
 * recording information about its default argument.
 *
 */
public class AnnotationConstructorParameter implements AnnotationFieldName {

    private Parameter parameter;
    
    private AnnotationTerm defaultArgument;

    public AnnotationConstructorParameter() {}
    
    /**
     * The corresponding parameter of the annotation constructor {@code Method}
     * @return
     */
    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public AnnotationTerm getDefaultArgument() {
        return defaultArgument;
    }

    public void setDefaultArgument(AnnotationTerm defaultArgument) {
        this.defaultArgument = defaultArgument;
    }
    
    public String toString() {
        return defaultArgument != null ? "=" + defaultArgument : "";
    }

    @Override
    public String getFieldNamePart() {
        return "default$"+parameter.getName();
    }

    @Override
    public Parameter getAnnotationField() {
        return getParameter();
    }
}
