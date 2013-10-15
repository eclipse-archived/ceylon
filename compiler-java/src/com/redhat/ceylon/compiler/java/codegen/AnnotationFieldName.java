package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.java.codegen.Naming.Prefix;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;

public interface AnnotationFieldName {

    public String getFieldName();
    public Prefix getFieldNamePrefix();

    public Parameter getAnnotationField();
    
}
