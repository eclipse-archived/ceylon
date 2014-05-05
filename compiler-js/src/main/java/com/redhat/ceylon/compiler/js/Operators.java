package com.redhat.ceylon.compiler.js;

import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;

public class Operators {

    static void simpleBinaryOp(final Tree.BinaryOperatorExpression exp,
            final String before, final String op, final String after, final GenerateJsVisitor gen) {
        if (before != null) {
            gen.out(before);
        }
        if (op.charAt(0)!='.' && exp.getLeftTerm() instanceof Tree.NaturalLiteral) {
            gen.out(Long.toString(gen.parseNaturalLiteral((Tree.NaturalLiteral)exp.getLeftTerm())));
        } else {
            gen.box(exp.getLeftTerm());
        }
        gen.out(op);
        if (exp.getRightTerm() instanceof Tree.NaturalLiteral) {
            gen.out(Long.toString(gen.parseNaturalLiteral((Tree.NaturalLiteral)exp.getRightTerm())));
        } else {
            gen.box(exp.getRightTerm());
        }
        if (after != null) {
            gen.out(after);
        }
    }

    static void genericBinaryOp(final Tree.BinaryOperatorExpression exp, final String op,
            final Map<TypeParameter, ProducedType> targs, final GenerateJsVisitor gen) {
        if (op.charAt(0)!='.' && exp.getLeftTerm() instanceof Tree.NaturalLiteral) {
            gen.out(Long.toString(gen.parseNaturalLiteral((Tree.NaturalLiteral)exp.getLeftTerm())));
        } else {
            gen.box(exp.getLeftTerm());
        }
        gen.out(op);
        if (exp.getRightTerm() instanceof Tree.NaturalLiteral) {
            gen.out(Long.toString(gen.parseNaturalLiteral((Tree.NaturalLiteral)exp.getRightTerm())));
        } else {
            gen.box(exp.getRightTerm());
        }
        if (targs != null) {
            gen.out(",");
            TypeUtils.printTypeArguments(exp, targs, gen, false);
        }
        gen.out(")");
    }

    static void unaryOp(final Tree.UnaryOperatorExpression exp, final String before, final String after,
            final GenerateJsVisitor gen) {
        if (before != null) {
            gen.out(before);
        }
        if ((after==null || after.charAt(0)!='.' ) && exp.getTerm() instanceof Tree.NaturalLiteral) {
            gen.out(Long.toString(gen.parseNaturalLiteral((Tree.NaturalLiteral)exp.getTerm())));
        } else {
            final int boxTypeLeft = gen.boxStart(exp.getTerm());
            exp.getTerm().visit(gen);
            if (boxTypeLeft == 4) gen.out("/*TODO: callable targs 9*/");
            gen.boxUnboxEnd(boxTypeLeft);
        }
        if (after != null) {
            gen.out(after);
        }
    }

    static void prefixIncrementOrDecrement(Tree.Term term, String functionName, final GenerateJsVisitor gen) {
        if (term instanceof Tree.BaseMemberExpression) {
            Tree.BaseMemberExpression bme = (Tree.BaseMemberExpression) term;
            boolean simpleSetter = gen.hasSimpleGetterSetter(bme.getDeclaration());
            String getMember = gen.memberAccess(bme, null);
            String applyFunc = String.format("%s.%s", getMember, functionName);
            gen.out("(");
            BmeGenerator.generateMemberAccess(bme, applyFunc, null, gen);
            if (!simpleSetter) { gen.out(",", getMember); }
            gen.out(")");
            
        } else if (term instanceof Tree.QualifiedMemberExpression) {
            Tree.QualifiedMemberExpression qme = (Tree.QualifiedMemberExpression) term;
            String primaryVar = gen.createRetainedTempVar();
            String getMember = gen.memberAccess(qme, primaryVar);
            String applyFunc = String.format("%s.%s", getMember, functionName);
            gen.out("(", primaryVar, "=");
            qme.getPrimary().visit(gen);
            gen.out(",");
            BmeGenerator.generateMemberAccess(qme, applyFunc, primaryVar, gen);
            if (!gen.hasSimpleGetterSetter(qme.getDeclaration())) {
                gen.out(",", getMember);
            }
            gen.out(")");
        }
    }

    static void postfixIncrementOrDecrement(Tree.Term term, String functionName, final GenerateJsVisitor gen) {
        if (term instanceof Tree.BaseMemberExpression) {
            Tree.BaseMemberExpression bme = (Tree.BaseMemberExpression) term;
            if (bme.getDeclaration() == null && gen.isInDynamicBlock()) {
                gen.out(bme.getIdentifier().getText(), "successor".equals(functionName) ? "++" : "--");
                return;
            }
            String oldValueVar = gen.createRetainedTempVar();
            String applyFunc = String.format("%s.%s", oldValueVar, functionName);
            gen.out("(", oldValueVar, "=", gen.memberAccess(bme, null), ",");
            BmeGenerator.generateMemberAccess(bme, applyFunc, null, gen);
            gen.out(",", oldValueVar, ")");

        } else if (term instanceof Tree.QualifiedMemberExpression) {
            Tree.QualifiedMemberExpression qme = (Tree.QualifiedMemberExpression) term;
            if (qme.getDeclaration() == null && gen.isInDynamicBlock()) {
                gen.out(qme.getIdentifier().getText(), "successor".equals(functionName) ? "++" : "--");
                return;
            }
            String primaryVar = gen.createRetainedTempVar();
            String oldValueVar = gen.createRetainedTempVar();
            String applyFunc = String.format("%s.%s", oldValueVar, functionName);
            gen.out("(", primaryVar, "=");
            qme.getPrimary().visit(gen);
            gen.out(",", oldValueVar, "=", gen.memberAccess(qme, primaryVar), ",");
            BmeGenerator.generateMemberAccess(qme, applyFunc, primaryVar, gen);
            gen.out(",", oldValueVar, ")");
        }
    }

}
