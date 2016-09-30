package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.model.typechecker.model.TypeParameter;

public interface GenericBuilder<T> {
    public abstract T typeParameter(TypeParameter tp);
}
