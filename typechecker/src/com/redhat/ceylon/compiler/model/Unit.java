package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

public class Unit extends Model {
	
	List<Import> imports = new ArrayList<Import>();
	Package pkg;
    private List<Declaration> declarations = new ArrayList<Declaration>();
	
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
	
}
