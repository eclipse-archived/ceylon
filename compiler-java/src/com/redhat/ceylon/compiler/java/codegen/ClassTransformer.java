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

import static com.sun.tools.javac.code.Flags.ABSTRACT;
import static com.sun.tools.javac.code.Flags.FINAL;
import static com.sun.tools.javac.code.Flags.INTERFACE;
import static com.sun.tools.javac.code.Flags.PRIVATE;
import static com.sun.tools.javac.code.Flags.PROTECTED;
import static com.sun.tools.javac.code.Flags.PUBLIC;
import static com.sun.tools.javac.code.Flags.STATIC;

import static com.redhat.ceylon.compiler.java.codegen.Naming.DeclNameFlag.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.redhat.ceylon.compiler.loader.model.LazyInterface;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.FunctionalParameter;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
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
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Block;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.DefaultArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierStatement;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBinary;
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
        final ClassOrInterface model = def.getDeclarationModel();
        noteDecl(model);
        final String className;
        String aliasedClassName = def.getIdentifier().getText();
        if (def instanceof Tree.AnyInterface) {
            className = declName(model, QUALIFIED).replaceFirst(".*\\.", "");
        } else {
            className = def.getIdentifier().getText();
        }
        ClassDefinitionBuilder classBuilder = ClassDefinitionBuilder
                .klass(this, Decl.isAncestorLocal(def), className, aliasedClassName);

        if (def instanceof Tree.AnyClass) {
            Tree.ParameterList paramList = ((Tree.AnyClass)def).getParameterList();
            for (Tree.Parameter param : paramList.getParameters()) {
                classBuilder.parameter(param);
                DefaultArgument defaultArgument = param.getDefaultArgument();
                if (defaultArgument != null
                        || param.getDeclarationModel().isSequenced()) {
                    ClassDefinitionBuilder cbForDevaultValues;
                    if (Strategy.defaultParameterMethodStatic(model)) {
                        cbForDevaultValues = classBuilder;
                    } else {
                        cbForDevaultValues = classBuilder.getCompanionBuilder(model);
                    }
                    cbForDevaultValues.defs(makeParamDefaultValueMethod(false, def.getDeclarationModel(), paramList, param));
                    // Add overloaded constructors for defaulted parameter
                    MethodDefinitionBuilder overloadBuilder = classBuilder.addConstructor();
                    makeOverloadsForDefaultedParameter(OL_BODY,
                            overloadBuilder,
                            model, paramList, param);
                }
            }
            satisfaction((Class)model, classBuilder);
            at(def);
        }
        
        if (def instanceof Tree.AnyInterface) {
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
            
            classBuilder.defs(makeCompanionAccessor((Interface)model, model.getType(), false));
            // Build the companion class
            buildCompanion(def, (Interface)model, classBuilder);   
        }
        
        // Transform the class/interface members
        CeylonVisitor visitor = gen().visitor;
        final ListBuffer<JCTree> prevDefs = visitor.defs;
        final boolean prevInInitializer = visitor.inInitializer;
        final ClassDefinitionBuilder prevClassBuilder = visitor.classBuilder;
        List<JCStatement> childDefs;
        try {
            visitor.defs = new ListBuffer<JCTree>();
            visitor.inInitializer = true;
            visitor.classBuilder = classBuilder;
            
            def.visitChildren(visitor);
            childDefs = (List<JCStatement>)visitor.getResult().toList();
        } finally {
            visitor.classBuilder = prevClassBuilder;
            visitor.inInitializer = prevInInitializer;
            visitor.defs = prevDefs;
        }

        // If it's a Class without initializer parameters...
        if (Strategy.generateMain(def)) {
            // ... then add a main() method
            classBuilder.body(makeMainForClass(model));
        }
        
        return classBuilder
            .modelAnnotations(model.getAnnotations())
            .modifiers(transformClassDeclFlags(def))
            .satisfies(model.getSatisfiedTypes())
            .caseTypes(model.getCaseTypes())
            .of(model.getSelfType())
            .init(childDefs)
            .build();
    }

    private void satisfaction(final Class model, ClassDefinitionBuilder classBuilder) {
        final java.util.List<ProducedType> satisfiedTypes = model.getSatisfiedTypes();
        Set<Interface> satisfiedInterfaces = new HashSet<Interface>();
        for (ProducedType satisfiedType : satisfiedTypes) {
            TypeDeclaration decl = satisfiedType.getDeclaration();
            if (!(decl instanceof Interface)) {
                continue;
            }
            concreteMembersFromSuperinterfaces((Class)model, classBuilder, satisfiedType, satisfiedInterfaces);
        }
    }

    /**
     * Generates companion fields ($Foo$impl) and methods
     */
    private void concreteMembersFromSuperinterfaces(final Class model,
            ClassDefinitionBuilder classBuilder, 
            ProducedType satisfiedType, Set<Interface> satisfiedInterfaces) {
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
                            final JCMethodDecl defaultValueDelegate = makeDelegateToCompanion(iface,
                                    typedParameter,
                                    PUBLIC | FINAL, 
                                    typeParameters, 
                                    typedParameter.getType(), 
                                    Naming.getDefaultedParamMethodName(method, param), 
                                    parameters.subList(0, parameters.indexOf(param)),
                                    Decl.isAncestorLocal(model),
                                    rawifyParametersAndResults);
                            classBuilder.defs(defaultValueDelegate);
                            
                            final JCMethodDecl overload = makeDelegateToCompanion(iface,
                                    typedMember,
                                    PUBLIC | FINAL, 
                                    typeParameters,  
                                    typedMember.getType(), 
                                    Naming.quoteMethodName(method), 
                                    parameters.subList(0, parameters.indexOf(param)),
                                    Decl.isAncestorLocal(model),
                                    rawifyParametersAndResults);
                            classBuilder.defs(overload);
                        }
                    }
                }
                // if it has the *most refined* default concrete member, 
                // then generate a method on the class
                // delegating to the $impl instance
                if (needsCompanionDelegate(model, member)) {
                    
                    final JCMethodDecl concreteMemberDelegate = makeDelegateToCompanion(iface,
                            typedMember,
                            PUBLIC, 
                            method.getTypeParameters(), 
                            method.getType(), 
                            Naming.quoteMethodName(method), 
                            method.getParameterLists().get(0).getParameters(),
                            Decl.isAncestorLocal(model),
                            rawifyParametersAndResults);
                    classBuilder.defs(concreteMemberDelegate);
                     
                }
            } else if (member instanceof Getter
                    || member instanceof Setter
                    || member instanceof Value) {
                TypedDeclaration attr = (TypedDeclaration)member;
                final ProducedTypedReference typedMember = satisfiedType.getTypedMember(attr, null);
                if (needsCompanionDelegate(model, member)) {
                    if (member instanceof Value 
                            || member instanceof Getter) {
                        final JCMethodDecl getterDelegate = makeDelegateToCompanion(iface, 
                                typedMember,
                                PUBLIC | (attr.isDefault() ? 0 : FINAL), 
                                Collections.<TypeParameter>emptyList(), 
                                typedMember.getType(), 
                                Naming.getGetterName(attr), 
                                Collections.<Parameter>emptyList(),
                                Decl.isAncestorLocal(model),
                                rawifyParametersAndResults);
                        classBuilder.defs(getterDelegate);
                    }
                    if (member instanceof Setter) { 
                        final JCMethodDecl setterDelegate = makeDelegateToCompanion(iface, 
                                typedMember,
                                PUBLIC | (attr.isDefault() ? 0 : FINAL), 
                                Collections.<TypeParameter>emptyList(), 
                                typeFact().getVoidDeclaration().getType(), 
                                Naming.getSetterName(attr), 
                                Collections.<Parameter>singletonList(((Setter)member).getParameter()),
                                Decl.isAncestorLocal(model),
                                rawifyParametersAndResults);
                        classBuilder.defs(setterDelegate);
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

    private boolean needsRawification(ProducedType type) {
        final Iterator<TypeParameter> refinedTypeParameters = type.getDeclaration().getTypeParameters().iterator();
        boolean rawifyParametersAndResults = false;
        for (ProducedType typeArg : type.getTypeArgumentList()) {
            final TypeParameter typeParam = refinedTypeParameters.next();
            if (typeParam.getSatisfiedTypes().isEmpty()
                    && (typeFact().isIntersection(typeArg) || typeFact().isUnion(typeArg))) {
                // Use the same hack that makeJavaType() does when handling iterables
                if (typeFact().getNonemptyIterableType(typeFact().getDefiniteType(typeArg)) == null) {
                    rawifyParametersAndResults = true;
                    break;
                }
            }
        }
        return rawifyParametersAndResults;
    }

    private boolean needsCompanionDelegate(final Class model, Declaration member) {
        final boolean mostRefined;
        if (member instanceof Setter) {
            mostRefined = member.equals(((Getter)model.getMember(member.getName(), null)).getSetter());
        } else {
            mostRefined = member.equals(model.getMember(member.getName(), null));
        }
        return mostRefined
                && (member.isDefault() || !member.isFormal());
    }

    /**
     * Generates a method which delegates to the companion instance $Foo$impl
     */
    private JCMethodDecl makeDelegateToCompanion(Interface iface,
            ProducedTypedReference typedMember, final long mods,
            final java.util.List<TypeParameter> typeParameters,
            final ProducedType methodType,
            final String methodName, final java.util.List<Parameter> parameters, boolean ancestorLocal,
            boolean rawifyParametersAndResults) {
        final MethodDefinitionBuilder concreteWrapper = MethodDefinitionBuilder.systemMethod(gen(), ancestorLocal, methodName);
        concreteWrapper.modifiers(mods);
        concreteWrapper.annotations(makeAtIgnore());
        concreteWrapper.annotations(makeAtOverride());
        if (!rawifyParametersAndResults) {
            for (TypeParameter tp : typeParameters) {
                concreteWrapper.typeParameter(tp);
            }
        }
        if (!isVoid(methodType)) {
            concreteWrapper.resultTypeNonWidening(typedMember, typedMember.getType(),
                    rawifyParametersAndResults ? JT_RAW_TP_BOUND : 0);
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
        if (isVoid(methodType)) {
            concreteWrapper.body(gen().make().Exec(expr));
        } else {
            concreteWrapper.body(gen().make().Return(expr));
        }
        final JCMethodDecl build = concreteWrapper.build();
        return build;
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
                        model.getType(), true, BoxingStrategy.BOXED, 
                        satisfiedType, true));
        final String fieldName = getCompanionFieldName(iface);
        classBuilder.init(make().Exec(make().Assign(
                makeSelect("this", fieldName),// TODO Use qualified name for quoting? 
                make().NewClass(null, 
                        null,
                        makeJavaType(satisfiedType, AbstractTransformer.JT_COMPANION | JT_SATISFIES),
                        state,
                        null))));
        
        classBuilder.field(PRIVATE | FINAL, fieldName, 
                makeJavaType(satisfiedType, AbstractTransformer.JT_COMPANION | JT_SATISFIES), null, false);

        classBuilder.defs(makeCompanionAccessor(iface, satisfiedType, true));
        
    }
    
    private List<JCTree> makeCompanionAccessor(Interface iface, ProducedType satisfiedType, boolean forImplementor) {
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
            MethodDefinitionBuilder thisMethod = MethodDefinitionBuilder.method(
                    this, true, true, getCompanionAccessorName(iface));
            if (!forImplementor && Decl.isAncestorLocal(iface)) {
                // For a local interface the return type cannot be a local
                // companion class, because that won't be visible at the 
                // top level, so use Object instead
                thisMethod.resultType(null, make().Type(syms().objectType));
            } else {
                thisMethod.resultType(null, makeJavaType(satisfiedType, JT_COMPANION));
            }
            thisMethod.annotations(forImplementor ? makeAtOverride() : makeAtIgnore());
            thisMethod.modifiers(PUBLIC);
            if (forImplementor) {
                thisMethod.body(make().Return(naming.makeCompanionFieldName(iface)));
            } else {
                thisMethod.noBody();
            }
            return List.<JCTree>of(thisMethod.build());
        }
        return List.<JCTree>nil();
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
        // ...initialize the $this field from a ctor parameter...
        ctor.parameter(0, "$this", makeJavaType(thisType), null);
        ListBuffer<JCStatement> bodyStatements = ListBuffer.<JCStatement>of(
                make().Exec(
                        make().Assign(
                                makeSelect("this", "$this"), 
                                makeUnquotedIdent("$this"))));
        ctor.body(bodyStatements.toList());
    }

    public List<JCStatement> transformRefinementSpecifierStatement(SpecifierStatement op, ClassDefinitionBuilder classBuilder) {
        List<JCStatement> result = List.<JCStatement>nil();
        // Check if this is a shortcut form of formal attribute refinement
        if (op.getRefinement()) {
            Tree.BaseMemberExpression expr = (Tree.BaseMemberExpression)op.getBaseMemberExpression();
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
                methDecl.setSpecifierExpression(op.getSpecifierExpression());
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
                
                // Generate the attribute
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
        boolean useField = Strategy.useField(model);
        String attrName = decl.getIdentifier().getText();

        // Only a non-formal attribute has a corresponding field
        // and if a captured class parameter exists with the same name we skip this part as well
        Parameter parameter = CodegenUtil.findParamForDecl(decl);
        boolean createField = Strategy.createField(parameter, model);
        boolean concrete = Decl.withinInterface(decl)
                && decl.getSpecifierOrInitializerExpression() != null;
        if (concrete || 
                (!Decl.isFormal(decl) 
                        && createField)) {
            JCExpression initialValue = null;
            if (decl.getSpecifierOrInitializerExpression() != null) {
                Value declarationModel = model;
                initialValue = expressionGen().transformExpression(decl.getSpecifierOrInitializerExpression().getExpression(), 
                        CodegenUtil.getBoxingStrategy(declarationModel), 
                        declarationModel.getType());
            }

            int flags = 0;
            ProducedTypedReference typedRef = getTypedReference(model);
            ProducedTypedReference nonWideningTypedRef = nonWideningTypeDecl(typedRef);
            ProducedType nonWideningType = nonWideningType(typedRef, nonWideningTypedRef);
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

        if (useField) {
            classBuilder.defs(makeGetter(decl, false));
            if (Decl.withinInterface(decl)) {
                classBuilder.getCompanionBuilder((Interface)decl.getDeclarationModel().getContainer()).defs(makeGetter(decl, true));
            }
            if (Decl.isMutable(decl)) {
                classBuilder.defs(makeSetter(decl, false));
                if (Decl.withinInterface(decl)) {
                    classBuilder.getCompanionBuilder((Interface)decl.getDeclarationModel().getContainer()).defs(makeSetter(decl, true));
                }
            }
        }
    }

	public List<JCTree> transform(AttributeSetterDefinition decl, boolean forCompanion) {
	    ListBuffer<JCTree> lb = ListBuffer.<JCTree>lb();
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
        if (!Strategy.onlyOnCompanion(decl.getDeclarationModel()) || forCompanion) {
            lb.appendList(builder.build());
        }
        
        return lb.toList();
    }

    public List<JCTree> transform(AttributeGetterDefinition decl, boolean forCompanion) {
        ListBuffer<JCTree> lb = ListBuffer.<JCTree>lb();
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
        if (!Strategy.onlyOnCompanion(decl.getDeclarationModel()) || forCompanion) {
            lb.appendList(builder.build());
        }
        
        return lb.toList();
    }

    private int transformClassDeclFlags(ClassOrInterface cdecl) {
        int result = 0;

        result |= Decl.isShared(cdecl) ? PUBLIC : 0;
        result |= cdecl.isAbstract() && (cdecl instanceof Class) ? ABSTRACT : 0;
        result |= (cdecl instanceof Interface) ? INTERFACE : 0;

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
        result |= (tdecl.isFormal() && !tdecl.isDefault() && !forCompanion) ? ABSTRACT : 0;
        result |= !(tdecl.isFormal() || tdecl.isDefault() || tdecl.getContainer() instanceof Interface) || forCompanion ? FINAL : 0;

        return result;
    }

    private int transformObjectDeclFlags(Value cdecl) {
        int result = 0;

        result |= FINAL;
        result |= !Decl.isAncestorLocal(cdecl) && Decl.isShared(cdecl) ? PUBLIC : 0;

        return result;
    }

    private List<JCTree> makeGetterOrSetter(Tree.AttributeDeclaration decl, boolean forCompanion, AttributeDefinitionBuilder builder, boolean isGetter) {
        at(decl);
        if (forCompanion) {
            if (decl.getSpecifierOrInitializerExpression() != null) {
                builder.getterBlock(make().Block(0, List.<JCStatement>of(make().Return(makeErroneous()))));
            } else {
                JCExpression accessor = naming.makeQualifiedName(
                        naming.makeUnquotedIdent("$this"), 
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
            .isFormal(Decl.isFormal(decl) && !forCompanion)
            .build();
    }
    
    private List<JCTree> makeGetter(Tree.AttributeDeclaration decl, boolean forCompanion) {
        at(decl);
        String attrName = decl.getIdentifier().getText();
        AttributeDefinitionBuilder getter = AttributeDefinitionBuilder
            .getter(this, attrName, decl.getDeclarationModel());
        return makeGetterOrSetter(decl, forCompanion, getter, true);
    }

    private List<JCTree> makeSetter(Tree.AttributeDeclaration decl, boolean forCompanion) {
        at(decl);
        String attrName = decl.getIdentifier().getText();
        AttributeDefinitionBuilder setter = AttributeDefinitionBuilder.setter(this, attrName, decl.getDeclarationModel());
        return makeGetterOrSetter(decl, forCompanion, setter, false);
    }

    public List<JCTree> transformWrappedMethod(Tree.AnyMethod def) {
        final Method model = def.getDeclarationModel();
        noteDecl(model);
        // Generate a wrapper class for the method
        String name = def.getIdentifier().getText();
        ClassDefinitionBuilder builder = ClassDefinitionBuilder.methodWrapper(this, Decl.isAncestorLocal(def), name, Decl.isShared(def));
        builder.body(classGen().transform(def, builder));
        
        // Toplevel method
        if (Strategy.generateMain(def)) {
            // Add a main() method
            builder.body(makeMainForFunction(model));
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

    public List<JCTree> transform(Tree.AnyMethod def, ClassDefinitionBuilder classBuilder) {
        // Transform the method body of the 'inner-most method'
        List<JCStatement> body = transformMethodBody(def);        
        return transform(def, classBuilder, body);
    }

    List<JCTree> transform(Tree.AnyMethod def,
            ClassDefinitionBuilder classBuilder, List<JCStatement> body) {
        final Method model = def.getDeclarationModel();
        final String methodName = Naming.quoteMethodNameIfProperty(model, gen());
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
            boolean actualAndAnnotations = def instanceof MethodDeclaration;
            List<JCStatement> cbody = specifier != null ? transformMplBody(model, body) 
                    : block != null ? statementGen().transform(block).getStatements() 
                    : null;
                    
            boolean transformOverloads = def instanceof MethodDeclaration || block != null;
            int overloadsDelegator = OL_BODY | OL_IMPLEMENTOR | (def instanceof MethodDeclaration ? OL_DELEGATOR : 0);
            
            boolean transformDefaultValues = def instanceof MethodDeclaration || block != null;
            
            List<JCTree> companionDefs = transformMethod(def, model, methodName, 
                        transformMethod,
                        actualAndAnnotations,
                        cbody,
                        transformOverloads,
                        overloadsDelegator,
                        transformDefaultValues,
                        false);
            classBuilder.getCompanionBuilder((TypeDeclaration)model.getContainer()).defs(companionDefs);
        }
        
        List<JCTree> result;
        if (Strategy.onlyOnCompanion(model)) {
            result = List.<JCTree>nil();
        } else {
            // Transform it for the interface/class
            List<JCStatement> cbody = !model.isInterfaceMember() ? transformMplBody(model, body) : null;
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
    private List<JCTree> transformMethod(Tree.AnyMethod def,
            final Method model, final String methodName,
            boolean transformMethod, boolean actualAndAnnotations, List<JCStatement> body, 
            boolean transformOverloads, int overloadsFlags, 
            boolean transformDefaultValues, boolean defaultValuesBody) {
        
        ListBuffer<JCTree> lb = ListBuffer.<JCTree>lb();
        boolean needsRaw = false;
        if (Decl.withinClassOrInterface(model)) {
            final Scope refinedFrom = model.getRefinedDeclaration().getContainer();
            final ProducedType inheritedFrom = ((ClassOrInterface)model.getContainer()).getType().getSupertype((TypeDeclaration)refinedFrom);
            needsRaw = needsRawification(inheritedFrom);
        }
        
        final MethodDefinitionBuilder methodBuilder = MethodDefinitionBuilder.method(
                this, Decl.isAncestorLocal(model), model.isClassOrInterfaceMember(), 
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
                        MethodDefinitionBuilder overloadBuilder = MethodDefinitionBuilder.method(this, Decl.isAncestorLocal(model), model.isClassOrInterfaceMember(),
                                methodName);
                        JCMethodDecl overloadedMethod = makeOverloadsForDefaultedParameter(
                                overloadsFlags, 
                                overloadBuilder, 
                                model, parameterList.getParameters(), parameter).build();
                        lb.prepend(overloadedMethod);
                    }
                    
                    if (transformDefaultValues) {
                        lb.add(makeParamDefaultValueMethod(defaultValuesBody, model, paramList, param));    
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
                methodBuilder.isActual(model.isActual())
                    .modelAnnotations(model.getAnnotations());
            }
            if (CodegenUtil.hasCompilerAnnotation(def, "test")){
                methodBuilder.annotations(List.of(make().Annotation(makeSelect("org", "junit", "Test"), List.<JCTree.JCExpression>nil())));
            }
            lb.prepend(methodBuilder.build());
        }
        return lb.toList();
    }

    /**
     * Constructs all but the outer-most method of a {@code Method} with 
     * multiple parameter lists 
     * @param model The {@code Method} model
     * @param body The inner-most body
     */
    List<JCStatement> transformMplBody(Method model,
            List<JCStatement> body) {
        ProducedType resultType = model.getType();
        for (int index = model.getParameterLists().size() - 1; index >  0; index--) {
            resultType = gen().typeFact().getCallableType(resultType);
            CallableBuilder cb = CallableBuilder.mpl(gen(), resultType, model.getParameterLists().get(index), body);
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
                if (initializingParameter.getContainer() instanceof Method) {
                    // We're initializing a local method, which will have a 
                    // class wrapper of the same name as the param, so 
                    // the param gets renamed
                    initialValue = makeUnquotedIdent(Naming.getAliasedParameterName(initializingParameter));
                } else {
                    initialValue = makeUnquotedIdent(initializingParameter.getName());
                }
            } else {
                // The field isn't initialized by a parameter, but later in the block
                initialValue = makeNull();
            }
            current().field(mods, model.getName(), makeJavaType(typeFact().getCallableType(model.getType())), initialValue, false);
            ListBuffer<JCExpression> args = ListBuffer.<JCExpression>lb();
            for (Parameter param : model.getParameterLists().get(0).getParameters()) {
                args.append(makeQuotedIdent(param.getName()));
            }
            JCExpression call = make().Apply(null, makeSelect(
                    model.getName(), "$call"), args.toList());
            call = gen().expressionGen().applyErasureAndBoxing(call, model.getType(), 
                    true, BoxingStrategy.UNBOXED, model.getType());
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
                final JCBinary cond = make().Binary(JCTree.EQ, makeQuotedIdent(model.getName()), makeNull());
                final JCStatement throw_ = make().Throw(make().NewClass(null, null, 
                        make().Type(syms().ceylonUninitializedMethodErrorType), 
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
                boolean prevNoExpressionlessReturn = statementGen().noExpressionlessReturn;
                try {
                    statementGen().noExpressionlessReturn = Decl.isMpl(model);
                
                    final Block block = ((Tree.MethodDefinition) def).getBlock();
                    body = statementGen().transform(block).getStatements();
                    // We void methods need to have their Callables return null
                    // so adjust here.
                    if (Decl.isMpl(model) &&
                            !block.getDefinitelyReturns()) {
                        if (Decl.isUnboxedVoid(model)) {
                            body = body.append(make().Return(makeNull()));
                        } else {
                            body = body.append(make().Return(makeErroneous(block, "non-void method doesn't definitely return")));
                        }
                    }
                } finally {
                    statementGen().noExpressionlessReturn = prevNoExpressionlessReturn;
                }
            }
        } else if (def instanceof MethodDeclaration
                && ((MethodDeclaration) def).getSpecifierExpression() != null) {
            body = transformSpecifiedMethodBody((MethodDeclaration)def, ((MethodDeclaration) def).getSpecifierExpression());
        }
        return body;
    }

    List<JCStatement> transformSpecifiedMethodBody(Tree.MethodDeclaration  def, SpecifierExpression specifierExpression) {
        final Method model = def.getDeclarationModel();
        List<JCStatement> body;
        MethodDeclaration methodDecl = (MethodDeclaration)def;
        JCExpression bodyExpr;
        Tree.Term term = null;
        if (specifierExpression != null
                && specifierExpression.getExpression() != null) {
            term = specifierExpression.getExpression().getTerm();
        }
        if (term instanceof Tree.FunctionArgument) {
            // Method specified with lambda: Don't bother generating a 
            // Callable, just transform the expr to use as the method body.
            Tree.FunctionArgument fa = (Tree.FunctionArgument)term;
            ProducedType resultType = model.getType();
            final java.util.List<com.redhat.ceylon.compiler.typechecker.tree.Tree.Parameter> lambdaParams = fa.getParameterLists().get(0).getParameters();
            final java.util.List<com.redhat.ceylon.compiler.typechecker.tree.Tree.Parameter> defParams = def.getParameterLists().get(0).getParameters();
            for (int ii = 0; ii < lambdaParams.size(); ii++) {
                gen().addVariableSubst(lambdaParams.get(ii).getIdentifier().getText(), 
                        defParams.get(ii).getIdentifier().getText());
            }
            bodyExpr = gen().expressionGen().transformExpression(fa.getExpression(), BoxingStrategy.UNBOXED, null);
            bodyExpr = gen().expressionGen().applyErasureAndBoxing(bodyExpr, resultType, 
                    true, 
                    model.getUnboxed() ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED, 
                            resultType);
            for (int ii = 0; ii < lambdaParams.size(); ii++) {
                gen().removeVariableSubst(lambdaParams.get(ii).getIdentifier().getText(), 
                        null);
            }
        } else {
            InvocationBuilder specifierBuilder = InvocationBuilder.forSpecifierInvocation(gen(), specifierExpression, methodDecl.getDeclarationModel());
            bodyExpr = specifierBuilder.build();
        }
        if (Decl.isUnboxedVoid(model)) {
            body = List.<JCStatement>of(make().Exec(bodyExpr));
        } else {
            body = List.<JCStatement>of(make().Return(bodyExpr));
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
                parameters, currentParam.getDeclarationModel());
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
        overloadBuilder.annotations(makeAtIgnore());
        
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
            if ((flags & OL_DELEGATOR) != 0) {
                methName = makeSelect(makeUnquotedIdent("$this"), Naming.quoteMethodNameIfProperty((Method)model, gen()));
            } else {
                methName = makeQuotedIdent(Naming.quoteMethodNameIfProperty((Method)model, gen()));
            }
            overloadBuilder.resultType((Method)model, 0);
        } else if (model instanceof Class) {
            overloadBuilder.modifiers(transformOverloadCtorFlags((Class)model));
            methName = makeUnquotedIdent("this");
        } else {
            throw new RuntimeException();
        }
        
        // TODO MPL
        if (model instanceof Method) {
            copyTypeParameters((Method)model, overloadBuilder);
        }

        // TODO Some simple default expressions (e.g. literals, null and 
        // base expressions it might be worth inlining the expression rather 
        // than calling the default value method.
        // TODO This really belongs in the invocation builder
        
        ListBuffer<JCExpression> args = ListBuffer.<JCExpression>lb();
        ListBuffer<JCStatement> vars = ListBuffer.<JCStatement>lb();
        
        final String companionInstanceName = tempName("$impl$");
        if (model instanceof Class
                && !Strategy.defaultParameterMethodStatic(model)) {
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
                    dpmQualifier = makeQuotedIdent(companionInstanceName);
                }
                JCExpression defaultValueMethodName = naming.makeDefaultedParamMethod(dpmQualifier, param2);
                
                String varName = tempName("$"+param2.getName()+"$");
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
                args.add(makeUnquotedIdent(varName));
            } else {
                overloadBuilder.parameter(param2, 0);
                args.add(makeQuotedIdent(Naming.getAliasedParameterName(param2)));
            }
        }
        
        // TODO Type args on method call
        if ((flags & OL_BODY) != 0) {
            JCExpression invocation = make().Apply(List.<JCExpression>nil(),
                    methName, args.toList());
               
            if (isVoid(model)) {
                vars.append(make().Exec(invocation));
                invocation = make().LetExpr(vars.toList(), makeNull());
                overloadBuilder.body(make().Exec(invocation));
            } else {
                invocation = make().LetExpr(vars.toList(), invocation);
                overloadBuilder.body(make().Return(invocation));
            }
        } else {
            overloadBuilder.noBody();
        }
        
        return overloadBuilder;
    }



    /**
     * Creates a (possibly abstract) method for retrieving the value for a 
     * defaulted parameter
     */
    private JCMethodDecl makeParamDefaultValueMethod(boolean noBody, Declaration container, 
            Tree.ParameterList params, Tree.Parameter currentParam) {
        at(currentParam);
        Parameter parameter = currentParam.getDeclarationModel();
        String name = Naming.getDefaultedParamMethodName(container, parameter );
        MethodDefinitionBuilder methodBuilder = MethodDefinitionBuilder.method(this, Decl.isAncestorLocal(container), true, name);
        methodBuilder.annotations(makeAtIgnore());
        int modifiers = 0;
        if (noBody) {
            modifiers |= PUBLIC | ABSTRACT;
        } else if (!(container instanceof Class 
                && Strategy.defaultParameterMethodStatic(container))) {
            // initializers can override parameter defaults
            modifiers |= FINAL;
        }
        if (container.isShared()) {
            modifiers |= PUBLIC;
        } else if (!container.isToplevel()
                && !noBody){
            modifiers |= PRIVATE;
        }
        if (Strategy.defaultParameterMethodStatic(container)) {
            modifiers |= STATIC;
        }
        methodBuilder.modifiers(modifiers);
        
        if (container instanceof Method) {
            copyTypeParameters((Method)container, methodBuilder);
        } else if (Decl.isToplevel(container)
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

        return methodBuilder.build();
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
        noteDecl(model);
        
        String name = model.getName();
        ClassDefinitionBuilder objectClassBuilder = ClassDefinitionBuilder.object(
                this, Decl.isAncestorLocal(model), name, null);
        
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
            objectClassBuilder.body(builder.build());
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
            result = result.append(makeLocalIdentityInstance(name, false));
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
    private JCMethodDecl makeMainForClass(final ClassOrInterface model) {
        at(null);
        JCExpression nameId = makeQuotedFQIdent(model.getQualifiedNameString());
        JCNewClass expr = make().NewClass(null, null, nameId, List.<JCTree.JCExpression>nil(), null);
        return makeMainMethod(model, expr);
    }
    
    /**
     * Makes a {@code main()} method which calls the given top-level method
     * @param method
     */
    private JCMethodDecl makeMainForFunction(Method method) {
        at(null);
        String path = method.getQualifiedNameString();
        JCExpression qualifiedName = makeSelect(makeQuotedFQIdent(path), Naming.quoteMethodName(method));
        JCMethodDecl mainMethod = makeMainMethod(method, make().Apply(null, qualifiedName, List.<JCTree.JCExpression>nil()));
        return mainMethod;
    }
    
    /** 
     * Makes a {@code main()} method which calls the given callee 
     * (a no-args method or class)
     * @param decl
     * @param callee
     */
    private JCMethodDecl makeMainMethod(Declaration decl, JCExpression callee) {
        // Add a main() method
        MethodDefinitionBuilder methbuilder = MethodDefinitionBuilder
                .main(this, Decl.isAncestorLocal(decl))
                .annotations(makeAtIgnore());
        // Add call to process.setupArguments
        JCExpression argsId = makeUnquotedIdent("args");
        JCMethodInvocation processExpr = make().Apply(null, makeFQIdent("ceylon", "language", "process", "getProcess"), List.<JCTree.JCExpression>nil());
        methbuilder.body(make().Exec(make().Apply(null, makeSelect(processExpr, "setupArguments"), List.<JCTree.JCExpression>of(argsId))));
        // Add call to toplevel method
        methbuilder.body(make().Exec(callee));
        return methbuilder.build();
    }
    
    void copyTypeParameters(Tree.AnyMethod def, MethodDefinitionBuilder methodBuilder) {
        copyTypeParameters(def.getDeclarationModel(), methodBuilder);
    }
    
    void copyTypeParameters(Functional def, MethodDefinitionBuilder methodBuilder) {
        if (def.getTypeParameters() != null) {
            for (TypeParameter t : def.getTypeParameters()) {
                methodBuilder.typeParameter(t);
            }
        }
    }
    
    void copyTypeParameters(Tree.AnyClass def, MethodDefinitionBuilder methodBuilder) {
        copyTypeParameters(def.getDeclarationModel(), methodBuilder);
    }
}
