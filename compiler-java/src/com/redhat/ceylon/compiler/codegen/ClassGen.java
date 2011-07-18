package com.redhat.ceylon.compiler.codegen;

import static com.sun.tools.javac.code.Flags.ABSTRACT;
import static com.sun.tools.javac.code.Flags.FINAL;
import static com.sun.tools.javac.code.Flags.INTERFACE;
import static com.sun.tools.javac.code.Flags.PRIVATE;
import static com.sun.tools.javac.code.Flags.PUBLIC;
import static com.sun.tools.javac.code.Flags.STATIC;
import static com.sun.tools.javac.code.TypeTags.VOID;

import java.util.HashMap;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeGetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeSetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.VoidModifier;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.util.Util;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

public class ClassGen extends GenPart {

    class ClassVisitor extends StatementVisitor {
        final ListBuffer<JCVariableDecl> params = new ListBuffer<JCVariableDecl>();
        final ListBuffer<JCTree> defs = new ListBuffer<JCTree>();
        final ListBuffer<JCAnnotation> langAnnotations = new ListBuffer<JCAnnotation>();
        final ListBuffer<JCStatement> initStmts = new ListBuffer<JCStatement>();
        final ListBuffer<JCTypeParameter> typeParams = new ListBuffer<JCTypeParameter>();
        final ListBuffer<Tree.AttributeDeclaration> attributeDecls = new ListBuffer<Tree.AttributeDeclaration>();
        final Map<String, Tree.AttributeGetterDefinition> getters = new HashMap<String, Tree.AttributeGetterDefinition>();
        final Map<String, Tree.AttributeSetterDefinition> setters = new HashMap<String, Tree.AttributeSetterDefinition>();

        ClassVisitor() {
            super(gen.statementGen);
        }

        // Class Initializer parameter
        public void visit(Tree.Parameter param) {
            // Create a parameter for the constructor
            String name = param.getIdentifier().getText();
            JCExpression type = gen.makeJavaType(param.getType().getTypeModel(), false);
            List<JCAnnotation> annots = gen.makeJavaTypeAnnotations(param.getDeclarationModel(), param.getType().getTypeModel());
            JCVariableDecl var = at(param).VarDef(make().Modifiers(0, annots), names().fromString(name), type, null);
            params.append(var);
            
            // Check if the parameter is used outside of the initializer
            if (param.getDeclarationModel().isCaptured()) {
                // If so we create a field for it initializing it with the parameter's value
                JCVariableDecl localVar = at(param).VarDef(make().Modifiers(FINAL | PRIVATE), names().fromString(name), type , null);
                defs.append(localVar);
                initStmts.append(at(param).Exec(at(param).Assign(makeSelect("this", localVar.getName().toString()), at(param).Ident(var.getName()))));
            }
        }

        public void visit(Tree.Block b) {
            b.visitChildren(this);
        }

        public void visit(Tree.MethodDefinition meth) {
            defs.appendList(convert(meth));
        }

        public void visit(Tree.MethodDeclaration meth) {
            defs.appendList(convert(meth));
        }

        public void visit(Tree.Annotation ann) {
            // Handled in processAnnotations
        }

        // FIXME: Here we've simplified CeylonTree.MemberDeclaration to
        // Tree.AttributeDeclaration
        public void visit(Tree.AttributeDeclaration decl) {
            boolean useField = decl.getDeclarationModel().isCaptured() || isShared(decl);

            Name attrName = names().fromString(decl.getIdentifier().getText());

            // Only a non-formal attribute has a corresponding field
            // and if a class parameter exists with the same name we skip this part as well
            if (!isFormal(decl) && !existsParam(params, attrName)) {
                JCExpression initialValue = null;
                if (decl.getSpecifierOrInitializerExpression() != null) {
                    initialValue = gen.expressionGen.convertExpression(decl.getSpecifierOrInitializerExpression().getExpression());
                }

                JCExpression type = gen.makeJavaType(gen.actualType(decl), false);

                if (useField) {
                    // A captured attribute gets turned into a field
                    int modifiers = convertAttributeFieldDeclFlags(decl);
                    defs.append(at(decl).VarDef(at(decl).Modifiers(modifiers, List.<JCTree.JCAnnotation>nil()), attrName, type, null));
                    if (initialValue != null) {
                        // The attribute's initializer gets moved to the constructor
                        // because it might be using locals of the initializer
                        stmts.append(at(decl).Exec(at(decl).Assign(makeSelect("this", decl.getIdentifier().getText()), initialValue)));
                    }
                } else {
                    // Otherwise it's local to the constructor
                    int modifiers = convertLocalDeclFlags(decl);
                    stmts.append(at(decl).VarDef(at(decl).Modifiers(modifiers, List.<JCTree.JCAnnotation>nil()), attrName, type, initialValue));
                }
            }

            if (useField) {
                // Remember attribute to be able to generate
                // missing getters and setters later on
                attributeDecls.append(decl);
            }
        }

        public void visit(final Tree.AttributeGetterDefinition getter) {
            JCTree.JCMethodDecl getterDef = convert(getter);
            defs.append(getterDef);
            getters.put(getter.getIdentifier().getText(), getter);
        }

        public void visit(final Tree.AttributeSetterDefinition setter) {
            JCTree.JCMethodDecl setterDef = convert(setter);
            defs.append(setterDef);
            setters.put(setter.getIdentifier().getText(), setter);
        }

        public void visit(final Tree.ClassDefinition cdecl) {
            defs.append(convert(cdecl));
        }

        public void visit(final Tree.InterfaceDefinition cdecl) {
            defs.append(convert(cdecl));
        }

        // FIXME: also support Tree.SequencedTypeParameter
        public void visit(Tree.TypeParameterDeclaration param) {
            typeParams.append(convert(param));
        }

        public void visit(Tree.ExtendedType extendedType) {
            if (extendedType.getInvocationExpression().getPositionalArgumentList() != null) {
                List<JCExpression> args = List.<JCExpression> nil();

                for (Tree.PositionalArgument arg : extendedType.getInvocationExpression().getPositionalArgumentList().getPositionalArguments())
                    args = args.append(gen.expressionGen.convertArg(arg));

                stmts.append(at(extendedType).Exec(at(extendedType).Apply(List.<JCExpression> nil(), at(extendedType).Ident(names()._super), args)));
            }
        }

        // FIXME: implement
        public void visit(Tree.TypeConstraint l) {
        }
    }

    public ClassGen(Gen2 gen) {
        super(gen);
    }

    // FIXME: figure out what insertOverloadedClassConstructors does and port it

    public JCClassDecl convert(final Tree.ClassOrInterface def) {

        ClassVisitor visitor = new ClassVisitor();
        def.visitChildren(visitor);

        if (def instanceof Tree.AnyClass) {
            // Constructor
            visitor.defs.append(createConstructor(def, visitor));

            // FIXME:
            // insertOverloadedClassConstructors(defs,
            // (CeylonTree.ClassDeclaration) cdecl);
        }

        addGettersAndSetters(visitor.defs, visitor.attributeDecls);

        visitor.langAnnotations.appendList(gen.makeAtCeylon());
        
        return at(def).ClassDef(
                at(def).Modifiers((long) convertClassDeclFlags(def), visitor.langAnnotations.toList()),
                names().fromString(def.getIdentifier().getText()),
                visitor.typeParams.toList(),
                getSuperclass(def),
                convertSatisfiedTypes(def.getDeclarationModel().getSatisfiedTypes()),
                visitor.defs.toList());
    }

    private List<JCExpression> convertSatisfiedTypes(java.util.List<ProducedType> list) {
        if (list == null) {
            return List.nil();
        }

        ListBuffer<JCExpression> satisfies = new ListBuffer<JCExpression>();
        for (ProducedType t : list) {
            satisfies.append(gen.makeJavaType(t, true));
        }
        return satisfies.toList();
    }


    private JCTree getSuperclass(Tree.ClassOrInterface cdecl) {
        JCTree superclass;
        if (cdecl instanceof Tree.AnyInterface) {
            // The VM insists that interfaces have java.lang.Object as their
            // superclass
            superclass = makeIdent(syms().objectType);
        } else {
            superclass = getSuperclass(((Tree.AnyClass) cdecl).getDeclarationModel().getExtendedType());
        }
        return superclass;
    }

    private JCTree getSuperclass(ProducedType extendedType) {
        JCExpression superclass = gen.makeJavaType(extendedType, true);
        // simplify if we can
        if(superclass instanceof JCTree.JCFieldAccess
                && ((JCTree.JCFieldAccess)superclass).sym.type == syms().objectType)
            superclass = null;
        return superclass;
    }

    private JCMethodDecl createConstructor(Tree.Declaration cdecl, ClassVisitor visitor) {
        return at(cdecl).MethodDef(
                make().Modifiers(convertConstructorDeclFlags(cdecl)),
                names().init,
                at(cdecl).TypeIdent(VOID),
                List.<JCTypeParameter>nil(),
                visitor.params.toList(),
                List.<JCExpression>nil(),
                at(cdecl).Block(0, visitor.initStmts.toList().appendList(visitor.stmts().toList())),
                null);
    }

    public boolean existsParam(ListBuffer<? extends JCTree> params, Name attrName) {
        for (JCTree decl : params) {
            if (decl instanceof JCVariableDecl) {
                JCVariableDecl var = (JCVariableDecl)decl;
                if (var.name.equals(attrName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private JCTree.JCMethodDecl convert(AttributeSetterDefinition decl) {
        JCBlock body = gen.statementGen.convert(decl.getBlock());
        String name = decl.getIdentifier().getText();
        JCExpression type = gen.makeJavaType(gen.actualType(decl), false);
        return make().MethodDef(make().Modifiers(convertAttributeGetSetDeclFlags(decl)), names().fromString(Util.getSetterName(name)), 
                make().TypeIdent(TypeTags.VOID),
                List.<JCTree.JCTypeParameter>nil(), 
                List.<JCTree.JCVariableDecl>of(make().VarDef(make().Modifiers(0), names().fromString(name), type, null)), 
                List.<JCTree.JCExpression>nil(), 
                body, null);
    }

    public JCTree.JCMethodDecl convert(AttributeGetterDefinition decl) {
        JCBlock body = gen.statementGen.convert(decl.getBlock());
        List<JCAnnotation> annots = gen.makeJavaTypeAnnotations(decl.getDeclarationModel(), gen.actualType(decl));
        return make().MethodDef(make().Modifiers(convertAttributeGetSetDeclFlags(decl), annots),
                names().fromString(Util.getGetterName(decl.getIdentifier().getText())), 
                gen.makeJavaType(gen.actualType(decl), false), 
                List.<JCTree.JCTypeParameter>nil(), 
                List.<JCTree.JCVariableDecl>nil(), 
                List.<JCTree.JCExpression>nil(), 
                body, null);
    }

    private int convertClassDeclFlags(Tree.ClassOrInterface cdecl) {
        int result = 0;

        result |= isShared(cdecl) ? PUBLIC : 0;
        result |= isAbstract(cdecl) && (cdecl instanceof Tree.AnyClass) ? ABSTRACT : 0;
        result |= (cdecl instanceof Tree.AnyInterface) ? INTERFACE : 0;

        return result;
    }

    private int convertMethodDeclFlags(Tree.AnyMethod def) {
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

    private int convertConstructorDeclFlags(Tree.Declaration cdecl) {
        int result = 0;

        if (cdecl instanceof Tree.ObjectDefinition)
            result |= PRIVATE;
        else if (isShared(cdecl))
            result |= PUBLIC;

        return result;
    }

    private int convertAttributeFieldDeclFlags(Tree.AttributeDeclaration cdecl) {
        int result = 0;

        result |= isMutable(cdecl) ? 0 : FINAL;
        result |= PRIVATE;

        return result;
    }

    private int convertLocalDeclFlags(Tree.AttributeDeclaration cdecl) {
        int result = 0;

        result |= isMutable(cdecl) ? 0 : FINAL;

        return result;
    }

    private int convertAttributeGetSetDeclFlags(Tree.TypedDeclaration cdecl) {
        int result = 0;

        result |= isShared(cdecl) ? PUBLIC : PRIVATE;
        result |= (isFormal(cdecl) && !isDefault(cdecl)) ? ABSTRACT : 0;
        result |= !(isFormal(cdecl) || isDefault(cdecl)) ? FINAL : 0;

        return result;
    }

    private int convertObjectDeclFlags(Tree.ObjectDefinition cdecl) {
        int result = 0;

        result |= FINAL;
        result |= isShared(cdecl) ? PUBLIC : 0;

        return result;
    }

    private void addGettersAndSetters(final ListBuffer<JCTree> defs, ListBuffer<Tree.AttributeDeclaration> attributeDecls) {
        class GetterVisitor extends Visitor {
            public void visit(Tree.AttributeDeclaration decl) {
                defs.add(makeGetter(decl));
                if(isMutable(decl))
                    defs.add(makeSetter(decl));
            }
        }
        GetterVisitor v = new GetterVisitor();
        for(Tree.AttributeDeclaration def : attributeDecls){
            def.visit(v);
        }
    }

    private JCTree makeGetter(Tree.AttributeDeclaration decl) {
        // FIXME: add at() calls?
        String atrrName = decl.getIdentifier().getText();
        JCBlock body = null;
        if (!isFormal(decl)) {
            body = make().Block(0, List.<JCTree.JCStatement>of(make().Return(gen.makeSelect("this", atrrName))));
        }
        
        JCExpression type = gen.makeJavaType(gen.actualType(decl), false);
        int mods = convertAttributeGetSetDeclFlags(decl);
        List<JCAnnotation> annots = gen.makeJavaTypeAnnotations(decl.getDeclarationModel(), gen.actualType(decl));
        if (isActual(decl)) {
            annots = annots.appendList(gen.makeAtOverride());
        }
        
        return make().MethodDef(make().Modifiers(mods, annots),
                names().fromString(Util.getGetterName(atrrName.toString())),
                type,
                List.<JCTree.JCTypeParameter>nil(),
                List.<JCTree.JCVariableDecl>nil(),
                List.<JCTree.JCExpression>nil(),
                body, null);
    }

    private JCTree makeSetter(Tree.AttributeDeclaration decl) {
        // FIXME: add at() calls?
        String atrrName = decl.getIdentifier().getText();
        JCBlock body = null;
        if (!isFormal(decl)) {
            body = make().Block(0, List.<JCTree.JCStatement>of(
                    make().Exec(
                            make().Assign(gen.makeSelect("this", atrrName),
                                    makeIdent(atrrName.toString())))));
        }
        
        JCExpression type = gen.makeJavaType(gen.actualType(decl), false);
        int mods = convertAttributeGetSetDeclFlags(decl);
        List<JCAnnotation> annots = gen.makeJavaTypeAnnotations(decl.getDeclarationModel(), gen.actualType(decl));
        final ListBuffer<JCAnnotation> langAnnotations = new ListBuffer<JCAnnotation>();
        if (isActual(decl)) {
            langAnnotations.appendList(gen.makeAtOverride());
        }
        
        return make().MethodDef(make().Modifiers(mods, langAnnotations.toList()),
                names().fromString(Util.getSetterName(atrrName.toString())), 
                make().TypeIdent(TypeTags.VOID), 
                List.<JCTree.JCTypeParameter>nil(), 
                List.<JCTree.JCVariableDecl>of(make().VarDef(make().Modifiers(0, annots), names().fromString(atrrName), type , null)), 
                List.<JCTree.JCExpression>nil(), 
                body, null);
    }

    private List<JCTree> convert(Tree.AnyMethod def) {
        List<JCTree> result = List.<JCTree> nil();
        final ListBuffer<JCVariableDecl> params = new ListBuffer<JCVariableDecl>();
        final ListBuffer<JCAnnotation> langAnnotations = new ListBuffer<JCAnnotation>();
        final ListBuffer<JCTypeParameter> typeParams = new ListBuffer<JCTypeParameter>();
        JCExpression restype;

        for (Tree.Parameter param : def.getParameterLists().get(0).getParameters()) {
            params.append(convert(param));
        }

        if (def.getTypeParameterList() != null) {
            for (Tree.TypeParameterDeclaration t : def.getTypeParameterList().getTypeParameterDeclarations()) {
                typeParams.append(convert(t));
            }
        }

        if (def.getType() instanceof VoidModifier) {
            restype = make().TypeIdent(VOID);
        } else {
            restype = gen.makeJavaType(gen.actualType(def), false);
            langAnnotations.appendList(gen.makeJavaTypeAnnotations(def.getDeclarationModel(), gen.actualType(def)));
        }
        
        // FIXME: Handle lots more flags here

        if (isActual(def)) {
            langAnnotations.appendList(gen.makeAtOverride());
        }

        JCBlock body = null;
        if (def instanceof Tree.MethodDefinition) {
            body = gen.statementGen.convert(((Tree.MethodDefinition)def).getBlock());
        }
                
        String name = def.getIdentifier().getText();
        int mods = convertMethodDeclFlags(def);
        JCMethodDecl meth = at(def).MethodDef(make().Modifiers(mods, langAnnotations.toList()), 
                names().fromString(name), 
                restype, typeParams.toList(), 
                params.toList(), List.<JCExpression> nil(), body, null);
        result = result.append(meth);
        
        return result;
    }

    public JCClassDecl methodClass(Tree.MethodDefinition def) {
        Name name = generateClassName(def, isToplevel(def));
        List<JCTree> meth = convert(def);
        // make a private constructor
        JCMethodDecl constr = make().MethodDef(make().Modifiers(Flags.PRIVATE),
                names().init,
                make().TypeIdent(VOID),
                List.<JCTree.JCTypeParameter>nil(),
                List.<JCTree.JCVariableDecl>nil(),
                List.<JCTree.JCExpression>nil(),
                make().Block(0, List.<JCTree.JCStatement>nil()),
                null);
        meth = meth.prepend(constr);
        
        List<JCAnnotation> annots = gen.makeAtCeylon().appendList(gen.makeAtMethod());
        return at(def).ClassDef(
                at(def).Modifiers(FINAL | (isShared(def) ? PUBLIC : 0), annots),
                name,
                List.<JCTypeParameter>nil(),
                null,
                List.<JCExpression>nil(),
                meth);
    }

    /**
     * Generates the class name for a method, object or attribute definition. If <tt>topLevel</tt> is <tt>true</tt>,
     * uses the declaration name, otherwise uses a generated name.
     *
     * @param decl the Ceylon declaration (actually a definition) to generate the class name for.
     * @param topLevel a boolean indicating whether the declaration is a top-level one.
     * @return the generated name.
     */
    private Name generateClassName(Tree.Declaration decl, boolean topLevel) {
        String name;
        if (topLevel)
            name = decl.getIdentifier().getText();
        else
            name = aliasName(decl.getIdentifier().getText() + "$class");
        return names().fromString(name);
    }

    public JCClassDecl objectClass(Tree.ObjectDefinition def, boolean topLevel) {
        ClassVisitor visitor = new ClassVisitor();
        def.visitChildren(visitor);

        visitor.defs.append(createConstructor(def, visitor));

        addGettersAndSetters(visitor.defs, visitor.attributeDecls);

        Name name = generateClassName(def, topLevel);

        visitor.langAnnotations.appendList(gen.makeAtCeylon());
        
        TypeDeclaration decl = def.getDeclarationModel().getType().getDeclaration();

        if (topLevel) {
            appendObjectGlobal(visitor.defs, def, make().Ident(name));
        }

        return at(def).ClassDef(
                at(def).Modifiers((long) convertObjectDeclFlags(def), visitor.langAnnotations.toList()),
                name,
                List.<JCTypeParameter>nil(),
                getSuperclass(decl.getExtendedType()),
                convertSatisfiedTypes(decl.getSatisfiedTypes()),
                visitor.defs.toList());
    }

    public void appendObjectGlobal(ListBuffer<JCTree> defs, Tree.ObjectDefinition decl, JCExpression generatedClassName) {
        GlobalGen.DefinitionBuilder builder = gen
                .globalGenAt(decl)
                .defineGlobal(generatedClassName, decl.getIdentifier().getText())
                // Add @Object
                .classAnnotations(gen.makeAtObject())
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
    }
    // this is due to the above commented code
    @SuppressWarnings("unused")
    private JCExpression convert(final Tree.Annotation userAnn, Tree.ClassOrInterface classDecl, String methodName) {
        List<JCExpression> values = List.<JCExpression> nil();
        // FIXME: handle named arguments
        for (Tree.PositionalArgument arg : userAnn.getPositionalArgumentList().getPositionalArguments()) {
            values = values.append(gen.expressionGen.convertExpression(arg.getExpression()));
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

    private JCTypeParameter convert(Tree.TypeParameterDeclaration param) {
        Tree.Identifier name = param.getIdentifier();
        ListBuffer<JCExpression> bounds = new ListBuffer<JCExpression>();
        java.util.List<ProducedType> types = param.getDeclarationModel().getSatisfiedTypes();
        for (ProducedType t : types) {
            if (!gen.willErase(t)) {
                bounds.append(gen.makeJavaType(t, false));
            }
        }
        return at(param).TypeParameter(names().fromString(name.getText()), bounds.toList());
    }

    private JCVariableDecl convert(Tree.Parameter param) {
        at(param);
        String name = param.getIdentifier().getText();
        JCExpression type = gen.makeJavaType(gen.actualType(param), false);
        List<JCAnnotation> annots = gen.makeJavaTypeAnnotations(param.getDeclarationModel(), gen.actualType(param));
        JCVariableDecl v = at(param).VarDef(make().Modifiers(FINAL, annots), names().fromString(name), type, null);

        return v;
    }

    public JCTree convert(AttributeDeclaration decl) {
        GlobalGen.DefinitionBuilder builder = gen.globalGenAt(decl)
            .defineGlobal(
                    gen.makeJavaType(gen.actualType(decl), false),
                    decl.getIdentifier().getText());

        // Add @Attribute (@Ceylon gets added by default)
        builder.classAnnotations(gen.makeAtAttribute());

        builder.valueAnnotations(gen.makeJavaTypeAnnotations(decl.getDeclarationModel(), gen.actualType(decl)));

        if (isShared(decl)) {
            builder
                    .classVisibility(PUBLIC)
                    .getterVisibility(PUBLIC)
                    .setterVisibility(PUBLIC);
        }

        if (!isMutable(decl)) {
            builder.immutable();
        }

        if (decl.getSpecifierOrInitializerExpression() != null) {
            builder.initialValue(gen.expressionGen.convertExpression(
                    decl.getSpecifierOrInitializerExpression().getExpression()));
        }

        return builder.build();
    }

}
