package com.redhat.ceylon.compiler.codegen;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AndOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AssignOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BinaryOperatorExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InvocationExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.OrOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PostfixExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SequenceEnumeration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.redhat.ceylon.compiler.util.Util;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCUnary;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

/**
 * This transformer deals with expressions only
 */
public class ExpressionTransformer extends AbstractTransformer {

    private static final class InvocationExpressionVisitor extends AbstractVisitor<JCExpression> {
		private final ListBuffer<JCExpression> args;

		private InvocationExpressionVisitor(ExpressionTransformer transformer, 
				ListBuffer<JCExpression> args) {
		    super(transformer.gen);
			this.args = args;
		}

		public void visit(Tree.QualifiedMemberExpression access) {
		    append(expressionGen.transform(access));
		}

		public void visit(Tree.Type type) {
		    // A constructor
		    append(at(type).NewClass(null, null, gen.makeJavaType(type.getTypeModel(), false), args.toList(), null));
		}

		public void visit(Tree.BaseTypeExpression typeExp) {
		    // A constructor
		    append(at(typeExp).NewClass(null, null, makeIdent(typeExp.getIdentifier().getText()), args.toList(), null));
		}

		public void visit(Tree.InvocationExpression chainedCall) {
		    append(expressionGen.transform(chainedCall));
		}

		public void visit(Tree.BaseMemberExpression access) {
		    append(expressionGen.transform(access));
		}
	}

	public ExpressionTransformer(CeylonTransformer gen) {
        super(gen);
    }

	static class TermVisitor extends AbstractVisitor<JCExpression> {
        //private ExpressionTransformer transformer;
		private Term expr;

        public TermVisitor(ExpressionTransformer transformer,
				Term expr) {
            super(transformer.gen);
        	this.expr = expr;
		}

		public void visit(Tree.This expr) {
			at(expr);
            append(makeIdent("this"));
        }

        public void visit(Tree.Super expr) {
        	at(expr);
            append(makeIdent("super"));
        }

        // FIXME: port dot operator?
        public void visit(Tree.NotEqualOp op) {
            append(expressionGen.transform(op));
        }

        public void visit(Tree.NotOp op) {
            append(expressionGen.transform(op));
        }

        public void visit(Tree.AssignOp op) {
            append(expressionGen.transform(op));
        }

        public void visit(Tree.IsOp op) {
            append(expressionGen.transform(op));
        }

        public void visit(Tree.RangeOp op) {
            append(expressionGen.transform(op));
        }

        public void visit(Tree.EntryOp op) {
            append(expressionGen.transform(op));
        }

        public void visit(Tree.LogicalOp op) {
            append(expressionGen.transform(op));
        }

        public void visit(Tree.UnaryOperatorExpression op) {
            append(expressionGen.transform(op));
        }

        public void visit(Tree.BinaryOperatorExpression op) {
            append(expressionGen.transform(op));
        }

        public void visit(Tree.ArithmeticAssignmentOp op){
            append(expressionGen.transform(op));
        }

        public void visit(Tree.BitwiseAssignmentOp op){
            append(expressionGen.transform(op));
        }

        public void visit(Tree.LogicalAssignmentOp op){
            append(expressionGen.transform(op));
        }

        public void visit(Tree.PrefixOperatorExpression op) {
            append(expressionGen.transform(op));
        }

        public void visit(Tree.PostfixOperatorExpression op) {
            append(expressionGen.transform(op));
        }

        // NB spec 1.3.11 says "There are only two types of numeric
        // literals: literals for Naturals and literals for Floats."
        public void visit(Tree.NaturalLiteral lit) {
            JCExpression n = make().Literal(Long.parseLong(lit.getText()));
            append(at(expr).Apply(null, makeSelect(makeIdent(syms().ceylonNaturalType), "instance"), List.of(n)));
        }

        public void visit(Tree.FloatLiteral lit) {
            JCExpression n = make().Literal(Double.parseDouble(lit.getText()));
            append(at(expr).Apply(null, makeSelect(makeIdent(syms().ceylonFloatType), "instance"), List.of(n)));
        }

        public void visit(Tree.CharLiteral lit) {
            JCExpression n = make().Literal(TypeTags.CHAR, (int) lit.getText().charAt(1));
            // XXX make().Literal(lit.value) doesn't work here... something
            // broken in javac?
            append(at(expr).Apply(null, makeSelect(makeIdent(syms().ceylonCharacterType), "instance"), List.of(n)));
        }

        public void visit(Tree.StringLiteral string) {
            append(expressionGen.transform(string));
        }

        public void visit(Tree.InvocationExpression call) {
            append(expressionGen.transform(call));
        }

        // FIXME: port ReflectedLiteral?
        public void visit(Tree.BaseMemberExpression value) {
            append(expressionGen.transform(value));
        }

        public void visit(Tree.QualifiedMemberExpression value) {
            append(expressionGen.transform(value));
        }

        // FIXME: port TypeName?
        public void visit(Tree.InitializerExpression value) {
            append(expressionGen.transformExpression(value.getExpression()));
        }

        public void visit(Tree.SequenceEnumeration value) {
            append(expressionGen.transform(value));
        }

        // FIXME: port Null?
        // FIXME: port Condition?
        // FIXME: port Subscript?
        // FIXME: port LowerBoud?
        // FIXME: port EnumList?
        public void visit(Tree.StringTemplate expr) {
            append(expressionGen.transformStringExpression(expr));
        }
    }
	
    JCExpression transformExpression(final Tree.Term expr) {
        TermVisitor v = new TermVisitor(this, expr);
        expr.visit(v);
        return v.getSingleResult();
    }

    private JCExpression transformStringExpression(Tree.StringTemplate expr) {
        ListBuffer<JCExpression> strings = new ListBuffer<JCExpression>();
        for (Tree.Expression t : expr.getExpressions()) {
            strings.append(transformExpression(t));
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
    private JCExpression transform(Tree.NotEqualOp op) {
        Tree.EqualOp newOp = new Tree.EqualOp(op.getAntlrTreeNode());
        newOp.setLeftTerm(op.getLeftTerm());
        newOp.setRightTerm(op.getRightTerm());
        Tree.NotOp newNotOp = new Tree.NotOp(op.getAntlrTreeNode());
        newNotOp.setTerm(newOp);
        return transform(newNotOp);
    }

    private JCExpression transform(Tree.NotOp op) {
        JCExpression result = null;
        JCExpression term = transformExpression(op.getTerm());
        JCExpression arg1 = gen.makeSelect("Boolean", "instance");
        JCExpression field1 = at(op.getTerm()).Apply(null, makeSelect(term, "booleanValue"), List.<JCExpression>nil());
        JCUnary jcu = at(op).Unary(JCTree.NOT, field1);
        List<JCExpression> arg2= List.of((JCExpression)jcu);
        result = at(op).Apply(null, arg1, arg2);
        return result;
    }

    private JCExpression transform(Tree.AssignOp op) {
        return transformAssignment(op, op.getLeftTerm(), op.getRightTerm());
    }

    JCExpression transformAssignment(Node op, Term leftTerm, Term rightTerm) {
        // right side is easy
        JCExpression rhs = transformExpression(rightTerm);
        // left side depends
        // FIXME: can this be anything else than a Primary?
        Declaration decl = ((Tree.Primary)leftTerm).getDeclaration();
        // FIXME: can this be anything else than a Value or a TypedDeclaration?
        boolean variable = false;
        if (decl instanceof Value) {
            variable = ((Value)decl).isVariable();
        } else if (decl instanceof TypedDeclaration) {
            variable = ((TypedDeclaration)decl).isVariable();
        }
        if(decl.isToplevel()){
            // must use top level setter
            return gen.globalGenAt(op).setGlobalValue(
                    makeIdent(decl.getContainer().getQualifiedName()),
                    decl.getName(),
                    rhs);
        } else if((decl instanceof Getter) || (Util.isClassAttribute(decl) && variable)){
            // must use the setter
            return at(op).Apply(List.<JCTree.JCExpression>nil(), makeIdent(Util.getSetterName(decl.getName())), 
                    List.<JCTree.JCExpression>of(rhs));
        } else {
            return at(op).Assign(make().Ident(names().fromString(decl.getName())), rhs);
        }
    }

    private JCExpression transform(Tree.IsOp op) {
        // FIXME: this is only working for BaseTypeExpression
        // FIXME: Nasty cast here. We can't call transformExpression()operands[1])
        // because that returns TypeName.class, not simply TypeName.
        Tree.BaseTypeExpression name = (Tree.BaseTypeExpression) op.getRightTerm();
        return at(op).Apply(null, makeSelect(makeIdent(syms().ceylonBooleanType), "instance"), List.<JCExpression> of(at(op).TypeTest(transformExpression(op.getLeftTerm()), makeIdent(name.getIdentifier().getText()))));
    }

    private JCExpression transform(Tree.RangeOp op) {
        JCExpression lower = transformExpression(op.getLeftTerm());
        JCExpression upper = transformExpression(op.getRightTerm());
        JCExpression type = gen.makeJavaType(op.getLeftTerm().getTypeModel(), false);
        return at(op).NewClass(null, null, at(op).TypeApply(makeIdent(syms().ceylonRangeType), List.<JCExpression> of(type)), List.<JCExpression> of(lower, upper), null);
    }

    private JCExpression transform(Tree.EntryOp op) {
        JCExpression key = transformExpression(op.getLeftTerm());
        JCExpression elem = transformExpression(op.getRightTerm());
        JCExpression keyType = gen.makeJavaType(op.getLeftTerm().getTypeModel(), false);
        JCExpression elemType = gen.makeJavaType(op.getRightTerm().getTypeModel(), false);
        return at(op).NewClass(null, null, at(op).TypeApply(makeIdent(syms().ceylonEntryType), List.<JCExpression> of(keyType, elemType)), List.<JCExpression> of(key, elem), null);
    }

    JCExpression transform(Tree.UnaryOperatorExpression op) {
        Tree.Term term = op.getTerm();
        if (term instanceof Tree.NaturalLiteral && op instanceof Tree.NegativeOp) {
            Tree.NaturalLiteral lit = (Tree.NaturalLiteral) term;
            return at(op).Apply(null, makeSelect(makeIdent(syms().ceylonIntegerType), "instance"), List.<JCExpression> of(make().Literal(-Long.parseLong(lit.getText()))));
        } else if (term instanceof Tree.NaturalLiteral && op instanceof Tree.PositiveOp) {
            Tree.NaturalLiteral lit = (Tree.NaturalLiteral) term;
            return at(op).Apply(null, makeSelect(makeIdent(syms().ceylonIntegerType), "instance"), List.<JCExpression> of(make().Literal(Long.parseLong(lit.getText()))));
        }
        return at(op).Apply(null, gen.makeSelect(transformExpression(term), unaryOperators.get(op.getClass())), List.<JCExpression> nil());
    }

    private JCExpression transform(Tree.ArithmeticAssignmentOp op){
        // desugar it
        Tree.BinaryOperatorExpression newOp;
        if(op instanceof Tree.AddAssignOp)
            newOp = new Tree.SumOp(op.getAntlrTreeNode());
        else if(op instanceof Tree.SubtractAssignOp)
            newOp = new Tree.DifferenceOp(op.getAntlrTreeNode());
        else if(op instanceof Tree.MultiplyAssignOp)
            newOp = new Tree.ProductOp(op.getAntlrTreeNode());
        else if(op instanceof Tree.DivideAssignOp)
            newOp = new Tree.QuotientOp(op.getAntlrTreeNode());
        else if(op instanceof Tree.RemainderAssignOp)
            newOp = new Tree.RemainderOp(op.getAntlrTreeNode());
        else
            throw new RuntimeException("Unsupported operator: "+op);
        return desugarAssignmentOp(op, newOp);
    }
    
    private JCExpression transform(Tree.BitwiseAssignmentOp op){
        // desugar it
        Tree.BinaryOperatorExpression newOp;
        if(op instanceof Tree.ComplementAssignOp)
            newOp = new Tree.ComplementOp(op.getAntlrTreeNode());
        else if(op instanceof Tree.UnionAssignOp)
            newOp = new Tree.UnionOp(op.getAntlrTreeNode());
        else if(op instanceof Tree.XorAssignOp)
            newOp = new Tree.XorOp(op.getAntlrTreeNode());
        else if(op instanceof Tree.IntersectAssignOp)
            newOp = new Tree.IntersectionOp(op.getAntlrTreeNode());
        else
            throw new RuntimeException("Unsupported operator: "+op);
        return desugarAssignmentOp(op, newOp);
    }

    private JCExpression transform(Tree.LogicalAssignmentOp op){
        // desugar it
        Tree.BinaryOperatorExpression newOp;
        if(op instanceof Tree.AndAssignOp)
            newOp = new Tree.AndOp(op.getAntlrTreeNode());
        else if(op instanceof Tree.OrAssignOp)
            newOp = new Tree.OrOp(op.getAntlrTreeNode());
        else
            throw new RuntimeException("Unsupported operator: "+op);
        return desugarAssignmentOp(op, newOp);
    }

    private JCExpression desugarAssignmentOp(Tree.AssignmentOp op, BinaryOperatorExpression newOp) {
        newOp.setLeftTerm(op.getLeftTerm());
        newOp.setRightTerm(op.getRightTerm());
        
        AssignOp assignOp = new Tree.AssignOp(op.getAntlrTreeNode());
        assignOp.setLeftTerm(op.getLeftTerm());
        assignOp.setRightTerm(newOp);
        return transform(assignOp);
    }

    private JCExpression transform(Tree.BinaryOperatorExpression op) {
        JCExpression result = null;
        Class<? extends Tree.OperatorExpression> operatorClass = op.getClass();

        boolean loseComparison = op instanceof Tree.SmallAsOp || op instanceof Tree.SmallerOp || op instanceof Tree.LargerOp || op instanceof Tree.LargeAsOp;

        if (loseComparison)
            operatorClass = Tree.CompareOp.class;

        JCExpression left = transformExpression(op.getLeftTerm());
        JCExpression right = transformExpression(op.getRightTerm());
        result = at(op).Apply(null, gen.makeSelect(left, binaryOperators.get(operatorClass)), List.of(right));

        if (loseComparison) {
            result = at(op).Apply(null, gen.makeSelect(result, binaryOperators.get(op.getClass())), List.<JCExpression> nil());
        }

        return result;
    }

    private JCExpression transform(Tree.LogicalOp op) {
        JCExpression result = null;
        JCExpression left = transformExpression(op.getLeftTerm());
        JCExpression right = transformExpression(op.getRightTerm());
        JCExpression arg1 = gen.makeSelect("Boolean", "instance");
        JCExpression field1 = at(op.getLeftTerm()).Apply(null, makeSelect(left, "booleanValue"), List.<JCExpression>nil());
        JCExpression field2 = at(op.getRightTerm()).Apply(null, makeSelect(right, "booleanValue"), List.<JCExpression>nil());

        JCBinary jcb = null;
        if (op instanceof AndOp) {
            jcb = at(op).Binary(JCTree.AND, field1, field2);
        }
        if (op instanceof OrOp) {
            jcb = at(op).Binary(JCTree.OR, field1, field2);
        }
        List<JCExpression> arg2= List.of((JCExpression)jcb);
        result = at(op).Apply(null, arg1, arg2);
        return result;
    }
    
    JCExpression transform(Tree.PostfixOperatorExpression expr) {
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

    private JCExpression transform(Tree.PrefixOperatorExpression expr) {
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
        JCExpression operand = transformExpression(term);
        return at(expr).Assign(operand, at(expr).Apply(null, makeSelect(operand, methodName), List.<JCExpression>nil()));
    }

    JCExpression transform(Tree.InvocationExpression ce) {
        final ListBuffer<JCExpression> args = new ListBuffer<JCExpression>();

        for (Tree.PositionalArgument arg : ce.getPositionalArgumentList().getPositionalArguments())
            args.append(transformArg(arg));

        InvocationExpressionVisitor visitor = new InvocationExpressionVisitor(this, args);
		ce.getPrimary().visit(visitor);

		JCExpression expr = visitor.getSingleResult();
		if (expr == null) {
			throw new RuntimeException();
		} else if (expr instanceof JCTree.JCNewClass) {
            return expr;
        } else {
            return at(ce).Apply(null, expr, args.toList());
        }
    }

    JCExpression transformArg(Tree.PositionalArgument arg) {
        return transformExpression(arg.getExpression());
    }

    JCExpression ceylonLiteral(String s) {
        JCLiteral lit = make().Literal(s);
        return make().Apply(null, makeSelect(makeIdent(syms().ceylonStringType), "instance"), List.<JCExpression> of(lit));
    }

    private JCExpression transform(Tree.StringLiteral string) {
        String value = string
                .getText()
                .substring(1, string.getText().length() - 1)
                .replace("\r\n", "\n")
                .replace("\r", "\n");
        at(string);
        return ceylonLiteral(value);
    }

    static class QualifiedMemberExpressionVisitor extends AbstractVisitor<JCExpression> {
    	
    	//private ExpressionTransformer transformer;

        public QualifiedMemberExpressionVisitor(ExpressionTransformer transformer) {
            super(transformer.gen);
        	//this.transformer = transformer;
        }
        
        // FIXME: this list of cases is incomplete from Gen
        public void visit(Tree.BaseMemberExpression op) {
            append(expressionGen.transform(op));
        }

        public void visit(Tree.QualifiedMemberExpression op) {
            append(expressionGen.transform(op));
        }

        public void visit(Tree.Expression tree) {
            append(expressionGen.transformExpression(tree));
        }
        
        public void visit(InvocationExpression that) { 
        	append(expressionGen.transform(that));
        }
        
        public void visit(Tree.This op) {
            append(expressionGen.makeIdent("this"));
        }
        
        public void visit(Tree.Super op) {
            append(expressionGen.makeIdent("super"));
        }
    }

    
    private JCExpression transform(final Tree.QualifiedMemberExpression access) {
        final Tree.Identifier memberName = access.getIdentifier();
        final Tree.Primary operand = access.getPrimary();

        at(access);
        QualifiedMemberExpressionVisitor v = new QualifiedMemberExpressionVisitor(this);
        operand.visit(v);
        
        JCExpression expr;
        if (gen.willErase(operand.getTypeModel())) {
            // Erased types need a type cast
            JCExpression targetType = gen.makeJavaType(access.getTarget().getDeclaringType(), false);
            expr = gen.makeSelect(make().TypeCast(targetType, v.getSingleResult()), memberName.getText());
        } else {
            expr = gen.makeSelect(v.getSingleResult(), memberName.getText());
        }
        return expr;
    }

    private JCExpression transform(Tree.BaseMemberExpression member) {
        Declaration decl = member.getDeclaration();
        if (decl instanceof Value) {
            if (decl.isToplevel()) {
                if ("null".equals(decl.getName())) {
                    // ERASURE
                    // FIXME this is a pretty brain-dead way to go about erase I think
                    return at(member).Literal(TypeTags.BOT, null);
                } else {
                    // it's a toplevel attribute
                    return gen.globalGenAt(member).getGlobalValue(
                            makeIdent(decl.getContainer().getQualifiedName()),
                            decl.getName());
                }
             } else if(Util.isClassAttribute(decl)) {
                // invoke the getter
                return at(member).Apply(List.<JCExpression>nil(), 
                        makeIdent(Util.getGetterName(decl.getName())), 
                        List.<JCExpression>nil());
            }
        } else if (decl instanceof Getter) {
            // invoke the getter
            if (decl.isToplevel()) {
                return gen.globalGenAt(member).getGlobalValue(
                        makeIdent(decl.getContainer().getQualifiedName()),
                        decl.getName());
            } else if (decl.isClassMember()) {
            	return at(member).Apply(List.<JCExpression>nil(), 
                        makeIdent(Util.getGetterName(decl.getName())), 
                        List.<JCExpression>nil());
            } else {// method local attr
                return at(member).Apply(List.<JCExpression>nil(), 
                        makeIdent(decl.getName(), Util.getGetterName(decl.getName())), 
                        List.<JCExpression>nil());
            }
        } else if (decl instanceof Method) {
            if (Util.isInnerMethod(decl)
            		|| decl.isToplevel()) {
                java.util.List<String> path = new LinkedList<String>();
                path.add(decl.getName());
                path.add(decl.getName());
                return makeIdent(path);
            }
        }
        return at(member).Ident(names().fromString(gen.substitute(member.getIdentifier().getText())));
    }

    public JCExpression transform(SequenceEnumeration value) {
        ListBuffer<JCExpression> elems = new ListBuffer<JCExpression>();
        if (value.getExpressionList() == null) {
            return gen.globalGen.getGlobalValue(makeIdent("ceylon", "language"), "$empty");
        } else {
            java.util.List<Expression> list = value.getExpressionList().getExpressions();
            for (Expression expr : list) {
                elems.append(transformExpression(expr));
            }
            ProducedType t = value.getTypeModel().getTypeArgumentList().get(0);
            return at(value).NewClass(null, null, at(value).TypeApply(makeIdent(syms().ceylonArraySequenceType), List.<JCExpression> of(gen.makeJavaType(t, false))), elems.toList(), null);
        }
    }
}
