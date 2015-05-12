/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.ceylon.compiler.typechecker.util;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.hasErrorOrWarning;
import static com.redhat.ceylon.model.typechecker.model.Util.isAbstraction;
import static com.redhat.ceylon.model.typechecker.model.Util.isTypeUnknown;

import com.redhat.ceylon.compiler.typechecker.analyzer.Warning;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ImportMemberOrType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ImportMemberOrTypeList;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.ProducedType;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;

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
            that.addUsageWarning(Warning.unusedImport,
                "import is never used: '" + 
                    that.getDeclarationModel().getName() + "'");
    	}
    }

	private boolean referenced(Tree.ImportMemberOrType that) {
		Declaration d = that.getDeclarationModel();
        boolean referenced=true;
        if (d!=null) {
        	referenced = rc.isReferenced(d);
        	if (isAbstraction(d)) {
        		for (Declaration od: ((Functional) d).getOverloads()) {
        			referenced=referenced||rc.isReferenced(od);
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
                declaration.getName()!=null &&
        		!declaration.isShared() && 
        		!declaration.isToplevel() && 
        		!rc.isReferenced(declaration) &&
        		!declaration.isParameter() &&
        		!(that instanceof Tree.Variable) &&
        		!(declaration instanceof TypeParameter &&
        		    ((TypeParameter) declaration).getDeclaration() 
        		            instanceof TypeParameter)) {
            that.addUsageWarning(Warning.unusedDeclaration,
                    "declaration is never used: '" + 
                        declaration.getName() + "'");
        }
        
    }

    @Override
    public void visit(Tree.Term that) {
        super.visit(that);
        if (!hasErrorOrWarning(that)) {
            ProducedType type = that.getTypeModel();
            if (!isTypeUnknown(type) && type.isNothing()) {
                that.addUsageWarning(Warning.expressionTypeNothing,
                        "expression has type 'Nothing'");
            }
        }
    }
}
