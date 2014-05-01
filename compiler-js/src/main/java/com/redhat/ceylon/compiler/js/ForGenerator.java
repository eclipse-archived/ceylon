package com.redhat.ceylon.compiler.js;

import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NaturalLiteral;

public class ForGenerator {

    final GenerateJsVisitor gen;
    private final Set<Declaration> directAccess;
    private final String iterVar;

    ForGenerator(GenerateJsVisitor generator, Set<Declaration> directAccess) {
        gen = generator;
        this.directAccess = directAccess;
        iterVar = gen.getNames().createTempVariable();
    }

    void generate(final Tree.ForStatement that) {
        if (gen.opts.isComment() && !gen.opts.isMinify()) {
            gen.out("//'for' statement at ", that.getUnit().getFilename(), " (", that.getLocation(), ")");
            if (that.getExits()) gen.out("//EXITS!");
            gen.endLine();
        }
        final Tree.ForIterator foriter = that.getForClause().getForIterator();
        boolean hasElse = that.getElseClause() != null && !that.getElseClause().getBlock().getStatements().isEmpty();
        final String itemVar = generateForLoop(foriter, hasElse);

        gen.visitStatements(that.getForClause().getBlock().getStatements());
        //If there's an else block, check for normal termination
        gen.endBlock();
        if (hasElse) {
            gen.endLine();
            gen.out("if(", GenerateJsVisitor.getClAlias(), "getFinished()", "===", itemVar, ")");
            gen.encloseBlockInFunction(that.getElseClause().getBlock());
        }
    }

    /** Generates code for the beginning of a "for" loop, returning the name of the variable used for the item. */
    private String generateForLoop(Tree.ForIterator that, boolean hasElse) {
        final Tree.SpecifierExpression iterable = that.getSpecifierExpression();
        final String itemVar;
        if (that instanceof Tree.ValueIterator) {
            itemVar = gen.getNames().name(((Tree.ValueIterator)that).getVariable().getDeclarationModel());
        } else {
            itemVar = gen.getNames().createTempVariable();
        }
        if (hasElse || !optimize(iterable, itemVar)) {
            gen.out("var ", itemVar, ";for(var ", iterVar,"=");
            iterable.visit(gen);
            gen.out(".iterator();(", itemVar, "=", iterVar, ".next())!==",
                    GenerateJsVisitor.getClAlias(), "getFinished();)");
        }
        gen.beginBlock();
        if (that instanceof Tree.ValueIterator) {
            directAccess.add(((Tree.ValueIterator)that).getVariable().getDeclarationModel());
        } else if (that instanceof Tree.KeyValueIterator) {
            String keyvar = gen.getNames().name(((Tree.KeyValueIterator)that).getKeyVariable().getDeclarationModel());
            String valvar = gen.getNames().name(((Tree.KeyValueIterator)that).getValueVariable().getDeclarationModel());
            gen.out("var ", keyvar, "=", itemVar, ".key,", valvar, "=", itemVar, ".item");
            gen.endLine(true);
            directAccess.add(((Tree.KeyValueIterator)that).getKeyVariable().getDeclarationModel());
            directAccess.add(((Tree.KeyValueIterator)that).getValueVariable().getDeclarationModel());
        }
        return itemVar;
    }

    boolean optimize(final Tree.SpecifierExpression iterable, final String itemVar) {
        final Tree.Term term = iterable.getExpression().getTerm();
        if (term instanceof Tree.RangeOp) {
            return optimizeRange((Tree.RangeOp)term, itemVar);
        } else if (term instanceof Tree.SegmentOp) {
            return optimizeSegment((Tree.SegmentOp)term, itemVar);
        }
        return false;
    }

    boolean optimizeRange(final Tree.RangeOp range, final String itemVar) {
        final Tree.Term left = range.getLeftTerm();
        final Tree.Term right = range.getRightTerm();
        if (left instanceof Tree.NaturalLiteral && right instanceof NaturalLiteral) {
            try {
                long una = gen.parseNaturalLiteral((Tree.NaturalLiteral)left);
                long dos = gen.parseNaturalLiteral((Tree.NaturalLiteral)right);
                optimizeNaturals(una, dos, itemVar);
                return true;
            } catch (NumberFormatException ex) {
                return false;
            }
        }
        gen.out("var ", itemVar, "=");
        left.visit(gen);
        gen.out(",", iterVar, "=");
        right.visit(gen);
        final String cmpvar = gen.getNames().createTempVariable();
        final String nxtvar = gen.getNames().createTempVariable();
        gen.out(",", cmpvar, "=", itemVar, ".compare(", iterVar, "),", nxtvar, "=", cmpvar, "===",
                GenerateJsVisitor.getClAlias(), "getSmaller()?'successor':'predecessor';for(;");
        gen.out(iterVar, ".compare(", itemVar, ")!==", cmpvar, ";");
        gen.out(itemVar, "=", itemVar, "[", nxtvar, "])");
        return true;
    }

    boolean optimizeSegment(Tree.SegmentOp that, final String itemVar) {
        final Tree.Term left = that.getLeftTerm();
        final Tree.Term right = that.getRightTerm();
        final boolean leftNat = left instanceof Tree.NaturalLiteral;
        final boolean rightNat = right instanceof Tree.NaturalLiteral;
        if (leftNat && rightNat) {
            try {
                long una = gen.parseNaturalLiteral((Tree.NaturalLiteral)left);
                long dos = gen.parseNaturalLiteral((Tree.NaturalLiteral)right);
                optimizeNaturals(una, una+dos-1, itemVar);
                return true;
            } catch (NumberFormatException ex) {
                return false;
            }
        }
        final String limvar = rightNat ? Long.toString(gen.parseNaturalLiteral((Tree.NaturalLiteral)right))
                : gen.getNames().createTempVariable();
        gen.out("var ", itemVar, "=");
        left.visit(gen);
        if (!rightNat) {
            gen.out(",", limvar, "=");
            right.visit(gen);
        }
        gen.out(";");
        final String tmpvar = gen.getNames().createTempVariable();
        gen.out("for(var ", tmpvar, "=0;", tmpvar,"<", limvar, ";", tmpvar, "++,(", itemVar, "=", itemVar, ".successor))");
        return true;
    }

    void optimizeNaturals(final long from, final long to, final String itemVar) {
        final boolean menor = from<to;
        gen.out("for(var ",itemVar,"=", Long.toString(from), ";", itemVar,
                menor ? "<=" : ">=", Long.toString(to), ";", itemVar,
                menor ? "++" : "--", ")");
    }

}
