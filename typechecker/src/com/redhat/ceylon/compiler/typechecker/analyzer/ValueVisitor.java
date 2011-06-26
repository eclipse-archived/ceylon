package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getDeclaration;

import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.model.ValueParameter;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class ValueVisitor extends Visitor {
    
    private TypedDeclaration declaration;
    private Context context;
    private boolean inCapturingScope = false;
    
    public ValueVisitor(TypedDeclaration declaration, Context context) {
        this.declaration = declaration;
        this.context = context;
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
        if (inCapturingScope) {
            if (that.getIdentifier()!=null) {
                TypedDeclaration d = (TypedDeclaration) getDeclaration(that.getScope(), that.getUnit(), that.getIdentifier(), context);
                if (d==declaration) {
                    if (d instanceof Value) {
                        ((Value) d).setCaptured(true);
                    }
                    else if (d instanceof ValueParameter) {
                        ((ValueParameter) d).setCaptured(true);
                    }
                    //TODO: remove this once we support capturing variable locals!
                    if (d.isVariable() && 
                        !(declaration.getContainer() instanceof Class) && 
                        !(declaration.getContainer() instanceof Package)) {
                        that.addError("access to variable local from capturing scope: " + declaration.getName());
                    }
                }
            }
        }
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
