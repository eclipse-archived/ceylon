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

package com.redhat.ceylon.compiler.java.codegen;

import static com.redhat.ceylon.compiler.java.codegen.Naming.DeclNameFlag.QUALIFIED;
import static com.sun.tools.javac.code.Flags.ABSTRACT;
import static com.sun.tools.javac.code.Flags.FINAL;
import static com.sun.tools.javac.code.Flags.INTERFACE;
import static com.sun.tools.javac.code.Flags.PRIVATE;
import static com.sun.tools.javac.code.Flags.PROTECTED;
import static com.sun.tools.javac.code.Flags.PUBLIC;
import static com.sun.tools.javac.code.Flags.STATIC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.redhat.ceylon.compiler.java.codegen.Naming.DeclNameFlag;
import com.redhat.ceylon.compiler.java.codegen.Naming.Substitution;
import com.redhat.ceylon.compiler.loader.model.LazyInterface;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.FunctionalParameter;
import com.redhat.ceylon.compiler.typechecker.model.Generic;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypeAlias;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.model.ValueParameter;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeGetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeSetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Block;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InvocationExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LazySpecifierExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierStatement;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

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

    public List<JCTree> transform(final Tree.ClassOrInterface def) {
        final ClassOrInterface model = def.getDeclarationModel();
        
        // we only create types for aliases so they can be imported with the model loader
        // and since we can't import local declarations let's just not create those types
        // in that case
        if(model.isAlias()
                && Decl.isAncestorLocal(def))
            return List.nil();
        
        naming.noteDecl(model);
        final String javaClassName;
        String ceylonClassName = def.getIdentifier().getText();
        if (def instanceof Tree.AnyInterface) {
            javaClassName = naming.declName(model, QUALIFIED).replaceFirst(".*\\.", "");
        } else {
            javaClassName = Naming.quoteClassName(def.getIdentifier().getText());
        }
        ClassDefinitionBuilder instantiatorImplCb;
        ClassDefinitionBuilder instantiatorDeclCb;
        if (Decl.withinInterface(model)) {
            instantiatorImplCb = gen().current().getCompanionBuilder((Interface)model.getContainer());
            instantiatorDeclCb = gen().current();
        } else {
            instantiatorImplCb = gen().current();
            instantiatorDeclCb = null;
        }
        ClassDefinitionBuilder classBuilder = ClassDefinitionBuilder
                .klass(this, javaClassName, ceylonClassName)
                .forDefinition(def);

        // Very special case for the remote possibility that we're compiling Void
        if ("ceylon.language::Void".equals(model.getQualifiedNameString())) {
            classBuilder.extending(null);
        }
        
        if (def instanceof Tree.AnyClass) {
            Tree.ParameterList paramList = ((Tree.AnyClass)def).getParameterList();
            Class cls = ((Tree.AnyClass)def).getDeclarationModel();
            // Member classes need a instantiator method
            boolean generateInstantiator = Strategy.generateInstantiator(cls);
            if(generateInstantiator){
                generateInstantiators(model, classBuilder, paramList, cls, instantiatorDeclCb, instantiatorImplCb);
            }

            if(def instanceof Tree.ClassDefinition){
                transformClass(def, model, classBuilder, paramList, generateInstantiator, cls, instantiatorDeclCb, instantiatorImplCb);
            }else{
                // class alias
                classBuilder.constructorModifiers(PRIVATE);
                classBuilder.annotations(makeAtAlias(model.getExtendedType()));
                classBuilder.isAlias(true);
            }
        }
        
        if (def instanceof Tree.AnyInterface) {
            if(def instanceof Tree.InterfaceDefinition){
                transformInterface(def, model, classBuilder);
            }else{
                // interface alias
                classBuilder.annotations(makeAtAlias(model.getExtendedType()));
                classBuilder.isAlias(true);
            }
        }

        // make sure we set the container in case we move it out
        addAtContainer(classBuilder, model);
        
        // Transform the class/interface members
        List<JCStatement> childDefs = visitClassOrInterfaceDefinition(def, classBuilder);

        // If it's a Class without initializer parameters...
        if (Strategy.generateMain(def)) {
            // ... then add a main() method
            classBuilder.method(makeMainForClass(model));
        }
        
        return classBuilder
            .modelAnnotations(model.getAnnotations())
            .modifiers(transformClassDeclFlags(def))
            .satisfies(model.getSatisfiedTypes())
            .caseTypes(model.getCaseTypes(), model.getSelfType())
            .init(childDefs)
            .build();
    }

    private List<JCStatement> visitClassOrInterfaceDefinition(Node def, ClassDefinitionBuilder classBuilder) {
        // Transform the class/interface members
        CeylonVisitor visitor = gen().visitor;
        
        // don't visit if we have errors in the initialiser
        if(def instanceof Tree.ClassOrInterface && visitor.hasClassInitialiserErrors((Tree.ClassOrInterface)def))
            return List.nil();
        
        final ListBuffer<JCTree> prevDefs = visitor.defs;
        final boolean prevInInitializer = visitor.inInitializer;
        final ClassDefinitionBuilder prevClassBuilder = visitor.classBuilder;
        try {
            visitor.defs = new ListBuffer<JCTree>();
            visitor.inInitializer = true;
            visitor.classBuilder = classBuilder;
            
            def.visitChildren(visitor);
            return (List<JCStatement>)visitor.getResult().toList();
        } finally {
            visitor.classBuilder = prevClassBuilder;
            visitor.inInitializer = prevInInitializer;
            visitor.defs = prevDefs;
        }
    }

    private void generateInstantiators(ClassOrInterface model, ClassDefinitionBuilder classBuilder, Tree.ParameterList paramList,
            Class cls, ClassDefinitionBuilder instantiatorDeclCb, ClassDefinitionBuilder instantiatorImplCb) {
        // TODO Instantiators on companion classes
        classBuilder.constructorModifiers(PROTECTED);
        if (Decl.withinInterface(cls)) {
            MethodDefinitionBuilder instBuilder = MethodDefinitionBuilder.systemMethod(this, naming.getInstantiatorMethodName(cls));
            makeOverloadsForDefaultedParameter(0,
                    instBuilder,
                    model, paramList, null);
            instantiatorDeclCb.method(instBuilder);
        }
        if (!Decl.withinInterface(cls)
                || !model.isFormal()) {
            MethodDefinitionBuilder instBuilder = MethodDefinitionBuilder.systemMethod(this, naming.getInstantiatorMethodName(cls));
            makeOverloadsForDefaultedParameter(!cls.isFormal() ? OL_BODY : 0,
                    instBuilder,
                    model, paramList, null);
            instantiatorImplCb.method(instBuilder);
        }
    }

    private void transformClass(com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassOrInterface def, ClassOrInterface model, ClassDefinitionBuilder classBuilder, 
            com.redhat.ceylon.compiler.typechecker.tree.Tree.ParameterList paramList, boolean generateInstantiator, 
            Class cls, ClassDefinitionBuilder instantiatorDeclCb, ClassDefinitionBuilder instantiatorImplCb) {
        
        for (Tree.Parameter param : paramList.getParameters()) {
            // Overloaded instantiators
            Parameter paramModel = param.getDeclarationModel();
            Parameter refinedParam = (Parameter)CodegenUtil.getTopmostRefinedDeclaration(param.getDeclarationModel());
            at(param);
            classBuilder.parameter(paramModel);
            if (paramModel.isDefaulted()
                    || paramModel.isSequenced()
                    || (generateInstantiator
                            && (refinedParam.isDefaulted()
                                    || refinedParam.isSequenced()))) {
                ClassDefinitionBuilder cbForDevaultValues;
                ClassDefinitionBuilder cbForDevaultValuesDecls = null;
                switch (Strategy.defaultParameterMethodOwner(model)) {
                case STATIC:
                    cbForDevaultValues = classBuilder;
                    break;
                case OUTER:
                    cbForDevaultValues = classBuilder.getContainingClassBuilder();
                    break;
                case OUTER_COMPANION:
                    cbForDevaultValues = classBuilder.getContainingClassBuilder().getCompanionBuilder(Decl.getClassOrInterfaceContainer(model, true));
                    cbForDevaultValuesDecls = classBuilder.getContainingClassBuilder();
                    break;
                default:
                    cbForDevaultValues = classBuilder.getCompanionBuilder(model);
                }
                if (generateInstantiator && refinedParam != paramModel) {}
                else {
                    cbForDevaultValues.method(makeParamDefaultValueMethod(false, def.getDeclarationModel(), paramList, param));
                    if (cbForDevaultValuesDecls != null) {
                        cbForDevaultValuesDecls.method(makeParamDefaultValueMethod(true, def.getDeclarationModel(), paramList, param));
                    }
                }
                if (generateInstantiator) {
                    if (Decl.withinInterface(cls)) {
                        MethodDefinitionBuilder instBuilder = MethodDefinitionBuilder.systemMethod(this, naming.getInstantiatorMethodName(cls));
                        makeOverloadsForDefaultedParameter(0,
                                instBuilder,
                                model, paramList, param);
                        instantiatorDeclCb.method(instBuilder);
                    }
                    MethodDefinitionBuilder instBuilder = MethodDefinitionBuilder.systemMethod(this, naming.getInstantiatorMethodName(cls));
                    makeOverloadsForDefaultedParameter(OL_BODY,
                            instBuilder,
                            model, paramList, param);
                    instantiatorImplCb.method(instBuilder);
                } else {
                    // Add overloaded constructors for defaulted parameter
                    MethodDefinitionBuilder overloadBuilder = classBuilder.addConstructor();
                    makeOverloadsForDefaultedParameter(OL_BODY,
                            overloadBuilder,
                            model, paramList, param);
                }
            }
        }
        satisfaction((Class)model, classBuilder);
        at(def);
        // Generate the inner members list for model loading
        addAtMembers(classBuilder, model);
    }

    private void transformInterface(com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassOrInterface def, ClassOrInterface model, ClassDefinitionBuilder classBuilder) {
        //  Copy all the qualifying type's type parameters into the interface
        ProducedType type = model.getType().getQualifyingType();
        while (type != null) {
            java.util.List<TypeParameter> typeArguments = type.getDeclaration().getTypeParameters();
            if (typeArguments == null) {
                continue;
            }
            for (TypeParameter typeArgument : typeArguments) {
                classBuilder.typeParameter(typeArgument);
            }
            type = type.getQualifyingType();
        }
        
        classBuilder.method(makeCompanionAccessor((Interface)model, model.getType(), false));
        // Build the companion class
        buildCompanion(def, (Interface)model, classBuilder);
        
        // Generate the inner members list for model loading
        addAtMembers(classBuilder, model);
    }

    private void addAtMembers(ClassDefinitionBuilder classBuilder, ClassOrInterface model) {
        List<JCExpression> members = List.nil();
        Package pkg = Decl.getPackageContainer(model);
        for(Declaration member : model.getMembers()){
            if(member instanceof ClassOrInterface == false
                    && member instanceof TypeAlias == false){
                continue;
            }
            TypeDeclaration innerType = (TypeDeclaration) member;
            // figure out its java name (strip the leading dot)
            String javaClass = naming.declName(innerType, DeclNameFlag.QUALIFIED).substring(1);
            String ceylonName = member.getName();
            JCAnnotation atMember = makeAtMember(ceylonName, javaClass, pkg.getQualifiedNameString());
            members = members.prepend(atMember);
        }
        classBuilder.annotations(makeAtMembers(members));
    }

    private void addAtContainer(ClassDefinitionBuilder classBuilder, TypeDeclaration model) {
        Package pkg = Decl.getPackageContainer(model);
        Scope scope = model.getContainer();
        if(scope == null || scope instanceof ClassOrInterface == false)
            return;
        ClassOrInterface container = (ClassOrInterface) scope; 
        // figure out its java name (strip the leading dot)
        String javaClass = naming.declName(container, DeclNameFlag.QUALIFIED).substring(1);
        String ceylonName = container.getName();
        List<JCAnnotation> atContainer = makeAtContainer(ceylonName, javaClass, pkg.getQualifiedNameString());
        classBuilder.annotations(atContainer);
    }

    private void satisfaction(final Class model, ClassDefinitionBuilder classBuilder) {
        final java.util.List<ProducedType> satisfiedTypes = model.getSatisfiedTypes();
        Set<Interface> satisfiedInterfaces = new HashSet<Interface>();
        // start by saying that we already satisfied each interface from superclasses
        Class superClass = model.getExtendedTypeDeclaration();
        while(superClass != null){
            for(TypeDeclaration interfaceDecl : superClass.getSatisfiedTypeDeclarations()){
                collectInterfaces((Interface) interfaceDecl, satisfiedInterfaces);
            }
            superClass = superClass.getExtendedTypeDeclaration();
        }
        // now satisfy each new interface
        for (ProducedType satisfiedType : satisfiedTypes) {
            TypeDeclaration decl = satisfiedType.getDeclaration();
            if (!(decl instanceof Interface)) {
                continue;
            }
            concreteMembersFromSuperinterfaces((Class)model, classBuilder, satisfiedType, satisfiedInterfaces);
        }
    }

    private void collectInterfaces(Interface interfaceDecl, Set<Interface> satisfiedInterfaces) {
        if(satisfiedInterfaces.add(interfaceDecl)){
            for(TypeDeclaration newInterfaceDecl : interfaceDecl.getSatisfiedTypeDeclarations()){
                collectInterfaces((Interface) newInterfaceDecl, satisfiedInterfaces);
            }
        }
    }

    /**
     * Generates companion fields ($Foo$impl) and methods
     */
    private void concreteMembersFromSuperinterfaces(final Class model,
            ClassDefinitionBuilder classBuilder, 
            ProducedType satisfiedType, Set<Interface> satisfiedInterfaces) {
        satisfiedType = satisfiedType.resolveAliases();
        Interface iface = (Interface)satisfiedType.getDeclaration();
        if (satisfiedInterfaces.contains(iface)
                || iface.getType().isExactly(typeFact().getIdentifiableDeclaration().getType())) {
            return;
        }
     
        // If there is no $impl (e.g. implementing a Java interface) 
        // then don't instantiate it...
        if (hasImpl(iface)) {
            // ... otherwise for each satisfied interface, 
            // instantiate an instance of the 
            // companion class in the constructor and assign it to a
            // $Interface$impl field
            transformInstantiateCompanions(classBuilder,
                    model, iface, satisfiedType);
        }
        
        if(!Decl.isCeylon(iface)){
            // let's not try to implement CMI for Java interfaces
            return;
        }
        
        // When we implement an interface with union or intersection type arguments
        // we have to make method results and parameters raw too
        boolean rawifyParametersAndResults = needsRawification(satisfiedType);
        // For each super interface
        for (Declaration member : iface.getMembers()) {
            
            if (member instanceof Class
                    && Strategy.generateInstantiator(member)
                    && model.getDirectMember(member.getName(), null, false) == null) {
                // instantiator method implementation
                Class klass = (Class)member;
                generateInstantiatorDelegate(classBuilder, satisfiedType,
                        iface, rawifyParametersAndResults, klass);
            } 
            
            if (Strategy.onlyOnCompanion(member)) {
                // non-shared interface methods don't need implementing
                // (they're just private methods on the $impl)
                continue;
            }
            if (member instanceof Method) {
                Method method = (Method)member;
                final ProducedTypedReference typedMember = satisfiedType.getTypedMember(method, Collections.<ProducedType>emptyList());
                final java.util.List<TypeParameter> typeParameters = method.getTypeParameters();
                final java.util.List<Parameter> parameters = method.getParameterLists().get(0).getParameters();
                if (!satisfiedInterfaces.contains((Interface)method.getContainer())) {
                    
                    for (Parameter param : parameters) {
                        if (param.isDefaulted()
                                || param.isSequenced()) {
                            final ProducedTypedReference typedParameter = typedMember.getTypedParameter(param);
                            // If that method has a defaulted parameter, 
                            // we need to generate a default value method
                            // which also delegates to the $impl
                            final MethodDefinitionBuilder defaultValueDelegate = makeDelegateToCompanion(iface,
                                    typedParameter,
                                    PUBLIC | FINAL, 
                                    typeParameters, 
                                    typedParameter.getType(), 
                                    Naming.getDefaultedParamMethodName(method, param), 
                                    parameters.subList(0, parameters.indexOf(param)),
                                    rawifyParametersAndResults);
                            classBuilder.method(defaultValueDelegate);
                            
                            final MethodDefinitionBuilder overload = makeDelegateToCompanion(iface,
                                    typedMember,
                                    PUBLIC | FINAL, 
                                    typeParameters,  
                                    typedMember.getType(), 
                                    naming.selector(method), 
                                    parameters.subList(0, parameters.indexOf(param)),
                                    rawifyParametersAndResults);
                            classBuilder.method(overload);
                        }
                    }
                    
                }
                // if it has the *most refined* default concrete member, 
                // then generate a method on the class
                // delegating to the $impl instance
                if (needsCompanionDelegate(model, member)) {
                    
                    final MethodDefinitionBuilder concreteMemberDelegate = makeDelegateToCompanion(iface,
                            typedMember,
                            PUBLIC | (method.isDefault() ? 0 : FINAL),
                            method.getTypeParameters(), 
                            method.getType(), 
                            naming.selector(method), 
                            method.getParameterLists().get(0).getParameters(),
                            rawifyParametersAndResults);
                    classBuilder.method(concreteMemberDelegate);
                     
                }
            } else if (member instanceof Getter
                    || member instanceof Setter
                    || member instanceof Value) {
                TypedDeclaration attr = (TypedDeclaration)member;
                final ProducedTypedReference typedMember = satisfiedType.getTypedMember(attr, null);
                if (needsCompanionDelegate(model, member)) {
                    if (member instanceof Value 
                            || member instanceof Getter) {
                        final MethodDefinitionBuilder getterDelegate = makeDelegateToCompanion(iface, 
                                typedMember,
                                PUBLIC | (attr.isDefault() ? 0 : FINAL), 
                                Collections.<TypeParameter>emptyList(), 
                                typedMember.getType(), 
                                Naming.getGetterName(attr), 
                                Collections.<Parameter>emptyList(),
                                rawifyParametersAndResults);
                        classBuilder.method(getterDelegate);
                    }
                    if (member instanceof Setter) { 
                        final MethodDefinitionBuilder setterDelegate = makeDelegateToCompanion(iface, 
                                typedMember,
                                PUBLIC | (((Setter)member).getGetter().isDefault() ? 0 : FINAL), 
                                Collections.<TypeParameter>emptyList(), 
                                typeFact().getAnythingDeclaration().getType(), 
                                Naming.getSetterName(attr), 
                                Collections.<Parameter>singletonList(((Setter)member).getParameter()),
                                rawifyParametersAndResults);
                        classBuilder.method(setterDelegate);
                    }
                    if (member instanceof Value 
                            && ((Value)attr).isVariable()) {
                        // I don't *think* this can happen because although a 
                        // variable Value can be declared on an interface it 
                        // will need to we refined as a Getter+Setter on a 
                        // subinterface in order for there to be a method in a 
                        // $impl to delegate to
                        throw new RuntimeException();
                    }
                }
            } else if (needsCompanionDelegate(model, member)) {
                log.error("ceylon", "Unhandled concrete interface member " + member.getQualifiedNameString() + " " + member.getClass());
            }
        }
        
        // Add $impl instances for the whole interface hierarchy
        satisfiedInterfaces.add(iface);
        for (ProducedType sat : iface.getSatisfiedTypes()) {
            sat = satisfiedType.getSupertype(sat.getDeclaration());
            concreteMembersFromSuperinterfaces(model, classBuilder, sat, satisfiedInterfaces);
        }
        
    }

    private void generateInstantiatorDelegate(
            ClassDefinitionBuilder classBuilder, ProducedType satisfiedType,
            Interface iface, boolean rawifyParametersAndResults, Class klass) {
        ProducedType typeMember = satisfiedType.getTypeMember(klass, Collections.<ProducedType>emptyList());
        java.util.List<TypeParameter> typeParameters = klass.getTypeParameters();
        java.util.List<Parameter> parameters = klass.getParameterLists().get(0).getParameters();
        
        String instantiatorMethodName = naming.getInstantiatorMethodName(klass);
        for (Parameter param : parameters) {
            if (param.isDefaulted()
                    || param.isSequenced()) {
                final ProducedTypedReference typedParameter = typeMember.getTypedParameter(param);
                // If that method has a defaulted parameter, 
                // we need to generate a default value method
                // which also delegates to the $impl
                final MethodDefinitionBuilder defaultValueDelegate = makeDelegateToCompanion(iface,
                        typedParameter,
                        PUBLIC | FINAL, 
                        typeParameters, 
                        typedParameter.getType(),
                        Naming.getDefaultedParamMethodName(klass, param), 
                        parameters.subList(0, parameters.indexOf(param)),
                        rawifyParametersAndResults);
                classBuilder.method(defaultValueDelegate);
                
                final MethodDefinitionBuilder overload = makeDelegateToCompanion(iface,
                        typeMember,
                        PUBLIC | FINAL, 
                        typeParameters,  
                        typeMember.getType(), 
                        instantiatorMethodName, 
                        parameters.subList(0, parameters.indexOf(param)),
                        rawifyParametersAndResults);
                classBuilder.method(overload);
            }
        }
        final MethodDefinitionBuilder overload = makeDelegateToCompanion(iface,
                typeMember,
                PUBLIC | FINAL, 
                typeParameters,  
                typeMember.getType(), 
                instantiatorMethodName, 
                parameters,
                rawifyParametersAndResults);
        classBuilder.method(overload);
    }

    private boolean needsRawification(ProducedType type) {
        final Iterator<TypeParameter> refinedTypeParameters = type.getDeclaration().getTypeParameters().iterator();
        boolean rawifyParametersAndResults = false;
        for (ProducedType typeArg : type.getTypeArgumentList()) {
            final TypeParameter typeParam = refinedTypeParameters.next();
            if (typeParam.getSatisfiedTypes().isEmpty()
                    && (typeFact().isIntersection(typeArg) || typeFact().isUnion(typeArg))) {
                // Use the same hack that makeJavaType() does when handling iterables
                if (!willEraseToSequential(typeArg)) {
                    rawifyParametersAndResults = true;
                    break;
                }
            }
        }
        return rawifyParametersAndResults;
    }

    private boolean needsCompanionDelegate(final Class model, Declaration member) {
        final boolean mostRefined;
        Declaration m = model.getMember(member.getName(), null, false);
        if (member instanceof Setter && m instanceof Getter) {
            mostRefined = member.equals(((Getter)m).getSetter());
        } else {
            mostRefined = member.equals(m);
        }
        return mostRefined
                && (member.isDefault() || !member.isFormal());
    }

    /**
     * Generates a method which delegates to the companion instance $Foo$impl
     */
    private MethodDefinitionBuilder makeDelegateToCompanion(Interface iface,
            ProducedReference typedMember, final long mods,
            final java.util.List<TypeParameter> typeParameters,
            final ProducedType methodType,
            final String methodName, final java.util.List<Parameter> parameters, 
            boolean rawifyParametersAndResults) {
        final MethodDefinitionBuilder concreteWrapper = MethodDefinitionBuilder.systemMethod(gen(), methodName);
        concreteWrapper.modifiers(mods);
        concreteWrapper.ignoreAnnotations();
        concreteWrapper.isOverride(true);
        if (!rawifyParametersAndResults) {
            for (TypeParameter tp : typeParameters) {
                concreteWrapper.typeParameter(tp);
            }
        }
        boolean explicitReturn = false;
        Declaration member = typedMember.getDeclaration();
        if (!isVoid(methodType) 
                || ((member instanceof Method || member instanceof Getter || member instanceof Value) && !Decl.isUnboxedVoid(member)) 
                || (member instanceof Method && Strategy.useBoxedVoid((Method)member))) {
            explicitReturn = true;
            if (typedMember instanceof ProducedTypedReference) {
                concreteWrapper.resultTypeNonWidening((ProducedTypedReference)typedMember, typedMember.getType(),
                        rawifyParametersAndResults ? JT_RAW_TP_BOUND : 0);
            } else {
                concreteWrapper.resultType(null, makeJavaType((ProducedType)typedMember));
            }
        }
        
        ListBuffer<JCExpression> arguments = ListBuffer.<JCExpression>lb();
        for (Parameter param : parameters) {
            final ProducedTypedReference typedParameter = typedMember.getTypedParameter(param);
            concreteWrapper.parameter(param, typedParameter.getType(), FINAL, rawifyParametersAndResults ? JT_RAW_TP_BOUND : 0);
            arguments.add(naming.makeName(param, Naming.NA_MEMBER));
        }
        JCExpression expr = make().Apply(
                null,  // TODO Type args
                makeSelect(getCompanionFieldName(iface), methodName),
                arguments.toList());
        if (!explicitReturn) {
            concreteWrapper.body(gen().make().Exec(expr));
        } else {
            concreteWrapper.body(gen().make().Return(expr));
        }
        return concreteWrapper;
    }

    private Boolean hasImpl(Interface iface) {
        if (gen().willEraseToObject(iface.getType())) {
            return false;
        }
        if (iface instanceof LazyInterface) {
            return ((LazyInterface)iface).isCeylon();
        }
        return true;
    }

    private void transformInstantiateCompanions(
            ClassDefinitionBuilder classBuilder, 
            Class model, Interface iface, ProducedType satisfiedType) {
        at(null);
        final List<JCExpression> state = List.<JCExpression>of(
                expressionGen().applyErasureAndBoxing(naming.makeThis(), 
                        model.getType(), false, true, BoxingStrategy.BOXED, 
                        satisfiedType, ExpressionTransformer.EXPR_FOR_COMPANION));

        JCExpression containerInstance = null;
        JCExpression ifaceImplType = null;
        if(!Decl.isToplevel(iface)){
            // if it's a member type we need to qualify the new instance with its $impl container
            ClassOrInterface interfaceContainer = Decl.getClassOrInterfaceContainer(iface, false);
            if(interfaceContainer instanceof Interface){
                ClassOrInterface modelContainer = model;
                while((modelContainer = Decl.getClassOrInterfaceContainer(modelContainer, false)) != null
                        && modelContainer.getType().getSupertype(interfaceContainer) == null){
                    // keep searching
                }
                Assert.that(modelContainer != null, "Could not find container that satisfies interface "
                        + iface.getQualifiedNameString() + " to find qualifying instance for companion instance for "
                        + model.getQualifiedNameString());
                // if it's an interface we just qualify it properly
                if(modelContainer instanceof Interface){
                    JCExpression containerType = makeJavaType(modelContainer.getType(), JT_COMPANION | JT_SATISFIES);
                    containerInstance = makeSelect(containerType, "this");
                    ifaceImplType = makeJavaType(satisfiedType, JT_COMPANION | JT_SATISFIES | JT_NON_QUALIFIED);
                }else{
                    // it's a class: find the right field used for the interface container impl
                    String containerFieldName = getCompanionFieldName((Interface)interfaceContainer);
                    JCExpression containerType = makeJavaType(modelContainer.getType(), JT_SATISFIES);
                    containerInstance = makeSelect(makeSelect(containerType, "this"), containerFieldName);
                    ifaceImplType = makeJavaType(satisfiedType, JT_COMPANION | JT_SATISFIES | JT_NON_QUALIFIED);
                }
            }
        }
        if(ifaceImplType == null){
            ifaceImplType = makeJavaType(satisfiedType, JT_COMPANION | JT_SATISFIES);
        }
        JCExpression newInstance = make().NewClass(containerInstance, 
                null,
                ifaceImplType,
                state,
                null);
        
        final String fieldName = getCompanionFieldName(iface);
        classBuilder.init(make().Exec(make().Assign(
                makeSelect("this", fieldName),// TODO Use qualified name for quoting? 
                newInstance)));
        
        classBuilder.field(PROTECTED | FINAL, fieldName, 
                makeJavaType(satisfiedType, AbstractTransformer.JT_COMPANION | JT_SATISFIES), null, false,
                makeAtIgnore());

        classBuilder.method(makeCompanionAccessor(iface, satisfiedType, true));
        
    }
    
    private MethodDefinitionBuilder makeCompanionAccessor(Interface iface, ProducedType satisfiedType, boolean forImplementor) {
        // Doing this only for interfaces with inner classes breaks BC for implementors
        // when an inner class is added to the interface. OTOH it means we 
        // don't have to have an access on every Ceylon interface when it 
        // isn't used on most of them.
        boolean hasInnerClasses = false;
        for (Declaration member : iface.getMembers()) {
            if (member instanceof Class) {
                hasInnerClasses = true;
                break;
            }
        }
        if (hasInnerClasses) {
            MethodDefinitionBuilder thisMethod = MethodDefinitionBuilder.systemMethod(
                    this, getCompanionAccessorName(iface));
            thisMethod.noAnnotations();
            if (!forImplementor && Decl.isAncestorLocal(iface)) {
                // For a local interface the return type cannot be a local
                // companion class, because that won't be visible at the 
                // top level, so use Object instead
                thisMethod.resultType(null, make().Type(syms().objectType));
            } else {
                thisMethod.resultType(null, makeJavaType(satisfiedType, JT_COMPANION));
            }
            if (forImplementor) {
                thisMethod.isOverride(true);
            } else {
                thisMethod.ignoreAnnotations();
            }
            thisMethod.modifiers(PUBLIC);
            if (forImplementor) {
                thisMethod.body(make().Return(naming.makeCompanionFieldName(iface)));
            } else {
                thisMethod.noBody();
            }
            return thisMethod;
        }
        return null;
    }

    private void buildCompanion(final Tree.ClassOrInterface def,
            final Interface model, ClassDefinitionBuilder classBuilder) {
        at(def);
        // Give the $impl companion a $this field...
        ClassDefinitionBuilder companionBuilder = classBuilder.getCompanionBuilder(model);
        
        ProducedType thisType = model.getType();
        companionBuilder.field(PRIVATE | FINAL, 
                "$this", 
                makeJavaType(thisType), 
                null, false);
        MethodDefinitionBuilder ctor = companionBuilder.addConstructor();
        ctor.modifiers(model.isShared() ? PUBLIC : 0);
        ParameterDefinitionBuilder pdb = ParameterDefinitionBuilder.instance(this, "$this");
        pdb.type(makeJavaType(thisType), null);
        // ...initialize the $this field from a ctor parameter...
        ctor.parameter(pdb);
        ListBuffer<JCStatement> bodyStatements = ListBuffer.<JCStatement>of(
                make().Exec(
                        make().Assign(
                                makeSelect(naming.makeThis(), "$this"), 
                                naming.makeQuotedThis())));
        ctor.body(bodyStatements.toList());
    }

    public List<JCStatement> transformRefinementSpecifierStatement(SpecifierStatement op, ClassDefinitionBuilder classBuilder) {
        List<JCStatement> result = List.<JCStatement>nil();
        // Check if this is a shortcut form of formal attribute refinement
        if (op.getRefinement()) {
            Tree.Term baseMemberTerm = op.getBaseMemberExpression();
            if(baseMemberTerm instanceof Tree.ParameterizedExpression)
                baseMemberTerm = ((Tree.ParameterizedExpression)baseMemberTerm).getPrimary();
            
            Tree.BaseMemberExpression expr = (BaseMemberExpression) baseMemberTerm;
            Declaration decl = expr.getDeclaration();
            
            if (decl instanceof Value) {
                // Now build a "fake" declaration for the attribute
                Tree.AttributeDeclaration attrDecl = new Tree.AttributeDeclaration(null);
                attrDecl.setDeclarationModel((Value)decl);
                attrDecl.setIdentifier(expr.getIdentifier());
                attrDecl.setScope(op.getScope());
    
                // Make sure the boxing information is set correctly
                BoxingDeclarationVisitor v = new CompilerBoxingDeclarationVisitor(this);
                v.visit(attrDecl);
                
                // Generate the attribute
                transform(attrDecl, classBuilder);
                
                // Generate the specifier statement
                result = result.append(expressionGen().transform(op));
            } else if (decl instanceof Method) {
                // Now build a "fake" declaration for the method
                Tree.MethodDeclaration methDecl = new Tree.MethodDeclaration(null);
                Method m = (Method)decl;
                methDecl.setDeclarationModel(m);
                methDecl.setIdentifier(expr.getIdentifier());
                methDecl.setScope(op.getScope());
                
                Tree.SpecifierExpression specifierExpression = op.getSpecifierExpression();
                methDecl.setSpecifierExpression(specifierExpression);
                
                if(specifierExpression instanceof Tree.LazySpecifierExpression == false){
                    Tree.Expression expression = specifierExpression.getExpression();
                    Tree.Term expressionTerm = Decl.unwrapExpressionsUntilTerm(expression);
                    // we can optimise lambdas and static method calls
                    if(!CodegenUtil.canOptimiseMethodSpecifier(expressionTerm, m)){
                        // we need a field to save the callable value
                        String name = naming.getMethodSpecifierAttributeName(m);
                        JCExpression specifierType = makeJavaType(expression.getTypeModel());
                        JCExpression specifier = expressionGen().transformExpression(expression);
                        classBuilder.field(PRIVATE | FINAL, name, specifierType, specifier, false);
                    }
                }
                
                for (ParameterList pl : m.getParameterLists()) {
                    Tree.ParameterList tpl = new Tree.ParameterList(null);
                    for (Parameter p : pl.getParameters()) {
                        Tree.Parameter tp = null;
                        if (p instanceof ValueParameter) {
                            Tree.ValueParameterDeclaration tvpd = new Tree.ValueParameterDeclaration(null);
                            tvpd.setDeclarationModel((ValueParameter)p);
                            tp = tvpd;
                        } else if (p instanceof FunctionalParameter) {
                            Tree.FunctionalParameterDeclaration tfpd = new Tree.FunctionalParameterDeclaration(null);
                            tfpd.setDeclarationModel((FunctionalParameter)p);
                            tp = tfpd;
                        }
                        tp.setScope(p.getContainer());
                        tp.setIdentifier(makeIdentifier(p.getName()));
                        tpl.addParameter(tp);
                    }
                    methDecl.addParameterList(tpl);
                }
                
                // Make sure the boxing information is set correctly
                BoxingDeclarationVisitor v = new CompilerBoxingDeclarationVisitor(this);
                v.visit(methDecl);
                
                // Generate the method
                classBuilder.method(methDecl);
            }
        } else {
            // Normal case, just generate the specifier statement
            result = result.append(expressionGen().transform(op));
        }
        return result;
    }

    private Tree.Identifier makeIdentifier(String name) {
        Tree.Identifier id = new Tree.Identifier(null);
        id.setText(name);
        return id;
    }
    
    public void transform(AttributeDeclaration decl, ClassDefinitionBuilder classBuilder) {
        final Value model = decl.getDeclarationModel();
        boolean lazy = decl.getSpecifierOrInitializerExpression() instanceof LazySpecifierExpression;
        boolean useField = Strategy.useField(model) && !lazy;
        String attrName = decl.getIdentifier().getText();

        // Only a non-formal or a concrete-non-lazy attribute has a corresponding field
        // and if a captured class parameter exists with the same name we skip this part as well
        Parameter parameter = CodegenUtil.findParamForDecl(decl);
        boolean createField = Strategy.createField(parameter, model) && !lazy;
        boolean concrete = Decl.withinInterface(decl)
                && decl.getSpecifierOrInitializerExpression() != null;
        if (!lazy && (concrete || (!Decl.isFormal(decl) && createField))) {
            ProducedTypedReference typedRef = getTypedReference(model);
            ProducedTypedReference nonWideningTypedRef = nonWideningTypeDecl(typedRef);
            ProducedType nonWideningType = nonWideningType(typedRef, nonWideningTypedRef);
            
            JCExpression initialValue = null;
            if (decl.getSpecifierOrInitializerExpression() != null) {
                Value declarationModel = model;
                initialValue = expressionGen().transformExpression(decl.getSpecifierOrInitializerExpression().getExpression(), 
                        CodegenUtil.getBoxingStrategy(declarationModel), 
                        nonWideningType);
            }

            int flags = 0;
            
            if (!CodegenUtil.isUnBoxed(nonWideningTypedRef.getDeclaration())) {
                flags |= JT_NO_PRIMITIVES;
            }
            JCExpression type = makeJavaType(nonWideningType, flags);

            int modifiers = (useField) ? transformAttributeFieldDeclFlags(decl) : transformLocalDeclFlags(decl);
            
            // If the attribute is really from a parameter then don't generate a field
            // (The ClassDefinitionBuilder does it in that case)
            if (parameter == null
                    || ((parameter instanceof ValueParameter) 
                            && ((ValueParameter)parameter).isHidden())) {
                if (concrete) {
                    classBuilder.getCompanionBuilder((TypeDeclaration)model.getContainer()).field(modifiers, attrName, type, initialValue, !useField);
                } else {
                    classBuilder.field(modifiers, attrName, type, initialValue, !useField);
                }        
            }
        }

        if (useField || Decl.withinInterface(decl) || lazy) {
            boolean generateCodeAsIfForCompanion = lazy && !Decl.withinInterface(decl);
            if (!Decl.withinInterface(decl) || model.isShared()) {
                // Generate getter in main class or interface (when shared)
                classBuilder.attribute(makeGetter(decl, generateCodeAsIfForCompanion));
            }
            if (Decl.withinInterface(decl)) {
                // Generate getter in companion class
                classBuilder.getCompanionBuilder((Interface)decl.getDeclarationModel().getContainer()).attribute(makeGetter(decl, true));
            }
            if (Decl.isMutable(decl)) {
                if (!Decl.withinInterface(decl) || model.isShared()) {
                    // Generate setter in main class or interface (when shared)
                    classBuilder.attribute(makeSetter(decl, generateCodeAsIfForCompanion));
                }
                if (Decl.withinInterface(decl)) {
                    // Generate setter in companion class
                    classBuilder.getCompanionBuilder((Interface)decl.getDeclarationModel().getContainer()).attribute(makeSetter(decl, true));
                }
            }
        }
    }

	public AttributeDefinitionBuilder transform(AttributeSetterDefinition decl, boolean forCompanion) {
	    if (Strategy.onlyOnCompanion(decl.getDeclarationModel()) && !forCompanion) {
	        return null;
	    }
        String name = decl.getIdentifier().getText();
        final AttributeDefinitionBuilder builder = AttributeDefinitionBuilder
                /* 
                 * We use the getter as TypedDeclaration here because this is the same type but has a refined
                 * declaration we can use to make sure we're not widening the attribute type.
                 */
            .setter(this, name, decl.getDeclarationModel().getGetter())
            .modifiers(transformAttributeGetSetDeclFlags(decl.getDeclarationModel(), false));
        
        // companion class members are never actual no matter what the Declaration says
        if(forCompanion)
            builder.notActual();
        
        if (Decl.withinClass(decl) || forCompanion) {
            JCBlock body = statementGen().transform(decl.getBlock());
            builder.setterBlock(body);
        } else {
            builder.isFormal(true);
        }
        
        return builder;
    }

    public AttributeDefinitionBuilder transform(AttributeGetterDefinition decl, boolean forCompanion) {
        if (Strategy.onlyOnCompanion(decl.getDeclarationModel()) && !forCompanion) {
            return null;
        }
        String name = decl.getIdentifier().getText();
        final AttributeDefinitionBuilder builder = AttributeDefinitionBuilder
            .getter(this, name, decl.getDeclarationModel())
            .modifiers(transformAttributeGetSetDeclFlags(decl.getDeclarationModel(), forCompanion));
        
        // companion class members are never actual no matter what the Declaration says
        if(forCompanion)
            builder.notActual();
        
        if (Decl.withinClass(decl) || forCompanion) {
            JCBlock body = statementGen().transform(decl.getBlock());
            builder.getterBlock(body);
        } else {
            builder.isFormal(true);
        }
        return builder;    
    }

    private int transformDeclarationSharedFlags(Declaration decl){
        return Decl.isShared(decl) && !Decl.isAncestorLocal(decl) ? PUBLIC : 0;
    }
    
    private int transformClassDeclFlags(ClassOrInterface cdecl) {
        int result = 0;

        result |= transformDeclarationSharedFlags(cdecl);
        result |= (cdecl.isAbstract() || cdecl.isFormal()) && (cdecl instanceof Class) ? ABSTRACT : 0;
        result |= (cdecl instanceof Interface) ? INTERFACE : 0;
        result |= cdecl.isAlias() && (cdecl instanceof Class) ? FINAL : 0;

        return result;
    }

    private int transformTypeAliasDeclFlags(TypeAlias decl) {
        int result = 0;

        result |= transformDeclarationSharedFlags(decl);
        result |= FINAL;

        return result;
    }

    private int transformClassDeclFlags(Tree.ClassOrInterface cdecl) {
        return transformClassDeclFlags(cdecl.getDeclarationModel());
    }
    
    private int transformOverloadCtorFlags(Class typeDecl) {
        return transformClassDeclFlags(typeDecl) & (PUBLIC | PRIVATE | PROTECTED);
    }
    
    private int transformMethodDeclFlags(Method def) {
        int result = 0;

        if (def.isToplevel()) {
            result |= def.isShared() ? PUBLIC : 0;
            result |= STATIC;
        } else if (Decl.isLocal(def)) {
            result |= def.isShared() ? PUBLIC : 0;
        } else {
            result |= def.isShared() ? PUBLIC : PRIVATE;
            result |= def.isFormal() && !def.isDefault() ? ABSTRACT : 0;
            result |= !(def.isFormal() || def.isDefault() || def.getContainer() instanceof Interface) ? FINAL : 0;
        }

        return result;
    }
    
    private int transformOverloadMethodDeclFlags(final Method model) {
        int mods = transformMethodDeclFlags(model);
        if (!Decl.withinInterface((model))) {
            mods |= FINAL;
        }
        return mods;
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

    private int transformAttributeGetSetDeclFlags(TypedDeclaration tdecl, boolean forCompanion) {
        if (tdecl instanceof Setter) {
            // Spec says: A setter may not be annotated shared, default or 
            // actual. The visibility and refinement modifiers of an attribute 
            // with a setter are specified by annotating the matching getter.
            tdecl = ((Setter)tdecl).getGetter();
        }
        
        int result = 0;

        result |= tdecl.isShared() ? PUBLIC : PRIVATE;
        result |= ((tdecl.isFormal() && !tdecl.isDefault()) && !forCompanion) ? ABSTRACT : 0;
        result |= !(tdecl.isFormal() || tdecl.isDefault() || Decl.withinInterface(tdecl)) || forCompanion ? FINAL : 0;

        return result;
    }

    private int transformObjectDeclFlags(Value cdecl) {
        int result = 0;

        result |= FINAL;
        result |= !Decl.isAncestorLocal(cdecl) && Decl.isShared(cdecl) ? PUBLIC : 0;

        return result;
    }

    private AttributeDefinitionBuilder makeGetterOrSetter(Tree.AttributeDeclaration decl, boolean forCompanion, AttributeDefinitionBuilder builder, boolean isGetter) {
        at(decl);
        if (forCompanion) {
            if (decl.getSpecifierOrInitializerExpression() != null) {
                Value declarationModel = decl.getDeclarationModel();
                ProducedTypedReference typedRef = getTypedReference(declarationModel);
                ProducedTypedReference nonWideningTypedRef = nonWideningTypeDecl(typedRef);
                ProducedType nonWideningType = nonWideningType(typedRef, nonWideningTypedRef);
                
                JCExpression expr = expressionGen().transformExpression(decl.getSpecifierOrInitializerExpression().getExpression(), 
                        CodegenUtil.getBoxingStrategy(declarationModel), 
                        nonWideningType);
                builder.getterBlock(make().Block(0, List.<JCStatement>of(make().Return(expr))));
            } else {
                JCExpression accessor = naming.makeQualifiedName(
                        naming.makeQuotedThis(), 
                        decl.getDeclarationModel(), 
                        Naming.NA_MEMBER | (isGetter ? Naming.NA_GETTER : Naming.NA_SETTER));
                
                if (isGetter) {
                    builder.getterBlock(make().Block(0, List.<JCStatement>of(make().Return(
                            make().Apply(
                                    null, 
                                    accessor, 
                                    List.<JCExpression>nil())))));
                } else {
                    List<JCExpression> args = List.<JCExpression>of(naming.makeName(decl.getDeclarationModel(), Naming.NA_MEMBER | Naming.NA_IDENT));
                    builder.setterBlock(make().Block(0, List.<JCStatement>of(make().Exec(
                            make().Apply(
                                    null, 
                                    accessor, 
                                    args)))));
                }
                
            }
        }
        if(forCompanion)
            builder.notActual();
        return builder
            .modifiers(transformAttributeGetSetDeclFlags(decl.getDeclarationModel(), forCompanion))
            .isFormal((Decl.isFormal(decl) || Decl.withinInterface(decl)) && !forCompanion);
    }
    
    private AttributeDefinitionBuilder makeGetter(Tree.AttributeDeclaration decl, boolean forCompanion) {
        at(decl);
        String attrName = decl.getIdentifier().getText();
        AttributeDefinitionBuilder getter = AttributeDefinitionBuilder
            .getter(this, attrName, decl.getDeclarationModel());
        return makeGetterOrSetter(decl, forCompanion, getter, true);
    }

    private AttributeDefinitionBuilder makeSetter(Tree.AttributeDeclaration decl, boolean forCompanion) {
        at(decl);
        String attrName = decl.getIdentifier().getText();
        AttributeDefinitionBuilder setter = AttributeDefinitionBuilder.setter(this, attrName, decl.getDeclarationModel());
        return makeGetterOrSetter(decl, forCompanion, setter, false);
    }

    public List<JCTree> transformWrappedMethod(Tree.AnyMethod def) {
        final Method model = def.getDeclarationModel();
        naming.noteDecl(model);
        // Generate a wrapper class for the method
        String name = def.getIdentifier().getText();
        ClassDefinitionBuilder builder = ClassDefinitionBuilder.methodWrapper(this, name, Decl.isShared(def));
        builder.methods(classGen().transform(def, builder));
        
        // Toplevel method
        if (Strategy.generateMain(def)) {
            // Add a main() method
            builder.method(makeMainForFunction(model));
        }
        
        List<JCTree> result = builder.build();
        
        if (Decl.isLocal(def)) {
            // Inner method
            JCVariableDecl call = at(def).VarDef(
                    make().Modifiers(FINAL),
                    naming.getSyntheticInstanceName(model),
                    naming.makeSyntheticClassname(model),
                    makeSyntheticInstance(model));
            result = result.append(call);
        } 
        return result;
    }

    public List<MethodDefinitionBuilder> transform(Tree.AnyMethod def, ClassDefinitionBuilder classBuilder) {
        // Transform the method body of the 'inner-most method'
        List<JCStatement> body = transformMethodBody(def);        
        return transform(def, classBuilder, body);
    }

    List<MethodDefinitionBuilder> transform(Tree.AnyMethod def,
            ClassDefinitionBuilder classBuilder, List<JCStatement> body) {
        final Method model = def.getDeclarationModel();
        final String methodName = naming.selector(model);
        if (Decl.withinInterface(model)) {
            // Transform it for the companion
            final Block block;
            final SpecifierExpression specifier;
            if (def instanceof MethodDeclaration) {
                block = null;
                specifier = ((MethodDeclaration) def).getSpecifierExpression();
            } else if (def instanceof MethodDefinition) {
                block = ((MethodDefinition) def).getBlock();
                specifier = null;
            } else {
                throw new RuntimeException();
            }
            boolean transformMethod = specifier != null || block != null;
            boolean actualAndAnnotations = def instanceof MethodDeclaration  && ((MethodDeclaration)def).getSpecifierExpression() == null;
            List<JCStatement> cbody = specifier != null ? transformMplBody(def.getParameterLists(), model, body) 
                    : block != null ? transformMethodBlock(model, block) 
                    : null;
                    
            boolean transformOverloads = def instanceof MethodDeclaration || block != null;
            int overloadsDelegator = OL_BODY | OL_IMPLEMENTOR | (def instanceof MethodDeclaration ? OL_DELEGATOR : 0);
            
            boolean transformDefaultValues = def instanceof MethodDeclaration || block != null;
            
            List<MethodDefinitionBuilder> companionDefs = transformMethod(def, model, methodName, 
                        transformMethod,
                        actualAndAnnotations,
                        cbody,
                        transformOverloads,
                        overloadsDelegator,
                        transformDefaultValues,
                        false);
            classBuilder.getCompanionBuilder((TypeDeclaration)model.getContainer()).methods(companionDefs);
        }
        
        List<MethodDefinitionBuilder> result = List.<MethodDefinitionBuilder>nil();
        if (!Strategy.onlyOnCompanion(model)) {
            // Transform it for the interface/class
            List<JCStatement> cbody = !model.isInterfaceMember() ? transformMplBody(def.getParameterLists(), model, body) : null;
            result = transformMethod(def, model, methodName, true, true, 
                    cbody, 
                    true,
                    Decl.withinInterface(model) ? 0 : OL_BODY,
                    true,
                    !Strategy.defaultParameterMethodOnSelf(model));
        }
        
        return result;
    }

    /**
     * Transforms a method, optionally generating overloads and 
     * default value methods
     * @param def The method
     * @param model The method model
     * @param methodName The method name
     * @param transformMethod Whether the method itself should be transformed.
     * @param actualAndAnnotations Whether the method itself is actual and has 
     * model annotations
     * @param body The body of the method (or null for an abstract method)
     * @param transformOverloads Whether to generate overload methods
     * @param overloadsFlags The overload flags
     * @param transformDefaultValues Whether to generate default value methods
     * @param defaultValuesBody Whether the default value methods should have a body
     */
    private List<MethodDefinitionBuilder> transformMethod(Tree.AnyMethod def,
            final Method model, final String methodName,
            boolean transformMethod, boolean actualAndAnnotations, List<JCStatement> body, 
            boolean transformOverloads, int overloadsFlags, 
            boolean transformDefaultValues, boolean defaultValuesBody) {
        
        ListBuffer<MethodDefinitionBuilder> lb = ListBuffer.<MethodDefinitionBuilder>lb();
        boolean needsRaw = false;
        if (Decl.withinClassOrInterface(model)) {
            final Scope refinedFrom = model.getRefinedDeclaration().getContainer();
            final ProducedType inheritedFrom = ((ClassOrInterface)model.getContainer()).getType().getSupertype((TypeDeclaration)refinedFrom);
            needsRaw = needsRawification(inheritedFrom);
        }
        
        final MethodDefinitionBuilder methodBuilder = MethodDefinitionBuilder.method(
                this, model.isClassOrInterfaceMember(), 
                methodName);
        final ParameterList parameterList = model.getParameterLists().get(0);
        Tree.ParameterList paramList = def.getParameterLists().get(0);
        for (Tree.Parameter param : paramList.getParameters()) {
            Parameter parameter = param.getDeclarationModel();
            methodBuilder.parameter(parameter, needsRaw ? JT_RAW_TP_BOUND : 0);
            if (parameter.isDefaulted()
                    || parameter.isSequenced()) {
                if (model.getRefinedDeclaration() == model) {
                    
                    if (transformOverloads) {
                        MethodDefinitionBuilder overloadBuilder = MethodDefinitionBuilder.method(this, model.isClassOrInterfaceMember(),
                                methodName);
                        MethodDefinitionBuilder overloadedMethod = makeOverloadsForDefaultedParameter(
                                overloadsFlags, 
                                overloadBuilder, 
                                model, parameterList.getParameters(), parameter);
                        lb.append(overloadedMethod);
                    }
                    
                    if (transformDefaultValues) {
                        lb.append(makeParamDefaultValueMethod(defaultValuesBody, model, paramList, param));    
                    }
                }
            }    
        }
        
        if (transformMethod) {
            methodBuilder.resultType(model, needsRaw ? JT_RAW_TP_BOUND : 0);
            if (!needsRaw) {
                copyTypeParameters(model, methodBuilder);
            }
            if (body != null) {
                // Construct the outermost method using the body we've built so far
                methodBuilder.body(body);
            } else {
                methodBuilder.noBody();
            }
            methodBuilder
                .modifiers(transformMethodDeclFlags(model));
            if (actualAndAnnotations) {
                methodBuilder.isOverride(model.isActual())
                    .modelAnnotations(model.getAnnotations());
            }
            if (CodegenUtil.hasCompilerAnnotation(def, "test")){
                methodBuilder.annotations(List.of(make().Annotation(naming.makeFQIdent("org", "junit", "Test"), List.<JCTree.JCExpression>nil())));
            }
            lb.append(methodBuilder);
        }
        return lb.toList();
    }

    /**
     * Constructs all but the outer-most method of a {@code Method} with 
     * multiple parameter lists 
     * @param model The {@code Method} model
     * @param body The inner-most body
     */
    List<JCStatement> transformMplBody(java.util.List<Tree.ParameterList> parameterListsTree,
            Method model,
            List<JCStatement> body) {
        ProducedType resultType = model.getType();
        for (int index = model.getParameterLists().size() - 1; index >  0; index--) {
            resultType = gen().typeFact().getCallableType(resultType);
            CallableBuilder cb = CallableBuilder.mpl(gen(), resultType, model.getParameterLists().get(index), parameterListsTree.get(index), body);
            body = List.<JCStatement>of(make().Return(cb.build()));
        }
        return body;
    }

    private List<JCStatement> transformMethodBody(Tree.AnyMethod def) {
        List<JCStatement> body = null;
        final Method model = def.getDeclarationModel();
        
        if (Decl.isDeferredOrParamInitialized(def)) {
            // Uninitialized or deferred initialized method => Make a Callable field
            final Parameter initializingParameter = CodegenUtil.findParamForDecl(def);
            int mods = PRIVATE;
            JCExpression initialValue;
            if (initializingParameter != null) {
                mods |= FINAL;
                int namingOptions = Naming.NA_MEMBER;
                if (initializingParameter.getContainer() instanceof Method) {
                    // We're initializing a local method, which will have a 
                    // class wrapper of the same name as the param, so 
                    // the param gets renamed
                    namingOptions |= Naming.NA_ALIASED;
                }
                initialValue = naming.makeName(initializingParameter, namingOptions);
            } else {
                // The field isn't initialized by a parameter, but later in the block
                initialValue = makeNull();
            }
            current().field(mods, model.getName(), makeJavaType(typeFact().getCallableType(model.getType())), initialValue, false);
            Invocation invocation = new CallableSpecifierInvocation(
                    this,
                    model,
                    initializingParameter != null ? 
                                            naming.makeName(initializingParameter, Naming.NA_MEMBER) : 
                                            naming.makeName(model, Naming.NA_MEMBER),
                    def);
            invocation.handleBoxing(true);
            JCExpression call = expressionGen().transformInvocation(invocation);
            JCStatement stmt;
            if (isVoid(def)) {
                stmt = make().Exec(call);
            } else {
                stmt = make().Return(call);
            }
            
            JCStatement result;
            if (initializingParameter == null) {
                // If the field isn't initialized by a parameter we have to 
                // cope with the possibility that it's never initialized
                final JCBinary cond = make().Binary(JCTree.EQ, naming.makeName(model, Naming.NA_MEMBER), makeNull());
                final JCStatement throw_ = make().Throw(make().NewClass(null, null, 
                        makeIdent(syms().ceylonUninitializedMethodErrorType), 
                        List.<JCExpression>nil(), 
                        null));
                result = make().If(cond, throw_, stmt);
            } else {
                result = stmt;
            }
            return List.<JCStatement>of(result);
        } else if (def instanceof Tree.MethodDefinition) {
            Scope container = model.getContainer();
            boolean isInterface = container instanceof com.redhat.ceylon.compiler.typechecker.model.Interface;
            if(!isInterface){
                final Block block = ((Tree.MethodDefinition) def).getBlock();
                body = transformMethodBlock(model, block);
            } 
        } else if (def instanceof MethodDeclaration
                && ((MethodDeclaration) def).getSpecifierExpression() != null) {
            body = transformSpecifiedMethodBody((MethodDeclaration)def, ((MethodDeclaration) def).getSpecifierExpression());
        }
        return body;
    }

    private List<JCStatement> transformMethodBlock(final Method model,
            final Block block) {
        List<JCStatement> body;
        boolean prevNoExpressionlessReturn = statementGen().noExpressionlessReturn;
        try {
            statementGen().noExpressionlessReturn = Decl.isMpl(model) || Strategy.useBoxedVoid(model);
            body = statementGen().transform(block).getStatements();    
        } finally {
            statementGen().noExpressionlessReturn = prevNoExpressionlessReturn;
        }
        // We void methods need to have their Callables return null
        // so adjust here.
        if ((Decl.isMpl(model) || Strategy.useBoxedVoid(model))
                && !block.getDefinitelyReturns()) {
            if (Decl.isUnboxedVoid(model)) {
                body = body.append(make().Return(makeNull()));
            } else {
                body = body.append(make().Return(makeErroneous(block, "non-void method doesn't definitely return")));
            }
        }
        return body;
    }

    List<JCStatement> transformSpecifiedMethodBody(Tree.MethodDeclaration  def, SpecifierExpression specifierExpression) {
        final Method model = def.getDeclarationModel();
        List<JCStatement> body;
        MethodDeclaration methodDecl = (MethodDeclaration)def;
        boolean isLazy = specifierExpression instanceof Tree.LazySpecifierExpression;
        boolean returnNull = false;
        JCExpression bodyExpr;
        Tree.Term term = null;
        if (specifierExpression != null
                && specifierExpression.getExpression() != null) {
            term = specifierExpression.getExpression().getTerm();
        }
        if (!isLazy && term instanceof Tree.FunctionArgument) {
            // Method specified with lambda: Don't bother generating a 
            // Callable, just transform the expr to use as the method body.
            Tree.FunctionArgument fa = (Tree.FunctionArgument)term;
            ProducedType resultType = model.getType();
            returnNull = isVoid(resultType) && fa.getExpression().getUnboxed();
            final java.util.List<com.redhat.ceylon.compiler.typechecker.tree.Tree.Parameter> lambdaParams = fa.getParameterLists().get(0).getParameters();
            final java.util.List<com.redhat.ceylon.compiler.typechecker.tree.Tree.Parameter> defParams = def.getParameterLists().get(0).getParameters();
            List<Substitution> substitutions = List.nil();
            for (int ii = 0; ii < lambdaParams.size(); ii++) {
                substitutions = substitutions.append(naming.addVariableSubst(lambdaParams.get(ii).getDeclarationModel(), 
                        defParams.get(ii).getIdentifier().getText()));
            }
            bodyExpr = gen().expressionGen().transformExpression(fa.getExpression(), BoxingStrategy.UNBOXED, null);
            bodyExpr = gen().expressionGen().applyErasureAndBoxing(bodyExpr, resultType, 
                    true, 
                    model.getUnboxed() ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED, 
                            resultType);
            for (Substitution subs : substitutions) {
                subs.close();
            }
        } else if (!isLazy && typeFact().isCallableType(term.getTypeModel())) {
            returnNull = isVoid(term.getTypeModel()) && term.getUnboxed();
            Method method = methodDecl.getDeclarationModel();
            Tree.Term primary = Decl.unwrapExpressionsUntilTerm(specifierExpression.getExpression());
            boolean lazy = specifierExpression instanceof Tree.LazySpecifierExpression;
            boolean inlined = CodegenUtil.canOptimiseMethodSpecifier(primary, method);
            Invocation invocation;
            if (lazy && primary instanceof InvocationExpression) {
                primary = ((InvocationExpression)primary).getPrimary();
            }
            if ((lazy || inlined)
                    && primary instanceof Tree.MemberOrTypeExpression
                    && ((Tree.MemberOrTypeExpression)primary).getDeclaration() instanceof Functional) {
                Declaration primaryDeclaration = ((Tree.MemberOrTypeExpression)primary).getDeclaration();
                ProducedReference producedReference = ((Tree.MemberOrTypeExpression)primary).getTarget();
                invocation = new MethodReferenceSpecifierInvocation(
                        this, 
                        (Tree.MemberOrTypeExpression)primary, 
                        primaryDeclaration,
                        producedReference,
                        method,
                        specifierExpression);
            } else if (!lazy && !inlined) {
                // must be a callable we stored
                String name = naming.getMethodSpecifierAttributeName(method);
                invocation = new CallableSpecifierInvocation(
                        this, 
                        method, 
                        naming.makeUnquotedIdent(name),
                        primary);
            } else if (isCeylonCallable(primary.getTypeModel())) {
                invocation = new CallableSpecifierInvocation(
                        this, 
                        method, 
                        expressionGen().transformExpression(primary),
                        primary);
            } else {
                throw Assert.fail("Unhandled primary " + primary);
            }
            invocation.handleBoxing(true);
            bodyExpr = expressionGen().transformInvocation(invocation);
        } else {
            ProducedTypedReference typedRef = getTypedReference(model);
            ProducedTypedReference nonWideningTypedRef = nonWideningTypeDecl(typedRef);
            ProducedType nonWideningType = nonWideningType(typedRef, nonWideningTypedRef);
            bodyExpr = expressionGen().transformExpression(term, CodegenUtil.getBoxingStrategy(model), nonWideningType);
        }
        if (!Decl.isUnboxedVoid(model) || Strategy.useBoxedVoid(model)) {
            if (returnNull) {
                body = List.<JCStatement>of(make().Exec(bodyExpr), make().Return(makeNull()));
            } else {
                body = List.<JCStatement>of(make().Return(bodyExpr));
            }
        } else {
            body = List.<JCStatement>of(make().Exec(bodyExpr));
        }
        return body;
    }

    private boolean isVoid(Tree.Declaration def) {
        if (def instanceof Tree.AnyMethod) {
            return gen().isVoid(((Tree.AnyMethod)def).getType().getTypeModel());
        } else if (def instanceof Tree.AnyClass) {
            // Consider classes void since ctors don't require a return statement
            return true;
        }
        throw new RuntimeException();
    }
    
    private boolean isVoid(Declaration def) {
        if (def instanceof Method) {
            return gen().isVoid(((Method)def).getType());
        } else if (def instanceof Class) {
            // Consider classes void since ctors don't require a return statement
            return true;
        }
        throw new RuntimeException();
    }

    private static int OL_BODY = 1<<0;
    private static int OL_IMPLEMENTOR = 1<<1;
    private static int OL_DELEGATOR = 1<<2;
    
    /**
     * Generates an overloaded method where all the defaulted parameters after 
     * and including the given {@code currentParam} are given their default 
     * values. Using Java-side overloading ensures positional invocations 
     * are binary compatible when new defaulted parameters are appended to a
     * parameter list.
     */
    private MethodDefinitionBuilder makeOverloadsForDefaultedParameter(
            int flags,
            MethodDefinitionBuilder overloadBuilder,
            Declaration model,
            Tree.ParameterList paramList,
            Tree.Parameter currentParam) {
        at(currentParam);
        java.util.List<Parameter> parameters = new java.util.ArrayList<Parameter>(paramList.getParameters().size());
        for (Tree.Parameter param : paramList.getParameters()) {
            parameters.add(param.getDeclarationModel());
        }
        return makeOverloadsForDefaultedParameter(flags,
                overloadBuilder, model,
                parameters, currentParam != null ? currentParam.getDeclarationModel() : null);
    }
    
    /**
     * Generates an overloaded method where all the defaulted parameters after 
     * and including the given {@code currentParam} are given their default 
     * values. Using Java-side overloading ensures positional invocations 
     * are binary compatible when new defaulted parameters are appended to a
     * parameter list.
     */
    private MethodDefinitionBuilder makeOverloadsForDefaultedParameter(
            int flags, MethodDefinitionBuilder overloadBuilder,
            final Declaration model, java.util.List<Parameter> parameters,
            final Parameter currentParam) {
        // need annotations for BC, but the method isn't really there
        overloadBuilder.ignoreAnnotations();
        
        final JCExpression methName;
        if (model instanceof Method) {
            long mods = transformOverloadMethodDeclFlags((Method)model);
            if ((flags & OL_BODY) != 0) {
                mods &= ~ABSTRACT;
            }
            if ((flags & OL_IMPLEMENTOR) != 0 || (flags & OL_DELEGATOR) != 0) {
                mods |= FINAL;
            }
            overloadBuilder.modifiers(mods);
            JCExpression qualifier;
            if ((flags & OL_DELEGATOR) != 0) {
                qualifier = naming.makeQuotedThis();
            } else {
                qualifier = null;
            }
            methName = naming.makeQualifiedName(qualifier, (Method)model, Naming.NA_MEMBER);
            overloadBuilder.resultType((Method)model, 0);
        } else if (model instanceof Class) {
            Class klass = (Class)model;
            if (Strategy.generateInstantiator(model)) {
                overloadBuilder.ignoreAnnotations();
                if (!klass.isAlias() 
                        && Strategy.generateInstantiator(klass.getExtendedTypeDeclaration())
                        && klass.isActual()){
                        //&& ((Class)model).getExtendedTypeDeclaration().getContainer() instanceof Class) {
                    overloadBuilder.isOverride(true);
                }
                // remove the FINAL bit in case it gets set, because that is valid for a class decl, but
                // not for a method if in an interface
                overloadBuilder.modifiers(transformClassDeclFlags(klass) & ~FINAL);
                methName = naming.makeInstantiatorMethodName(null, klass);
                JCExpression resultType;
                ProducedType type = klass.isAlias() ? klass.getExtendedType() : klass.getType();
                if (Decl.isAncestorLocal(model)) {
                    // We can't expose a local type name to a place it's not visible
                    resultType = make().Type(syms().objectType);
                } else {
                    resultType = makeJavaType(type);
                }
                overloadBuilder.resultType(null, resultType);
            } else {   
                overloadBuilder.modifiers(transformOverloadCtorFlags(klass));
                methName = naming.makeThis();
            }
        } else {
            throw new RuntimeException();
        }
        
        // TODO MPL
        if (model instanceof Method) {
            copyTypeParameters((Functional)model, overloadBuilder);
        } else if (Strategy.generateInstantiator(model)) {
            for (TypeParameter tp : typeParametersForInstantiator((Class)model)) {
                overloadBuilder.typeParameter(tp);
            }
        }

        // TODO Some simple default expressions (e.g. literals, null and 
        // base expressions it might be worth inlining the expression rather 
        // than calling the default value method.
        // TODO This really belongs in the invocation builder
        
        ListBuffer<JCExpression> args = ListBuffer.<JCExpression>lb();
        ListBuffer<JCStatement> vars = ListBuffer.<JCStatement>lb();
        
        final Naming.SyntheticName companionInstanceName = naming.temp("$impl$");
        if (model instanceof Class
                && !Strategy.defaultParameterMethodStatic(model)
                && !Strategy.defaultParameterMethodOnOuter(model)
                && currentParam != null) {
            Class classModel = (Class)model;
            vars.append(makeVar(companionInstanceName, 
                    makeJavaType(classModel.getType(), AbstractTransformer.JT_COMPANION),
                    make().NewClass(null, 
                            null,
                            makeJavaType(classModel.getType(), AbstractTransformer.JT_COMPANION),
                            List.<JCExpression>nil(), null)));
        }
        
        boolean useDefault = false;
        for (Parameter param2 : parameters) {
            
            if (param2 == currentParam) {
                useDefault = true;
            }
            if (useDefault) {
                List<JCExpression> typeArguments = List.<JCExpression>nil();
                JCIdent dpmQualifier;
                if (Strategy.defaultParameterMethodOnSelf(model) 
                        || Strategy.defaultParameterMethodOnOuter(model)
                        || (flags & OL_IMPLEMENTOR) != 0) {
                    dpmQualifier = null;
                } else if (Strategy.defaultParameterMethodStatic(model)){
                    dpmQualifier = null;
                    if (model instanceof Class) {
                        typeArguments = typeArguments((Class)model);
                    } else if (model instanceof Method) {
                        typeArguments = typeArguments((Method)model);
                    }
                } else {
                    dpmQualifier = companionInstanceName.makeIdent();
                }
                JCExpression defaultValueMethodName = naming.makeDefaultedParamMethod(dpmQualifier, param2);
                
                Naming.SyntheticName varName = naming.temp("$"+param2.getName()+"$");
                final ProducedType paramType;
                if (param2 instanceof FunctionalParameter) {
                    paramType = typeFact().getCallableType(param2.getType());
                } else {
                    paramType = param2.getType();
                }
                vars.append(makeVar(varName, 
                        makeJavaType(paramType), 
                        make().Apply(typeArguments, 
                                defaultValueMethodName, 
                                ListBuffer.<JCExpression>lb().appendList(args).toList())));
                args.add(varName.makeIdent());
            } else {
                overloadBuilder.parameter(param2, 0);
                args.add(naming.makeName(param2, Naming.NA_MEMBER | Naming.NA_ALIASED));
            }
        }
        
        // TODO Type args on method call
        if ((flags & OL_BODY) != 0) {
            JCExpression invocation;
            if (Strategy.generateInstantiator(model)) {
                Class klass = (Class) model;
                ProducedType type = klass.isAlias() ? klass.getExtendedType() : klass.getType();
                invocation = make().NewClass(null, 
                        null, 
                        makeJavaType(type, JT_CLASS_NEW | JT_NON_QUALIFIED),
                        args.toList(),
                        null);
            } else {
                invocation = make().Apply(List.<JCExpression>nil(),
                    methName, args.toList());
            }
               
            if (!isVoid(model)
                    || (model instanceof Method && Strategy.useBoxedVoid((Method)model)) 
                    || Strategy.generateInstantiator(model)) {
                if (!vars.isEmpty()) {
                    invocation = make().LetExpr(vars.toList(), invocation);
                }
                overloadBuilder.body(make().Return(invocation));
            } else {
                vars.append(make().Exec(invocation));
                invocation = make().LetExpr(vars.toList(), makeNull());
                overloadBuilder.body(make().Exec(invocation));
            }
        } else {
            overloadBuilder.noBody();
        }
        
        return overloadBuilder;
    }

    /**
     * When generating an instantiator method if the inner class has a type 
     * parameter with the same name as a type parameter of an outer type, then the 
     * instantiator method shouldn't declare its own type parameter of that 
     * name -- it should use the captured one. This method filters out the
     * type parameters of the inner class which are the same as type parameters 
     * of the outer class so that they can be captured.
     */
    private java.util.List<TypeParameter> typeParametersForInstantiator(final Class model) {
        Assert.that(Strategy.generateInstantiator(model));
        java.util.List<TypeParameter> filtered = new ArrayList<TypeParameter>();
        java.util.List<TypeParameter> tps = model.getTypeParameters();
        if (tps != null) {
            for (TypeParameter tp : tps) {
                boolean omit = false;
                Scope s = model.getContainer();
                while (!(s instanceof Package)) {
                    if (s instanceof Generic) {
                        for (TypeParameter outerTp : ((Generic)s).getTypeParameters()) {
                            if (tp.getName().equals(outerTp.getName())) {
                                omit = true;
                            }
                        }
                    }
                    s = s.getContainer();
                }
                if (!omit) {
                    filtered.add(tp);
                }
            }
        }
        return filtered;
    }

    /**
     * Creates a (possibly abstract) method for retrieving the value for a 
     * defaulted parameter
     */
    MethodDefinitionBuilder makeParamDefaultValueMethod(boolean noBody, Declaration container, 
            Tree.ParameterList params, Tree.Parameter currentParam) {
        at(currentParam);
        Parameter parameter = currentParam.getDeclarationModel();
        String name = Naming.getDefaultedParamMethodName(container, parameter );
        MethodDefinitionBuilder methodBuilder = MethodDefinitionBuilder.systemMethod(this, name);
        methodBuilder.ignoreAnnotations();
        int modifiers = 0;
        if (noBody) {
            modifiers |= PUBLIC | ABSTRACT;
        } else if (container == null
                || !(container instanceof Class 
                        && Strategy.defaultParameterMethodStatic(container))) {
            // initializers can override parameter defaults
            modifiers |= FINAL;
        }
        if (container != null && container.isShared()) {
            modifiers |= PUBLIC;
        } else if (container == null || (!container.isToplevel()
                && !noBody)){
            modifiers |= PRIVATE;
        }
        if (Strategy.defaultParameterMethodStatic(container)) {
            modifiers |= STATIC;
        }
        methodBuilder.modifiers(modifiers);
        
        if (container instanceof Method) {
            copyTypeParameters((Method)container, methodBuilder);
        } else if (container != null
                && Decl.isToplevel(container)
                && container instanceof Class) {
            copyTypeParameters((Class)container, methodBuilder);
        }
        
        // Add any of the preceding parameters as parameters to the method
        for (Tree.Parameter p : params.getParameters()) {
            if (p == currentParam) {
                break;
            }
            at(p);
            methodBuilder.parameter(p.getDeclarationModel(), 0);
        }

        // The method's return type is the same as the parameter's type
        methodBuilder.resultType(parameter, parameter.getType(), 0);

        // The implementation of the method
        if (noBody) {
            methodBuilder.noBody();
        } else {
            JCExpression expr = expressionGen().transform(currentParam);
            JCBlock body = at(currentParam).Block(0, List.<JCStatement> of(at(currentParam).Return(expr)));
            methodBuilder.block(body);
        }

        return methodBuilder;
    }

    public List<JCTree> transformObjectDefinition(Tree.ObjectDefinition def, ClassDefinitionBuilder containingClassBuilder) {
        return transformObject(def, def.getDeclarationModel(), 
                def.getAnonymousClass(), containingClassBuilder, Decl.isLocal(def));
    }
    
    public List<JCTree> transformObjectArgument(Tree.ObjectArgument def) {
        return transformObject(def, def.getDeclarationModel(), 
                def.getAnonymousClass(), null, false);
    }
    
    private List<JCTree> transformObject(Tree.StatementOrArgument def, Value model, 
            Class klass,
            ClassDefinitionBuilder containingClassBuilder,
            boolean makeLocalInstance) {
        naming.noteDecl(model);
        
        String name = model.getName();
        ClassDefinitionBuilder objectClassBuilder = ClassDefinitionBuilder.object(
                this, name);
        
        CeylonVisitor visitor = gen().visitor;
        final ListBuffer<JCTree> prevDefs = visitor.defs;
        final boolean prevInInitializer = visitor.inInitializer;
        final ClassDefinitionBuilder prevClassBuilder = visitor.classBuilder;
        List<JCStatement> childDefs;
        try {
            visitor.defs = new ListBuffer<JCTree>();
            visitor.inInitializer = true;
            visitor.classBuilder = objectClassBuilder;
            
            def.visitChildren(visitor);
            childDefs = (List<JCStatement>)visitor.getResult().toList();
        } finally {
            visitor.classBuilder = prevClassBuilder;
            visitor.inInitializer = prevInInitializer;
            visitor.defs = prevDefs;
        }

        satisfaction(klass, objectClassBuilder);
        
        TypeDeclaration decl = model.getType().getDeclaration();

        if (Decl.isToplevel(model)
                && def instanceof Tree.ObjectDefinition) {
            // generate a field and getter
            AttributeDefinitionBuilder builder = AttributeDefinitionBuilder
                    // TODO attr build take a JCExpression className
                    .wrapped(this, null, model.getName(), model, true)
                    .immutable()
                    .initialValue(makeNewClass(naming.makeName(model, Naming.NA_FQ | Naming.NA_WRAPPER)))
                    .is(PUBLIC, Decl.isShared(decl))
                    .is(STATIC, true);
            objectClassBuilder.defs(builder.build());
        }

        List<JCTree> result = objectClassBuilder
            .annotations(makeAtObject())
            .modelAnnotations(model.getAnnotations())
            .modifiers(transformObjectDeclFlags(model))
            .constructorModifiers(PRIVATE)
            .satisfies(decl.getSatisfiedTypes())
            .init(childDefs)
            .build();
        
        if (makeLocalInstance) {
            result = result.append(makeLocalIdentityInstance(name, objectClassBuilder.getClassName(), false));
        } else if (Decl.withinClassOrInterface(model)) {
            boolean visible = Decl.isCaptured(model);
            int modifiers = FINAL | ((visible) ? PRIVATE : 0);
            JCExpression type = makeJavaType(klass.getType());
            JCExpression initialValue = makeNewClass(makeJavaType(klass.getType()), null);
            containingClassBuilder.field(modifiers, name, type, initialValue, !visible);
            
            if (visible) {
                result = result.appendList(AttributeDefinitionBuilder
                    .getter(this, name, model)
                    .modifiers(transformAttributeGetSetDeclFlags(model, false))
                    .build());
            }
        }
        
        return result;
    }
    
    /**
     * Makes a {@code main()} method which calls the given top-level method
     * @param def
     */
    private MethodDefinitionBuilder makeMainForClass(ClassOrInterface model) {
        at(null);
        if(model.isAlias())
            model = model.getExtendedTypeDeclaration();
        JCExpression nameId = makeJavaType(model.getType(), JT_RAW);
        JCNewClass expr = make().NewClass(null, null, nameId, List.<JCTree.JCExpression>nil(), null);
        return makeMainMethod(model, expr);
    }
    
    /**
     * Makes a {@code main()} method which calls the given top-level method
     * @param method
     */
    private MethodDefinitionBuilder makeMainForFunction(Method method) {
        at(null);
        JCExpression qualifiedName = naming.makeName(method, Naming.NA_FQ | Naming.NA_WRAPPER | Naming.NA_MEMBER);
        MethodDefinitionBuilder mainMethod = makeMainMethod(method, make().Apply(null, qualifiedName, List.<JCTree.JCExpression>nil()));
        return mainMethod;
    }
    
    /** 
     * Makes a {@code main()} method which calls the given callee 
     * (a no-args method or class)
     * @param decl
     * @param callee
     */
    private MethodDefinitionBuilder makeMainMethod(Declaration decl, JCExpression callee) {
        // Add a main() method
        MethodDefinitionBuilder methbuilder = MethodDefinitionBuilder
                .main(this)
                .ignoreAnnotations();
        // Add call to process.setupArguments
        JCExpression argsId = makeUnquotedIdent("args");
        JCMethodInvocation processExpr = make().Apply(null, naming.makeLanguageValue("process"), List.<JCTree.JCExpression>nil());
        methbuilder.body(make().Exec(make().Apply(null, makeSelect(processExpr, "setupArguments"), List.<JCTree.JCExpression>of(argsId))));
        // Add call to toplevel method
        methbuilder.body(make().Exec(callee));
        return methbuilder;
    }
    
    void copyTypeParameters(Functional def, MethodDefinitionBuilder methodBuilder) {
        if (def.getTypeParameters() != null) {
            for (TypeParameter t : def.getTypeParameters()) {
                methodBuilder.typeParameter(t);
            }
        }
    }

    public List<JCTree> transform(final Tree.TypeAliasDeclaration def) {
        final TypeAlias model = def.getDeclarationModel();
        
        // we only create types for aliases so they can be imported with the model loader
        // and since we can't import local declarations let's just not create those types
        // in that case
        if(Decl.isAncestorLocal(def))
            return List.nil();
        
        naming.noteDecl(model);
        String ceylonClassName = def.getIdentifier().getText();
        final String javaClassName = Naming.quoteClassName(def.getIdentifier().getText());

        ClassDefinitionBuilder classBuilder = ClassDefinitionBuilder
                .klass(this, javaClassName, ceylonClassName);

        // class alias
        classBuilder.constructorModifiers(PRIVATE);
        classBuilder.annotations(makeAtTypeAlias(model.getExtendedType()));
        classBuilder.isAlias(true);

        // make sure we set the container in case we move it out
        addAtContainer(classBuilder, model);

        visitClassOrInterfaceDefinition(def, classBuilder);

        return classBuilder
            .modelAnnotations(model.getAnnotations())
            .modifiers(transformTypeAliasDeclFlags(model))
            .satisfies(model.getSatisfiedTypes())
            .build();
    }
}
