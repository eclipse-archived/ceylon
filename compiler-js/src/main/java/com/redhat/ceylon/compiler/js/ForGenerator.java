package com.redhat.ceylon.compiler.js;

import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NaturalLiteral;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Value;

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
        final Set<Value> caps = new HashSet<>();
        final String itemVar = generateForLoop(foriter, hasElse, caps);
        gen.encloseBlockInFunction(that.getForClause().getBlock(), false, caps);
        //If there's an else block, check for normal termination
        gen.endBlock();
        if (hasElse) {
            gen.endLine();
            gen.out("if(", gen.getClAlias(), "finished()", "===", itemVar, ")");
            gen.encloseBlockInFunction(that.getElseClause().getBlock(), true, null);
        }
    }

    /** Generates code for the beginning of a "for" loop, returning the name of the variable used for the item. */
    private String generateForLoop(Tree.ForIterator that, boolean hasElse, Set<Value> capturedValues) {
        final Tree.SpecifierExpression iterable = that.getSpecifierExpression();
        final String itemVar;
        boolean captured=false;
        if (that instanceof Tree.ValueIterator) {
            final Value val = ((Tree.ValueIterator)that).getVariable().getDeclarationModel();
            captured=val.isCaptured();
            if (captured) {
                itemVar = gen.getNames().createTempVariable();
                capturedValues.add(val);
            } else {
                itemVar = gen.getNames().name(val);
            }
        } else {
            itemVar = gen.getNames().createTempVariable();
        }
        boolean isNative=iterateNative(iterable.getExpression().getTerm(), itemVar);
        if (!isNative) {
            if (hasElse || !optimize(iterable, itemVar)) {
                gen.out("var ", itemVar, ";for(var ", iterVar,"=");
                iterable.visit(gen);
                gen.out(".iterator();(", itemVar, "=", iterVar, ".next())!==",
                        gen.getClAlias(), "finished();)");
            }
            gen.beginBlock();
        }
        if (that instanceof Tree.ValueIterator) {
            if (captured) {
                gen.out("var ", gen.getNames().name(((Tree.ValueIterator)that).getVariable().getDeclarationModel()),
                        "=", itemVar, ";");
            }
            directAccess.add(((Tree.ValueIterator)that).getVariable().getDeclarationModel());
        } else if (that instanceof Tree.PatternIterator) {
            gen.out("var ");
            Destructurer d = new Destructurer(((Tree.PatternIterator) that).getPattern(), gen, directAccess,
                    itemVar, true);
            if (d.getCapturedValues() != null) {
                capturedValues.addAll(d.getCapturedValues());
            }
            gen.endLine(true);
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

    boolean iterateNative(final Tree.Term term, final String itemVar) {
        if (!(gen.isInDynamicBlock() && (term.getTypeModel() == null || term.getTypeModel().isUnknown()))) {
            return false;
        }
        final String termVar = gen.getNames().createTempVariable();
        gen.out("var ", termVar, "=");
        term.visit(gen);
        gen.out(";for(var ", iterVar,"=0;",iterVar,"<", termVar,
                ".length;",iterVar,"++){var ",itemVar,"=", termVar, "[",iterVar,"];");
        return true;
    }

    boolean optimizeRange(final Tree.RangeOp range, final String itemVar) {
        final Tree.Term left = range.getLeftTerm();
        final Tree.Term right = range.getRightTerm();
        if (left instanceof Tree.NaturalLiteral && right instanceof NaturalLiteral) {
            try {
                long una = gen.parseNaturalLiteral((Tree.NaturalLiteral)left, false);
                long dos = gen.parseNaturalLiteral((Tree.NaturalLiteral)right, false);
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
        final String cfvar = gen.getNames().createTempVariable();
        gen.out(",", cmpvar, "=", itemVar, ".compare(", iterVar, "),", nxtvar, "=", cmpvar, "===",
                gen.getClAlias(), "smaller()?'successor':'predecessor';for(var ",
                cfvar, "=", gen.getClAlias(), "eorl$(", cmpvar, ");", cfvar,
                "(", iterVar, ",", itemVar, ");");
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
                long una = gen.parseNaturalLiteral((Tree.NaturalLiteral)left, false);
                long dos = gen.parseNaturalLiteral((Tree.NaturalLiteral)right, false);
                optimizeNaturals(una, una+dos-1, itemVar);
                return true;
            } catch (NumberFormatException ex) {
                return false;
            }
        }
        final String limvar = rightNat ? Long.toString(gen.parseNaturalLiteral((Tree.NaturalLiteral)right, false))
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
