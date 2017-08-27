package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCExpression;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCFieldAccess;
import com.redhat.ceylon.langtools.tools.javac.util.List;
import com.redhat.ceylon.model.typechecker.model.Type;

/**
 * Every use of {@code com.redhat.ceylon.compiler.java.Util} 
 * goes through this, so it's easier to know where and how 
 * {@code Util} methods are used
 */
class RuntimeUtil {

    private final AbstractTransformer abstractTransformer;

    /**
     * @param abstractTransformer
     */
    RuntimeUtil(AbstractTransformer abstractTransformer) {
        this.abstractTransformer = abstractTransformer;
    }

    private JCFieldAccess makeUtilSelection(String methodName) {
        return this.abstractTransformer.make().Select(this.abstractTransformer.make().QualIdent(this.abstractTransformer.syms().ceylonUtilType.tsym), 
                      this.abstractTransformer.names().fromString(methodName));
    }
    
    /**
     * Invokes a static method of the Util helper class
     * @param typeArguments The arguments to the method
     * @param methodName name of the method
     * @param arguments The arguments to the invocation
     * @return the invocation AST
     */
    private JCExpression makeUtilInvocation(List<JCExpression> typeArguments, String methodName, List<JCExpression> arguments) {
        return this.abstractTransformer.make().Apply(typeArguments, 
                            makeUtilSelection(methodName), 
                            arguments);
    }

    public JCExpression toByteArray(JCExpression expr, List<JCExpression> initialElements) {
        return makeUtilInvocation(null, "toByteArray", initialElements.prepend(expr));
    }

    public JCExpression toShortArray(JCExpression expr, List<JCExpression> initialElements) {
        return makeUtilInvocation(null, "toShortArray", initialElements.prepend(expr));
    }

    public JCExpression toIntArray(JCExpression expr, List<JCExpression> initialElements) {
        return makeUtilInvocation(null, "toIntArray", initialElements.prepend(expr));
    }

    public JCExpression toLongArray(JCExpression expr, List<JCExpression> initialElements) {
        return makeUtilInvocation(null, "toLongArray", initialElements.prepend(expr));
    }

    public JCExpression toFloatArray(JCExpression expr, List<JCExpression> initialElements) {
        return makeUtilInvocation(null, "toFloatArray", initialElements.prepend(expr));
    }

    public JCExpression toDoubleArray(JCExpression expr, List<JCExpression> initialElements) {
        return makeUtilInvocation(null, "toDoubleArray", initialElements.prepend(expr));
    }

    public JCExpression toCharArray(JCExpression expr, List<JCExpression> initialElements) {
        return makeUtilInvocation(null, "toCharArray", initialElements.prepend(expr));
    }

    public JCExpression toBooleanArray(JCExpression expr, List<JCExpression> initialElements) {
        return makeUtilInvocation(null, "toBooleanArray", initialElements.prepend(expr));
    }

    /**
     * Make a call to 
     * {@code Util.toJavaStringArray(Iterable<String> tail, java.lang.String... initialElements) }
     */
    public JCExpression toJavaStringArray(JCExpression expr, List<JCExpression> initialElements) {
        return makeUtilInvocation(null, "toJavaStringArray", initialElements.prepend(expr));
    }

    public JCExpression toArray(JCExpression expr, JCExpression klassLiteral, List<JCExpression> initialElements) {
        return makeUtilInvocation(null, "toArray", initialElements.prependList(List.of(expr, klassLiteral)));
    }
    
    /**
     * Make a call to 
     * {@code Util.toArray(List<T> tail, T[] array, T... initialElements) }
     */
    public JCExpression toArray(JCExpression seq, JCExpression arrayExpr, List<JCExpression> initialElements, JCExpression typeArgs) {
        return makeUtilInvocation(List.of(typeArgs), 
                "toArray",
                initialElements.prependList(List.of(seq, arrayExpr)));
    }
    
    public JCExpression sequentialOf(JCExpression reifiedType, JCExpression iterable){
        return makeUtilInvocation(null, "sequentialOf", List.of(reifiedType, iterable));
    }

    /**
     * Casts a <tt>ceylon.language.Sequential</tt> type to a <tt>ceylon.language.Sequence</tt> type.
     */
    public JCExpression castSequentialToSequence(JCExpression sequentialExpr, Type iteratedType) {
        return makeUtilInvocation(null, "asSequence", List.of(sequentialExpr));
    }
    
    public JCExpression isIdentifiable(JCExpression varExpr) {
        return makeUtilInvocation(null, "isIdentifiable", List.of(varExpr));
    }
    
    public JCExpression isBasic(JCExpression varExpr) {
        return makeUtilInvocation(null, "isBasic", List.of(varExpr));
    }
    
    public JCExpression isReified(JCExpression varExpr,
            Type testedType) {
        return makeUtilInvocation(null, "isReified", List.of(varExpr, this.abstractTransformer.makeReifiedTypeArgument(testedType)));
    }

    public JCExpression checkNull(JCExpression expr) {
        return makeUtilInvocation(null, "checkNull", List.of(expr));
    }

    public JCExpression storeArgs(JCExpression expr) {
        return makeUtilInvocation(null, "storeArgs", List.of(expr));
    }

    /**
     * Makes a call to 
     * {@code Util.sequentialCopy($reifiedT, Object[] initial, Sequential rest)} 
     */
    public JCExpression sequentialInstance(JCExpression typeArgument,
            JCExpression reifiedTypeArgument, 
            JCExpression /*Sequential*/ rest, 
            List<JCExpression> /*T...*/elements) {
        // Explicitly box the elements into an Object[] to avoid ambiguous varargs invocation
        
        /*
         * sequentialCopy($reifiedT, 0, 
                elements.length, elements, rest)
         */
        JCExpression array = abstractTransformer.make().NewArray(
                abstractTransformer.make().Type(abstractTransformer.syms().objectType), 
                List.<JCExpression>nil(), 
                elements);
        return makeUtilInvocation(typeArgument != null ? List.of(typeArgument) : null, "sequentialCopy", 
                List.<JCExpression>of(reifiedTypeArgument, 
                        array,
                        rest));
    }

    public JCExpression throwableMessage(JCExpression qualExpr) {
        return makeUtilInvocation(null, "throwableMessage", List.of(qualExpr));
    }

    public JCExpression suppressedExceptions(JCExpression qualExpr) {
        return makeUtilInvocation(null, "suppressedExceptions", List.of(qualExpr));
    }

    public JCExpression tuple_spanFrom(List<JCExpression> arguments) {
        return makeUtilInvocation(null, "tuple_spanFrom", arguments);
    }

    /**
     * <p>Invoke {@link com.redhat.ceylon.compiler.java.Util#sequentialWrapperBoxed(int[])} ,
     * {@link com.redhat.ceylon.compiler.java.Util#sequentialWrapperBoxed(java.lang.String[])} etc
     * with the given array.</p>
     * 
     * <p>Note that this method wraps int[] into Sequential&lt;Character&gt; and so is only valid
     * for code points and not interop if you want Sequential&lt;Integer&gt;. Use #sequentialWrapperBoxedForInteger
     * if you want that. This is due to backwards-compatibility. </p>
     *  
     * <p>Note that subsequent changes to the array will not be visible in the resulting Sequential due to boxing<p>
     */
    public JCExpression sequentialWrapperBoxed(JCExpression arrayOfUnboxed) {
        return makeUtilInvocation(null, "sequentialWrapperBoxed", List.of(arrayOfUnboxed));
    }

    /**
     * <p>Invoke {@link com.redhat.ceylon.compiler.java.Util#sequentialWrapperBoxedForInteger(int[])}
     * with the given array.</p>
     * 
     * <p>Note that this method wraps int[] into Sequential&lt;Integer&gt; and so is only valid
     * for interop if you want Sequential&lt;Integer&gt;. Use #sequentialWrapperBoxed
     * if you don't want that. This is due to backwards-compatibility. </p>
     *  
     * <p>Note that subsequent changes to the array will be not visible in the resulting Sequential due to boxing<p>
     */
    public JCExpression sequentialWrapperBoxedForInteger(JCExpression arrayOfUnboxed) {
        return makeUtilInvocation(null, "sequentialWrapperBoxedForInteger", List.of(arrayOfUnboxed));
    }

    public JCExpression makeArray(List<JCExpression> dimensions) {
        return makeUtilInvocation(null, "makeArray", dimensions);
    }

    public JCExpression fillArray(List<JCExpression> arguments) {
        return makeUtilInvocation(null, "fillArray", arguments);
    }

    public JCExpression rethrow(JCExpression exception) {
        return makeUtilInvocation(null, "rethrow", List.of(exception));
    }

    public JCExpression throwableMessage() {
        return makeUtilSelection("throwableMessage");
    }

    public JCExpression suppressedExceptions() {
        return makeUtilSelection("suppressedExceptions");
    }

    public JCExpression getBooleanArray(JCExpression indexable, JCExpression index) {
        return makeUtilInvocation(null, "getBooleanArray", List.of(indexable, index));
    }

    public JCExpression getFloatArray(JCExpression indexable, JCExpression index) {
        return makeUtilInvocation(null, "getFloatArray", List.of(indexable, index));
    }

    public JCExpression getIntegerArray(JCExpression indexable, JCExpression index) {
        return makeUtilInvocation(null, "getIntegerArray", List.of(indexable, index));
    }

    public JCExpression getCharacterArray(JCExpression indexable, JCExpression index) {
        return makeUtilInvocation(null, "getCharacterArray", List.of(indexable, index));
    }

    public JCExpression getByteArray(JCExpression indexable, JCExpression index) {
        return makeUtilInvocation(null, "getByteArray", List.of(indexable, index));
    }
    
    public JCExpression getStringArray(JCExpression indexable, JCExpression index) {
        return makeUtilInvocation(null, "getStringArray", List.of(indexable, index));
    }

    public JCExpression arrayLength(JCExpression array) {
        return makeUtilInvocation(null, "arrayLength", List.of(array));
    }

    public JCExpression toByte(JCExpression expr) {
        return makeUtilInvocation(null, "toByte", List.of(expr));
    }
    
    public JCExpression toShort(JCExpression expr) {
        return makeUtilInvocation(null, "toShort", List.of(expr));
    }
    
    public JCExpression toInt(JCExpression expr) {
        return makeUtilInvocation(null, "toInt", List.of(expr));
    }
    
    public JCExpression setter(JCExpression lookup, JCExpression fieldName) {
        return makeUtilInvocation(null, "setter", List.of(lookup, fieldName));
    }

    public JCExpression unpack(JCExpression expr) {
        return makeUtilInvocation(null, "unpack", List.of(expr));
    }

    public JCExpression recover() {
        return makeUtilInvocation(null, "recover", List.<JCExpression>nil());
    }

    public JCExpression sequentialWrapper(JCExpression typeArg, JCExpression reifiedTypeArg, JCExpression array) {
        return makeUtilInvocation(List.of(typeArg), "sequentialWrapper", List.of(reifiedTypeArg, array));
    }

    public JCExpression sequentialWrapperCopy(JCExpression typeArg, JCExpression reifiedTypeArg, JCExpression array) {
        return makeUtilInvocation(List.of(typeArg), "sequentialWrapperCopy", List.of(reifiedTypeArg, array));
    }

    public JCExpression sequentialToTuple(JCExpression typeArg, JCExpression reifiedTypeArg, JCExpression sequential) {
        return makeUtilInvocation(List.of(typeArg), "sequentialToTuple", List.of(reifiedTypeArg, sequential));
    }
    
    public JCExpression toIterable(JCExpression typeArg, JCExpression reifiedElement, JCExpression javaIterable) {
        return makeUtilInvocation(List.of(typeArg), "toIterable", List.of(reifiedElement, javaIterable));
    }
    
    public JCExpression toIterable(JCExpression javaIterable) {
        return makeUtilInvocation(null, "toIterable", List.of(javaIterable));
    }

    public JCExpression javaClassForModel(JCExpression metamodelModel) {
        return makeUtilInvocation(null, "javaClassForModel", List.of(metamodelModel));
    }

    public JCExpression classErasure(JCExpression javaClass) {
        return makeUtilInvocation(null, "classErasure", List.of(javaClass));
    }

    public JCExpression assertIsFailed(boolean negated, JCExpression $reified$Type, JCExpression operand) {
        return makeUtilInvocation(null, "assertIsFailed", List.of(
                abstractTransformer.make().Literal(negated),
                $reified$Type,
                operand));
    }
    
    public JCExpression assertBinOpFailed(JCExpression lhs, JCExpression rhs) {
        return makeUtilInvocation(null, "assertBinOpFailed", List.of(
                lhs, rhs));
    }
    
    public JCExpression assertWithinOpFailed(JCExpression lhs, JCExpression middle, JCExpression rhs) {
        return makeUtilInvocation(null, "assertWithinOpFailed", List.of(
                lhs, middle, rhs));
    }
}