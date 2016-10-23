package com.redhat.ceylon.model.typechecker.model;

import java.util.List;

public interface ImportScope {
    public List<Import> getImports();
    public void addImport(Import imp);
    public void removeImport(Import imp);
    public Import getImport(String name);
}
