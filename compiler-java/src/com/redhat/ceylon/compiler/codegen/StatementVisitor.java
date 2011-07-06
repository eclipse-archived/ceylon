package com.redhat.ceylon.compiler.codegen;

import com.redhat.ceylon.compiler.typechecker.tree.NaturalVisitor;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

import static com.sun.tools.javac.code.Flags.FINAL;

class StatementVisitor extends Visitor implements NaturalVisitor {
    final ListBuffer<JCTree.JCStatement> stmts = ListBuffer.lb();
    private StatementGen statementGen;
    private ExpressionGen expressionGen;

    StatementVisitor(StatementGen statementGen) {
        this.statementGen = statementGen;
        this.expressionGen = statementGen.gen.expressionGen;
    }

    public ListBuffer<JCTree.JCStatement> stmts() {
        return stmts;
    }

    public void visit(Tree.InvocationExpression expr) {
        append(statementGen.at(expr).Exec(expressionGen.convert(expr)));
    }

    public void visit(Tree.Return ret) {
        append(statementGen.convert(ret));
    }

    public void visit(Tree.IfStatement stat) {
        append(statementGen.convert(stat));
    }

    public void visit(Tree.WhileStatement stat) {
        append(statementGen.convert(stat));
    }

    public void visit(Tree.DoWhileStatement stat) {
        append(statementGen.convert(stat));
    }

    public void visit(Tree.ForStatement stat) {
        append(statementGen.convert(stat));
    }

    public void visit(Tree.Break stat) {
        append(statementGen.convert(stat));
    }

    public void visit(Tree.AttributeDeclaration decl) {
        append(statementGen.convert(decl));
    }

    public void visit(Tree.SpecifierStatement op) {
        append(statementGen.convert(op));
    }

    // FIXME: not sure why we don't have just an entry for Tree.Term here...
    public void visit(Tree.OperatorExpression op) {
        append(statementGen.at(op).Exec(expressionGen.convertExpression(op)));
    }

    public void visit(Tree.Expression tree) {
        append(statementGen.at(tree).Exec(expressionGen.convertExpression(tree)));
    }

    public void visit(Tree.MethodDefinition decl) {
        JCTree.JCClassDecl innerDecl = statementGen.gen.classGen.methodClass(decl);
        append(innerDecl);
        JCTree.JCIdent name = statementGen.make().Ident(innerDecl.name);
        JCVariableDecl call = statementGen.at(decl).VarDef(
                statementGen.make().Modifiers(FINAL),
                statementGen.names().fromString(decl.getIdentifier().getText()),
                name,
                statementGen.at(decl).NewClass(null, null, name, List.<JCTree.JCExpression>nil(), null));
        append(call);
    }

    @Override
    public void visit(Tree.ObjectDefinition that) {
        // TODO
        super.visit(that);
    }

    // FIXME: I think those should just go in convertExpression no?
    public void visit(Tree.PostfixOperatorExpression expr) {
        append(statementGen.at(expr).Exec(expressionGen.convert(expr)));
    }

    public void visit(Tree.PrefixOperatorExpression expr) {
        append(statementGen.at(expr).Exec(expressionGen.convert(expr)));
    }

    public void visit(Tree.ExpressionStatement tree) {
        append(statementGen.at(tree).Exec(expressionGen.convertExpression(tree.getExpression())));
    }

    private void append(JCTree.JCStatement stmt) {
        stmts.append(stmt);
    }

    private void append(List<JCTree.JCStatement> list) {
        stmts.appendList(list);
    }
}
