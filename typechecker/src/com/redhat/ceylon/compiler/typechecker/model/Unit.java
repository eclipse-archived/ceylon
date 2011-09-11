package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Unit {

    Package pkg;
    List<Import> imports = new ArrayList<Import>();
    List<Declaration> declarations = new ArrayList<Declaration>();
    String filename;
    List<ImportList> importLists = new ArrayList<ImportList>();

    public List<Import> getImports() {
        return imports;
    }

    public List<ImportList> getImportLists() {
        return importLists;
    }

    public Package getPackage() {
        return pkg;
    }

    public void setPackage(Package p) {
        pkg = p;
    }

    public List<Declaration> getDeclarations() {
        return declarations;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "Unit[" + filename + "]";
    }

    /**
     * Search the imports of a compilation unit
     * for the declaration.
     */
    public Declaration getImportedDeclaration(String name) {
        for (Import i: getImports()) {
            if (i.getAlias().equals(name)) {
                return i.getDeclaration();
            }
        }
        return null;
    }
    
    public Map<String, DeclarationWithProximity> getMatchingImportedDeclarations(String startingWith, int proximity) {
    	Map<String, DeclarationWithProximity> result = new TreeMap<String, DeclarationWithProximity>();
        for (Import i: getImports()) {
            if (i.getAlias().startsWith(startingWith)) {
                result.put(i.getAlias(), new DeclarationWithProximity(i, proximity));
            }
        }
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Unit) {
            Unit that = (Unit) obj;
            return that.getPackage().equals(getPackage())
                    && that.getFilename().equals(getFilename());
        }
        else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return getFilename().hashCode();
    }

}
