package com.redhat.ceylon.compiler.typechecker.analyzer;

import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class DependedUponVisitor extends Visitor {
	private final PhasedUnit phasedUnit;
	private final PhasedUnits phasedUnits;
	
	public DependedUponVisitor(PhasedUnit phasedUnit, PhasedUnits phasedUnits) {
		this.phasedUnit = phasedUnit;
		this.phasedUnits = phasedUnits;
	}
	
	private String getSrcFolderRelativePath(Unit u)
	{
	    return u.getPackage().getQualifiedNameString().replace('.', '/') + "/" + u.getFilename();
	}
	
	private void storeDependency(Declaration d)
	{
	    Unit declarationUnit = d.getUnit();
	    if (declarationUnit != null)
	    {
		    String currentUnitName = getSrcFolderRelativePath(phasedUnit.getUnit());
	    	String dependedOnUnitName = getSrcFolderRelativePath(d.getUnit());
	    	if (! dependedOnUnitName.equals(currentUnitName))
	    	{
        		PhasedUnit dependedOnPhasedUnit = phasedUnits.getPhasedUnitFromRelativePath(dependedOnUnitName);
        		if (dependedOnPhasedUnit != null) {
                    dependedOnPhasedUnit.getDependentsOf().add(currentUnitName);        		    
        		}
	    	}
	    }
	}
	
	@Override
	public void visit(Tree.MemberOrTypeExpression that) {
	    	storeDependency(that.getDeclaration());
		super.visit(that);
	}
		
	@Override
	public void visit(Tree.NamedArgument that) {
	    	storeDependency(that.getParameter());
		super.visit(that);
	}
		
	@Override
	public void visit(Tree.Type that) {
	    	storeDependency(that.getTypeModel().getDeclaration());
		super.visit(that);
	}
		
	@Override
	public void visit(Tree.ImportMemberOrType that) {
	    	storeDependency(that.getDeclarationModel());
		super.visit(that);
	}
		
}
