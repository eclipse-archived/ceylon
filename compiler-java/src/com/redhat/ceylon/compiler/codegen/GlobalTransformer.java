package com.redhat.ceylon.compiler.codegen;


import com.redhat.ceylon.compiler.util.Util;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

/**
 * Helper class to generate definitions of globals (singletons), as required by translations of top-level object and
 * attribute definitions.
 *
 * Global variables are represented by a class with name matching that of the variable exactly (no mangling is done).
 * The generated class contains a <tt>private static</tt> field named <tt>value</tt> and static accessors
 * <tt>get<em>VariableName</em>()</tt> and <tt>set<em>VariableName</em>()</tt>. It is possible to customize the
 * generated class.
 *
 * Methods are also provided to generate expressions to get and set the global value using the accessors.
 */
public final class GlobalTransformer extends AbstractTransformer {

    public GlobalTransformer(CeylonTransformer gen) {
        super(gen);
    }

    /**
     * Returns a {@link AttributeDefinitionBuilder} instance through which the global class can be customized and then generated.
     * For details of possible customizations see the documentation of <tt>DefinitionBuilder</tt>. To finish the
     * generation, call {@link com.redhat.ceylon.compiler.codegen.AttributeDefinitionBuilder#build()}.
     * @param variableType the type of the global
     * @param variableName the name of the global
     * @return a {@link AttributeDefinitionBuilder} to customize the class further before generating it.
     */
    public AttributeDefinitionBuilder defineGlobal(JCTree.JCExpression variableType, String variableName) {
        return new AttributeDefinitionBuilder(this, variableType, variableName);
    }

    /**
     * Generates and returns the code to get the value of the global variable.
     * @param packageName the package of the variable
     * @param variableName the name of the variable
     * @return the expression tree to get the variable value.
     */
    public JCTree.JCExpression getGlobalValue(JCTree.JCExpression packageName, String variableName) {
        // packageName.variableName.getValue()
        Name methodName = getGetterName(variableName);
        return make().Apply(
                List.<JCTree.JCExpression>nil(),
                getClassMethod(packageName, variableName, methodName),
                List.<JCTree.JCExpression>nil());
    }

    /**
     * Generates and returns the code to set the value of the global variable.
     * @param packageName the package of the variable
     * @param variableName the name of the variable
     * @param newValue the value to set the variable to.
     * @return the expression tree to set the variable value.
     */
    public JCTree.JCExpression setGlobalValue(JCTree.JCExpression packageName, String variableName, JCTree.JCExpression newValue) {
        // packageName.variableName.setValue(newValue)
        Name methodName = getSetterName(variableName);
        return make().Apply(
                List.<JCTree.JCExpression>nil(),
                getClassMethod(packageName, variableName, methodName),
                List.of(newValue));
    }

    private JCTree.JCFieldAccess getClassMethod(JCTree.JCExpression packageName, String variableName, Name methodName) {
        return make().Select(make().Select(packageName, getClassName(variableName)), methodName);
    }

    private Name getClassName(String variableName) {
        return gen.quoteName(variableName);
    }

    private Name getGetterName(String variableName) {
        return names().fromString(Util.getGetterName(variableName));
    }

    private Name getSetterName(String variableName) {
        return names().fromString(Util.getSetterName(variableName));
    }
}
