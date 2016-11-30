/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.ceylon.compiler.typechecker.util;

import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.hasErrorOrWarning;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isAbstraction;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isForBackend;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isTypeUnknown;

import com.redhat.ceylon.common.Backends;
import com.redhat.ceylon.compiler.typechecker.analyzer.Warning;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.Setter;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeAlias;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.Value;

/**
 *
 * @author kulikov
 */
public class UsageVisitor extends Visitor {
    
    private final ReferenceCounter rc;
    
    public UsageVisitor(ReferenceCounter rc) {
        this.rc = rc;
    }
    
    @Override public void visit(Tree.CompilationUnit that) {
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.ImportMemberOrType that) {
        super.visit(that);
        if (!referenced(that)) {
            Declaration declaration = that.getDeclarationModel();
            that.addUsageWarning(Warning.unusedImport,
                "import is never used: " + 
                kind(declaration) + 
                " '" + declaration.getName() + 
                "' has no local references");
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
            Tree.ImportMemberOrTypeList imtl = 
                    that.getImportMemberOrTypeList();
            if (imtl!=null) {
                for (Tree.ImportMemberOrType m: 
                        imtl.getImportMemberOrTypes()) {
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
        Backends bs = declaration.getNativeBackends();
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
            if (bs.none() 
                    || isForBackend(bs, 
                            that.getUnit()
                                .getSupportedBackends())) {
                that.addUsageWarning(Warning.unusedDeclaration,
                        "declaration is never used: " + 
                        kind(declaration) +
                        " '" + declaration.getName() + 
                        "' has no local references");
            }
        }
    }

    @Override
    public void visit(Tree.Term that) {
        super.visit(that);
        if (!hasErrorOrWarning(that)) {
            Type type = that.getTypeModel();
            if (!isTypeUnknown(type) && type.isNothing()) {
                Backends inBackends = 
                        that.getScope()
                            .getScopedBackends();
                if (inBackends.none() 
                        || isForBackend(inBackends, 
                                that.getUnit()
                                    .getSupportedBackends())) {
                    that.addUsageWarning(Warning.expressionTypeNothing,
                            "expression has type 'Nothing'");
                }
            }
        }
    }

    private static String kind(Declaration declaration) {
        if (declaration instanceof Function) {
            return "function";
        }
        if (declaration instanceof Value) {
            return "value";
        }
        if (declaration instanceof Setter) {
            return "setter";
        }
        if (declaration instanceof Class) {
            return "class";
        }
        if (declaration instanceof Interface) {
            return "interface";
        }
        if (declaration instanceof TypeParameter) {
            return "type parameter";
        }
        if (declaration instanceof Constructor) {
            return "constructor";
        }
        if (declaration instanceof TypeAlias) {
            return "type alias";
        }
        return "declaration";
    }
}
