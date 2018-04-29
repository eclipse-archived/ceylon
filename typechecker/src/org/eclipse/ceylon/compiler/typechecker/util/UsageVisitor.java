/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eclipse.ceylon.compiler.typechecker.util;

import static org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil.hasErrorOrWarning;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isAbstraction;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isForBackend;

import org.eclipse.ceylon.common.Backends;
import org.eclipse.ceylon.compiler.typechecker.analyzer.Warning;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.Constructor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.Interface;
import org.eclipse.ceylon.model.typechecker.model.Scope;
import org.eclipse.ceylon.model.typechecker.model.Setter;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeAlias;
import org.eclipse.ceylon.model.typechecker.model.TypeParameter;
import org.eclipse.ceylon.model.typechecker.model.Value;

/**
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
                "import is never used: " 
                + kind(declaration) 
                + " '" 
                + declaration.getName() 
                + "' has no local references");
        }
    }

    private boolean referenced(Tree.ImportMemberOrType that) {
        Declaration dec = that.getDeclarationModel();
        if (dec!=null) {
            if (rc.isReferenced(dec)) {
                return true;
            }
            if (isAbstraction(dec)) {
                for (Declaration od: dec.getOverloads()) {
                    if (rc.isReferenced(od)) {
                        return true;
                    }
                }
            }
            Tree.ImportMemberOrTypeList imtl = 
                    that.getImportMemberOrTypeList();
            if (imtl!=null) {
                for (Tree.ImportMemberOrType m: 
                        imtl.getImportMemberOrTypes()) {
                    if (referenced(m)) {
                        return true;
                    }
                }
                if (imtl.getImportWildcard()!=null) {
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public void visit(Tree.Declaration that) {
        super.visit(that);
        Declaration declaration = that.getDeclarationModel();
        if (declaration!=null 
                && declaration.getName()!=null 
                && !declaration.isShared() 
                && !declaration.isToplevel() 
                && !rc.isReferenced(declaration) 
                && !declaration.isParameter() 
                && !(that instanceof Tree.Variable) 
                && !(declaration instanceof TypeParameter) 
                && isEnabled(declaration.getNativeBackends(), 
                        that)) {
            that.addUsageWarning(Warning.unusedDeclaration,
                  "declaration is never used: " 
                  + kind(declaration) 
                  + " '" 
                  + declaration.getName() 
                  + "' has no local references");
         }
    }

    @Override
    public void visit(Tree.Term that) {
        super.visit(that);
        Scope scope = that.getScope();
        Type type = that.getTypeModel();
        if (!hasErrorOrWarning(that) 
                && type!=null
                && type.isNothing() 
                && isEnabled(scope.getScopedBackends(), 
                        that)) {
            that.addUsageWarning(Warning.expressionTypeNothing,
                    "expression has type 'Nothing'");
         }
    }

    private boolean isEnabled(Backends backends, Node node) {
        return backends.none() 
            || isForBackend(backends, 
                    node.getUnit()
                        .getSupportedBackends());
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
