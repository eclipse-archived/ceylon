package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.util.List;

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

    public JCExpression toJavaStringArray(JCExpression expr, List<JCExpression> initialElements) {
        return makeUtilInvocation(null, "toJavaStringArray", initialElements.prepend(expr));
    }

    public JCExpression toArray(JCExpression expr, JCExpression klassLiteral, List<JCExpression> initialElements) {
        return makeUtilInvocation(null, "toArray", initialElements.prependList(List.of(expr, klassLiteral)));
    }
    
    public JCExpression toArray(JCExpression seq, JCExpression arrayExpr, List<JCExpression> initialElements, JCExpression typeArgs) {
        return makeUtilInvocation(List.of(typeArgs), 
                "toArray",
                initialElements.prependList(List.of(seq, arrayExpr)));
    }
    
    public JCExpression sequentialInstance(JCExpression iterable){
        return makeUtilInvocation(null, "sequentialInstance", List.of(iterable));
    }
    
    public JCExpression sequentialInstance(JCExpression typeArgument, JCExpression reifiedTypeArgument, JCExpression annoAttr){
        return makeUtilInvocation(List.of(typeArgument), "sequentialInstance", List.<JCExpression>of(reifiedTypeArgument, annoAttr));
    }

    /**
     * Casts a <tt>ceylon.language.Sequential</tt> type to a <tt>ceylon.language.Sequence</tt> type.
     */
    public JCExpression castSequentialToSequence(JCExpression sequentialExpr, ProducedType iteratedType) {
        return makeUtilInvocation(null, "asSequence", List.of(sequentialExpr));
    }
    
    public JCExpression isIdentifiable(JCExpression varExpr) {
        return makeUtilInvocation(null, "isIdentifiable", List.of(varExpr));
    }
    
    public JCExpression isBasic(JCExpression varExpr) {
        return makeUtilInvocation(null, "isBasic", List.of(varExpr));
    }
    
    public JCExpression isReified(JCExpression varExpr,
            ProducedType testedType) {
        return makeUtilInvocation(null, "isReified", List.of(varExpr, this.abstractTransformer.makeReifiedTypeArgument(testedType)));
    }

    public JCExpression checkNull(JCExpression expr) {
        return makeUtilInvocation(null, "checkNull", List.of(expr));
    }

    public JCExpression spreadOp(List<JCExpression> arguments,
            List<JCExpression> typeArguments) {
        return makeUtilInvocation(typeArguments, "spreadOp", arguments);
    }

    public JCExpression sequentialInstance(List<JCExpression> arguments,
            List<JCExpression> typeArguments) {
        return makeUtilInvocation(typeArguments, "sequentialInstance", arguments);
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

    public JCExpression sequentialInstanceBoxed(JCExpression argument) {
        return makeUtilInvocation(null, "sequentialInstanceBoxed", List.of(argument));
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

    public JCExpression arrayLength(JCExpression array) {
        return makeUtilInvocation(null, "arrayLength", List.of(array));
    }
}