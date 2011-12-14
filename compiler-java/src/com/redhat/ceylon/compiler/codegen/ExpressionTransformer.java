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

import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.util.Decl;
import com.redhat.ceylon.compiler.util.Util;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCConditional;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCForLoop;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCNewArray;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
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

    private boolean inStatement = false;
    
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

	//
	// Statement expressions
	
    public JCStatement transform(Tree.ExpressionStatement tree) {
        // ExpressionStatements do not return any value, therefore we don't care about the type of the expressions.
        inStatement = true;
        JCStatement result = at(tree).Exec(transformExpression(tree.getExpression(), BoxingStrategy.INDIFFERENT, null));
        inStatement = false;
        return result;
    }
    
    public JCStatement transform(Tree.SpecifierStatement op) {
        // SpecifierStatement do not return any value, therefore we don't care about the type of the expressions.
        inStatement = true;
        JCStatement result = at(op).Exec(transformAssignment(op, op.getBaseMemberExpression(), op.getSpecifierExpression().getExpression()));
        inStatement = false;
        return result;
    }
    
    //
    // Any sort of expression
    
    JCExpression transformExpression(final Tree.Term expr) {
        return transformExpression(expr, BoxingStrategy.BOXED, null);
    }

    JCExpression transformExpression(final Tree.Term expr, BoxingStrategy boxingStrategy, ProducedType expectedType) {
        if (expr == null) {
            return null;
        }
        
        at(expr);
        if (inStatement && boxingStrategy != BoxingStrategy.INDIFFERENT) {
            // We're not directly inside the ExpressionStatement anymore
            inStatement = false;
        }
        
        CeylonVisitor v = new CeylonVisitor(gen());
        // FIXME: shouldn't that be in the visitor?
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

        result = applyErasureAndBoxing(result, expr, boxingStrategy, expectedType);

        return result;
    }
    
    //
    // Boxing and erasure of expressions
    
    private JCExpression applyErasureAndBoxing(JCExpression result, Tree.Term expr, BoxingStrategy boxingStrategy, ProducedType expectedType) {
        ProducedType exprType = expr.getTypeModel();
        boolean exprBoxed = !Util.isUnBoxed(expr);
        return applyErasureAndBoxing(result, exprType, exprBoxed, boxingStrategy, expectedType);
    }
    
    private JCExpression applyErasureAndBoxing(JCExpression result, ProducedType exprType,
            boolean exprBoxed,
            BoxingStrategy boxingStrategy, ProducedType expectedType) {
        
        if (expectedType != null
                && !(expectedType.getDeclaration() instanceof TypeParameter) 
                && willEraseToObject(exprType)
                // don't add cast to an erased type 
                && !willEraseToObject(expectedType)
                // don't add cast for null
                && !isNothing(exprType)) {
            // Erased types need a type cast
            JCExpression targetType = makeJavaType(expectedType, AbstractTransformer.TYPE_ARGUMENT);
            exprType = expectedType;
            result = make().TypeCast(targetType, result);
        }

        // we must to the boxing after the cast to the proper type
        return boxUnboxIfNecessary(result, exprBoxed, exprType, boxingStrategy);
    }

    //
    // Literals
    
    JCExpression ceylonLiteral(String s) {
        JCLiteral lit = make().Literal(s);
        return lit;
    }

    public JCExpression transform(Tree.StringLiteral string) {
        // FIXME: this is appalling
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
        // FIXME: go unicode, but how?
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

    public JCExpression transformStringExpression(Tree.StringTemplate expr) {
        at(expr);
        JCExpression builder;
        builder = make().NewClass(null, null, makeIdent("java.lang.StringBuilder"), List.<JCExpression>nil(), null);

        java.util.List<Tree.StringLiteral> literals = expr.getStringLiterals();
        java.util.List<Tree.Expression> expressions = expr.getExpressions();
        for (int ii = 0; ii < literals.size(); ii += 1) {
            Tree.StringLiteral literal = literals.get(ii);
            if (!"\"\"".equals(literal.getText())) {// ignore empty string literals
                at(literal);
                builder = make().Apply(null, makeSelect(builder, "append"), List.<JCExpression>of(transform(literal)));
            }
            if (ii == expressions.size()) {
                // The loop condition includes the last literal, so break out
                // after that because we've already exhausted all the expressions
                break;
            }
            Tree.Expression expression = expressions.get(ii);
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

    public JCExpression transform(Tree.SequenceEnumeration value) {
        at(value);
        if (value.getExpressionList() == null) {
            return makeEmpty();
        } else {
            java.util.List<Tree.Expression> list = value.getExpressionList().getExpressions();
            ProducedType seqElemType = value.getTypeModel().getTypeArgumentList().get(0);
            return makeSequence(list, seqElemType);
        }
    }

    public JCTree transform(Tree.This expr) {
        at(expr);
        return makeIdent("this");
    }

    public JCTree transform(Tree.Super expr) {
        at(expr);
        return makeIdent("super");
    }

    public JCTree transform(Tree.Outer expr) {
        at(expr);
        ProducedType outerClass = com.redhat.ceylon.compiler.typechecker.model.Util.getOuterClassOrInterface(expr.getScope());
        return makeIdent(outerClass.getDeclaration().getName(), "this");
    }

    //
    // Unary and Binary operators that can be overridden
    
    private static Map<Class<? extends Tree.UnaryOperatorExpression>, String> unaryOperators;
    private static Map<Class<? extends Tree.BinaryOperatorExpression>, String> binaryOperators;

    static {
        unaryOperators = new HashMap<Class<? extends Tree.UnaryOperatorExpression>, String>();
        binaryOperators = new HashMap<Class<? extends Tree.BinaryOperatorExpression>, String>();

        // Unary operators
        unaryOperators.put(Tree.PositiveOp.class, "positiveValue");
        unaryOperators.put(Tree.NegativeOp.class, "negativeValue");
        // FIXME: this isn't tested but not sure the spec still wants it
        unaryOperators.put(Tree.FormatOp.class, "formatted");

        // Binary operators
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
        binaryOperators.put(Tree.LargerOp.class, "largerThan");
        binaryOperators.put(Tree.SmallerOp.class, "smallerThan");
        binaryOperators.put(Tree.LargeAsOp.class, "asLargeAs");
        binaryOperators.put(Tree.SmallAsOp.class, "asSmallAs");
    }

    //
    // Unary operators

    public JCExpression transform(Tree.NotOp op) {
        // No need for an erasure cast since Term must be Boolean and we never need to erase that
        JCExpression term = transformExpression(op.getTerm(), Util.getBoxingStrategy(op), null);
        JCUnary jcu = at(op).Unary(JCTree.NOT, term);
        return jcu;
    }

    public JCExpression transform(Tree.IsOp op) {
        // we don't need any erasure type cast for an "is" test
        JCExpression expression = transformExpression(op.getTerm());
        at(op);
        return makeTypeTest(expression, op.getType().getTypeModel());
    }

    public JCTree transform(Tree.Nonempty op) {
        // we don't need any erasure type cast for a "nonempty" test
        JCExpression expression = transformExpression(op.getTerm());
        at(op);
        return makeTypeTest(expression, op.getUnit().getSequenceDeclaration().getType());
    }

    public JCTree transform(Tree.Exists op) {
        // we don't need any erasure type cast for an "exists" test
        JCExpression expression = transformExpression(op.getTerm());
        at(op);
        return  make().Binary(JCTree.NE, expression, makeNull());
    }

    public JCExpression transform(Tree.PositiveOp op) {
        return transformOverridableUnaryOperator(op, op.getUnit().getInvertableDeclaration());
    }

    public JCExpression transform(Tree.NegativeOp op) {
        return transformOverridableUnaryOperator(op, op.getUnit().getInvertableDeclaration());
    }

    public JCExpression transform(Tree.UnaryOperatorExpression op) {
        return transformOverridableUnaryOperator(op, (ProducedType)null);
    }

    private JCExpression transformOverridableUnaryOperator(Tree.UnaryOperatorExpression op, Interface compoundType) {
        ProducedType leftType = getSupertype(op.getTerm(), compoundType);
        return transformOverridableUnaryOperator(op, leftType);
    }
    
    private JCExpression transformOverridableUnaryOperator(Tree.UnaryOperatorExpression op, ProducedType expectedType) {
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

    //
    // Binary operators
    
    // FIXME: I'm pretty sure sugar is not supposed to be in there
    public JCExpression transform(Tree.NotEqualOp op) {
        Tree.EqualOp newOp = new Tree.EqualOp(op.getToken());
        newOp.setLeftTerm(op.getLeftTerm());
        newOp.setRightTerm(op.getRightTerm());
        Tree.NotOp newNotOp = new Tree.NotOp(op.getToken());
        newNotOp.setTerm(newOp);
        return transform(newNotOp);
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

    public JCTree transform(Tree.DefaultOp op) {
        JCExpression left = transformExpression(op.getLeftTerm(), BoxingStrategy.BOXED, typeFact().getBooleanDeclaration().getType());
        JCExpression right = transformExpression(op.getRightTerm());
        String varName = tempName();
        JCExpression varIdent = makeIdent(varName);
        JCExpression test = at(op).Binary(JCTree.NE, varIdent, makeNull());
        JCExpression cond = make().Conditional(test , varIdent, right);
        JCExpression typeExpr = makeJavaType(op.getLeftTerm().getTypeModel(), 0);
        return makeLetExpr(varName, null, typeExpr, left, cond);
    }

    public JCTree transform(Tree.ThenOp op) {
        JCExpression left = transformExpression(op.getLeftTerm(), Util.getBoxingStrategy(op.getLeftTerm()), typeFact().getBooleanDeclaration().getType());
        JCExpression right = transformExpression(op.getRightTerm());
        return make().Conditional(left , right, makeNull());
    }
    
    public JCTree transform(Tree.InOp op) {
        JCExpression left = transformExpression(op.getLeftTerm(), BoxingStrategy.BOXED, typeFact().getEqualityDeclaration().getType());
        JCExpression right = transformExpression(op.getRightTerm(), BoxingStrategy.BOXED, typeFact().getCategoryDeclaration().getType());
        String varName = tempName();
        JCExpression varIdent = makeIdent(varName);
        JCExpression contains = at(op).Apply(null, makeSelect(right, "contains"), List.<JCExpression> of(varIdent));
        JCExpression typeExpr = makeJavaType(op.getLeftTerm().getTypeModel(), NO_PRIMITIVES);
        return makeLetExpr(varName, null, typeExpr, left, contains);
    }

    // Logical operators
    
    public JCExpression transform(Tree.LogicalOp op) {
        // Both terms are Booleans and can't be erased to anything
        JCExpression left = transformExpression(op.getLeftTerm(), BoxingStrategy.UNBOXED, null);
        return transformLogicalOp(op, op.getClass(), left, op.getRightTerm());
    }

    private JCExpression transformLogicalOp(Node op, Class<? extends Tree.LogicalOp> operatorClass, 
            JCExpression left, Tree.Term rightTerm) {
        // Both terms are Booleans and can't be erased to anything
        JCExpression right = transformExpression(rightTerm, BoxingStrategy.UNBOXED, null);

        JCBinary jcb = null;
        if (operatorClass == Tree.AndOp.class) {
            jcb = at(op).Binary(JCTree.AND, left, right);
        }else if (operatorClass == Tree.OrOp.class) {
            jcb = at(op).Binary(JCTree.OR, left, right);
        }else{
            log.error("ceylon", "Not supported yet: "+op.getNodeType());
            return at(op).Erroneous(List.<JCTree>nil());
        }
        return jcb;
    }

    // Comparison operators
    
    public JCExpression transform(Tree.ComparisonOp op) {
        return transformOverridableBinaryOperator(op, op.getUnit().getComparableDeclaration());
    }

    public JCExpression transform(Tree.CompareOp op) {
        return transformOverridableBinaryOperator(op, op.getUnit().getComparableDeclaration());
    }

    // Arithmetic operators
    
    public JCExpression transform(Tree.ArithmeticOp op) {
        return transformOverridableBinaryOperator(op, op.getUnit().getNumericDeclaration());
    }
    
    public JCExpression transform(Tree.SumOp op) {
        return transformOverridableBinaryOperator(op, op.getUnit().getSummableDeclaration());
    }

    public JCExpression transform(Tree.RemainderOp op) {
        return transformOverridableBinaryOperator(op, op.getUnit().getIntegralDeclaration());
    }

    // Overridable binary operators
    
    public JCExpression transform(Tree.BinaryOperatorExpression op) {
        return transformOverridableBinaryOperator(op, null, null);
    }

    private JCExpression transformOverridableBinaryOperator(Tree.BinaryOperatorExpression op, Interface compoundType) {
        ProducedType leftType = getSupertype(op.getLeftTerm(), compoundType);
        ProducedType rightType = getTypeArgument(leftType);
        return transformOverridableBinaryOperator(op, leftType, rightType);
    }

    private JCExpression transformOverridableBinaryOperator(Tree.BinaryOperatorExpression op, ProducedType leftType, ProducedType rightType) {
        JCExpression left = transformExpression(op.getLeftTerm(), BoxingStrategy.BOXED, leftType);
        return transformOverridableBinaryOperator(op, op.getClass(), left, rightType);
    }

    private JCExpression transformOverridableBinaryOperator(Tree.BinaryOperatorExpression op, 
            Class<? extends Tree.OperatorExpression> operatorClass, 
            JCExpression left, ProducedType rightType) {
        JCExpression result = null;
        
        JCExpression right = transformExpression(op.getRightTerm(), BoxingStrategy.BOXED, rightType);
        
        if (operatorClass == Tree.IdenticalOp.class) {
            result = at(op).Binary(JCTree.EQ, left, right);
        } else {
            Class<? extends Tree.OperatorExpression> originalOperatorClass = operatorClass;
            boolean loseComparison = 
                    operatorClass == Tree.SmallAsOp.class 
                    || operatorClass == Tree.SmallerOp.class 
                    || operatorClass == Tree.LargerOp.class
                    || operatorClass == Tree.LargeAsOp.class;
    
            if (loseComparison) {
                operatorClass = Tree.CompareOp.class;
            }
            
            String operatorMethodName = binaryOperators.get(operatorClass);
            if (operatorMethodName == null) {
                return make().Erroneous();
            }
            result = at(op).Apply(null, makeSelect(left, operatorMethodName), List.of(right));
    
            if (loseComparison) {
                String operatorMethodName2 = binaryOperators.get(originalOperatorClass);
                if (operatorMethodName2 == null) {
                    return make().Erroneous();
                }
                result = at(op).Apply(null, makeSelect(result, operatorMethodName2), List.<JCExpression> nil());
            }
        }

        return result;
    }

    //
    // Operator-Assignment expressions

    public JCExpression transform(final Tree.ArithmeticAssignmentOp op){
        // desugar it
        final Class<? extends Tree.OperatorExpression> infixOpClass;
        Interface compoundType = op.getUnit().getNumericDeclaration();
        if(op instanceof Tree.AddAssignOp){
            infixOpClass = Tree.SumOp.class;
            compoundType = op.getUnit().getSummableDeclaration();
        }else if(op instanceof Tree.SubtractAssignOp){
            infixOpClass = Tree.DifferenceOp.class;
        }else if(op instanceof Tree.MultiplyAssignOp)
            infixOpClass = Tree.ProductOp.class;
        else if(op instanceof Tree.DivideAssignOp)
            infixOpClass = Tree.QuotientOp.class;
        else if(op instanceof Tree.RemainderAssignOp){
            infixOpClass = Tree.RemainderOp.class;
            compoundType = op.getUnit().getIntegralDeclaration();
        }else{
            log.error("ceylon", "Not supported yet: "+op.getNodeType());
            return at(op).Erroneous(List.<JCTree>nil());
        }
        
        final ProducedType leftType = getSupertype(op.getLeftTerm(), compoundType);
        final ProducedType rightType = getTypeArgument(leftType, 0);

        // we work on boxed types
        return transformAssignAndReturnOperation(op, op.getLeftTerm(), true, 
                leftType, rightType, 
                new AssignAndReturnOperationFactory(){
            @Override
            public JCExpression getNewValue(JCExpression previousValue) {
                // make this call: previousValue OP RHS
                return transformOverridableBinaryOperator(op, infixOpClass, previousValue, rightType);
            }
        });
    }
    
    public JCExpression transform(Tree.BitwiseAssignmentOp op){
        log.error("ceylon", "Not supported yet: "+op.getNodeType());
        return at(op).Erroneous(List.<JCTree>nil());
    }

    public JCExpression transform(final Tree.LogicalAssignmentOp op){
        // desugar it
        final Class<? extends Tree.LogicalOp> operatorClass;
        if(op instanceof Tree.AndAssignOp)
            operatorClass = Tree.AndOp.class;
        else if(op instanceof Tree.OrAssignOp)
            operatorClass = Tree.OrOp.class;
        else{
            log.error("ceylon", "Not supported yet: "+op.getNodeType());
            return at(op).Erroneous(List.<JCTree>nil());
        }
        ProducedType valueType = op.getLeftTerm().getTypeModel();
        // we work on unboxed types
        return transformAssignAndReturnOperation(op, op.getLeftTerm(), false, 
                valueType, valueType, new AssignAndReturnOperationFactory(){
            @Override
            public JCExpression getNewValue(JCExpression previousValue) {
                // make this call: previousValue OP RHS
                return transformLogicalOp(op, operatorClass, previousValue, op.getRightTerm());
            }
        });
    }

    // Postfix operator
    
    public JCExpression transform(Tree.PostfixOperatorExpression expr) {
        String methodName;
        if (expr instanceof Tree.PostfixIncrementOp){
            methodName = "getSuccessor";
        }else if (expr instanceof Tree.PostfixDecrementOp){
            methodName = "getPredecessor";
        }else{
            log.error("ceylon", "Not supported yet: "+expr.getNodeType());
            return at(expr).Erroneous(List.<JCTree>nil());
        }
        
        Interface compoundType = expr.getUnit().getOrdinalDeclaration();
        ProducedType valueType = getSupertype(expr.getTerm(), compoundType);
        ProducedType returnType = getTypeArgument(valueType, 0);

        Tree.Term term = expr.getTerm();
        List<JCVariableDecl> decls = List.nil();
        List<JCStatement> stats = List.nil();
        JCExpression result = null;
        // attr++
        // (let $tmp = attr; attr = $tmp.getSuccessor(); $tmp;)
        if(term instanceof Tree.BaseMemberExpression){
            JCExpression getter = transform((Tree.BaseMemberExpression)term, null);
            at(expr);
            // Type $tmp = attr
            JCExpression exprType = makeJavaType(returnType, NO_PRIMITIVES);
            Name varName = names().fromString(tempName("op"));
            // make sure we box the results if necessary
            getter = applyErasureAndBoxing(getter, term, BoxingStrategy.BOXED, returnType);
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
            // always return boxed
            result = make().Ident(varName);
        }
        else if(term instanceof Tree.QualifiedMemberExpression){
            // e.attr++
            // (let $tmpE = e, $tmpV = $tmpE.attr; $tmpE.attr = $tmpV.getSuccessor(); $tmpV;)
            Tree.QualifiedMemberExpression qualified = (Tree.QualifiedMemberExpression) term;

            // transform the primary, this will get us a boxed primary 
            JCExpression e = transformQualifiedMemberPrimary(qualified);
            at(expr);
            
            // Type $tmpE = e
            JCExpression exprType = makeJavaType(qualified.getTarget().getQualifyingType(), NO_PRIMITIVES);
            Name varEName = names().fromString(tempName("opE"));
            JCVariableDecl tmpEVar = make().VarDef(make().Modifiers(0), varEName, exprType, e);

            // Type $tmpV = $tmpE.attr
            JCExpression attrType = makeJavaType(returnType, NO_PRIMITIVES);
            Name varVName = names().fromString(tempName("opV"));
            JCExpression getter = transformMemberExpression(qualified, make().Ident(varEName), null);
            // make sure we box the results if necessary
            getter = applyErasureAndBoxing(getter, term, BoxingStrategy.BOXED, returnType);
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
            // always return boxed
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
    
    // Prefix operator
    
    public JCExpression transform(Tree.PrefixOperatorExpression expr) {
        final String methodName;
        if (expr instanceof Tree.IncrementOp){
            methodName = "getSuccessor";
        }else if (expr instanceof Tree.DecrementOp){
            methodName = "getPredecessor";
        }else{
            log.error("ceylon", "Not supported yet: "+expr.getNodeType());
            return at(expr).Erroneous(List.<JCTree>nil());
        }
        
        Interface compoundType = expr.getUnit().getOrdinalDeclaration();
        ProducedType valueType = getSupertype(expr.getTerm(), compoundType);
        ProducedType returnType = getTypeArgument(valueType, 0);
        // we work on boxed types
        return transformAssignAndReturnOperation(expr, expr.getTerm(), true, 
                valueType, returnType, new AssignAndReturnOperationFactory(){
            @Override
            public JCExpression getNewValue(JCExpression previousValue) {
                // make this call: previousValue.getSuccessor() or previousValue.getPredecessor()
                return make().Apply(null, makeSelect(previousValue, methodName), List.<JCExpression>nil());
            }
        });
    }
    
    //
    // Function to deal with expressions that have side-effects
    
    private interface AssignAndReturnOperationFactory {
        JCExpression getNewValue(JCExpression previousValue);
    }
    
    private JCExpression transformAssignAndReturnOperation(Node operator, Tree.Term term, 
            boolean boxResult, ProducedType valueType, ProducedType returnType, 
            AssignAndReturnOperationFactory factory){
        
        List<JCVariableDecl> decls = List.nil();
        List<JCStatement> stats = List.nil();
        JCExpression result = null;
        // attr
        // (let $tmp = OP(attr); attr = $tmp; $tmp)
        if(term instanceof Tree.BaseMemberExpression){
            JCExpression getter = transform((Tree.BaseMemberExpression)term, null);
            at(operator);
            // Type $tmp = OP(attr);
            JCExpression exprType = makeJavaType(returnType, boxResult ? NO_PRIMITIVES : 0);
            Name varName = names().fromString(tempName("op"));
            // make sure we box the results if necessary
            getter = applyErasureAndBoxing(getter, term, boxResult ? BoxingStrategy.BOXED : BoxingStrategy.UNBOXED, valueType);
            JCExpression newValue = factory.getNewValue(getter);
            // no need to box/unbox here since newValue and $tmpV share the same boxing type
            JCVariableDecl tmpVar = make().VarDef(make().Modifiers(0), varName, exprType, newValue);
            decls = decls.prepend(tmpVar);

            // attr = $tmp
            // make sure the result is unboxed if necessary, $tmp may be boxed
            JCExpression value = make().Ident(varName);
            value = boxUnboxIfNecessary(value, boxResult, term.getTypeModel(), Util.getBoxingStrategy(term));
            JCExpression assignment = transformAssignment(operator, term, value);
            stats = stats.prepend(at(operator).Exec(assignment));
            
            // $tmp
            // return, with the box type we asked for
            result = make().Ident(varName);
        }
        else if(term instanceof Tree.QualifiedMemberExpression){
            // e.attr
            // (let $tmpE = e, $tmpV = OP($tmpE.attr); $tmpE.attr = $tmpV; $tmpV;)
            Tree.QualifiedMemberExpression qualified = (Tree.QualifiedMemberExpression) term;

            // transform the primary, this will get us a boxed primary 
            JCExpression e = transformQualifiedMemberPrimary(qualified);
            at(operator);
            
            // Type $tmpE = e
            JCExpression exprType = makeJavaType(qualified.getTarget().getQualifyingType(), NO_PRIMITIVES);
            Name varEName = names().fromString(tempName("opE"));
            JCVariableDecl tmpEVar = make().VarDef(make().Modifiers(0), varEName, exprType, e);

            // Type $tmpV = OP($tmpE.attr)
            JCExpression attrType = makeJavaType(returnType, boxResult ? NO_PRIMITIVES : 0);
            Name varVName = names().fromString(tempName("opV"));
            JCExpression getter = transformMemberExpression(qualified, make().Ident(varEName), null);
            // make sure we box the results if necessary
            getter = applyErasureAndBoxing(getter, term, boxResult ? BoxingStrategy.BOXED : BoxingStrategy.UNBOXED, valueType);
            JCExpression newValue = factory.getNewValue(getter);
            // no need to box/unbox here since newValue and $tmpV share the same boxing type
            JCVariableDecl tmpVVar = make().VarDef(make().Modifiers(0), varVName, attrType, newValue);

            // define all the variables
            decls = decls.prepend(tmpVVar);
            decls = decls.prepend(tmpEVar);
            
            // $tmpE.attr = $tmpV
            // make sure $tmpV is unboxed if necessary
            JCExpression value = make().Ident(varVName);
            value = boxUnboxIfNecessary(value, boxResult, term.getTypeModel(), Util.getBoxingStrategy(term));
            JCExpression assignment = transformAssignment(operator, term, make().Ident(varEName), value);
            stats = stats.prepend(at(operator).Exec(assignment));
            
            // $tmpV
            // return, with the box type we asked for
            result = make().Ident(varVName);
        }else{
            log.error("ceylon", "Not supported yet");
            return at(operator).Erroneous(List.<JCTree>nil());
        }
        // OP(e?.attr) is probably not legal
        // OP(a[i]) is not for M1 but will be:
        // (let $tmpA = a, $tmpI = i, $tmpV = OP($tmpA.item($tmpI)); $tmpA.setItem($tmpI, $tmpV); $tmpV;)
        // OP(a?[i]) is probably not legal
        // OP(a[i1..i1]) and OP(a[i1...]) are probably not legal
        // OP(a[].attr) and OP(a[].e.attr) are probably not legal

        return make().LetExpr(decls, stats, result);
    }

    //
    // Invocations
    
    public JCExpression transform(Tree.InvocationExpression ce) {
        if (ce.getPositionalArgumentList() != null) {
            return transformPositionalInvocation(ce);
        } else if (ce.getNamedArgumentList() != null) {
            return transformNamedInvocation(ce);
        } else {
            throw new RuntimeException("Illegal State");
        }
    }
    
    // Named invocation
    
    private JCExpression transformNamedInvocation(final Tree.InvocationExpression ce) {
        ListBuffer<JCVariableDecl> vars = ListBuffer.lb();
        ListBuffer<JCExpression> args = ListBuffer.lb();

        java.util.List<ProducedType> typeArgumentModels = getTypeArguments(ce);
        List<JCExpression> typeArgs = transformTypeArguments(typeArgumentModels);
        boolean isRaw = typeArgs.isEmpty();

        Declaration primaryDecl = ce.getPrimary().getDeclaration();
        if (primaryDecl != null) {
            java.util.List<ParameterList> paramLists = ((Functional)primaryDecl).getParameterLists();
            java.util.List<Tree.NamedArgument> namedArguments = ce.getNamedArgumentList().getNamedArguments();
            java.util.List<Parameter> declaredParams = paramLists.get(0).getParameters();
            Parameter lastDeclared = declaredParams.size() > 0 ? declaredParams.get(declaredParams.size() - 1) : null;
            boolean boundSequenced = false;
            String varBaseName = aliasName("arg");
            
            int numDeclared = declaredParams.size();
            int numPassed = namedArguments.size();
            for (Tree.NamedArgument namedArg : namedArguments) {
                at(namedArg);
                Parameter declaredParam = namedArg.getParameter();
                if (declaredParam.isSequenced()) {
                    boundSequenced = true;
                }
                Tree.Expression expr = ((Tree.SpecifiedArgument)namedArg).getSpecifierExpression().getExpression();
                int index = declaredParams.indexOf(declaredParam);
                String varName = varBaseName + "$" + index;
                BoxingStrategy boxType = Util.getBoxingStrategy(declaredParam);
                ProducedType type = getTypeForParameter(declaredParam, isRaw, typeArgumentModels);
                // if we can't pick up on the type from the declaration, revert to the type of the expression
                if(isTypeParameter(type))
                    type = expr.getTypeModel();
                JCExpression typeExpr = makeJavaType(type, (boxType == BoxingStrategy.BOXED) ? TYPE_ARGUMENT : 0);
                JCExpression argExpr = transformExpression(expr, boxType, type);
                JCVariableDecl varDecl = makeVar(varName, typeExpr, argExpr);
                vars.append(varDecl);
            }
            
            Tree.SequencedArgument sequencedArgument = ce.getNamedArgumentList().getSequencedArgument();
            if (sequencedArgument != null) {
                at(sequencedArgument);
                int index = namedArguments.size();
                String varName = varBaseName + "$" + index;
                JCExpression typeExpr = makeJavaType(lastDeclared.getType(), AbstractTransformer.WANT_RAW_TYPE);
                JCExpression argExpr = makeSequenceRaw(sequencedArgument.getExpressionList().getExpressions());
                JCVariableDecl varDecl = makeVar(varName, typeExpr, argExpr);
                vars.append(varDecl);
            } else if (lastDeclared != null 
                    && lastDeclared.isSequenced() 
                    && !boundSequenced) {
                int index = namedArguments.size();
                String varName = varBaseName + "$" + index;
                JCExpression typeExpr = makeJavaType(lastDeclared.getType(), AbstractTransformer.WANT_RAW_TYPE);
                JCVariableDecl varDecl = makeVar(varName, typeExpr, makeEmpty());
                vars.append(varDecl);
            } else {
                String containerName;
                if (Decl.withinClassOrInterface(primaryDecl)) {
                    containerName = ((Declaration)primaryDecl.getContainer()).getName();
                } else {
                    containerName = primaryDecl.getName();
                }
                String className = Util.getCompanionClassName(containerName);
                // append any arguments for defaulted parameters
                for (int ii = numPassed; ii < numDeclared; ii++) {
                    Parameter param = declaredParams.get(ii);
                    String varName = varBaseName + "$" + ii;
                    String methodName = Util.getDefaultedParamMethodName(primaryDecl, param);
                    List<JCExpression> arglist = (ii > 0) ? makeVarRefArgumentList(varBaseName, ii) : List.<JCExpression> nil();
                    JCExpression argExpr = at(ce).Apply(null, makeIdentOrSelect(null, className, methodName), arglist);
                    BoxingStrategy boxType = Util.getBoxingStrategy(param);
                    ProducedType type = getTypeForParameter(param, isRaw, typeArgumentModels);
                    JCExpression typeExpr = makeJavaType(type, (boxType == BoxingStrategy.BOXED) ? TYPE_ARGUMENT : 0);
                    JCVariableDecl varDecl = makeVar(varName, typeExpr, argExpr);
                    vars.append(varDecl);
                }
            }
            
            args.appendList(makeVarRefArgumentList(varBaseName, numDeclared));
        }

        return makeInvocation(ce, vars, args, typeArgs);
    }

    // Positional invocation
    
    private JCExpression transformPositionalInvocation(Tree.InvocationExpression ce) {
        ListBuffer<JCVariableDecl> vars = null;
        ListBuffer<JCExpression> args = new ListBuffer<JCExpression>();

        Declaration primaryDecl = ce.getPrimary().getDeclaration();
        Tree.PositionalArgumentList positional = ce.getPositionalArgumentList();

        java.util.List<ProducedType> typeArgumentModels = getTypeArguments(ce);
        List<JCExpression> typeArgs = transformTypeArguments(typeArgumentModels);
        boolean isRaw = typeArgs.isEmpty();
        
        java.util.List<ParameterList> paramLists = ((Functional)primaryDecl).getParameterLists();
        java.util.List<Parameter> declaredParams = paramLists.get(0).getParameters();
        int numDeclared = declaredParams.size();
        int numPassed = positional.getPositionalArguments().size();
        Parameter lastDeclaredParam = declaredParams.isEmpty() ? null : declaredParams.get(declaredParams.size() - 1); 
        if (lastDeclaredParam != null 
                && lastDeclaredParam.isSequenced()
                && positional.getEllipsis() == null // foo(sequence...) syntax => no need to box
                && numPassed >= (numDeclared -1)) {
            // => call to a varargs method
            // first, append the normal args
            for (int ii = 0; ii < numDeclared - 1; ii++) {
                Tree.PositionalArgument arg = positional.getPositionalArguments().get(ii);
                args.append(transformArg(arg.getExpression(), arg.getParameter(), isRaw, typeArgumentModels));
            }
            JCExpression boxed;
            // then, box the remaining passed arguments
            if (numDeclared -1 == numPassed) {
                // box as Empty
                boxed = makeEmpty();
            } else {
                // box with an ArraySequence<T>
                List<Tree.Expression> x = List.<Tree.Expression>nil();
                for (int ii = numDeclared - 1; ii < numPassed; ii++) {
                    Tree.PositionalArgument arg = positional.getPositionalArguments().get(ii);
                    x = x.append(arg.getExpression());
                }
                boxed = makeSequenceRaw(x);
            }
            args.append(boxed);
        } else if (numPassed < numDeclared) {
            vars = ListBuffer.lb();
            String varBaseName = aliasName("arg");
            String containerName;
            if (Decl.withinClassOrInterface(primaryDecl)) {
                containerName = ((Declaration)primaryDecl.getContainer()).getName();
            } else {
                containerName = primaryDecl.getName();
            }
            String className = Util.getCompanionClassName(containerName);
            // append the normal args
            for (Tree.PositionalArgument arg : positional.getPositionalArguments()) {
                at(arg);
                Parameter declaredParam = arg.getParameter();
                Tree.Expression expr = arg.getExpression();
                int index = declaredParams.indexOf(declaredParam);
                String varName = varBaseName + "$" + index;
                BoxingStrategy boxType = Util.getBoxingStrategy(declaredParam);
                ProducedType type = getTypeForParameter(declaredParam, isRaw, typeArgumentModels);
                // if we can't pick up on the type from the declaration, revert to the type of the expression
                if(isTypeParameter(type))
                    type = expr.getTypeModel();
                JCExpression typeExpr = makeJavaType(type, (boxType == BoxingStrategy.BOXED) ? TYPE_ARGUMENT : 0);
                JCExpression argExpr = transformExpression(expr, boxType, type);
                JCVariableDecl varDecl = makeVar(varName, typeExpr, argExpr);
                vars.append(varDecl);
            }
            // append any arguments for defaulted parameters
            for (int ii = numPassed; ii < numDeclared; ii++) {
                Parameter param = declaredParams.get(ii);
                String varName = varBaseName + "$" + ii;
                String methodName = Util.getDefaultedParamMethodName(primaryDecl, param);
                List<JCExpression> arglist = (ii > 0) ? makeVarRefArgumentList(varBaseName, ii) : List.<JCExpression> nil();
                JCExpression argExpr = at(ce).Apply(null, makeIdentOrSelect(null, className, methodName), arglist);
                BoxingStrategy boxType = Util.getBoxingStrategy(param);
                ProducedType type = getTypeForParameter(param, isRaw, typeArgumentModels);
                JCExpression typeExpr = makeJavaType(type, (boxType == BoxingStrategy.BOXED) ? TYPE_ARGUMENT : 0);
                JCVariableDecl varDecl = makeVar(varName, typeExpr, argExpr);
                vars.append(varDecl);
            }
            args.appendList(makeVarRefArgumentList(varBaseName, numDeclared));
        } else {
            // append the normal args
            for (Tree.PositionalArgument arg : positional.getPositionalArguments()) {
                args.append(transformArg(arg.getExpression(), arg.getParameter(), isRaw, typeArgumentModels));
            }
        }

        return makeInvocation(ce, vars, args, typeArgs);
    }

    // Make a list of $arg0, $arg1, ... , $argN
    private List<JCExpression> makeVarRefArgumentList(String varBaseName, int argCount) {
        List<JCExpression> names = List.<JCExpression> nil();
        for (int i = 0; i < argCount; i++) {
            names = names.append(make().Ident(names().fromString(varBaseName + "$" + i)));
        }
        return names;
    }

    private JCExpression makeInvocation(
            final Tree.InvocationExpression ce,
            final ListBuffer<JCVariableDecl> vars,
            final ListBuffer<JCExpression> args,
            final List<JCExpression> typeArgs) {
        at(ce);
        if (ce.getPrimary() instanceof Tree.BaseTypeExpression) {
            ProducedType classType = (ProducedType)((Tree.BaseTypeExpression)ce.getPrimary()).getTarget();
            JCExpression newExpr = make().NewClass(null, null, makeJavaType(classType, CLASS_NEW), args.toList(), null);
            return (vars != null) ? make().LetExpr(vars.toList(), newExpr) : newExpr;
        } else {
            JCExpression result = transformPrimary(ce.getPrimary(), new TermTransformer() {

                @Override
                public JCExpression transform(JCExpression primaryExpr, String selector) {
                    JCExpression tmpCallPrimExpr = null;
                    if (vars != null && primaryExpr != null && selector != null) {
                        // Prepare the first argument holding the primary for the call
                        String callVarName = aliasName("call");
                        JCExpression callVarExpr = make().Ident(names().fromString(callVarName));
                        ProducedType type = ((Tree.QualifiedMemberExpression)ce.getPrimary()).getTarget().getQualifyingType();
                        JCVariableDecl callVar = makeVar(callVarName, makeJavaType(type, NO_PRIMITIVES), primaryExpr);
                        vars.prepend(callVar);
                        tmpCallPrimExpr = makeIdentOrSelect(callVarExpr, selector);
                    } else {
                        tmpCallPrimExpr = makeIdentOrSelect(primaryExpr, selector);
                    }
                    JCExpression callExpr = make().Apply(typeArgs, tmpCallPrimExpr, args.toList());

                    if (vars != null) {
                        ProducedType returnType = ce.getTypeModel();
                        if (isVoid(returnType)) {
                            // void methods get wrapped like (let $arg$1=expr, $arg$0=expr in call($arg$0, $arg$1); null)
                            return make().LetExpr(vars.toList(), List.<JCStatement>of(make().Exec(callExpr)), makeNull());
                        } else {
                            // all other methods like (let $arg$1=expr, $arg$0=expr in call($arg$0, $arg$1))
                            return make().LetExpr(vars.toList(), callExpr);
                        }
                    } else {
                        return callExpr;
                    }
                }
                
            });
            return result;
        }
    }

    // Invocation helper functions
    
    private java.util.List<ProducedType> getTypeArguments(Tree.InvocationExpression ce) {
        if(ce.getPrimary() instanceof Tree.StaticMemberOrTypeExpression){
            return ((Tree.StaticMemberOrTypeExpression)ce.getPrimary()).getTypeArguments().getTypeModels();
        }
        return null;
    }

    private List<JCExpression> transformTypeArguments(java.util.List<ProducedType> typeArguments) {
        List<JCExpression> result = List.<JCExpression> nil();
        if(typeArguments != null){
            for (ProducedType arg : typeArguments) {
                // cancel type parameters and go raw if we can't specify them
                if(willEraseToObject(arg)
                        || isTypeParameter(arg))
                    return List.nil();
                result = result.append(makeJavaType(arg, AbstractTransformer.TYPE_ARGUMENT));
            }
        }
        return result;
    }
    
    // used by ClassDefinitionBuilder too, for super()
    JCExpression transformArg(Tree.Term expr, Parameter parameter, boolean isRaw, java.util.List<ProducedType> typeArgumentModels) {
        // deal with upstream errors, must have already been reported so let's not throw further
        if(parameter == null)
            return make().Erroneous();
        ProducedType type = getTypeForParameter(parameter, isRaw, typeArgumentModels);
        return transformExpression(expr, 
                Util.getBoxingStrategy(parameter), 
                type);
    }

    private ProducedType getTypeForParameter(Parameter parameter, boolean isRaw, java.util.List<ProducedType> typeArgumentModels) {
        ProducedType type = parameter.getType();
        if(isTypeParameter(type)){
            TypeParameter tp = (TypeParameter) type.getDeclaration();
            if(!isRaw && typeArgumentModels != null){
                // try to use the inferred type if we're not going raw
                Scope scope = parameter.getContainer();
                int typeParamIndex = getTypeParameterIndex(scope, tp);
                if(typeParamIndex != -1)
                    return typeArgumentModels.get(typeParamIndex);
            }
            if(tp.getSatisfiedTypes().size() >= 1){
                // try the first satisfied type
                type = tp.getSatisfiedTypes().get(0).getType();
                // unless it's erased, in which case try for more specific
                if(!willEraseToObject(type))
                    return type;
            }
        }
        return type;
    }

    private int getTypeParameterIndex(Scope scope, TypeParameter tp) {
        if(scope instanceof Method)
            return ((Method)scope).getTypeParameters().indexOf(tp);
        return ((ClassOrInterface)scope).getTypeParameters().indexOf(tp);
    }

    //
    // Member expressions

    private interface TermTransformer {
        JCExpression transform(JCExpression primaryExpr, String selector);
    }

    // Qualified members
    
    public JCExpression transform(Tree.QualifiedMemberExpression expr) {
        return transform(expr, null);
    }
    
    private JCExpression transform(Tree.QualifiedMemberExpression expr, TermTransformer transformer) {
        JCExpression result;
        if (expr.getMemberOperator() instanceof Tree.SafeMemberOp) {
            JCExpression primaryExpr = transformQualifiedMemberPrimary(expr);
            String tmpVarName = aliasName("safe");
            JCExpression typeExpr = makeJavaType(expr.getTarget().getQualifyingType(), NO_PRIMITIVES);
            JCExpression transExpr = transformMemberExpression(expr, makeIdent(tmpVarName), transformer);
            JCExpression testExpr = make().Binary(JCTree.NE, makeIdent(tmpVarName), makeNull());                
            JCExpression condExpr = make().Conditional(testExpr, transExpr, makeNull());
            result = makeLetExpr(tmpVarName, null, typeExpr, primaryExpr, condExpr);
        } else if (expr.getMemberOperator() instanceof Tree.SpreadOp) {
            result = transformSpreadOperator(expr, transformer);
        } else {
            JCExpression primaryExpr = transformQualifiedMemberPrimary(expr);
            result = transformMemberExpression(expr, primaryExpr, transformer);
        }
        return result;
    }

    private JCExpression transformSpreadOperator(Tree.QualifiedMemberExpression expr, TermTransformer transformer) {
        at(expr);
        
        String varBaseName = aliasName("spread");
        // sequence
        String srcSequenceName = varBaseName+"$0";
        ProducedType srcSequenceType = typeFact().getNonemptySequenceType(expr.getPrimary().getTypeModel());
        ProducedType srcElementType = typeFact().getElementType(srcSequenceType);
        JCExpression srcSequenceTypeExpr = makeJavaType(srcSequenceType, NO_PRIMITIVES);
        JCExpression srcSequenceExpr = transformExpression(expr.getPrimary(), BoxingStrategy.BOXED, srcSequenceType);

        // reset back here after transformExpression
        at(expr);

        // size, getSize() always unboxed, but we need to cast to int for Java array access
        String sizeName = varBaseName+"$2";
        JCExpression sizeType = make().TypeIdent(TypeTags.INT);
        JCExpression sizeExpr = make().TypeCast(syms().intType, make().Apply(null, 
                make().Select(makeIdent(srcSequenceName), names().fromString("getSize")), 
                List.<JCTree.JCExpression>nil()));
        
        // new array
        String newArrayName = varBaseName+"$4";
        JCExpression arrayElementType = makeJavaType(expr.getTarget().getType(), NO_PRIMITIVES);
        JCExpression newArrayType = make().TypeArray(arrayElementType);
        JCNewArray newArrayExpr = make().NewArray(arrayElementType, List.of(makeIdent(sizeName)), null);
        
        // return the new array
        JCExpression returnArrayType = makeJavaType(expr.getTarget().getType(), SATISFIES);
        JCExpression returnArrayIdent = make().QualIdent(syms().ceylonArraySequenceType.tsym);
        JCExpression returnArrayTypeExpr;
        // avoid putting type parameters such as j.l.Object
        if(returnArrayType != null)
            returnArrayTypeExpr = make().TypeApply(returnArrayIdent, List.of(returnArrayType));
        else // go raw
            returnArrayTypeExpr = returnArrayIdent;
        JCNewClass returnArray = make().NewClass(null, null, 
                returnArrayTypeExpr, 
                List.of(makeIdent(newArrayName)), null);
        
        // for loop
        Name indexVarName = names().fromString(aliasName("index"));
        // int index = 0
        JCStatement initVarDef = make().VarDef(make().Modifiers(0), indexVarName, make().TypeIdent(TypeTags.INT), makeInteger(0));
        List<JCStatement> init = List.of(initVarDef);
        // index < size
        JCExpression cond = make().Binary(JCTree.LT, make().Ident(indexVarName), makeIdent(sizeName));
        // index++
        JCExpression stepExpr = make().Unary(JCTree.POSTINC, make().Ident(indexVarName));
        List<JCExpressionStatement> step = List.of(make().Exec(stepExpr));
        
        // newArray[index]
        JCExpression dstArrayExpr = make().Indexed(makeIdent(newArrayName), make().Ident(indexVarName));
        // srcSequence.item(box(index))
        // index is always boxed
        JCExpression boxedIndex = boxType(make().Ident(indexVarName), typeFact().getIntegerDeclaration().getType());
        JCExpression sequenceItemExpr = make().Apply(null, 
                make().Select(makeIdent(srcSequenceName), names().fromString("item")),
                List.<JCExpression>of(boxedIndex));
        // item.member
        sequenceItemExpr = applyErasureAndBoxing(sequenceItemExpr, srcElementType, true, BoxingStrategy.BOXED, 
                expr.getTarget().getQualifyingType());
        JCExpression appliedExpr = transformMemberExpression(expr, sequenceItemExpr, transformer);
        // reset back here after transformMemberExpression
        at(expr);
        // we always need to box to put in array
        appliedExpr = boxUnboxIfNecessary(appliedExpr, expr, 
                expr.getTarget().getType(), BoxingStrategy.BOXED);
        // newArray[index] = box(srcSequence.item(box(index)).member)
        JCStatement body = make().Exec(make().Assign(dstArrayExpr, appliedExpr));
        
        // for
        JCForLoop forStmt = make().ForLoop(init, cond , step , body);
        
        // build the whole thing
        return makeLetExpr(varBaseName, 
                List.<JCStatement>of(forStmt), 
                srcSequenceTypeExpr, srcSequenceExpr,
                sizeType, sizeExpr,
                newArrayType, newArrayExpr,
                returnArray);
    }

    private JCExpression transformQualifiedMemberPrimary(Tree.QualifiedMemberExpression expr) {
        if(expr.getTarget() == null)
            return at(expr).Erroneous(List.<JCTree>nil());
        return transformExpression(expr.getPrimary(), BoxingStrategy.BOXED, 
                expr.getTarget().getQualifyingType());
    }
    
    // Base members
    
    public JCExpression transform(Tree.BaseMemberExpression expr) {
        return transformMemberExpression(expr, null, null);
    }

    private JCExpression transform(Tree.BaseMemberExpression expr, TermTransformer transformer) {
        return transformMemberExpression(expr, null, transformer);
    }
    
    private JCExpression transformPrimary(Tree.Primary primary, TermTransformer transformer) {
        if (primary instanceof Tree.QualifiedMemberExpression) {
            return transform((Tree.QualifiedMemberExpression)primary, transformer);
        } else if (primary instanceof Tree.BaseMemberExpression) {
            return transform((Tree.BaseMemberExpression)primary, transformer);
        } else {
            return makeIdent(primary.getDeclaration().getName());
        }
    }
    
    private JCExpression transformMemberExpression(Tree.StaticMemberOrTypeExpression expr, JCExpression primaryExpr, TermTransformer transformer) {
        JCExpression result = null;

        // do not throw, an error will already have been reported
        Declaration decl = expr.getDeclaration();
        if (decl == null) {
            return make().Erroneous(List.<JCTree>nil());
        }
        
        JCExpression qualExpr = null;
        String selector = null;
        if (decl instanceof Getter) {
            // invoke the getter
            if (decl.isToplevel()) {
                primaryExpr = null;
                qualExpr = makeIdentOrSelect(makeFQIdent(decl.getContainer().getQualifiedNameString()), Util.quoteIfJavaKeyword(decl.getName()), Util.getGetterName(decl.getName()));
                selector = null;
            } else if (decl.isClassMember()) {
                selector = Util.getGetterName(decl.getName());
            } else {
                // method local attr
                if (!isRecursiveReference(expr)) {
                    primaryExpr = makeIdentOrSelect(primaryExpr, decl.getName() + "$getter");
                }
                selector = Util.getGetterName(decl.getName());
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
                    primaryExpr = makeIdentOrSelect(makeFQIdent(decl.getContainer().getQualifiedNameString()), Util.quoteIfJavaKeyword(decl.getName()));
                    selector = Util.getGetterName(decl.getName());
                }
            } else if (Decl.isClassAttribute(decl)) {
                // invoke the getter
                selector = Util.getGetterName(decl.getName());
             } else if (decl.isCaptured() || decl.isShared()) {
                 // invoke the qualified getter
                 primaryExpr = makeIdentOrSelect(primaryExpr, decl.getName());
                 selector = Util.getGetterName(decl.getName());
            }
        } else if (decl instanceof Method) {
            if (Decl.withinMethod(decl)) {
                java.util.List<String> path = new LinkedList<String>();
                if (!isRecursiveReference(expr)) {
                    path.add(decl.getName());
                }
                path.add(Util.quoteIfJavaKeyword(decl.getName()));
                primaryExpr = null;
                qualExpr = makeIdent(path);
                selector = null;
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
                path.add(Util.quoteIfJavaKeyword(decl.getName()));
                primaryExpr = null;
                qualExpr = makeIdent(path);
                selector = null;
            } else {
                // not toplevel, not within method, must be a class member
                selector = Util.quoteMethodName(decl.getName());
            }
        }
        if (result == null) {
            boolean useGetter = !(decl instanceof Method);
            if (qualExpr == null && selector == null) {
                useGetter = decl.isClassOrInterfaceMember() && Util.isErasedAttribute(decl.getName());
                if (useGetter) {
                    selector = Util.quoteMethodName(decl.getName());
                } else {
                    selector = substitute(decl.getName());
                }
            }
            
            if (qualExpr == null) {
                qualExpr = primaryExpr;
            }
            
            if (transformer != null) {
                result = transformer.transform(qualExpr, selector);
            } else {
                result = makeIdentOrSelect(qualExpr, selector);
                if (useGetter) {
                    result = make().Apply(List.<JCTree.JCExpression>nil(),
                            result,
                            List.<JCTree.JCExpression>nil());
                }
            }
        }
        
        return result;
    }

    //
    // Array access

    public JCTree transform(Tree.IndexExpression access) {
        boolean safe = access.getIndexOperator() instanceof Tree.SafeIndexOp;

        // depends on the operator
        Tree.ElementOrRange elementOrRange = access.getElementOrRange();
        if(elementOrRange instanceof Tree.Element){
            Tree.Element element = (Tree.Element) elementOrRange;
            // let's see what types there are
            ProducedType leftType = access.getPrimary().getTypeModel();
            if(safe)
                leftType = access.getUnit().getDefiniteType(leftType);
            ProducedType leftCorrespondenceType = leftType.getSupertype(access.getUnit().getCorrespondenceDeclaration());
            ProducedType rightType = getTypeArgument(leftCorrespondenceType, 0);
            
            // do the index
            JCExpression index = transformExpression(element.getExpression(), BoxingStrategy.BOXED, rightType);

            // look at the lhs
            JCExpression lhs = transformExpression(access.getPrimary(), BoxingStrategy.BOXED, leftCorrespondenceType);

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
            // find the types
            ProducedType leftType = access.getPrimary().getTypeModel();
            ProducedType leftRangedType = leftType.getSupertype(access.getUnit().getRangedDeclaration());
            ProducedType rightType = getTypeArgument(leftRangedType, 0);
            // look at the lhs
            JCExpression lhs = transformExpression(access.getPrimary(), BoxingStrategy.BOXED, leftRangedType);
            // do the indices
            Tree.ElementRange range = (Tree.ElementRange) elementOrRange;
            JCExpression start = transformExpression(range.getLowerBound(), BoxingStrategy.BOXED, rightType);
            JCExpression end;
            if(range.getUpperBound() != null)
                end = transformExpression(range.getUpperBound(), BoxingStrategy.BOXED, rightType);
            else
                end = makeNull();
            // make a "lhs.span(start, end)" call
            return at(access).Apply(List.<JCTree.JCExpression>nil(), 
                    make().Select(lhs, names().fromString("span")), List.of(start, end));
        }
    }

    //
    // Assignment

    public JCExpression transform(Tree.AssignOp op) {
        return transformAssignment(op, op.getLeftTerm(), op.getRightTerm());
    }

    private JCExpression transformAssignment(Node op, Tree.Term leftTerm, Tree.Term rightTerm) {
        // FIXME: can this be anything else than a Primary?
        TypedDeclaration decl = (TypedDeclaration) ((Tree.Primary)leftTerm).getDeclaration();

        // Remember and disable inStatement for RHS
        boolean tmpInStatement = inStatement;
        inStatement = false;
        
        // right side
        final JCExpression rhs = transformExpression(rightTerm, Util.getBoxingStrategy(decl), decl.getType());

        if (tmpInStatement) {
            return transformAssignment(op, leftTerm, rhs);
        } else {
            ProducedType valueType = leftTerm.getTypeModel();
            return transformAssignAndReturnOperation(op, leftTerm, Util.getBoxingStrategy(decl) == BoxingStrategy.BOXED, 
                    valueType, valueType, new AssignAndReturnOperationFactory(){
                @Override
                public JCExpression getNewValue(JCExpression previousValue) {
                    return rhs;
                }
            });
        }
    }
    
    private JCExpression transformAssignment(final Node op, Tree.Term leftTerm, JCExpression rhs) {
        // left hand side can be either BaseMemberExpression, QualifiedMemberExpression or array access (M2)
        // TODO: array access (M2)
        JCExpression expr = null;
        if(leftTerm instanceof Tree.BaseMemberExpression)
            expr = null;
        else if(leftTerm instanceof Tree.QualifiedMemberExpression){
            Tree.QualifiedMemberExpression qualified = ((Tree.QualifiedMemberExpression)leftTerm);
            expr = transformExpression(qualified.getPrimary(), BoxingStrategy.BOXED, qualified.getTarget().getQualifyingType());
        }else{
            log.error("ceylon", "Not supported yet: "+op.getNodeType());
            return at(op).Erroneous(List.<JCTree>nil());
        }
        return transformAssignment(op, leftTerm, expr, rhs);
    }
    
    private JCExpression transformAssignment(Node op, Tree.Term leftTerm, JCExpression lhs, JCExpression rhs) {
        JCExpression result = null;

        // FIXME: can this be anything else than a Primary?
        TypedDeclaration decl = (TypedDeclaration) ((Tree.Primary)leftTerm).getDeclaration();

        boolean variable = decl.isVariable();
        
        at(op);
        String selector = Util.getSetterName(decl.getName());
        if (decl.isToplevel()) {
            // must use top level setter
            lhs = makeIdentOrSelect(makeFQIdent(decl.getContainer().getQualifiedNameString()), Util.quoteIfJavaKeyword(decl.getName()));
        } else if ((decl instanceof Getter)) {
            // must use the setter
            if (Decl.withinMethod(decl)) {
                lhs = makeIdentOrSelect(lhs, decl.getName() + "$setter");
            }
        } else if (variable && (Decl.isClassAttribute(decl))) {
            // must use the setter, nothing to do
        } else if (variable && (decl.isCaptured() || decl.isShared())) {
            // must use the qualified setter
            lhs = makeIdentOrSelect(lhs, decl.getName());
        } else {
            result = at(op).Assign(makeIdentOrSelect(lhs, decl.getName()), rhs);
        }
        
        if (result == null) {
            result = make().Apply(List.<JCTree.JCExpression>nil(),
                    makeIdentOrSelect(lhs, selector),
                    List.<JCTree.JCExpression>of(rhs));
        }
        
        return result;
    }
    
    //
    // Type helper functions

    private ProducedType getSupertype(Tree.Term term, Interface compoundType){
        return term.getTypeModel().getSupertype(compoundType);
    }

    private ProducedType getTypeArgument(ProducedType leftType) {
        if (leftType!=null && leftType.getTypeArguments().size()==1) {
            return leftType.getTypeArgumentList().get(0);
        }
        return null;
    }

    private ProducedType getTypeArgument(ProducedType leftType, int i) {
        if (leftType!=null && leftType.getTypeArguments().size() > i) {
            return leftType.getTypeArgumentList().get(i);
        }
        return null;
    }
    
    //
    // Helper functions
    
    private JCExpression makeIdentOrSelect(JCExpression expr, String name, String... names) {
        if (name != null) {
            if (expr != null) {
                return makeSelect(makeSelect(expr, name), names);
            } else {
                return makeSelect(makeIdent(name), names);
            }
        } else {
            return expr;
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

}
