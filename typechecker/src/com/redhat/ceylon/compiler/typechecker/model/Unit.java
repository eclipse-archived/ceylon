package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.List;

public class Unit {
	
    Package pkg;
	List<Import> imports = new ArrayList<Import>();
    List<Declaration> declarations = new ArrayList<Declaration>();
    String filename;
	
	public List<Import> getImports() {
		return imports;
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
            Declaration d = i.getDeclaration();
            if (i.getAlias().equals(name)) {
                return d;
            }
        }
        return null;
    }
    
}
