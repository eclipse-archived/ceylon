package org.eclipse.ceylon.compiler.java.codegen;

import org.eclipse.ceylon.model.typechecker.model.TypeParameter;

public interface GenericBuilder<T> {
    public abstract T typeParameter(TypeParameter tp);
}
