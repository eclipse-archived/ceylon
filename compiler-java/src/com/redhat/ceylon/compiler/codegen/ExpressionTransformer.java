/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.compiler.codegen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AndOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AssignOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BinaryOperatorExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Element;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ElementOrRange;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ElementRange;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IndexExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InvocationExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.OrOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Outer;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgumentList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SequenceEnumeration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StringLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Super;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.This;
import com.redhat.ceylon.compiler.util.Decl;
import com.redhat.ceylon.compiler.util.Util;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCConditional;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCUnary;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

/**
 * This transformer deals with expressions only
 */
public class ExpressionTransformer extends AbstractTransformer {

    public static ExpressionTransformer getInstance(Context context) {
        ExpressionTransformer trans = context.get(ExpressionTransformer.class);
        if (trans == null) {
            trans = new ExpressionTransformer(context);
            context.put(ExpressionTransformer.class, trans);
        }
        return trans;
    }

	private ExpressionTransformer(Context context) {
        super(context);
    }

    JCExpression transformExpression(final Tree.Term expr) {
        return transformExpression(expr, BoxingStrategy.BOXED, null);
    }

    JCExpression transformExpression(final Tree.Term expr, BoxingStrategy boxingStrategy, ProducedType expectedType) {
        CeylonVisitor v = new CeylonVisitor(gen());
        if (expr instanceof Tree.Expression) {
            // Cope with things like ((expr))
            Tree.Expression expr2 = (Tree.Expression)expr;
            while(((Tree.Expression)expr2).getTerm() instanceof Tree.Expression) {
                expr2 = (Tree.Expression)expr2.getTerm();
            }
            expr2.visitChildren(v);
        } else {
            expr.visit(v);
        }
        
        if (!v.hasResult()) {
        	return make().Erroneous();
        }
        
        JCExpression result = v.getSingleResult();

        ProducedType exprType = expr.getTypeModel();
        
        if (expectedType != null 
                && willEraseToObject(expr.getTypeModel())
                // don't add cast to an erased type 
                && !willEraseToObject(expectedType)
                // don't add cast for null
                && !isNothing(expr.getTypeModel())) {
            // Erased types need a type cast
            JCExpression targetType = makeJavaType(expectedType, AbstractTransformer.TYPE_ARGUMENT);
            exprType = expectedType;
            result = make().TypeCast(targetType, result);
        }

        // we must to the boxing after the cast to the proper type
        result = boxUnboxIfNecessary(result, expr, exprType, boxingStrategy);

        return result;
    }
    
    public JCExpression transformStringExpression(Tree.StringTemplate expr) {
        at(expr);
        JCExpression builder;
        builder = make().NewClass(null, null, makeIdent("java.lang.StringBuilder"), List.<JCExpression>nil(), null);

        java.util.List<StringLiteral> literals = expr.getStringLiterals();
        java.util.List<Expression> expressions = expr.getExpressions();
        for (int ii = 0; ii < literals.size(); ii += 1) {
            StringLiteral literal = literals.get(ii);
            if (!"\"\"".equals(literal.getText())) {// ignore empty string literals
                at(literal);
                builder = make().Apply(null, makeSelect(builder, "append"), List.<JCExpression>of(transform(literal)));
            }
            if (ii == expressions.size()) {
                // The loop condition includes the last literal, so break out
                // after that because we've already exhausted all the expressions
                break;
            }
            Expression expression = expressions.get(ii);
            at(expression);
            // Here in both cases we don't need a type cast for erasure
            if (isCeylonBasicType(expression.getTypeModel())) {// TODO: Test should be erases to String, long, int, boolean, char, byte, float, double
                // If erases to a Java primitive just call append, don't box it just to call format. 
                builder = make().Apply(null, makeSelect(builder, "append"), List.<JCExpression>of(transformExpression(expression, BoxingStrategy.UNBOXED, null)));
            } else {
                JCMethodInvocation formatted = make().Apply(null, makeSelect(transformExpression(expression), "getFormatted"), List.<JCExpression>nil());
                builder = make().Apply(null, makeSelect(builder, "append"), List.<JCExpression>of(formatted));
            }
        }

        return make().Apply(null, makeSelect(builder, "toString"), List.<JCExpression>nil());
    }

    private static Map<Class<? extends Tree.UnaryOperatorExpression>, String> unaryOperators;
    private static Map<Class<? extends Tree.BinaryOperatorExpression>, String> binaryOperators;

    static {
        unaryOperators = new HashMap<Class<? extends Tree.UnaryOperatorExpression>, String>();
        binaryOperators = new HashMap<Class<? extends Tree.BinaryOperatorExpression>, String>();

        // Unary operators
        unaryOperators.put(Tree.PositiveOp.class, "positiveValue");
        unaryOperators.put(Tree.NegativeOp.class, "negativeValue");
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
        binaryOperators.put(Tree.EqualOp.class, "equals");
        binaryOperators.put(Tree.CompareOp.class, "compare");

        // Binary operators that act on intermediary Comparison objects
        binaryOperators.put(Tree.LargerOp.class, "larger");
        binaryOperators.put(Tree.SmallerOp.class, "smaller");
        binaryOperators.put(Tree.LargeAsOp.class, "largeAs");
        binaryOperators.put(Tree.SmallAsOp.class, "smallAs");
    }

    // FIXME: I'm pretty sure sugar is not supposed to be in there
    public JCExpression transform(Tree.NotEqualOp op) {
        Tree.EqualOp newOp = new Tree.EqualOp(op.getToken());
        newOp.setLeftTerm(op.getLeftTerm());
        newOp.setRightTerm(op.getRightTerm());
        Tree.NotOp newNotOp = new Tree.NotOp(op.getToken());
        newNotOp.setTerm(newOp);
        return transform(newNotOp);
    }

    public JCExpression transform(Tree.NotOp op) {
        // No need for an erasure cast since Term must be Boolean and we never need to erase that
        JCExpression term = transformExpression(op.getTerm(), Util.getBoxingStrategy(op), null);
        JCUnary jcu = at(op).Unary(JCTree.NOT, term);
        return jcu;
    }

    public JCExpression transform(Tree.AssignOp op) {
        return transformAssignment(op, op.getLeftTerm(), op.getRightTerm());
    }

    JCExpression transformAssignment(Node op, Term leftTerm, Term rightTerm) {
        // FIXME: can this be anything else than a Primary?
        TypedDeclaration decl = (TypedDeclaration) ((Tree.Primary)leftTerm).getDeclaration();

        // right side is easy
        JCExpression rhs = transformExpression(rightTerm, Util.getBoxingStrategy(decl), decl.getType());
        return transformAssignment(op, leftTerm, rhs);
    }
    
    JCExpression transformAssignment(Node op, Term leftTerm, JCExpression rhs) {
        // left side depends
        
        JCExpression expr = null;
        CeylonVisitor v = new CeylonVisitor(gen());
        leftTerm.visitChildren(v);
        if (v.hasResult()) {
            expr = v.getSingleResult();
        }
        return transformAssignment(op, leftTerm, expr, rhs);
    }
    
    JCExpression transformAssignment(Node op, Term leftTerm, JCExpression expr, JCExpression rhs) {
        JCExpression result = null;

        // FIXME: can this be anything else than a Primary?
        TypedDeclaration decl = (TypedDeclaration) ((Tree.Primary)leftTerm).getDeclaration();

        boolean variable = decl.isVariable();
        
        if (decl.isToplevel()) {
            // must use top level setter
            result = globalGen().setGlobalValue(
                    makeFQIdent(decl.getContainer().getQualifiedNameString()),
                    decl.getName(),
                    rhs);
        } else if ((decl instanceof Getter)) {
            // must use the setter
            if (Decl.withinMethod(decl)) {
                result = at(op).Apply(List.<JCTree.JCExpression>nil(),
                        makeIdentOrSelect(expr, decl.getName() + "$setter", Util.getSetterName(decl.getName())),
                        List.<JCTree.JCExpression>of(rhs));
            } else {
                result = at(op).Apply(List.<JCTree.JCExpression>nil(),
                        makeIdentOrSelect(expr, Util.getSetterName(decl.getName())),
                        List.<JCTree.JCExpression>of(rhs));            
            }
        } else if (variable && (Decl.isClassAttribute(decl))) {
            // must use the setter
            result = at(op).Apply(List.<JCTree.JCExpression>nil(),
                    makeIdentOrSelect(expr, Util.getSetterName(decl.getName())), 
                    List.<JCTree.JCExpression>of(rhs));
        } else if (variable && (decl.isCaptured() || decl.isShared())) {
            // must use the qualified setter
            result = at(op).Apply(List.<JCTree.JCExpression>nil(),
                    makeIdentOrSelect(expr, decl.getName(), Util.getSetterName(decl.getName())), 
                    List.<JCTree.JCExpression>of(rhs));
        } else {
            result = at(op).Assign(makeIdentOrSelect(expr, decl.getName()), rhs);
        }
        
        return result;
    }

    public JCExpression transform(Tree.IsOp op) {
        JCExpression type = makeJavaType(op.getType().getTypeModel());
        // we don't need any erasure type cast for an "is" test
        return at(op).TypeTest(transformExpression(op.getTerm()), type);
    }

    public JCExpression transform(Tree.RangeOp op) {
        // we need to get the range bound type
        ProducedType comparableType = getSupertype(op.getLeftTerm(), op.getUnit().getComparableDeclaration());
        ProducedType paramType = getTypeArgument(comparableType);
        JCExpression lower = transformExpression(op.getLeftTerm(), BoxingStrategy.BOXED, paramType);
        JCExpression upper = transformExpression(op.getRightTerm(), BoxingStrategy.BOXED, paramType);
        ProducedType rangeType = typeFact().getRangeType(op.getLeftTerm().getTypeModel());
        JCExpression typeExpr = makeJavaType(rangeType, CeylonTransformer.CLASS_NEW);
        return at(op).NewClass(null, null, typeExpr, List.<JCExpression> of(lower, upper), null);
    }

    public JCExpression transform(Tree.EntryOp op) {
        // no erasure cast needed for both terms
        JCExpression key = transformExpression(op.getLeftTerm());
        JCExpression elem = transformExpression(op.getRightTerm());
        ProducedType entryType = typeFact().getEntryType(op.getLeftTerm().getTypeModel(), op.getRightTerm().getTypeModel());
        JCExpression typeExpr = makeJavaType(entryType, CeylonTransformer.CLASS_NEW);
        return at(op).NewClass(null, null, typeExpr , List.<JCExpression> of(key, elem), null);
    }

    public JCExpression transform(Tree.PositiveOp op) {
        return transformUnaryOperator(op, op.getUnit().getInvertableDeclaration());
    }

    public JCExpression transform(Tree.NegativeOp op) {
        return transformUnaryOperator(op, op.getUnit().getInvertableDeclaration());
    }

    public JCExpression transformUnaryOperator(Tree.UnaryOperatorExpression op, Interface compoundType) {
        ProducedType leftType = getSupertype(op.getTerm(), compoundType);
        return transform(op, leftType);
    }
    
    private ProducedType getSupertype(Term term, Interface compoundType){
        return term.getTypeModel().getSupertype(compoundType);
    }

    public JCExpression transform(Tree.UnaryOperatorExpression op, ProducedType expectedType) {
        at(op);
        Tree.Term term = op.getTerm();
        if (term instanceof Tree.NaturalLiteral && op instanceof Tree.NegativeOp) {
            Tree.NaturalLiteral lit = (Tree.NaturalLiteral) term;
            return makeLong(-Long.parseLong(lit.getText()));
        } else if (term instanceof Tree.NaturalLiteral && op instanceof Tree.PositiveOp) {
            Tree.NaturalLiteral lit = (Tree.NaturalLiteral) term;
            return makeLong(Long.parseLong(lit.getText()));
        }
        
        String operatorMethodName = unaryOperators.get(op.getClass());
        if (operatorMethodName == null) {
        	return make().Erroneous();
        }
        
        return make().Apply(null, makeSelect(transformExpression(term, BoxingStrategy.BOXED, expectedType), 
                Util.getGetterName(operatorMethodName)), List.<JCExpression> nil());
    }

    public JCExpression transform(Tree.ArithmeticAssignmentOp op){
        // desugar it
        Tree.BinaryOperatorExpression newOp;
        if(op instanceof Tree.AddAssignOp)
            newOp = new Tree.SumOp(op.getToken());
        else if(op instanceof Tree.SubtractAssignOp)
            newOp = new Tree.DifferenceOp(op.getToken());
        else if(op instanceof Tree.MultiplyAssignOp)
            newOp = new Tree.ProductOp(op.getToken());
        else if(op instanceof Tree.DivideAssignOp)
            newOp = new Tree.QuotientOp(op.getToken());
        else if(op instanceof Tree.RemainderAssignOp)
            newOp = new Tree.RemainderOp(op.getToken());
        else
            throw new RuntimeException("Unsupported operator: "+op);
        return desugarAssignmentOp(op, newOp);
    }
    
    public JCExpression transform(Tree.BitwiseAssignmentOp op){
        // desugar it
        Tree.BinaryOperatorExpression newOp;
        if(op instanceof Tree.ComplementAssignOp)
            newOp = new Tree.ComplementOp(op.getToken());
        else if(op instanceof Tree.UnionAssignOp)
            newOp = new Tree.UnionOp(op.getToken());
        else if(op instanceof Tree.XorAssignOp)
            newOp = new Tree.XorOp(op.getToken());
        else if(op instanceof Tree.IntersectAssignOp)
            newOp = new Tree.IntersectionOp(op.getToken());
        else
            throw new RuntimeException("Unsupported operator: "+op);
        return desugarAssignmentOp(op, newOp);
    }

    public JCExpression transform(Tree.LogicalAssignmentOp op){
        // desugar it
        Tree.BinaryOperatorExpression newOp;
        if(op instanceof Tree.AndAssignOp)
            newOp = new Tree.AndOp(op.getToken());
        else if(op instanceof Tree.OrAssignOp)
            newOp = new Tree.OrOp(op.getToken());
        else
            throw new RuntimeException("Unsupported operator: "+op);
        return desugarAssignmentOp(op, newOp);
    }

    // FIXME GET RID OF THIS, IT'S WRONG because we evaluate the LHS twice
    private JCExpression desugarAssignmentOp(Tree.AssignmentOp op, BinaryOperatorExpression newOp) {
        newOp.setLeftTerm(op.getLeftTerm());
        newOp.setRightTerm(op.getRightTerm());
        
        AssignOp assignOp = new Tree.AssignOp(op.getToken());
        assignOp.setLeftTerm(op.getLeftTerm());
        assignOp.setRightTerm(newOp);
        assignOp.setTypeModel(op.getTypeModel());
        newOp.setTypeModel(op.getTypeModel());

        if(Util.isUnBoxed(op))
            Util.markUnBoxed(newOp);
        return transform(assignOp);
    }

    public JCExpression transform(Tree.ComparisonOp op) {
        return transformArithmeticOperator(op, op.getUnit().getComparableDeclaration());
    }

    public JCExpression transform(Tree.ArithmeticOp op) {
        return transformArithmeticOperator(op, op.getUnit().getNumericDeclaration());
    }
    
    public JCExpression transform(Tree.SumOp op) {
        return transformArithmeticOperator(op, op.getUnit().getSummableDeclaration());
    }

    public JCExpression transform(Tree.RemainderOp op) {
        return transformArithmeticOperator(op, op.getUnit().getIntegralDeclaration());
    }

    public JCExpression transformArithmeticOperator(Tree.BinaryOperatorExpression op, Interface compoundType) {
        ProducedType leftType = getSupertype(op.getLeftTerm(), compoundType);
        ProducedType rightType = getTypeArgument(leftType);
        return transform(op, leftType, rightType);
    }
    
    private ProducedType getTypeArgument(ProducedType leftType) {
        if (leftType!=null && leftType.getTypeArguments().size()==1) {
            return leftType.getTypeArgumentList().get(0);
        }
        return null;
    }

    private ProducedType getFirstTypeArgument(ProducedType leftType) {
        if (leftType!=null && leftType.getTypeArguments().size() >= 1) {
            return leftType.getTypeArgumentList().get(0);
        }
        return null;
    }

    public JCExpression transform(Tree.BinaryOperatorExpression op, ProducedType leftType, ProducedType rightType) {
        JCExpression result = null;
        
        JCExpression left = transformExpression(op.getLeftTerm(), BoxingStrategy.BOXED, leftType);
        JCExpression right = transformExpression(op.getRightTerm(), BoxingStrategy.BOXED, rightType);
        
        if (op instanceof Tree.IdenticalOp) {
            result = at(op).Binary(JCTree.EQ, left, right);
        } else {
            Class<? extends Tree.OperatorExpression> operatorClass = op.getClass();
    
            boolean loseComparison = op instanceof Tree.SmallAsOp || op instanceof Tree.SmallerOp || op instanceof Tree.LargerOp || op instanceof Tree.LargeAsOp;
    
            if (loseComparison) {
                operatorClass = Tree.CompareOp.class;
            }
            
            String operatorMethodName = binaryOperators.get(operatorClass);
            if (operatorMethodName == null) {
            	return make().Erroneous();
            }
            result = at(op).Apply(null, makeSelect(left, operatorMethodName), List.of(right));
    
            if (loseComparison) {
                String operatorMethodName2 = binaryOperators.get(op.getClass());
                if (operatorMethodName2 == null) {
                	return make().Erroneous();
                }
                result = at(op).Apply(null, makeSelect(result, operatorMethodName2), List.<JCExpression> nil());
            }
        }

        return result;
    }

    public JCExpression transform(Tree.LogicalOp op) {
        // Both terms are Booleans and can't be erased to anything
        JCExpression left = transformExpression(op.getLeftTerm(), BoxingStrategy.UNBOXED, null);
        JCExpression right = transformExpression(op.getRightTerm(), BoxingStrategy.UNBOXED, null);

        JCBinary jcb = null;
        if (op instanceof AndOp) {
            jcb = at(op).Binary(JCTree.AND, left, right);
        }
        if (op instanceof OrOp) {
            jcb = at(op).Binary(JCTree.OR, left, right);
        }
        return jcb;
    }
    
    JCExpression transform(Tree.PostfixOperatorExpression expr) {
        String methodName;
        if (expr instanceof Tree.PostfixIncrementOp){
            methodName = "getSuccessor";
        }else if (expr instanceof Tree.PostfixDecrementOp){
            methodName = "getPredecessor";
        }else{
            log.error("ceylon", "Not supported yet: "+expr.getNodeType());
            return at(expr).Erroneous(List.<JCTree>nil());
        }
        
        Term term = expr.getTerm();
        List<JCVariableDecl> decls = List.nil();
        List<JCStatement> stats = List.nil();
        JCExpression result = null;
        // attr++
        // (let $tmp = attr; attr = $tmp.getSuccessor(); $tmp;)
        if(term instanceof Tree.BaseMemberExpression){
            JCExpression getter = transform((Tree.BaseMemberExpression)term);
            at(expr);
            // Type $tmp = attr
            JCExpression exprType = makeJavaType(expr.getTerm().getTypeModel(), NO_PRIMITIVES);
            Name varName = names().fromString(tempName("op"));
            // make sure we box the results if necessary
            getter = boxUnboxIfNecessary(getter, term, term.getTypeModel(), BoxingStrategy.BOXED);
            JCVariableDecl tmpVar = make().VarDef(make().Modifiers(0), varName, exprType, getter);
            decls = decls.prepend(tmpVar);

            // attr = $tmp.getSuccessor()
            JCExpression successor = make().Apply(null, 
                                                  makeSelect(make().Ident(varName), methodName), 
                                                  List.<JCExpression>nil());
            // make sure the result is boxed if necessary, the result of successor/predecessor is always boxed
            successor = boxUnboxIfNecessary(successor, true, term.getTypeModel(), Util.getBoxingStrategy(term));
            JCExpression assignment = transformAssignment(expr, term, successor);
            stats = stats.prepend(at(expr).Exec(assignment));
            // $tmp
            result = make().Ident(varName);
        }
        else if(term instanceof Tree.QualifiedMemberExpression){
            // e.attr++
            // (let $tmpE = e, $tmpV = $tmpE.attr; $tmpE.attr = $tmpV.getSuccessor(); $tmpV;)
            Tree.QualifiedMemberExpression qualified = (QualifiedMemberExpression) term;

            // transform the primary, this will get us a boxed primary 
            JCExpression e = transformQualifiedMemberPrimary(qualified);
            at(expr);
            
            // Type $tmpE = e
            JCExpression exprType = makeJavaType(qualified.getTarget().getQualifyingType(), NO_PRIMITIVES);
            Name varEName = names().fromString(tempName("opE"));
            JCVariableDecl tmpEVar = make().VarDef(make().Modifiers(0), varEName, exprType, e);

            // Type $tmpV = $tmpE.attr
            JCExpression attrType = makeJavaType(term.getTypeModel(), NO_PRIMITIVES);
            Name varVName = names().fromString(tempName("opV"));
            JCExpression getter = transformMemberExpression(qualified, make().Ident(varEName));
            // make sure we box the results if necessary
            getter = boxUnboxIfNecessary(getter, term, term.getTypeModel(), BoxingStrategy.BOXED);
            JCVariableDecl tmpVVar = make().VarDef(make().Modifiers(0), varVName, attrType, getter);

            // define all the variables
            decls = decls.prepend(tmpVVar);
            decls = decls.prepend(tmpEVar);
            
            // $tmpE.attr = $tmpV.getSuccessor()
            JCExpression successor = make().Apply(null, 
                                                  makeSelect(make().Ident(varVName), methodName), 
                                                  List.<JCExpression>nil());
            // make sure the result is boxed if necessary, the result of successor/predecessor is always boxed
            successor = boxUnboxIfNecessary(successor, true, term.getTypeModel(), Util.getBoxingStrategy(term));
            JCExpression assignment = transformAssignment(expr, term, make().Ident(varEName), successor);
            stats = stats.prepend(at(expr).Exec(assignment));
            
            // $tmpV
            result = make().Ident(varVName);
        }else{
            log.error("ceylon", "Not supported yet");
            return at(expr).Erroneous(List.<JCTree>nil());
        }
        // e?.attr++ is probably not legal
        // a[i]++ is not for M1 but will be:
        // (let $tmpA = a, $tmpI = i, $tmpV = $tmpA.item($tmpI); $tmpA.setItem($tmpI, $tmpV.getSuccessor()); $tmpV;)
        // a?[i]++ is probably not legal
        // a[i1..i1]++ and a[i1...]++ are probably not legal
        // a[].attr++ and a[].e.attr++ are probably not legal

        return make().LetExpr(decls, stats, result);
    }

    public JCExpression transform(Tree.PrefixOperatorExpression expr) {
        boolean successor;
        if (expr instanceof Tree.IncrementOp) {
            successor = true;
        } else if (expr instanceof Tree.DecrementOp) {
            successor = false;
        } else {
            return make().Erroneous();
        }
        return makePrefixOp(expr, expr.getTerm(), successor);
    }

    private JCExpression makePrefixOp(Node expr, Term term, boolean successor) {
        String methodName;
        if (successor) {
            methodName = "getSuccessor";
        } else {
            methodName = "getPredecessor";
        }
        JCExpression operand = transformExpression(term);
        return at(expr).Assign(operand, at(expr).Apply(null, makeSelect(operand, methodName), List.<JCExpression>nil()));
    }

    JCExpression transform(Tree.InvocationExpression ce) {
        if (ce.getPositionalArgumentList() != null) {
            return transformPositionalInvocation(ce);
        } else if (ce.getNamedArgumentList() != null) {
            return transformNamedInvocation(ce);
        } else {
            throw new RuntimeException("Illegal State");
        }
    }
    
    private JCExpression transformNamedInvocation(InvocationExpression ce) {
        return new NamedArgumentCallHelper(this, ce).generate();
    }

    private JCExpression transformPositionalInvocation(InvocationExpression ce) {
        final ListBuffer<JCExpression> args = new ListBuffer<JCExpression>();

        boolean isVarargs = false;
        Declaration primaryDecl = ce.getPrimary().getDeclaration();
        PositionalArgumentList positional = ce.getPositionalArgumentList();
        if (primaryDecl instanceof Method || primaryDecl instanceof com.redhat.ceylon.compiler.typechecker.model.Class) {
            java.util.List<Parameter> declaredParams;
            if(primaryDecl instanceof Method){
                Method methodDecl = (Method)primaryDecl;
                declaredParams = methodDecl.getParameterLists().get(0).getParameters();
            }else{
                com.redhat.ceylon.compiler.typechecker.model.Class classDecl = (com.redhat.ceylon.compiler.typechecker.model.Class)primaryDecl;
                declaredParams = classDecl.getParameterLists().get(0).getParameters();
            }
            int numDeclared = declaredParams.size();
            java.util.List<PositionalArgument> passedArguments = positional.getPositionalArguments();
            int numPassed = passedArguments.size();
            Parameter lastDeclaredParam = declaredParams.isEmpty() ? null : declaredParams.get(declaredParams.size() - 1); 
            if (lastDeclaredParam != null 
                    && lastDeclaredParam.isSequenced()
                    && positional.getEllipsis() == null) {// foo(sequence...) syntax => no need to box
                // => call to a varargs method
                isVarargs = true;
                // first, append the normal args
                for (int ii = 0; ii < numDeclared - 1; ii++) {
                    Tree.PositionalArgument arg = positional.getPositionalArguments().get(ii);
                    args.append(transformArg(arg));
                }
                JCExpression boxed;
                // then, box the remaining passed arguments
                if (numDeclared -1 == numPassed) {
                    // box as Empty
                    boxed = makeEmpty();
                } else {
                    // box with an ArraySequence<T>
                    List<Expression> x = List.<Expression>nil();
                    for (int ii = numDeclared - 1; ii < numPassed; ii++) {
                        Tree.PositionalArgument arg = positional.getPositionalArguments().get(ii);
                        x = x.append(arg.getExpression());
                    }
                    boxed = makeSequenceRaw(x);
                }
                args.append(boxed);
            }
        }

        if (!isVarargs) {
            for (Tree.PositionalArgument arg : positional.getPositionalArguments())
                args.append(transformArg(arg));
        }

        List<JCExpression> typeArgs = transformTypeArguments(ce);
                    
        CeylonVisitor visitor = new CeylonVisitor(gen(), typeArgs, args);
        ce.getPrimary().visit(visitor);

        if (!visitor.hasResult()) {
        	return make().Erroneous();
        }
        
        JCExpression expr = visitor.getSingleResult();
        if (expr instanceof JCTree.JCNewClass) {
            return expr;
        } else {
            return at(ce).Apply(typeArgs, expr, args.toList());
        }
    }

    List<JCExpression> transformTypeArguments(Tree.InvocationExpression def) {
        List<JCExpression> result = List.<JCExpression> nil();
        if (def.getPrimary() instanceof Tree.StaticMemberOrTypeExpression) {
            Tree.StaticMemberOrTypeExpression expr = (Tree.StaticMemberOrTypeExpression)def.getPrimary();
            java.util.List<ProducedType> args = expr.getTypeArguments().getTypeModels();
            if(args != null){
                for (ProducedType arg : args) {
                    result = result.append(makeJavaType(arg, AbstractTransformer.TYPE_ARGUMENT));
                }
            }
        }
        return result;
    }
    
    JCExpression transformArg(Tree.PositionalArgument arg) {
        // deal with upstream errors, must have already been reported so let's not throw further
        if(arg.getParameter() == null)
            return make().Erroneous();
        return transformExpression(arg.getExpression(), 
                Util.getBoxingStrategy(arg.getParameter()), 
                arg.getParameter().getType());
    }

    JCExpression ceylonLiteral(String s) {
        JCLiteral lit = make().Literal(s);
        return lit;
    }

    public JCExpression transform(Tree.StringLiteral string) {
        String value = string
                .getText()
                .substring(1, string.getText().length() - 1)
                .replace("\r\n", "\n")
                .replace("\r", "\n")
                .replace("\\b", "\b")
                .replace("\\t", "\t")
                .replace("\\n", "\n")
                .replace("\\f", "\f")
                .replace("\\r", "\r")
                .replace("\\\\", "\\")
                .replace("\\\"", "\"")
                .replace("\\'", "'")
                .replace("\\`", "`");
        at(string);
        return ceylonLiteral(value);
    }

    public JCExpression transform(Tree.QuotedLiteral string) {
        String value = string
                .getText()
                .substring(1, string.getText().length() - 1);
        JCExpression result = makeSelect(makeIdent(syms().ceylonQuotedType), "instance");
        return at(string).Apply(null, result, List.<JCTree.JCExpression>of(make().Literal(value)));
    }

    public JCExpression transform(Tree.CharLiteral lit) {
        JCExpression expr = make().Literal(TypeTags.CHAR, (int) lit.getText().charAt(1));
        // XXX make().Literal(lit.value) doesn't work here... something
        // broken in javac?
        return expr;
    }

    public JCExpression transform(Tree.FloatLiteral lit) {
        JCExpression expr = make().Literal(Double.parseDouble(lit.getText()));
        return expr;
    }

    public JCExpression transform(Tree.NaturalLiteral lit) {
        JCExpression expr = make().Literal(Long.parseLong(lit.getText()));
        return expr;
    }
    
    public JCExpression transform(Tree.QualifiedMemberExpression expr) {
        JCExpression primaryExpr = transformQualifiedMemberPrimary(expr);
        
        return transformMemberExpression(expr, primaryExpr);
    }

    private JCExpression transformQualifiedMemberPrimary(Tree.QualifiedMemberExpression expr) {
        if(expr.getTarget() == null)
            return at(expr).Erroneous(List.<JCTree>nil());
        return transformExpression(expr.getPrimary(), BoxingStrategy.BOXED, 
                expr.getTarget().getQualifyingType());
    }
    
    public JCExpression transform(Tree.BaseMemberExpression expr) {
        return transformMemberExpression(expr, null);
    }
    
    private JCExpression transformMemberExpression(Tree.StaticMemberOrTypeExpression expr, JCExpression primaryExpr) {
        JCExpression result = null;

        // do not throw, an error will already have been reported
        Declaration decl = expr.getDeclaration();
        if (decl == null) {
            return make().Erroneous(List.<JCTree>nil());
        }
        
        if (decl instanceof Getter) {
            // invoke the getter
            if (decl.isToplevel()) {
                result = globalGen().getGlobalValue(
                        makeFQIdent(decl.getContainer().getQualifiedNameString()),
                        decl.getName(),
                        decl.getName());
            } else if (decl.isClassMember()) {
                result =  make().Apply(List.<JCExpression>nil(), 
                        makeIdentOrSelect(primaryExpr, Util.getGetterName(decl.getName())),
                        List.<JCExpression>nil());
            } else {
                // method local attr
                JCExpression fn;
                if (isRecursiveReference(expr)) {
                    fn = makeIdentOrSelect(primaryExpr, Util.getGetterName(decl.getName()));
                } else {
                    fn = makeIdentOrSelect(primaryExpr, decl.getName() + "$getter", Util.getGetterName(decl.getName()));
                }
                result = make().Apply(List.<JCExpression>nil(), fn, List.<JCExpression>nil());
            }
        } else if (decl instanceof Value) {
            if (decl.isToplevel()) {
                // ERASURE
                if ("null".equals(decl.getName())) {
                    // FIXME this is a pretty brain-dead way to go about erase I think
                    result = makeNull();
                } else if (isBooleanTrue(decl)) {
                    result = makeBoolean(true);
                } else if (isBooleanFalse(decl)) {
                    result = makeBoolean(false);
                } else {
                    // it's a toplevel attribute
                    result = globalGen().getGlobalValue(
                            makeFQIdent(decl.getContainer().getQualifiedNameString()),
                            decl.getName());
                }
            } else if (Decl.isClassAttribute(decl)) {
                // invoke the getter
                result = make().Apply(List.<JCExpression>nil(), 
                       makeIdentOrSelect(primaryExpr, Util.getGetterName(decl.getName())),
                       List.<JCExpression>nil());
             } else if (decl.isCaptured() || decl.isShared()) {
                 // invoke the qualified getter
                 result = make().Apply(List.<JCExpression>nil(), 
                        makeIdentOrSelect(primaryExpr, decl.getName(), Util.getGetterName(decl.getName())),
                        List.<JCExpression>nil());
            }
        } else if (decl instanceof Method) {
            if (Decl.withinMethod(decl)) {
                java.util.List<String> path = new LinkedList<String>();
                if (!isRecursiveReference(expr)) {
                    path.add(decl.getName());
                }
                path.add(Util.quoteMethodName(decl.getName()));
                result = makeIdent(path);
            } else if (decl.isToplevel()) {
                java.util.List<String> path = new LinkedList<String>();
                // FQN must start with empty ident (see https://github.com/ceylon/ceylon-compiler/issues/148)
                if (!decl.getContainer().getQualifiedNameString().isEmpty()) {
                    path.add("");
                	path.addAll(Arrays.asList(decl.getContainer().getQualifiedNameString().split("\\.")));
                } else {
                    path.add("");
                }
                // class
                path.add(Util.quoteIfJavaKeyword(decl.getName()));
                // method
                path.add(Util.quoteMethodName(decl.getName()));
                result = makeIdent(path);
            } else {
                result = makeIdentOrSelect(primaryExpr, Util.quoteMethodName(decl.getName()));
            }
        }
        if (result == null) {
            if (Util.isErasedAttribute(decl.getName())) {
                result = make().Apply(null,
                        makeIdentOrSelect(primaryExpr, Util.quoteMethodName(decl.getName())),
                        List.<JCExpression>nil());
            } else {
                result = makeIdentOrSelect(primaryExpr, substitute(decl.getName()));
            }
        }
        
        return result;
    }

    private JCExpression makeIdentOrSelect(JCExpression expr, String... names) {
        if (expr != null) {
            return makeSelect(expr, names);
        } else {
            return makeIdent(names);
        }
    }
    
    private boolean isRecursiveReference(Tree.StaticMemberOrTypeExpression expr) {
        Declaration decl = expr.getDeclaration();
        Scope s = expr.getScope();
        while (!(s instanceof Declaration) && (s.getContainer() != s)) {
            s = s.getContainer();
        }
        return (s instanceof Declaration) && (s == decl);
    }
    
    public JCExpression transform(Tree.BaseTypeExpression typeExp, List<JCExpression> args) {
        // A constructor
        return at(typeExp).NewClass(null, null, makeJavaType(typeExp.getTypeModel(), CLASS_NEW), args, null);
    }

    public JCExpression transform(SequenceEnumeration value) {
        at(value);
        if (value.getExpressionList() == null) {
            return makeEmpty();
        } else {
            java.util.List<Expression> list = value.getExpressionList().getExpressions();
            ProducedType seqElemType = value.getTypeModel().getTypeArgumentList().get(0);
            return makeSequence(list, seqElemType);
        }
    }

    public JCTree transform(This expr) {
        at(expr);
        return makeIdent("this");
    }

    public JCTree transform(Super expr) {
        at(expr);
        return makeIdent("super");
    }

    public JCTree transform(Outer expr) {
        at(expr);
        ProducedType outerClass = com.redhat.ceylon.compiler.typechecker.model.Util.getOuterClassOrInterface(expr.getScope());
        return makeIdent(outerClass.getDeclaration().getName(), "this");
    }

    public JCTree transform(IndexExpression access) {
        boolean safe = access.getIndexOperator() instanceof Tree.SafeIndexOp;

        // depends on the operator
        ElementOrRange elementOrRange = access.getElementOrRange();
        if(elementOrRange instanceof Tree.Element){
            Tree.Element element = (Element) elementOrRange;
            // let's see what types there are
            ProducedType leftType = access.getPrimary().getTypeModel();
            if(safe)
                leftType = access.getUnit().getDefiniteType(leftType);
            ProducedType leftCorrespondenceType = leftType.getSupertype(access.getUnit().getCorrespondenceDeclaration());
            ProducedType rightType = getFirstTypeArgument(leftCorrespondenceType);
            
            // do the index
            JCExpression index = transformExpression(element.getExpression(), BoxingStrategy.BOXED, rightType);

            // look at the lhs
            JCExpression lhs = transformExpression(access.getPrimary(), BoxingStrategy.UNBOXED, leftCorrespondenceType);

            if(!safe)
                // make a "lhs.item(index)" call
                return at(access).Apply(List.<JCTree.JCExpression>nil(), 
                        make().Select(lhs, names().fromString("item")), List.of(index));
            // make a (let ArrayElem tmp = lhs in (tmp != null ? tmp.item(index) : null)) call
            JCExpression arrayType = makeJavaType(leftCorrespondenceType);
            Name varName = names().fromString(tempName("safeaccess"));
            // tmpVar.item(index)
            JCExpression safeAccess = make().Apply(List.<JCTree.JCExpression>nil(), 
                    make().Select(make().Ident(varName), names().fromString("item")), List.of(index));

            at(access.getPrimary());
            // (tmpVar != null ? safeAccess : null)
            JCConditional conditional = make().Conditional(make().Binary(JCTree.NE, make().Ident(varName), makeNull()), 
                    safeAccess, makeNull());
            // ArrayElem tmp = lhs
            JCVariableDecl tmpVar = make().VarDef(make().Modifiers(0), varName, arrayType, lhs);
            // (let tmpVar in conditional)
            return make().LetExpr(tmpVar, conditional);
        }else{
            // look at the lhs
            JCExpression lhs = transformExpression(access.getPrimary(), BoxingStrategy.UNBOXED, null);
            // do the indices
            Tree.ElementRange range = (ElementRange) elementOrRange;
            JCExpression start = transformExpression(range.getLowerBound(), BoxingStrategy.UNBOXED, null);
            JCExpression end = transformExpression(range.getUpperBound(), BoxingStrategy.UNBOXED, null);
            // make a "lhs.span(start, end)" call
            return at(access).Apply(List.<JCTree.JCExpression>nil(), 
                    make().Select(lhs, names().fromString("span")), List.of(start, end));
        }
    }
}
