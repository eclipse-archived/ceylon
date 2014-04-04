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
        gen.box(exp.getLeftTerm());
        gen.out(op);
        gen.box(exp.getRightTerm());
        if (after != null) {
            gen.out(after);
        }
    }

    static void genericBinaryOp(final Tree.BinaryOperatorExpression exp, final String op,
            final Map<TypeParameter, ProducedType> targs, final GenerateJsVisitor gen) {
        gen.box(exp.getLeftTerm());
        gen.out(op);
        gen.box(exp.getRightTerm());
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
        final int boxTypeLeft = gen.boxStart(exp.getTerm());
        exp.getTerm().visit(gen);
        if (boxTypeLeft == 4) gen.out("/*TODO: callable targs 9*/");
        gen.boxUnboxEnd(boxTypeLeft);
        if (after != null) {
            gen.out(after);
        }
    }
}
