package org.eclipse.ceylon.compiler.java.runtime.metamodel;

import org.eclipse.ceylon.model.typechecker.model.Parameter;

import ceylon.language.Array;

public interface DefaultValueProvider {
    Object getDefaultParameterValue(Parameter parameter, Array<java.lang.Object> values, int collectedValueCount);
}
