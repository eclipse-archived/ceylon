package com.redhat.ceylon.compiler.codegen;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree.Factory;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Name;

public class GenPart {
    protected Gen2 gen;

    public GenPart(Gen2 gen) {
        this.gen = gen;
    }

    protected Factory at(Node node) {
        return gen.at(node);
    }

    protected JCExpression makeIdent(String ident) {
        return gen.makeIdent(ident);
    }

    protected JCExpression makeIdent(Iterable<String> ident) {
        return gen.makeIdent(ident);
    }

    protected JCExpression makeIdent(String... ident) {
        return gen.makeIdent(ident);
    }

    protected JCExpression makeIdent(Type type) {
        return gen.makeIdent(type);
    }

    protected TreeMaker make() {
        return gen.make();
    }

    protected Symtab syms() {
        return gen.syms;
    }

    protected JCFieldAccess makeSelect(JCExpression s1, String s2) {
        return gen.makeSelect(s1, s2);
    }

    protected JCFieldAccess makeSelect(String s1, String s2) {
        return makeSelect(make().Ident(names().fromString(s1)), s2);
    }

    protected Name.Table names() {
        return gen.names;
    }

    protected String tempName() {
        return gen.tempName();
    }

    protected String tempName(String prefix) {
        return gen.tempName(prefix);
    }
	
    protected boolean hasCompilerAnnotation(Tree.Declaration decl, String annotation) {
        if (decl.getAnnotationList() == null)
            return false;
        for (Tree.Annotation a : decl.getAnnotationList().getAnnotations()) {
            if (!(a.getPrimary() instanceof Tree.BaseMemberExpression))
                throw new RuntimeException("Invalid annotation primary: " + a.getPrimary().getNodeType());
            Tree.BaseMemberExpression member = (Tree.BaseMemberExpression) a.getPrimary();
            if (member.getIdentifier().getText().equals(annotation))
                return true;
        }
        return false;
    }
}
