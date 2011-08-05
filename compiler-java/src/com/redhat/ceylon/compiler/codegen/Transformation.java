package com.redhat.ceylon.compiler.codegen;

import com.redhat.ceylon.compiler.loader.CeylonModelLoader;
import com.redhat.ceylon.compiler.loader.TypeFactory;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.JCTree.Factory;
import com.sun.tools.javac.util.Name;

public interface Transformation {

    public abstract TreeMaker make();

    public abstract Factory at(Node node);

    public abstract Symtab syms();

    public abstract Name.Table names();

    public abstract CeylonModelLoader loader();

    public abstract TypeFactory typeFact();

    public abstract CeylonTransformer gen();

    public abstract ExpressionTransformer expressionGen();

    public abstract StatementTransformer statementGen();

    public abstract ClassTransformer classGen();

    public abstract GlobalTransformer globalGen();

}