/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.ceylon.compiler.typechecker.util;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ImportMemberOrType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ImportMemberOrTypeList;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 *
 * @author kulikov
 */
public class UsageVisitor extends Visitor {
	
	private ReferenceCounter rc;
	
	public UsageVisitor(ReferenceCounter rc) {
		this.rc = rc;
	}
	
    @Override
    public void visit(Tree.ImportMemberOrType that) {
        super.visit(that);
        if (!referenced(that)) {
    		that.addUsageWarning("import is never used: " + 
    				that.getDeclarationModel().getName());
    	}
    }

	private boolean referenced(Tree.ImportMemberOrType that) {
		Declaration d = that.getDeclarationModel();
        boolean referenced=true;
        if (d!=null) {
        	referenced = rc.referenced(d);
        	if (d instanceof Functional) {
        		if (((Functional) d).isAbstraction()) {
        			for (Declaration od: ((Functional) d).getOverloads()) {
        				referenced=referenced||rc.referenced(od);
        			}
        		}
        	}
        	ImportMemberOrTypeList imtl = that.getImportMemberOrTypeList();
        	if (imtl!=null) {
        		for (ImportMemberOrType m: imtl.getImportMemberOrTypes()) {
					referenced=referenced||referenced(m);
        		}
        		if (imtl.getImportWildcard()!=null) {
        		    referenced = true;
        		}
        	}
        }
		return referenced;
	}

    @Override
    public void visit(Tree.Declaration that) {
        super.visit(that);
        Declaration declaration = that.getDeclarationModel();
        if (declaration!=null && 
        		!declaration.isShared() && 
        		!declaration.isToplevel() && 
        		!rc.referenced(declaration) &&
        		!declaration.isParameter() &&
        		!(that instanceof Tree.Variable)) {
            that.addUsageWarning("declaration is never used: " + 
        		    declaration.getName());
        }
    }

}
