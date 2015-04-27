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

import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.antlr.runtime.Token;

import com.redhat.ceylon.compiler.java.codegen.Naming.DeclNameFlag;
import com.redhat.ceylon.compiler.java.codegen.Naming.Substitution;
import com.redhat.ceylon.compiler.java.codegen.Naming.Suffix;
import com.redhat.ceylon.compiler.java.codegen.Naming.SyntheticName;
import com.redhat.ceylon.compiler.java.codegen.Naming.Unfix;
import com.redhat.ceylon.compiler.java.codegen.StatementTransformer.DeferredSpecification;
import com.redhat.ceylon.compiler.java.codegen.recovery.Drop;
import com.redhat.ceylon.compiler.java.codegen.recovery.Errors;
import com.redhat.ceylon.compiler.java.codegen.recovery.HasErrorException;
import com.redhat.ceylon.compiler.java.codegen.recovery.ThrowerMethod;
import com.redhat.ceylon.compiler.java.codegen.recovery.TransformationPlan;
import com.redhat.ceylon.compiler.loader.model.AnnotationTarget;
import com.redhat.ceylon.compiler.loader.model.LazyInterface;
import com.redhat.ceylon.compiler.loader.model.OutputElement;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassAlias;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Constructor;
import com.redhat.ceylon.compiler.typechecker.model.ControlBlock;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Generic;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
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
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.UnknownType;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AnnotationList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeGetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeSetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LazySpecifierExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MemberOrTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SequencedArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierOrInitializerExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierStatement;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCReturn;
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
        
        naming.clearSubstitutions(model);
        final String javaClassName;
        String ceylonClassName = def.getIdentifier().getText();
        if (def instanceof Tree.AnyInterface) {
            javaClassName = naming.makeTypeDeclarationName(model, QUALIFIED).replaceFirst(".*\\.", "");
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
                .klass(this, javaClassName, ceylonClassName, Decl.isLocal(model))
                .forDefinition(model);
        
        // Very special case for Anything
        if ("ceylon.language::Anything".equals(model.getQualifiedNameString())) {
            classBuilder.extending(model.getType(), null, false);
        }
        
        if (def instanceof Tree.AnyClass) {
            classBuilder.getInitBuilder().modifiers(transformConstructorDeclFlags(model));
            Tree.AnyClass classDef = (Tree.AnyClass)def;
            Class cls = classDef.getDeclarationModel();
            // Member classes need a instantiator method
            boolean generateInstantiator = Strategy.generateInstantiator(cls);
            if (!cls.hasConstructors()) {
                classBuilder.getInitBuilder().userAnnotations(expressionGen().transformAnnotations(false, OutputElement.CONSTRUCTOR, def));
            }
            if(generateInstantiator
                    && !cls.hasConstructors()){
                classBuilder.getInitBuilder().modifiers(PROTECTED);
                generateInstantiators(classBuilder, cls, null, instantiatorDeclCb, instantiatorImplCb, classDef, classDef.getParameterList());
            }
            
            classBuilder.annotations(expressionGen().transformAnnotations(true, OutputElement.TYPE, def));
            if(def instanceof Tree.ClassDefinition){
                transformClass((Tree.ClassDefinition)def, cls, classBuilder, classDef.getParameterList(), generateInstantiator, instantiatorDeclCb, instantiatorImplCb);
            }else{
                // class alias
                classBuilder.getInitBuilder().modifiers(PRIVATE);
                transformClassAlias((Tree.ClassDeclaration)def, classBuilder);
            }
            
            addMissingUnrefinedMembers(def, cls, classBuilder);
        }
        
        if (def instanceof Tree.AnyInterface) {
            classBuilder.annotations(expressionGen().transformAnnotations(true, OutputElement.TYPE, def));
            if(def instanceof Tree.InterfaceDefinition){
                transformInterface(def, (Interface)model, classBuilder);
            }else{
                // interface alias
                classBuilder.annotations(makeAtAlias(model.getExtendedType(), null));
                classBuilder.isAlias(true);
            }
            classBuilder.isDynamic(model.isDynamic());
        }

        // make sure we set the container in case we move it out
        addAtContainer(classBuilder, model);
        
        // Transform the class/interface members
        List<JCStatement> childDefs = visitClassOrInterfaceDefinition(def, classBuilder);
        // everything else is synthetic
        at(null);
        // If it's a Class without initializer parameters...
        if (Strategy.generateMain(def)) {
            // ... then add a main() method
            classBuilder.method(makeMainForClass(model));
        }
        classBuilder
            .modelAnnotations(model.getAnnotations())
            .modifiers(transformClassDeclFlags(model))
            .satisfies(model.getSatisfiedTypes())
            .caseTypes(model.getCaseTypes(), model.getSelfType())
            .defs((List)childDefs);
        
        // aliases don't need a $getType method
        if(!model.isAlias()){
            // only classes get a $getType method
            if(model instanceof Class)
                classBuilder.addGetTypeMethod(model.getType());
            if(supportsReifiedAlias(model))
                classBuilder.reifiedAlias(model.getType());
        }
        // reset position before initializer constructor is generated. 
        at(def);
        List<JCTree> result;
        if (Decl.isAnnotationClass(def)) {
            ListBuffer<JCTree> trees = ListBuffer.lb();
            trees.addAll(transformAnnotationClass((Tree.AnyClass)def));
            transformAnnotationClassConstructor((Tree.AnyClass)def, classBuilder);
            trees.addAll(classBuilder.build());
            result = trees.toList();
        } else {
            result = classBuilder.build();
        }
        
        return result;
    }

    /** 
     * Recover from members not being refined in the class hierarchy 
     * by generating a stub method that throws.
     */
    private void addMissingUnrefinedMembers(
            Node def,
            Class classModel,
            ClassDefinitionBuilder classBuilder) {
        
        for (ProducedReference unrefined : classModel.getUnimplementedFormals()) {
            Declaration formalMember = unrefined.getDeclaration();//classModel.getMember(memberName, null, false);
            String errorMessage  = "formal member '"+formalMember.getName()+"' of '"+((TypeDeclaration)formalMember.getContainer()).getName()+"' not implemented in class hierarchy";
            java.util.List<ProducedType> params = new java.util.ArrayList<ProducedType>();
            if (formalMember instanceof Generic) {
                for (TypeParameter tp: ((Generic) formalMember).getTypeParameters()) {
                    params.add(tp.getType());
                }
            }
            if (formalMember instanceof Value) {
                addRefinedThrowerAttribute(classBuilder, errorMessage, classModel,
                        (Value)formalMember);
            } else if (formalMember instanceof Method) {
                addRefinedThrowerMethod(classBuilder, errorMessage, classModel,
                        (Method)formalMember);
            } else if (formalMember instanceof Class
                    && formalMember.isClassMember()) {
                addRefinedThrowerInstantiatorMethod(classBuilder, errorMessage, classModel,
                        (Class)formalMember, unrefined);
            }
            // formal member class of interface handled in
            // makeDelegateToCompanion()
        }
    
    }

    private void addRefinedThrowerInstantiatorMethod(ClassDefinitionBuilder classBuilder,
            String message, ClassOrInterface classModel, Class formalClass, ProducedReference unrefined) {
        MethodDefinitionBuilder mdb = MethodDefinitionBuilder.systemMethod(this, 
                naming.getInstantiatorMethodName(formalClass));
        mdb.modifiers(transformClassDeclFlags(formalClass) &~ABSTRACT);
        for (TypeParameter tp: formalClass.getTypeParameters()) {
            mdb.typeParameter(tp);
            mdb.reifiedTypeParameter(tp);
        }
        for (Parameter formalP : formalClass.getParameterList().getParameters()) {
            ParameterDefinitionBuilder pdb = ParameterDefinitionBuilder.systemParameter(this, formalP.getName());
            pdb.sequenced(formalP.isSequenced());
            pdb.defaulted(formalP.isDefaulted());
            pdb.type(makeJavaType(unrefined.getTypedParameter(formalP).getType()), null);
            mdb.parameter(pdb);
        }
        mdb.resultType(makeJavaType(unrefined.getType()), null);
        mdb.body(makeThrowUnresolvedCompilationError(message));
        classBuilder.method(mdb);
    }

    private Iterable<java.util.List<Parameter>> overloads(Functional f) {
        java.util.List<java.util.List<Parameter>> result = new ArrayList<java.util.List<Parameter>>();
        result.add(f.getParameterLists().get(0).getParameters());
        int ii = 0;
        for (Parameter p : f.getParameterLists().get(0).getParameters()) {
            if (p.isDefaulted()
                    || (p.isSequenced() && !p.isAtLeastOne())) {
                result.add(f.getParameterLists().get(0).getParameters().subList(0, ii));
            }
            ii++;
        }
        return result;
    }
    
    private void addRefinedThrowerMethod(ClassDefinitionBuilder classBuilder,
            String error, ClassOrInterface classModel,
            Method formalMethod) {
        Method refined = refineMethod(classModel, 
                classModel.getType().getTypedMember(formalMethod, Collections.<ProducedType>emptyList()),
                classModel, formalMethod, classModel.getUnit());
        // TODO When the method in inherited from an interface and is defaulted 
        // we need to generate a DPM as well, otherwise the class is missing
        // the DPM and javac barfs.
        for (java.util.List<Parameter> parameterList : overloads(refined)) {
            MethodDefinitionBuilder mdb = MethodDefinitionBuilder.method(this, refined);
            mdb.isOverride(true);
            mdb.modifiers(transformMethodDeclFlags(refined));
            for (TypeParameter tp: formalMethod.getTypeParameters()) {
                mdb.typeParameter(tp);
                mdb.reifiedTypeParameter(tp);
            }
            for (Parameter param : parameterList) {
                mdb.parameter(param, null, 0, false);
            }
            mdb.resultType(refined, 0);
            mdb.body(makeThrowUnresolvedCompilationError(error));
            classBuilder.method(mdb);
        }
    }

    private Class refineClass(
            Scope container,
            ProducedReference pr,
            ClassOrInterface classModel, 
            Class formalClass,
            Unit unit) {
        Class refined = new Class();
        refined.setActual(true);
        refined.setShared(formalClass.isShared());
        refined.setContainer(container);
        refined.setExtendedType(formalClass.getType());
        refined.setDeprecated(formalClass.isDeprecated());
        refined.setName(formalClass.getName());
        refined.setRefinedDeclaration(formalClass.getRefinedDeclaration());
        refined.setScope(container);
        //refined.setType(pr.getType());
        refined.setUnit(unit);
        for (ParameterList formalPl : formalClass.getParameterLists()) {
            ParameterList refinedPl = new ParameterList();
            for (Parameter formalP : formalPl.getParameters()){
                Parameter refinedP = new Parameter();
                refinedP.setAtLeastOne(formalP.isAtLeastOne());
                refinedP.setDeclaration(refined);
                refinedP.setDefaulted(formalP.isDefaulted());
                refinedP.setDeclaredAnything(formalP.isDeclaredAnything());
                refinedP.setHidden(formalP.isHidden());
                refinedP.setSequenced(formalP.isSequenced());
                refinedP.setName(formalP.getName());
                final ProducedTypedReference typedParameter = pr.getTypedParameter(formalP);
                MethodOrValue paramModel;
                if (formalP.getModel() instanceof Value) {
                    Value paramValueModel = refineValue((Value)formalP.getModel(), typedParameter, refined, classModel.getUnit());
                    paramValueModel.setInitializerParameter(refinedP);
                    paramModel = paramValueModel;
                } else {
                    Method paramFunctionModel = refineMethod(refined, typedParameter, classModel, (Method)formalP.getModel(), unit);
                    paramFunctionModel.setInitializerParameter(refinedP);
                    paramModel = paramFunctionModel; 
                }
                refinedP.setModel(paramModel);
                refinedPl.getParameters().add(refinedP);
            }
            refined.addParameterList(refinedPl);
        }
        return refined;
    }
    
    private Method refineMethod(
            Scope container,
            ProducedTypedReference pr,
            ClassOrInterface classModel, 
            Method formalMethod,
            Unit unit) {
        Method refined = new Method();
        refined.setActual(true);
        refined.setShared(formalMethod.isShared());
        refined.setContainer(container);
        refined.setDefault(true);// in case there are subclasses
        refined.setDeferred(false);
        refined.setDeprecated(formalMethod.isDeprecated());
        refined.setName(formalMethod.getName());
        refined.setRefinedDeclaration(formalMethod.getRefinedDeclaration());
        refined.setScope(container);
        refined.setType(pr.getType());
        refined.setUnit(unit);
        refined.setUnboxed(formalMethod.getUnboxed());
        refined.setUntrustedType(formalMethod.getUntrustedType());
        refined.setTypeErased(formalMethod.getTypeErased());
        ArrayList<TypeParameter> refinedTp = new ArrayList<TypeParameter>();;
        for (TypeParameter formalTp : formalMethod.getTypeParameters()) {
            refinedTp.add(formalTp);
        }
        refined.setTypeParameters(refinedTp);
        for (ParameterList formalPl : formalMethod.getParameterLists()) {
            ParameterList refinedPl = new ParameterList();
            for (Parameter formalP : formalPl.getParameters()){
                Parameter refinedP = new Parameter();
                refinedP.setAtLeastOne(formalP.isAtLeastOne());
                refinedP.setDeclaration(refined);
                refinedP.setDefaulted(formalP.isDefaulted());
                refinedP.setDeclaredAnything(formalP.isDeclaredAnything());
                refinedP.setHidden(formalP.isHidden());
                refinedP.setSequenced(formalP.isSequenced());
                refinedP.setName(formalP.getName());
                final ProducedTypedReference typedParameter = pr.getTypedParameter(formalP);
                MethodOrValue paramModel;
                if (formalP.getModel() instanceof Value) {
                    Value paramValueModel = refineValue((Value)formalP.getModel(), typedParameter, refined, classModel.getUnit());
                    paramValueModel.setInitializerParameter(refinedP);
                    paramModel = paramValueModel;
                } else {
                    Method paramFunctionModel = refineMethod(refined, typedParameter, classModel, (Method)formalP.getModel(), unit);
                    paramFunctionModel.setInitializerParameter(refinedP);
                    paramModel = paramFunctionModel; 
                }
                refinedP.setModel(paramModel);
                refinedPl.getParameters().add(refinedP);
            }
            refined.addParameterList(refinedPl);
        }
        return refined;
    }

    /**
     * Adds a getter (and possibly a setter) to {@code classBuilder} 
     * which throws
     * @param classBuilder The class builder to add the method to
     * @param error The message
     * @param classModel The class which doesn't refine {@code formalAttribute}
     * @param formalAttribute The formal attribute that hasn't been refined in {@code classModel}
     */
    private void addRefinedThrowerAttribute(
            ClassDefinitionBuilder classBuilder, String error,
            ClassOrInterface classModel, Value formalAttribute) {
        Value refined = refineValue(formalAttribute, formalAttribute.getProducedTypedReference(null, null), classModel, classModel.getUnit());
        AttributeDefinitionBuilder getterBuilder = AttributeDefinitionBuilder.getter(this, refined.getName(), refined);
        getterBuilder.skipField();
        getterBuilder.modifiers(transformAttributeGetSetDeclFlags(refined, false));
        getterBuilder.getterBlock(make().Block(0, List.<JCStatement>of(makeThrowUnresolvedCompilationError(error))));
        classBuilder.attribute(getterBuilder);
        if (formalAttribute.isVariable()) {
            AttributeDefinitionBuilder setterBuilder = AttributeDefinitionBuilder.setter(this, refined.getName(), refined);
            setterBuilder.skipField();
            setterBuilder.modifiers(transformAttributeGetSetDeclFlags(refined, false));
            setterBuilder.setterBlock(make().Block(0, List.<JCStatement>of(makeThrowUnresolvedCompilationError(error))));
            classBuilder.attribute(setterBuilder);
        }
    }

    private Value refineValue(Value formalAttribute,
            ProducedTypedReference producedValue,
            Scope container, 
            Unit unit) {
        Value refined = new Value();
        refined.setActual(true);
        refined.setContainer(container);
        refined.setName(formalAttribute.getName());
        refined.setRefinedDeclaration(formalAttribute.getRefinedDeclaration());
        refined.setScope(container);
        refined.setVariable(formalAttribute.isVariable());
        
        refined.setShared(formalAttribute.isShared());
        refined.setTransient(formalAttribute.isTransient());
        refined.setType(producedValue.getType());// TODO
        refined.setTypeErased(formalAttribute.getTypeErased());
        refined.setUnboxed(formalAttribute.getUnboxed());
        refined.setUntrustedType(formalAttribute.getUntrustedType());
        refined.setUnit(unit);
        return refined;
    }

    

    private void transformClassAlias(final Tree.ClassDeclaration def,
            ClassDefinitionBuilder classBuilder) {
        ClassAlias model = (ClassAlias)def.getDeclarationModel();
        ProducedType aliasedClass = model.getExtendedType();
        TypeDeclaration classOrCtor = def.getClassSpecifier().getType().getDeclarationModel();
        while (classOrCtor instanceof ClassAlias) {
            classOrCtor = ((ClassAlias)classOrCtor).getConstructor();
        }
        classBuilder.annotations(makeAtAlias(aliasedClass, classOrCtor instanceof Constructor ? (Constructor)classOrCtor : null));
        classBuilder.isAlias(true);
        MethodDefinitionBuilder instantiator = transformClassAliasInstantiator(
                def, model, aliasedClass);
        
        ClassDefinitionBuilder cbInstantiator = null;
        switch (Strategy.defaultParameterMethodOwner(model)) {
        case STATIC:
            cbInstantiator = classBuilder;
            break;
        case OUTER:
            cbInstantiator =  classBuilder.getContainingClassBuilder();
            break;
        case OUTER_COMPANION:
            cbInstantiator = classBuilder.getContainingClassBuilder().getCompanionBuilder(Decl.getClassOrInterfaceContainer(model, true));
            break;
        default:
            throw BugException.unhandledEnumCase(Strategy.defaultParameterMethodOwner(model));
        }
        
        cbInstantiator.method(instantiator);
    }

    /**
     * Builds the instantiator method for a class aliases. In 1.0 you can't
     * actually invoke these, they exist just so there's somewhere to put the
     * class alias annotations. In 1.2 (when we fix #1295) the
     * instantiators will actually do something.
     */
    private MethodDefinitionBuilder transformClassAliasInstantiator(
            final Tree.AnyClass def, Class model, ProducedType aliasedClass) {
        MethodDefinitionBuilder instantiator = MethodDefinitionBuilder.systemMethod(this, naming.getAliasInstantiatorMethodName(model));
        int f = 0;
        if (Strategy.defaultParameterMethodStatic(def.getDeclarationModel())) {
            f = STATIC;
        }
        instantiator.modifiers((transformClassDeclFlags(model) & ~FINAL)| f);
        for (TypeParameter tp : typeParametersOfAllContainers(model, true)) {
            instantiator.typeParameter(tp);
        }
        
        instantiator.resultType(null, makeJavaType(aliasedClass));
        instantiator.annotationFlags(Annotations.MODEL_AND_USER | Annotations.IGNORE);
        // We need to reify the parameters, at least so they have reified annotations
        
        
        for (final Tree.Parameter param : def.getParameterList().getParameters()) {
            // Overloaded instantiators
            Parameter paramModel = param.getParameterModel();
            at(param);
            transformParameter(instantiator, param, paramModel, Decl.getMemberDeclaration(def, param));
        }
        instantiator.body(make().Throw(makeNewClass(makeJavaType(typeFact().getExceptionDeclaration().getType(), JT_CLASS_NEW))));
        return instantiator;
    }

    /**
     * Generates a constructor for an annotation class which takes the 
     * annotation type as parameter.
     * @param classBuilder
     */
    private void transformAnnotationClassConstructor(
            Tree.AnyClass def,
            ClassDefinitionBuilder classBuilder) {
        Class klass = def.getDeclarationModel();
        MethodDefinitionBuilder annoCtor = classBuilder.addConstructor();
        annoCtor.ignoreModelAnnotations();
        // constructors are never final
        annoCtor.modifiers(transformClassDeclFlags(klass) & ~FINAL);
        ParameterDefinitionBuilder pdb = ParameterDefinitionBuilder.systemParameter(this, "anno");
        pdb.type(makeJavaType(klass.getType(), JT_ANNOTATION), null);
        annoCtor.parameter(pdb);
        
        // It's up to the caller to invoke value() on the Java annotation for a sequenced
        // annotation
        
        ListBuffer<JCExpression> args = ListBuffer.lb();
        for (Tree.Parameter parameter : def.getParameterList().getParameters()) {
            at(parameter);
            Parameter parameterModel = parameter.getParameterModel();
            JCExpression annoAttr = make().Apply(null, naming.makeQuotedQualIdent(naming.makeUnquotedIdent("anno"),
                    parameter.getParameterModel().getName()),
                    List.<JCExpression>nil());
            ProducedType parameterType = parameterModel.getType();
            JCExpression argExpr;
            if (typeFact().isIterableType(parameterType)
                    && !isCeylonString(parameterType)) {
                // Convert from array to Sequential
                ProducedType iteratedType = typeFact().getIteratedType(parameterType);
                if (isCeylonBasicType(iteratedType)) {
                    argExpr = utilInvocation().sequentialWrapperBoxed(annoAttr);
                } else if (Decl.isAnnotationClass(iteratedType.getDeclaration())) {
                    // Can't use Util.sequentialAnnotation becase we need to 'box'
                    // the Java annotations in their Ceylon annotation class
                    argExpr = make().Apply(null, naming.makeUnquotedIdent(naming.getAnnotationSequenceMethodName()), List.of(annoAttr));
                    ListBuffer<JCStatement> stmts = ListBuffer.lb();
                    SyntheticName array = naming.synthetic(Unfix.$array$);
                    SyntheticName sb = naming.synthetic(Unfix.$sb$);
                    SyntheticName index = naming.synthetic(Unfix.$index$);
                    SyntheticName element = naming.synthetic(Unfix.$element$);
                    stmts.append(makeVar(FINAL, sb, 
                            make().TypeArray(make().Type(syms().objectType)),
                            make().NewArray(make().Type(syms().objectType), List.of(naming.makeQualIdent(array.makeIdent(), "length")), null)));
                    stmts.append(makeVar(index, 
                            make().Type(syms().intType),
                            make().Literal(0)));
                    stmts.append(make().ForeachLoop(
                            makeVar(element, makeJavaType(iteratedType, JT_ANNOTATION), null), 
                            array.makeIdent(), 
                            make().Exec(make().Assign(
                                    make().Indexed(sb.makeIdent(), 
                                            make().Unary(JCTree.POSTINC, index.makeIdent())), 
                                    instantiateAnnotationClass(iteratedType, element.makeIdent())))));
                    stmts.append(make().Return(
                            make().NewClass(null,
                                    null,
                                    make().QualIdent(syms().ceylonTupleType.tsym),
                                    List.of(makeReifiedTypeArgument(iteratedType),
                                            sb.makeIdent(),
                                            makeEmpty(),
                                            make().Literal(false)), 
                                    null)));
                    classBuilder.method(
                            MethodDefinitionBuilder.systemMethod(this, naming.getAnnotationSequenceMethodName())
                                .ignoreModelAnnotations()
                                .modifiers(PRIVATE | STATIC)
                                .resultType(null, makeJavaType(typeFact().getSequentialType(iteratedType)))
                                .parameter(ParameterDefinitionBuilder.systemParameter(this, array.getName())
                                        .type(make().TypeArray(makeJavaType(iteratedType, JT_ANNOTATION)), null))
                                .body(stmts.toList()));
                } else if (isCeylonMetamodelDeclaration(iteratedType)) {
                    argExpr = makeMetamodelInvocation("parseMetamodelReferences", 
                            List.<JCExpression>of(makeReifiedTypeArgument(iteratedType), annoAttr), 
                            List.<JCExpression>of(makeJavaType(iteratedType, JT_TYPE_ARGUMENT)));
                } else if (Decl.isEnumeratedTypeWithAnonCases(iteratedType)) {
                    argExpr = makeMetamodelInvocation("parseEnumerationReferences", 
                            List.<JCExpression>of(makeReifiedTypeArgument(iteratedType), annoAttr), 
                            List.<JCExpression>of(makeJavaType(iteratedType, JT_TYPE_ARGUMENT)));
                } else {
                    argExpr = makeErroneous(parameter, "compiler bug");
                }
            } else if (Decl.isAnnotationClass(parameterType.getDeclaration())) {
                argExpr = instantiateAnnotationClass(parameterType, annoAttr);
            } else if (isCeylonMetamodelDeclaration(parameterType)) {
                argExpr = makeMetamodelInvocation("parseMetamodelReference", 
                            List.<JCExpression>of(annoAttr), 
                            List.<JCExpression>of(makeJavaType(parameterType, JT_TYPE_ARGUMENT)));
            } else if (Decl.isEnumeratedTypeWithAnonCases(parameterType)) {
                argExpr = makeMetamodelInvocation("parseEnumerationReference", 
                        List.<JCExpression>of(annoAttr), 
                        null);
            } else {
                argExpr = annoAttr;
                argExpr = expressionGen().applyErasureAndBoxing(annoAttr, parameterType.withoutUnderlyingType(), false, BoxingStrategy.UNBOXED, parameterType);
            }
            args.add(argExpr);
        }
        annoCtor.body(at(def).Exec(
                make().Apply(null,  naming.makeThis(), args.toList())));
    }

    private JCNewClass instantiateAnnotationClass(
            ProducedType annotationClass,
            JCExpression javaAnnotationInstance) {
        return make().NewClass(null, null, makeJavaType(annotationClass), 
                List.<JCExpression>of(javaAnnotationInstance), null);
    }

    /**
     * Transforms an annotation class into a Java annotation type.
     * <pre>
     * annotation class Foo(String s, Integer i=1) {}
     * </pre>
     * is transformed into
     * <pre>
     * @Retention(RetentionPolicy.RUNTIME)
     * @interface Foo$annotation$ {
     *     String s();
     *     long i() default 1;
     * }
     * </pre>
     * If the annotation class is a subtype of SequencedAnnotation a wrapper
     * annotation is also generated:
     * <pre>
     * @Retention(RetentionPolicy.RUNTIME)
     * @interface Foo$annotations${
     *     Foo$annotation$[] value();
     * }
     * </pre>
     */
    private List<JCTree> transformAnnotationClass(Tree.AnyClass def) {
        Class klass = (Class)def.getDeclarationModel();
        String annotationName = Naming.suffixName(Suffix.$annotation$, klass.getName());
        ClassDefinitionBuilder annoBuilder = ClassDefinitionBuilder.klass(this, annotationName, null, false);
        
        // annotations are never explicitly final in Java
        annoBuilder.modifiers(Flags.ANNOTATION | Flags.INTERFACE | (transformClassDeclFlags(klass) & ~FINAL));
        annoBuilder.getInitBuilder().modifiers(transformClassDeclFlags(klass) & ~FINAL);
        annoBuilder.annotations(makeAtRetention(RetentionPolicy.RUNTIME));
        annoBuilder.annotations(makeAtIgnore());
        annoBuilder.annotations(expressionGen().transformAnnotations(false, OutputElement.ANNOTATION_TYPE, def));
        
        for (Tree.Parameter p : def.getParameterList().getParameters()) {
            Parameter parameterModel = p.getParameterModel();
            annoBuilder.method(makeAnnotationMethod(p));
        }
        List<JCTree> result;
        if (isSequencedAnnotation(klass)) {
            result = annoBuilder.annotations(makeAtAnnotationTarget(EnumSet.noneOf(AnnotationTarget.class))).build();
            String wrapperName = Naming.suffixName(Suffix.$annotations$, klass.getName());
            ClassDefinitionBuilder sequencedBuilder = ClassDefinitionBuilder.klass(this, wrapperName, null, false);
            // annotations are never explicitely final in Java
            sequencedBuilder.modifiers(Flags.ANNOTATION | Flags.INTERFACE | (transformClassDeclFlags(klass) & ~FINAL));
            sequencedBuilder.annotations(makeAtRetention(RetentionPolicy.RUNTIME));
            MethodDefinitionBuilder mdb = MethodDefinitionBuilder.systemMethod(this, naming.getSequencedAnnotationMethodName());
            mdb.annotationFlags(Annotations.MODEL_AND_USER);
            mdb.modifiers(PUBLIC | ABSTRACT);
            mdb.resultType(null, make().TypeArray(makeJavaType(klass.getType(), JT_ANNOTATION)));
            mdb.noBody();
            ClassDefinitionBuilder sequencedAnnotation = sequencedBuilder.method(mdb);
            sequencedAnnotation.annotations(transformAnnotationConstraints(klass));
            sequencedAnnotation.annotations(makeAtIgnore());
            result = result.appendList(sequencedAnnotation.build());
            
        } else {
            result = annoBuilder.annotations(transformAnnotationConstraints(klass)).build();
        }
        
        return result;
    }
    
    private List<JCAnnotation> makeAtRetention(RetentionPolicy retentionPolicy) {
        return List.of(
                make().Annotation(
                        make().Type(syms().retentionType), 
                        List.of(naming.makeQuotedQualIdent(make().Type(syms().retentionPolicyType), retentionPolicy.name()))));
    }
    
    /** 
     * Makes {@code @java.lang.annotation.Target(types)} 
     * where types are the given element types.
     */
    private List<JCAnnotation> makeAtAnnotationTarget(EnumSet<AnnotationTarget> types) {
        List<JCExpression> typeExprs = List.<JCExpression>nil();
        for (AnnotationTarget type : types) {
            typeExprs = typeExprs.prepend(naming.makeQuotedQualIdent(make().Type(syms().elementTypeType), type.name()));
        }
        return List.of(
                make().Annotation(
                        make().Type(syms().targetType), 
                        List.<JCExpression>of(make().NewArray(null, null, typeExprs))));
    }
    
    private List<JCAnnotation> transformAnnotationConstraints(Class klass) {
        TypeDeclaration meta = (TypeDeclaration)typeFact().getLanguageModuleDeclaration("ConstrainedAnnotation");
        ProducedType constrainedType = klass.getType().getSupertype(meta);
        EnumSet<AnnotationTarget> types = EnumSet.noneOf(AnnotationTarget.class);
        if (constrainedType != null) {
            ProducedType programElement = constrainedType.getTypeArgumentList().get(2);
            if (programElement.covers(((TypeDeclaration)typeFact().getLanguageModuleDeclarationDeclaration("InterfaceDeclaration")).getType())
                    || programElement.covers(((TypeDeclaration)typeFact().getLanguageModuleDeclarationDeclaration("ClassDeclaration")).getType())
                    || programElement.covers(((TypeDeclaration)typeFact().getLanguageModuleDeclarationDeclaration("Package")).getType())
                    || programElement.covers(((TypeDeclaration)typeFact().getLanguageModuleDeclarationDeclaration("Module")).getType())) {
                types.add(AnnotationTarget.TYPE);
            } 
            if (programElement.covers(((TypeDeclaration)typeFact().getLanguageModuleDeclarationDeclaration("ValueDeclaration")).getType())
                    || programElement.covers(((TypeDeclaration)typeFact().getLanguageModuleDeclarationDeclaration("FunctionDeclaration")).getType())) {
                types.add(AnnotationTarget.METHOD);
                types.add(AnnotationTarget.PARAMETER);
            } 
            if (programElement.covers(((TypeDeclaration)typeFact().getLanguageModuleDeclarationDeclaration("ConstructorDeclaration")).getType())) {
                types.add(AnnotationTarget.CONSTRUCTOR);
            } 
            if (programElement.covers(((TypeDeclaration)typeFact().getLanguageModuleDeclarationDeclaration("Import")).getType())) {
                types.add(AnnotationTarget.FIELD);
            }
            if (programElement.covers(((TypeDeclaration)typeFact().getLanguageModuleDeclarationDeclaration("SetterDeclaration")).getType())) {
                types.add(AnnotationTarget.METHOD);
            }
            if (programElement.covers(((TypeDeclaration)typeFact().getLanguageModuleDeclarationDeclaration("AliasDeclaration")).getType())) {
                types.add(AnnotationTarget.TYPE);
            }
        }
        return makeAtAnnotationTarget(types);
    }

    private JCExpression transformAnnotationParameterDefault(Tree.Parameter p) {
        Tree.SpecifierOrInitializerExpression defaultArgument = Decl.getDefaultArgument(p);
        Tree.Expression defaultExpression = defaultArgument.getExpression();
        Tree.Term term = defaultExpression.getTerm();
        JCExpression defaultLiteral = null;
        if (term instanceof Tree.Literal
                && !(term instanceof Tree.QuotedLiteral)) {
            defaultLiteral = expressionGen().transform((Tree.Literal)term);
        } else if (term instanceof Tree.BaseMemberExpression) {
            Tree.BaseMemberExpression bme = (Tree.BaseMemberExpression)term;
            Declaration decl = bme.getDeclaration();
            if (isBooleanTrue(decl)) {
                defaultLiteral = makeBoolean(true);
            } else if (isBooleanFalse(decl)) {
                defaultLiteral = makeBoolean(false);
            } else if (typeFact().isEmptyType(bme.getTypeModel())) {
                defaultLiteral = make().NewArray(null, null, List.<JCExpression>nil());
            } else if (Decl.isAnonCaseOfEnumeratedType(bme)) {
                defaultLiteral = makeClassLiteral(bme.getTypeModel());
            } else {
                defaultLiteral = make().Literal(bme.getDeclaration().getQualifiedNameString());
            }
        } else if (term instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression mte = (Tree.MemberOrTypeExpression)term;
            defaultLiteral = make().Literal(mte.getDeclaration().getQualifiedNameString());
        } else if (term instanceof Tree.SequenceEnumeration) {
            Tree.SequenceEnumeration seq = (Tree.SequenceEnumeration)term;
            SequencedArgument sequencedArgument = seq.getSequencedArgument();
            defaultLiteral = makeArrayInitializer(sequencedArgument);
        } else if (term instanceof Tree.Tuple) {
            Tree.Tuple seq = (Tree.Tuple)term;
            SequencedArgument sequencedArgument = seq.getSequencedArgument();
            defaultLiteral = makeArrayInitializer(sequencedArgument);
        } else if (term instanceof Tree.InvocationExpression) {
            // Allow invocations of annotation constructors, so long as they're
            // themselves being invoked with permitted arguments
            Tree.InvocationExpression invocation = (Tree.InvocationExpression)term;
            try {
                defaultLiteral = AnnotationInvocationVisitor.transform(expressionGen(), invocation);
            } catch (BugException e) {
                defaultLiteral = e.makeErroneous(this, invocation);
            }
        } else if (term instanceof Tree.MemberLiteral) {
            defaultLiteral = expressionGen().makeMetaLiteralStringLiteralForAnnotation((Tree.MemberLiteral) term);
        } else if (term instanceof Tree.TypeLiteral) {
            defaultLiteral = expressionGen().makeMetaLiteralStringLiteralForAnnotation((Tree.TypeLiteral) term);
        }
        if (defaultLiteral == null) {
            defaultLiteral = makeErroneous(p, "compiler bug: " + p.getParameterModel().getName() + " has an unsupported defaulted parameter expression");
        }
        return defaultLiteral;
    }

    private JCExpression transformAnnotationMethodType(Tree.Parameter parameter) {
        ProducedType parameterType = parameter.getParameterModel().getType();
        JCExpression type = null;
        if (isScalarAnnotationParameter(parameterType)) {
            type = makeJavaType(parameterType.withoutUnderlyingType(), JT_ANNOTATION);
        } else if (isMetamodelReference(parameterType)) {
            type = make().Type(syms().stringType);
        } else if (Decl.isEnumeratedTypeWithAnonCases(parameterType)) {
            type = makeJavaClassTypeBounded(parameterType);
        } else if (typeFact().isIterableType(parameterType)) {
            ProducedType iteratedType = typeFact().getIteratedType(parameterType);
            if (isScalarAnnotationParameter(iteratedType)) {
                JCExpression scalarType = makeJavaType(iteratedType, JT_ANNOTATION);
                type = make().TypeArray(scalarType);
            } else if (isMetamodelReference(iteratedType)) {
                JCExpression scalarType = make().Type(syms().stringType);
                type = make().TypeArray(scalarType);
            } else if (Decl.isEnumeratedTypeWithAnonCases(iteratedType)) {
                JCExpression scalarType = makeJavaClassTypeBounded(iteratedType);
                type = make().TypeArray(scalarType);
            }
        }
        if (type == null) {
            type = makeErroneous(parameter, "compiler bug: " + parameter.getParameterModel().getName() + " has an unsupported annotation parameter type");
        }
        return type;
    }

    private boolean isMetamodelReference(ProducedType parameterType) {
        return isCeylonMetamodelDeclaration(parameterType);
    }
    /**
     * Makes a new Array expression suitable for use in initializing a Java array
     * using as elements the positional arguments 
     * of the given {@link Tree.SequencedArgument} (which must 
     * be {@link Tree.ListedArgument}s).
     * 
     * <pre>
     *     Whatever[] w = <strong>{ listedArg1, listedArg2, ... }</strong>
     *     //             ^---------- this bit ---------^
     * </pre>
     * 
     * @param sequencedArgument
     * @return The array initializer expression
     */
    JCExpression makeArrayInitializer(
            Tree.SequencedArgument sequencedArgument) {
        JCExpression defaultLiteral;
        ListBuffer<JCExpression> elements = ListBuffer.<JCTree.JCExpression>lb();
        if (sequencedArgument != null) {
            for (Tree.PositionalArgument arg : sequencedArgument.getPositionalArguments()) {
                if (arg instanceof Tree.ListedArgument) {
                    Tree.ListedArgument la = (Tree.ListedArgument)arg;
                    elements.append(expressionGen().transformExpression(la.getExpression().getTerm(), BoxingStrategy.UNBOXED, la.getExpression().getTypeModel()));
                } else {
                    elements = null;
                    break;
                }
            }
        }
        defaultLiteral = elements == null ? null : 
            make().NewArray(null, List.<JCExpression>nil(), elements.toList());
        return defaultLiteral;
    }

    private boolean isScalarAnnotationParameter(ProducedType parameterType) {
        return isCeylonBasicType(parameterType)
                || Decl.isAnnotationClass(parameterType.getDeclaration());
    }

    private List<JCStatement> visitClassOrInterfaceDefinition(Node def, ClassDefinitionBuilder classBuilder) {
        // Transform the class/interface members
        CeylonVisitor visitor = gen().visitor;
        
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
            naming.closeScopedSubstitutions(def.getScope());
        }
    }

    private void generateInstantiators(ClassDefinitionBuilder classBuilder, 
            Class cls, Constructor ctor, ClassDefinitionBuilder instantiatorDeclCb, 
            ClassDefinitionBuilder instantiatorImplCb, Tree.Declaration node, Tree.ParameterList pl) {
        // TODO Instantiators on companion classes
        ParameterList parameterList = ctor != null ? ctor.getParameterLists().get(0) : cls.getParameterList();
        if (Decl.withinInterface(cls)) {
            DefaultedArgumentOverload overloaded = new DefaultedArgumentInstantiator(daoAbstract, cls, ctor);
            MethodDefinitionBuilder instBuilder = overloaded.makeOverload(
                    parameterList,
                    null,
                    cls.getTypeParameters());
            instantiatorDeclCb.method(instBuilder);
        }
        if (!Decl.withinInterface(cls)
                || !cls.isFormal()) {
            DefaultedArgumentOverload overloaded = new DefaultedArgumentInstantiator(!cls.isFormal() ? new DaoThis(node, pl) : daoAbstract, cls, ctor);
            MethodDefinitionBuilder instBuilder = overloaded.makeOverload(
                    parameterList,
                    null,
                    cls.getTypeParameters());
            instantiatorImplCb.method(instBuilder);
        }
    }

    private void makeAttributeForValueParameter(ClassDefinitionBuilder classBuilder, 
            Tree.Parameter parameterTree, Tree.TypedDeclaration memberTree) {
        Parameter decl = parameterTree.getParameterModel();
        if (!(decl.getModel() instanceof Value)) {
            return;
        }        
        final Value value = (Value)decl.getModel();
        if (decl.getDeclaration() instanceof Constructor) {
            classBuilder.field(PUBLIC | FINAL, decl.getName(), makeJavaType(decl.getType()), 
                    null, false, expressionGen().transformAnnotations(false, OutputElement.FIELD, memberTree));
            classBuilder.getInitBuilder().init(make().Exec(make().Assign(naming.makeQualIdent(naming.makeThis(), decl.getName()), naming.makeName(value, Naming.NA_IDENT))));
        } else if (parameterTree instanceof Tree.ValueParameterDeclaration
                && (value.isShared() || value.isCaptured())) {
            makeFieldForParameter(classBuilder, decl, memberTree);
            AttributeDefinitionBuilder adb = AttributeDefinitionBuilder.getter(this, decl.getName(), decl.getModel());
            adb.modifiers(classGen().transformAttributeGetSetDeclFlags(decl.getModel(), false));
            adb.userAnnotations(expressionGen().transformAnnotations(true, OutputElement.GETTER, memberTree));
            classBuilder.attribute(adb);
            if (value.isVariable()) {
                AttributeDefinitionBuilder setter = AttributeDefinitionBuilder.setter(this, decl.getName(), decl.getModel());
                setter.modifiers(classGen().transformAttributeGetSetDeclFlags(decl.getModel(), false));
                //setter.userAnnotations(expressionGen().transform(AnnotationTarget.SETTER, memberTree.getAnnotationList()));
                classBuilder.attribute(setter);
            }
        } else if (decl.isHidden()
                        // TODO Isn't this always true here? We know this is a parameter to a Class
                        && (decl.getDeclaration() instanceof TypeDeclaration)) {
            Declaration member = CodegenUtil.findMethodOrValueForParam(decl);
            if (Strategy.createField(decl, (Value)member)) {
                // The field itself is created by when we transform the AttributeDeclaration 
                // but it has to be initialized here so all the fields are initialized in parameter order
                
                JCExpression parameterExpr = makeUnquotedIdent(Naming.getAliasedParameterName(decl));
                ProducedTypedReference typedRef = getTypedReference(value);
                ProducedTypedReference nonWideningTypedRef = nonWideningTypeDecl(typedRef);
                ProducedType paramType = nonWideningType(typedRef, nonWideningTypedRef);
                if (!paramType.isExactly(decl.getType())) {
                    // The parameter type follows normal erasure rules, not affected by inheritance
                    // but the attribute respects non-widening rules, so we may need to cast
                    // the parameter to the field type (see #1728)
                    parameterExpr = make().TypeCast(classGen().transformClassParameterType(decl), parameterExpr);
                }
                classBuilder.getInitBuilder().init(make().Exec(
                        make().Assign(naming.makeQualifiedName(naming.makeThis(), value, Naming.NA_IDENT),
                                parameterExpr)));
            }
        }
    }

    private int transformClassParameterDeclFlags(Parameter param) {
        return param.getModel().isVariable() ? 0 : FINAL;
    }
    
    private void makeFieldForParameter(ClassDefinitionBuilder classBuilder,
            Parameter decl, Tree.Declaration annotated) {
        MethodOrValue model = decl.getModel();
        classBuilder.defs(make().VarDef(make().Modifiers(transformClassParameterDeclFlags(decl) | PRIVATE, 
                makeAtIgnore().prependList(expressionGen().transformAnnotations(false, OutputElement.FIELD, annotated))),
                names().fromString(Naming.quoteFieldName(decl.getName())), 
                classGen().transformClassParameterType(decl), null));
        
        classBuilder.getInitBuilder().init(make().Exec(make().Assign(
                naming.makeQualifiedName(naming.makeThis(), model, Naming.NA_IDENT), 
                naming.makeName(model, Naming.NA_IDENT_PARAMETER_ALIASED))));
    }
    

    private void transformParameter(ParameterizedBuilder classBuilder, 
            Tree.Parameter p, Parameter param, Tree.TypedDeclaration member) {
        JCExpression type = makeJavaType(param.getModel(), param.getType(), 0);
        ParameterDefinitionBuilder pdb = ParameterDefinitionBuilder.explicitParameter(this, param);
        pdb.aliasName(Naming.getAliasedParameterName(param));
        if (Naming.aliasConstructorParameterName(param.getModel())) {
            naming.addVariableSubst(param.getModel(), naming.suffixName(Suffix.$param$, param.getName()));
        }
        pdb.sequenced(param.isSequenced());
        pdb.defaulted(param.isDefaulted());
        pdb.type(type, makeJavaTypeAnnotations(param.getModel()));
        pdb.modifiers(transformClassParameterDeclFlags(param));
        if (!(param.getModel().isShared() || param.getModel().isCaptured())) {
            // We load the model for shared parameters from the corresponding member
            pdb.modelAnnotations(param.getModel().getAnnotations());
        }
        if (member != null) {
            pdb.userAnnotations(expressionGen().transformAnnotations(p instanceof Tree.ParameterDeclaration
                    && !(param.getModel().isShared() || param.getModel().isCaptured()), OutputElement.PARAMETER, member));
        } else if (p instanceof Tree.ParameterDeclaration &&
                param.getDeclaration() instanceof Constructor) {
            pdb.userAnnotations(expressionGen().transformAnnotations(true, OutputElement.PARAMETER, ((Tree.ParameterDeclaration)p).getTypedDeclaration()));
        }

        if (classBuilder instanceof ClassDefinitionBuilder
                && pdb.requiresBoxedVariableDecl()) {
            ((ClassDefinitionBuilder)classBuilder).getInitBuilder().init(pdb.buildBoxedVariableDecl());
        }
        classBuilder.parameter(pdb);
    }
    
    private void transformClass(
            Tree.AnyClass def, 
            Class model, 
            ClassDefinitionBuilder classBuilder, 
            Tree.ParameterList paramList, 
            boolean generateInstantiator, 
            ClassDefinitionBuilder instantiatorDeclCb, 
            ClassDefinitionBuilder instantiatorImplCb) {
        // do reified type params first
        classBuilder.reifiedTypeParameters(model.getTypeParameters());
        if (def.getParameterList() != null) {
            for (Tree.Parameter param : def.getParameterList().getParameters()) {
                Tree.TypedDeclaration member = def != null ? Decl.getMemberDeclaration(def, param) : null;
                makeAttributeForValueParameter(classBuilder, param, member);
                makeMethodForFunctionalParameter(classBuilder, param, member);
            }
            transformClassOrCtorParameters(def, model, null, def, def.getParameterList(), 
                    classBuilder,
                    classBuilder.getInitBuilder(), 
                    generateInstantiator, instantiatorDeclCb,
                    instantiatorImplCb);
        }
        satisfaction(def.getSatisfiedTypes(), model, classBuilder);
        serialization(model, classBuilder);
        at(def);
        
        // Generate the inner members list for model loading
        addAtMembers(classBuilder, model, def);
        addAtLocalDeclarations(classBuilder, def);
        
        // Make sure top types satisfy reified type
        addReifiedTypeInterface(classBuilder, model);
    }

    private void transformClassOrCtorParameters(
            Tree.AnyClass def,
            Class cls,
            Constructor constructor,
            Tree.Declaration node,
            Tree.ParameterList paramList,
            ClassDefinitionBuilder classBuilder,
            ParameterizedBuilder constructorBuilder,
            boolean generateInstantiator, 
            ClassDefinitionBuilder instantiatorDeclCb,
            ClassDefinitionBuilder instantiatorImplCb) {
        
        if (constructor != null
                && !Decl.isDefaultConstructor(constructor)) {
            constructorBuilder.parameter(makeConstructorNameParameter(constructor));
        }
        for (final Tree.Parameter param : paramList.getParameters()) {
            // Overloaded instantiators
            
            Parameter paramModel = param.getParameterModel();
            Parameter refinedParam = CodegenUtil.findParamForDecl(
                    (TypedDeclaration)CodegenUtil.getTopmostRefinedDeclaration(param.getParameterModel().getModel()));
            at(param);
            Tree.TypedDeclaration member = def != null ? Decl.getMemberDeclaration(def, param) : null;
            transformParameter(constructorBuilder, param, paramModel, member);
            
            if (Strategy.hasDefaultParameterValueMethod(paramModel)
                    || Strategy.hasDefaultParameterOverload(paramModel)
                    || (generateInstantiator
                            && refinedParam != null
                            && (Strategy.hasDefaultParameterValueMethod(refinedParam)
                                    || Strategy.hasDefaultParameterOverload(refinedParam)))) {
                ClassDefinitionBuilder cbForDevaultValues;
                ClassDefinitionBuilder cbForDevaultValuesDecls = null;
                switch (Strategy.defaultParameterMethodOwner(constructor != null ? constructor : cls)) {
                case STATIC:
                    cbForDevaultValues = classBuilder;
                    break;
                case OUTER:
                    cbForDevaultValues = classBuilder.getContainingClassBuilder();
                    break;
                case OUTER_COMPANION:
                    cbForDevaultValues = classBuilder.getContainingClassBuilder().getCompanionBuilder(Decl.getClassOrInterfaceContainer(cls, true));
                    if ((constructor == null || constructor.isShared())
                            && cls.isShared()) {
                        cbForDevaultValuesDecls = classBuilder.getContainingClassBuilder();
                    }
                    break;
                default:
                    cbForDevaultValues = classBuilder.getCompanionBuilder(cls);
                }
                if ((Strategy.hasDefaultParameterValueMethod(paramModel) 
                            || (refinedParam != null && Strategy.hasDefaultParameterValueMethod(refinedParam)))) { 
                    if (!generateInstantiator || Decl.equal(refinedParam, paramModel)) {
                        cbForDevaultValues.method(makeParamDefaultValueMethod(false, constructor != null ? constructor : cls, paramList, param));
                        if (cbForDevaultValuesDecls != null) {
                            cbForDevaultValuesDecls.method(makeParamDefaultValueMethod(true, constructor != null ? constructor : cls, paramList, param));
                        }
                    } else if (Strategy.hasDelegatedDpm(cls) && cls.getContainer() instanceof Class) {
                        java.util.List<Parameter> parameters = paramList.getModel().getParameters();
                        MethodDefinitionBuilder mdb = 
                        makeDelegateToCompanion((Interface)cls.getRefinedDeclaration().getContainer(),
                                paramModel.getModel().getProducedTypedReference(cls.getType(), null),
                                ((TypeDeclaration)cls.getContainer()).getType(),
                                FINAL | (transformClassDeclFlags(cls) & ~ABSTRACT), 
                                List.<TypeParameter>nil(), Collections.<java.util.List<ProducedType>>emptyList(),
                                paramModel.getType(), 
                                Naming.getDefaultedParamMethodName(cls, paramModel),
                                parameters.subList(0, parameters.indexOf(paramModel)), 
                                false, 
                                Naming.getDefaultedParamMethodName(cls, paramModel));
                        cbForDevaultValues.method(mdb);
                    }
                }
                boolean addOverloadedConstructor = false;
                if (generateInstantiator) {
                    if (Decl.withinInterface(cls)) {
                        
                        MethodDefinitionBuilder instBuilder = new DefaultedArgumentInstantiator(daoAbstract, cls, constructor).makeOverload(
                                paramList.getModel(),
                                param.getParameterModel(),
                                cls.getTypeParameters());
                        instantiatorDeclCb.method(instBuilder);
                    }
                    if (!Decl.withinInterface(cls) || !cls.isFormal()) {
                        MethodDefinitionBuilder instBuilder = new DefaultedArgumentInstantiator(new DaoThis(node, paramList), cls, constructor).makeOverload(
                                paramList.getModel(),
                                param.getParameterModel(),
                                cls.getTypeParameters());
                        instantiatorImplCb.method(instBuilder);
                    } else {
                        addOverloadedConstructor  = true;
                    }
                } else {
                    addOverloadedConstructor = true;
                }
                if (addOverloadedConstructor) {
                    // Add overloaded constructors for defaulted parameter
                    MethodDefinitionBuilder overloadBuilder;
                    DefaultedArgumentConstructor dac;
                    if (constructor != null) {
                        dac = new DefaultedArgumentConstructor(classBuilder.addConstructor(), constructor, node, paramList);
                    } else {
                        dac = new DefaultedArgumentConstructor(classBuilder.addConstructor(), cls, node, paramList);
                    }
                    overloadBuilder = dac.makeOverload(
                            paramList.getModel(),
                            param.getParameterModel(),
                            cls.getTypeParameters());
                }
            }
        }
    }
    
    private ParameterDefinitionBuilder makeConstructorNameParameter(Constructor ctor) {
        Class clz = (Class)ctor.getContainer();
        ParameterDefinitionBuilder pdb = ParameterDefinitionBuilder.implicitParameter(this, Naming.Unfix.$name$.toString());
        pdb.ignored();
        JCExpression type = naming.makeTypeDeclarationExpression(null, ctor, DeclNameFlag.QUALIFIED);
        pdb.type(type, null);
        return pdb;
    }

    /**
     * Add extra constructor and methods required for serialization
     */
    private void serialization(Class model, ClassDefinitionBuilder classBuilder) {
        if (!model.isSerializable()) {
            return;
        }
        at(null);
        classBuilder.serializable();
        serializationConstructor(model, classBuilder);
        serializationSerialize(model, classBuilder);
        serializationDeserialize(model, classBuilder);
    }
    
    private boolean hasField(Declaration member) {
        if (member instanceof Value) {
            Value value = (Value)member;
            if (!value.isTransient()
                    && !value.isFormal()
                    && (value.isShared() || value.isCaptured())) {
                return true;
            }
        }
        return false;
    }
    
    /** 
     * <p>Generates the serialization constructor 
     * with signature {@code ($Serialization$)} which:</p>
     * <ul>
     * <li>invokes {@code super()}, if the super class is also 
     *     serializable,</li>
     * <li>initializes all companion instance fields to a 
     *     newly instantiated companion instance,</li>
     * <li>initializes all reified type argument fields to null,</li>
     * <li>initializes all reference attribute fields to null,</li>
     * <li>initializesall primitive attribute fields to a default 
     *     value (basically some kind of 0)</li>
     * </ul>
     */
    private void serializationConstructor(Class model, ClassDefinitionBuilder classBuilder) {
        MethodDefinitionBuilder ctor = classBuilder.addConstructor();
        ctor.ignoreModelAnnotations();
        ctor.modifiers(PUBLIC);
        ParameterDefinitionBuilder serializationPdb = ParameterDefinitionBuilder.systemParameter(this, "ignored");
        serializationPdb.modifiers(FINAL);
        serializationPdb.type(make().Type(syms().ceylonSerializationType), null);
        ctor.parameter(serializationPdb);
        
        for (TypeParameter tp : model.getTypeParameters()) {
            ctor.reifiedTypeParameter(tp);
        }
        
        final ListBuffer<JCStatement> stmts = ListBuffer.lb();

        if (extendsSerializable(model)) {
            // invoke super
            ListBuffer<JCExpression> superArgs = ListBuffer.<JCExpression>lb();
            superArgs.add(naming.makeUnquotedIdent("ignored"));
            for (JCExpression ta : makeReifiedTypeArguments(model.getExtendedType())) {
                superArgs.add(ta);
            }
            stmts.add(make().Exec(make().Apply(null,
                    naming.makeSuper(),
                    superArgs.toList())));
        }

        // initialize reified type arguments to according to parameters
        for (TypeParameter tp : model.getTypeParameters()) {
            stmts.add(makeReifiedTypeParameterAssignment(tp));
        }
        
        // initialize companion instances to a new companion instance
        if (!model.getSatisfiedTypeDeclarations().isEmpty()) {
            SatisfactionVisitor visitor = new SatisfactionVisitor() {
                @Override
                public void visit(Class model, Interface iface) {
                    if (hasImpl(iface)) {
                        stmts.add(makeCompanionInstanceAssignment(model, iface, model.getType().getSupertype(iface)));
                    }
                }
            };
            HashSet<Interface> satisfiedInterfaces = new HashSet<Interface>();
            for (ProducedType satisfiedType : model.getSatisfiedTypes()) {
                TypeDeclaration decl = satisfiedType.getDeclaration();
                if (!(decl instanceof Interface)) {
                    continue;
                }
                // make sure we get the right instantiation of the interface
                satisfiedType = model.getType().getSupertype(decl);
                walkSatisfiedInterfaces(model, visitor, satisfiedType, satisfiedInterfaces);
            }
        }
        
        // initialize attribute fields to null or a zero
        for (Declaration member : model.getMembers()) {
            if (hasField(member)) {
                Value value = (Value)member;
                if (value.isLate()) {
                    continue;
                }
                // initialize all reference fields to null and all primitive 
                // fields to a default value.
                JCExpression nullOrZero;
                if (CodegenUtil.isUnBoxed(value)) {
                    Object literal;
                    ProducedType type = value.getType();
                    if (isCeylonBoolean(type)) {
                        literal = false;
                    } else if (isCeylonByte(type)) {
                        literal = (byte)0;
                    } else if (isCeylonInteger(type)) {
                        literal = 0L;
                    } else if (isCeylonCharacter(type)) {
                        literal = 0;
                    } else if (isCeylonFloat(type)) {
                        literal = 0.0;
                    } else if (isCeylonString(type)) {
                        literal = "";
                    } else {
                        throw BugException.unhandledCase(type);
                    }
                    nullOrZero = make().Literal(literal);
                } else {
                    nullOrZero = makeNull();
                }
                stmts.add(make().Exec(make().Assign(
                        naming.makeQualifiedName(naming.makeThis(), value, Naming.NA_IDENT), 
                        nullOrZero)));
            }
        }
        ctor.body(stmts.toList());
    }
    
    static interface SatisfactionVisitor {
        void visit(Class model, Interface iface);
    }
    
    private void walkSatisfiedInterfaces(final Class model,
            SatisfactionVisitor visitor, 
            ProducedType satisfiedType, Set<Interface> satisfiedInterfaces) {
        satisfiedType = satisfiedType.resolveAliases();
        Interface iface = (Interface)satisfiedType.getDeclaration();
        
        if (satisfiedInterfaces.contains(iface)
                || iface.getType().isExactly(typeFact().getIdentifiableDeclaration().getType())) {
            return;
        }
        
        visitor.visit(model, iface);
        
        satisfiedInterfaces.add(iface);
        // recurse up the hierarchy
        for (ProducedType sat : iface.getSatisfiedTypes()) {
            sat = model.getType().getSupertype(sat.getDeclaration());
            walkSatisfiedInterfaces(model, visitor, sat, satisfiedInterfaces);
        }
    }

    private boolean extendsSerializable(Class model) {
        return !typeFact().getObjectDeclaration().getType().isExactly(model.getExtendedType())
                && !typeFact().getBasicDeclaration().getType().isExactly(model.getExtendedType());
    }
    
    /**
     * <p>Generates the {@code $serialize$()} method to serialize the classes state
     * which:</p>
     * <ul>
     * <li>invokes {@code super.$serialize$()}, if the super class is also 
     *     serializable,</li>
     * <li>invokes {@code dtor.putTypeArgument()} for each type argument in the 
     *     class,</li>
     * <li>invokes {@code dtor.putValue()} for each attribute in the class 
     *     whose state is held in a field.</li>
     * </ul>
     */
    private void serializationSerialize(Class model,
            ClassDefinitionBuilder classBuilder) {
        MethodDefinitionBuilder mdb = MethodDefinitionBuilder.systemMethod(this, Unfix.$serialize$.toString());
        mdb.isOverride(true);
        mdb.ignoreModelAnnotations();
        mdb.modifiers(PUBLIC);
        ParameterDefinitionBuilder pdb = ParameterDefinitionBuilder.systemParameter(this, Unfix.deconstructor.toString());
        pdb.modifiers(FINAL);
        pdb.type(makeJavaType(typeFact().getDeconstructorType()), null);
        mdb.parameter(pdb);
        
        ListBuffer<JCStatement> stmts = ListBuffer.lb();
        if (extendsSerializable(model)) {
            // invoke super.$serialize$()
            stmts.add(make().Exec(make().Apply(null,
                    naming.makeQualIdent(naming.makeSuper(), Unfix.$serialize$.toString()),
                    List.<JCExpression>of(naming.makeUnquotedIdent(Unfix.deconstructor)))));
        }
        
        // Get the outer instance, if any
        if (model.getContainer() instanceof ClassOrInterface) {
            ClassOrInterface outerInstanceModel = (ClassOrInterface)model.getContainer();
            ProducedType outerInstanceType = outerInstanceModel.getType();
            stmts.add(make().Exec(make().Apply(
                    List.of(makeJavaType(outerInstanceType, JT_TYPE_ARGUMENT)), 
                    naming.makeQualIdent(naming.makeUnquotedIdent(Unfix.deconstructor.toString()), "putOuterInstance"),
                    List.of(makeReifiedTypeArgument(outerInstanceType),
                            expressionGen().makeOuterExpr(outerInstanceType)))));
        }
        
        // get state from fields
        for (Declaration member : model.getMembers()) {
            if (hasField(member)) {
                Value value = (Value)member;
                final ProducedType serializedValueType;
                JCExpression serializedValue;
                //if (Decl.isValueTypeDecl(simplifyType(value.getType()))) {
                
                if (value.isLate()) {
                    stmts.add(make().If(
                            make().Unary(JCTree.NOT, 
                                    naming.makeUnquotedIdent(naming.getInitializationFieldName(value.getName()))), 
                            makeThrowAssertionException(make().Literal("instance cannot be serialized: " + member.getQualifiedNameString() + " has not been initialized")), 
                            null));
                }
                
                serializedValueType = value.getType();
                if (value.isToplevel() || value.isLate()) {
                    serializedValue = make().Apply(null, naming.makeQualifiedName(naming.makeThis(), value, Naming.NA_MEMBER), List.<JCExpression>nil());
                } else {
                    serializedValue = naming.makeQualifiedName(naming.makeThis(), value, Naming.NA_IDENT);
                }
                serializedValue = expressionGen().applyErasureAndBoxing(serializedValue, 
                        value.getType(), 
                        !CodegenUtil.isUnBoxed(value), 
                        BoxingStrategy.BOXED, 
                        value.getType());
                stmts.add(make().Exec(make().Apply(
                        List.of(makeJavaType(serializedValueType, JT_TYPE_ARGUMENT)), 
                        naming.makeQualIdent(naming.makeUnquotedIdent(Unfix.deconstructor.toString()), "putValue"),
                        List.of(makeReifiedTypeArgument(serializedValueType),
                                makeValueDeclaration(value),
                                serializedValue))));
            }
        }
        
        mdb.body(stmts.toList());
        classBuilder.method(mdb);
    }
    /**
     * <p>Generates the {@code $deserialize$()} method to deserialize 
     * the classes state, which:</p>
     * <ul>
     * <li>invokes {@code super.$deserialize$()}, if the super class is also 
     *     serializable,</li>
     * <li>assigns each reified type argument in the 
     *     class by invoking {@code dted.getTypeArgument()},</li>
     * <li>assigns each field in the 
     *     class by invoking {@code dted.getValue()}.</li>
     * </ul>
     */
    private void serializationDeserialize(Class model,
            ClassDefinitionBuilder classBuilder) {
        MethodDefinitionBuilder mdb = MethodDefinitionBuilder.systemMethod(this, Unfix.$deserialize$.toString());
        mdb.isOverride(true);
        mdb.ignoreModelAnnotations();
        mdb.modifiers(PUBLIC);
        ParameterDefinitionBuilder pdb = ParameterDefinitionBuilder.systemParameter(this, Unfix.deconstructed.toString());
        pdb.modifiers(FINAL);
        pdb.type(make().Type(syms().ceylonDeconstructedType), null);
        mdb.parameter(pdb);
        
        ListBuffer<JCStatement> stmts = ListBuffer.lb();
        boolean requiredLookup = false;
        
        
        // assign fields
        for (Declaration member : model.getMembers()) {
            if (hasField(member)) {
                requiredLookup |= makeDeserializationAssignment(stmts, (Value)member);
            }
        }
        if (requiredLookup) {
            // if we needed to use a lookup object to reset final fields, 
            // prepend that variable
            stmts.prepend(makeVar(FINAL, 
                    "lookup", 
                    naming.makeQualIdent(make().Type(syms().methodHandlesType), "Lookup"), 
                    make().Apply(null, naming.makeQuotedFQIdent("java.lang.invoke.MethodHandles.lookup"), List.<JCExpression>nil())));
        }
        
        if (extendsSerializable(model)) {
            // prepend the invocation of super.$serialize$()
            stmts.prepend(make().Exec(make().Apply(null,
                    naming.makeQualIdent(naming.makeSuper(), Unfix.$deserialize$.toString()),
                    List.<JCExpression>of(naming.makeUnquotedIdent(Unfix.deconstructed.toString())))));
        }
        mdb.body(stmts.toList());
        classBuilder.method(mdb);
    }
    
    private boolean makeDeserializationAssignment(ListBuffer<JCStatement> stmts, Value value) {
        ProducedType typeOrReferenceType;
        boolean isValueType = Decl.isValueTypeDecl(simplifyType(value.getType()));
        //if (isValueType) {
        typeOrReferenceType = value.getType();
        //} else {
          //  typeOrReferenceType = typeFact().getReferenceType(value.getType());
        //}
            
        Naming.SyntheticName n = naming.alias("valueOrRef");
        JCExpression newValue = make().Apply(List.of(makeJavaType(typeOrReferenceType, JT_TYPE_ARGUMENT)),
                naming.makeQualIdent(naming.makeUnquotedIdent(Unfix.deconstructed.toString()), "getValue"),
                List.<JCExpression>of(
                        makeReifiedTypeArgument(typeOrReferenceType),
                        makeValueDeclaration(value)));
        // let ( 
        // Object valueOrRef = ^^;
        // valueOrRef instanceof Reference ? (($InstanceLeaker$)valueOrRef).$leakInstance$() : (Type)valueOrRef;
        // )
        newValue = make().LetExpr(makeVar(0, n, make().Type(syms().objectType), newValue), 
                make().Conditional(
                        make().TypeTest(n.makeIdent(),
                                makeJavaType(typeFact().getReferenceType(value.getType()), JT_RAW)), 
                        make().Apply(null,
                                naming.makeQualIdent(make().TypeCast(
                                        make().TypeApply(make().QualIdent(syms().ceylonInstanceLeakerType.tsym), List.of(makeJavaType(value.getType(), JT_TYPE_ARGUMENT))), n.makeIdent()), "$leakInstance$"),
                                List.<JCExpression>nil()), 
                        make().TypeCast(makeJavaType(typeOrReferenceType, JT_NO_PRIMITIVES), n.makeIdent())));

        if (isValueType) {
            newValue = expressionGen().applyErasureAndBoxing(newValue, 
                    value.getType(), true, CodegenUtil.getBoxingStrategy(value), 
                    value.getType());
        } else {
            // We need to obtain the instance from the reference
            // but we don't need the instance to be fully deserialized
        }
        boolean requiredLookup = false;
        final JCStatement assignment;
        if (value.isToplevel() || value.isLate()) {// XXX duplicates logic in AttributeDefinitionBuilder
            // We use the setter for late values, since that will allocate 
            // the array if needed.
            assignment = make().Exec(make().Apply(null,
                    naming.makeQualifiedName(naming.makeThis(), value, Naming.NA_MEMBER | Naming.NA_SETTER),
                    List.of(newValue)));
        } else {
            // We bypass the setter
            if (value.isVariable()) {
                assignment = make().Exec(make().Assign(
                        naming.makeQualifiedName(naming.makeThis(), value, Naming.NA_IDENT), 
                        newValue));
            } else {
                // The field will have final modifier, so we need some 
                // jiggery pokery to reset it.
                requiredLookup = true;
                String fieldName = value.getName();
                JCExpression fieldType = makeJavaType(value.getType());//TODO probably wrong
                assignment = makeReassignFinalField(fieldType, fieldName, newValue);
            }
        }
        stmts.add(assignment);
        return requiredLookup;
    }

    private JCStatement makeReassignFinalField(JCExpression fieldType,
            String fieldName, JCExpression newValue) {
        final JCStatement assignment;
        JCExpression mhExpr = utilInvocation().setter(
                naming.makeUnquotedIdent("lookup"),
                //naming.makeQualIdent(makeJavaType(((Class)value.getContainer()).getType(), JT_NO_PRIMITIVES), "class"),
                make().Literal(fieldName)// TODO field name should encapsulated
                );
        
        JCExpression expr = make().Apply(null, 
                naming.makeQualIdent(mhExpr, "invokeExact"), 
                List.of(naming.makeThis(), 
                        make().TypeCast(fieldType, newValue)));// We always typecast here, due to method handle
        assignment = make().Exec(expr);
        return assignment;
    }
    
    private JCExpression makeValueDeclaration(Value value) {
        return expressionGen().makeMemberValueOrFunctionDeclarationLiteral(null, value, false);
    }
    
    /**
     * Generate a method for a shared FunctionalParameter which delegates to the Callable 
     * @param klass 
     * @param annotations */
    private void makeMethodForFunctionalParameter(
            ClassDefinitionBuilder classBuilder,  
            Tree.Parameter paramTree, Tree.TypedDeclaration memberDecl) {
        Parameter paramModel = paramTree.getParameterModel();

        if (Strategy.createMethod(paramModel)) {
            Tree.MethodDeclaration methodDecl = (Tree.MethodDeclaration)memberDecl;
            makeFieldForParameter(classBuilder, paramModel, memberDecl);
            Method method = (Method)paramModel.getModel();

            java.util.List<Parameter> parameters = method.getParameterLists().get(0).getParameters();
            CallBuilder callBuilder = CallBuilder.instance(this).invoke(
                    naming.makeQualIdent(naming.makeName(method, Naming.NA_IDENT), 
                            Naming.getCallableMethodName(method)));
            for (Parameter parameter : parameters) {
                JCExpression parameterExpr = naming.makeName(parameter.getModel(), Naming.NA_IDENT);
                parameterExpr = expressionGen().applyErasureAndBoxing(parameterExpr, parameter.getType(), 
                        !CodegenUtil.isUnBoxed(parameter.getModel()), BoxingStrategy.BOXED, 
                        parameter.getType());
                callBuilder.argument(parameterExpr);
            }
            JCExpression expr = callBuilder.build();
            JCStatement body;
            if (isVoid(memberDecl) && Decl.isUnboxedVoid(method) && !Strategy.useBoxedVoid(method)) {
                body = make().Exec(expr);
            } else {
                expr = expressionGen().applyErasureAndBoxing(expr, paramModel.getType(), true, CodegenUtil.getBoxingStrategy(method), paramModel.getType());
                body = make().Return(expr);
            }
            classBuilder.methods(transformMethod(method, null, methodDecl, methodDecl.getParameterLists(),
                    methodDecl,
                    true, method.isActual(), true, 
                    List.of(body), new DaoThis(methodDecl, methodDecl.getParameterLists().get(0)), false));
        }
    }
    
    private void addReifiedTypeInterface(ClassDefinitionBuilder classBuilder, ClassOrInterface model) {
        if(model.getExtendedType() == null || willEraseToObject(model.getExtendedType()) || !Decl.isCeylon(model.getExtendedTypeDeclaration()))
            classBuilder.reifiedType();
    }

    /**
     * Transforms the type of the given class parameter
     * @param decl
     * @return
     */
    JCExpression transformClassParameterType(Parameter parameter) {
        MethodOrValue decl = parameter.getModel();
        if (!(decl.getContainer() instanceof Class)) {
            throw new BugException("expected parameter of Class");
        }
        JCExpression type;
        MethodOrValue attr = decl;
        if (!Decl.isTransient(attr)) {
            ProducedTypedReference typedRef = getTypedReference(attr);
            ProducedTypedReference nonWideningTypedRef = nonWideningTypeDecl(typedRef);
            ProducedType paramType = nonWideningType(typedRef, nonWideningTypedRef);
            type = makeJavaType(nonWideningTypedRef.getDeclaration(), paramType, 0);
        } else {
            ProducedType paramType = decl.getType();
            type = makeJavaType(decl, paramType, 0);
        }
        return type;
    }

    private void transformInterface(Tree.ClassOrInterface def, 
            Interface model, 
            ClassDefinitionBuilder classBuilder) {
        //  Copy all the qualifying type's type parameters into the interface
        java.util.List<TypeParameter> typeParameters = typeParametersOfAllContainers(model, false);
        for(TypeParameter tp : typeParameters){
            classBuilder.typeParameter(tp, false);
        }
        
        if(model.isCompanionClassNeeded()){
            classBuilder.method(makeCompanionAccessor(model, model.getType(), null, false));
            // Build the companion class
            buildCompanion(def, (Interface)model, classBuilder);
        }
        
        addAmbiguousMembers(classBuilder, model);
        
        // Generate the inner members list for model loading
        addAtMembers(classBuilder, model, def);
        addAtLocalDeclarations(classBuilder, def);
    }

    private void addAmbiguousMembers(ClassDefinitionBuilder classBuilder, Interface model) {
        // only if we refine more than one interface
        java.util.List<ProducedType> satisfiedTypes = model.getSatisfiedTypes();
        if(satisfiedTypes.size() <= 1)
            return;
        Set<Interface> satisfiedInterfaces = new HashSet<Interface>();
        for(TypeDeclaration interfaceDecl : model.getSatisfiedTypeDeclarations()){
            collectInterfaces((Interface) interfaceDecl, satisfiedInterfaces);
        }

        Set<Interface> ambiguousInterfaces = new HashSet<Interface>();
        for(Interface satisfiedInterface : satisfiedInterfaces){
            if(isInheritedWithDifferentTypeArguments(satisfiedInterface, model.getType()) != null){
                ambiguousInterfaces.add(satisfiedInterface);
            }
        }
        Set<String> treated = new HashSet<String>();
        for(Interface ambiguousInterface : ambiguousInterfaces){
            for(Declaration member : ambiguousInterface.getMembers()){
                String name = member.getName();
                // skip if already handled
                if(treated.contains(name))
                    continue;
                // skip if it's implemented directly
                if(model.getDirectMember(name, null, false) != null){
                    treated.add(name);
                    continue;
                }
                // find if we have different implementations in two direct interfaces
                LOOKUP:
                for(int i=0;i<satisfiedTypes.size();i++){
                    ProducedType firstInterface = satisfiedTypes.get(i);
                    Declaration member1 = firstInterface.getDeclaration().getMember(name, null, false);
                    // if we can't find it in this interface, move to the next
                    if(member1 == null)
                        continue;
                    // try to find member in other interfaces
                    for(int j=i+1;j<satisfiedTypes.size();j++){
                        ProducedType secondInterface = satisfiedTypes.get(j);
                        Declaration member2 = secondInterface.getDeclaration().getMember(name, null, false);
                        // if we can't find it in this interface, move to the next
                        if(member2 == null)
                            continue;
                        // we have it in two separate interfaces
                        ProducedReference typedMember1 = firstInterface.getTypedReference(member1, Collections.<ProducedType>emptyList());
                        ProducedReference typedMember2 = secondInterface.getTypedReference(member2, Collections.<ProducedType>emptyList());
                        ProducedType type1 = simplifyType(typedMember1.getType());
                        ProducedType type2 = simplifyType(typedMember2.getType());
                        if(!type1.isExactly(type2)){
                            // treat it and stop looking for other interfaces
                            addAmbiguousMember(classBuilder, model, name);
                            break LOOKUP;
                        }
                    }
                }
                // that member has no conflict
                treated.add(name);
            }
        }
    }

    private void addAmbiguousMember(ClassDefinitionBuilder classBuilder, Interface model, String name) {
        Declaration member = model.getMember(name, null, false);
        ProducedType satisfiedType = model.getType().getSupertype(model);
        if (member instanceof Class) {
            Class klass = (Class)member;
            if (Strategy.generateInstantiator(member)
                    && !klass.hasConstructors()) {
                // instantiator method implementation
                generateInstantiatorDelegate(classBuilder, satisfiedType,
                        model, klass, null, model.getType(), false);
            } 
            if (klass.hasConstructors()) {
                for (Declaration m : klass.getMembers()) {
                    if (m instanceof Constructor
                            && Strategy.generateInstantiator(m)) {
                        Constructor ctor = (Constructor)m;
                        generateInstantiatorDelegate(classBuilder, satisfiedType,
                                model, klass, ctor, model.getType(), false);
                    }
                }
                
            }
        }else if (member instanceof Method) {
            Method method = (Method)member;
            final ProducedTypedReference typedMember = satisfiedType.getTypedMember(method, Collections.<ProducedType>emptyList());
            java.util.List<java.util.List<ProducedType>> producedTypeParameterBounds = producedTypeParameterBounds(
                    typedMember, method);
            final java.util.List<TypeParameter> typeParameters = method.getTypeParameters();
            final java.util.List<Parameter> parameters = method.getParameterLists().get(0).getParameters();

            for (Parameter param : parameters) {

                if (Strategy.hasDefaultParameterOverload(param)) {
                    MethodDefinitionBuilder overload = new DefaultedArgumentMethodTyped(null, MethodDefinitionBuilder.method(this, method), typedMember)
                    .makeOverload(
                            method.getParameterLists().get(0),
                            param,
                            typeParameters);
                    overload.modifiers(PUBLIC | ABSTRACT);
                    classBuilder.method(overload);
                }
            }
        
            final MethodDefinitionBuilder concreteMemberDelegate = makeDelegateToCompanion(null,
                    typedMember,
                    model.getType(),
                    PUBLIC | ABSTRACT,
                    method.getTypeParameters(), 
                    producedTypeParameterBounds, 
                    typedMember.getType(), 
                    naming.selector(method), 
                    method.getParameterLists().get(0).getParameters(),
                    ((Method) member).getTypeErased(),
                    null, 
                    false);
            classBuilder.method(concreteMemberDelegate);
        } else if (member instanceof Value
                || member instanceof Setter) {
            TypedDeclaration attr = (TypedDeclaration)member;
            final ProducedTypedReference typedMember = satisfiedType.getTypedMember(attr, Collections.<ProducedType>emptyList());
            if (member instanceof Value) {
                final MethodDefinitionBuilder getterDelegate = makeDelegateToCompanion(null, 
                        typedMember,
                        model.getType(),
                        PUBLIC | ABSTRACT, 
                        Collections.<TypeParameter>emptyList(), 
                        Collections.<java.util.List<ProducedType>>emptyList(),
                        typedMember.getType(), 
                        Naming.getGetterName(attr), 
                        Collections.<Parameter>emptyList(),
                        attr.getTypeErased(),
                        null,
                        false);
                classBuilder.method(getterDelegate);
            }
            if (member instanceof Setter) { 
                final MethodDefinitionBuilder setterDelegate = makeDelegateToCompanion(null, 
                        typedMember,
                        model.getType(),
                        PUBLIC | ABSTRACT, 
                        Collections.<TypeParameter>emptyList(), 
                        Collections.<java.util.List<ProducedType>>emptyList(),
                        typeFact().getAnythingDeclaration().getType(), 
                        Naming.getSetterName(attr), 
                        Collections.<Parameter>singletonList(((Setter)member).getParameter()),
                        ((Setter) member).getTypeErased(),
                        null,
                        false);
                classBuilder.method(setterDelegate);
            }
        }
    }

    private void addAtMembers(ClassDefinitionBuilder classBuilder, ClassOrInterface model, 
            Tree.ClassOrInterface def) {
        List<JCExpression> members = List.nil();
        for(Declaration member : model.getMembers()){
            if(member instanceof ClassOrInterface == false
                    && member instanceof TypeAlias == false){
                continue;
            }
            TypeDeclaration innerType = (TypeDeclaration) member;
            Tree.Declaration innerTypeTree = findInnerType(def, innerType.getName());
            if(innerTypeTree != null) {
                TransformationPlan plan = errors().hasDeclarationAndMarkBrokenness(innerTypeTree);
                if (plan instanceof Drop) {
                    continue;
                }
            }
            JCAnnotation atMember;
            // interfaces are moved to toplevel so they can lose visibility of member types if they are local
            if(Decl.isLocal(model) && model instanceof Interface)
                atMember = makeAtMember(innerType.getName());
            else
                atMember = makeAtMember(innerType.getType());
            members = members.prepend(atMember);
        }
        classBuilder.annotations(makeAtMembers(members));
    }

    private Tree.Declaration findInnerType(Tree.ClassOrInterface def, String name) {
        Tree.Body body;
        if(def instanceof Tree.ClassDefinition)
            body = ((Tree.ClassDefinition) def).getClassBody();
        else if(def instanceof Tree.InterfaceDefinition)
            body = ((Tree.InterfaceDefinition) def).getInterfaceBody();
        else
            return null;
        for(Node node : body.getStatements()){
            if(node instanceof Tree.Declaration
                    && ((Tree.Declaration) node).getIdentifier() != null
                    && ((Tree.Declaration) node).getIdentifier().getText().equals(name))
                return (Tree.Declaration) node;
        }
        return null;
    }

    private void addAtLocalDeclarations(ClassDefinitionBuilder classBuilder, Tree.ClassOrInterface tree) {
        classBuilder.annotations(makeAtLocalDeclarations(tree));
    }

    private void addAtContainer(ClassDefinitionBuilder classBuilder, TypeDeclaration model) {
        Scope scope = model.getContainer();
        boolean inlineObjectInToplevelAttr = Decl.isTopLevelObjectExpressionType(model);
        if(scope == null || (scope instanceof Package && !inlineObjectInToplevelAttr))
            return;
        if(scope instanceof ClassOrInterface && !inlineObjectInToplevelAttr){
            ClassOrInterface container = (ClassOrInterface) scope;
            List<JCAnnotation> atContainer = makeAtContainer(container.getType());
            classBuilder.annotations(atContainer);
        }else{
            if(model instanceof Interface)
                classBuilder.annotations(makeLocalContainerPath((Interface) model));
            Declaration declarationContainer = getDeclarationContainer(model);
            classBuilder.annotations(makeAtLocalDeclaration(model.getQualifier(), declarationContainer == null));
        }
        
    }

    private void satisfaction(Tree.SatisfiedTypes satisfied, final Class model, ClassDefinitionBuilder classBuilder) {
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
        if (satisfied != null) {
            for (Tree.StaticType type : satisfied.getTypes()) {
                try {
                    ProducedType satisfiedType = type.getTypeModel();
                    TypeDeclaration decl = satisfiedType.getDeclaration();
                    if (!(decl instanceof Interface)) {
                        continue;
                    }
                    // make sure we get the right instantiation of the interface
                    satisfiedType = model.getType().getSupertype(decl);
                    concreteMembersFromSuperinterfaces(model, classBuilder, satisfiedType, satisfiedInterfaces);
                } catch (BugException e) {
                    e.addError(type);
                }
            }
        }
        // now find the set of interfaces we implemented twice with more refined type parameters
        if(model.getExtendedTypeDeclaration() != null){
            // reuse that Set
            satisfiedInterfaces.clear();
            for(TypeDeclaration interfaceDecl : model.getSatisfiedTypeDeclarations()){
                collectInterfaces((Interface) interfaceDecl, satisfiedInterfaces);
            }
            // now see if we refined them
            for(Interface iface : satisfiedInterfaces){
                // skip those we can't do anything about
                if(!supportsReified(iface) || !CodegenUtil.isCompanionClassNeeded(iface))
                    continue;
                ProducedType thisType = model.getType().getSupertype(iface);
                ProducedType superClassType = model.getExtendedType().getSupertype(iface);
                if(thisType != null
                        && superClassType != null
                        && !thisType.isExactly(superClassType)
                        && thisType.isSubtypeOf(superClassType)){
                    // we're refining it
                    classBuilder.refineReifiedType(thisType);
                }
            }
        }
    }

    private void collectInterfaces(Interface interfaceDecl, Set<Interface> satisfiedInterfaces) {
        if(satisfiedInterfaces.add(interfaceDecl)){
            for(TypeDeclaration newInterfaceDecl : interfaceDecl.getSatisfiedTypeDeclarations()){
                if (!(newInterfaceDecl instanceof UnknownType)) {
                    collectInterfaces((Interface) newInterfaceDecl, satisfiedInterfaces);
                }
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
        
        // For each super interface
        for (Declaration member : iface.getMembers()) {
            
            if (member instanceof Class) {
                Class klass = (Class)member;
                if (Strategy.generateInstantiator(member)
                        && !klass.hasConstructors()
                    && !model.isFormal()
                    && needsCompanionDelegate(model, member)
                    && model.getDirectMember(member.getName(), null, false) == null) {
                    // instantiator method implementation
                    generateInstantiatorDelegate(classBuilder, satisfiedType,
                            iface, klass, null, model.getType(), !member.isFormal());
                } 
                if (klass.hasConstructors()) {
                    for (Declaration m : klass.getMembers()) {
                        if (m instanceof Constructor
                                && Strategy.generateInstantiator(m)) {
                            Constructor ctor = (Constructor)m;
                            generateInstantiatorDelegate(classBuilder, satisfiedType,
                                    iface, klass, ctor, model.getType(), true);
                        }
                    }
                    
                }
            }
            
            // type aliases are on the $impl class
            if(member instanceof TypeAlias)
                continue;
            
            if (Strategy.onlyOnCompanion(member)) {
                // non-shared interface methods don't need implementing
                // (they're just private methods on the $impl)
                continue;
            }
            if (member instanceof Method) {
                Method method = (Method)member;
                final ProducedTypedReference typedMember = satisfiedType.getTypedMember(method, Collections.<ProducedType>emptyList());
                Declaration sub = (Declaration)model.getMember(method.getName(), null, false);
                if (sub instanceof Method) {
                    Method subMethod = (Method)sub;
                    if (subMethod.getParameterLists().isEmpty()) {
                        continue;
                    }
                    java.util.List<java.util.List<ProducedType>> producedTypeParameterBounds = producedTypeParameterBounds(
                            typedMember, subMethod);
                    final ProducedTypedReference refinedTypedMember = model.getType().getTypedMember(subMethod, Collections.<ProducedType>emptyList());
                    final java.util.List<TypeParameter> typeParameters = subMethod.getTypeParameters();
                    final java.util.List<Parameter> parameters = subMethod.getParameterLists().get(0).getParameters();
                    boolean hasOverloads = false;
                    if (!satisfiedInterfaces.contains((Interface)method.getContainer())) {

                        for (Parameter param : parameters) {
                            if (Strategy.hasDefaultParameterValueMethod(param)
                                    && CodegenUtil.getTopmostRefinedDeclaration(param.getModel()).getContainer().equals(member)) {
                                final ProducedTypedReference typedParameter = typedMember.getTypedParameter(param);
                                // If that method has a defaulted parameter, 
                                // we need to generate a default value method
                                // which also delegates to the $impl
                                final MethodDefinitionBuilder defaultValueDelegate = makeDelegateToCompanion(iface,
                                        typedParameter,
                                        model.getType(),
                                        PUBLIC | FINAL, 
                                        typeParameters, producedTypeParameterBounds,
                                        typedParameter.getType(), 
                                        Naming.getDefaultedParamMethodName(method, param), 
                                        parameters.subList(0, parameters.indexOf(param)),
                                        param.getModel().getTypeErased(),
                                        null);
                                classBuilder.method(defaultValueDelegate);
                            }

                            if (Strategy.hasDefaultParameterOverload(param)) {
                                if ((method.isDefault() || method.isShared() && !method.isFormal())
                                        && Decl.equal(method, subMethod)) {
                                    MethodDefinitionBuilder overload = new DefaultedArgumentMethodTyped(new DaoThis((Tree.AnyMethod)null, null), MethodDefinitionBuilder.method(this, subMethod), typedMember)
                                        .makeOverload(
                                            subMethod.getParameterLists().get(0),
                                            param,
                                            typeParameters);
                                    classBuilder.method(overload);
                                }

                                hasOverloads = true;
                            }
                        }
                    }
                    // if it has the *most refined* default concrete member, 
                    // then generate a method on the class
                    // delegating to the $impl instance
                    if (needsCompanionDelegate(model, member)) {

                        final MethodDefinitionBuilder concreteMemberDelegate = makeDelegateToCompanion(iface,
                                typedMember,
                                model.getType(),
                                PUBLIC | (method.isDefault() ? 0 : FINAL),
                                typeParameters, 
                                producedTypeParameterBounds, 
                                typedMember.getType(), 
                                naming.selector(method), 
                                method.getParameterLists().get(0).getParameters(),
                                ((Method) member).getTypeErased(),
                                null);
                        classBuilder.method(concreteMemberDelegate);
                    }

                    if (hasOverloads
                            && (method.isDefault() || method.isShared() && !method.isFormal())
                            && Decl.equal(method, subMethod)) {
                        final MethodDefinitionBuilder canonicalMethod = makeDelegateToCompanion(iface,
                                typedMember,
                                model.getType(),
                                PRIVATE,
                                subMethod.getTypeParameters(), 
                                producedTypeParameterBounds,
                                typedMember.getType(), 
                                Naming.selector(method, Naming.NA_CANONICAL_METHOD), 
                                method.getParameterLists().get(0).getParameters(),
                                ((Method) member).getTypeErased(),
                                naming.selector(method));
                        classBuilder.method(canonicalMethod);
                    }
                }
            } else if (member instanceof Value
                    || member instanceof Setter) {
                TypedDeclaration attr = (TypedDeclaration)member;
                final ProducedTypedReference typedMember = satisfiedType.getTypedMember(attr, null);
                if (needsCompanionDelegate(model, member)) {
                    if (member instanceof Value) {
                        final MethodDefinitionBuilder getterDelegate = makeDelegateToCompanion(iface, 
                                typedMember,
                                model.getType(),
                                PUBLIC | (attr.isDefault() ? 0 : FINAL), 
                                Collections.<TypeParameter>emptyList(), 
                                Collections.<java.util.List<ProducedType>>emptyList(),
                                typedMember.getType(), 
                                Naming.getGetterName(attr), 
                                Collections.<Parameter>emptyList(),
                                attr.getTypeErased(),
                                null);
                        classBuilder.method(getterDelegate);
                    }
                    if (member instanceof Setter) { 
                        final MethodDefinitionBuilder setterDelegate = makeDelegateToCompanion(iface, 
                                typedMember,
                                model.getType(),
                                PUBLIC | (((Setter)member).getGetter().isDefault() ? 0 : FINAL), 
                                Collections.<TypeParameter>emptyList(), 
                                Collections.<java.util.List<ProducedType>>emptyList(),
                                typeFact().getAnythingDeclaration().getType(), 
                                Naming.getSetterName(attr), 
                                Collections.<Parameter>singletonList(((Setter)member).getParameter()),
                                ((Setter) member).getTypeErased(),
                                null);
                        classBuilder.method(setterDelegate);
                    }
                    if (Decl.isValue(member) 
                            && ((Value)attr).isVariable()) {
                        // I don't *think* this can happen because although a 
                        // variable Value can be declared on an interface it 
                        // will need to we refined as a Getter+Setter on a 
                        // subinterface in order for there to be a method in a 
                        // $impl to delegate to
                        throw new BugException("assertion failed: " + member.getQualifiedNameString() + " was unexpectedly a variable value");
                    }
                }
            } else if (needsCompanionDelegate(model, member)) {
                throw new BugException("unhandled concrete interface member " + member.getQualifiedNameString() + " " + member.getClass());
            }
        }
        
        // Add $impl instances for the whole interface hierarchy
        satisfiedInterfaces.add(iface);
        for (ProducedType sat : iface.getSatisfiedTypes()) {
            sat = model.getType().getSupertype(sat.getDeclaration());
            concreteMembersFromSuperinterfaces(model, classBuilder, sat, satisfiedInterfaces);
        }
        
    }

    private java.util.List<java.util.List<ProducedType>> producedTypeParameterBounds(
            final ProducedReference typedMember, Generic subMethod) {
        java.util.List<java.util.List<ProducedType>> producedTypeParameterBounds = new ArrayList<java.util.List<ProducedType>>(subMethod.getTypeParameters().size());
        for (TypeParameter tp : subMethod.getTypeParameters()) {
            java.util.List<ProducedType> satisfiedTypes = tp.getType().getSatisfiedTypes();
            ArrayList<ProducedType> bounds = new ArrayList<>(satisfiedTypes.size());
            for (ProducedType bound : satisfiedTypes) {
                bounds.add(bound.getType().substitute(typedMember.getTypeArguments()));
            }
            producedTypeParameterBounds.add(bounds);
        }
        return producedTypeParameterBounds;
    }

    private void generateInstantiatorDelegate(
            ClassDefinitionBuilder classBuilder, ProducedType satisfiedType,
            Interface iface, Class klass, Constructor ctor, ProducedType currentType, boolean includeBody) {
        ProducedType typeMember = satisfiedType.getTypeMember(klass, klass.getType().getTypeArgumentList());
        if (ctor != null) {
            typeMember = ctor.getProducedType(typeMember, Collections.<ProducedType>emptyList());
        }
        java.util.List<TypeParameter> typeParameters = klass.getTypeParameters();
        java.util.List<Parameter> parameters = (ctor != null ? ctor.getParameterLists() : klass.getParameterLists()).get(0).getParameters();
        long flags = PUBLIC;
        if(includeBody)
            flags |= FINAL;
        else
            flags |= ABSTRACT;
        
        String instantiatorMethodName = naming.getInstantiatorMethodName(klass);
        for (Parameter param : parameters) {
            if (Strategy.hasDefaultParameterValueMethod(param)
                    && !klass.isActual()) {
                final ProducedTypedReference typedParameter = typeMember.getTypedParameter(param);
                // If that method has a defaulted parameter, 
                // we need to generate a default value method
                // which also delegates to the $impl
                final MethodDefinitionBuilder defaultValueDelegate = makeDelegateToCompanion(iface,
                        typedParameter,
                        currentType,
                        flags, 
                        typeParameters, 
                        producedTypeParameterBounds(typeMember, klass),
                        typedParameter.getType(),
                        Naming.getDefaultedParamMethodName(ctor != null ? ctor : klass, param), 
                        parameters.subList(0, parameters.indexOf(param)),
                        param.getModel().getTypeErased(),
                        null,
                        includeBody);
                classBuilder.method(defaultValueDelegate);
            }
            if (Strategy.hasDefaultParameterOverload(param)) {
                final MethodDefinitionBuilder overload = makeDelegateToCompanion(iface,
                        typeMember,
                        currentType,
                        flags, 
                        typeParameters, 
                        producedTypeParameterBounds(typeMember, klass),
                        typeMember.getType(), 
                        instantiatorMethodName, 
                        parameters.subList(0, parameters.indexOf(param)),
                        false,
                        null,
                        includeBody);
                classBuilder.method(overload);
            }
        }
        final MethodDefinitionBuilder overload = makeDelegateToCompanion(iface,
                typeMember,
                currentType,
                flags, 
                typeParameters, 
                producedTypeParameterBounds(typeMember, klass),
                typeMember.getType(), 
                instantiatorMethodName, 
                parameters,
                false,
                null,
                includeBody);
        classBuilder.method(overload);
    }

    private boolean needsCompanionDelegate(final Class model, Declaration member) {
        final boolean mostRefined;
        Declaration m = model.getMember(member.getName(), null, false);
        if (member instanceof Setter && Decl.isGetter(m)) {
            mostRefined = member.equals(((Value)m).getSetter());
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
            ProducedReference typedMember,
            ProducedType currentType,
            final long mods,
            final java.util.List<TypeParameter> typeParameters,
            final java.util.List<java.util.List<ProducedType>> producedTypeParameterBounds,
            final ProducedType methodType,
            final String methodName,
            final java.util.List<Parameter> parameters, 
            boolean typeErased,
            final String targetMethodName) {
        return makeDelegateToCompanion(iface, typedMember, currentType, mods, typeParameters,
                producedTypeParameterBounds, methodType, methodName, parameters, typeErased, targetMethodName, true);
    }
    
    /**
     * Generates a method which delegates to the companion instance $Foo$impl
     */
    private MethodDefinitionBuilder makeDelegateToCompanion(Interface iface,
            ProducedReference typedMember,
            ProducedType currentType,
            final long mods,
            final java.util.List<TypeParameter> typeParameters,
            final java.util.List<java.util.List<ProducedType>> producedTypeParameterBounds,
            final ProducedType methodType,
            final String methodName,
            final java.util.List<Parameter> parameters, 
            boolean typeErased,
            final String targetMethodName,
            boolean includeBody) {
        final MethodDefinitionBuilder concreteWrapper = MethodDefinitionBuilder.systemMethod(gen(), methodName);
        concreteWrapper.modifiers(mods);
        concreteWrapper.ignoreModelAnnotations();
        if ((mods & PRIVATE) == 0) {
            concreteWrapper.isOverride(true);
        }
        if(typeParameters != null) {
            concreteWrapper.reifiedTypeParametersFromModel(typeParameters);
        }
        Iterator<java.util.List<ProducedType>> iterator = producedTypeParameterBounds.iterator();
        if(typeParameters != null) {
            for (TypeParameter tp : typeParameters) {
                concreteWrapper.typeParameter(tp, iterator.next());
            }
        }
        
        boolean explicitReturn = false;
        Declaration member = typedMember.getDeclaration();
        ProducedType returnType = null;
        if (!isAnything(methodType) 
                || ((member instanceof Method || member instanceof Value) && !Decl.isUnboxedVoid(member)) 
                || (member instanceof Method && Strategy.useBoxedVoid((Method)member))) {
            explicitReturn = true;
            if(CodegenUtil.isHashAttribute(member)){
                // delegates for hash attributes are int
                concreteWrapper.resultType(null, make().Type(syms().intType));
                returnType = typedMember.getType();
            }else if (typedMember instanceof ProducedTypedReference) {
                ProducedTypedReference typedRef = (ProducedTypedReference) typedMember;
                // This is very much like for method refinement: if the supertype is erased -> go raw.
                // Except for some reason we only need to do it with multiple inheritance with different type
                // arguments, so let's not go overboard
                int flags = 0;
                if(CodegenUtil.hasTypeErased((TypedDeclaration)member.getRefinedDeclaration()) ||
                        CodegenUtil.hasTypeErased((TypedDeclaration)member)
                        && isInheritedTwiceWithDifferentTypeArguments(currentType, iface)){
                    flags |= AbstractTransformer.JT_RAW;
                }
                concreteWrapper.resultTypeNonWidening(currentType, typedRef, typedMember.getType(), flags);
                // FIXME: this is redundant with what we computed in the previous line in concreteWrapper.resultTypeNonWidening
                ProducedTypedReference nonWideningTypedRef = gen().nonWideningTypeDecl(typedRef, currentType);
                returnType = gen().nonWideningType(typedRef, nonWideningTypedRef);
            } else {
                concreteWrapper.resultType(null, makeJavaType((ProducedType)typedMember));
                returnType = (ProducedType) typedMember;
            }
        }
        
        ListBuffer<JCExpression> arguments = ListBuffer.<JCExpression>lb();
        if(typeParameters != null){
            for(TypeParameter tp : typeParameters){
                arguments.add(naming.makeUnquotedIdent(naming.getTypeArgumentDescriptorName(tp)));
            }
        }
        if (typedMember.getDeclaration() instanceof Constructor
                && !Decl.isDefaultConstructor((Constructor)typedMember.getDeclaration())) {
            concreteWrapper.parameter(makeConstructorNameParameter((Constructor)typedMember.getDeclaration()));
            arguments.add(naming.makeUnquotedIdent(Unfix.$name$));
        }
        for (Parameter param : parameters) {
            final ProducedTypedReference typedParameter = typedMember.getTypedParameter(param);
            ProducedType type;
            // if the supertype method itself got erased to Object, we can't do better than this
            if(gen().willEraseToObject(param.getType()) && !gen().willEraseToBestBounds(param))
                type = typeFact().getObjectDeclaration().getType();
            else
                type = typedParameter.getType();
            concreteWrapper.parameter(param, type, FINAL, 0, true);
            arguments.add(naming.makeName(param.getModel(), Naming.NA_MEMBER));
        }
        if(includeBody){
            JCExpression qualifierThis = makeUnquotedIdent(getCompanionFieldName(iface));
            // if the best satisfied type is not the one we think we implement, we may need to cast
            // our impl accessor to get the expected bounds of the qualifying type
            if(explicitReturn){
                ProducedType javaType = getBestSatisfiedType(currentType, iface);
                ProducedType ceylonType = typedMember.getQualifyingType();
                // don't even bother if the impl accessor is turned to raw because casting it to raw doesn't help
                if(!isTurnedToRaw(ceylonType)
                        // if it's exactly the same we don't need any cast
                        && !javaType.isExactly(ceylonType))
                    // this will add the proper cast to the impl accessor
                    qualifierThis = expressionGen().applyErasureAndBoxing(qualifierThis, currentType, 
                            false, true, BoxingStrategy.BOXED, ceylonType,
                            ExpressionTransformer.EXPR_WANTS_COMPANION);
            }
            JCExpression expr = make().Apply(
                    null,  // TODO Type args
                    makeSelect(qualifierThis, (targetMethodName != null) ? targetMethodName : methodName),
                    arguments.toList());
            if (isUnimplementedMemberClass(currentType, typedMember)) {
                concreteWrapper.body(makeThrowUnresolvedCompilationError(
                        // TODO encapsulate the error message
                        "formal member '"+typedMember.getDeclaration().getName()+"' of '"+iface.getName()+"' not implemented in class hierarchy"));
                current().broken();
            } else if (!explicitReturn) {
                concreteWrapper.body(gen().make().Exec(expr));
            } else {
                // deal with erasure and stuff
                BoxingStrategy boxingStrategy;
                boolean exprBoxed;
                if(member instanceof TypedDeclaration){
                    TypedDeclaration typedDecl = (TypedDeclaration) member;
                    exprBoxed = !CodegenUtil.isUnBoxed(typedDecl);
                    boxingStrategy = CodegenUtil.getBoxingStrategy(typedDecl);
                }else{
                    // must be a class or interface
                    exprBoxed = true;
                    boxingStrategy = BoxingStrategy.UNBOXED;
                }
                // if our interface impl is turned to raw, the whole call will be seen as raw by javac, so we may need
                // to force an additional cast
                if(isTurnedToRaw(typedMember.getQualifyingType())
                        // see note in BoxingVisitor.visit(QualifiedMemberExpression) about mixin super calls and variant type args
                        // in invariant locations
                        || needsRawCastForMixinSuperCall(iface, methodType))
                    typeErased = true;
                expr = gen().expressionGen().applyErasureAndBoxing(expr, methodType, typeErased, 
                        exprBoxed, boxingStrategy,
                        returnType, 0);
                concreteWrapper.body(gen().make().Return(expr));
            }
        }
        return concreteWrapper;
    }

    private boolean isUnimplementedMemberClass(ProducedType currentType, ProducedReference typedMember) {
        if (typedMember instanceof ProducedType
                && currentType.getDeclaration() instanceof Class) {// member class
            for (ProducedReference formal : ((Class)currentType.getDeclaration()).getUnimplementedFormals()) {
                if (formal.getDeclaration().equals(typedMember.getDeclaration())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isInheritedTwiceWithDifferentTypeArguments(ProducedType currentType, Interface iface) {
        ProducedType firstSatisfiedType = getFirstSatisfiedType(currentType, iface);
        ProducedType supertype = currentType.getSupertype(iface);
        return !supertype.isExactly(firstSatisfiedType);
    }

    private Boolean hasImpl(Interface iface) {
        if (gen().willEraseToObject(iface.getType())) {
            return false;
        }
        // Java interfaces never have companion classes
        if (iface instanceof LazyInterface
            && !((LazyInterface)iface).isCeylon()){
            return false;
        }
        return CodegenUtil.isCompanionClassNeeded(iface);
    }

    private void transformInstantiateCompanions(
            ClassDefinitionBuilder classBuilder, 
            Class model, 
            Interface iface,
            ProducedType satisfiedType) {
        at(null);
        
        // make sure we get the first type that java will find when it looks up
        final ProducedType bestSatisfiedType = getBestSatisfiedType(model.getType(), iface);
        
        classBuilder.getInitBuilder().init(makeCompanionInstanceAssignment(model, iface, satisfiedType));
        
        classBuilder.field(PROTECTED | FINAL, getCompanionFieldName(iface), 
                makeJavaType(bestSatisfiedType, AbstractTransformer.JT_COMPANION | JT_SATISFIES), null, false,
                makeAtIgnore());

        classBuilder.method(makeCompanionAccessor(iface, bestSatisfiedType, model, true));
    }

    /**
     * Returns the companion instances assignment expression used in the constructor,
     * e.g.
     * <pre>
     * this.$ceylon$language$Enumerable$this$ = new .ceylon.language.Enumerable$impl<.com.redhat.ceylon.compiler.java.test.structure.klass.SerializableEnumerable>(.com.redhat.ceylon.compiler.java.test.structure.klass.SerializableEnumerable.$TypeDescriptor$, this);
     * </pre>
     * @param classBuilder 
     * @return 
     */
    private JCExpressionStatement makeCompanionInstanceAssignment(final Class model,
            final Interface iface, final ProducedType satisfiedType) {
        
        final ProducedType bestSatisfiedType = getBestSatisfiedType(model.getType(), iface);
        
        JCExpression containerInstance = null;
        if(!Decl.isToplevel(iface) && !Decl.isLocal(iface)){
            // if it's a member type we need to qualify the new instance with its $impl container
            ClassOrInterface interfaceContainer = Decl.getClassOrInterfaceContainer(iface, false);
            if(interfaceContainer instanceof Interface){
                ClassOrInterface modelContainer = model;
                while((modelContainer = Decl.getClassOrInterfaceContainer(modelContainer, false)) != null
                        && modelContainer.getType().getSupertype(interfaceContainer) == null){
                    // keep searching
                }
                if (modelContainer == null) {
                    throw new BugException("Could not find container that satisfies interface "
                        + iface.getQualifiedNameString() + " to find qualifying instance for companion instance for "
                        + model.getQualifiedNameString());
                }
                // if it's an interface we just qualify it properly
                if(modelContainer instanceof Interface){
                    JCExpression containerType = makeJavaType(modelContainer.getType(), JT_COMPANION | JT_SATISFIES | JT_RAW);
                    containerInstance = makeSelect(containerType, "this");
                }else{
                    // it's a class: find the right field used for the interface container impl
                    String containerFieldName = getCompanionFieldName((Interface)interfaceContainer);
                    JCExpression containerType = makeJavaType(modelContainer.getType(), JT_SATISFIES);
                    containerInstance = makeSelect(makeSelect(containerType, "this"), containerFieldName);
                }
            }
        }
        
        List<JCExpression> state = List.nil();
        
        // pass all reified type info to the constructor
        for(JCExpression t : makeReifiedTypeArguments(satisfiedType)){
            state = state.append(t);
        }
        // pass the instance of this
        state = state.append( expressionGen().applyErasureAndBoxing(naming.makeThis(), 
                model.getType(), false, true, BoxingStrategy.BOXED, 
                bestSatisfiedType, ExpressionTransformer.EXPR_FOR_COMPANION));
        
        final JCExpression ifaceImplType;
        if(!Decl.isToplevel(iface) && !Decl.isLocal(iface)
                && Decl.getClassOrInterfaceContainer(iface, false) instanceof Interface){
            ifaceImplType = makeJavaType(bestSatisfiedType, JT_COMPANION | JT_SATISFIES | JT_NON_QUALIFIED);
        } else {
            ifaceImplType = makeJavaType(bestSatisfiedType, JT_COMPANION | JT_SATISFIES);
        }
        
        JCExpression newInstance = make().NewClass(containerInstance, 
                null,
                ifaceImplType,
                state,
                null);
        
        JCExpressionStatement companionInstanceAssign = make().Exec(make().Assign(
                makeSelect("this", getCompanionFieldName(iface)),// TODO Use qualified name for quoting? 
                newInstance));
        return companionInstanceAssign;
    }
    
    private MethodDefinitionBuilder makeCompanionAccessor(Interface iface, ProducedType satisfiedType, 
            Class currentType, boolean forImplementor) {
        MethodDefinitionBuilder thisMethod = MethodDefinitionBuilder.systemMethod(
                this, naming.getCompanionAccessorName(iface));
        thisMethod.noModelAnnotations();
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
            thisMethod.ignoreModelAnnotations();
        }
        thisMethod.modifiers(PUBLIC);
        if (forImplementor) {
            thisMethod.body(make().Return(naming.makeCompanionFieldName(iface)));
        } else {
            thisMethod.noBody();
        }
        return thisMethod;
    }

    private ProducedType getBestSatisfiedType(ProducedType currentType, Interface iface) {
        ProducedType refinedSuperType = currentType.getSupertype(iface);
        ProducedType firstSatisfiedType = getFirstSatisfiedType(currentType, iface);
        // in the very special case of the first satisfied type having type arguments erased to Object and
        // the most refined one having free type parameters, we prefer the one with free type parameters
        // because Java prefers it and it's in range with what nonWideningType does
        Map<TypeParameter, ProducedType> refinedTAs = refinedSuperType.getTypeArguments();
        Map<TypeParameter, ProducedType> firstTAs = firstSatisfiedType.getTypeArguments();
        for(TypeParameter tp : iface.getTypeParameters()){
            ProducedType refinedTA = refinedTAs.get(tp);
            ProducedType firstTA = firstTAs.get(tp);
            if(willEraseToObject(firstTA) && isTypeParameter(refinedTA))
                return refinedSuperType;
        }
        return firstSatisfiedType;
    }

    private ProducedType getFirstSatisfiedType(ProducedType currentType, Interface iface) {
        ProducedType found = null;
        TypeDeclaration currentDecl = currentType.getDeclaration();
        if (Decl.equal(currentDecl, iface)) {
            return currentType;
        }
        if(currentDecl.getExtendedType() != null){
            ProducedType supertype = currentType.getSupertype(currentDecl.getExtendedTypeDeclaration());
            found = getFirstSatisfiedType(supertype, iface);
            if(found != null)
                return found;
        }
        for(ProducedType superInterfaceType : currentType.getSatisfiedTypes()){
            found = getFirstSatisfiedType(superInterfaceType, iface);
            if(found != null)
                return found;
        }
        return null;
    }

    private void buildCompanion(final Tree.ClassOrInterface def,
            final Interface model, ClassDefinitionBuilder classBuilder) {
        at(def);
        // Give the $impl companion a $this field...
        classBuilder.getCompanionBuilder2(model);
    }

    private List<JCAnnotation> makeLocalContainerPath(Interface model) {
        List<String> path = List.nil();
        Scope container = model.getContainer();
        while(container != null
                && container instanceof Package == false){
            if(container instanceof Declaration)
                path = path.prepend(((Declaration) container).getPrefixedName());
            container = container.getContainer();
        }
        return makeAtLocalContainer(path, model.isCompanionClassNeeded() ? model.getJavaCompanionClassName() : null);
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
            
            if (Decl.isValue(decl) || Decl.isGetter(decl)) {
                // Now build a "fake" declaration for the attribute
                Tree.AttributeDeclaration attrDecl = new Tree.AttributeDeclaration(null);
                attrDecl.setDeclarationModel((Value)decl);
                attrDecl.setIdentifier(expr.getIdentifier());
                attrDecl.setScope(op.getScope());
                attrDecl.setSpecifierOrInitializerExpression(op.getSpecifierExpression());
                attrDecl.setAnnotationList(makeShortcutRefinementAnnotationTrees());
                
                // Make sure the boxing information is set correctly
                BoxingDeclarationVisitor v = new CompilerBoxingDeclarationVisitor(this);
                v.visit(attrDecl);
                
                // Generate the attribute
                transform(attrDecl, classBuilder);
            } else if (decl instanceof Method) {
                // Now build a "fake" declaration for the method
                Tree.MethodDeclaration methDecl = new Tree.MethodDeclaration(null);
                Method m = (Method)decl;
                methDecl.setDeclarationModel(m);
                methDecl.setIdentifier(expr.getIdentifier());
                methDecl.setScope(op.getScope());
                methDecl.setAnnotationList(makeShortcutRefinementAnnotationTrees());
                
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

                java.util.List<Tree.ParameterList> parameterListTrees = null;
                if(op.getBaseMemberExpression() instanceof Tree.ParameterizedExpression){
                    parameterListTrees = ((Tree.ParameterizedExpression)op.getBaseMemberExpression()).getParameterLists();
                }

                int plIndex = 0;
                // copy from formal declaration
                for (ParameterList pl : m.getParameterLists()) {
                    Tree.ParameterList parameterListTree = null;
                    if(parameterListTrees != null)
                        parameterListTree = parameterListTrees.get(plIndex++);
                    Tree.ParameterList tpl = new Tree.ParameterList(null);
                    tpl.setModel(pl);
                    int pIndex = 0;
                    for (Parameter p : pl.getParameters()) {
                        Tree.Parameter parameterTree = null;
                        if(parameterListTree != null)
                            parameterTree = parameterListTree.getParameters().get(pIndex++);
                        Tree.Parameter tp = null;
                        if (p.getModel() instanceof Value) {
                            Tree.ValueParameterDeclaration tvpd = new Tree.ValueParameterDeclaration(null);
                            if(parameterTree != null)
                                tvpd.setTypedDeclaration(((Tree.ParameterDeclaration)parameterTree).getTypedDeclaration());
                            tvpd.setParameterModel(p);
                            tp = tvpd;
                        } else if (p.getModel() instanceof Method) {
                            Tree.FunctionalParameterDeclaration tfpd = new Tree.FunctionalParameterDeclaration(null);
                            if(parameterTree != null)
                                tfpd.setTypedDeclaration(((Tree.ParameterDeclaration)parameterTree).getTypedDeclaration());
                            tfpd.setParameterModel(p);
                            tp = tfpd;
                        } else {
                            throw BugException.unhandledDeclarationCase(p.getModel());
                        }
                        tp.setScope(p.getDeclaration().getContainer());
                        //tp.setIdentifier(makeIdentifier(p.getName()));
                        tpl.addParameter(tp);
                    }
                    methDecl.addParameterList(tpl);
                }
                
                // Make sure the boxing information is set correctly
                BoxingDeclarationVisitor v = new CompilerBoxingDeclarationVisitor(this);
                v.visit(methDecl);
                
                // Generate the method
                classBuilder.method(methDecl, Errors.GENERATE);
            }
        } else {
            // Normal case, just generate the specifier statement
            result = result.append(expressionGen().transform(op));
        }
        
        Tree.Term term = op.getBaseMemberExpression();
        if (term instanceof Tree.BaseMemberExpression) {
            Tree.BaseMemberExpression bme = (Tree.BaseMemberExpression)term;
            DeferredSpecification ds = statementGen().getDeferredSpecification(bme.getDeclaration());
            if (ds != null && needsInnerSubstitution(term.getScope(), bme.getDeclaration())){
                result = result.append(ds.openInnerSubstitution());
            }
        }
        return result;
    }

    private AnnotationList makeShortcutRefinementAnnotationTrees() {
        AnnotationList annotationList = new AnnotationList(null);
        
        Tree.Annotation shared = new Tree.Annotation(null);
        Tree.BaseMemberExpression sharedPrimary = new Tree.BaseMemberExpression(null);
        sharedPrimary.setDeclaration(typeFact().getLanguageModuleDeclaration("shared"));
        shared.setPrimary(sharedPrimary);
        annotationList.addAnnotation(shared);
        
        Tree.Annotation actual = new Tree.Annotation(null);
        Tree.BaseMemberExpression actualPrimary = new Tree.BaseMemberExpression(null);
        actualPrimary.setDeclaration(typeFact().getLanguageModuleDeclaration("actual"));
        actual.setPrimary(actualPrimary);
        annotationList.addAnnotation(actual);
        
        return annotationList;
    }

    /**
     * We only need an inner substitution if we're within that substitution's scope
     */
    private boolean needsInnerSubstitution(Scope scope, Declaration declaration) {
        while(scope != null && scope instanceof Package == false){
            if(scope instanceof ControlBlock){
                Set<Value> specifiedValues = ((ControlBlock) scope).getSpecifiedValues();
                if(specifiedValues != null && specifiedValues.contains(declaration))
                    return true;
            }
            scope = scope.getScope();
        }
        return false;
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
        if (!lazy && 
                (concrete || 
                        (!Decl.isFormal(decl) 
                                && createField))) {
            ProducedTypedReference typedRef = getTypedReference(model);
            ProducedTypedReference nonWideningTypedRef = nonWideningTypeDecl(typedRef);
            ProducedType nonWideningType = nonWideningType(typedRef, nonWideningTypedRef);
            
            if (Decl.isIndirect(decl)) {
                attrName = Naming.getAttrClassName(model, 0);
                nonWideningType = getGetterInterfaceType(model);
            }
            
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
            // (makeAttributeForValueParameter() or makeMethodForFunctionalParameter() 
            //  does it in those cases)
            if (parameter == null
                    || parameter.isHidden()) {
                if (concrete) {
                    classBuilder.getCompanionBuilder((TypeDeclaration)model.getContainer()).field(modifiers, attrName, type, initialValue, !useField);
                } else {
                    
                    List<JCAnnotation> annos = makeAtIgnore().prependList(expressionGen().transformAnnotations(false, OutputElement.FIELD, decl));
                    if (Decl.hasAbstractConstructor((Class)decl.getDeclarationModel().getContainer())) {
                        annos = annos.prependList(makeAtNoInitCheck());
                    }
                    // fields should be ignored, they are accessed by the getters
                    classBuilder.field(modifiers, attrName, type, initialValue, !useField, annos);
                    if (model.isLate()) {
                        classBuilder.field(PRIVATE | Flags.VOLATILE, Naming.getInitializationFieldName(attrName), 
                                make().Type(syms().booleanType), 
                                make().Literal(false), false, makeAtIgnore());
                    }
                }        
            }
            
            // A shared attribute might be initialized in a for statement, so
            // we might need a def-assignment subst for it
            List<JCAnnotation> annots = makeJavaTypeAnnotations(decl.getDeclarationModel());
            JCStatement outerSubs = statementGen().openOuterSubstitutionIfNeeded(
                    decl.getDeclarationModel(), model.getType(), annots, 0);
            if (outerSubs != null) {
                classBuilder.getInitBuilder().init(outerSubs);
            }
        }

        boolean withinInterface = Decl.withinInterface(decl);
        if (useField || withinInterface || lazy) {
            if (!withinInterface || model.isShared()) {
                // Generate getter in main class or interface (when shared)
                classBuilder.attribute(makeGetter(decl, false, lazy));
            }
            if (withinInterface && lazy) {
                // Generate getter in companion class
                classBuilder.getCompanionBuilder((Interface)decl.getDeclarationModel().getContainer()).attribute(makeGetter(decl, true, lazy));
            }
            if (Decl.isVariable(decl) || Decl.isLate(decl)) {
                if (!withinInterface || model.isShared()) {
                    // Generate setter in main class or interface (when shared)
                    classBuilder.attribute(makeSetter(decl, false, lazy));
                }
                if (withinInterface && lazy) {
                    // Generate setter in companion class
                    classBuilder.getCompanionBuilder((Interface)decl.getDeclarationModel().getContainer()).attribute(makeSetter(decl, true, lazy));
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
            .modifiers(transformAttributeGetSetDeclFlags(decl.getDeclarationModel(), forCompanion));
        
        // companion class members are never actual no matter what the Declaration says
        if(forCompanion)
            builder.notActual();
        
        if (Decl.withinClass(decl) || forCompanion) {
            JCBlock setterBlock = makeSetterBlock(decl.getDeclarationModel(), decl.getBlock(), decl.getSpecifierExpression());
            builder.setterBlock(setterBlock);
        } else {
            builder.isFormal(true);
        }
        builder.userAnnotationsSetter(expressionGen().transformAnnotations(true, OutputElement.SETTER, decl));
        return builder;
    }

    public AttributeDefinitionBuilder transform(AttributeGetterDefinition decl, boolean forCompanion) {
        if (Strategy.onlyOnCompanion(decl.getDeclarationModel()) && !forCompanion) {
            return null;
        }
        String name = decl.getIdentifier().getText();
        //expressionGen().transform(decl.getAnnotationList());
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
        builder.userAnnotations(expressionGen().transformAnnotations(true, OutputElement.GETTER, decl));
        return builder;    
    }

    private int transformDeclarationSharedFlags(Declaration decl){
        return Decl.isShared(decl) && !Decl.isAncestorLocal(decl) ? PUBLIC : 0;
    }
    
    private int transformClassDeclFlags(ClassOrInterface cdecl) {
        int result = 0;

        result |= transformDeclarationSharedFlags(cdecl);
        // aliases cannot be abstract, especially since they're just placeholders
        result |= (cdecl instanceof Class) && (cdecl.isAbstract() || cdecl.isFormal()) && !cdecl.isAlias() ? ABSTRACT : 0;
        result |= (cdecl instanceof Interface) ? INTERFACE : 0;
        // aliases are always final placeholders, final classes are also final
        result |= (cdecl instanceof Class) && (cdecl.isAlias() || cdecl.isFinal())  ? FINAL : 0;

        return result;
    }
    
    private int transformConstructorDeclFlags(ClassOrInterface cdecl) {
        return transformDeclarationSharedFlags(cdecl);
    }
    private int transformConstructorDeclFlags(Constructor ctor) {
        return Decl.isShared(ctor) 
                && !Decl.isAncestorLocal(ctor) 
                && !ctor.isAbstract() ? PUBLIC : PRIVATE;
    }

    private int transformTypeAliasDeclFlags(TypeAlias decl) {
        int result = 0;

        result |= transformDeclarationSharedFlags(decl);
        result |= FINAL;

        return result;
    }
    
    private int transformMethodDeclFlags(Method def) {
        int result = 0;

        if (def.isToplevel()) {
            result |= def.isShared() ? PUBLIC : 0;
            result |= STATIC;
        } else if (Decl.isLocalNotInitializer(def)) {
            result |= def.isShared() ? PUBLIC : 0;
        } else {
            result |= def.isShared() ? PUBLIC : PRIVATE;
            result |= def.isFormal() && !def.isDefault() ? ABSTRACT : 0;
            result |= !(def.isFormal() || def.isDefault() || def.getContainer() instanceof Interface) ? FINAL : 0;
        }

        return result;
    }
    
    private int transformAttributeFieldDeclFlags(Tree.AttributeDeclaration cdecl) {
        int result = 0;

        result |= Decl.isVariable(cdecl) || Decl.isLate(cdecl) ? 0 : FINAL;
        result |= PRIVATE;
        
        return result;
    }

    private int transformLocalDeclFlags(Tree.AttributeDeclaration cdecl) {
        int result = 0;

        result |= Decl.isVariable(cdecl) ? 0 : FINAL;

        return result;
    }

    /**
     * Returns the modifier flags to be used for the getter & setter for the 
     * given attribute-like declaration.  
     * @param tdecl attribute-like declaration (Value, Getter, Parameter etc)
     * @param forCompanion Whether the getter/setter is on a companion type
     * @return The modifier flags.
     */
    int transformAttributeGetSetDeclFlags(TypedDeclaration tdecl, boolean forCompanion) {
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

    private AttributeDefinitionBuilder makeGetterOrSetter(Tree.AttributeDeclaration decl, boolean forCompanion, boolean lazy, 
                                                          AttributeDefinitionBuilder builder, boolean isGetter) {
        at(decl);
        if (forCompanion || lazy) {
            SpecifierOrInitializerExpression specOrInit = decl.getSpecifierOrInitializerExpression();
            if (specOrInit != null) {
                HasErrorException error = errors().getFirstExpressionErrorAndMarkBrokenness(specOrInit.getExpression());
                if (error != null) {
                    builder.getterBlock(make().Block(0, List.<JCStatement>of(this.makeThrowUnresolvedCompilationError(error))));
                } else {
                    Value declarationModel = decl.getDeclarationModel();
                    ProducedTypedReference typedRef = getTypedReference(declarationModel);
                    ProducedTypedReference nonWideningTypedRef = nonWideningTypeDecl(typedRef);
                    ProducedType nonWideningType = nonWideningType(typedRef, nonWideningTypedRef);
                    
                    JCExpression expr = expressionGen().transformExpression(specOrInit.getExpression(), 
                            CodegenUtil.getBoxingStrategy(declarationModel), 
                            nonWideningType);
                    expr = convertToIntIfHashAttribute(declarationModel, expr);
                    builder.getterBlock(make().Block(0, List.<JCStatement>of(make().Return(expr))));
                }
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
    
    private AttributeDefinitionBuilder makeGetter(Tree.AttributeDeclaration decl, boolean forCompanion, boolean lazy) {
        at(decl);
        String attrName = decl.getIdentifier().getText();
        AttributeDefinitionBuilder getter = AttributeDefinitionBuilder
            .getter(this, attrName, decl.getDeclarationModel());
        if(!decl.getDeclarationModel().isInterfaceMember()
                || (decl.getDeclarationModel().isShared() ^ forCompanion))
            getter.userAnnotations(expressionGen().transformAnnotations(true, OutputElement.GETTER, decl));
        else
            getter.ignoreAnnotations();
        
        if (Decl.isIndirect(decl)) {
            getter.getterBlock(generateIndirectGetterBlock(decl.getDeclarationModel()));
        }
        
        return makeGetterOrSetter(decl, forCompanion, lazy, getter, true);
    }

    private JCTree.JCBlock generateIndirectGetterBlock(Value v) {
        JCTree.JCExpression returnExpr;
        returnExpr = naming.makeQualIdent(naming.makeName(v, Naming.NA_WRAPPER), "get_");
        returnExpr = make().Apply(null, returnExpr, List.<JCExpression>nil());
        JCReturn returnValue = make().Return(returnExpr);
        List<JCStatement> stmts = List.<JCTree.JCStatement>of(returnValue);   
        JCTree.JCBlock block = make().Block(0L, stmts);
        return block;
    }

    private AttributeDefinitionBuilder makeSetter(Tree.AttributeDeclaration decl, boolean forCompanion, boolean lazy) {
        at(decl);
        String attrName = decl.getIdentifier().getText();
        AttributeDefinitionBuilder setter = AttributeDefinitionBuilder.setter(this, attrName, decl.getDeclarationModel());
        setter.userAnnotationsSetter(expressionGen().transformAnnotations(false, OutputElement.SETTER, decl));
        return makeGetterOrSetter(decl, forCompanion, lazy, setter, false);
    }

    public List<JCTree> transformWrappedMethod(Tree.AnyMethod def, TransformationPlan plan) {
        final Method model = def.getDeclarationModel();
        if (model.isParameter()) {
            return List.nil();
        }
        naming.clearSubstitutions(model);
        // Generate a wrapper class for the method
        String name = def.getIdentifier().getText();
        ClassDefinitionBuilder builder = ClassDefinitionBuilder.methodWrapper(this, name, Decl.isShared(def));
        
        if (Decl.isAnnotationConstructor(def)) {
            AnnotationInvocation ai = ((AnnotationInvocation)def.getDeclarationModel().getAnnotationConstructor());
            if (ai != null) {
                builder.annotations(List.of(makeAtAnnotationInstantiation(ai)));
                builder.annotations(makeExprAnnotations(def, ai));
            }
        }
        
        builder.methods(classGen().transform(def, plan, builder));
        
        // Toplevel method
        if (Strategy.generateMain(def)) {
            // Add a main() method
            builder.method(makeMainForFunction(model));
        }
        
        if(Decl.isLocal(model) || Decl.isToplevel(model)){
            builder.annotations(makeAtLocalDeclarations(def));
        }
        if(Decl.isLocal(model)){
            builder.annotations(makeAtLocalDeclaration(model.getQualifier(), false));
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
        
        //if (Decl.isAnnotationConstructor(def)) {
            //result = result.prependList(transformAnnotationConstructorType(def));
        //}
        return result;
    }

    /**
     * Make the {@code @*Exprs} annotations to hold the literal arguments 
     * to the invocation.
     */
    private List<JCAnnotation> makeExprAnnotations(Tree.AnyMethod def,
            AnnotationInvocation ai) {
        AnnotationInvocation ctor = (AnnotationInvocation)def.getDeclarationModel().getAnnotationConstructor();
        return ai.makeExprAnnotations(expressionGen(), ctor, List.<AnnotationFieldName>nil());
    }
    
    public JCAnnotation makeAtAnnotationInstantiation(AnnotationInvocation invocation) {
        return invocation.encode(this, ListBuffer.<JCExpression>lb());
    }

    private MethodDefinitionBuilder makeAnnotationMethod(Tree.Parameter parameter) {
        Parameter parameterModel = parameter.getParameterModel();
        JCExpression type = transformAnnotationMethodType(parameter);
        JCExpression defaultValue = parameterModel.isDefaulted() ? transformAnnotationParameterDefault(parameter) : null;
        MethodDefinitionBuilder mdb = MethodDefinitionBuilder.method(this, parameterModel.getModel(), Naming.NA_ANNOTATION_MEMBER);
        if (isMetamodelReference(parameterModel.getType())
                || 
                (typeFact().isIterableType(parameterModel.getType())
                && isMetamodelReference(typeFact().getIteratedType(parameterModel.getType())))) {
            mdb.modelAnnotations(List.of(make().Annotation(make().Type(syms().ceylonAtDeclarationReferenceType), 
                    List.<JCExpression>nil())));
        } else if (Decl.isEnumeratedTypeWithAnonCases(parameterModel.getType())
                || 
                (typeFact().isIterableType(parameterModel.getType())
                        && Decl.isEnumeratedTypeWithAnonCases(typeFact().getIteratedType(parameterModel.getType())))) {
            mdb.modelAnnotations(List.of(make().Annotation(make().Type(syms().ceylonAtEnumerationReferenceType), 
                    List.<JCExpression>nil())));
        }
        mdb.modifiers(PUBLIC | ABSTRACT);
        mdb.resultType(null, type);
        mdb.defaultValue(defaultValue);
        mdb.noBody();
        return mdb;
    }

    public List<MethodDefinitionBuilder> transform(Tree.AnyMethod def, TransformationPlan plan, ClassDefinitionBuilder classBuilder) {
        if (def.getDeclarationModel().isParameter()) {
            return List.nil();
        }
        if (plan instanceof ThrowerMethod) {
            addRefinedThrowerMethod(classBuilder, 
                    plan.getErrorMessage().getMessage(), 
                    (Class)def.getDeclarationModel().getContainer(),
                    (Method)def.getDeclarationModel().getRefinedDeclaration());
            return List.<MethodDefinitionBuilder>nil();
        }
        // Transform the method body of the 'inner-most method'
        boolean prevSyntheticClassBody = expressionGen().withinSyntheticClassBody(Decl.isMpl(def.getDeclarationModel())
                || Decl.isLocalNotInitializer(def)
                || expressionGen().isWithinSyntheticClassBody());
        List<JCStatement> body = transformMethodBody(def);
        expressionGen().withinSyntheticClassBody(prevSyntheticClassBody);
        return transform(def, classBuilder, body);
    }

    List<MethodDefinitionBuilder> transform(Tree.AnyMethod def,
            ClassDefinitionBuilder classBuilder, List<JCStatement> body) {
        final Method model = def.getDeclarationModel();
        
        List<MethodDefinitionBuilder> result = List.<MethodDefinitionBuilder>nil();
        if (!Decl.withinInterface(model)) {
            // Transform to the class
            boolean refinedResultType = !model.getType().isExactly(
                    ((TypedDeclaration)model.getRefinedDeclaration()).getType());
            result = transformMethod(def, 
                    true,
                    true,
                    true,
                    transformMplBodyUnlessSpecifier(def, model, body),
                    refinedResultType 
                    && !Decl.withinInterface(model.getRefinedDeclaration())? new DaoSuper() : new DaoThis(def, def.getParameterLists().get(0)),
                    !Strategy.defaultParameterMethodOnSelf(model));
        } else {// Is within interface
            // Transform the definition to the companion class, how depends
            // on what kind of method it is
            List<MethodDefinitionBuilder> companionDefs;
            if (def instanceof Tree.MethodDeclaration) {
                final SpecifierExpression specifier = ((Tree.MethodDeclaration) def).getSpecifierExpression();
                if (specifier == null) {
                    // formal or abstract 
                    // (still need overloads and DPMs on the companion)
                    companionDefs = transformMethod(def,  
                            false,
                            true,
                            true,
                            null,
                            new DaoCompanion(def, def.getParameterLists().get(0)),
                            false);   
                } else {
                    companionDefs = transformMethod(def,
                            true,
                            false,
                            !model.isShared(),
                            transformMplBodyUnlessSpecifier(def, model, body),
                            new DaoCompanion(def, def.getParameterLists().get(0)),
                            false);
                }
            } else if (def instanceof Tree.MethodDefinition) {
                companionDefs = transformMethod(def,  
                        true,
                        false,
                        !model.isShared(),
                        transformMplBodyUnlessSpecifier(def, model, body),
                        new DaoCompanion(def, def.getParameterLists().get(0)),
                        false);
            } else {
                throw BugException.unhandledNodeCase(def);
            }
            if(!companionDefs.isEmpty())
                classBuilder.getCompanionBuilder((TypeDeclaration)model.getContainer())
                    .methods(companionDefs);
            
            // Transform the declaration to the target interface
            // but only if it's shared
            if (Decl.isShared(model)) {
                result = transformMethod(def, 
                        true,
                        true,
                        true,
                        null,
                        daoAbstract,
                        !Strategy.defaultParameterMethodOnSelf(model));
            }
        }
        return result;
    }

    
    /**
     * Transforms a method, generating default argument overloads and 
     * default value methods
     * @param def The method
     * @param model The method model
     * @param methodName The method name
     * @param transformMethod Whether the method itself should be transformed.
     * @param actualAndAnnotations Whether the method itself is actual and has 
     * model annotations
     * @param body The body of the method (or null for an abstract method)
     * @param daoTransformation The default argument overload transformation
     * @param transformDefaultValues Whether to generate default value methods
     * @param defaultValuesBody Whether the default value methods should have a body
     */
    private List<MethodDefinitionBuilder> transformMethod(Tree.AnyMethod def,
            boolean transformMethod, boolean actual, boolean includeAnnotations, List<JCStatement> body, 
            DaoBody daoTransformation, 
            boolean defaultValuesBody) {
        return transformMethod(def.getDeclarationModel(), 
                def.getTypeParameterList(),
                def,
                def.getParameterLists(),
                def,
                transformMethod, actual, includeAnnotations, body,
                daoTransformation,
                defaultValuesBody);
    }
    
    private List<MethodDefinitionBuilder> transformMethod(
            final Method methodModel,
            Tree.TypeParameterList typeParameterList,
            Tree.AnyMethod node, 
            java.util.List<Tree.ParameterList> parameterLists,
            Tree.Declaration annotated,
            boolean transformMethod, boolean actual, boolean includeAnnotations, List<JCStatement> body, 
            DaoBody daoTransformation, 
            boolean defaultValuesBody) {
        
        ListBuffer<MethodDefinitionBuilder> lb = ListBuffer.<MethodDefinitionBuilder>lb();
        Declaration refinedDeclaration = methodModel.getRefinedDeclaration();
        
        final MethodDefinitionBuilder methodBuilder = MethodDefinitionBuilder.method(this, methodModel);
        
        // do the reified type param arguments
        if (gen().supportsReified(methodModel)) {
            methodBuilder.reifiedTypeParameters(methodModel.getTypeParameters());
        }
        
        if (methodModel.getParameterLists().size() > 1) {
            methodBuilder.mpl(methodModel.getParameterLists());
        }
        
        boolean hasOverloads = false;
        Tree.ParameterList parameterList = parameterLists.get(0);
        for (final Tree.Parameter parameter : parameterList.getParameters()) {
            Parameter parameterModel = parameter.getParameterModel();
            List<JCAnnotation> annotations = null;
            if (includeAnnotations){
                Tree.TypedDeclaration typedDeclaration = Decl.getMemberDeclaration(annotated, parameter);
                // it can be null in the case of specifier refinement with no param list, but which we still optimise
                // to a real method
                // f = function(Integer param) => 2;
                if(typedDeclaration != null)
                    annotations = expressionGen().transformAnnotations(true, OutputElement.PARAMETER, typedDeclaration);
            }
            int flags = 0;
            if (rawParameters(methodModel)) {
                flags |= JT_RAW;
            }
            methodBuilder.parameter(parameterModel, annotations, flags, true);

            if (Strategy.hasDefaultParameterValueMethod(parameterModel)
                    || Strategy.hasDefaultParameterOverload(parameterModel)) {
                if (Decl.equal(refinedDeclaration, methodModel)
                        || (!Decl.withinInterface(methodModel) && body != null)
                        || Decl.withinInterface(methodModel) && daoTransformation instanceof DaoCompanion == false) {
                    
                    if (daoTransformation != null && (daoTransformation instanceof DaoCompanion == false || body != null)) {
                        DaoBody daoTrans = (body == null) ? daoAbstract : new DaoThis(node, parameterList);
                        
                        MethodDefinitionBuilder overloadedMethod = new DefaultedArgumentMethod(daoTrans, MethodDefinitionBuilder.method(this, methodModel), methodModel)
                            .makeOverload(
                                parameterList.getModel(),
                                parameter.getParameterModel(),
                                methodModel.getTypeParameters());
                        overloadedMethod.location(null);
                        lb.append(overloadedMethod);
                    }
                    
                    if (Decl.equal(refinedDeclaration, methodModel)
                            && Strategy.hasDefaultParameterValueMethod(parameterModel)) {
                        lb.append(makeParamDefaultValueMethod(defaultValuesBody, methodModel, parameterList, parameter));
                    }
                }
                
                hasOverloads = true;
            }
        }

        // Determine if we need to generate a "canonical" method
        boolean createCanonical = hasOverloads
                && Decl.withinClassOrInterface(methodModel)
                && body != null;
        
        if (createCanonical) {
            // Creates the private "canonical" method containing the actual body
            MethodDefinitionBuilder canonicalMethod = new CanonicalMethod(daoTransformation, methodModel, body)
                .makeOverload(
                    parameterList.getModel(),
                    null,
                    methodModel.getTypeParameters());
            lb.append(canonicalMethod);
        }
        
        if (transformMethod) {
            methodBuilder.modifiers(transformMethodDeclFlags(methodModel));
            if (actual) {
                methodBuilder.isOverride(methodModel.isActual());
            }
            if (includeAnnotations) {
                methodBuilder.userAnnotations(expressionGen().transformAnnotations(true, OutputElement.METHOD, annotated));
                methodBuilder.modelAnnotations(methodModel.getAnnotations());
            } else {
                methodBuilder.ignoreModelAnnotations();
            }
            methodBuilder.resultType(methodModel, 0);
            copyTypeParameters(methodModel, methodBuilder);
            
            if (createCanonical) {
                // Creates method that redirects to the "canonical" method containing the actual body
                MethodDefinitionBuilder overloadedMethod = new CanonicalMethod(new DaoThis(node, parameterList), methodBuilder, methodModel)
                    .makeOverload(
                        parameterList.getModel(),
                        null,
                        methodModel.getTypeParameters());
                lb.append(overloadedMethod);
            } else {
                if (body != null) {
                    // Construct the outermost method using the body we've built so far
                    methodBuilder.body(body);
                } else {
                    methodBuilder.noBody();
                }
                lb.append(methodBuilder);
            }
        }
        return lb.toList();
    }

    List<JCStatement> transformMplBodyUnlessSpecifier(Tree.AnyMethod def,
            Method model,
            List<JCStatement> body) {
        if (def instanceof Tree.MethodDeclaration) {
            Tree.SpecifierExpression specifier = ((Tree.MethodDeclaration)def).getSpecifierExpression();
            if (specifier == null) {
                return body;
            } else if (!(specifier instanceof Tree.LazySpecifierExpression)) {
                if (!CodegenUtil.canOptimiseMethodSpecifier(specifier.getExpression().getTerm(), model)) {
                    return body;
                }
            }
        }
        return transformMplBody(def.getParameterLists(), model, body);
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
            ParameterList pl = model.getParameterLists().get(index);
            resultType = gen().typeFact().getCallableType(List.of(resultType, typeFact().getParameterTypesAsTupleType(pl.getParameters(), model.getReference())));
            CallableBuilder cb = CallableBuilder.mpl(gen(), resultType, pl, parameterListsTree.get(index), body);
            body = List.<JCStatement>of(make().Return(cb.build()));
        }
        return body;
    }

    private List<JCStatement> transformMethodBody(Tree.AnyMethod def) {
        List<JCStatement> body = null;
        final Method model = def.getDeclarationModel();
        
        if (model.isDeferred()) {
            // Uninitialized or deferred initialized method => Make a Callable field
            String fieldName = naming.selector(model);
            final Parameter initializingParameter = CodegenUtil.findParamForDecl(def);
            int mods = PRIVATE;
            JCExpression initialValue;
            if (initializingParameter != null) {
                mods |= FINAL;
                initialValue = makeUnquotedIdent(Naming.getAliasedParameterName(initializingParameter));
            } else {
                // The field isn't initialized by a parameter, but later in the block
                initialValue = makeNull();
            }
            ProducedType callableType = model.getReference().getFullType();
            current().field(mods, fieldName, makeJavaType(callableType), initialValue, false);
            Invocation invocation = new CallableSpecifierInvocation(
                    this,
                    model,
                    makeUnquotedIdent(fieldName),
                    // we don't have to give a Term here because it's used for casting the Callable in case of callable erasure, 
                    // but with deferred methods we can't define them so that they are erased so we're good
                    null,
                    def);
            invocation.handleBoxing(true);
            JCExpression call = expressionGen().transformInvocation(invocation);
            JCStatement stmt;
            if (!isVoid(def) || !Decl.isUnboxedVoid(model) || Strategy.useBoxedVoid((Method)model)) {
                stmt = make().Return(call);
            } else {
                stmt = make().Exec(call);
            }
            
            JCStatement result;
            if (initializingParameter == null) {
                // If the field isn't initialized by a parameter we have to 
                // cope with the possibility that it's never initialized
                final JCBinary cond = make().Binary(JCTree.EQ, makeUnquotedIdent(fieldName), makeNull());
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
            body = transformMethodBlock((Tree.MethodDefinition)def);
        } else if (def instanceof MethodDeclaration
                && ((MethodDeclaration) def).getSpecifierExpression() != null) {
            body = transformSpecifiedMethodBody((MethodDeclaration)def, ((MethodDeclaration) def).getSpecifierExpression());
        }
        return body;
    }

    private List<JCStatement> transformMethodBlock(
            final Tree.MethodDefinition def) {
        final Method model = def.getDeclarationModel();
        final Tree.Block block = def.getBlock();
        List<JCStatement> body;
        boolean prevNoExpressionlessReturn = statementGen().noExpressionlessReturn;
        try {
            statementGen().noExpressionlessReturn = Decl.isMpl(model) || Strategy.useBoxedVoid(model);
            body = statementGen().transformBlock(block);
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
                body = body.append(make().Return(makeErroneous(block, "compiler bug: non-void method doesn't definitely return")));
            }
        }
        return body;
    }

    List<JCStatement> transformSpecifiedMethodBody(Tree.MethodDeclaration  def, SpecifierExpression specifierExpression) {
        final Method model = def.getDeclarationModel();
        List<JCStatement> body;
        Tree.MethodDeclaration methodDecl = def;
        boolean isLazy = specifierExpression instanceof Tree.LazySpecifierExpression;
        boolean returnNull = false;
        JCExpression bodyExpr;
        Tree.Term term = null;
        if (specifierExpression != null
                && specifierExpression.getExpression() != null) {
            term = Decl.unwrapExpressionsUntilTerm(specifierExpression.getExpression());
            HasErrorException error = errors().getFirstExpressionErrorAndMarkBrokenness(term);
            if (error != null) {
                return List.<JCStatement>of(this.makeThrowUnresolvedCompilationError(error));
            }
        }
        if (!isLazy && term instanceof Tree.FunctionArgument) {
            // Method specified with lambda: Don't bother generating a 
            // Callable, just transform the expr to use as the method body.
            Tree.FunctionArgument fa = (Tree.FunctionArgument)term;
            ProducedType resultType = model.getType();
            returnNull = isAnything(resultType) && fa.getExpression().getUnboxed();
            final java.util.List<Tree.Parameter> lambdaParams = fa.getParameterLists().get(0).getParameters();
            final java.util.List<Tree.Parameter> defParams = def.getParameterLists().get(0).getParameters();
            List<Substitution> substitutions = List.nil();
            for (int ii = 0; ii < lambdaParams.size(); ii++) {
                substitutions = substitutions.append(naming.addVariableSubst(
                        (TypedDeclaration)lambdaParams.get(ii).getParameterModel().getModel(), 
                        defParams.get(ii).getParameterModel().getName()));
            }
            bodyExpr = gen().expressionGen().transformExpression(fa.getExpression(), 
                            returnNull ? BoxingStrategy.INDIFFERENT : CodegenUtil.getBoxingStrategy(model), 
                            resultType);
            for (Substitution subs : substitutions) {
                subs.close();
            }
        } else if (!isLazy && typeFact().isCallableType(term.getTypeModel())) {
            returnNull = isAnything(term.getTypeModel()) && term.getUnboxed();
            Method method = methodDecl.getDeclarationModel();
            boolean lazy = specifierExpression instanceof Tree.LazySpecifierExpression;
            boolean inlined = CodegenUtil.canOptimiseMethodSpecifier(term, method);
            Invocation invocation;
            if ((lazy || inlined)
                    && term instanceof Tree.MemberOrTypeExpression
                    && ((Tree.MemberOrTypeExpression)term).getDeclaration() instanceof Functional) {
                Declaration primaryDeclaration = ((Tree.MemberOrTypeExpression)term).getDeclaration();
                ProducedReference producedReference = ((Tree.MemberOrTypeExpression)term).getTarget();
                invocation = new MethodReferenceSpecifierInvocation(
                        this, 
                        (Tree.MemberOrTypeExpression)term, 
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
                        term,
                        term);
            } else if (isCeylonCallableSubtype(term.getTypeModel())) {
                invocation = new CallableSpecifierInvocation(
                        this, 
                        method, 
                        expressionGen().transformExpression(term),
                        term,
                        term);
            } else {
                throw new BugException(term, "unhandled primary: " + term == null ? "null" : term.getNodeType());
            }
            invocation.handleBoxing(true);
            bodyExpr = expressionGen().transformInvocation(invocation);
        } else {
            bodyExpr = expressionGen().transformExpression(model, term);
            // The innermost of an MPL method declared void needs to return null
            returnNull = Decl.isUnboxedVoid(model) && Decl.isMpl(model);
        }
        if (!Decl.isUnboxedVoid(model)
                || Decl.isMpl(model)
                || Strategy.useBoxedVoid(model)) {
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
        return isVoid(def.getDeclarationModel());
    }
    
    private boolean isVoid(Declaration def) {
        if (def instanceof Method) {
            return gen().isAnything(((Method)def).getType());
        } else if (def instanceof Class
                || def instanceof Constructor) {
            // Consider classes void since ctors don't require a return statement
            return true;
        }
        throw BugException.unhandledDeclarationCase(def);
    }

    /** 
     * Generate a body for the overload method which delegates to the 
     * canonical method using a let to spubstitute defaulted parameters
     */
    private static int OL_BODY_DELEGATE_CANONICAL = 1<<0;
    /** 
     * Modifies OL_BODY_DELEGATE_CANONICAL so that the method is suitable 
     * for a companion class.
     */
    private static int OL_COMPANION = 1<<1;
    /** 
     * Modifies OL_BODY_DELEGATE_CANONICAL so that the canonical method on the 
     * interface instantce, {@code $this}, is called, rather than the 
     * canonical method on {@code this}.
     */ 
    private static int OL_DELEGATE_INTERFACE_INSTANCE = 1<<2;
    
    /** 
     * Abstraction over possible transformations for the body of an overloaded
     * declaration which supplies a defaulted argument.
     * @see DefaultedArgumentOverload 
     */
    abstract class DaoBody {
        
        protected List<JCExpression> makeTypeArguments(DefaultedArgumentOverload ol) {
            if (ol.defaultParameterMethodOnSelf() 
                    || ol.defaultParameterMethodOnOuter()) {
                return List.<JCExpression>nil();
            } else if (ol.defaultParameterMethodStatic()){
                Functional f = (Functional)ol.getModel();
                return typeArguments(f instanceof Constructor ? (Class)((Constructor)f).getContainer() : f);
            } else {
                return List.<JCExpression>nil();
            }
        }

        abstract void makeBody(DefaultedArgumentOverload overloaded,
                MethodDefinitionBuilder overloadBuilder,
                ParameterList parameterList,
                Parameter currentParameter,
                java.util.List<TypeParameter> typeParameterList);

        JCExpression makeMethodNameQualifier() {
            return null;
        }
        
    }
    
    /** 
     * a body-less (i.e. abstract) transformation.
     */
    private class DaoAbstract extends DaoBody {
        @Override
        public void makeBody(DefaultedArgumentOverload overloaded,
                MethodDefinitionBuilder overloadBuilder,
                ParameterList parameterList,
                Parameter currentParameter,
                java.util.List<TypeParameter> typeParameterList) {
            overloadBuilder.noBody();
        }
    };
    final DaoAbstract daoAbstract = new DaoAbstract();
    /**
     * a transformation for an overloaded 
     * default parameter body which delegates to the "canonical"
     * method/constructor using a {@code let} expresssion
     * to substitute defaulted arguments
     */
    private class DaoThis extends DaoBody {
        private final Tree.Declaration declTree;
        private final Token firstExecutable;
        private final Tree.ParameterList pl;

        public DaoThis(Tree.Declaration invocation, Tree.ParameterList pl) {
            this.declTree = invocation;
            this.firstExecutable = pl != null ? pl.getEndToken() : null;
            this.pl = pl;
        }
        @Override
        public void makeBody(DefaultedArgumentOverload overloaded,
                MethodDefinitionBuilder overloadBuilder,
                ParameterList parameterList,
                Parameter currentParameter,
                java.util.List<TypeParameter> typeParameterList) {
            ListBuffer<JCExpression> delegateArgs = ListBuffer.<JCExpression>lb();
            overloaded.appendImplicitArgumentsDelegate(typeParameterList, delegateArgs);
            ListBuffer<JCExpression> dpmArgs = ListBuffer.<JCExpression>lb();
            overloaded.appendImplicitArgumentsDpm(typeParameterList, dpmArgs);
            
            ListBuffer<JCStatement> vars = ListBuffer.<JCStatement>lb();
            
            boolean initedVars = false;
            boolean useDefault = false;
            for (Parameter parameterModel : parameterList.getParameters()) {
                if (currentParameter != null && Decl.equal(parameterModel, currentParameter)) {
                    useDefault = true;
                }
                if (useDefault) {
                    at(parameterLocation(parameterModel));
                    JCExpression defaultArgument;
                    if (Strategy.hasDefaultParameterValueMethod(parameterModel)) {
                        if (!initedVars) {
                            // Only call init vars if we actually invoke a defaulted param method
                            overloaded.initVars(currentParameter, vars);
                        }
                        initedVars = true;
                        JCExpression defaultValueMethodName = naming.makeDefaultedParamMethod(overloaded.makeDefaultArgumentValueMethodQualifier(), parameterModel);
                        defaultArgument = make().Apply(makeTypeArguments(overloaded), 
                                defaultValueMethodName, 
                                ListBuffer.<JCExpression>lb().appendList(dpmArgs).toList());
                    } else if (Strategy.hasEmptyDefaultArgument(parameterModel)) {
                        defaultArgument = makeEmptyAsSequential(true);
                    } else {
                        defaultArgument = makeErroneous(null, "compiler bug: parameter " + parameterModel.getName() + " has an unsupported default value");
                    }
                    Naming.SyntheticName varName = naming.temp(parameterModel.getName());
                    ProducedType paramType = overloaded.parameterType(parameterModel);
                    vars.append(makeVar(varName, 
                            makeJavaType(paramType, CodegenUtil.isUnBoxed(parameterModel.getModel()) ? 0 : JT_NO_PRIMITIVES), 
                            defaultArgument));
                    at(null);
                    delegateArgs.add(varName.makeIdent());
                    dpmArgs.add(varName.makeIdent());
                } else {
                    delegateArgs.add(naming.makeName(parameterModel.getModel(), Naming.NA_MEMBER | Naming.NA_ALIASED));
                    dpmArgs.add(naming.makeName(parameterModel.getModel(), Naming.NA_MEMBER | Naming.NA_ALIASED));
                }
            }
            makeBody(overloaded, overloadBuilder, delegateArgs, vars);
        }
        
        private Node parameterLocation(Parameter parameterModel) {
            if (this.pl != null) {
                for (Tree.Parameter p : pl.getParameters()) {
                    if (p.getParameterModel().equals(parameterModel)) {
                        return p;
                    }
                }
            }
            return null;
        }
        protected final void makeBody(DefaultedArgumentOverload overloaded, 
                MethodDefinitionBuilder overloadBuilder, 
                ListBuffer<JCExpression> args, 
                ListBuffer<JCStatement> vars) {
            at(pl, firstExecutable);
            JCExpression invocation = overloaded.makeInvocation(args);
            Declaration model = overloaded.getModel();// TODO Yuk
            if (!isVoid(model)
                    // MPL overloads always return a Callable
                    || (model instanceof Functional && Decl.isMpl((Functional) model))
                    || (model instanceof Method && !(Decl.isUnboxedVoid(model)))
                    || (model instanceof Method && Strategy.useBoxedVoid((Method)model)) 
                    || Strategy.generateInstantiator(model) && overloaded instanceof DefaultedArgumentInstantiator) {
                if (!vars.isEmpty()) {
                    invocation = at(declTree).LetExpr(vars.toList(), invocation);
                }
                overloadBuilder.body(make().Return(invocation));
            } else {
                vars.append(at(pl, firstExecutable).Exec(invocation));
                invocation = at(declTree).LetExpr(vars.toList(), makeNull());
                overloadBuilder.body(make().Exec(invocation));
            }
        }
    }
    
    /**
     * specialises {@link DaoThis} for transforming declarations for companion classes
     */
    private class DaoCompanion extends DaoThis {
        DaoCompanion(Tree.AnyMethod invocation, Tree.ParameterList pl) {
            super(invocation, pl);
        }
        @Override
        protected final List<JCExpression> makeTypeArguments(DefaultedArgumentOverload ol) {
            return List.<JCExpression>nil();
        }
    }
    
    /**
     * a transformation for an overloaded 
     * default parameter method body which delegates
     * to the super class. This is used when we need to refine the return 
     * type of a DPM. 
     */
    private class DaoSuper extends DaoBody {

        JCExpression makeMethodNameQualifier() {
            return naming.makeSuper();
        }
        
        @Override
        void makeBody(
                DefaultedArgumentOverload overloaded,
                MethodDefinitionBuilder overloadBuilder,
                ParameterList parameterList,
                Parameter currentParameter,
                java.util.List<TypeParameter> typeParameterList) {
            
            ListBuffer<JCExpression> args = ListBuffer.<JCExpression>lb();
            for (Parameter parameter : parameterList.getParameters()) {
                if (currentParameter != null && Decl.equal(parameter, currentParameter)) {
                    break;
                }
                args.add(naming.makeUnquotedIdent(parameter.getName()));
            }
            JCExpression superCall = overloaded.makeInvocation(args);
            /*JCMethodInvocation superCall = make().Apply(null,
                    naming.makeQualIdent(naming.makeSuper(), ((Method)overloaded.getModel()).getName()),
                    args.toList());*/
            JCExpression refinedType = makeJavaType(((Method)overloaded.getModel()).getType(), JT_NO_PRIMITIVES);
            overloadBuilder.body(make().Return(make().TypeCast(refinedType, superCall)));
        }
    }
    
    /**
     * A base class for transformations used for Ceylon declarations
     * which have defaulted parameters. We generate an overloaded 
     * method/constructor whose implementation which supplies the default
     * argument and delegates to the "canonical" method.
     * 
     * Subclasses specialise for the different kinds of declaration, and 
     * a separate set of classes handle the various transformations for the 
     * body of an overloaded declaration (see {@link DaoBody})
     */
    abstract class DefaultedArgumentOverload {
        protected final DaoBody daoBody;
        protected final MethodDefinitionBuilder overloadBuilder;
        
        /**
         * @param daoBody for the body, or null if no body required
         */
        protected DefaultedArgumentOverload(DaoBody daoBody, MethodDefinitionBuilder overloadBuilder){
            this.daoBody = daoBody;
            this.overloadBuilder = overloadBuilder;
        }
        
        protected abstract long getModifiers();

        protected abstract JCExpression makeMethodName();

        protected abstract void resultType();

        protected abstract void typeParameters();

        protected void parameters(ParameterList parameterList, Parameter currentParameter) {
            for (Parameter parameter : parameterList.getParameters()) {
                if (currentParameter != null && Decl.equal(parameter, currentParameter)) {
                    break;
                }
                overloadBuilder.parameter(parameter, null, 0, false);
            }
        }
        
        protected void appendImplicitParameters(java.util.List<TypeParameter> typeParameterList) {
            if(typeParameterList != null){
                overloadBuilder.reifiedTypeParameters(typeParameterList);
            }
        }
        
        protected abstract void appendImplicitArgumentsDelegate(java.util.List<TypeParameter> typeParameterList,
                ListBuffer<JCExpression> args);
        protected abstract void appendImplicitArgumentsDpm(java.util.List<TypeParameter> typeParameterList,
                ListBuffer<JCExpression> args);
        
        protected ProducedType parameterType(Parameter parameterModel) {
            ProducedType paramType = null;
            if (parameterModel.getModel() instanceof Method) {
                paramType = typeFact().getCallableType(parameterModel.getType());
            } else {
                paramType = parameterModel.getType();
            }
            return paramType;
        }

        protected abstract void initVars(Parameter currentParameter, ListBuffer<JCStatement> vars);

        protected final boolean defaultParameterMethodOnSelf() {
            return Strategy.defaultParameterMethodOnSelf(getModel());
        }

        protected final boolean defaultParameterMethodOnOuter() {
            return Strategy.defaultParameterMethodOnOuter(getModel());
        }

        protected final boolean defaultParameterMethodStatic() {
            return Strategy.defaultParameterMethodStatic(getModel());
        }

        protected abstract Declaration getModel();

        protected JCExpression makeInvocation(ListBuffer<JCExpression> args) {
            final JCExpression methName = makeMethodName();
            return make().Apply(List.<JCExpression>nil(),
                    methName, args.toList());
        }

        /** Returns the qualiifier to use when invoking the default parameter value method */
        protected abstract JCIdent makeDefaultArgumentValueMethodQualifier();
        
        
        /**
         * Generates an overloaded method or constructor.
         */
        public MethodDefinitionBuilder makeOverload (
                ParameterList parameterList,
                Parameter currentParameter,
                java.util.List<TypeParameter> typeParameterList) {

            // Make the declaration
            // need annotations for BC, but the method isn't really there
            overloadBuilder.ignoreModelAnnotations();
            overloadBuilder.modifiers(getModifiers());
            resultType();
            typeParameters();

            appendImplicitParameters(typeParameterList);
            parameters(parameterList, currentParameter);
            
            // Make the body, but only if we want one. null means we want a formal method
            // TODO MPL
            // TODO Type args on method call
            
            if(daoBody != null){
                daoBody.makeBody(this, overloadBuilder,
                        parameterList,
                        currentParameter,
                        typeParameterList);
            }
            
            return overloadBuilder;
        }
    }
    
    /**
     * A transformation for generating overloaded <em>methods</em> for 
     * defaulted arguments. 
     */
    class DefaultedArgumentMethod extends DefaultedArgumentOverload {
        private final Method method;

        DefaultedArgumentMethod(DaoBody daoBody, MethodDefinitionBuilder mdb, Method method) {
            super(daoBody, mdb);
            this.method = method;
        }

        @Override
        protected Method getModel() {
            return method;
        }
        
        @Override
        protected long getModifiers() {
            long mods = transformMethodDeclFlags(method);
            if (daoBody instanceof DaoAbstract == false) {
                mods &= ~ABSTRACT;
            }
            if (daoBody instanceof DaoCompanion) {
                mods |= FINAL;
            }
            return mods;
        }

        @Override
        protected final JCExpression makeMethodName() {
            int flags = Naming.NA_MEMBER;
            if (Decl.withinClassOrInterface(method)) {
                flags |= Naming.NA_CANONICAL_METHOD;
            }
            return naming.makeQualifiedName(daoBody.makeMethodNameQualifier(), method, flags);
        }

        @Override
        protected void resultType() {
            overloadBuilder.resultType(method, 0);
        }

        @Override
        protected void typeParameters() {
            copyTypeParameters(method, overloadBuilder);
        }

        @Override
        protected void appendImplicitArgumentsDelegate(java.util.List<TypeParameter> typeParameterList,
                ListBuffer<JCExpression> args) {
            if(typeParameterList != null){
                // we pass the reified type parameters along
                for(TypeParameter tp : typeParameterList){
                    args.append(makeUnquotedIdent(naming.getTypeArgumentDescriptorName(tp)));
                }
            }
        }
        @Override
        protected void appendImplicitArgumentsDpm(java.util.List<TypeParameter> typeParameterList,
                ListBuffer<JCExpression> args) {
            appendImplicitArgumentsDelegate(typeParameterList, args);
        }

        @Override
        protected void initVars(Parameter currentParameter, ListBuffer<JCStatement> vars) {
        }

        @Override
        protected JCIdent makeDefaultArgumentValueMethodQualifier() {
            return null;
        }
    }
    
    /**
     * A transformation for generating overloaded <em>methods</em> for 
     * defaulted arguments. 
     */
    class DefaultedArgumentMethodTyped extends DefaultedArgumentMethod {
        private ProducedTypedReference typedMember;

        DefaultedArgumentMethodTyped(DaoBody daoBody, MethodDefinitionBuilder mdb, ProducedTypedReference typedMember) {
            super(daoBody, mdb, (Method)typedMember.getDeclaration());
            this.typedMember = typedMember;
        }

        @Override
        protected void resultType() {
            if (!isAnything(getModel().getType())
                    || !Decl.isUnboxedVoid(getModel())
                    || Strategy.useBoxedVoid((Method)getModel())) {
                ProducedTypedReference typedRef = (ProducedTypedReference) typedMember;
                overloadBuilder.resultTypeNonWidening(typedMember.getQualifyingType(), typedRef, typedMember.getType(), 0);
            } else {
                super.resultType();
            }
        }
        
        @Override
        protected void parameters(ParameterList parameterList, Parameter currentParameter) {
            for (Parameter param : parameterList.getParameters()) {
                if (currentParameter != null && Decl.equal(param, currentParameter)) {
                    break;
                }
                ProducedType type = paramType(param);
                overloadBuilder.parameter(param, type, FINAL, 0, true);
            }
        }
        
        @Override
        protected ProducedType parameterType(Parameter parameter) {
            ProducedType paramType = paramType(parameter);
            if (parameter.getModel() instanceof Method) {
                paramType = typeFact().getCallableType(parameter.getType());
            }
            return paramType;
        }

        private ProducedType paramType(Parameter parameter) {
            final ProducedTypedReference typedParameter = typedMember.getTypedParameter(parameter);
            ProducedType paramType;
            // if the supertype method itself got erased to Object, we can't do better than this
            if (gen().willEraseToObject(parameter.getType()) && !gen().willEraseToBestBounds(parameter)) {
                paramType = typeFact().getObjectDeclaration().getType();
            } else {
                paramType = typedParameter.getType();
            }
            return paramType;
        }

        @Override
        public MethodDefinitionBuilder makeOverload(
                ParameterList parameterList, Parameter currentParameter,
                java.util.List<TypeParameter> typeParameterList) {
            overloadBuilder.isOverride(true);
            return super.makeOverload(parameterList, currentParameter,
                    typeParameterList);
        }
        
        
    }
    
    /**
     * A transformation for generating the canonical <em>method</em> used by the
     * defaulted argument overload methods
     */
    class CanonicalMethod extends DefaultedArgumentMethod {
        private List<JCStatement> body;
        private boolean useBody;
        
        CanonicalMethod(DaoBody daoBody, MethodDefinitionBuilder mdb, Method method) {
            super(daoBody, mdb, method);
            useBody = false;
        }
        
        CanonicalMethod(DaoBody daoBody, Method method, List<JCStatement> body) {
            super(daoBody, MethodDefinitionBuilder.method(ClassTransformer.this, method, Naming.NA_CANONICAL_METHOD), method);
            this.body = body;
            useBody = true;
        }
        
        @Override
        protected long getModifiers() {
            long mods = super.getModifiers();
            if (useBody) {
                mods = mods & ~PUBLIC & ~FINAL | PRIVATE;
            }
            return mods;
        }
        
        /**
         * Generates a canonical method
         */
        @Override
        public MethodDefinitionBuilder makeOverload (
                ParameterList parameterList,
                Parameter currentParameter,
                java.util.List<TypeParameter> typeParameterList) {

            if (!useBody) {
                daoBody.makeBody(this, overloadBuilder,
                        parameterList,
                        currentParameter,
                        typeParameterList);
            } else {
                // Make the declaration
                // need annotations for BC, but the method isn't really there
                overloadBuilder.ignoreModelAnnotations();
                overloadBuilder.modifiers(getModifiers());
                resultType();
                typeParameters();

                appendImplicitParameters(typeParameterList);
                parameters(parameterList, currentParameter);
                
                if (body != null) {
                    // Construct the outermost method using the body we've built so far
                    overloadBuilder.body(body);
                } else {
                    overloadBuilder.noBody();
                }
            }
            
            return overloadBuilder;
        }

    }
    
    /**
     * A base class for transformations that generate overloaded declarations for 
     * defaulted arguments. 
     */
    abstract class DefaultedArgumentClass extends DefaultedArgumentOverload {
        
        protected final Class klass;
        protected final Constructor constructor;
        
        protected Naming.SyntheticName companionInstanceName = null;

        DefaultedArgumentClass(DaoBody daoBody, MethodDefinitionBuilder mdb, Class klass, Constructor constructor) {
            super(daoBody, mdb);
            this.klass = klass;
            this.constructor = constructor;
        }
        
        @Override
        protected final Declaration getModel() {
            return constructor != null ? constructor : klass;
        }
        
        @Override
        protected void appendImplicitParameters(java.util.List<TypeParameter> typeParameterList) {
            super.appendImplicitParameters(typeParameterList);
            if (constructor != null
                    && !Decl.isDefaultConstructor(constructor)) {
                overloadBuilder.parameter(makeConstructorNameParameter(constructor));
            }
        }
        

        @Override
        protected void appendImplicitArgumentsDelegate(java.util.List<TypeParameter> typeParameterList,
                ListBuffer<JCExpression> args) {
            appendImplicitArgumentsDpm(typeParameterList, args);
            if (constructor != null
                    && !Decl.isDefaultConstructor(constructor)) {
                args.append(naming.makeUnquotedIdent(Unfix.$name$));
            }
        }
        

        @Override
        protected void appendImplicitArgumentsDpm(java.util.List<TypeParameter> typeParameterList,
                ListBuffer<JCExpression> args) {
            if(typeParameterList != null){
                // we pass the reified type parameters along
                for(TypeParameter tp : typeParameterList){
                    args.append(makeUnquotedIdent(naming.getTypeArgumentDescriptorName(tp)));
                }
            }
        }
        
        @Override
        protected void initVars(Parameter currentParameter, ListBuffer<JCStatement> vars) {
            if (!Strategy.defaultParameterMethodStatic(klass)
                    && !Strategy.defaultParameterMethodOnOuter(klass)
                    && currentParameter != null) {
                companionInstanceName = naming.temp("impl");
                vars.append(makeVar(companionInstanceName, 
                        makeJavaType(klass.getType(), AbstractTransformer.JT_COMPANION),
                        make().NewClass(null, 
                                null,
                                makeJavaType(klass.getType(), AbstractTransformer.JT_CLASS_NEW | AbstractTransformer.JT_COMPANION),
                                List.<JCExpression>nil(), null)));
            }
        }
        
        @Override
        protected JCIdent makeDefaultArgumentValueMethodQualifier() {
            if (defaultParameterMethodOnOuter()){
                // if we're refining a class we can't declare new default values, so we should get
                // them from the instance rather than the outer interface impl
                if(getModel().isActual() && getModel().getContainer() instanceof Interface)
                    return naming.makeUnquotedIdent("$this");
                return null;
            }else if (defaultParameterMethodOnSelf() 
                    || daoBody instanceof DaoCompanion) {
                return null;
            } else if (defaultParameterMethodStatic()){
                return null;
            } else {
                return companionInstanceName.makeIdent();
            }
        }
    }
    
    /**
     * A transformation for generating overloaded <em>constructors</em> for 
     * defaulted arguments. 
     */
    class DefaultedArgumentConstructor extends DefaultedArgumentClass {

        DefaultedArgumentConstructor(MethodDefinitionBuilder mdb, Class klass, Tree.Declaration node, Tree.ParameterList pl) {
            super(new DaoThis(node, pl), mdb, klass, null);
        }
        
        DefaultedArgumentConstructor(MethodDefinitionBuilder mdb, Constructor constructor, Tree.Declaration node, Tree.ParameterList pl) {
            super(new DaoThis(node, pl), mdb, (Class)constructor.getContainer(), constructor);
        }
        
        @Override
        protected long getModifiers() {
            return transformConstructorDeclFlags(klass) & (PUBLIC | PRIVATE | PROTECTED);
        }

        @Override
        protected JCExpression makeMethodName() {
            return naming.makeQualifiedThis(daoBody.makeMethodNameQualifier());
        }

        @Override
        protected void resultType() {
            // Constructor has no result type
        }

        @Override
        protected void typeParameters() {
            // Constructor has type parameters
        }
        
    }
    
    /**
     * A transformation for generating overloaded <em>instantiator methods</em> for 
     * defaulted arguments. 
     */
    class DefaultedArgumentInstantiator extends DefaultedArgumentClass {

        DefaultedArgumentInstantiator(DaoBody daoBody, Class klass, Constructor ctor) {
            super(daoBody, MethodDefinitionBuilder.systemMethod(ClassTransformer.this, naming.getInstantiatorMethodName(klass)), klass, ctor);
        }

        @Override
        protected long getModifiers() {
            // remove the FINAL bit in case it gets set, because that is valid for a class decl, but
            // not for a method if in an interface
            long modifiers = transformClassDeclFlags(klass) & ~FINAL;
            // alias classes cannot be abstract since they're placeholders, but it's possible to have formal class aliases
            // and the instantiator method needs the abstract bit
            if(klass.isFormal() && klass.isAlias())
                modifiers |= ABSTRACT;
            return modifiers;
            
        }

        @Override
        protected JCExpression makeMethodName() {
            return naming.makeInstantiatorMethodName(daoBody.makeMethodNameQualifier(), klass);
        }

        @Override
        protected void resultType() {
            /* Not actually part of the return type */
            overloadBuilder.ignoreModelAnnotations();
            if (!klass.isAlias() 
                    && Strategy.generateInstantiator(klass.getExtendedTypeDeclaration())
                    && klass.isActual()
                    && klass.getExtendedTypeDeclaration().getContainer() instanceof Class) {
                overloadBuilder.isOverride(true);
            }
            /**/
            
            JCExpression resultType;
            ProducedType type = klass.isAlias() ? klass.getExtendedType() : klass.getType();
            if (Strategy.isInstantiatorUntyped(klass)) {
                // We can't expose a local type name to a place it's not visible
                resultType = make().Type(syms().objectType);
            } else {
                resultType = makeJavaType(type);
            }
            overloadBuilder.resultType(null, resultType);
        }

        @Override
        protected void typeParameters() {
            for (TypeParameter tp : typeParametersForInstantiator(klass)) {
                overloadBuilder.typeParameter(tp);
            }
        }
        
        @Override
        protected void appendImplicitArgumentsDelegate(java.util.List<TypeParameter> typeParameterList,
                ListBuffer<JCExpression> args) {
            ProducedType type = klass.isAlias() ? klass.getExtendedType() : klass.getType();
            type = type.resolveAliases();
            // fetch the type parameters from the klass we're instantiating itself if any
            for(ProducedType pt : type.getTypeArgumentList()){
                args.append(makeReifiedTypeArgument(pt));
            }
            if (constructor != null
                    && !Decl.isDefaultConstructor(constructor)) {
                args.append(naming.makeUnquotedIdent(Unfix.$name$));
            }
        }

        @Override
        protected JCExpression makeInvocation(ListBuffer<JCExpression> args) {
            ProducedType type = klass.isAlias() ? klass.getExtendedType() : klass.getType();
            return make().NewClass(null, 
                    null, 
                    makeJavaType(type, JT_CLASS_NEW | JT_NON_QUALIFIED),
                    args.toList(),
                    null);
        }
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

    private java.util.List<TypeParameter> typeParametersOfAllContainers(final ClassOrInterface model, boolean includeModelTypeParameters) {
        java.util.List<java.util.List<TypeParameter>> r = new ArrayList<java.util.List<TypeParameter>>(1);
        Scope s = model.getContainer();
        while (!(s instanceof Package)) {
            if (s instanceof Generic) {
                r.add(0, ((Generic)s).getTypeParameters());
            }
            s = s.getContainer();
        }
        Set<String> names = new HashSet<String>();
        for(TypeParameter tp : model.getTypeParameters()){
            names.add(tp.getName());
        }
        java.util.List<TypeParameter> result = new ArrayList<TypeParameter>(1);
        for (java.util.List<TypeParameter> tps : r) {
            for(TypeParameter tp : tps){
                if(names.add(tp.getName())){
                    result.add(tp);
                }
            }
        }
        if(includeModelTypeParameters)
            result.addAll(model.getTypeParameters());
        return result;
    }
    
    
    /**
     * Creates a (possibly abstract) method for retrieving the value for a 
     * defaulted parameter
     * @param typeParameterList 
     */
    MethodDefinitionBuilder makeParamDefaultValueMethod(boolean noBody, Declaration container, 
            Tree.ParameterList params, Tree.Parameter currentParam) {
        at(currentParam);
        Parameter parameter = currentParam.getParameterModel();
        if (!Strategy.hasDefaultParameterValueMethod(parameter)) {
            throw new BugException();
        }
        MethodDefinitionBuilder methodBuilder = MethodDefinitionBuilder.systemMethod(this, Naming.getDefaultedParamMethodName(container, parameter));
        methodBuilder.ignoreModelAnnotations();
        if (container != null && Decl.isAnnotationConstructor(container)) {
            AnnotationInvocation ac = (AnnotationInvocation)((Method)container).getAnnotationConstructor();
            for (AnnotationConstructorParameter acp : ac.getConstructorParameters()) {
                if (acp.getParameter().equals(parameter)
                        && acp.getDefaultArgument() != null) {
                    methodBuilder.userAnnotations(acp.getDefaultArgument().makeDpmAnnotations(expressionGen()));
                }
            }
        }
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
            // static default parameter methods should be consistently public so that if non-shared class Top and
            // shared class Bottom which extends Top both have the same default param name, we don't get an error
            // if the Bottom class tries to "hide" a static public method with a private one
            modifiers &= ~PRIVATE;
            modifiers |= STATIC | PUBLIC;
        }
        methodBuilder.modifiers(modifiers);
        
        if (container instanceof Constructor) {
            copyTypeParameters((Class)container.getContainer(), methodBuilder);
            methodBuilder.reifiedTypeParameters(((Class)container.getContainer()).getTypeParameters());
        } else if(container instanceof Generic) {
            // make sure reified type parameters are accepted
            copyTypeParameters((Generic)container, methodBuilder);
            methodBuilder.reifiedTypeParameters(((Generic)container).getTypeParameters());
        }
        // Add any of the preceding parameters as parameters to the method
        for (Tree.Parameter p : params.getParameters()) {
            if (p.equals(currentParam)) {
                break;
            }
            at(p);
            methodBuilder.parameter(p.getParameterModel(), null, 0, container instanceof Class);
        }

        // The method's return type is the same as the parameter's type
        methodBuilder.resultType(parameter.getModel(), parameter.getType(), 0);

        // The implementation of the method
        if (noBody) {
            methodBuilder.noBody();
        } else {
            HasErrorException error = errors().getFirstExpressionErrorAndMarkBrokenness(Decl.getDefaultArgument(currentParam).getExpression());
            if (error != null) {
                methodBuilder.body(this.makeThrowUnresolvedCompilationError(error));
            } else {
                java.util.List<TypeParameter> copiedTypeParameters = null;
                if(container instanceof Functional) {
                    copiedTypeParameters = ((Functional) container).getTypeParameters();
                    if(copiedTypeParameters != null)
                        addTypeParameterSubstitution(copiedTypeParameters);
                }
                try{
                    JCExpression expr = expressionGen().transform(currentParam);
                    JCBlock body = at(currentParam).Block(0, List.<JCStatement> of(at(currentParam).Return(expr)));
                    methodBuilder.block(body);
                }finally{
                    if(copiedTypeParameters != null)
                        popTypeParameterSubstitution();
                }
            }
        }

        return methodBuilder;
    }

    public List<JCTree> transformObjectDefinition(Tree.ObjectDefinition def, ClassDefinitionBuilder containingClassBuilder) {
        return transformObject(def, def, def.getSatisfiedTypes(), def.getDeclarationModel(), 
                def.getAnonymousClass(), containingClassBuilder, Decl.isLocalNotInitializer(def));
    }
    
    public List<JCTree> transformObjectArgument(Tree.ObjectArgument def) {
        return transformObject(def, null, def.getSatisfiedTypes(), def.getDeclarationModel(), 
                def.getAnonymousClass(), null, false);
    }

    public List<JCTree> transformObjectExpression(Tree.ObjectExpression def) {
        return transformObject(def, null, def.getSatisfiedTypes(), null, 
                def.getAnonymousClass(), null, false);
    }
    
    private List<JCTree> transformObject(Node def,
            Tree.Declaration annotated,
            Tree.SatisfiedTypes satisfiesTypes,
            Value model, 
            Class klass,
            ClassDefinitionBuilder containingClassBuilder,
            boolean makeLocalInstance) {
        naming.clearSubstitutions(klass);
        
        String name = klass.getName();
        ClassDefinitionBuilder objectClassBuilder = ClassDefinitionBuilder.object(
                this, name, Decl.isLocal(klass)).forDefinition(klass);
        
        // Make sure top types satisfy reified type
        addReifiedTypeInterface(objectClassBuilder, klass);
        if(supportsReifiedAlias(klass))
            objectClassBuilder.reifiedAlias(klass.getType());
        
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
 
        addMissingUnrefinedMembers(def, klass, objectClassBuilder);
        satisfaction(satisfiesTypes, klass, objectClassBuilder);
        serialization(klass, objectClassBuilder);
        
        if (model != null
                && Decl.isToplevel(model)
                && def instanceof Tree.ObjectDefinition) {
            // generate a field and getter
            AttributeDefinitionBuilder builder = AttributeDefinitionBuilder
                    // TODO attr build take a JCExpression className
                    .wrapped(this, null, objectClassBuilder, model.getName(), model, true)
                    .userAnnotations(makeAtIgnore())
                    .userAnnotationsSetter(makeAtIgnore())
                    .immutable()
                    .initialValue(makeNewClass(naming.makeName(model, Naming.NA_FQ | Naming.NA_WRAPPER)))
                    .is(PUBLIC, Decl.isShared(klass))
                    .is(STATIC, true);
            if (annotated != null) {
                builder.fieldAnnotations(expressionGen().transformAnnotations(false, OutputElement.FIELD, annotated));
                builder.userAnnotations(expressionGen().transformAnnotations(true, OutputElement.GETTER, annotated));
            }            
            objectClassBuilder.defs(builder.build());
        }
        if (annotated != null) {
            objectClassBuilder.annotations(expressionGen().transformAnnotations(false, OutputElement.TYPE, annotated));
            objectClassBuilder.getInitBuilder().userAnnotations(expressionGen().transformAnnotations(false, OutputElement.CONSTRUCTOR, annotated));
        }
        
        // make sure we set the container in case we move it out
        addAtContainer(objectClassBuilder, klass);

        objectClassBuilder.getInitBuilder().modifiers(PRIVATE);
        objectClassBuilder
            .annotations(makeAtObject())
            .satisfies(klass.getSatisfiedTypes())
            .defs((List)childDefs)
            .addGetTypeMethod(klass.getType());
        
        if(model != null)
            objectClassBuilder
            .modelAnnotations(model.getAnnotations())
            .modifiers(transformObjectDeclFlags(model));
        
        List<JCTree> result = objectClassBuilder.build();
        
        if (makeLocalInstance) {
            if(model.isSelfCaptured()){
                // if it's captured we need to box it and define the var before the class, so it can access it
                JCNewClass newInstance = makeNewClass(objectClassBuilder.getClassName(), false, null);
                JCFieldAccess setter = naming.makeSelect(Naming.getLocalValueName(model), Naming.getSetterName(model));
                JCStatement assign = make().Exec(make().Assign(setter, newInstance));
                result = result.prepend(assign);

                JCVariableDecl localDecl = makeVariableBoxDecl(null, model);
                result = result.prepend(localDecl);
            }else{
                // not captured, we can define the var after the class
                JCVariableDecl localDecl = makeLocalIdentityInstance(name, objectClassBuilder.getClassName(), false);
                result = result.append(localDecl);
            }
            
        } else if (model != null && Decl.withinClassOrInterface(model)) {
            boolean visible = Decl.isCaptured(model);
            int modifiers = FINAL | ((visible) ? PRIVATE : 0);
            JCExpression type = makeJavaType(klass.getType());
            JCExpression initialValue = makeNewClass(makeJavaType(klass.getType()), null);
            containingClassBuilder.field(modifiers, name, type, initialValue, !visible);
            
            if (visible) {
                AttributeDefinitionBuilder getter = AttributeDefinitionBuilder
                .getter(this, name, model)
                .modifiers(transformAttributeGetSetDeclFlags(model, false));
                if (def instanceof Tree.ObjectDefinition) {
                    getter.userAnnotations(expressionGen().transformAnnotations(true, OutputElement.GETTER, ((Tree.ObjectDefinition)def)));
                }
                result = result.appendList(getter.build());
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
        List<JCExpression> arguments = makeBottomReifiedTypeParameters(model.getTypeParameters());
        JCNewClass expr = make().NewClass(null, null, nameId, arguments, null);
        return makeMainMethod(model, expr);
    }
    
    /**
     * Makes a {@code main()} method which calls the given top-level method
     * @param method
     */
    private MethodDefinitionBuilder makeMainForFunction(Method method) {
        at(null);
        JCExpression qualifiedName = naming.makeName(method, Naming.NA_FQ | Naming.NA_WRAPPER | Naming.NA_MEMBER);
        List<JCExpression> arguments = makeBottomReifiedTypeParameters(method.getTypeParameters());
        MethodDefinitionBuilder mainMethod = makeMainMethod(method, make().Apply(null, qualifiedName, arguments));
        return mainMethod;
    }
    
    private List<JCExpression> makeBottomReifiedTypeParameters(
            java.util.List<TypeParameter> typeParameters) {
        List<JCExpression> arguments = List.nil();
        for(int i=typeParameters.size()-1;i>=0;i--){
            arguments = arguments.prepend(gen().makeNothingTypeDescriptor());
        }
        return arguments;
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
                .ignoreModelAnnotations();
        // Add call to process.setupArguments
        JCExpression argsId = makeUnquotedIdent("args");
        JCMethodInvocation processExpr = make().Apply(null, naming.makeLanguageValue("process"), List.<JCTree.JCExpression>nil());
        methbuilder.body(make().Exec(make().Apply(null, makeSelect(processExpr, "setupArguments"), List.<JCTree.JCExpression>of(argsId))));
        // Add call to toplevel method
        methbuilder.body(make().Exec(callee));
        return methbuilder;
    }
    
    void copyTypeParameters(Generic def, MethodDefinitionBuilder methodBuilder) {
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
        
        naming.clearSubstitutions(model);
        String ceylonClassName = def.getIdentifier().getText();
        final String javaClassName = Naming.quoteClassName(def.getIdentifier().getText());

        ClassDefinitionBuilder classBuilder = ClassDefinitionBuilder
                .klass(this, javaClassName, ceylonClassName, Decl.isLocal(model));

        // class alias
        classBuilder.getInitBuilder().modifiers(PRIVATE);
        classBuilder.annotations(makeAtTypeAlias(model.getExtendedType()));
        classBuilder.annotations(expressionGen().transformAnnotations(true, OutputElement.TYPE, def));
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

    public List<JCTree> transform(Tree.Constructor that, ClassDefinitionBuilder classBuilder) {
        ListBuffer<JCTree> result = ListBuffer.<JCTree>lb();
        Constructor ctor = that.getDeclarationModel();
        Class clz = (Class)ctor.getContainer();
        
        at(that);
        MethodDefinitionBuilder ctorDb = MethodDefinitionBuilder.constructor(this);
        
        ClassDefinitionBuilder decl = null;
        ClassDefinitionBuilder impl = null;
        boolean generateInstantiator = Strategy.generateInstantiator(ctor);
        if (generateInstantiator) {
            if (clz.getContainer() instanceof Interface) {
                decl = classBuilder.getContainingClassBuilder();
                impl = classBuilder.getContainingClassBuilder().getCompanionBuilder((Interface)clz.getContainer());
            } else {
                decl = classBuilder.getContainingClassBuilder();
                impl = classBuilder.getContainingClassBuilder();
            }
            generateInstantiators(classBuilder, clz, ctor, decl, impl, that, that.getParameterList());
        }
        
        ctorDb.userAnnotations(expressionGen().transformAnnotations(true, OutputElement.CONSTRUCTOR, that));
        if (!Decl.isDefaultConstructor(ctor)) {
            ctorDb.modelAnnotations(makeAtName(ctor.getName()));
        }
        ctorDb.modifiers(transformConstructorDeclFlags(ctor));
        
        if (Decl.isDefaultConstructor(ctor)) {
            transformClassOrCtorParameters(null, (Class)ctor.getContainer(), ctor, that, that.getParameterList(), 
                    classBuilder, classBuilder.getInitBuilder(), generateInstantiator, decl, impl);
            // Note: We don't need to explicitly add the reified type parameters to the ctor
            // in this case because they're already in the initbuilders list of parameters.
            for (ParameterDefinitionBuilder p : classBuilder.getInitBuilder().getParameterList()) {
                ctorDb.parameter(p);
            }
        } else {
            ClassDefinitionBuilder constructorNameClass = ClassDefinitionBuilder.klass(this, 
                    naming.makeTypeDeclarationName(ctor), null, true);
            int classMods = transformConstructorDeclFlags(ctor);
            JCVariableDecl constructorNameConst;
            if (clz.isToplevel() || 
                    (clz.isMember() && Decl.isToplevel((Declaration)clz.getContainer()))) {
                classMods |= FINAL | STATIC;
                constructorNameConst = make().VarDef(make().Modifiers(classMods, makeAtIgnore()),
                        names().fromString(naming.makeTypeDeclarationName(ctor)),
                        naming.makeTypeDeclarationExpression(null, ctor, DeclNameFlag.QUALIFIED), 
                        makeNull());
            } else {
                classMods &= ~(PRIVATE | PROTECTED | PUBLIC);
                constructorNameConst = null;
            }
            constructorNameClass.modifiers(classMods);
            constructorNameClass.annotations(makeAtIgnore());
            constructorNameClass.getInitBuilder().modifiers(PRIVATE);
            
            if (clz.isToplevel()) {
                result.addAll(constructorNameClass.build());
                classBuilder.defs(constructorNameConst);
            } else if (clz.isClassMember()){
                classBuilder.getContainingClassBuilder().defs(constructorNameClass.build());
                classBuilder.getContainingClassBuilder().defs(constructorNameConst);
            } else if (clz.isInterfaceMember()){
                classBuilder.getContainingClassBuilder().getCompanionBuilder(clz).defs(constructorNameClass.build());
                classBuilder.getContainingClassBuilder().getCompanionBuilder(clz).defs(constructorNameConst);
            } else {
                result.addAll(constructorNameClass.build());
            }
            
            for (TypeParameter tp : clz.getTypeParameters()) {
                ctorDb.reifiedTypeParameter(tp);
            }
            
            transformClassOrCtorParameters(null, (Class)ctor.getContainer(), ctor, that, that.getParameterList(), 
                    classBuilder, ctorDb, generateInstantiator, decl, impl);
            
        }
        
        List<JCStatement> ctorBody = classBuilder.getInitBuilder().getBody(that, classBuilder);
        at(that);
        ctorDb.block(make().Block(0, ctorBody));
        result.add(ctorDb.build());
        return result.toList();
    }

}
