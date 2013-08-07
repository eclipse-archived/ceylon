package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.typechecker.model.Parameter;

public interface AnnotationFieldName {

    public String getFieldNamePart();

    public Parameter getAnnotationField();
    
}
