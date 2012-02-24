package com.redhat.ceylon.compiler.typechecker.model;

import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;

public class ExternalUnit extends Unit {
    private final Set<PhasedUnit> dependentsOf = new HashSet<PhasedUnit>();

    public Set<PhasedUnit> getDependentsOf() {
        return dependentsOf;
    }
}
