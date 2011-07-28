package com.redhat.ceylon.compiler.codegen;

import static com.sun.tools.javac.code.Flags.FINAL;

import com.redhat.ceylon.compiler.typechecker.tree.NaturalVisitor;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;

class StatementVisitor extends AbstractVisitor<JCStatement> implements NaturalVisitor {

    StatementVisitor(CeylonTransformer gen) {
        super(gen);
    }

    public void visit(Tree.InvocationExpression expr) {
        append(at(expr).Exec(expressionGen.transform(expr)));
    }

    public void visit(Tree.Return ret) {
        append(statementGen.transform(ret));
    }

    public void visit(Tree.IfStatement stat) {
        appendList(statementGen.transform(stat));
    }

    public void visit(Tree.WhileStatement stat) {
        appendList(statementGen.transform(stat));
    }

//    public void visit(Tree.DoWhileStatement stat) {
//        append(statementGen.transform(stat));
//    }

    public void visit(Tree.ForStatement stat) {
        appendList(statementGen.transform(stat));
    }

    public void visit(Tree.Break stat) {
        appendList(statementGen.transform(stat));
    }

    public void visit(Tree.AttributeDeclaration decl) {
        append(statementGen.transform(decl));
    }

    public void visit(Tree.SpecifierStatement op) {
        append(statementGen.transform(op));
    }

    // FIXME: not sure why we don't have just an entry for Tree.Term here...
    public void visit(Tree.OperatorExpression op) {
        append(at(op).Exec(expressionGen.transformExpression(op)));
    }

    public void visit(Tree.Expression tree) {
        append(at(tree).Exec(expressionGen.transformExpression(tree)));
    }

    public void visit(Tree.MethodDefinition decl) {
        JCTree.JCClassDecl innerDecl = classGen.methodClass(decl);
        append(innerDecl);
        JCTree.JCIdent name = make().Ident(innerDecl.name);
        JCVariableDecl call = at(decl).VarDef(
                make().Modifiers(FINAL),
                names().fromString(decl.getIdentifier().getText()),
                name,
                at(decl).NewClass(null, null, name, List.<JCTree.JCExpression>nil(), null));
        append(call);
    }

    @Override
    public void visit(Tree.ObjectDefinition that) {
        // TODO
        super.visit(that);
    }

    // FIXME: I think those should just go in transformExpression no?
    public void visit(Tree.PostfixOperatorExpression expr) {
        append(at(expr).Exec(expressionGen.transform(expr)));
    }

    public void visit(Tree.PrefixOperatorExpression expr) {
        append(at(expr).Exec(expressionGen.transform(expr)));
    }

    public void visit(Tree.ExpressionStatement tree) {
        append(at(tree).Exec(expressionGen.transformExpression(tree.getExpression())));
    }
}
