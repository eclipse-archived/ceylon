package com.redhat.ceylon.compiler.js;

import com.redhat.ceylon.compiler.js.util.TypeUtils;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Setter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Value;

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
    
    private final TypedDeclaration declaration;
    private boolean inCapturingScope = false;
    private int sameScope;
    
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
    }

    private void visitReference(Tree.Primary that) {
        if (inCapturingScope) {
            capture(that);
        }
    }

    private void capture(Tree.Primary that) {
        if (that instanceof Tree.MemberOrTypeExpression) {
            TypedDeclaration d = (TypedDeclaration) ((Tree.MemberOrTypeExpression) that).getDeclaration();
            if (d==declaration) {
                if (d.isParameter()) {
                    if (!d.getContainer().equals(that.getScope()) || sameScope>0) {
                        //a reference from a default argument 
                        //expression of the same parameter 
                        //list does not capture a parameter
                        ((FunctionOrValue) d).setCaptured(true);
                    }
                } else if (d instanceof Value && !TypeUtils.isConstructor(d) && !d.isToplevel()) {
                    ((Value) d).setCaptured(true);
                }
            }
        }
    }

    @Override
    public void visit(Tree.QualifiedMemberExpression that) {
        super.visit(that);
        if (isSelfReference(that.getPrimary())) {
            visitReference(that);
        }
        else {
            capture(that);
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
    
    private void captureContainer(Declaration d) {
        if (d == null || d.isAnonymous()) {
            return;
        }
        Declaration cd = ModelUtil.getContainingDeclaration(d);
        if (cd != null && !cd.isAnonymous() && !cd.isCaptured()) {
            if (cd instanceof FunctionOrValue) {
                ((FunctionOrValue) cd).setCaptured(true);
            }
        }
    }

    @Override public void visit(Tree.ClassDefinition that) {
        boolean cs = enterCapturingScope();
        super.visit(that.getClassBody());
        captureContainer(that.getDeclarationModel());
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
    
    @Override public void visit(Tree.TypedArgument that) {
        boolean cs = enterCapturingScope();
        super.visit(that);
        exitCapturingScope(cs);
    }
    
    @Override public void visit(Tree.FunctionArgument that) {
        boolean cs = enterCapturingScope();
        super.visit(that);
        exitCapturingScope(cs);
    }    
    
    @Override
    public void visit(Tree.LazySpecifierExpression that) {
        if(that.getExpression() == null)return;
        boolean cs = enterCapturingScope();
        sameScope++;
        that.getExpression().visit(this);
        sameScope--;
        exitCapturingScope(cs);
    }

    @Override
    public void visit(Tree.Parameter that) {
        //Mark all class initializer parameters as captured
        if (that.getParameterModel().getDeclaration() instanceof com.redhat.ceylon.model.typechecker.model.Class) {
            that.getParameterModel().getModel().setCaptured(true);
        }
        super.visit(that);
    }

}
