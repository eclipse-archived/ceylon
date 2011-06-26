package com.redhat.ceylon.compiler.typechecker.analyzer;

import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Validates that non-variable values are well-defined
 * within the local scope in which they occur. Checks
 * that they are not used before they are defined, that
 * they are always specified before they are used, and
 * that they are never specified twice.
 * 
 * @author Gavin King
 *
 */
public class SelfReferenceVisitor extends Visitor {
        
    private Tree.Statement lastExecutableStatement;
    private boolean declarationSection = false;

    @Override
    public void visit(Tree.AnnotationList that) {}
    
    @Override
    public void visit(Tree.ClassBody that) {
        Tree.Statement les = null;
        for (Tree.Statement s: that.getStatements()) {
            if (s instanceof Tree.ExecutableStatement) {
                les = s;
            }
            else {
                if (s instanceof Tree.AttributeDeclaration) {
                    if ( ((Tree.AttributeDeclaration) s).getSpecifierOrInitializerExpression()!=null ) {
                        les = s;
                    }
                }
                if (s instanceof Tree.MethodDeclaration) {
                    if ( ((Tree.MethodDeclaration) s).getSpecifierExpression()!=null ) {
                        les = s;
                    }
                }
            }
        }
        Tree.Statement oles = lastExecutableStatement;
        boolean ods = declarationSection;
        declarationSection = les==null;
        lastExecutableStatement = les;
        super.visit(that);
        lastExecutableStatement = oles;
        declarationSection = ods;
    }
    
    boolean inDeclarationSection() {
        return declarationSection;
    }
    
    @Override
    public void visit(Tree.Statement that) {
        super.visit(that);
        declarationSection = declarationSection || (that==lastExecutableStatement);
    }
    
    @Override
    public void visit(Tree.Return that) {
        super.visit(that);
        //TODO: MOVE TO DIFFERENT VISITOR!
        if (lastExecutableStatement!=null && 
                that.getExpression()!=null &&
                that.getExpression().getTerm() instanceof Tree.This 
                && !inDeclarationSection()) {
            that.addError("leaks this reference");
        }
    }

    @Override
    public void visit(Tree.SpecifierOrInitializerExpression that) {
        super.visit(that);
        //TODO: MOVE TO DIFFERENT VISITOR!
        if (lastExecutableStatement!=null &&
                that.getExpression().getTerm() instanceof Tree.This 
                && !inDeclarationSection()) {
            that.addError("leaks this reference");
        }
        //TODO: assignments
    }

    @Override
    public void visit(Tree.AssignmentOp that) {
        super.visit(that);
        //TODO: MOVE TO DIFFERENT VISITOR!
        if (lastExecutableStatement!=null &&
                that.getRightTerm() instanceof Tree.This 
                && !inDeclarationSection()) {
            that.addError("leaks this reference");
        }
        //TODO: assignments
    }

    @Override
    public void visit(Tree.PositionalArgumentList that) {
        super.visit(that);
        //TODO: MOVE TO DIFFERENT VISITOR!
        if (lastExecutableStatement!=null && !inDeclarationSection()) {
            for ( Tree.PositionalArgument arg: that.getPositionalArguments()) {
                if (arg.getExpression().getTerm() instanceof Tree.This) {
                    arg.addError("leaks this reference");
                }
            }
        }
    }

    @Override
    public void visit(Tree.NamedArgumentList that) {
        super.visit(that);
        //TODO: MOVE TO DIFFERENT VISITOR!
        if (lastExecutableStatement!=null && !inDeclarationSection()) {
            for ( Tree.NamedArgument arg: that.getNamedArguments()) {
                if (arg instanceof Tree.SpecifiedArgument) {
                    if (((Tree.SpecifiedArgument)arg).getSpecifierExpression().getExpression().getTerm() instanceof Tree.This) {
                        arg.addError("leaks this reference");
                    }
                }
            }
        }
    }
      
}
