package com.redhat.ceylon.compiler.typechecker.util;

import com.redhat.ceylon.compiler.typechecker.context.TypecheckerUnit;

public interface UnitFactory {
    TypecheckerUnit createUnit();
}
