package com.redhat.ceylon.model.loader.model;

import com.redhat.ceylon.model.typechecker.model.Value;

public class JavaParameterValue extends Value {
    @Override
    public boolean isJava() {
        return true;
    }
}
