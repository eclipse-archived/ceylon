package com.redhat.ceylon.model.typechecker.model;

import java.util.List;
import java.util.Map;

public interface ImportableScope extends Scope {
    public Map<String, DeclarationWithProximity> getImportableDeclarations(Unit unit, String startingWith, List<Import> imports, int proximity);
}
