/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.ceylon.compiler.typechecker.util;

import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.hasErrorOrWarning;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isAbstraction;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isTypeUnknown;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.BackendSupport;
import com.redhat.ceylon.compiler.typechecker.analyzer.Warning;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ImportMemberOrType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ImportMemberOrTypeList;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;

/**
 *
 * @author kulikov
 */
public class UsageVisitor extends Visitor {
	
    private final ReferenceCounter rc;
    private final BackendSupport backendSupport;
    
    private Backend inBackend = null;
	
	public UsageVisitor(ReferenceCounter rc,
	        BackendSupport backendSupport) {
		this.rc = rc;
        this.backendSupport = backendSupport;
	}
	
    @Override public void visit(Tree.CompilationUnit that) {
        Backend ib = inBackend;
        String nat = 
                that.getUnit()
                    .getPackage()
                    .getModule()
                    .getNativeBackend();
        inBackend = Backend.fromAnnotation(nat);
        super.visit(that);
        inBackend = ib;
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
        		for (Declaration od: d.getOverloads()) {
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
        Backend ib = inBackend;
        String nat = that.getDeclarationModel().getNativeBackend();
        if (nat != null) {
            inBackend = Backend.fromAnnotation(nat);
        }
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
            if (inBackend == null || backendSupport.supportsBackend(inBackend)) {
                that.addUsageWarning(Warning.unusedDeclaration,
                        "declaration is never used: '" + 
                            declaration.getName() + "'");
            }
        }
        inBackend = ib;
    }

    @Override
    public void visit(Tree.Term that) {
        super.visit(that);
        if (!hasErrorOrWarning(that)) {
            Type type = that.getTypeModel();
            if (!isTypeUnknown(type) && type.isNothing()) {
                if (inBackend == null || backendSupport.supportsBackend(inBackend)) {
                    that.addUsageWarning(Warning.expressionTypeNothing,
                            "expression has type 'Nothing'");
                }
            }
        }
    }
}
