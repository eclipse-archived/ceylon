package com.redhat.ceylon.compiler.codegen;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

/**
 * Helper class to generate definitions of global variables. Top-level object and method definitions are generally
 * translated as a new class, handled elsewhere, and a global variable of the class, handled by this class. Top-level
 * attribute definitions are also translated as a global variable.
 *
 * All these global variables are represented by a class with name matching that of the variable exactly (no mangling is
 * done). The generated class contains a <tt>private static</tt> field named <tt>value</tt> and static methods
 * <tt>getValue()</tt> and <tt>setValue()</tt>. It is possible to customize the generated class in certain ways.
 *
 * Methods are also provided to generate expressions to get and set the global value using the <tt>get/setValue()</tt>
 * methods.
 */
public final class GlobalGen extends GenPart {

    private final Name fieldName;
    private final Name getterName;
    private final Name setterName;

    public GlobalGen(Gen2 gen) {
        super(gen);
        fieldName = names().fromString("value");
        getterName = names().fromString("getValue");
        setterName = names().fromString("setValue");
    }

    /**
     * Returns a {@link DefinitionBuilder} instance through which the global class can be customized and then generated.
     * For details of possible customizations see the documentation of <tt>DefinitionBuilder</tt>. To finish the
     * generation, call {@link com.redhat.ceylon.compiler.codegen.GlobalGen.DefinitionBuilder#build()}.
     * @param variableType the type of the global
     * @param variableName the name of the global
     * @return a {@link DefinitionBuilder} to customize the class further before generating it.
     */
    public DefinitionBuilder defineGlobal(JCTree.JCExpression variableType, Name variableName) {
        return new DefinitionBuilder(variableType, variableName);
    }

    /**
     * Generates and returns the code to get the value of the global variable.
     * @param packageName the package of the variable
     * @param variableName the name of the variable
     * @return the expression tree to get the variable value.
     */
    public JCTree.JCExpression getGlobalValue(JCTree.JCExpression packageName, Name variableName) {
        // packageName.variableName.getValue()
        return make().Apply(
                List.<JCTree.JCExpression>nil(),
                make().Select(make().Select(packageName, variableName), getterName),
                List.<JCTree.JCExpression>nil());
    }

    /**
     * Generates and returns the code to set the value of the global variable.
     * @param packageName the package of the variable
     * @param variableName the name of the variable
     * @param newValue the value to set the variable to.
     * @return the expression tree to set the variable value.
     */
    public JCTree.JCExpression setGlobalValue(JCTree.JCExpression packageName, Name variableName, JCTree.JCExpression newValue) {
        // packageName.variableName.setValue(newValue)
        return make().Apply(
                List.<JCTree.JCExpression>nil(),
                make().Select(make().Select(packageName, variableName), setterName),
                List.of(newValue));
    }

    /**
     * Builds a class for global variables. See {@link GlobalGen} for an overview.
     *
     * The generated class can be customized by calling methods of this class.
     */
    public class DefinitionBuilder {
        private final JCTree.JCExpression variableType;
        private final Name variableName;

        private long classVisibility;

        private boolean readable = true;
        private long getterVisibility;

        private JCTree.JCExpression variableInit;

        private boolean writable = true;
        private long setterVisibility;

        public DefinitionBuilder(JCTree.JCExpression variableType, Name variableName) {
            this.variableType = variableType;
            this.variableName = variableName;
        }

        /**
         * Generates the class and returns the generated tree.
         * @return the generated class tree, to be added to the appropriate {@link JCTree.JCCompilationUnit}.
         */
        public JCTree build() {
            List<JCTree> defs = List.nil();

            defs = defs.append(generateField());

            if (readable) {
                defs = defs.append(generateGetter());
            }

            if (writable) {
                defs = defs.append(generateSetter());
            }

            return make().ClassDef(
                    make().Modifiers(Flags.FINAL | classVisibility),
                    variableName,
                    List.<JCTree.JCTypeParameter>nil(),
                    null,
                    List.<JCTree.JCExpression>nil(),
                    defs);
        }

        private JCTree generateField() {
            int flags = Flags.PRIVATE | Flags.STATIC;
            if (!writable) {
                flags |= Flags.FINAL;
            }

            return make().VarDef(
                    make().Modifiers(flags),
                    fieldName,
                    variableType,
                    variableInit
            );
        }

        private JCTree generateGetter() {
            JCTree.JCBlock body = make().Block(0L, List.<JCTree.JCStatement>of(
                    make().Return(make().Ident(fieldName))
            ));
            return make().MethodDef(
                    make().Modifiers(Flags.STATIC | getterVisibility),
                    getterName,
                    variableType,
                    List.<JCTree.JCTypeParameter>nil(),
                    List.<JCTree.JCVariableDecl>nil(),
                    List.<JCTree.JCExpression>nil(),
                    body,
                    null
            );
        }

        private JCTree generateSetter() {
            Name paramName = names().fromString("newValue");

            JCTree.JCBlock body = make().Block(0L, List.<JCTree.JCStatement>of(
                    make().Exec(
                            make().Assign(
                                    make().Ident(fieldName),
                                    make().Ident(paramName)))
            ));
            return make().MethodDef(
                    make().Modifiers(Flags.STATIC | setterVisibility),
                    setterName,
                    makeIdent("void"),
                    List.<JCTree.JCTypeParameter>nil(),
                    List.<JCTree.JCVariableDecl>of(
                            make().VarDef(make().Modifiers(0), paramName, variableType, null)
                    ),
                    List.<JCTree.JCExpression>nil(),
                    body,
                    null
            );
        }

        /**
         * Sets the visibility of the generated class.
         * @param classVisibility a visibility flag (see {@link Flags})
         * @return this instance for method chaining
         */
        public DefinitionBuilder classVisibility(long classVisibility) {
            this.classVisibility = classVisibility;
            return this;
        }

        /**
         * Sets the visibility of the generated getter.
         * @param getterVisibility a visibility flag (see {@link Flags})
         * @return this instance for method chaining
         */
        public DefinitionBuilder getterVisibility(long getterVisibility) {
            this.getterVisibility = getterVisibility;
            return this;
        }

        /**
         * Sets the visibility of the generated setter.
         * @param setterVisibility a visibility flag (see {@link Flags})
         * @return this instance for method chaining
         */
        public DefinitionBuilder setterVisibility(long setterVisibility) {
            this.setterVisibility = setterVisibility;
            return this;
        }

        /**
         * Causes the generated global to be immutable. The <tt>value</tt> field is declared <tt>final</tt> with the
         * initial value given by the parameter, and no setter is generated.
         * @param initialValue the initial value of the global.
         * @return this instance for method chaining
         */
        public DefinitionBuilder immutableWithInitialValue(JCTree.JCExpression initialValue) {
            this.writable = false;
            this.variableInit = initialValue;
            return this;
        }
    }
}
