package com.redhat.ceylon.compiler.java.codegen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.model.ControlBlock;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Visitor which adds information to the model needed for handling 
 * Values declared before a for/else loop, but whose value is specified
 * within the loop.
 * See #1227.
 */
public class DefiniteAssignmentVisitor extends Visitor {
    
    private ControlBlock forBlock = null;
    private ControlBlock elseBlock = null;
    
    private HashMap<Value, ControlBlock> tracked = new HashMap<Value, ControlBlock>();
    
    public void visit(Tree.AnyMethod that) {
        ControlBlock prevControlBlock = forBlock;
        forBlock = null;
        super.visit(that);
        forBlock = prevControlBlock;
    }
    
    public void visit(Tree.AnyAttribute that) {
        ControlBlock prevControlBlock = forBlock;
        forBlock = null;
        super.visit(that);
        forBlock = prevControlBlock;
    }
    
    public void visit(Tree.AnyClass that) {
        ControlBlock prevControlBlock = forBlock;
        forBlock = null;
        super.visit(that);
        forBlock = prevControlBlock;
    }
    
    public void visit(Tree.AttributeDeclaration that) {
        // We're interested in non-variable, deferred AttributeDeclarations 
        // that are declared outside a for/else loop, but specified inside it
        if (that.getSpecifierOrInitializerExpression() == null
                && !that.getDeclarationModel().isVariable()) {
            tracked.put(that.getDeclarationModel(), forBlock);
        }
    }
    
    public void visit(Tree.SpecifierStatement stmt) {
        Tree.Term bme = stmt.getBaseMemberExpression();
        if (bme instanceof Tree.MemberOrTypeExpression) {
            Declaration decl = ((Tree.MemberOrTypeExpression)bme).getDeclaration();
            if (tracked.containsKey(decl) // non-variable and deferred
                    && forBlock != null // specification is in a for/else
                    && !forBlock.equals(tracked.get(decl))) { // not declared in *this* for/else
                if (elseBlock == null) {
                    ((Value)decl).setSpecifiedInForElse(true);
                }
                ControlBlock assigningBlock = elseBlock != null ? elseBlock : forBlock;
                Set<Value> assigned = assigningBlock.getSpecifiedValues();
                if (assigned == null) {
                    assigned = new HashSet<Value>(1);
                    assigningBlock.setSpecifiedValues(assigned);
                }
                assigned.add((Value)decl);
            }
        }
        super.visit(stmt);
    }
    
    public void visit(Tree.ForStatement that) {
        ControlBlock prevControlBlock = forBlock;
        
        forBlock = that.getForClause().getControlBlock();
        that.getForClause().visit(this);
        
        if (that.getElseClause() != null) {
            elseBlock = that.getElseClause().getControlBlock();
            that.getElseClause().visit(this);
            elseBlock = null;
        }
        
        forBlock = prevControlBlock;
    }
}
