package com.redhat.ceylon.compiler.java.runtime.metamodel;

import com.redhat.ceylon.compiler.typechecker.model.Parameter;

public interface DefaultValueProvider {
    Object getDefaultParameterValue(Parameter parameter, Object[] values, int collectedValueCount);
}
