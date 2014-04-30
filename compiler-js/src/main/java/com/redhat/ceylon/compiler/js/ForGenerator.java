package com.redhat.ceylon.compiler.js;

import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;

public class ForGenerator {

    final GenerateJsVisitor gen;
    private final Set<Declaration> directAccess;

    ForGenerator(GenerateJsVisitor generator, Set<Declaration> directAccess) {
        gen = generator;
        this.directAccess = directAccess;
    }

    void generate(final Tree.ForStatement that) {
        if (gen.opts.isComment() && !gen.opts.isMinify()) {
            gen.out("//'for' statement at ", that.getUnit().getFilename(), " (", that.getLocation(), ")");
            if (that.getExits()) gen.out("//EXITS!");
            gen.endLine();
        }
        final Tree.ForIterator foriter = that.getForClause().getForIterator();
        final String itemVar = generateForLoop(foriter);

        boolean hasElse = that.getElseClause() != null && !that.getElseClause().getBlock().getStatements().isEmpty();
        gen.visitStatements(that.getForClause().getBlock().getStatements());
        //If there's an else block, check for normal termination
        gen.endBlock();
        if (hasElse) {
            gen.endLine();
            gen.out("if(", GenerateJsVisitor.getClAlias(), "getFinished() === ", itemVar, ")");
            gen.encloseBlockInFunction(that.getElseClause().getBlock());
        }
    }

    /** Generates code for the beginning of a "for" loop, returning the name of the variable used for the item. */
    private String generateForLoop(Tree.ForIterator that) {
        Tree.SpecifierExpression iterable = that.getSpecifierExpression();
        final String iterVar = gen.getNames().createTempVariable();
        final String itemVar;
        if (that instanceof Tree.ValueIterator) {
            itemVar = gen.getNames().name(((Tree.ValueIterator)that).getVariable().getDeclarationModel());
        } else {
            itemVar = gen.getNames().createTempVariable();
        }
        gen.out("var ", itemVar, ";for(var ", iterVar,"=");
        iterable.visit(gen);
        gen.out(".iterator();(", itemVar, "=", iterVar, ".next())!==", GenerateJsVisitor.getClAlias(), "getFinished();)");
        gen.beginBlock();
        if (that instanceof Tree.ValueIterator) {
            directAccess.add(((Tree.ValueIterator)that).getVariable().getDeclarationModel());
        } else if (that instanceof Tree.KeyValueIterator) {
            String keyvar = gen.getNames().name(((Tree.KeyValueIterator)that).getKeyVariable().getDeclarationModel());
            String valvar = gen.getNames().name(((Tree.KeyValueIterator)that).getValueVariable().getDeclarationModel());
            gen.out("var ", keyvar, "=", itemVar, ".key");
            gen.endLine(true);
            gen.out("var ", valvar, "=", itemVar, ".item");
            gen.endLine(true);
            directAccess.add(((Tree.KeyValueIterator)that).getKeyVariable().getDeclarationModel());
            directAccess.add(((Tree.KeyValueIterator)that).getValueVariable().getDeclarationModel());
        }
        return itemVar;
    }

}
