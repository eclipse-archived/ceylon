package org.eclipse.ceylon.model.loader.model;

import org.eclipse.ceylon.model.typechecker.model.Value;

public class JavaParameterValue extends Value {
    @Override
    public boolean isJava() {
        return true;
    }
}
