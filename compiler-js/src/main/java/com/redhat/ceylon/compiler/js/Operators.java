package com.redhat.ceylon.compiler.js;

import java.util.Map;

import com.redhat.ceylon.compiler.js.util.TypeUtils;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.SiteVariance;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;

public class Operators {

    static void unwrappedNumberOrTerm(final Tree.Term term, final boolean wrapFloat, final GenerateJsVisitor gen) {
        if (term instanceof Tree.NaturalLiteral) {
            gen.out("(", Long.toString(gen.parseNaturalLiteral((Tree.NaturalLiteral)term, false)), ")");
        } else if (term instanceof Tree.FloatLiteral) {
            if (wrapFloat) {
                gen.out(gen.getClAlias(), "Float(", term.getText(), ")");
            } else {
                gen.out("(", term.getText(), ")");
            }
        } else {
            gen.box(term);
        }
    }
    static void simpleBinaryOp(final Tree.BinaryOperatorExpression exp,
            final String before, final String op, final String after, final GenerateJsVisitor gen) {
        if (before != null) {
            gen.out(before);
        }
        if (op.charAt(0)!='.' && exp.getLeftTerm() instanceof Tree.NaturalLiteral) {
            gen.out("(", Long.toString(gen.parseNaturalLiteral((Tree.NaturalLiteral)exp.getLeftTerm(), false)), ")");
        } else {
            unwrappedNumberOrTerm(exp.getLeftTerm(), ".divided(".equals(op), gen);
        }
        gen.out(op);
        unwrappedNumberOrTerm(exp.getRightTerm(), ".divided(".equals(op), gen);
        if (after != null) {
            gen.out(after);
        }
    }

    static void genericBinaryOp(final Tree.BinaryOperatorExpression exp, final String op,
            final Map<TypeParameter, Type> targs, final Map<TypeParameter, SiteVariance> overrides,
            final GenerateJsVisitor gen) {
        if (op.charAt(0)!='.' && exp.getLeftTerm() instanceof Tree.NaturalLiteral) {
            gen.out(Long.toString(gen.parseNaturalLiteral((Tree.NaturalLiteral)exp.getLeftTerm(), false)));
        } else {
            gen.box(exp.getLeftTerm());
        }
        gen.out(op);
        if (exp.getRightTerm() instanceof Tree.NaturalLiteral) {
            gen.out(Long.toString(gen.parseNaturalLiteral((Tree.NaturalLiteral)exp.getRightTerm(), false)));
        } else {
            gen.box(exp.getRightTerm());
        }
        if (targs != null) {
            gen.out(",");
            TypeUtils.printTypeArguments(exp, targs, gen, false, overrides);
        }
        gen.out(")");
    }

    static void unaryOp(final Tree.UnaryOperatorExpression exp, final String before, final String after,
            final GenerateJsVisitor gen) {
        if (before != null) {
            gen.out(before);
        }
        if ((after==null || after.charAt(0)!='.' ) && exp.getTerm() instanceof Tree.NaturalLiteral) {
            gen.out(Long.toString(gen.parseNaturalLiteral((Tree.NaturalLiteral)exp.getTerm(), false)));
        } else {
            final int boxTypeLeft = gen.boxStart(exp.getTerm());
            if (exp.getTerm() instanceof Tree.BaseMemberExpression) {
                BmeGenerator.generateBme((Tree.BaseMemberExpression)exp.getTerm(), gen,
                        !(exp instanceof Tree.Exists));
            } else {
                exp.getTerm().visit(gen);
            }
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

    static void indexOp(final Tree.IndexExpression that, final GenerateJsVisitor gen) {
        that.getPrimary().visit(gen);
        Tree.ElementOrRange eor = that.getElementOrRange();
        if (eor instanceof Tree.Element) {
            final Tree.Expression _elemexpr = ((Tree.Element)eor).getExpression();
            final String _end;
            if (gen.isInDynamicBlock() && ModelUtil.isTypeUnknown(that.getPrimary().getTypeModel())) {
                gen.out("[");
                _end = "]";
            } else {
                gen.out(".$_get(");
                _end = ")";
            }
            if (!gen.isNaturalLiteral(_elemexpr.getTerm())) {
                _elemexpr.visit(gen);
            }
            gen.out(_end);
        } else {//range, or spread?
            Tree.ElementRange er = (Tree.ElementRange)eor;
            Expression sexpr = er.getLength();
            if (sexpr == null) {
                if (er.getLowerBound() == null) {
                    gen.out(".spanTo(");
                } else if (er.getUpperBound() == null) {
                    gen.out(".spanFrom(");
                } else {
                    gen.out(".span(");
                }
            } else {
                gen.out(".measure(");
            }
            if (er.getLowerBound() != null) {
                if (!gen.isNaturalLiteral(er.getLowerBound().getTerm())) {
                    er.getLowerBound().visit(gen);
                }
                if (er.getUpperBound() != null || sexpr != null) {
                    gen.out(",");
                }
            }
            if (er.getUpperBound() != null) {
                if (!gen.isNaturalLiteral(er.getUpperBound().getTerm())) {
                    er.getUpperBound().visit(gen);
                }
            } else if (sexpr != null) {
                sexpr.visit(gen);
            }
            gen.out(")");
        }
    }

    static void segmentOrRange(final Tree.BinaryOperatorExpression that,
            String op, String typeArgumentName, GenerateJsVisitor gen) {
        final Tree.Term left  = that.getLeftTerm();
        final Tree.Term right = that.getRightTerm();
        gen.out(gen.getClAlias(), op, "(");
        left.visit(gen);
        gen.out(",");
        right.visit(gen);
        gen.out(",{", typeArgumentName, "$", op, ":");
        TypeUtils.typeNameOrList(that,
                ModelUtil.unionType(left.getTypeModel(), right.getTypeModel(), that.getUnit()),
                gen, false);
        gen.out("})");
    }

    static void withinOp(final Tree.WithinOp that, GenerateJsVisitor gen) {
        final String ttmp = gen.getNames().createTempVariable();
        gen.out("(", ttmp, "=");
        gen.box(that.getTerm());
        gen.out(",");
        if (gen.isInDynamicBlock() && ModelUtil.isTypeUnknown(that.getTerm().getTypeModel())) {
            final String tmpl = gen.getNames().createTempVariable();
            final String tmpu = gen.getNames().createTempVariable();
            gen.out(tmpl, "=");
            gen.box(that.getLowerBound().getTerm());
            gen.out(",", tmpu, "=");
            gen.box(that.getUpperBound().getTerm());
            gen.out(",(", gen.getClAlias(), "nn$(", ttmp,")&&");
            if (that.getLowerBound() instanceof Tree.OpenBound) {
                gen.out(ttmp, ".largerThan&&", ttmp, ".largerThan(", tmpl, ")||", ttmp, ">", tmpl, ")");
            } else {
                gen.out(ttmp, ".notSmallerThan&&", ttmp, ".notSmallerThan(", tmpl, ")||", ttmp, ">=", tmpl, ")");
            }
            gen.out("&&(", gen.getClAlias(), "nn$(", ttmp,")&&");
            if (that.getUpperBound() instanceof Tree.OpenBound) {
                gen.out(ttmp, ".smallerThan&&", ttmp, ".smallerThan(", tmpu, ")||", ttmp, "<", tmpu, ")");
            } else {
                gen.out(ttmp, ".notLargerThan&&", ttmp, ".notLargerThan(", tmpu, ")||", ttmp, "<=", tmpu, ")");
            }
        } else {
            if (that.getLowerBound() instanceof Tree.OpenBound) {
                gen.out(ttmp, ".largerThan(");
            } else {
                gen.out(ttmp,".notSmallerThan(");
            }
            gen.box(that.getLowerBound().getTerm());
            gen.out(")&&");
            if (that.getUpperBound() instanceof Tree.OpenBound) {
                gen.out(ttmp, ".smallerThan(");
            } else {
                gen.out(ttmp, ".notLargerThan(");
            }
            gen.box(that.getUpperBound().getTerm());
            gen.out(")");
        }
        gen.out(")");
    }

    static void largeAs(Tree.LargeAsOp that, GenerateJsVisitor gen) {
        if (gen.isInDynamicBlock() && ModelUtil.isTypeUnknown(that.getLeftTerm().getTypeModel())) {
            //Try to use notSmallerThan() if it exists
            nativeBinaryOp(that, "notSmallerThan", ">=", null, gen);
        } else {
            final boolean usenat = gen.canUseNativeComparator(that.getLeftTerm(), that.getRightTerm());
            if (usenat) {
                simpleBinaryOp(that, "(", ">=", ")", gen);
            } else {
                simpleBinaryOp(that, null, ".notSmallerThan(", ")", gen);
            }
        }
    }

    static void smallAs(Tree.SmallAsOp that, GenerateJsVisitor gen) {
        if (gen.isInDynamicBlock() && ModelUtil.isTypeUnknown(that.getLeftTerm().getTypeModel())) {
            //Try to use notLargerThan() if it exists
            nativeBinaryOp(that, "notLargerThan", "<=", null, gen);
        } else {
            final boolean usenat = gen.canUseNativeComparator(that.getLeftTerm(), that.getRightTerm());
            if (usenat) {
                simpleBinaryOp(that, "(", "<=", ")", gen);
            } else {
                simpleBinaryOp(that, null, ".notLargerThan(", ")", gen);
            }
        }
    }

    static void larger(Tree.LargerOp that, GenerateJsVisitor gen) {
        if (gen.isInDynamicBlock() && ModelUtil.isTypeUnknown(that.getLeftTerm().getTypeModel())) {
            //Try to use largerThan() if it exists
            nativeBinaryOp(that, "largerThan", ">", null, gen);
        } else {
            final boolean usenat = gen.canUseNativeComparator(that.getLeftTerm(), that.getRightTerm());
            if (usenat) {
                simpleBinaryOp(that, "(", ">", ")", gen);
            } else {
                simpleBinaryOp(that, null, ".largerThan(", ")", gen);
            }
        }
    }

    static void smaller(Tree.SmallerOp that, GenerateJsVisitor gen) {
        if (gen.isInDynamicBlock() && ModelUtil.isTypeUnknown(that.getLeftTerm().getTypeModel())) {
            //Try to use smallerThan() if it exists
            nativeBinaryOp(that, "smallerThan", "<", null, gen);
        } else {
            final boolean usenat = gen.canUseNativeComparator(that.getLeftTerm(), that.getRightTerm());
            if (usenat) {
                simpleBinaryOp(that, "(", "<", ")", gen);
            } else {
                simpleBinaryOp(that, null, ".smallerThan(", ")", gen);
            }
        }
    }

    static void notEqual(Tree.NotEqualOp that, GenerateJsVisitor gen) {
        gen.out("!");
        builtInBinaryOp(that, "$eq$", gen);
    }

    static void equal(Tree.EqualOp that, GenerateJsVisitor gen) {
        if (gen.isInDynamicBlock() && ModelUtil.isTypeUnknown(that.getLeftTerm().getTypeModel())) {
            //Try to use equals() if it exists
            builtInBinaryOp(that, "$eq$", gen);
        } else {
            final boolean usenat = gen.canUseNativeComparator(that.getLeftTerm(), that.getRightTerm());
            if (usenat) {
                gen.out("(");
                that.getLeftTerm().visit(gen);
                gen.out("==");
                that.getRightTerm().visit(gen);
                gen.out(")");
            } else {
                builtInBinaryOp(that, "$eq$", gen);
            }
        }
    }

    static void builtInBinaryOp(final Tree.BinaryOperatorExpression that, final String builtInOp,
            final GenerateJsVisitor gen) {
        gen.out(gen.getClAlias(), builtInOp, "(");
        gen.box(that.getLeftTerm());
        gen.out(",");
        gen.box(that.getRightTerm());
        gen.out(")");
    }

    static void nativeBinaryOp(final Tree.BinaryOperatorExpression that,
            final String method, final String op, final String post, final GenerateJsVisitor gen) {
        //Try to use equals() if it exists
        String ltmp = gen.getNames().createTempVariable();
        String rtmp = gen.getNames().createTempVariable();
        gen.out("(", ltmp, "=");
        gen.box(that.getLeftTerm());
        gen.out(",", rtmp, "=");
        gen.box(that.getRightTerm());
        gen.out(",(", gen.getClAlias(), "nn$(", ltmp,")&&", ltmp, ".", method, "&&",
                ltmp, ".", method, "(", rtmp, ")");
        if (post != null) {
            gen.out(post);
        }
        gen.out(")||", ltmp, op, rtmp, ")");
    }

    static void neg(Tree.NegativeOp that, GenerateJsVisitor gen) {
        Tree.Term term = that.getTerm();
        if (term instanceof Tree.Expression) {
            term = ((Tree.Expression)term).getTerm();
        }
        if (term instanceof Tree.NaturalLiteral) {
            long t = gen.parseNaturalLiteral((Tree.NaturalLiteral)term, true);
            gen.out("(", Long.toString(t), ")");
            return;
        }
        final Type d = term.getTypeModel();
        final boolean useMinus = (d != null && d.isSubtypeOf(that.getUnit().getIntegerType()))
                || (gen.isInDynamicBlock() && ModelUtil.isTypeUnknown(d));
        Operators.unaryOp(that, useMinus?"(-":null, useMinus?")":".negated", gen);
    }

    static void generateSafeOp(final Tree.QualifiedMemberOrTypeExpression that,
            final GenerateJsVisitor gen) {
        boolean isMethod = that.getDeclaration() instanceof Function;
        String lhsVar = gen.createRetainedTempVar();
        gen.out("(", lhsVar, "=");
        gen.supervisit(that);
        gen.out(",");
        if (isMethod) {
            gen.out(gen.getClAlias(), "jsc$3(", lhsVar, ",");
        }
        gen.out(gen.getClAlias(),"nn$(", lhsVar, ")?");
        if (isMethod && !that.getDeclaration().getTypeParameters().isEmpty()) {
            //Function ref with type parameters
            BmeGenerator.printGenericMethodReference(gen, that, lhsVar, gen.memberAccess(that, lhsVar));
        } else {
            gen.out(gen.memberAccess(that, lhsVar));
        }
        gen.out(":null)");
        if (isMethod) {
            gen.out(")");
        }
    }

}
