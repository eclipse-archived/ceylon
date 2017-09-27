package org.eclipse.ceylon.compiler.java.codegen;

public interface ParameterizedBuilder<B extends ParameterizedBuilder<B>> {

    public B parameter(ParameterDefinitionBuilder pdb);
    
}
