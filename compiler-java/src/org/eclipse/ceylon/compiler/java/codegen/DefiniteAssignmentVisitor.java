package org.eclipse.ceylon.compiler.java.codegen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.model.typechecker.model.ControlBlock;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.Value;

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
    
    private void checkForCycle(Node node, final Declaration decl, final AnnotationInvocation invocation, Set<Declaration> s) {
        if (!node.getErrors().isEmpty()) {
            return;
        }
        if (!AnnotationModelVisitor.isAnnotationConstructor(decl)) {
            return;
        }
        Declaration d = decl;
        while (d instanceof Function) {
            if (!s.add(d)) {
                node.addError("recursive annotation constructor: '"+decl.getName()+"' invokes itself");
                break;
            }
            AnnotationInvocation annotationConstructor = (AnnotationInvocation)((Function)d).getAnnotationConstructor();
            if (annotationConstructor == null) {
                return;
            }
            d = annotationConstructor.getPrimary();
        }
        for (AnnotationConstructorParameter param : invocation.getConstructorParameters()) {
            if (param.getDefaultArgument() instanceof InvocationAnnotationTerm) {
                InvocationAnnotationTerm t = (InvocationAnnotationTerm)param.getDefaultArgument();
                checkForCycle(node, t.getInstantiation().getPrimary(), t.getInstantiation(), s);
            }
        }
    }
    
    public void visit(Tree.AnyMethod that) {
        ControlBlock prevControlBlock = forBlock;
        forBlock = null;
        super.visit(that);
        checkForCycle(that, that.getDeclarationModel(), (AnnotationInvocation)that.getDeclarationModel().getAnnotationConstructor(), new HashSet<Declaration>());
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
                && !that.getDeclarationModel().isVariable()
                && !that.getDeclarationModel().isLate()) {
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
                addSpecified(decl, assigningBlock);
            }
        }
        super.visit(stmt);
    }

    protected void addSpecified(Declaration decl, ControlBlock assigningBlock) {
        Set<Value> assigned = assigningBlock.getSpecifiedValues();
        if (assigned == null) {
            assigned = new HashSet<Value>(1);
            assigningBlock.setSpecifiedValues(assigned);
        }
        assigned.add((Value)decl);
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
        
        if (prevControlBlock != null) {
            Set<Value> specifiedValues = forBlock.getSpecifiedValues();
            if (specifiedValues != null) {
                for (Declaration decl : new ArrayList<Value>(specifiedValues)) {
                    if (!prevControlBlock.equals(tracked.get(decl))) {
                        addSpecified(decl, prevControlBlock);
                        specifiedValues.remove(decl);
                    }
                }
            }
        }
        
        forBlock = prevControlBlock;
    }
}
