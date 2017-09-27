package org.eclipse.ceylon.compiler.typechecker.util;

import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.AnyAttribute;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.AnyMethod;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.ClassDefinition;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.Declaration;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.ExecutableStatement;

public class StatisticsVisitor extends Visitor {
    
    private int statements = 0;
    private int declarations = 0;
    private int classes = 0;
    private int methods = 0;
    private int attributes = 0;
    
    @Override
    public void visit(ExecutableStatement that) {
        statements++;
        super.visit(that);
    }
    
    @Override
    public void visit(Declaration that) {
        declarations++;
        super.visit(that);
    }
    
    @Override
    public void visit(ClassDefinition that) {
        classes++;
        super.visit(that);
    }

    @Override
    public void visit(AnyMethod that) {
        methods++;
        super.visit(that);
    }

    @Override
    public void visit(AnyAttribute that) {
        attributes++;
        super.visit(that);
    }
    
    public void print() {
        System.out.println(statements + " statements, " + 
                        declarations + " declarations, " + 
                        classes + " classes, " + 
                        methods + " functions, " + 
                        attributes + " values");
    }
    
}
