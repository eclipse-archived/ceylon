package com.redhat.ceylon.compiler.codegen;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

public final class GlobalGen extends GenPart {

    public GlobalGen(Gen2 gen) {
        super(gen);
    }

    public DefinitionBuilder defineGlobal(JCTree.JCExpression type, Name name) {
        return new DefinitionBuilder(type, name);
    }

    public class DefinitionBuilder {
        private final Name fieldName = names().fromString("value");
        private final Name getterName = names().fromString("getValue");
        private final Name setterName = names().fromString("setValue");

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

        public DefinitionBuilder classVisibility(long classVisibility) {
            this.classVisibility = classVisibility;
            return this;
        }

        public DefinitionBuilder getterVisibility(long getterVisibility) {
            this.getterVisibility = getterVisibility;
            return this;
        }

        public DefinitionBuilder setterVisibility(long setterVisibility) {
            this.setterVisibility = setterVisibility;
            return this;
        }

        public DefinitionBuilder immutableWithInitialValue(JCTree.JCExpression initialValue) {
            this.writable = false;
            this.variableInit = initialValue;
            return this;
        }
    }
}
