package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

public class Unit extends Model {
	
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
	
}
