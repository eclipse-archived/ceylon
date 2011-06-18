package com.redhat.ceylon.compiler.codegen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.redhat.ceylon.compiler.codegen.Gen2.Singleton;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PostfixOperatorExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PrefixOperatorExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.util.Util;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

public class ExpressionGen extends GenPart {

    public ExpressionGen(Gen2 gen) {
        super(gen);
    }

    JCExpression convertExpression(final Tree.Term expr) {
        class V extends Visitor {
            public JCExpression result;

            public void visit(Tree.This expr) {
                at(expr);
                result = makeIdent("this");
            }

            public void visit(Tree.Super expr) {
                at(expr);
                result = makeIdent("super");
            }

            // FIXME: port dot operator?
            public void visit(Tree.NotEqualOp op) {
                result = convert(op);
            }

            public void visit(Tree.NotOp op) {
                result = convert(op);
            }

            public void visit(Tree.AssignOp op) {
                result = convert(op);
            }

            public void visit(Tree.IsOp op) {
                result = convert(op);
            }

            public void visit(Tree.RangeOp op) {
                result = convert(op);
            }

            public void visit(Tree.UnaryOperatorExpression op) {
                result = convert(op);
            }

            public void visit(Tree.BinaryOperatorExpression op) {
                result = convert(op);
            }

            public void visit(Tree.PrefixOperatorExpression op) {
                result = convert(op);
            }

            public void visit(Tree.PostfixOperatorExpression op) {
                result = convert(op);
            }

            // NB spec 1.3.11 says "There are only two types of numeric
            // literals: literals for Naturals and literals for Floats."
            public void visit(Tree.NaturalLiteral lit) {
                JCExpression n = make().Literal(Long.parseLong(lit.getText()));
                result = at(expr).Apply(null, makeSelect(makeIdent(syms().ceylonNaturalType), "instance"), List.of(n));
            }

            public void visit(Tree.FloatLiteral lit) {
                JCExpression n = make().Literal(Double.parseDouble(lit.getText()));
                result = at(expr).Apply(null, makeSelect(makeIdent(syms().ceylonFloatType), "instance"), List.of(n));
            }

            public void visit(Tree.CharLiteral lit) {
                JCExpression n = make().Literal(TypeTags.CHAR, (int) lit.getText().charAt(1));
                // XXX make().Literal(lit.value) doesn't work here... something
                // broken in javac?
                result = at(expr).Apply(null, makeSelect(makeIdent(syms().ceylonCharacterType), "instance"), List.of(n));
            }

            public void visit(Tree.StringLiteral string) {
                result = convert(string);
            }

            public void visit(Tree.InvocationExpression call) {
                result = convert(call);
            }

            // FIXME: port ReflectedLiteral?
            public void visit(Tree.BaseMemberExpression value) {
                result = convert(value);
            }

            public void visit(Tree.QualifiedMemberExpression value) {
                result = convert(value);
            }

            // FIXME: port TypeName?
            public void visit(Tree.InitializerExpression value) {
                result = convertExpression(value.getExpression());
            }

            // FIXME: port Null?
            // FIXME: port Condition?
            // FIXME: port Subscript?
            // FIXME: port LowerBoud?
            // FIXME: port EnumList?
            public void visit(Tree.StringTemplate expr) {
                result = convertStringExpression(expr);
            }
        }

        V v = new V();
        expr.visit(v);
        return v.result;
    }

    private JCExpression convertStringExpression(Tree.StringTemplate expr) {
        ListBuffer<JCExpression> strings = new ListBuffer<JCExpression>();
        for (Tree.Expression t : expr.getExpressions()) {
            strings.append(convertExpression(t));
        }

        return make().Apply(null, makeSelect(makeIdent(syms().ceylonStringType), "instance"), strings.toList());
    }

    private static Map<Class<? extends Tree.UnaryOperatorExpression>, String> unaryOperators;
    private static Map<Class<? extends Tree.BinaryOperatorExpression>, String> binaryOperators;

    static {
        unaryOperators = new HashMap<Class<? extends Tree.UnaryOperatorExpression>, String>();
        binaryOperators = new HashMap<Class<? extends Tree.BinaryOperatorExpression>, String>();

        // Unary operators
        unaryOperators.put(Tree.NegativeOp.class, "inverse");
        unaryOperators.put(Tree.NotOp.class, "complement");
        unaryOperators.put(Tree.FormatOp.class, "string");

        // Binary operators that act on types
        binaryOperators.put(Tree.SumOp.class, "plus");
        binaryOperators.put(Tree.DifferenceOp.class, "minus");
        binaryOperators.put(Tree.ProductOp.class, "times");
        binaryOperators.put(Tree.QuotientOp.class, "divided");
        binaryOperators.put(Tree.PowerOp.class, "power");
        binaryOperators.put(Tree.RemainderOp.class, "remainder");
        binaryOperators.put(Tree.IntersectionOp.class, "and");
        binaryOperators.put(Tree.UnionOp.class, "or");
        binaryOperators.put(Tree.XorOp.class, "xor");
        binaryOperators.put(Tree.EqualOp.class, "equalsXXX");
        binaryOperators.put(Tree.IdenticalOp.class, "identical");
        binaryOperators.put(Tree.CompareOp.class, "compare");

        // Binary operators that act on intermediary Comparison objects
        binaryOperators.put(Tree.LargerOp.class, "larger");
        binaryOperators.put(Tree.SmallerOp.class, "smaller");
        binaryOperators.put(Tree.LargeAsOp.class, "largeAs");
        binaryOperators.put(Tree.SmallAsOp.class, "smallAs");
    }

    // FIXME: I'm pretty sure sugar is not supposed to be in there
    private JCExpression convert(Tree.NotEqualOp op) {
        Tree.EqualOp newOp = new Tree.EqualOp(op.getAntlrTreeNode());
        newOp.setLeftTerm(op.getLeftTerm());
        newOp.setRightTerm(op.getRightTerm());
        Tree.NotOp newNotOp = new Tree.NotOp(op.getAntlrTreeNode());
        newNotOp.setTerm(newOp);
        return convert(newNotOp);
    }

    // FIXME: I'm pretty sure sugar is not supposed to be in there
    private JCExpression convert(Tree.NotOp op) {
        return at(op).Apply(null, makeSelect(makeIdent(syms().ceylonBooleanType), "instance"), List.<JCExpression> of(at(op).Conditional(convertExpression(op.getTerm()), make().Literal(TypeTags.BOOLEAN, 0), make().Literal(TypeTags.BOOLEAN, 1))));
    }

    private JCExpression convert(Tree.AssignOp op) {
        JCExpression rhs = convertExpression(op.getRightTerm());
        JCExpression lhs = convertExpression(op.getLeftTerm());
        return at(op).Apply(null, at(op).Select(lhs, names().fromString("set")), List.of(rhs));
    }

    private JCExpression convert(Tree.IsOp op) {
        // FIXME: this is only working for BaseTypeExpression
        // FIXME: Nasty cast here. We can't call convertExpression()operands[1])
        // because that returns TypeName.class, not simply TypeName.
        Tree.BaseTypeExpression name = (Tree.BaseTypeExpression) op.getRightTerm();
        return at(op).Apply(null, makeSelect(makeIdent(syms().ceylonBooleanType), "instance"), List.<JCExpression> of(at(op).TypeTest(convertExpression(op.getLeftTerm()), makeIdent(name.getIdentifier().getText()))));
    }

    private JCExpression convert(Tree.RangeOp op) {
        JCExpression lower = convertExpression(op.getLeftTerm());
        JCExpression upper = convertExpression(op.getRightTerm());
        return at(op).NewClass(null, null, at(op).TypeApply(makeIdent(syms().ceylonRangeType), List.<JCExpression> of(null)), List.<JCExpression> of(lower, upper), null);
    }

    JCExpression convert(Tree.UnaryOperatorExpression op) {
        Tree.Term term = op.getTerm();
        if (term instanceof Tree.NaturalLiteral && op instanceof Tree.NegativeOp) {
            Tree.NaturalLiteral lit = (Tree.NaturalLiteral) term;
            return at(op).Apply(null, makeSelect(makeIdent(syms().ceylonIntegerType), "instance"), List.<JCExpression> of(make().Literal(-Long.parseLong(lit.getText()))));
        }
        return at(op).Apply(null, at(op).Select(convertExpression(term), names().fromString(unaryOperators.get(op.getClass()))), List.<JCExpression> nil());
    }

    private JCExpression convert(Tree.BinaryOperatorExpression op) {
        JCExpression result = null;
        Class<? extends Tree.OperatorExpression> operatorClass = op.getClass();

        boolean loseComparison = op instanceof Tree.SmallAsOp || op instanceof Tree.SmallerOp || op instanceof Tree.LargerOp || op instanceof Tree.LargeAsOp;

        if (loseComparison)
            operatorClass = Tree.CompareOp.class;

        result = at(op).Apply(null, at(op).Select(convertExpression(op.getLeftTerm()), names().fromString(binaryOperators.get(operatorClass))), List.of(convertExpression(op.getRightTerm())));

        if (loseComparison) {
            result = at(op).Apply(null, at(op).Select(result, names().fromString(binaryOperators.get(op.getClass()))), List.<JCExpression> nil());
        }

        return result;
    }

    JCExpression convert(Tree.PostfixOperatorExpression expr) {
        String methodName;
        boolean successor;
        if (expr instanceof Tree.PostfixIncrementOp){
            successor = true;
            methodName = "getPredecessor";
        }else if (expr instanceof Tree.PostfixDecrementOp){
            successor = false;
            methodName = "getSuccessor";
        }else
            throw new RuntimeException("Not implemented: " + expr.getNodeType());
        JCExpression op = makePrefixOp(expr, expr.getPrimary(), successor);
        return at(expr).Apply(null, makeSelect(op, methodName), List.<JCExpression>nil());
    }

    private JCExpression convert(Tree.PrefixOperatorExpression expr) {
        boolean successor;
        if (expr instanceof Tree.IncrementOp)
            successor = true;
        else if (expr instanceof Tree.DecrementOp)
            successor = false;
        else
            throw new RuntimeException("Not implemented: " + expr.getNodeType());
        return makePrefixOp(expr, expr.getTerm(), successor);
    }

    private JCExpression makePrefixOp(Node expr, Term term, boolean successor) {
        String methodName;
        if (successor)
            methodName = "getSuccessor";
        else
            methodName = "getPredecessor";
        JCExpression operand = convertExpression(term);
        return at(expr).Assign(operand, at(expr).Apply(null, makeSelect(operand, methodName), List.<JCExpression>nil()));
    }

    JCExpression convert(Tree.InvocationExpression ce) {
        final Singleton<JCExpression> expr = new Singleton<JCExpression>();
        final ListBuffer<JCExpression> args = new ListBuffer<JCExpression>();

        for (Tree.PositionalArgument arg : ce.getPositionalArgumentList().getPositionalArguments())
            args.append(convertArg(arg));

        ce.getPrimary().visit(new Visitor() {
            public void visit(Tree.QualifiedMemberExpression access) {
                expr.append(convert(access));
            }

            public void visit(Tree.Type type) {
                // A constructor
                expr.append(at(type).NewClass(null, null, gen.convert(type), args.toList(), null));
            }

            public void visit(Tree.InvocationExpression chainedCall) {
                expr.append(convert(chainedCall));
            }

            public void visit(Tree.BaseMemberExpression access) {
                expr.append(convert(access));
            }
        });

        if (expr.thing() instanceof JCTree.JCNewClass)
            return expr.thing();
        else
            return at(ce).Apply(null, expr.thing(), args.toList());
    }

    JCExpression convertArg(Tree.PositionalArgument arg) {
        return convertExpression(arg.getExpression());
    }

    JCExpression ceylonLiteral(String s) {
        JCLiteral lit = make().Literal(s);
        return make().Apply(null, makeSelect(makeIdent(syms().ceylonStringType), "instance"), List.<JCExpression> of(lit));
    }

    private JCExpression convert(Tree.StringLiteral string) {
        String value = string.getText().substring(1, string.getText().length() - 1);
        at(string);
        return ceylonLiteral(value);
    }

    private JCExpression convert(final Tree.QualifiedMemberExpression access) {
        final Tree.Identifier memberName = access.getIdentifier();
        final Tree.Primary operand = access.getPrimary();

        class V extends Visitor {
            public JCExpression result;

            // FIXME: this list of cases is incomplete from Gen
            public void visit(Tree.BaseMemberExpression op) {
                result = at(access).Select(convert(op), names().fromString(memberName.getText()));
            }

            public void visit(Tree.QualifiedMemberExpression op) {
                result = at(access).Select(convert(op), names().fromString(memberName.getText()));
            }

            public void visit(Tree.Expression tree) {
                result = at(access).Select(convertExpression(tree), names().fromString(memberName.getText()));
            }
        }

        V v = new V();
        operand.visit(v);
        return v.result;
    }

    private JCExpression convert(Tree.BaseMemberExpression member) {
        Declaration decl = member.getDeclaration();
        if(decl instanceof Value){
            Scope container = decl.getContainer();
            if(container instanceof Package){
                // it's a toplevel attribute
                java.util.List<String> path = new LinkedList<String>();
                path.addAll(container.getQualifiedName());
                path.add("$"+decl.getName());
                path.add(Util.getGetterName(decl.getName()));
                return at(member).Apply(List.<JCExpression>nil(), makeIdent(path), List.<JCExpression>nil());
            }
        }
        return make().Ident(names().fromString(member.getIdentifier().getText()));
    }
}
