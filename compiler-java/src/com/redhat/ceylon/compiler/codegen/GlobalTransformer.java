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


import com.redhat.ceylon.compiler.util.Util;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.util.Context;
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

    public static GlobalTransformer getInstance(Context context) {
        GlobalTransformer trans = context.get(GlobalTransformer.class);
        if (trans == null) {
            trans = new GlobalTransformer(context);
            context.put(GlobalTransformer.class, trans);
        }
        return trans;
    }

    private GlobalTransformer(Context context) {
        super(context);
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
     * Generates and returns the code to get the value of the global variable.
     * @param packageName the package of the variable
     * @param className the name of the variable
     * @param variableName the name of the variable
     * @return the expression tree to get the variable value.
     */
    public JCTree.JCExpression getGlobalValue(JCTree.JCExpression packageName, String className, String variableName) {
        // packageName.variableName.getValue()
        Name methodName = getGetterName(variableName);
        return make().Apply(
                List.<JCTree.JCExpression>nil(),
                getClassMethod(packageName, className, methodName),
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

    /**
     * Generates and returns the code to set the value of the global variable.
     * @param packageName the package of the variable
     * @param className the name of the variable
     * @param variableName the name of the variable
     * @param newValue the value to set the variable to.
     * @return the expression tree to set the variable value.
     */
    public JCTree.JCExpression setGlobalValue(JCTree.JCExpression packageName, String className, String variableName, JCTree.JCExpression newValue) {
        // packageName.variableName.setValue(newValue)
        Name methodName = getSetterName(variableName);
        return make().Apply(
                List.<JCTree.JCExpression>nil(),
                getClassMethod(packageName, className, methodName),
                List.of(newValue));
    }

    private JCTree.JCFieldAccess getClassMethod(JCTree.JCExpression packageName, String variableName, Name methodName) {
        return make().Select(make().Select(packageName, getClassName(variableName)), methodName);
    }

    private Name getClassName(String variableName) {
        return names().fromString(Util.quoteIfJavaKeyword(variableName));
    }

    private Name getGetterName(String variableName) {
        return names().fromString(Util.getGetterName(variableName));
    }

    private Name getSetterName(String variableName) {
        return names().fromString(Util.getSetterName(variableName));
    }
}
