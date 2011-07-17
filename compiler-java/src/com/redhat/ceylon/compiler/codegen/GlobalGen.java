package com.redhat.ceylon.compiler.codegen;

import static com.sun.tools.javac.code.TypeTags.VOID;

import com.redhat.ceylon.compiler.util.Util;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

/**
 * Helper class to generate definitions of global variables. Top-level object and method definitions are generally
 * translated as a new class, handled elsewhere, and a global variable of the class, handled by this class. Top-level
 * attribute definitions are also translated as a global variable.
 *
 * All these global variables are represented by a class with name matching that of the variable exactly (no mangling is
 * done). The generated class contains a <tt>private static</tt> field named <tt>value</tt> and static accessors
 * <tt>get<em>VariableName</em>()</tt> and <tt>set<em>VariableName</em>()</tt>. It is possible to customize the
 * generated class.
 *
 * Methods are also provided to generate expressions to get and set the global value using the accessors.
 */
public final class GlobalGen extends GenPart {

    public GlobalGen(Gen2 gen) {
        super(gen);
    }

    /**
     * Returns a {@link DefinitionBuilder} instance through which the global class can be customized and then generated.
     * For details of possible customizations see the documentation of <tt>DefinitionBuilder</tt>. To finish the
     * generation, call {@link com.redhat.ceylon.compiler.codegen.GlobalGen.DefinitionBuilder#build()}.
     * @param variableType the type of the global
     * @param variableName the name of the global
     * @return a {@link DefinitionBuilder} to customize the class further before generating it.
     */
    public DefinitionBuilder defineGlobal(JCTree.JCExpression variableType, String variableName) {
        return new DefinitionBuilder(variableType, variableName);
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

    /**
     * Builds a class for global variables. See {@link GlobalGen} for an overview.
     *
     * The generated class can be customized by calling methods of this class.
     */
    public class DefinitionBuilder {
        private final Name fieldName = names().fromString("value");

        private final JCTree.JCExpression variableType;
        private final String variableName;

        private long classVisibility;

        private boolean readable = true;
        private long getterVisibility;

        private JCTree.JCExpression variableInit;

        private boolean writable = true;
        private long setterVisibility;
        private List<JCTree.JCAnnotation> valueAnnotations = List.nil();
        private List<JCTree.JCAnnotation> classAnnotations = List.nil();

        public DefinitionBuilder(JCTree.JCExpression variableType, String variableName) {
            this.variableType = variableType;
            this.variableName = variableName;
        }

        /**
         * Generates the class and returns the generated tree.
         * @return the generated class tree, to be added to the appropriate {@link JCTree.JCCompilationUnit}.
         */
        public JCTree build() {
            ListBuffer<JCTree> defs = ListBuffer.lb();
            appendDefinitionsTo(defs);
            return make().ClassDef(
                    make().Modifiers(Flags.FINAL | classVisibility, classAnnotations.prependList(gen.makeAtCeylon())),
                    getClassName(variableName),
                    List.<JCTree.JCTypeParameter>nil(),
                    null,
                    List.<JCTree.JCExpression>nil(),
                    defs.toList());
        }

        /**
         * Appends to <tt>defs</tt> the definitions that would go into the class generated by {@link #build()}
         * @param defs a {@link ListBuffer} to which the definitions will be appended.
         */
        public void appendDefinitionsTo(ListBuffer<JCTree> defs) {
            defs.append(generateField());

            if (readable) {
                defs.append(generateGetter());
            }

            if (writable) {
                defs.append(generateSetter());
            }
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
                    make().Modifiers(Flags.STATIC | getterVisibility, valueAnnotations),
                    getGetterName(variableName),
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
                    getSetterName(variableName),
                    make().TypeIdent(TypeTags.VOID),
                    List.<JCTree.JCTypeParameter>nil(),
                    List.<JCTree.JCVariableDecl>of(
                            make().VarDef(make().Modifiers(0, valueAnnotations), paramName, variableType, null)
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
         * Sets the visibility of the generated getter. If no getter is generated the visibility will be silently
         * ignored.
         * @param getterVisibility a visibility flag (see {@link Flags})
         * @return this instance for method chaining
         */
        public DefinitionBuilder getterVisibility(long getterVisibility) {
            this.getterVisibility = getterVisibility;
            return this;
        }

        /**
         * Sets the visibility of the generated setter. If no setter is generated the visibility will be silently
         * ignored.
         * @param setterVisibility a visibility flag (see {@link Flags})
         * @return this instance for method chaining
         */
        public DefinitionBuilder setterVisibility(long setterVisibility) {
            this.setterVisibility = setterVisibility;
            return this;
        }

        /**
         * Causes the generated global to be immutable. The <tt>value</tt> field is declared <tt>final</tt> and no
         * setter is generated.
         * @return this instance for method chaining
         */
        public DefinitionBuilder immutable() {
            this.writable = false;
            return this;
        }

        /**
         * The <tt>value</tt> field will be declared with the initial value given by the parameter
         * @param initialValue the initial value of the global.
         * @return this instance for method chaining
         */
        public DefinitionBuilder initialValue(JCTree.JCExpression initialValue) {
            this.variableInit = initialValue;
            return this;
        }

        /**
         * Applies the given <tt>valueAnnotations</tt> to the getter method and to the parameter of the setter method.
         * @param valueAnnotations the annotations to apply.
         * @return this instance for method chaining
         */
        public DefinitionBuilder valueAnnotations(List<JCTree.JCAnnotation> valueAnnotations) {
            this.valueAnnotations = valueAnnotations;
            return this;
        }

        public DefinitionBuilder classAnnotations(List<JCTree.JCAnnotation> classAnnotations) {
            this.classAnnotations = classAnnotations;
            return this;
        }
    }
}
