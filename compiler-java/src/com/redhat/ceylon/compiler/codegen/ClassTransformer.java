package com.redhat.ceylon.compiler.codegen;

import static com.sun.tools.javac.code.Flags.ABSTRACT;
import static com.sun.tools.javac.code.Flags.FINAL;
import static com.sun.tools.javac.code.Flags.INTERFACE;
import static com.sun.tools.javac.code.Flags.PRIVATE;
import static com.sun.tools.javac.code.Flags.PUBLIC;
import static com.sun.tools.javac.code.Flags.STATIC;

import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeGetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeSetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.VoidModifier;
import com.redhat.ceylon.compiler.util.Decl;
import com.redhat.ceylon.compiler.util.Util;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

/**
 * This transformer deals with class/interface declarations
 */
public class ClassTransformer extends AbstractTransformer {

    public static ClassTransformer getInstance(Context context) {
        ClassTransformer trans = context.get(ClassTransformer.class);
        if (trans == null) {
            trans = new ClassTransformer(context);
            context.put(ClassTransformer.class, trans);
        }
        return trans;
    }

    private ClassTransformer(Context context) {
        super(context);
    }

    // FIXME: figure out what insertOverloadedClassConstructors does and port it

    public List<JCTree> transform(final Tree.ClassOrInterface def) {
        String className = def.getIdentifier().getText();
        ClassDefinitionBuilder classBuilder = ClassDefinitionBuilder.klass(this, className);
        
        CeylonVisitor visitor = new CeylonVisitor(gen(), classBuilder);
        def.visitChildren(visitor);

        // Check if it's a Class without initializer parameters
        if (def instanceof Tree.AnyClass && Decl.isToplevel(def) && !Decl.isAbstract(def)) {
            com.redhat.ceylon.compiler.typechecker.model.Class c = (com.redhat.ceylon.compiler.typechecker.model.Class) def.getDeclarationModel();
            if (c.getParameterList().getParameters().isEmpty()) {
                // Add a main() method
                JCTree.JCIdent nameId = make().Ident(names().fromString(Util.quoteIfJavaKeyword(className)));
                JCNewClass expr = make().NewClass(null, null, nameId, List.<JCTree.JCExpression>nil(), null);
                classBuilder.body(makeMainMethod(expr));
            }
        }
        
        return classBuilder
            .modifiers(transformClassDeclFlags(def))
            .satisfies(def.getDeclarationModel().getSatisfiedTypes())
            .init((List<JCStatement>)visitor.getResult().toList())
            .build();
    }

    public void transform(AttributeDeclaration decl, ClassDefinitionBuilder classBuilder) {
        boolean useField = decl.getDeclarationModel().isCaptured() || Decl.isShared(decl);
        String attrName = decl.getIdentifier().getText();

        // Only a non-formal attribute has a corresponding field
        // and if a class parameter exists with the same name we skip this part as well
        if (!Decl.isFormal(decl) && !classBuilder.existsParam(attrName)) {
            JCExpression initialValue = null;
            if (decl.getSpecifierOrInitializerExpression() != null) {
                initialValue = expressionGen().transformExpression(decl.getSpecifierOrInitializerExpression().getExpression(), Util.getBoxingStrategy(decl.getDeclarationModel()));
            }

            int flags = 0;
            if(isGenericsImplementation(decl.getDeclarationModel()))
                flags |= TYPE_ARGUMENT;
            JCExpression type = makeJavaType(actualType(decl), flags);

            int modifiers = (useField) ? transformAttributeFieldDeclFlags(decl) : transformLocalDeclFlags(decl);
            classBuilder.field(modifiers, attrName, type, initialValue, !useField);
        }

        if (useField) {
            classBuilder.defs(makeGetter(decl));
            if (Decl.isMutable(decl)) {
                classBuilder.defs(makeSetter(decl));
            }
        }        
    }
    
    public List<JCTree> transform(AttributeSetterDefinition decl) {
        JCBlock body = statementGen().transform(decl.getBlock());
        String name = decl.getIdentifier().getText();
        return AttributeDefinitionBuilder
            .setter(this, name, decl.getDeclarationModel())
            .modifiers(transformAttributeGetSetDeclFlags(decl))
            .setterBlock(body)
            .build();
    }

    public List<JCTree> transform(AttributeGetterDefinition decl) {
        String name = decl.getIdentifier().getText();
        JCBlock body = statementGen().transform(decl.getBlock());
        return AttributeDefinitionBuilder
            .getter(this, name, decl.getDeclarationModel())
            .modifiers(transformAttributeGetSetDeclFlags(decl))
            .getterBlock(body)
            .build();
    }

    private int transformClassDeclFlags(Tree.ClassOrInterface cdecl) {
        int result = 0;

        result |= Decl.isShared(cdecl) ? PUBLIC : 0;
        result |= Decl.isAbstract(cdecl) && (cdecl instanceof Tree.AnyClass) ? ABSTRACT : 0;
        result |= (cdecl instanceof Tree.AnyInterface) ? INTERFACE : 0;

        return result;
    }

    private int transformMethodDeclFlags(Tree.AnyMethod def) {
        int result = 0;

        if (Decl.isToplevel(def)) {
            result |= Decl.isShared(def) ? PUBLIC : 0;
            result |= STATIC;
        } else if (Decl.isInner(def)) {
            result |= Decl.isShared(def) ? PUBLIC : 0;
        } else {
            result |= Decl.isShared(def) ? PUBLIC : PRIVATE;
            result |= (Decl.isFormal(def) && !Decl.isDefault(def)) ? ABSTRACT : 0;
            result |= !(Decl.isFormal(def) || Decl.isDefault(def)) ? FINAL : 0;
        }

        return result;
    }

    private int transformAttributeFieldDeclFlags(Tree.AttributeDeclaration cdecl) {
        int result = 0;

        result |= Decl.isMutable(cdecl) ? 0 : FINAL;
        result |= PRIVATE;

        return result;
    }

    private int transformLocalDeclFlags(Tree.AttributeDeclaration cdecl) {
        int result = 0;

        result |= Decl.isMutable(cdecl) ? 0 : FINAL;

        return result;
    }

    private int transformAttributeGetSetDeclFlags(Tree.TypedDeclaration cdecl) {
        int result = 0;

        result |= Decl.isShared(cdecl) ? PUBLIC : PRIVATE;
        result |= (Decl.isFormal(cdecl) && !Decl.isDefault(cdecl)) ? ABSTRACT : 0;
        result |= !(Decl.isFormal(cdecl) || Decl.isDefault(cdecl)) ? FINAL : 0;

        return result;
    }

    private int transformObjectDeclFlags(Tree.ObjectDefinition cdecl) {
        int result = 0;

        result |= FINAL;
        result |= Decl.isShared(cdecl) ? PUBLIC : 0;

        return result;
    }

    private List<JCTree> makeGetter(Tree.AttributeDeclaration decl) {
        at(decl);
        String atrrName = decl.getIdentifier().getText();
        JCBlock body = null;
        if (!Decl.isFormal(decl)) {
            body = make().Block(0, List.<JCTree.JCStatement>of(make().Return(makeSelect("this", atrrName))));
        }
        
        return AttributeDefinitionBuilder
            .getter(this, atrrName, decl.getDeclarationModel())
            .modifiers(transformAttributeGetSetDeclFlags(decl))
            .isActual(Decl.isActual(decl))
            .getterBlock(body)
            .build();
    }

    private List<JCTree> makeSetter(Tree.AttributeDeclaration decl) {
        at(decl);
        String atrrName = decl.getIdentifier().getText();
        JCBlock body = null;
        if (!Decl.isFormal(decl)) {
            body = make().Block(0, List.<JCTree.JCStatement>of(
                    make().Exec(
                            make().Assign(makeSelect("this", atrrName),
                                    makeIdent(atrrName.toString())))));
        }
        
        return AttributeDefinitionBuilder
            .setter(this, atrrName, decl.getDeclarationModel())
            .modifiers(transformAttributeGetSetDeclFlags(decl))
            .isActual(Decl.isActual(decl))
            .setterBlock(body)
            .build();
    }

    public List<JCTree> transformWrappedMethod(Tree.MethodDefinition def) {
        // Generate a wrapper class for the method
        String name = def.getIdentifier().getText();
        JCTree.JCIdent nameId = make().Ident(names().fromString(Util.quoteIfJavaKeyword(name)));
        ClassDefinitionBuilder builder = ClassDefinitionBuilder.methodWrapper(this, name, Decl.isShared(def));
        builder.body(classGen().transform(def));
        if (Decl.withinMethod(def)) {
            // Inner method
            List<JCTree> result = builder.build();
            JCVariableDecl call = at(def).VarDef(
                    make().Modifiers(FINAL),
                    names().fromString(name),
                    nameId,
                    makeNewClass(name));
            return result.append(call);
        } else {
            // Toplevel method
            if (!def.getParameterLists().isEmpty() && def.getParameterLists().get(0).getParameters().isEmpty()) {
                // Add a main() method
                builder.body(makeMainMethod(make().Apply(null, nameId, List.<JCTree.JCExpression>nil())));
            }
            return builder.build();                
        }
    }

    public JCMethodDecl transform(Tree.AnyMethod def) {
        String name = def.getIdentifier().getText();
        MethodDefinitionBuilder methodBuilder = MethodDefinitionBuilder.method(this, name);
        
        for (Tree.Parameter param : def.getParameterLists().get(0).getParameters()) {
            methodBuilder.parameter(param);
        }

        if (def.getTypeParameterList() != null) {
            for (Tree.TypeParameterDeclaration t : def.getTypeParameterList().getTypeParameterDeclarations()) {
                methodBuilder.typeParameter(t);
            }
        }

        if (!(def.getType() instanceof VoidModifier)) {
            methodBuilder.resultType(def.getDeclarationModel());
        }
        
        if (def instanceof Tree.MethodDefinition) {
            Scope container = def.getDeclarationModel().getContainer();
            boolean isInterface = container instanceof com.redhat.ceylon.compiler.typechecker.model.Interface;
            if(!isInterface){
                JCBlock body = statementGen().transform(((Tree.MethodDefinition)def).getBlock());
                methodBuilder.block(body);
            }else
                methodBuilder.noBody();
        }
        
        if(Util.hasCompilerAnnotation(def, "test")){
            methodBuilder.annotations(List.of(make().Annotation(makeIdent("org.junit.Test"), List.<JCTree.JCExpression>nil())));
        }
        
        return methodBuilder
            .modifiers(transformMethodDeclFlags(def))
            .isActual(Decl.isActual(def))
            .build();
    }

    public JCMethodDecl transformConcreteInterfaceMember(MethodDefinition def, ProducedType type) {
        String name = def.getIdentifier().getText();
        MethodDefinitionBuilder methodBuilder = MethodDefinitionBuilder.method(gen(), name);
        
        JCExpression typeExpr = makeJavaType(type);
        methodBuilder.parameter(FINAL, "$this", typeExpr, List.<JCTree.JCAnnotation>nil());
        for (Tree.Parameter param : def.getParameterLists().get(0).getParameters()) {
            methodBuilder.parameter(param);
        }

        if (def.getTypeParameterList() != null) {
            for (Tree.TypeParameterDeclaration t : def.getTypeParameterList().getTypeParameterDeclarations()) {
                methodBuilder.typeParameter(t);
            }
        }

        if (!(def.getType() instanceof VoidModifier)) {
            methodBuilder.resultType(def.getDeclarationModel());
        }
        
        // FIXME: this needs rewriting to map non-qualified refs to $this
        JCBlock body = statementGen().transform(def.getBlock());
        methodBuilder.block(body);
                
        return methodBuilder
            .modifiers(transformMethodDeclFlags(def) | STATIC)
            .isActual(Decl.isActual(def))
            .build();
    }

    public List<JCTree> transformObject(Tree.ObjectDefinition def, ClassDefinitionBuilder containingClassBuilder) {
        String name = def.getIdentifier().getText();
        ClassDefinitionBuilder objectClassBuilder = ClassDefinitionBuilder.klass(this, name);
        
        CeylonVisitor visitor = new CeylonVisitor(gen(), objectClassBuilder);
        def.visitChildren(visitor);

        TypeDeclaration decl = def.getDeclarationModel().getType().getDeclaration();

        if (Decl.isToplevel(def)) {
            objectClassBuilder.body(makeObjectGlobal(def, name).toList());
        }

        List<JCTree> result = objectClassBuilder
            .annotations(makeAtObject())
            .modifiers(transformObjectDeclFlags(def))
            .constructorModifiers(PRIVATE)
            .satisfies(decl.getSatisfiedTypes())
            .init((List<JCStatement>)visitor.getResult().toList())
            .build();
        
        if (Decl.withinMethod(def)) {
            result = result.append(makeLocalIdentityInstance(name, false));
        } else if (Decl.withinClassOrInterface(def)) {
            boolean visible = def.getDeclarationModel().isCaptured() || Decl.isShared(def);
            int modifiers = FINAL | ((visible) ? PRIVATE : 0);
            JCTree.JCIdent type = make().Ident(names().fromString(name));
            JCExpression initialValue = makeNewClass(name);
            containingClassBuilder.field(modifiers, name, type, initialValue, !visible);
            
            if (visible) {
                result = result.appendList(AttributeDefinitionBuilder
                    .getter(this, name, def.getDeclarationModel())
                    .modifiers(transformAttributeGetSetDeclFlags(def))
                    .isActual(Decl.isActual(def))
                    .build());
            }
        }
        
        return result;
    }

    private ListBuffer<JCTree> makeObjectGlobal(Tree.ObjectDefinition decl, String generatedClassName) {
        ListBuffer<JCTree> defs = ListBuffer.lb();
        AttributeDefinitionBuilder builder = AttributeDefinitionBuilder
                .wrapped(this, decl.getIdentifier().getText(), decl.getDeclarationModel())
                .immutable()
                .initialValue(makeNewClass(generatedClassName))
                .is(PUBLIC, Decl.isShared(decl))
                .is(STATIC, true);

        builder.appendDefinitionsTo(defs);
        return defs;
    }
    
    private JCExpression transform(final Tree.Annotation userAnn, Tree.ClassOrInterface classDecl, String methodName) {
        List<JCExpression> values = List.<JCExpression> nil();
        // FIXME: handle named arguments
        for (Tree.PositionalArgument arg : userAnn.getPositionalArgumentList().getPositionalArguments()) {
            values = values.append(expressionGen().transformExpression(arg.getExpression()));
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
            args = List.<JCExpression> of(classLiteral, expressionGen().ceylonLiteral(methodName), result);
        else
            args = List.<JCExpression> of(classLiteral, result);

        result = at(userAnn).Apply(null, addAnnotation, args);

        return result;
    }

    private JCMethodDecl makeMainMethod(JCExpression callee) {
        // Add a main() method
        MethodDefinitionBuilder methbuilder = MethodDefinitionBuilder.main(this);
        // Add call to process.setupArguments
        JCIdent argsId = make().Ident(names().fromString("args"));
        JCMethodInvocation processExpr = make().Apply(null, makeIdent("ceylon", "language", "process", "getProcess"), List.<JCTree.JCExpression>nil());
        methbuilder.body(make().Exec(make().Apply(null, makeSelect(processExpr, "setupArguments"), List.<JCTree.JCExpression>of(argsId))));
        // Add call to toplevel method
        methbuilder.body(make().Exec(callee));
        return methbuilder.build();
    }
}
