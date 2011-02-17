package com.redhat.ceylon.compiler.model;

public abstract class Element extends Model {
	
	Scope container;
	Unit unit;
	
	public Unit getUnit() {
		return unit;
	}
	
	public void setUnit(Unit compilationUnit) {
		this.unit = compilationUnit;
	}

	public Scope getContainer() {
		return container;
	}
	
	public void setContainer(Scope scope) {
		this.container = scope;
	}
	
}
