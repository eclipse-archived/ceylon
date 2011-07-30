package com.redhat.ceylon.compiler.codegen;

import static com.sun.tools.javac.code.Flags.ABSTRACT;
import static com.sun.tools.javac.code.Flags.FINAL;
import static com.sun.tools.javac.code.Flags.INTERFACE;
import static com.sun.tools.javac.code.Flags.PRIVATE;
import static com.sun.tools.javac.code.Flags.PUBLIC;
import static com.sun.tools.javac.code.Flags.STATIC;

import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeGetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeSetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.VoidModifier;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

/**
 * This transformer deals with class/interface declarations
 */
public class ClassTransformer extends AbstractTransformer {

    public ClassTransformer(CeylonTransformer gen) {
        super(gen);
    }

    // FIXME: figure out what insertOverloadedClassConstructors does and port it

    public JCClassDecl transform(final Tree.ClassOrInterface def) {
        String className = def.getIdentifier().getText();
        ClassDefinitionBuilder classBuilder = ClassDefinitionBuilder.klass(gen, className);
        
        CeylonVisitor visitor = new CeylonVisitor(gen, classBuilder);
        def.visitChildren(visitor);

        return classBuilder
            .modifiers(transformClassDeclFlags(def))
            .satisfies(def.getDeclarationModel().getSatisfiedTypes())
            .init((List<JCStatement>)visitor.getResult().toList())
            .build();
    }

    public void transform(AttributeDeclaration decl, ClassDefinitionBuilder classBuilder) {
        boolean useField = decl.getDeclarationModel().isCaptured() || isShared(decl);

        Name attrName = names().fromString(decl.getIdentifier().getText());

        // Only a non-formal attribute has a corresponding field
        // and if a class parameter exists with the same name we skip this part as well
        if (!isFormal(decl) && !classBuilder.existsParam(attrName.toString())) {
            JCExpression initialValue = null;
            if (decl.getSpecifierOrInitializerExpression() != null) {
                initialValue = gen.expressionGen.transformExpression(decl.getSpecifierOrInitializerExpression().getExpression());
            }

            JCExpression type = gen.makeJavaType(gen.actualType(decl), false);

            if (useField) {
                // A captured attribute gets turned into a field
                int modifiers = transformAttributeFieldDeclFlags(decl);
                classBuilder.defs(at(decl).VarDef(at(decl).Modifiers(modifiers, List.<JCTree.JCAnnotation>nil()), attrName, type, null));
                if (initialValue != null) {
                    // The attribute's initializer gets moved to the constructor
                    // because it might be using locals of the initializer
                    classBuilder.init(at(decl).Exec(at(decl).Assign(makeSelect("this", decl.getIdentifier().getText()), initialValue)));
                }
            } else {
                // Otherwise it's local to the constructor
                int modifiers = transformLocalDeclFlags(decl);
                classBuilder.init(at(decl).VarDef(at(decl).Modifiers(modifiers, List.<JCTree.JCAnnotation>nil()), attrName, type, initialValue));
            }
        }

        if (useField) {
            classBuilder.defs(makeGetter(decl));
            if (isMutable(decl)) {
                classBuilder.defs(makeSetter(decl));
            }
        }        
    }
    
    public JCTree.JCMethodDecl transform(AttributeSetterDefinition decl) {
        JCBlock body = gen.statementGen.transform(decl.getBlock());
        String name = decl.getIdentifier().getText();
        return MethodDefinitionBuilder
            .setter(gen, name, gen.actualType(decl))
            .modifiers(transformAttributeGetSetDeclFlags(decl))
            .block(body)
            .build();
    }

    public JCTree.JCMethodDecl transform(AttributeGetterDefinition decl) {
        String name = decl.getIdentifier().getText();
        JCBlock body = gen.statementGen.transform(decl.getBlock());
        return MethodDefinitionBuilder
            .getter(gen, name, gen.actualType(decl))
            .modifiers(transformAttributeGetSetDeclFlags(decl))
            .block(body)
            .build();
    }

    private int transformClassDeclFlags(Tree.ClassOrInterface cdecl) {
        int result = 0;

        result |= isShared(cdecl) ? PUBLIC : 0;
        result |= isAbstract(cdecl) && (cdecl instanceof Tree.AnyClass) ? ABSTRACT : 0;
        result |= (cdecl instanceof Tree.AnyInterface) ? INTERFACE : 0;

        return result;
    }

    private int transformMethodDeclFlags(Tree.AnyMethod def) {
        int result = 0;

        if (isToplevel(def)) {
            result |= isShared(def) ? PUBLIC : 0;
            result |= STATIC;
        } else if (isInner(def)) {
            result |= isShared(def) ? PUBLIC : 0;
        } else {
            result |= isShared(def) ? PUBLIC : PRIVATE;
            result |= (isFormal(def) && !isDefault(def)) ? ABSTRACT : 0;
            result |= !(isFormal(def) || isDefault(def)) ? FINAL : 0;
        }

        return result;
    }

    private int transformAttributeFieldDeclFlags(Tree.AttributeDeclaration cdecl) {
        int result = 0;

        result |= isMutable(cdecl) ? 0 : FINAL;
        result |= PRIVATE;

        return result;
    }

    private int transformLocalDeclFlags(Tree.AttributeDeclaration cdecl) {
        int result = 0;

        result |= isMutable(cdecl) ? 0 : FINAL;

        return result;
    }

    private int transformAttributeGetSetDeclFlags(Tree.TypedDeclaration cdecl) {
        int result = 0;

        result |= isShared(cdecl) ? PUBLIC : PRIVATE;
        result |= (isFormal(cdecl) && !isDefault(cdecl)) ? ABSTRACT : 0;
        result |= !(isFormal(cdecl) || isDefault(cdecl)) ? FINAL : 0;

        return result;
    }

    private int transformObjectDeclFlags(Tree.ObjectDefinition cdecl) {
        int result = 0;

        result |= FINAL;
        result |= isShared(cdecl) ? PUBLIC : 0;

        return result;
    }

    public JCTree makeGetter(Tree.AttributeDeclaration decl) {
        at(decl);
        String atrrName = decl.getIdentifier().getText();
        JCBlock body = null;
        if (!isFormal(decl)) {
            body = make().Block(0, List.<JCTree.JCStatement>of(make().Return(gen.makeSelect("this", atrrName))));
        }
        
        return MethodDefinitionBuilder
            .getter(gen, atrrName, gen.actualType(decl))
            .modifiers(transformAttributeGetSetDeclFlags(decl))
            .isActual(isActual(decl))
            .block(body)
            .build();
    }

    public JCTree makeSetter(Tree.AttributeDeclaration decl) {
        at(decl);
        String atrrName = decl.getIdentifier().getText();
        JCBlock body = null;
        if (!isFormal(decl)) {
            body = make().Block(0, List.<JCTree.JCStatement>of(
                    make().Exec(
                            make().Assign(gen.makeSelect("this", atrrName),
                                    makeIdent(atrrName.toString())))));
        }
        
        return MethodDefinitionBuilder
            .setter(gen, atrrName, gen.actualType(decl))
            .modifiers(transformAttributeGetSetDeclFlags(decl))
            .isActual(isActual(decl))
            .block(body)
            .build();
    }

    public JCMethodDecl transform(Tree.AnyMethod def) {
        String name = def.getIdentifier().getText();
        MethodDefinitionBuilder methodBuilder = MethodDefinitionBuilder.method(gen, name);
        
        for (Tree.Parameter param : def.getParameterLists().get(0).getParameters()) {
            methodBuilder.parameter(param);
        }

        if (def.getTypeParameterList() != null) {
            for (Tree.TypeParameterDeclaration t : def.getTypeParameterList().getTypeParameterDeclarations()) {
                methodBuilder.typeParameter(t);
            }
        }

        if (!(def.getType() instanceof VoidModifier)) {
            methodBuilder.resultType(gen.actualType(def));
        }
        
        if (def instanceof Tree.MethodDefinition) {
            JCBlock body = gen.statementGen.transform(((Tree.MethodDefinition)def).getBlock());
            methodBuilder.block(body);
        }
                
        return methodBuilder
            .modifiers(transformMethodDeclFlags(def))
            .isActual(isActual(def))
            .build();
    }

    public JCClassDecl methodClass(Tree.MethodDefinition def) {
        String name = generateClassName(def, isToplevel(def));
        JCMethodDecl meth = transform(def);
        return ClassDefinitionBuilder.klass(gen, name)
            .annotations(gen.makeAtMethod())
            .modifiers(FINAL, isShared(def) ? PUBLIC : 0)
            .constructorModifiers(PRIVATE)
            .body(meth)
            .build();
    }

    /**
     * Generates the class name for a method, object or attribute definition. If <tt>topLevel</tt> is <tt>true</tt>,
     * uses the declaration name, otherwise uses a generated name.
     *
     * @param decl the Ceylon declaration (actually a definition) to generate the class name for.
     * @param topLevel a boolean indicating whether the declaration is a top-level one.
     * @return the generated name.
     */
    private String generateClassName(Tree.Declaration decl, boolean topLevel) {
        String name;
        if (topLevel)
            name = decl.getIdentifier().getText();
        else
            name = aliasName(decl.getIdentifier().getText() + "$class");
        return name;
    }

    public JCClassDecl objectClass(Tree.ObjectDefinition def, boolean topLevel) {
        String name = generateClassName(def, topLevel);
        ClassDefinitionBuilder classBuilder = ClassDefinitionBuilder.klass(gen, name);
        
        CeylonVisitor visitor = new CeylonVisitor(gen, classBuilder);
        def.visitChildren(visitor);

        TypeDeclaration decl = def.getDeclarationModel().getType().getDeclaration();

        if (topLevel) {
            classBuilder.body(makeObjectGlobal(def, make().Ident(names().fromString(name))).toList());
        }

        return classBuilder
            .annotations(gen.makeAtObject())
            .modifiers(transformObjectDeclFlags(def))
            .constructorModifiers(PRIVATE)
            .satisfies(decl.getSatisfiedTypes())
            .init((List<JCStatement>)visitor.getResult().toList())
            .build();
    }

    public ListBuffer<JCTree> makeObjectGlobal(Tree.ObjectDefinition decl, JCExpression generatedClassName) {
        ListBuffer<JCTree> defs = ListBuffer.lb();
        GlobalTransformer.DefinitionBuilder builder = gen
                .globalGenAt(decl)
                .defineGlobal(generatedClassName, decl.getIdentifier().getText())
                .valueAnnotations(gen.makeJavaTypeAnnotations(decl.getDeclarationModel(), gen.actualType(decl)))
                .immutable()
                .skipConstructor()
                .initialValue(make().NewClass(null, List.<JCExpression>nil(), generatedClassName, List.<JCExpression>nil(), null));

        if (isShared(decl)) {
            builder
                    .classVisibility(PUBLIC)
                    .getterVisibility(PUBLIC)
                    .setterVisibility(PUBLIC);
        }

        builder.appendDefinitionsTo(defs);
        return defs;
    }
    
    // this is due to the above commented code
    @SuppressWarnings("unused")
    private JCExpression transform(final Tree.Annotation userAnn, Tree.ClassOrInterface classDecl, String methodName) {
        List<JCExpression> values = List.<JCExpression> nil();
        // FIXME: handle named arguments
        for (Tree.PositionalArgument arg : userAnn.getPositionalArgumentList().getPositionalArguments()) {
            values = values.append(gen.expressionGen.transformExpression(arg.getExpression()));
        }

        JCExpression classLiteral;
        if (classDecl != null) {
            classLiteral = makeSelect(classDecl.getIdentifier().getText(), "class");
        } else {
            classLiteral = makeSelect(methodName, "class");
        }

        // FIXME: can we have something else?
        Tree.BaseMemberExpression primary = (Tree.BaseMemberExpression) userAnn.getPrimary();
        String name = primary.getIdentifier().getText();
        JCExpression result = at(userAnn).Apply(null, makeSelect(name , name), values);
        JCIdent addAnnotation = at(userAnn).Ident(names().fromString("addAnnotation"));
        List<JCExpression> args;
        if (methodName != null)
            args = List.<JCExpression> of(classLiteral, gen.expressionGen.ceylonLiteral(methodName), result);
        else
            args = List.<JCExpression> of(classLiteral, result);

        result = at(userAnn).Apply(null, addAnnotation, args);

        return result;
    }

    private boolean isShared(Tree.Declaration decl) {
        return decl.getDeclarationModel().isShared();
    }

    private boolean isAbstract(Tree.ClassOrInterface decl) {
        return decl.getDeclarationModel().isAbstract();
    }

    private boolean isDefault(Tree.Declaration decl) {
        return decl.getDeclarationModel().isDefault();
    }

    private boolean isFormal(Tree.Declaration decl) {
        return decl.getDeclarationModel().isFormal();
    }

    private boolean isActual(Tree.Declaration decl) {
        return decl.getDeclarationModel().isActual();
    }

    private boolean isMutable(Tree.AttributeDeclaration decl) {
        return decl.getDeclarationModel().isVariable();
    }

    private boolean isToplevel(Tree.Declaration decl) {
        return decl.getDeclarationModel().isToplevel();
    }

    private boolean isInner(Tree.Declaration decl) {
        return gen.isInner(decl.getDeclarationModel());
    }
}
