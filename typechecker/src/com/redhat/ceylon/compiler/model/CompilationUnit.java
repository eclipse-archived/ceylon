package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

public class CompilationUnit extends Model {
	
	List<Import> imports = new ArrayList<Import>();
	
	public List<Import> getImports() {
		return imports;
	}
	
}
