package com.redhat.ceylon.compiler.typechecker.analyzer;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.model.ValueParameter;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Determines if a value is "captured" by 
 * block nested in the same containing scope.
 * 
 * For example, a captured value in a class
 * body is an attribute. A captured value in
 * a method body can outlive the execution of
 * the method.
 * 
 * @author Gavin King
 *
 */
public class ValueVisitor extends Visitor {
    
    private TypedDeclaration declaration;
    private boolean inCapturingScope = false;
    
    public ValueVisitor(TypedDeclaration declaration) {
        this.declaration = declaration;
    }
    
    private boolean enterCapturingScope() {
        boolean cs = inCapturingScope;
        inCapturingScope = true;
        return cs;
    }
    
    private void exitCapturingScope(boolean cs) {
        inCapturingScope = cs;
    }
    
    @Override public void visit(Tree.BaseMemberExpression that) {
        visitReference(that);
        /*if (that.getIdentifier()!=null) {
            TypedDeclaration d = (TypedDeclaration) getDeclaration(that.getScope(), that.getUnit(), that.getIdentifier(), context);
            visitReference(that, d);
        }*/
    }

    private void visitReference(Tree.Primary that) {
        if (inCapturingScope) {
            TypedDeclaration d = (TypedDeclaration) that.getDeclaration();
            if (d==declaration) {
                if (d instanceof Value) {
                    ((Value) d).setCaptured(true);
                }
                else if (d instanceof ValueParameter) {
                    ((ValueParameter) d).setCaptured(true);
                }
                //TODO: remove this once we support capturing variable locals!
                if (d.isVariable() && !d.isClassMember() && !d.isToplevel()) {
                    that.addError("access to variable local from capturing scope: " + declaration.getName());
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.QualifiedMemberExpression that) {
        if (isSelfReference(that.getPrimary())) {
            visitReference(that);
        }
    }

    private boolean isSelfReference(Tree.Primary that) {
        return that instanceof Tree.This || that instanceof Tree.Outer;
    }

    @Override public void visit(Tree.Declaration that) {
        Declaration dm = that.getDeclarationModel();
        if (dm==declaration.getContainer() 
                || dm==declaration
                || (dm instanceof Setter && ((Setter) dm).getGetter()==declaration)) {
            inCapturingScope = false;
        }
        super.visit(that);
    }
    
    @Override public void visit(Tree.ClassDefinition that) {
        boolean cs = enterCapturingScope();
        super.visit(that);
        exitCapturingScope(cs);
    }
    
    @Override public void visit(Tree.ObjectDefinition that) {
        boolean cs = enterCapturingScope();
        super.visit(that);
        exitCapturingScope(cs);
    }
    
    @Override public void visit(Tree.MethodDefinition that) {
        boolean cs = enterCapturingScope();
        super.visit(that);
        exitCapturingScope(cs);
    }
    
    @Override public void visit(Tree.AttributeGetterDefinition that) {
        boolean cs = enterCapturingScope();
        super.visit(that);
        exitCapturingScope(cs);
    }
    
    @Override public void visit(Tree.AttributeSetterDefinition that) {
        boolean cs = enterCapturingScope();
        super.visit(that);
        exitCapturingScope(cs);
    }
    
}
