package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Array;

import com.redhat.ceylon.compiler.typechecker.model.Parameter;

public interface DefaultValueProvider {
    Object getDefaultParameterValue(Parameter parameter, Array<java.lang.Object> values, int collectedValueCount);
}
