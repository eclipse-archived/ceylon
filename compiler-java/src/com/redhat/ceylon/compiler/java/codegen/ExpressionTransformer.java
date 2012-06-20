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

package com.redhat.ceylon.compiler.java.codegen;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import com.redhat.ceylon.compiler.java.codegen.Operators.AssignmentOperatorTranslation;
import com.redhat.ceylon.compiler.java.codegen.Operators.OperatorTranslation;
import com.redhat.ceylon.compiler.java.codegen.Operators.OptimisationStrategy;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.model.LazyMethod;
import com.redhat.ceylon.compiler.loader.model.LazyValue;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.FunctionalParameter;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BooleanCondition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Comprehension;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Condition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.DefaultArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ExistsCondition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ExistsOrNonemptyCondition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ExpressionComprehensionClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ForComprehensionClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.FunctionArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IfComprehensionClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IsCondition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.KeyValueIterator;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NonemptyCondition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ValueIterator;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Variable;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
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

    static{
        // only there to make sure this class is initialised before the enums defined in it, otherwise we
        // get an initialisation error
        Operators.init();
    }
    
    private boolean inStatement = false;
    private boolean withinInvocation = false;
    private boolean withinCallableInvocation = false;
    private boolean withinSuperInvocation = false;
    
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
        JCStatement  result = at(op).Exec(transformAssignment(op, op.getBaseMemberExpression(), op.getSpecifierExpression().getExpression()));
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
        
        // Cope with things like ((expr))
        // FIXME: shouldn't that be in the visitor?
        Tree.Term term = expr;
        while (term instanceof Tree.Expression) {
            term = ((Tree.Expression)term).getTerm();
        }
        
        CeylonVisitor v = gen().visitor;
        final ListBuffer<JCTree> prevDefs = v.defs;
        final boolean prevInInitializer = v.inInitializer;
        final ClassDefinitionBuilder prevClassBuilder = v.classBuilder;
        JCExpression result = makeErroneous();
        try {
            v.defs = new ListBuffer<JCTree>();
            v.inInitializer = false;
            v.classBuilder = gen().current();
            term.visit(v);
            if (v.hasResult()) {
                result = v.getSingleResult();
            }
        } finally {
            v.classBuilder = prevClassBuilder;
            v.inInitializer = prevInInitializer;
            v.defs = prevDefs;
        }

        result = applyErasureAndBoxing(result, expr, boxingStrategy, expectedType);

        return result;
    }
    
    JCExpression transform(FunctionArgument farg) {
        Method model = farg.getDeclarationModel();
        ProducedType callableType = typeFact().getCallableType(model.getType());
        // TODO MPL
        CallableBuilder callableBuilder = CallableBuilder.anonymous(
                gen(),
                farg.getExpression(),
                model.getParameterLists().get(0),
                callableType);
        return callableBuilder.build();
    }
    
    //
    // Boxing and erasure of expressions
    
    private JCExpression applyErasureAndBoxing(JCExpression result, Tree.Term expr, BoxingStrategy boxingStrategy, ProducedType expectedType) {
        ProducedType exprType = expr.getTypeModel();
        boolean exprBoxed = !CodegenUtil.isUnBoxed(expr);
        return applyErasureAndBoxing(result, exprType, exprBoxed, boxingStrategy, expectedType);
    }
    
    JCExpression applyErasureAndBoxing(JCExpression result, ProducedType exprType,
            boolean exprBoxed,
            BoxingStrategy boxingStrategy, ProducedType expectedType) {
        return applyErasureAndBoxing(result, exprType, exprBoxed, boxingStrategy, expectedType, false);
    }
    
    JCExpression applyErasureAndBoxing(JCExpression result, ProducedType exprType,
            boolean exprBoxed,
            BoxingStrategy boxingStrategy, ProducedType expectedType, boolean forCompanion) {
        
        boolean canCast = false;
        
        if (expectedType != null
                // don't add cast to an erased type 
                && !willEraseToObject(expectedType)
                // don't add cast for null
                && !isNothing(exprType)) {
            if(willEraseToObject(exprType)){
                // Set the new expression type to a "clean" copy of the expected type
                // (without the underlying type, because the cast is always to a non-primitive)
                expectedType = getTypeOrSelfType(expectedType);
                exprType = expectedType.withoutUnderlyingType();
                // Erased types need a type cast
                JCExpression targetType = makeJavaType(expectedType, AbstractTransformer.JT_TYPE_ARGUMENT);
                result = make().TypeCast(targetType, result);
            }else 
                canCast = true;
        }

        // we must do the boxing after the cast to the proper type
        JCExpression ret = boxUnboxIfNecessary(result, exprBoxed, exprType, boxingStrategy);
        // now check if we need variance casts
        if (canCast) {
            ret = applyVarianceCasts(ret, exprType, exprBoxed, boxingStrategy, expectedType, forCompanion);
        }
        ret = applySelfTypeCasts(ret, exprType, exprBoxed, boxingStrategy, expectedType);
        ret = applyJavaTypeConversions(ret, exprType, expectedType, boxingStrategy);
        return ret;
    }

    private JCExpression applyVarianceCasts(JCExpression result, ProducedType exprType,
            boolean exprBoxed,
            BoxingStrategy boxingStrategy, ProducedType expectedType, boolean forCompanion) {
        // unboxed types certainly don't need casting for variance
        if(exprBoxed || boxingStrategy == BoxingStrategy.BOXED){
            VarianceCastResult varianceCastResult = getVarianceCastResult(expectedType, exprType);
            if(varianceCastResult != null){
                // Types with variance types need a type cast, let's start with a raw cast to get rid
                // of Java's type system constraint (javac doesn't grok multiple implementations of the same
                // interface with different type params, which the JVM allows)
                JCExpression targetType = makeJavaType(expectedType, AbstractTransformer.JT_RAW);
                // do not change exprType here since this is just a Java workaround
                result = make().TypeCast(targetType, result);
                // now, because a raw cast is losing a lot of info, can we do better?
                if(varianceCastResult.isBetterCastAvailable()){
                    // let's recast that to something finer than a raw cast
                    targetType = makeJavaType(varianceCastResult.castType, AbstractTransformer.JT_TYPE_ARGUMENT | (forCompanion ? JT_SATISFIES : 0));
                    result = make().TypeCast(targetType, result);
                }
            }
        }
        return result;
    }
    
    private ProducedType getTypeOrSelfType(ProducedType exprType) {
        final ProducedType selfType = exprType.getDeclaration().getSelfType();
        if (selfType != null) {
            return findTypeArgument(exprType, selfType.getDeclaration());
        }
        return exprType; 
    }
    
    private JCExpression applySelfTypeCasts(JCExpression result, ProducedType exprType,
            boolean exprBoxed,
            BoxingStrategy boxingStrategy, ProducedType expectedType) {
        if (expectedType == null) {
            return result;
        }
        final ProducedType selfType = exprType.getDeclaration().getSelfType();
        if (selfType != null) {
            if (selfType.isExactly(exprType) // self-type within its own scope
                    || !exprType.isExactly(expectedType, false)) { // XXX Is this right?
                final ProducedType castType = findTypeArgument(exprType, selfType.getDeclaration());
                // the fact that the original expr was or not boxed doesn't mean the current result is boxed or not
                // as boxing transformations occur before this method
                boolean resultBoxed = boxingStrategy == BoxingStrategy.BOXED
                        || (boxingStrategy == BoxingStrategy.INDIFFERENT && exprBoxed);
                JCExpression targetType = makeJavaType(castType, resultBoxed ? AbstractTransformer.JT_TYPE_ARGUMENT : 0);
                result = make().TypeCast(targetType, result);
            }
        }
        // Self type as a type arg:
        for (ProducedType typeArgument : expectedType.getTypeArgumentList()) {
            result = applyGenericSelfTypeCasts(result, expectedType, typeArgument);            
        }
        for (ProducedType typeArgument : exprType.getTypeArgumentList()) {
            result = applyGenericSelfTypeCasts(result, expectedType, typeArgument);
        }
        
        return result;
    }

    private JCExpression applyGenericSelfTypeCasts(JCExpression result, ProducedType expectedType,
            ProducedType typeArgument) {
        if (typeArgument.getDeclaration() != null 
                && typeArgument.getDeclaration().getSelfType() != null) {
            JCExpression targetType = makeJavaType(expectedType, AbstractTransformer.JT_TYPE_ARGUMENT );
            // Need a raw cast to cast away the type argument before casting its self type back
            JCExpression rawType = makeJavaType(expectedType, AbstractTransformer.JT_RAW);
            result = make().TypeCast(targetType, make().TypeCast(rawType, result));
        }
        return result;
    }

    private ProducedType findTypeArgument(ProducedType type, TypeDeclaration declaration) {
        if(type == null)
            return null;
        ProducedType typeArgument = type.getTypeArguments().get(declaration);
        if(typeArgument != null)
            return typeArgument;
        return findTypeArgument(type.getQualifyingType(), declaration);
    }

    private JCExpression applyJavaTypeConversions(JCExpression ret, ProducedType exprType, ProducedType expectedType, BoxingStrategy boxingStrategy) {
        ProducedType definiteExprType = simplifyType(exprType);
        String convertFrom = definiteExprType.getUnderlyingType();

        ProducedType definiteExpectedType = null;
        String convertTo = null;
        if(expectedType != null){
            definiteExpectedType = simplifyType(expectedType);
            convertTo = definiteExpectedType.getUnderlyingType();
        }
        // check for identity conversion
        if(convertFrom != null && convertFrom.equals(convertTo))
            return ret;
        boolean arrayUnbox = boxingStrategy == BoxingStrategy.UNBOXED && definiteExpectedType != null && isCeylonArray(definiteExpectedType);
        if(convertTo != null){
            if(convertTo.equals("byte"))
                ret = make().TypeCast(syms().byteType, ret);
            else if(convertTo.equals("short"))
                ret = make().TypeCast(syms().shortType, ret);
            else if(convertTo.equals("int"))
                ret = make().TypeCast(syms().intType, ret);
            else if(convertTo.equals("float"))
                ret = make().TypeCast(syms().floatType, ret);
            else if(convertTo.equals("char"))
                ret = make().TypeCast(syms().charType, ret);
            else if(convertTo.equals("byte[]"))
                ret = make().TypeCast(make().TypeArray(make().TypeIdent(TypeTags.BYTE)), ret);
            else if(convertTo.equals("short[]"))
                ret = make().TypeCast(make().TypeArray(make().TypeIdent(TypeTags.SHORT)), ret);
            else if(convertTo.equals("int[]"))
                ret = make().TypeCast(make().TypeArray(make().TypeIdent(TypeTags.INT)), ret);
            else if(convertTo.equals("long[]"))
                ret = make().TypeCast(make().TypeArray(make().TypeIdent(TypeTags.LONG)), ret);
            else if(convertTo.equals("float[]"))
                ret = make().TypeCast(make().TypeArray(make().TypeIdent(TypeTags.FLOAT)), ret);
            else if(convertTo.equals("double[]"))
                ret = make().TypeCast(make().TypeArray(make().TypeIdent(TypeTags.DOUBLE)), ret);
            else if(convertTo.equals("char[]"))
                ret = make().TypeCast(make().TypeArray(make().TypeIdent(TypeTags.CHAR)), ret);
            else if(convertTo.equals("boolean[]"))
                ret = make().TypeCast(make().TypeArray(make().TypeIdent(TypeTags.BOOLEAN)), ret);
            else if (arrayUnbox) {
                String ct = convertTo.substring(0, convertTo.length() - 2);
                ret = make().TypeCast(make().TypeArray(makeQuotedQualIdentFromString(ct)), ret);
            }
        } else if (arrayUnbox) {
            ProducedType ct = getArrayComponentType(definiteExpectedType);
            ret = make().TypeCast(make().TypeArray(makeJavaType(ct)), ret);
        }
        return ret;
    }
    
    /**
     * Gets the first type parameter from the type model representing a 
     * ceylon.language.Array<Element>.
     * @param typeModel
     * @return The component type of the Array.
     */
    protected ProducedType getArrayComponentType(ProducedType typeModel) {
        assert isCeylonArray(typeModel);
        return typeModel.getTypeArgumentList().get(0);
    }
    
    private static class VarianceCastResult {
        ProducedType castType;
        
        VarianceCastResult(ProducedType castType){
            this.castType = castType;
        }
        
        private VarianceCastResult(){}
        
        boolean isBetterCastAvailable(){
            return castType != null;
        }
    }
    
    private static final VarianceCastResult RawCastVarianceResult = new VarianceCastResult();

    private VarianceCastResult getVarianceCastResult(ProducedType expectedType, ProducedType exprType) {
        // exactly the same type, doesn't need casting
        if(exprType.isExactly(expectedType))
            return null;
        // if we're not trying to put it into an interface, there's no need
        if(!(expectedType.getDeclaration() instanceof Interface))
            return null;
        // the interface must have type arguments, otherwise we can't use raw types
        if(expectedType.getTypeArguments().isEmpty())
            return null;
        // see if any of those type arguments has variance
        boolean hasVariance = false;
        for(TypeParameter t : expectedType.getTypeArguments().keySet()){
            if(t.isContravariant() || t.isCovariant()){
                hasVariance = true;
                break;
            }
        }
        if(!hasVariance)
            return null;
        // see if we're inheriting the interface twice with different type parameters
        java.util.List<ProducedType> satisfiedTypes = new LinkedList<ProducedType>();
        for(ProducedType superType : exprType.getSupertypes()){
            if(superType.getDeclaration() == expectedType.getDeclaration())
                satisfiedTypes.add(superType);
        }
        // we need at least two instantiations
        if(satisfiedTypes.size() <= 1)
            return null;
        boolean needsCast = false;
        // we need at least one that differs
        for(ProducedType superType : satisfiedTypes){
            if(!exprType.isExactly(superType)){
                needsCast = true;
                break;
            }
        }
        // no cast needed if they are all the same type
        if(!needsCast)
            return null;
        // find the better cast match
        for(ProducedType superType : satisfiedTypes){
            if(expectedType.isExactly(superType))
                return new VarianceCastResult(superType);
        }
        // nothing better than a raw cast (Stef: not sure that can happen)
        return RawCastVarianceResult;
    }

    //
    // Literals
    
    JCExpression ceylonLiteral(String s) {
        JCLiteral lit = make().Literal(s);
        return lit;
    }

    public JCExpression transform(Tree.StringLiteral string) {
        String value = string
                .getText()
                .substring(1, string.getText().length() - 1);
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
        double value = Double.parseDouble(lit.getText());
        // Don't need to handle the negative infinity and negative zero cases 
        // because Ceylon Float literals have no sign
        if (value == Double.POSITIVE_INFINITY) {
            return makeErroneous(lit, "Literal so large it is indistinguishable from infinity");
        } else if (value == 0.0 && !lit.getText().equals("0.0")) {
            return makeErroneous(lit, "Literal so small it is indistinguishable from zero");
        }
        JCExpression expr = make().Literal(value);
        return expr;
    }

    private JCExpression integerLiteral(Node node, String num) {
        try {
            return make().Literal(Long.parseLong(num));
        } catch (NumberFormatException e) {
            return makeErroneous(node, "Literal outside representable range");
        }
    }
    
    public JCExpression transform(Tree.NaturalLiteral lit) {
        return integerLiteral(lit, lit.getText());
    }

    public JCExpression transformStringExpression(Tree.StringTemplate expr) {
        at(expr);
        JCExpression builder;
        builder = make().NewClass(null, null, makeFQIdent("java","lang","StringBuilder"), List.<JCExpression>nil(), null);

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
                String method = isCeylonCharacter(expression.getTypeModel()) ? "appendCodePoint" : "append";
                builder = make().Apply(null, makeSelect(builder, method), List.<JCExpression>of(transformExpression(expression, BoxingStrategy.UNBOXED, null)));
            } else {
                JCMethodInvocation formatted = make().Apply(null, makeSelect(transformExpression(expression), "toString"), List.<JCExpression>nil());
                builder = make().Apply(null, makeSelect(builder, "append"), List.<JCExpression>of(formatted));
            }
        }

        return make().Apply(null, makeSelect(builder, "toString"), List.<JCExpression>nil());
    }

    public JCExpression transform(Tree.SequenceEnumeration value) {
        at(value);
        if (value.getComprehension() != null) {
            return make().Apply(null, make().Select(transformComprehension(value.getComprehension()), names().fromString("getSequence")), List.<JCExpression>nil());
        } else if (value.getSequencedArgument() != null) {
            java.util.List<Tree.Expression> list = value.getSequencedArgument().getExpressionList().getExpressions();
            if (value.getSequencedArgument().getEllipsis() == null) {
                ProducedType seqElemType = typeFact().getIteratedType(value.getTypeModel());
                return makeSequence(list, seqElemType);
            } else {
                return make().Apply(null, make().Select(transformExpression(list.get(0)), names().fromString("getSequence")), List.<JCExpression>nil());
            }
        } else {
            return makeEmpty();
        }
    }

    public JCTree transform(Tree.This expr) {
        at(expr);
        if (needDollarThis(expr.getScope())) {
            return makeUnquotedIdent("$this");
        }
        if (isWithinCallableInvocation()) {
            return makeSelect(makeJavaType(expr.getTypeModel()), "this");
        } 
        return makeUnquotedIdent("this");    
    }

    public JCTree transform(Tree.Super expr) {
        at(expr);
        return makeUnquotedIdent("super");
    }

    public JCTree transform(Tree.Outer expr) {
        at(expr);
        ProducedType outerClass = com.redhat.ceylon.compiler.typechecker.model.Util.getOuterClassOrInterface(expr.getScope());
        final TypeDeclaration outerDeclaration = outerClass.getDeclaration();
        if (outerDeclaration instanceof Interface) {
            return makeSelect(makeJavaType(outerClass, JT_COMPANION | JT_RAW), "this");
        }
        return makeSelect(makeQuotedIdent(outerDeclaration.getName()), "this");
    }

    //
    // Unary and Binary operators that can be overridden
    
    //
    // Unary operators

    public JCExpression transform(Tree.NotOp op) {
        // No need for an erasure cast since Term must be Boolean and we never need to erase that
        JCExpression term = transformExpression(op.getTerm(), CodegenUtil.getBoxingStrategy(op), null);
        JCUnary jcu = at(op).Unary(JCTree.NOT, term);
        return jcu;
    }

    public JCExpression transform(Tree.IsOp op) {
        // we don't need any erasure type cast for an "is" test
        JCExpression expression = transformExpression(op.getTerm());
        at(op);
        String varName = tempName();
        JCExpression test = makeTypeTest(null, varName, op.getType().getTypeModel());
        return makeLetExpr(varName, List.<JCStatement>nil(), make().Type(syms().objectType), expression, test);
    }

    public JCTree transform(Tree.Nonempty op) {
        // we don't need any erasure type cast for a "nonempty" test
        JCExpression expression = transformExpression(op.getTerm());
        at(op);
        String varName = tempName();
        JCExpression test = makeNonEmptyTest(null, varName);
        return makeLetExpr(varName, List.<JCStatement>nil(), make().Type(syms().objectType), expression, test);
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
        if (op.getTerm() instanceof Tree.NaturalLiteral) {
            // To cope with -9223372036854775808 we can't just parse the 
            // number separately from the sign
            return integerLiteral(op.getTerm(), "-" + op.getTerm().getText());
        }
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

        OperatorTranslation operator = Operators.getOperator(op.getClass());
        if (operator == null) {
            return makeErroneous(op);
        }

        if(operator.getOptimisationStrategy(op, this).useJavaOperator()){
            // optimisation for unboxed types
            JCExpression expr = transformExpression(term, BoxingStrategy.UNBOXED, expectedType);
            // unary + is essentially a NOOP
            if(operator == OperatorTranslation.UNARY_POSITIVE)
                return expr;
            return make().Unary(operator.javacOperator, expr);
        }
        
        return make().Apply(null, makeSelect(transformExpression(term, BoxingStrategy.BOXED, expectedType), 
                Util.getGetterName(operator.ceylonMethod)), List.<JCExpression> nil());
    }

    //
    // Binary operators
    
    public JCExpression transform(Tree.NotEqualOp op) {
        OperatorTranslation operator = Operators.OperatorTranslation.BINARY_EQUAL;
        OptimisationStrategy optimisationStrategy = operator.getOptimisationStrategy(op, this);
        
        // we want it unboxed only if the operator is optimised
        // we don't care about the left erased type, since equals() is on Object
        JCExpression left = transformExpression(op.getLeftTerm(), optimisationStrategy.getBoxingStrategy(), null);
        // we don't care about the right erased type, since equals() is on Object
        JCExpression expr = transformOverridableBinaryOperator(op, operator, optimisationStrategy, left, null);
        return at(op).Unary(JCTree.NOT, expr);
    }

    public JCExpression transform(Tree.RangeOp op) {
        // we need to get the range bound type
        ProducedType comparableType = getSupertype(op.getLeftTerm(), op.getUnit().getComparableDeclaration());
        ProducedType paramType = getTypeArgument(comparableType);
        JCExpression lower = transformExpression(op.getLeftTerm(), BoxingStrategy.BOXED, paramType);
        JCExpression upper = transformExpression(op.getRightTerm(), BoxingStrategy.BOXED, paramType);
        ProducedType rangeType = typeFact().getRangeType(op.getLeftTerm().getTypeModel());
        JCExpression typeExpr = makeJavaType(rangeType, CeylonTransformer.JT_CLASS_NEW);
        return at(op).NewClass(null, null, typeExpr, List.<JCExpression> of(lower, upper), null);
    }

    public JCExpression transform(Tree.EntryOp op) {
        // no erasure cast needed for both terms
        JCExpression key = transformExpression(op.getLeftTerm());
        JCExpression elem = transformExpression(op.getRightTerm());
        ProducedType entryType = typeFact().getEntryType(op.getLeftTerm().getTypeModel(), op.getRightTerm().getTypeModel());
        JCExpression typeExpr = makeJavaType(entryType, CeylonTransformer.JT_CLASS_NEW);
        return at(op).NewClass(null, null, typeExpr , List.<JCExpression> of(key, elem), null);
    }

    public JCTree transform(Tree.DefaultOp op) {
        JCExpression left = transformExpression(op.getLeftTerm());
        JCExpression right = transformExpression(op.getRightTerm());
        String varName = tempName();
        JCExpression varIdent = makeUnquotedIdent(varName);
        JCExpression test = at(op).Binary(JCTree.NE, varIdent, makeNull());
        JCExpression cond = make().Conditional(test , varIdent, right);
        JCExpression typeExpr = makeJavaType(op.getTypeModel(), JT_NO_PRIMITIVES);
        return makeLetExpr(varName, null, typeExpr, left, cond);
    }

    public JCTree transform(Tree.ThenOp op) {
        JCExpression left = transformExpression(op.getLeftTerm(), CodegenUtil.getBoxingStrategy(op.getLeftTerm()), typeFact().getBooleanDeclaration().getType());
        JCExpression right = transformExpression(op.getRightTerm());
        return make().Conditional(left , right, makeNull());
    }
    
    public JCTree transform(Tree.InOp op) {
        JCExpression left = transformExpression(op.getLeftTerm(), BoxingStrategy.BOXED, typeFact().getObjectDeclaration().getType());
        JCExpression right = transformExpression(op.getRightTerm(), BoxingStrategy.BOXED, typeFact().getCategoryDeclaration().getType());
        String varName = tempName();
        JCExpression varIdent = makeUnquotedIdent(varName);
        JCExpression contains = at(op).Apply(null, makeSelect(right, "contains"), List.<JCExpression> of(varIdent));
        JCExpression typeExpr = makeJavaType(op.getLeftTerm().getTypeModel(), JT_NO_PRIMITIVES);
        return makeLetExpr(varName, null, typeExpr, left, contains);
    }

    // Logical operators
    
    public JCExpression transform(Tree.LogicalOp op) {
        OperatorTranslation operator = Operators.getOperator(op.getClass());
        if(operator == null){
            return makeErroneous(op, "Not supported yet: "+op.getNodeType());
        }
        // Both terms are Booleans and can't be erased to anything
        JCExpression left = transformExpression(op.getLeftTerm(), BoxingStrategy.UNBOXED, null);
        return transformLogicalOp(op, operator, left, op.getRightTerm());
    }

    private JCExpression transformLogicalOp(Node op, OperatorTranslation operator, 
            JCExpression left, Tree.Term rightTerm) {
        // Both terms are Booleans and can't be erased to anything
        JCExpression right = transformExpression(rightTerm, BoxingStrategy.UNBOXED, null);

        return at(op).Binary(operator.javacOperator, left, right);
    }

    // Comparison operators
    
    public JCExpression transform(Tree.IdenticalOp op){
        // The only thing which might be unboxed is boolean, and we can follow the rules of == for optimising it,
        // which are simple and require that both types be booleans to be unboxed, otherwise they must be boxed
        OptimisationStrategy optimisationStrategy = OperatorTranslation.BINARY_EQUAL.getOptimisationStrategy(op, this);
        JCExpression left = transformExpression(op.getLeftTerm(), optimisationStrategy.getBoxingStrategy(), null);
        JCExpression right = transformExpression(op.getRightTerm(), optimisationStrategy.getBoxingStrategy(), null);
        return at(op).Binary(JCTree.EQ, left, right);
    }
    
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
    
    public JCExpression transform(Tree.BitwiseOp op) {
    	JCExpression result = transformOverridableBinaryOperator(op, null, null);
    	JCExpression expectedType = makeJavaType(op.getTypeModel());
    	return make().TypeCast(expectedType, result);
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
        OperatorTranslation operator = Operators.getOperator(op.getClass());
        if (operator == null) {
            return makeErroneous(op);
        }
        OptimisationStrategy optimisationStrategy = operator.getOptimisationStrategy(op, this);

        JCExpression left = transformExpression(op.getLeftTerm(), optimisationStrategy.getBoxingStrategy(), leftType);
        return transformOverridableBinaryOperator(op, operator, optimisationStrategy, left, rightType);
    }

    private JCExpression transformOverridableBinaryOperator(Tree.BinaryOperatorExpression op, 
            OperatorTranslation originalOperator, OptimisationStrategy optimisatonStrategy, 
            JCExpression left, ProducedType rightType) {
        JCExpression result = null;
        
        JCExpression right = transformExpression(op.getRightTerm(), optimisatonStrategy.getBoxingStrategy(), rightType);

        // optimise if we can
        if(optimisatonStrategy.useJavaOperator()){
            return make().Binary(originalOperator.javacOperator, left, right);
        }

        boolean loseComparison = 
                originalOperator == OperatorTranslation.BINARY_SMALLER 
                || originalOperator == OperatorTranslation.BINARY_SMALL_AS 
                || originalOperator == OperatorTranslation.BINARY_LARGER
                || originalOperator == OperatorTranslation.BINARY_LARGE_AS;

        // for comparisons we need to invoke compare()
        OperatorTranslation actualOperator = originalOperator;
        if (loseComparison) {
            actualOperator = Operators.OperatorTranslation.BINARY_COMPARE;
            if (actualOperator == null) {
                return makeErroneous();
            }
        }

        result = at(op).Apply(null, makeSelect(left, actualOperator.ceylonMethod), List.of(right));

        if (loseComparison) {
            result = at(op).Apply(null, makeSelect(result, originalOperator.ceylonMethod), List.<JCExpression> nil());
        }

        return result;
    }

    //
    // Operator-Assignment expressions

    public JCExpression transform(final Tree.ArithmeticAssignmentOp op){
        final AssignmentOperatorTranslation operator = Operators.getAssignmentOperator(op.getClass());
        if(operator == null){
            return makeErroneous(op, "Not supported yet: "+op.getNodeType());
        }

        // see if we can optimise it
        if(op.getUnboxed() && CodegenUtil.isDirectAccessVariable(op.getLeftTerm())){
            return optimiseAssignmentOperator(op, operator);
        }
        
        // we can use unboxed types if both operands are unboxed
        final boolean boxResult = !op.getUnboxed();
        
        // find the proper type
        Interface compoundType = op.getUnit().getNumericDeclaration();
        if(op instanceof Tree.AddAssignOp){
            compoundType = op.getUnit().getSummableDeclaration();
        }else if(op instanceof Tree.RemainderAssignOp){
            compoundType = op.getUnit().getIntegralDeclaration();
        }
        
        final ProducedType leftType = getSupertype(op.getLeftTerm(), compoundType);
        final ProducedType rightType = getMostPreciseType(op.getLeftTerm(), getTypeArgument(leftType, 0));

        // we work on boxed types
        return transformAssignAndReturnOperation(op, op.getLeftTerm(), boxResult, 
                leftType, rightType, 
                new AssignAndReturnOperationFactory(){
            @Override
            public JCExpression getNewValue(JCExpression previousValue) {
                // make this call: previousValue OP RHS
                JCExpression ret = transformOverridableBinaryOperator(op, operator.binaryOperator, 
                        boxResult ? OptimisationStrategy.NONE : OptimisationStrategy.OPTIMISE, 
                        previousValue, rightType);
                ret = unAutoPromote(ret, rightType);
                return ret;
            }
        });
    }

    public JCExpression transform(final Tree.BitwiseAssignmentOp op){
        final AssignmentOperatorTranslation operator = Operators.getAssignmentOperator(op.getClass());
        if(operator == null){
            return makeErroneous(op, "Not supported yet: "+op.getNodeType());
        }
    	
        ProducedType valueType = op.getLeftTerm().getTypeModel();
        
        return transformAssignAndReturnOperation(op, op.getLeftTerm(), false, valueType, valueType, new AssignAndReturnOperationFactory() {
            @Override
            public JCExpression getNewValue(JCExpression previousValue) {
            	JCExpression result = transformOverridableBinaryOperator(op, operator.binaryOperator, OptimisationStrategy.NONE, previousValue, null);
            	JCExpression expectedType = makeJavaType(op.getTypeModel());
            	return make().TypeCast(expectedType, result);
            }
        });
    }

    public JCExpression transform(final Tree.LogicalAssignmentOp op){
        final AssignmentOperatorTranslation operator = Operators.getAssignmentOperator(op.getClass());
        if(operator == null){
            return makeErroneous(op, "Not supported yet: "+op.getNodeType());
        }
        
        // optimise if we can
        if(CodegenUtil.isDirectAccessVariable(op.getLeftTerm())){
            return optimiseAssignmentOperator(op, operator);
        }
        
        ProducedType valueType = op.getLeftTerm().getTypeModel();
        // we work on unboxed types
        return transformAssignAndReturnOperation(op, op.getLeftTerm(), false, 
                valueType, valueType, new AssignAndReturnOperationFactory(){
            @Override
            public JCExpression getNewValue(JCExpression previousValue) {
                // make this call: previousValue OP RHS
                return transformLogicalOp(op, operator.binaryOperator, 
                        previousValue, op.getRightTerm());
            }
        });
    }

    private JCExpression optimiseAssignmentOperator(final Tree.AssignmentOp op, final AssignmentOperatorTranslation operator) {
        // we don't care about their types since they're unboxed and we know it
        JCExpression left = transformExpression(op.getLeftTerm(), BoxingStrategy.UNBOXED, null);
        JCExpression right = transformExpression(op.getRightTerm(), BoxingStrategy.UNBOXED, null);
        return at(op).Assignop(operator.javacOperator, left, right);
    }

    // Postfix operator
    
    public JCExpression transform(Tree.PostfixOperatorExpression expr) {
        OperatorTranslation operator = Operators.getOperator(expr.getClass());
        if(operator == null){
            return makeErroneous(expr, "Not supported yet: "+expr.getNodeType());
        }
        
        OptimisationStrategy optimisationStrategy = operator.getOptimisationStrategy(expr, this);
        boolean canOptimise = optimisationStrategy.useJavaOperator();
        
        // only fully optimise if we don't have to access the getter/setter
        if(canOptimise && CodegenUtil.isDirectAccessVariable(expr.getTerm())){
            JCExpression term = transformExpression(expr.getTerm(), BoxingStrategy.UNBOXED, expr.getTypeModel());
            return at(expr).Unary(operator.javacOperator, term);
        }
        
        Tree.Term term = expr.getTerm();

        Interface compoundType = expr.getUnit().getOrdinalDeclaration();
        ProducedType valueType = getSupertype(expr.getTerm(), compoundType);
        ProducedType returnType = getMostPreciseType(term, getTypeArgument(valueType, 0));

        List<JCVariableDecl> decls = List.nil();
        List<JCStatement> stats = List.nil();
        JCExpression result = null;
        // we can optimise that case a bit sometimes
        boolean boxResult = !canOptimise;

        // attr++
        // (let $tmp = attr; attr = $tmp.getSuccessor(); $tmp;)
        if(term instanceof Tree.BaseMemberExpression){
            JCExpression getter = transform((Tree.BaseMemberExpression)term, null);
            at(expr);
            // Type $tmp = attr
            JCExpression exprType = makeJavaType(returnType, boxResult ? JT_NO_PRIMITIVES : 0);
            Name varName = names().fromString(tempName("op"));
            // make sure we box the results if necessary
            getter = applyErasureAndBoxing(getter, term, boxResult ? BoxingStrategy.BOXED : BoxingStrategy.UNBOXED, returnType);
            JCVariableDecl tmpVar = make().VarDef(make().Modifiers(0), varName, exprType, getter);
            decls = decls.prepend(tmpVar);

            // attr = $tmp.getSuccessor()
            JCExpression successor;
            if(canOptimise){
                // use +1/-1 if we can optimise a bit
                successor = make().Binary(operator == OperatorTranslation.UNARY_POSTFIX_INCREMENT ? JCTree.PLUS : JCTree.MINUS, 
                        make().Ident(varName), makeInteger(1));
                successor = unAutoPromote(successor, returnType);
            }else{
                successor = make().Apply(null, 
                                         makeSelect(make().Ident(varName), operator.ceylonMethod), 
                                         List.<JCExpression>nil());
                // make sure the result is boxed if necessary, the result of successor/predecessor is always boxed
                successor = boxUnboxIfNecessary(successor, true, term.getTypeModel(), CodegenUtil.getBoxingStrategy(term));
            }
            JCExpression assignment = transformAssignment(expr, term, successor);
            stats = stats.prepend(at(expr).Exec(assignment));

            // $tmp
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
            JCExpression exprType = makeJavaType(qualified.getTarget().getQualifyingType(), JT_NO_PRIMITIVES);
            Name varEName = names().fromString(tempName("opE"));
            JCVariableDecl tmpEVar = make().VarDef(make().Modifiers(0), varEName, exprType, e);

            // Type $tmpV = $tmpE.attr
            JCExpression attrType = makeJavaType(returnType, boxResult ? JT_NO_PRIMITIVES : 0);
            Name varVName = names().fromString(tempName("opV"));
            JCExpression getter = transformMemberExpression(qualified, make().Ident(varEName), null);
            // make sure we box the results if necessary
            getter = applyErasureAndBoxing(getter, term, boxResult ? BoxingStrategy.BOXED : BoxingStrategy.UNBOXED, returnType);
            JCVariableDecl tmpVVar = make().VarDef(make().Modifiers(0), varVName, attrType, getter);

            // define all the variables
            decls = decls.prepend(tmpVVar);
            decls = decls.prepend(tmpEVar);
            
            // $tmpE.attr = $tmpV.getSuccessor()
            JCExpression successor;
            if(canOptimise){
                // use +1/-1 if we can optimise a bit
                successor = make().Binary(operator == OperatorTranslation.UNARY_POSTFIX_INCREMENT ? JCTree.PLUS : JCTree.MINUS, 
                        make().Ident(varVName), makeInteger(1));
                successor = unAutoPromote(successor, returnType);
            }else{
                successor = make().Apply(null, 
                                         makeSelect(make().Ident(varVName), operator.ceylonMethod), 
                                         List.<JCExpression>nil());
                //  make sure the result is boxed if necessary, the result of successor/predecessor is always boxed
                successor = boxUnboxIfNecessary(successor, true, term.getTypeModel(), CodegenUtil.getBoxingStrategy(term));
            }
            JCExpression assignment = transformAssignment(expr, term, make().Ident(varEName), successor);
            stats = stats.prepend(at(expr).Exec(assignment));
            
            // $tmpV
            result = make().Ident(varVName);
        }else{
            return makeErroneous(term, "Not supported yet");
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
    
    public JCExpression transform(final Tree.PrefixOperatorExpression expr) {
        final OperatorTranslation operator = Operators.getOperator(expr.getClass());
        if(operator == null){
            return makeErroneous(expr, "Not supported yet: "+expr.getNodeType());
        }
        
        OptimisationStrategy optimisationStrategy = operator.getOptimisationStrategy(expr, this);
        final boolean canOptimise = optimisationStrategy.useJavaOperator();
        
        Term term = expr.getTerm();
        // only fully optimise if we don't have to access the getter/setter
        if(canOptimise && CodegenUtil.isDirectAccessVariable(term)){
            JCExpression jcTerm = transformExpression(term, BoxingStrategy.UNBOXED, expr.getTypeModel());
            return at(expr).Unary(operator.javacOperator, jcTerm);
        }

        Interface compoundType = expr.getUnit().getOrdinalDeclaration();
        ProducedType valueType = getSupertype(term, compoundType);
        final ProducedType returnType = getMostPreciseType(term, getTypeArgument(valueType, 0));
        
        // we work on boxed types unless we could have optimised
        return transformAssignAndReturnOperation(expr, term, !canOptimise, 
                valueType, returnType, new AssignAndReturnOperationFactory(){
            @Override
            public JCExpression getNewValue(JCExpression previousValue) {
                // use +1/-1 if we can optimise a bit
                if(canOptimise){
                    JCExpression ret = make().Binary(operator == OperatorTranslation.UNARY_PREFIX_INCREMENT ? JCTree.PLUS : JCTree.MINUS, 
                            previousValue, makeInteger(1));
                    ret = unAutoPromote(ret, returnType);
                    return ret;
                }
                // make this call: previousValue.getSuccessor() or previousValue.getPredecessor()
                return make().Apply(null, makeSelect(previousValue, operator.ceylonMethod), List.<JCExpression>nil());
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
            JCExpression exprType = makeJavaType(returnType, boxResult ? JT_NO_PRIMITIVES : 0);
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
            value = boxUnboxIfNecessary(value, boxResult, term.getTypeModel(), CodegenUtil.getBoxingStrategy(term));
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
            JCExpression exprType = makeJavaType(qualified.getTarget().getQualifyingType(), JT_NO_PRIMITIVES);
            Name varEName = names().fromString(tempName("opE"));
            JCVariableDecl tmpEVar = make().VarDef(make().Modifiers(0), varEName, exprType, e);

            // Type $tmpV = OP($tmpE.attr)
            JCExpression attrType = makeJavaType(returnType, boxResult ? JT_NO_PRIMITIVES : 0);
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
            value = boxUnboxIfNecessary(value, boxResult, term.getTypeModel(), CodegenUtil.getBoxingStrategy(term));
            JCExpression assignment = transformAssignment(operator, term, make().Ident(varEName), value);
            stats = stats.prepend(at(operator).Exec(assignment));
            
            // $tmpV
            // return, with the box type we asked for
            result = make().Ident(varVName);
        }else{
            return makeErroneous(operator, "Not supported yet");
        }
        // OP(e?.attr) is probably not legal
        // OP(a[i]) is not for M1 but will be:
        // (let $tmpA = a, $tmpI = i, $tmpV = OP($tmpA.item($tmpI)); $tmpA.setItem($tmpI, $tmpV); $tmpV;)
        // OP(a?[i]) is probably not legal
        // OP(a[i1..i1]) and OP(a[i1...]) are probably not legal
        // OP(a[].attr) and OP(a[].e.attr) are probably not legal

        return make().LetExpr(decls, stats, result);
    }


    public JCExpression transform(Tree.Parameter param) {
        // Transform the expression marking that we're inside a defaulted parameter for $this-handling
        //needDollarThis  = true;
        JCExpression expr;
        at(param);
        DefaultArgument defaultArgument = param.getDefaultArgument();
        if (defaultArgument != null) {
            SpecifierExpression spec = defaultArgument.getSpecifierExpression();
            expr = expressionGen().transformExpression(spec.getExpression(), CodegenUtil.getBoxingStrategy(param.getDeclarationModel()), param.getDeclarationModel().getType());
        } else if (param.getDeclarationModel().isSequenced()) {
            expr = makeEmpty();
        } else {
            expr = makeErroneous(param, "No default and not sequenced");
        }
        //needDollarThis = false;
        return expr;
    }
    
    //
    // Invocations
    
    public JCExpression transform(Tree.InvocationExpression ce) {
        return InvocationBuilder.forInvocation(this, ce).build();
    }
    
    public JCExpression transformFunctional(Tree.Term expr,
            Functional functional) {
        return CallableBuilder.methodReference(gen(), expr, functional.getParameterLists().get(0)).build();
    }

    //
    // Member expressions

    public static interface TermTransformer {
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
            JCExpression typeExpr = makeJavaType(expr.getTarget().getQualifyingType(), JT_NO_PRIMITIVES);
            JCExpression transExpr = transformMemberExpression(expr, makeUnquotedIdent(tmpVarName), transformer);
            if (!isWithinInvocation()
                    && isCeylonCallable(expr.getTypeModel())) {
                return transExpr;
            }
            transExpr = boxUnboxIfNecessary(transExpr, expr, expr.getTarget().getType(), BoxingStrategy.BOXED);
            JCExpression testExpr = make().Binary(JCTree.NE, makeUnquotedIdent(tmpVarName), makeNull());
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

        // this holds the ternary test for empty
        String testVarName = aliasName("spreadTest");
        ProducedType testSequenceType = typeFact().getFixedSizedType(expr.getPrimary().getTypeModel());
        JCExpression testSequenceTypeExpr = makeJavaType(testSequenceType, JT_NO_PRIMITIVES);
        JCExpression testSequenceExpr = transformExpression(expr.getPrimary(), BoxingStrategy.BOXED, testSequenceType);

        // reset back here after transformExpression
        at(expr);

        // this holds the whole spread operation
        String varBaseName = aliasName("spread");
        // sequence
        String srcSequenceName = varBaseName+"$0";
        ProducedType srcSequenceType = typeFact().getNonemptySequenceType(expr.getPrimary().getTypeModel());
        ProducedType srcElementType = typeFact().getElementType(srcSequenceType);
        JCExpression srcSequenceTypeExpr = makeJavaType(srcSequenceType, JT_NO_PRIMITIVES);
        JCExpression srcSequenceExpr = make().TypeCast(srcSequenceTypeExpr, makeUnquotedIdent(testVarName));

        // size, getSize() always unboxed, but we need to cast to int for Java array access
        String sizeName = varBaseName+"$2";
        JCExpression sizeType = make().TypeIdent(TypeTags.INT);
        JCExpression sizeExpr = make().TypeCast(syms().intType, make().Apply(null, 
                make().Select(makeUnquotedIdent(srcSequenceName), names().fromString("getSize")), 
                List.<JCTree.JCExpression>nil()));

        // new array
        String newArrayName = varBaseName+"$4";
        JCExpression arrayElementType = makeJavaType(expr.getTarget().getType(), JT_NO_PRIMITIVES);
        JCExpression newArrayType = make().TypeArray(arrayElementType);
        JCNewArray newArrayExpr = make().NewArray(arrayElementType, List.of(makeUnquotedIdent(sizeName)), null);

        // return the new array
        JCExpression returnArrayType = makeJavaType(expr.getTarget().getType(), JT_SATISFIES);
        JCExpression returnArrayIdent = make().QualIdent(syms().ceylonArraySequenceType.tsym);
        JCExpression returnArrayTypeExpr;
        // avoid putting type parameters such as j.l.Object
        if(returnArrayType != null)
            returnArrayTypeExpr = make().TypeApply(returnArrayIdent, List.of(returnArrayType));
        else // go raw
            returnArrayTypeExpr = returnArrayIdent;
        JCNewClass returnArray = make().NewClass(null, null, 
                returnArrayTypeExpr, 
                List.of(makeUnquotedIdent(newArrayName)), null);

        // for loop
        Name indexVarName = names().fromString(aliasName("index"));
        // int index = 0
        JCStatement initVarDef = make().VarDef(make().Modifiers(0), indexVarName, make().TypeIdent(TypeTags.INT), makeInteger(0));
        List<JCStatement> init = List.of(initVarDef);
        // index < size
        JCExpression cond = make().Binary(JCTree.LT, make().Ident(indexVarName), makeUnquotedIdent(sizeName));
        // index++
        JCExpression stepExpr = make().Unary(JCTree.POSTINC, make().Ident(indexVarName));
        List<JCExpressionStatement> step = List.of(make().Exec(stepExpr));

        // newArray[index]
        JCExpression dstArrayExpr = make().Indexed(makeUnquotedIdent(newArrayName), make().Ident(indexVarName));
        // srcSequence.item(box(index))
        // index is always boxed
        JCExpression boxedIndex = boxType(make().Ident(indexVarName), typeFact().getIntegerDeclaration().getType());
        JCExpression sequenceItemExpr = make().Apply(null, 
                make().Select(makeUnquotedIdent(srcSequenceName), names().fromString("item")),
                List.<JCExpression>of(boxedIndex));
        // item.member
        sequenceItemExpr = applyErasureAndBoxing(sequenceItemExpr, srcElementType, true, BoxingStrategy.BOXED, 
                expr.getTarget().getQualifyingType());
        JCExpression appliedExpr = transformMemberExpression(expr, sequenceItemExpr, transformer);
        
        // This short-circuit is here for spread invocations
        // The code has been called recursively and the part after this if-statement will
        // be handled by the previous recursion
        if (!isWithinInvocation() && isCeylonCallable(expr.getTypeModel())) {
            return appliedExpr;
        }
        
        // reset back here after transformMemberExpression
        at(expr);
        // we always need to box to put in array
        appliedExpr = boxUnboxIfNecessary(appliedExpr, expr, 
                expr.getTarget().getType(), BoxingStrategy.BOXED);
        // newArray[index] = box(srcSequence.item(box(index)).member)
        JCStatement body = make().Exec(make().Assign(dstArrayExpr, appliedExpr));
        
        // for
        JCForLoop forStmt = make().ForLoop(init, cond , step , body);
        
        // build the whole spread operation
        JCExpression spread = makeLetExpr(varBaseName, 
                List.<JCStatement>of(forStmt), 
                srcSequenceTypeExpr, srcSequenceExpr,
                sizeType, sizeExpr,
                newArrayType, newArrayExpr,
                returnArray);
        
        JCExpression resultExpr;

        if (typeFact().isEmptyType(expr.getPrimary().getTypeModel())) {
            ProducedType emptyOrSequence = typeFact().getEmptyType(typeFact().getSequenceType(expr.getTarget().getType()));
            resultExpr = make().TypeCast(makeJavaType(emptyOrSequence), 
                    make().Conditional(makeNonEmptyTest(makeUnquotedIdent(testVarName)), 
                        spread, makeEmpty()));
        } else {
            resultExpr = spread;
        }
        
        // now surround it with the test
        return makeLetExpr(testVarName, List.<JCStatement>nil(),
                testSequenceTypeExpr, testSequenceExpr,
                resultExpr);
    }

    private JCExpression transformQualifiedMemberPrimary(Tree.QualifiedMemberOrTypeExpression expr) {
        if(expr.getTarget() == null)
            return makeErroneous();
        return transformExpression(expr.getPrimary(), BoxingStrategy.BOXED, 
                expr.getTarget().getQualifyingType());
    }
    
    // Base members
    
    public JCExpression transform(Tree.BaseMemberExpression expr) {
        return transform(expr, null);
    }

    private JCExpression transform(Tree.BaseMemberOrTypeExpression expr, TermTransformer transformer) {
        return transformMemberExpression(expr, null, transformer);
    }
    
    // Type members
    
    public JCExpression transform(Tree.QualifiedTypeExpression expr) {
        return transform(expr, null);
    }
    
    public JCExpression transform(Tree.BaseTypeExpression expr) {
        return transform(expr, null);
    }
    
    private JCExpression transform(Tree.QualifiedTypeExpression expr, TermTransformer transformer) {
        JCExpression primaryExpr = transformQualifiedMemberPrimary(expr);
        return transformMemberExpression(expr, primaryExpr, transformer);
    }
    
    // Generic code for all primaries
    
    public JCExpression transformPrimary(Tree.Primary primary, TermTransformer transformer) {
        if (primary instanceof Tree.QualifiedMemberExpression) {
            return transform((Tree.QualifiedMemberExpression)primary, transformer);
        } else if (primary instanceof Tree.BaseMemberExpression) {
            return transform((Tree.BaseMemberExpression)primary, transformer);
        } else if (primary instanceof Tree.BaseTypeExpression) {
            return transform((Tree.BaseTypeExpression)primary, transformer);
        } else if (primary instanceof Tree.QualifiedTypeExpression) {
            return transform((Tree.QualifiedTypeExpression)primary, transformer);
        } else if (primary instanceof Tree.MemberOrTypeExpression) {
            return makeQuotedIdent(((Tree.MemberOrTypeExpression)primary).getDeclaration().getName());
        } else if (primary instanceof Tree.InvocationExpression){
            JCExpression primaryExpr = transform((Tree.InvocationExpression)primary);
            if (transformer != null) {
                primaryExpr = transformer.transform(primaryExpr, null);
            }
            return primaryExpr;
        } else {
            return makeErroneous(primary, "Unhandled primary");
        }
    }
    
    private JCExpression transformMemberExpression(Tree.StaticMemberOrTypeExpression expr, JCExpression primaryExpr, TermTransformer transformer) {
        JCExpression result = null;

        // do not throw, an error will already have been reported
        Declaration decl = expr.getDeclaration();
        if (decl == null) {
            return makeErroneous();
        }
        
        // Explanation: primaryExpr and qualExpr both specify what is to come before the selector
        // but the important difference is that primaryExpr is used for those situations where
        // the result comes from the actual Ceylon code while qualExpr is used for those situations
        // where we need to refer to synthetic objects (like wrapper classes for toplevel methods)
        
        JCExpression qualExpr = null;
        String selector = null;
        if (decl instanceof Functional
                && !(decl instanceof FunctionalParameter) // A functional parameter will already be Callable-wrapped
                && isCeylonCallable(expr.getTypeModel())
                && !isWithinInvocation()) {
            result = transformFunctional(expr, (Functional)decl);
        } else if (decl instanceof Getter) {
            // invoke the getter
            if (decl.isToplevel()) {
                primaryExpr = null;
                qualExpr = makeQualIdent(makeFQIdent(decl.getContainer().getQualifiedNameString()), Util.quoteIfJavaKeyword(decl.getName()), CodegenUtil.getGetterName(decl));
                selector = null;
            } else if (decl.isClassMember()
                        || decl.isInterfaceMember()) {
                selector = CodegenUtil.getGetterName(decl);
            } else {
                // method local attr
                if (!isRecursiveReference(expr)) {
                    primaryExpr = makeQualIdent(primaryExpr, decl.getName() + "$getter");
                }
                selector = CodegenUtil.getGetterName(decl);
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
                    String topClsName = (decl instanceof LazyValue) ? ((LazyValue)decl).getRealName() : decl.getName();
                    primaryExpr = makeQualIdent(makeFQIdent(Util.quoteJavaKeywords(decl.getContainer().getQualifiedNameString())), Util.quoteIfJavaKeyword(topClsName));
                    selector = CodegenUtil.getGetterName(decl);
                }
            } else if (Decl.isClassAttribute(decl)) {
                if (Decl.isJavaField(decl) || isWithinSuperInvocation()){
                    selector = decl.getName();
                } else {
                    // invoke the getter, using the Java interop form of Util.getGetterName because this is the only case
                    // (Value inside a Class) where we might refer to JavaBean properties
                    selector = CodegenUtil.getGetterName(decl);
                }
            } else if (decl.isCaptured() || decl.isShared()) {
                TypeDeclaration typeDecl = ((Value)decl).getType().getDeclaration();
                boolean isObject = Character.isLowerCase(typeDecl.getName().charAt(0));
                if (Decl.isLocal(typeDecl)
                        && isObject) {
                    // accessing a local 'object' declaration, so don't need a getter 
                } else if (decl.isCaptured() && !((Value) decl).isVariable()) {
                    // accessing a local that is not getter wrapped
                } else {
                    primaryExpr = makeQualIdent(primaryExpr, decl.getName());
                    selector = CodegenUtil.getGetterName(decl);
                }
            }
        } else if (decl instanceof Method) {
            if (Decl.isLocal(decl)) {
                java.util.List<String> path = new LinkedList<String>();
                if (!isRecursiveReference(expr)) {
                    path.add(decl.getName());
                } 
                primaryExpr = null;
                // Only want to quote the method name 
                // e.g. enum.$enum()
                qualExpr = makeQuotedQualIdent(makeQualIdent(path), CodegenUtil.quoteMethodName(decl));
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
                String topClsName = (decl instanceof LazyMethod) ? ((LazyMethod)decl).getRealName() : decl.getName();
                // class
                path.add(topClsName);
                // method
                path.add(CodegenUtil.quoteMethodName(decl));
                primaryExpr = null;
                qualExpr = makeQuotedQualIdent(path);
                selector = null;
            } else {
                // not toplevel, not within method, must be a class member
                selector = Util.getErasedMethodName(CodegenUtil.quoteMethodNameIfProperty((Method) decl, gen()));
            }
        }
        if (result == null) {
            boolean useGetter = !(decl instanceof Method) && !(Decl.isJavaField(decl)) && !isWithinSuperInvocation();
            if (qualExpr == null && selector == null) {
                useGetter = decl.isClassOrInterfaceMember() && CodegenUtil.isErasedAttribute(decl.getName());
                if (useGetter) {
                    selector = CodegenUtil.quoteMethodName(decl);
                } else {
                    selector = substitute(decl.getName());
                }
            }
            
            if (qualExpr == null) {
                qualExpr = primaryExpr;
            }
            
            if (qualExpr == null && needDollarThis(expr)) {
                qualExpr = makeUnquotedIdent("$this");
            }
            if (qualExpr == null && decl.isStaticallyImportable()) {
                qualExpr = makeQuotedFQIdent(decl.getContainer().getQualifiedNameString());
            }
            
            if (transformer != null) {
                result = transformer.transform(qualExpr, selector);
            } else {
                result = makeQualIdent(qualExpr, selector);
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

    private boolean needDollarThis(Tree.StaticMemberOrTypeExpression expr) {
        if (expr instanceof Tree.BaseMemberExpression) {
            // We need to add a `$this` prefix to the member expression if:
            // * The member was declared on an interface I and
            // * The member is being used in the companion class of I or 
            //   some subinterface of I, and 
            // * The member is shared (non-shared means its only on the companion class)
            final Declaration decl = expr.getDeclaration();
            
            // Find the method/getter/setter where the expr is being used
            Scope scope = expr.getScope();
            while (Decl.isLocalScope(scope)) {
                scope = scope.getContainer();
            }
            // Is it being used in an interface (=> impl) which is a subtyle of the declaration
            if (scope instanceof Interface
                    && ((Interface) scope).getType().isSubtypeOf(scope.getDeclaringType(decl))) {
                return decl.isShared();
            }
        }
        return false;
    }
    
    private boolean needDollarThis(Scope scope) {
        while (Decl.isLocalScope(scope)) {
            scope = scope.getContainer();
        }
        return scope instanceof Interface;
    }

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
        // FIXME: can this be anything else than a Tree.MemberOrTypeExpression?
        TypedDeclaration decl = (TypedDeclaration) ((Tree.MemberOrTypeExpression)leftTerm).getDeclaration();

        // Remember and disable inStatement for RHS
        boolean tmpInStatement = inStatement;
        inStatement = false;
        
        // right side
        final JCExpression rhs = transformExpression(rightTerm, CodegenUtil.getBoxingStrategy(decl), leftTerm.getTypeModel());

        if (tmpInStatement) {
            return transformAssignment(op, leftTerm, rhs);
        } else {
            ProducedType valueType = leftTerm.getTypeModel();
            return transformAssignAndReturnOperation(op, leftTerm, CodegenUtil.getBoxingStrategy(decl) == BoxingStrategy.BOXED, 
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
            if (needDollarThis((Tree.BaseMemberExpression)leftTerm)) {
                expr = makeUnquotedIdent("$this");
            } else {
                expr = null;
            }
        else if(leftTerm instanceof Tree.QualifiedMemberExpression){
            Tree.QualifiedMemberExpression qualified = ((Tree.QualifiedMemberExpression)leftTerm);
            expr = transformExpression(qualified.getPrimary(), BoxingStrategy.BOXED, qualified.getTarget().getQualifyingType());
        }else{
            return makeErroneous(op, "Not supported yet: "+op.getNodeType());
        }
        return transformAssignment(op, leftTerm, expr, rhs);
    }
    
    private JCExpression transformAssignment(Node op, Tree.Term leftTerm, JCExpression lhs, JCExpression rhs) {
        JCExpression result = null;

        // FIXME: can this be anything else than a Tree.MemberOrTypeExpression?
        TypedDeclaration decl = (TypedDeclaration) ((Tree.MemberOrTypeExpression)leftTerm).getDeclaration();

        boolean variable = decl.isVariable();
        
        at(op);
        String selector = CodegenUtil.getSetterName(decl);
        if (decl.isToplevel()) {
            // must use top level setter
            lhs = makeQualIdent(makeFQIdent(Util.quoteJavaKeywords(decl.getContainer().getQualifiedNameString())), Util.quoteIfJavaKeyword(decl.getName()));
        } else if ((decl instanceof Getter)) {
            // must use the setter
            if (Decl.isLocal(decl)) {
                lhs = makeQualIdent(lhs, decl.getName() + "$setter");
            }
        } else if (decl instanceof Method
                && !Decl.withinClassOrInterface(decl)) {
            // Deferred method initialization of a local function
            result = at(op).Assign(makeQualIdent(lhs, decl.getName(), decl.getName()), rhs);
        } else if (variable && (Decl.isClassAttribute(decl))) {
            // must use the setter, nothing to do, unless it's a java field
            if(Decl.isJavaField(decl)){
                if (decl.isStaticallyImportable()) {
                    // static field
                    result = at(op).Assign(makeQualIdent(makeQuotedFQIdent(decl.getContainer().getQualifiedNameString()), decl.getName()), rhs);
                }else{
                    // normal field
                    result = at(op).Assign(makeQualIdent(lhs, decl.getName()), rhs);
                }
            }
        } else if (variable && (decl.isCaptured() || decl.isShared())) {
            // must use the qualified setter
            lhs = makeQualIdent(lhs, decl.getName());
        } else {
            result = at(op).Assign(makeQualIdent(lhs, decl.getName()), rhs);
        }
        
        if (result == null) {
            result = make().Apply(List.<JCTree.JCExpression>nil(),
                    makeQualIdent(lhs, selector),
                    List.<JCTree.JCExpression>of(rhs));
        }
        
        return result;
    }

    /** Creates an anonymous class that extends Iterable and implements the specified comprehension.
     */
    public JCExpression transformComprehension(Comprehension comp) {
        at(comp);
        Tree.ComprehensionClause clause = comp.getForComprehensionClause();
        ProducedType targetIterType = typeFact().getIterableType(clause.getTypeModel());
        int idx = 0;
        ExpressionComprehensionClause excc = null;
        String prevItemVar = null;
        String ctxtName = null;
        //Iterator fields
        ListBuffer<JCTree> fields = new ListBuffer<JCTree>();
        HashSet<String> fieldNames = new HashSet<String>();
        while (clause != null) {
            final String iterVar = "iter$"+idx;
            String itemVar = null;
            //spread 1162
            if (clause instanceof ForComprehensionClause) {

                ForComprehensionClause fcl = (ForComprehensionClause)clause;
                SpecifierExpression specexpr = fcl.getForIterator().getSpecifierExpression();
                ProducedType iterType = specexpr.getExpression().getTypeModel();
                JCExpression iterTypeExpr = makeJavaType(typeFact().getIteratorType(
                        typeFact().getIteratedType(iterType)));
                if (clause == comp.getForComprehensionClause()) {
                    //The first iterator can be initialized as a field
                    fields.add(make().VarDef(make().Modifiers(2), names().fromString(iterVar), iterTypeExpr,
                        make().Apply(null, make().Select(transformExpression(specexpr.getExpression()),
                            names().fromString("getIterator")), List.<JCExpression>nil())));
                    fieldNames.add(iterVar);
                } else {
                    //The subsequent iterators need to be inside a method,
                    //in case they depend on the current element of the previous iterator
                    fields.add(make().VarDef(make().Modifiers(2), names().fromString(iterVar), iterTypeExpr, null));
                    fieldNames.add(iterVar);
                    JCBlock body = make().Block(0l, List.<JCStatement>of(
                            make().If(make().Binary(JCTree.EQ, makeUnquotedIdent(iterVar), makeNull()),
                                    make().Exec(make().Apply(null, makeSelect("this", ctxtName), List.<JCExpression>nil())),
                                    null),
                            make().Exec(make().Assign(makeUnquotedIdent(iterVar), make().Apply(null,
                                    make().Select(transformExpression(specexpr.getExpression()),
                                    names().fromString("getIterator")), List.<JCExpression>nil()))),
                            make().Return(makeUnquotedIdent(iterVar))
                    ));
                    fields.add(make().MethodDef(make().Modifiers(2),
                            names().fromString(iterVar), iterTypeExpr, List.<JCTree.JCTypeParameter>nil(),
                            List.<JCTree.JCVariableDecl>nil(), List.<JCExpression>nil(), body, null));
                }
                if (fcl.getForIterator() instanceof ValueIterator) {

                    //Add the item variable as a field in the iterator
                    Value item = ((ValueIterator)fcl.getForIterator()).getVariable().getDeclarationModel();
                    itemVar = item.getName();
                    fields.add(make().VarDef(make().Modifiers(2), names().fromString(itemVar), makeJavaType(item.getType(),JT_NO_PRIMITIVES), null));
                    fieldNames.add(itemVar);

                } else if (fcl.getForIterator() instanceof KeyValueIterator) {
                    //Add the key and value variables as fields in the iterator
                    KeyValueIterator kviter = (KeyValueIterator)fcl.getForIterator();
                    Value kdec = kviter.getKeyVariable().getDeclarationModel();
                    Value vdec = kviter.getValueVariable().getDeclarationModel();
                    //But we'll use this as the name for the context function and base for the exhausted field
                    itemVar = "kv$" + kdec.getName() + "$" + vdec.getName();
                    fields.add(make().VarDef(make().Modifiers(2), names().fromString(kdec.getName()),
                            makeJavaType(kdec.getType(), JT_NO_PRIMITIVES), null));
                    fields.add(make().VarDef(make().Modifiers(2), names().fromString(vdec.getName()),
                            makeJavaType(vdec.getType(), JT_NO_PRIMITIVES), null));
                    fieldNames.add(kdec.getName());
                    fieldNames.add(vdec.getName());
                } else {
                    return makeErroneous(fcl, "No support yet for iterators of type " + fcl.getForIterator().getClass().getName());
                }
                fields.add(make().VarDef(make().Modifiers(2), names().fromString(itemVar+"$exhausted"),
                        makeJavaType(typeFact().getBooleanDeclaration().getType()), null));

                //Now the context for this iterator
                ListBuffer<JCStatement> contextBody = new ListBuffer<JCStatement>();
                if (idx>0) {
                    //Subsequent iterators may depend on the item from the previous loop so we make sure we have one
                    contextBody.add(make().If(make().Binary(JCTree.EQ, makeUnquotedIdent(iterVar), makeNull()),
                            make().Exec(make().Apply(null, makeSelect("this", iterVar), List.<JCExpression>nil())), null));
                }

                //Assign the next item to an Object variable
                String tmpItem = tempName("item");
                contextBody.add(make().VarDef(make().Modifiers(0), names().fromString(tmpItem),
                        makeJavaType(typeFact().getObjectDeclaration().getType()),
                        make().Apply(null, make().Select(makeUnquotedIdent(iterVar), names().fromString("next")), List.<JCExpression>nil())));
                //Then we check if it's exhausted
                contextBody.add(make().Exec(make().Assign(makeUnquotedIdent(itemVar+"$exhausted"),
                        make().Binary(JCTree.EQ, makeUnquotedIdent(tmpItem), makeFinished()))));
                //Variables get assigned in the else block
                ListBuffer<JCStatement> elseBody = new ListBuffer<JCStatement>();
                if (fcl.getForIterator() instanceof ValueIterator) {
                    ProducedType itemType = ((ValueIterator)fcl.getForIterator()).getVariable().getDeclarationModel().getType();
                    elseBody.add(make().Exec(make().Assign(makeUnquotedIdent(itemVar),
                            make().TypeCast(makeJavaType(itemType,JT_NO_PRIMITIVES), makeUnquotedIdent(tmpItem)))));
                } else {
                    KeyValueIterator kviter = (KeyValueIterator)fcl.getForIterator();
                    Value key = kviter.getKeyVariable().getDeclarationModel();
                    Value item = kviter.getValueVariable().getDeclarationModel();
                    //Assign the key and item to the corresponding fields with the proper type casts
                    //equivalent to k=(KeyType)((Entry<KeyType,ItemType>)tmpItem).getKey()
                    JCExpression castEntryExpr = make().TypeCast(
                        makeJavaType(typeFact().getIteratedType(iterType)),
                        makeUnquotedIdent(tmpItem));
                    elseBody.add(make().Exec(make().Assign(makeUnquotedIdent(key.getName()),
                        make().TypeCast(makeJavaType(key.getType(), JT_NO_PRIMITIVES),
                            make().Apply(null, make().Select(castEntryExpr, names().fromString("getKey")),
                                List.<JCExpression>nil())
                    ))));
                    //equivalent to v=(ItemType)((Entry<KeyType,ItemType>)tmpItem).getItem()
                    elseBody.add(make().Exec(make().Assign(makeUnquotedIdent(item.getName()),
                        make().TypeCast(makeJavaType(item.getType(), JT_NO_PRIMITIVES),
                            make().Apply(null, make().Select(castEntryExpr, names().fromString("getItem")),
                                List.<JCExpression>nil())
                    ))));
                }
                ListBuffer<JCStatement> innerBody = new ListBuffer<JCStatement>();
                if (idx>0) {
                    //Subsequent contexts run once for every iteration of the previous loop
                    //This will reset our previous context by getting a new iterator if the previous loop isn't done
                    innerBody.add(make().If(make().Apply(null, makeSelect("this", ctxtName), List.<JCExpression>nil()),
                            make().Block(0, List.<JCStatement>of(
                                make().Exec(make().Assign(makeUnquotedIdent(iterVar),
                                        make().Apply(null, makeSelect("this", iterVar), List.<JCExpression>nil()))),
                                make().Return(make().Apply(null,
                                        make().Select(makeUnquotedIdent("this"),
                                        names().fromString(itemVar)), List.<JCExpression>nil()))
                    )), null));
                }
                innerBody.add(make().Return(makeBoolean(false)));
                //Assign the next item to the corresponding variables if not exhausted yet
                contextBody.add(make().If( makeUnquotedIdent(itemVar+"$exhausted"),
                    make().Block(0, innerBody.toList()),
                    make().Block(0, elseBody.toList())));
                contextBody.add(make().Return(makeBoolean(true)));
                //Create the context method that returns the next item for this iterator
                ctxtName = itemVar;
                fields.add(make().MethodDef(make().Modifiers(2), names().fromString(itemVar),
                    makeJavaType(typeFact().getBooleanDeclaration().getType()),
                    List.<JCTree.JCTypeParameter>nil(), List.<JCTree.JCVariableDecl>nil(), List.<JCExpression>nil(),
                    make().Block(0, contextBody.toList()), null));
                clause = fcl.getComprehensionClause();

            } else if (clause instanceof IfComprehensionClause) {

                Condition cond = ((IfComprehensionClause)clause).getCondition();
                //The context of an if is an iteration through the parent, checking each element against the condition
                Variable var = null;
                boolean reassign = false;
                if (cond instanceof IsCondition || cond instanceof ExistsOrNonemptyCondition) {
                    var = cond instanceof IsCondition ? ((IsCondition)cond).getVariable()
                            : ((ExistsOrNonemptyCondition)cond).getVariable();
                    //Initialize the condition's attribute to finished so that this is returned
                    //in case the condition is not met and the iterator is exhausted
                    if (!fieldNames.contains(var.getDeclarationModel().getName())) {
                        fields.add(make().VarDef(make().Modifiers(2), names().fromString(var.getDeclarationModel().getName()),
                                makeJavaType(var.getDeclarationModel().getType(),JT_NO_PRIMITIVES), null));
                        reassign = true;
                    }
                }
                //Filter contexts need to check if the previous context applies and then check the condition
                JCExpression condExpr = make().Apply(null,
                    make().Select(makeUnquotedIdent("this"), names().fromString(ctxtName)), List.<JCExpression>nil());
                //_AND_ the previous iterator condition with the comprehension's
                final JCExpression otherCondition;
                if (cond instanceof IsCondition) {
                    JCExpression _expr = transformExpression(var.getSpecifierExpression().getExpression());
                    String _varName = tempName("compr");
                    JCExpression test = makeTypeTest(null, _varName, ((IsCondition) cond).getType().getTypeModel());
                    test = makeLetExpr(_varName, List.<JCStatement>nil(), make().Type(syms().objectType), _expr, test);
                    if (reassign) {
                        _expr = make().Assign(makeUnquotedIdent(var.getDeclarationModel().getName()),
                                make().Conditional(test, make().TypeCast(makeJavaType(var.getDeclarationModel().getType(), JT_NO_PRIMITIVES), _expr), makeNull()));
                        otherCondition = make().Binary(JCTree.EQ, _expr, makeNull());
                    } else {
                        otherCondition = make().Unary(JCTree.NOT, test);
                    }

                } else if (cond instanceof ExistsCondition) {
                    JCExpression expression = transformExpression(var.getSpecifierExpression().getExpression());
                    if (reassign) {
                        //Assign the expression, check it's not null
                        expression = make().Assign(makeUnquotedIdent(var.getDeclarationModel().getName()), expression);
                    }
                    otherCondition =  make().Binary(JCTree.EQ, expression, makeNull());

                } else if (cond instanceof NonemptyCondition) {
                    JCExpression expression = transformExpression(var.getSpecifierExpression().getExpression());
                    String varName = tempName("compr");
                    JCExpression test = makeNonEmptyTest(null, varName);
                    test = makeLetExpr(varName, List.<JCStatement>nil(), make().Type(syms().objectType), expression, test);
                    if (reassign) {
                        //Assign the expression if it's nonempty
                        expression = make().Assign(makeUnquotedIdent(var.getDeclarationModel().getName()),
                                make().Conditional(test, make().TypeCast(makeJavaType(var.getDeclarationModel().getType(), JT_NO_PRIMITIVES), expression), makeNull()));
                        otherCondition = make().Binary(JCTree.EQ, expression, makeNull());
                    } else {
                        otherCondition = make().Unary(JCTree.NOT, test);
                    }

                } else if (cond instanceof BooleanCondition) {
                    otherCondition = make().Unary(JCTree.NOT, transformExpression(((BooleanCondition) cond).getExpression(),
                        BoxingStrategy.UNBOXED, typeFact().getBooleanDeclaration().getType()));
                } else {
                    return makeErroneous(cond, "This type of condition is not supported yet for comprehensions");
                }
                condExpr = make().Binary(JCTree.AND, condExpr, otherCondition);
                //Create the context method that filters from the last iterator
                ctxtName = "next"+idx;
                fields.add(make().MethodDef(make().Modifiers(2), names().fromString(ctxtName),
                    makeJavaType(typeFact().getBooleanDeclaration().getType()),
                    List.<JCTree.JCTypeParameter>nil(), List.<JCTree.JCVariableDecl>nil(),
                    List.<JCExpression>nil(), make().Block(0, List.<JCStatement>of(
                        make().WhileLoop(condExpr, make().Block(0, List.<JCStatement>nil())),
                        make().Return(make().Unary(JCTree.NOT, makeUnquotedIdent(prevItemVar+"$exhausted")))
                )), null));
                clause = ((IfComprehensionClause)clause).getComprehensionClause();
                itemVar = prevItemVar;

            } else if (clause instanceof ExpressionComprehensionClause) {

                //Just keep a reference to the expression
                excc = (ExpressionComprehensionClause)clause;
                at(excc);
                clause = null;

            } else {
                return makeErroneous(clause, "No support for comprehension clause of type " + clause.getClass().getName());
            }
            idx++;
            if (itemVar != null) prevItemVar = itemVar;
        }

        //Define the next() method for the Iterator
        fields.add(make().MethodDef(make().Modifiers(1), names().fromString("next"),
            makeJavaType(typeFact().getObjectDeclaration().getType()), List.<JCTree.JCTypeParameter>nil(),
            List.<JCTree.JCVariableDecl>nil(), List.<JCExpression>nil(), make().Block(0, List.<JCStatement>of(
                make().Return(
                    make().Conditional(
                        make().Apply(null, make().Select(makeUnquotedIdent("this"),
                            names().fromString(ctxtName)), List.<JCExpression>nil()),
                        transformExpression(excc.getExpression(), BoxingStrategy.BOXED, typeFact().getIteratedType(targetIterType)),
                        makeFinished()))
        )), null));
        //Define the inner iterator class
        ProducedType iteratorType = typeFact().getIteratorType(typeFact().getIteratedType(targetIterType));
        JCExpression iterator = make().NewClass(null, null,makeJavaType(iteratorType, JT_CLASS_NEW|JT_EXTENDS),
                List.<JCExpression>nil(), make().AnonymousClassDef(make().Modifiers(0), fields.toList()));
        //Define the anonymous iterable class
        JCExpression iterable = make().NewClass(null, null,
                make().TypeApply(makeIdent(syms().ceylonAbstractIterableType),
                    List.<JCExpression>of(makeJavaType(typeFact().getIteratedType(targetIterType), JT_NO_PRIMITIVES))),
                List.<JCExpression>nil(), make().AnonymousClassDef(make().Modifiers(0), List.<JCTree>of(
                    make().MethodDef(make().Modifiers(1), names().fromString("getIterator"),
                        makeJavaType(iteratorType, JT_CLASS_NEW|JT_EXTENDS),
                    List.<JCTree.JCTypeParameter>nil(), List.<JCTree.JCVariableDecl>nil(), List.<JCExpression>nil(),
                    make().Block(0, List.<JCStatement>of(make().Return(iterator))), null)
        )));
        return iterable;
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

    private JCExpression unAutoPromote(JCExpression ret, ProducedType returnType) {
        // +/- auto-promotes to int, so if we're using java types we'll need a cast
        return applyJavaTypeConversions(ret, typeFact().getIntegerDeclaration().getType(), 
                returnType, BoxingStrategy.UNBOXED);
    }

    private ProducedType getMostPreciseType(Term term, ProducedType defaultType) {
        // special case for interop when we're dealing with java types
        ProducedType termType = term.getTypeModel();
        if(termType.getUnderlyingType() != null)
            return termType;
        return defaultType;
    }

    //
    // Helper functions
    
    private boolean isRecursiveReference(Tree.StaticMemberOrTypeExpression expr) {
        Declaration decl = expr.getDeclaration();
        Scope s = expr.getScope();
        while (!(s instanceof Declaration) && (s.getContainer() != s)) {
            s = s.getContainer();
        }
        return (s instanceof Declaration) && (s == decl);
    }

    boolean isWithinInvocation() {
        return withinInvocation;
    }

    void setWithinInvocation(boolean withinInvocation) {
        this.withinInvocation = withinInvocation;
    }

    public boolean isWithinCallableInvocation() {
        return withinCallableInvocation;
    }

    public void setWithinCallableInvocation(boolean withinCallableInvocation) {
        this.withinCallableInvocation = withinCallableInvocation;
    }

    public boolean isWithinSuperInvocation() {
        return withinSuperInvocation;
    }

    public void setWithinSuperInvocation(boolean withinSuperInvocation) {
        this.withinSuperInvocation = withinSuperInvocation;
    }

}
