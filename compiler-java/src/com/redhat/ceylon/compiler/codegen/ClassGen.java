package com.redhat.ceylon.compiler.codegen;

import static com.sun.tools.javac.code.Flags.ABSTRACT;
import static com.sun.tools.javac.code.Flags.FINAL;
import static com.sun.tools.javac.code.Flags.INTERFACE;
import static com.sun.tools.javac.code.Flags.PRIVATE;
import static com.sun.tools.javac.code.Flags.PUBLIC;
import static com.sun.tools.javac.code.Flags.STATIC;
import static com.sun.tools.javac.code.TypeTags.VOID;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.redhat.ceylon.compiler.codegen.Gen2.Singleton;
import com.redhat.ceylon.compiler.typechecker.model.*;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeGetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeSetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.util.Util;
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
            JCExpression type = gen.makeJavaType(param.getType().getTypeModel());
            boolean sharedMethod = ((Declaration)param.getDeclarationModel().getContainer()).isShared();
            List<JCAnnotation> annots = gen.makeJavaTypeAnnotations(param.getType().getTypeModel(), sharedMethod);
            if (sharedMethod) {
                annots = annots.append(gen.makeAtName(name));
            }
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

                JCExpression type = gen.makeJavaType(gen.actualType(decl));
                List<JCAnnotation> annots = gen.makeJavaTypeAnnotations(gen.actualType(decl), false);

                if (useField) {
                    // A captured attribute gets turned into a field
                    int modifiers = convertAttributeFieldDeclFlags(decl);
                    defs.append(at(decl).VarDef(at(decl).Modifiers(modifiers, annots), attrName, type, null));
                    if (initialValue != null) {
                        // The attribute's initializer gets moved to the constructor
                        // because it might be using locals of the initializer
                        stmts.append(at(decl).Exec(at(decl).Assign(makeSelect("this", decl.getIdentifier().getText()), initialValue)));
                    }
                } else {
                    // Otherwise it's local to the constructor
                    int modifiers = convertLocalDeclFlags(decl);
                    stmts.append(at(decl).VarDef(at(decl).Modifiers(modifiers, annots), attrName, type, initialValue));
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

    public JCClassDecl convert(final Tree.ClassOrInterface cdecl) {

        ClassVisitor visitor = new ClassVisitor();
        cdecl.visitChildren(visitor);

        if (cdecl instanceof Tree.AnyClass) {
            // Constructor
            visitor.defs.append(createConstructor(cdecl, visitor));

            // FIXME:
            // insertOverloadedClassConstructors(defs,
            // (CeylonTree.ClassDeclaration) cdecl);
        }

        addGettersAndSetters(visitor.defs, visitor.attributeDecls);

        return at(cdecl).ClassDef(
                at(cdecl).Modifiers((long) convertClassDeclFlags(cdecl), visitor.langAnnotations.toList()),
                names().fromString(cdecl.getIdentifier().getText()),
                processTypeConstraints(cdecl.getTypeConstraintList(), visitor.typeParams.toList()),
                getSuperclass(cdecl),
                convertSatisfiedTypes(cdecl.getSatisfiedTypes()),
                visitor.defs.toList());
    }

    private List<JCExpression> convertSatisfiedTypes(Tree.SatisfiedTypes satisfiedTypes) {
        if (satisfiedTypes == null) {
            return List.nil();
        }

        ListBuffer<JCExpression> satisfies = new ListBuffer<JCExpression>();
        for (Tree.Type t : satisfiedTypes.getTypes()) {
            satisfies.append(gen.makeJavaType(t.getTypeModel()));
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
            superclass = convertExtendedType(((Tree.AnyClass) cdecl).getExtendedType());
        }
        return superclass;
    }

    private JCTree convertExtendedType(Tree.ExtendedType extendedType) {
        JCTree superclass;
        if (extendedType == null)
            superclass = makeIdent(syms().ceylonIdentifiableObjectType);
        else {
            superclass = gen.makeJavaType(extendedType.getType().getTypeModel());
        }
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
        JCExpression type = gen.makeJavaType(gen.actualType(decl));
        List<JCAnnotation> annots = gen.makeJavaTypeAnnotations(gen.actualType(decl), false);
        return make().MethodDef(make().Modifiers(0), names().fromString(Util.getSetterName(name)), 
                makeIdent("void"), 
                List.<JCTree.JCTypeParameter>nil(), 
                List.<JCTree.JCVariableDecl>of(make().VarDef(make().Modifiers(0, annots), names().fromString(name), type, null)), 
                List.<JCTree.JCExpression>nil(), 
                body, null);
    }

    public JCTree.JCMethodDecl convert(AttributeGetterDefinition decl) {
        JCBlock body = gen.statementGen.convert(decl.getBlock());
        List<JCAnnotation> annots = gen.makeJavaTypeAnnotations(gen.actualType(decl), false);
        return make().MethodDef(make().Modifiers(0, annots),
                names().fromString(Util.getGetterName(decl.getIdentifier().getText())), 
                gen.makeJavaType(gen.actualType(decl)), 
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

    private int convertMethodDeclFlags(Tree.Declaration cdecl) {
        int result = 0;

        result |= isShared(cdecl) ? PUBLIC : PRIVATE;
        result |= isFormal(cdecl) ? ABSTRACT : 0;
        result |= !(isFormal(cdecl) || isDefault(cdecl)) ? FINAL : 0;

        return result;
    }

    private int convertConstructorDeclFlags(Tree.Declaration cdecl) {
        int result = 0;

        result |= isShared(cdecl) ? PUBLIC : 0;

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

    private int convertAttributeGetSetDeclFlags(Tree.AttributeDeclaration cdecl) {
        int result = 0;

        result |= isShared(cdecl) ? PUBLIC : PRIVATE;
        result |= isFormal(cdecl) ? ABSTRACT : 0;
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
        
        JCExpression type = gen.makeJavaType(gen.actualType(decl));
        int mods = convertAttributeGetSetDeclFlags(decl);
        List<JCAnnotation> annots = gen.makeJavaTypeAnnotations(gen.actualType(decl), false);
        if (isActual(decl)) {
            annots = annots.append(gen.makeAtOverride());
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
        
        JCExpression type = gen.makeJavaType(gen.actualType(decl));
        int mods = convertAttributeGetSetDeclFlags(decl);
        List<JCAnnotation> annots = gen.makeJavaTypeAnnotations(gen.actualType(decl), false);
        final ListBuffer<JCAnnotation> langAnnotations = new ListBuffer<JCAnnotation>();
        if (isActual(decl)) {
            langAnnotations.append(gen.makeAtOverride());
        }
        
        return make().MethodDef(make().Modifiers(mods, langAnnotations.toList()),
                names().fromString(Util.getSetterName(atrrName.toString())), 
                makeIdent("void"), 
                List.<JCTree.JCTypeParameter>nil(), 
                List.<JCTree.JCVariableDecl>of(make().VarDef(make().Modifiers(0, annots), names().fromString(atrrName), type , null)), 
                List.<JCTree.JCExpression>nil(), 
                body, null);
    }

    // Rewrite a list of Ceylon-style type constraints into Java trees.
    // class TypeWithParameter<X, Y>()
    // given X satisfies List
    // given Y satisfies Comparable
    // becomes
    // class TypeWithParameter<X extends List, Y extends Comparable> extends
    // ceylon.Object {
    private List<JCTypeParameter> processTypeConstraints(Tree.TypeConstraintList typeConstraintList, List<JCTypeParameter> typeParams) {
        if (typeConstraintList == null)
            return typeParams;

        LinkedHashMap<String, JCTypeParameter> symtab = new LinkedHashMap<String, JCTypeParameter>();
        for (JCTypeParameter item : typeParams) {
            symtab.put(item.getName().toString(), item);
        }

        for (final Tree.TypeConstraint tc : typeConstraintList.getTypeConstraints()) {
            String name = tc.getIdentifier().getText();
            JCTypeParameter tp = symtab.get(name);
            if (tp == null)
                throw new RuntimeException("Class \"" + name + "\" in satisfies list not found");

            ListBuffer<JCExpression> bounds = new ListBuffer<JCExpression>();
            if (tc.getSatisfiedTypes() != null) {
                for (Tree.Type type : tc.getSatisfiedTypes().getTypes())
                    bounds.add(gen.makeJavaType(type.getTypeModel()));

                if (tp.getBounds() != null) {
                    tp.bounds = tp.getBounds().appendList(bounds.toList());
                } else {
                    JCTypeParameter newTp = at(tc).TypeParameter(names().fromString(name), bounds.toList());
                    symtab.put(name, newTp);
                }
            }

            if (tc.getAbstractedType() != null)
                throw new RuntimeException("\"abstracts\" not supported yet");
        }

        // FIXME: This just converts a map to a List. There ought to be a
        // better way to do it
        ListBuffer<JCTypeParameter> result = new ListBuffer<JCTypeParameter>();
        for (JCTypeParameter p : symtab.values()) {
            result.add(p);
        }
        return result.toList();
    }

    private List<JCTree> convert(Tree.MethodDefinition decl) {
        final Singleton<JCBlock> body = new Singleton<JCBlock>();
        
        body.append(gen.statementGen.convert(decl.getBlock()));
        
        return convertMethod(decl, body);
    }

    private List<JCTree> convert(Tree.MethodDeclaration decl) {
        return convertMethod(decl, null);
    }
    
    private List<JCTree> convertMethod(Tree.AnyMethod decl, Singleton<JCBlock> body) {
        List<JCTree> result = List.<JCTree> nil();
        final ListBuffer<JCVariableDecl> params = new ListBuffer<JCVariableDecl>();
        final ListBuffer<JCAnnotation> langAnnotations = new ListBuffer<JCAnnotation>();
        Singleton<JCExpression> restypebuf = new Singleton<JCExpression>();
        final ListBuffer<JCTypeParameter> typeParams = new ListBuffer<JCTypeParameter>();

        processMethodDeclaration(decl, params, restypebuf, typeParams, langAnnotations);
        
        JCExpression restype = restypebuf.thing();

        // FIXME: Handle lots more flags here

        if (isActual(decl)) {
            langAnnotations.append(gen.makeAtOverride());
        }

        int mods = convertMethodDeclFlags(decl);
        JCMethodDecl meth = at(decl).MethodDef(make().Modifiers(mods, langAnnotations.toList()), 
                names().fromString(decl.getIdentifier().getText()), 
                restype, processTypeConstraints(decl.getTypeConstraintList(), typeParams.toList()), 
                params.toList(), List.<JCExpression> nil(), (body != null) ? body.thing() : null, null);
        result = result.append(meth);
        
        return result;
    }

    public JCClassDecl methodClass(Tree.MethodDefinition decl) {
        final ListBuffer<JCVariableDecl> params = new ListBuffer<JCVariableDecl>();
        final Singleton<JCBlock> body = new Singleton<JCBlock>();
        Singleton<JCExpression> restype = new Singleton<JCExpression>();
        final ListBuffer<JCAnnotation> langAnnotations = new ListBuffer<JCAnnotation>();
        final ListBuffer<JCTypeParameter> typeParams = new ListBuffer<JCTypeParameter>();

        final boolean topLevel = decl.getScope().getContainer() instanceof Package;

        processMethodDeclaration(decl, params, restype, typeParams, langAnnotations);

        body.append(gen.statementGen.convert(decl.getBlock()));

        JCMethodDecl meth = at(decl).MethodDef(
                make().Modifiers((topLevel ? PUBLIC | STATIC : 0),langAnnotations.toList()),
                names().fromString("run"),
                restype.thing(),
                processTypeConstraints(decl.getTypeConstraintList(), typeParams.toList()),
                params.toList(), List.<JCExpression>nil(), body.thing(), null);

        return at(decl).ClassDef(
                at(decl).Modifiers((topLevel ? PUBLIC : 0), List.<JCAnnotation>nil()),
                generateClassName(decl, topLevel),
                List.<JCTypeParameter>nil(),
                makeIdent(syms().ceylonIdentifiableObjectType),
                List.<JCExpression>nil(),
                List.<JCTree>of(meth));
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

    // FIXME: There must be a better way to do this.
    private void processMethodDeclaration(final Tree.AnyMethod decl, final ListBuffer<JCVariableDecl> params, final Singleton<JCExpression> restype, final ListBuffer<JCTypeParameter> typeParams, final ListBuffer<JCAnnotation> langAnnotations) {

        for (Tree.Parameter param : decl.getParameterLists().get(0).getParameters()) {
            params.append(convert(param));
        }

        if (decl.getTypeParameterList() != null)
            for (Tree.TypeParameterDeclaration t : decl.getTypeParameterList().getTypeParameterDeclarations()) {
                typeParams.append(convert(t));
            }

        restype.append(gen.makeJavaType(gen.actualType(decl)));
        langAnnotations.appendList(gen.makeJavaTypeAnnotations(gen.actualType(decl), false));
    }

    public JCClassDecl objectClass(Tree.ObjectDefinition decl, boolean topLevel) {
        ClassVisitor visitor = new ClassVisitor();
        decl.visitChildren(visitor);

        visitor.defs.append(createConstructor(decl, visitor));

        addGettersAndSetters(visitor.defs, visitor.attributeDecls);

        Name name = generateClassName(decl, topLevel);

        if (topLevel) {
            // TODO: This seems like a hack. If decl is a top-level definition, we create the singleton to hold the
            // object instance here (inside the class). Otherwise we create a separate instance in StatementVisitor,
            // alongside the class.
            JCIdent nameIdent = make().Ident(name);
            visitor.defs.append(
                    at(decl).VarDef(
                            at(decl).Modifiers(PUBLIC | STATIC | FINAL, List.<JCTree.JCAnnotation>nil()),
                            names().fromString("$INSTANCE"),
                            nameIdent,
                            at(decl).NewClass(null, List.<JCExpression>nil(), nameIdent, List.<JCExpression>nil(), null)
                            ));
        }

        return at(decl).ClassDef(
                at(decl).Modifiers((long) convertObjectDeclFlags(decl), visitor.langAnnotations.toList()),
                name,
                List.<JCTypeParameter>nil(),
                convertExtendedType(decl.getExtendedType()),
                convertSatisfiedTypes(decl.getSatisfiedTypes()),
                visitor.defs.toList());
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
        JCExpression result = at(userAnn).Apply(null, makeSelect(primary.getIdentifier().getText(), "run"), values);
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

    private JCTypeParameter convert(Tree.TypeParameterDeclaration param) {
        // FIXME: implement this
        if (param.getTypeVariance() != null)
            throw new RuntimeException("Variance not implemented");
        Tree.Identifier name = param.getIdentifier();
        return at(param).TypeParameter(names().fromString(name.getText()), List.<JCExpression> nil());
    }

    private JCVariableDecl convert(Tree.Parameter param) {
        at(param);
        String name = param.getIdentifier().getText();
        JCExpression type = gen.makeJavaType(gen.actualType(param));
        boolean sharedMethod = ((Declaration)param.getDeclarationModel().getContainer()).isShared();
        List<JCAnnotation> annots = gen.makeJavaTypeAnnotations(gen.actualType(param), sharedMethod );
        if (sharedMethod) {
            annots = annots.append(gen.makeAtName(name));
        }
        JCVariableDecl v = at(param).VarDef(make().Modifiers(FINAL, annots), names().fromString(name), type, null);

        return v;
    }

    public JCTree convert(AttributeDeclaration decl) {
        // we make a class for it
        String name = decl.getIdentifier().getText();
        String className = "$"+name;
        String getterName = Util.getGetterName(name);
        String fieldName = "value";
        boolean shared = isShared(decl);
        boolean variable = isMutable(decl);
        JCExpression type = gen.makeJavaType(gen.actualType(decl));
        List<JCAnnotation> annots = gen.makeJavaTypeAnnotations(gen.actualType(decl), false);
        
        // its value
        JCExpression initialValue = null;
        if (decl.getSpecifierOrInitializerExpression() != null)
            initialValue = gen.expressionGen.convertExpression(decl.getSpecifierOrInitializerExpression().getExpression());

        // make a static var for it
        int varMods = PRIVATE | (variable ? 0 : FINAL) | STATIC; 
        JCVariableDecl varDef = at(decl).VarDef(make().Modifiers(varMods), names().fromString(fieldName), 
                type, initialValue);
        
        // now make a getter in any case
        int methodMods = (shared ? PUBLIC : 0) | STATIC;
        JCBlock getterBody = make().Block(0, List.<JCTree.JCStatement>of(make().Return(makeIdent(fieldName))));
        JCMethodDecl getter = make().MethodDef(make().Modifiers(methodMods, annots), names().fromString(getterName), 
                type, List.<JCTree.JCTypeParameter>nil(), List.<JCTree.JCVariableDecl>nil(), 
                List.<JCTree.JCExpression>nil(), getterBody, null);

        JCMethodDecl setter = null;
        if(variable){
            String setterName = Util.getSetterName(name);
            JCBlock setterBody = make().Block(0, 
                    List.<JCTree.JCStatement>of(
                            make().Exec(
                                    make().Assign(makeIdent(className, fieldName), 
                                            makeIdent(fieldName)))));
            setter = make().MethodDef(make().Modifiers(methodMods), 
                    names().fromString(setterName), 
                    makeIdent("void"), 
                    List.<JCTree.JCTypeParameter>nil(), 
                    List.<JCTree.JCVariableDecl>of(make().VarDef(make().Modifiers(0, annots), 
                            names().fromString(fieldName), type, null)), 
                    List.<JCTree.JCExpression>nil(), 
                    setterBody, null);
        }
        
        // and the class
        List<JCTree> defs = (setter != null) ?
                List.<JCTree>of(varDef, getter, setter)
                : List.<JCTree>of(varDef, getter);
        int classMods = (shared ? PUBLIC : 0) | FINAL;
        return make().ClassDef(make().Modifiers(classMods), names().fromString(className), 
                List.<JCTree.JCTypeParameter>nil(), null, List.<JCTree.JCExpression>nil(), defs);
    }
}
