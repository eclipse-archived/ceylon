package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.model.loader.NamingBase.Prefix;
import com.redhat.ceylon.model.typechecker.model.Parameter;

public interface AnnotationFieldName {

    public String getFieldName();
    public Prefix getFieldNamePrefix();

    public Parameter getAnnotationField();
    
}
