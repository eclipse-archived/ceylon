package org.eclipse.ceylon.compiler.java.codegen;

import org.eclipse.ceylon.model.loader.NamingBase.Prefix;
import org.eclipse.ceylon.model.typechecker.model.Parameter;

public interface AnnotationFieldName {

    public String getFieldName();
    public Prefix getFieldNamePrefix();

    public Parameter getAnnotationField();
    
}
