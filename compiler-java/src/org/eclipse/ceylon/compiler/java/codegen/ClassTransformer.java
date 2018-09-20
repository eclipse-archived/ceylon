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

package org.eclipse.ceylon.compiler.java.codegen;

import static org.eclipse.ceylon.compiler.java.codegen.AttributeDefinitionBuilder.field;
import static org.eclipse.ceylon.compiler.java.codegen.AttributeDefinitionBuilder.getter;
import static org.eclipse.ceylon.compiler.java.codegen.AttributeDefinitionBuilder.setter;
import static org.eclipse.ceylon.compiler.java.codegen.ClassDefinitionBuilder.klass;
import static org.eclipse.ceylon.compiler.java.codegen.ClassDefinitionBuilder.methodWrapper;
import static org.eclipse.ceylon.compiler.java.codegen.ClassDefinitionBuilder.object;
import static org.eclipse.ceylon.compiler.java.codegen.MethodDefinitionBuilder.constructor;
import static org.eclipse.ceylon.compiler.java.codegen.MethodDefinitionBuilder.method;
import static org.eclipse.ceylon.compiler.java.codegen.MethodDefinitionBuilder.systemMethod;
import static org.eclipse.ceylon.compiler.java.codegen.Naming.DeclNameFlag.QUALIFIED;
import static org.eclipse.ceylon.compiler.java.codegen.ParameterDefinitionBuilder.explicitParameter;
import static org.eclipse.ceylon.compiler.java.codegen.ParameterDefinitionBuilder.implicitParameter;
import static org.eclipse.ceylon.compiler.java.codegen.ParameterDefinitionBuilder.systemParameter;
import static org.eclipse.ceylon.langtools.tools.javac.code.Flags.ABSTRACT;
import static org.eclipse.ceylon.langtools.tools.javac.code.Flags.FINAL;
import static org.eclipse.ceylon.langtools.tools.javac.code.Flags.INTERFACE;
import static org.eclipse.ceylon.langtools.tools.javac.code.Flags.PRIVATE;
import static org.eclipse.ceylon.langtools.tools.javac.code.Flags.PROTECTED;
import static org.eclipse.ceylon.langtools.tools.javac.code.Flags.PUBLIC;
import static org.eclipse.ceylon.langtools.tools.javac.code.Flags.STATIC;
import static org.eclipse.ceylon.langtools.tools.javac.code.Flags.TRANSIENT;
import static org.eclipse.ceylon.langtools.tools.javac.code.Flags.VARARGS;

import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.antlr.runtime.Token;
import org.eclipse.ceylon.compiler.java.codegen.MethodDefinitionBuilder.NonWideningParam;
import org.eclipse.ceylon.compiler.java.codegen.MethodDefinitionBuilder.WideningRules;
import org.eclipse.ceylon.compiler.java.codegen.Naming.DeclNameFlag;
import org.eclipse.ceylon.compiler.java.codegen.Naming.Substitution;
import org.eclipse.ceylon.compiler.java.codegen.Naming.SyntheticName;
import org.eclipse.ceylon.compiler.java.codegen.StatementTransformer.DeferredSpecification;
import org.eclipse.ceylon.compiler.java.codegen.Strategy.DefaultParameterMethodOwner;
import org.eclipse.ceylon.compiler.java.codegen.recovery.Drop;
import org.eclipse.ceylon.compiler.java.codegen.recovery.Errors;
import org.eclipse.ceylon.compiler.java.codegen.recovery.Generate;
import org.eclipse.ceylon.compiler.java.codegen.recovery.HasErrorException;
import org.eclipse.ceylon.compiler.java.codegen.recovery.PrivateConstructorOnly;
import org.eclipse.ceylon.compiler.java.codegen.recovery.ThrowerCatchallConstructor;
import org.eclipse.ceylon.compiler.java.codegen.recovery.ThrowerMethod;
import org.eclipse.ceylon.compiler.java.codegen.recovery.TransformationPlan;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.AnnotationList;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.AttributeGetterDefinition;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.AttributeSetterDefinition;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.BaseMemberExpression;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.FunctionArgument;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.MethodDeclaration;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.SequencedArgument;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.SpecifierExpression;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.SpecifierStatement;
import org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil;
import org.eclipse.ceylon.langtools.tools.javac.code.Flags;
import org.eclipse.ceylon.langtools.tools.javac.code.TypeTag;
import org.eclipse.ceylon.langtools.tools.javac.jvm.Target;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCAnnotation;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCBinary;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCBlock;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCCase;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCExpression;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCExpressionStatement;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCFieldAccess;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCIdent;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCNewClass;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCPrimitiveTypeTree;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCReturn;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCStatement;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCSwitch;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCThrow;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCVariableDecl;
import org.eclipse.ceylon.langtools.tools.javac.util.Context;
import org.eclipse.ceylon.langtools.tools.javac.util.List;
import org.eclipse.ceylon.langtools.tools.javac.util.ListBuffer;
import org.eclipse.ceylon.model.loader.NamingBase;
import org.eclipse.ceylon.model.loader.NamingBase.Suffix;
import org.eclipse.ceylon.model.loader.NamingBase.Unfix;
import org.eclipse.ceylon.model.loader.model.AnnotationTarget;
import org.eclipse.ceylon.model.loader.model.JavaBeanValue;
import org.eclipse.ceylon.model.loader.model.LazyInterface;
import org.eclipse.ceylon.model.loader.model.OutputElement;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.ClassAlias;
import org.eclipse.ceylon.model.typechecker.model.ClassOrInterface;
import org.eclipse.ceylon.model.typechecker.model.Constructor;
import org.eclipse.ceylon.model.typechecker.model.ControlBlock;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.FunctionOrValue;
import org.eclipse.ceylon.model.typechecker.model.Functional;
import org.eclipse.ceylon.model.typechecker.model.Generic;
import org.eclipse.ceylon.model.typechecker.model.Interface;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Package;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.ParameterList;
import org.eclipse.ceylon.model.typechecker.model.Reference;
import org.eclipse.ceylon.model.typechecker.model.Scope;
import org.eclipse.ceylon.model.typechecker.model.Setter;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeAlias;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypeParameter;
import org.eclipse.ceylon.model.typechecker.model.TypedDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypedReference;
import org.eclipse.ceylon.model.typechecker.model.Unit;
import org.eclipse.ceylon.model.typechecker.model.Value;

/**
 * This transformer deals with class/interface declarations
 */
public class ClassTransformer extends AbstractTransformer {
    
    private static final Comparator<Declaration> DeclarationComparator = new Comparator<Declaration>(){
        @Override
        public int compare(Declaration a, Declaration b) {
            int cmp = a.getQualifiedNameString().compareTo(b.getQualifiedNameString());
            if (cmp != 0) {
                return cmp;
            }
            // getters and setters are distinct, but have the same name
            if (a.getDeclarationKind() != null && b.getDeclarationKind() != null) {
                cmp = a.getDeclarationKind().compareTo(b.getDeclarationKind());
                if (cmp != 0) {
                    return cmp;
                }
            }
            if (a instanceof Function
                    && b instanceof Function) {
                // They could be java overloaded methods, so we have to compare parameter lists
                Function afn = (Function)a;
                Function bfn = (Function)b;
                java.util.List<ParameterList> afpls = afn.getParameterLists();
                java.util.List<ParameterList> bfpls = bfn.getParameterLists();
                if (afpls != null && !afpls.isEmpty()
                        && bfpls != null && !bfpls.isEmpty()) {
                    java.util.List<Parameter> apl = afn.getFirstParameterList().getParameters();
                    java.util.List<Parameter> bpl = bfn.getFirstParameterList().getParameters();
                    if (apl.size() < bpl.size()) {
                        return -1;
                    } else if (apl.size() > bpl.size()) {
                        return 1;
                    } else {
                        Iterator<Parameter> aiter = apl.iterator();
                        Iterator<Parameter> biter = bpl.iterator();
                        while (aiter.hasNext()) {
                            if (biter.hasNext()) {
                                // make sure to compare parameter *types*, not the parameters themselves
                                cmp = this.compare(aiter.next().getModel().getTypeDeclaration(),
                                        biter.next().getModel().getTypeDeclaration());
                                if (cmp != 0) {
                                    return cmp;
                                }
                            } else {
                                return 1;
                            }
                        }
                        if (biter.hasNext()) {
                            return -1;
                        }
                    }
                } else if (afpls != null && !afpls.isEmpty()
                        && (bfpls == null || bfpls.isEmpty())) {
                    return -1;
                } else if ((afpls == null || afpls.isEmpty())
                        && bfpls != null && !bfpls.isEmpty()) {
                    return 1;
                } else {
                    return 0;
                }
            }
            return cmp;
        }
    };

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
        if (model.isToplevel() && isEe(model)) {
            replaceModifierTransformation(new EeModifierTransformation());
        }
        
        // we only create types for aliases so they can be imported with the model loader
        // and since we can't import local declarations let's just not create those types
        // in that case
        if(model.isAlias() && Decl.isAncestorLocal(model))
            return List.nil();
        
        naming.clearSubstitutions(model);
        final String javaClassName;
        String ceylonClassName = def.getIdentifier().getText();
        if (def instanceof Tree.AnyInterface) {
            javaClassName = naming.makeTypeDeclarationName(model, QUALIFIED).replaceFirst(".*\\.", "");
        } else {
            javaClassName = Naming.quoteClassName(ceylonClassName);
        }
        ClassDefinitionBuilder instantiatorImplCb;
        ClassDefinitionBuilder instantiatorDeclCb;
        if (model.isInterfaceMember()) {
            instantiatorImplCb = gen().current().getCompanionBuilder((Interface)model.getContainer());
            instantiatorDeclCb = gen().current();
        } else {
            instantiatorImplCb = gen().current();
            instantiatorDeclCb = null;
        }
        ClassDefinitionBuilder classBuilder =
                 klass(this, javaClassName, ceylonClassName, Decl.isLocal(model))
                .forDefinition(model)
                .hasDelegatingConstructors(CodegenUtil.hasDelegatingConstructors(def));
        
        classBuilder.getInitBuilder().deprecated(model.isDeprecated());
        
        // Very special case for Anything
        if (model.isAnything()) {
            classBuilder.extending(model.getType(), null);
        }
        
        if (def instanceof Tree.AnyClass) {
            classBuilder.getInitBuilder().modifiers(modifierTransformation().constructor(model));
            Tree.AnyClass classDef = (Tree.AnyClass)def;
            Class cls = classDef.getDeclarationModel();
            // Member classes need a instantiator method
            boolean generateInstantiator = Strategy.generateInstantiator(cls);
            if (!cls.hasConstructors()) {
                classBuilder.getInitBuilder().userAnnotations(expressionGen().transformAnnotations(OutputElement.CONSTRUCTOR, def));
            }
            if(generateInstantiator
                    && !cls.hasConstructors()
                    && !cls.hasEnumerated()){
                if (!cls.isStatic()) {
                    classBuilder.getInitBuilder().modifiers(PROTECTED);
                }
                generateInstantiators(classBuilder, cls, null, instantiatorDeclCb, instantiatorImplCb, classDef, classDef.getParameterList());
            }
            
            classBuilder.annotations(expressionGen().transformAnnotations(OutputElement.TYPE, def));
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
            classBuilder.annotations(expressionGen().transformAnnotations(OutputElement.TYPE, def));
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
        
        transformTypeParameters(classBuilder, model);
        
        // Transform the class/interface members
        List<JCStatement> childDefs = visitClassOrInterfaceDefinition(def, classBuilder);
        // everything else is synthetic
        at(null);
        TransformationPlan plan = errors().hasDeclarationError(def);
        if (plan instanceof ThrowerCatchallConstructor) {
            classBuilder.broken();
            MethodDefinitionBuilder initBuilder = classBuilder.noInitConstructor().addConstructor(model.isDeprecated());
            initBuilder.body(statementGen().makeThrowUnresolvedCompilationError(plan.getErrorMessage().getMessage()));
            // Although we have the class pl which we could use we don't know 
            // that it won't collide with the default named constructor's pl
            // which would cause a javac error about two constructors with the same sig
            // so we generate a Object... here. There's still a risk of collision though
            // when the default constructor has pl (ObjectArray).
            ParameterDefinitionBuilder pdb = implicitParameter(this, "ignored");
            pdb.modifiers(VARARGS);
            pdb.type(new TransformedType(make().TypeArray(make().Type(syms().objectType))));
            initBuilder.parameter(pdb);
        } else if (plan instanceof PrivateConstructorOnly) {
            classBuilder.broken();
            MethodDefinitionBuilder initBuilder = classBuilder.noInitConstructor().addConstructor(model.isDeprecated());
            initBuilder.body(statementGen().makeThrowUnresolvedCompilationError(plan.getErrorMessage().getMessage()));
            initBuilder.modifiers(PRIVATE);
        }
        // If it's a Class without initializer parameters...
        if (Strategy.generateMain(def)) {
            // ... then add a main() method
            classBuilder.method(makeMainForClass(model));
        }
        classBuilder
            .modelAnnotations(model.getAnnotations())
            .modifiers(modifierTransformation().classFlags(model))
            .satisfies(model.getSatisfiedTypes())
            .caseTypes(model.getCaseTypes(), model.getSelfType())
            .defs(childDefs);
        
        // aliases and native headers don't need a $getType method
        if(!model.isAlias()){
            // only classes get a $getType method
            if(model instanceof Class)
                classBuilder.addGetTypeMethod(model.getType());
            if(supportsReifiedAlias(model))
                classBuilder.reifiedAlias(model.getType());
        }
        
        // Now, once all the fields have been added,
        // we can add things which depend on knowing all the fields
        if (Strategy.generateJpaCtor(model) 
                && plan instanceof Generate) {
            buildJpaConstructor((Class)model, classBuilder);
        }
        
        if (model instanceof Class
                && !(model instanceof ClassAlias)
                && plan instanceof Generate) {
            Class c = (Class)model;
            if (Strategy.introduceJavaIoSerializable(c, typeFact().getJavaIoSerializable())) {
                classBuilder.introduce(make().QualIdent(syms().serializableType.tsym));
                if (Strategy.useSerializationProxy(c)
                        && noValueConstructorErrors((Tree.ClassDefinition)def)) {
                    at(def);
                    addWriteReplace(c, classBuilder);
                }
            }
            serialization(c, classBuilder);
        }
        
        // reset position before initializer constructor is generated. 
        at(def);
        classBuilder.at(def);
        List<JCTree> result;
        if (Decl.isAnnotationClass(def.getDeclarationModel())) {
            ListBuffer<JCTree> trees = new ListBuffer<JCTree>();
            trees.addAll(transformAnnotationClass((Tree.AnyClass)def));
            transformAnnotationClassConstructor((Tree.AnyClass)def, classBuilder);
            // you only need that method if you satisfy Annotation which is erased to j.l.a.Annotation
            if(model.inherits(typeFact().getAnnotationDeclaration()))
                classBuilder.addAnnotationTypeMethod(model.getType());
            trees.addAll(classBuilder.build());
            result = trees.toList();
        } else {
            result = classBuilder.build();
        }
        
        if (model.isToplevel() && isEe(model)) {
            replaceModifierTransformation(new ModifierTransformation());
        }
        
        return result;
    }

    private boolean noValueConstructorErrors(Tree.ClassDefinition def) {
        for (Tree.Statement s : def.getClassBody().getStatements()) {
            if (s instanceof Tree.Constructor
                    || s instanceof Tree.Enumerated) {
                if (errors().hasAnyError((Tree.Declaration)s)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Adds a write replace method which replaces value constructor instances 
     * with a SerializationProxy
     * @param model
     * @param classBuilder
     */
    protected void addWriteReplace(final Class model,
            ClassDefinitionBuilder classBuilder) {
        MethodDefinitionBuilder mdb = systemMethod(this, "writeReplace");
        mdb.resultType(new TransformedType(make().Type(syms().objectType), null, makeAtNonNull()));
        mdb.modifiers(PRIVATE | FINAL);
        ListBuffer<JCStatement> stmts = new ListBuffer<JCStatement>();
        SyntheticName name = naming.synthetic(Unfix.$name$);
        stmts.add(makeVar(FINAL, name, make().Type(syms().stringType), null));
        if (model.hasEnumerated()) {
            JCStatement tail;
            if (Decl.hasOnlyValueConstructors(model)) {
                tail = make().Throw(statementGen().makeNewEnumeratedTypeError("Instance not of any constructor"));
            } else {
                tail =  make().Return(naming.makeThis());
            }
            for (Declaration member : model.getMembers()) {
                if (Decl.isValueConstructor(member) ) {
                    Value val = (Value)member;
                    tail = make().If(
                            make().Binary(JCTree.Tag.EQ, naming.makeThis(), 
                                    naming.getValueConstructorFieldName(val).makeIdent()), 
                            make().Block(0, List.<JCStatement>of(make().Exec(make().Assign(name.makeIdent(), make().Literal(Naming.getGetterName(member)))))), 
                            tail);
                }
            }
            
            stmts.add(tail);
        } else if (model.isAnonymous()) {
            stmts.add(make().Exec(make().Assign(name.makeIdent(), make().Literal(Naming.getGetterName((Value)model.getContainer().getDirectMember(model.getName(), null, false))))));
        } else {
            throw new BugException("Unsupported need for writeReplace()");
        }
        // final String name;
        // if(this == instA) {
        //    name = "getInstA";
        // } // ... else { throw new 
        
        // return new SerializationProxy(outer, Foo.clazz, name);
        List<JCExpression> args = List.<JCExpression>of(name.makeIdent());
        if (model.isMember() && !model.isStatic()) {
            ClassOrInterface outer = (ClassOrInterface)model.getContainer();
            args = args.prepend(makeClassLiteral(outer.getType()));
            args = args.prepend(naming.makeQualifiedThis(naming.makeTypeDeclarationExpression(null, outer, DeclNameFlag.QUALIFIED)));
        } else {
            args = args.prepend(makeClassLiteral(model.getType())); 
        }
        stmts.add(make().Return(make().NewClass(null, null,
                make().QualIdent(syms().ceylonSerializationProxyType.tsym),
                args, 
                null)));
        mdb.body(stmts.toList());
        classBuilder.method(mdb);
    }

    protected void buildJpaConstructor(Class model, ClassDefinitionBuilder classBuilder) {
        MethodDefinitionBuilder ctor = classBuilder.addConstructor(model.isDeprecated());
        ctor.modelAnnotations(makeAtJpa());
        ctor.modelAnnotations(makeAtIgnore());
        ctor.modifiers(modifierTransformation().jpaConstructor(model));
        for (TypeParameter tp : model.getTypeParameters()) {
            ctor.reifiedTypeParameter(tp);
        }
        
        final ListBuffer<JCStatement> stmts = new ListBuffer<JCStatement>();
        
        // invoke super (or this if
        ListBuffer<JCExpression> superArgs = new ListBuffer<JCExpression>();
        if (model.isSerializable()) {
            superArgs.add(make().TypeCast(make().QualIdent(syms().ceylonSerializationType.tsym),
                    makeNull()));
            for (JCExpression ta : makeReifiedTypeArguments(model.getType())) {
                superArgs.add(ta);
            }
        } else {
            for (JCExpression ta : makeReifiedTypeArguments(model.getExtendedType())) {
                superArgs.add(ta);
            }
        }
        stmts.add(make().Exec(make().Apply(null,
                model.isSerializable() ? naming.makeThis() : naming.makeSuper(),
                superArgs.toList())));
        if (!model.isSerializable()) {
            buildFieldInits(model, classBuilder, stmts);
        }
        ctor.body(stmts.toList());
    }

    /** 
     * Recover from members not being refined in the class hierarchy 
     * by generating a stub method that throws.
     */
    private void addMissingUnrefinedMembers(
            Node def,
            Class classModel,
            ClassDefinitionBuilder classBuilder) {
        
        for (Reference unrefined : classModel.getUnimplementedFormals()) {
            Declaration formalMember = unrefined.getDeclaration();//classModel.getMember(memberName, null, false);
            String errorMessage = 
                    "formal member '"
                    + formalMember.getName()
                    + "' of '"
                    + ((TypeDeclaration)formalMember.getContainer()).getName()
                    + "' not implemented in class hierarchy";
            java.util.List<Type> params = new java.util.ArrayList<Type>();
            for (TypeParameter tp: formalMember.getTypeParameters()) {
                params.add(tp.getType());
            }
            if (formalMember instanceof Value) {
                addRefinedThrowerAttribute(classBuilder, errorMessage, def, classModel,
                        (Value)formalMember);
            } else if (formalMember instanceof Function) {
                addRefinedThrowerMethod(classBuilder, errorMessage, classModel,
                        (Function)formalMember);
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
            String message, ClassOrInterface classModel, Class formalClass, Reference unrefined) {
        MethodDefinitionBuilder mdb = systemMethod(this, 
                naming.getInstantiatorMethodName(formalClass));
        mdb.modifiers(modifierTransformation().classFlags(formalClass) &~ABSTRACT);
        for (TypeParameter tp: formalClass.getTypeParameters()) {
            mdb.typeParameter(tp);
            mdb.reifiedTypeParameter(tp);
        }
        for (Parameter formalP : formalClass.getParameterList().getParameters()) {
            ParameterDefinitionBuilder pdb = systemParameter(this, formalP.getName());
            pdb.sequenced(formalP.isSequenced());
            pdb.defaulted(formalP.isDefaulted());
            pdb.type(new TransformedType(makeJavaType(unrefined.getTypedParameter(formalP).getType())));
            mdb.parameter(pdb);
        }
        mdb.resultType(makeJavaType(unrefined.getType()), null);
        mdb.body(makeThrowUnresolvedCompilationError(message));
        classBuilder.method(mdb);
    }

    private Iterable<java.util.List<Parameter>> overloads(Functional f) {
        java.util.List<java.util.List<Parameter>> result = new ArrayList<java.util.List<Parameter>>();
        java.util.List<Parameter> parameters = f.getFirstParameterList().getParameters();
        result.add(parameters);
        int ii = 0;
        for (Parameter p : parameters) {
            if (p.isDefaulted()
                    || (p.isSequenced() && !p.isAtLeastOne())) {
                result.add(parameters.subList(0, ii));
            }
            ii++;
        }
        return result;
    }
    
    private void addRefinedThrowerMethod(ClassDefinitionBuilder classBuilder,
            String error, ClassOrInterface classModel,
            Function formalMethod) {
        Function refined = refineMethod(classModel, 
                classModel.getType().getTypedMember(formalMethod, Collections.<Type>emptyList()),
                classModel, formalMethod, classModel.getUnit());
        // TODO When the method in inherited from an interface and is defaulted 
        // we need to generate a DPM as well, otherwise the class is missing
        // the DPM and javac barfs.
        for (java.util.List<Parameter> parameterList : overloads(refined)) {
            MethodDefinitionBuilder mdb = method(this, refined);
            mdb.isOverride(true);
            mdb.modifiers(modifierTransformation().method(refined));
            for (TypeParameter tp: formalMethod.getTypeParameters()) {
                mdb.typeParameter(tp);
                mdb.reifiedTypeParameter(tp);
            }
            for (Parameter param : parameterList) {
                mdb.parameter(null, param, null, 0, WideningRules.NONE);
            }
            mdb.resultType(refined, 0);
            mdb.body(makeThrowUnresolvedCompilationError(error));
            classBuilder.method(mdb);
        }
    }

    /*private Class refineClass(
            Scope container,
            Reference pr,
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
                final TypedReference typedParameter = pr.getTypedParameter(formalP);
                FunctionOrValue paramModel;
                if (formalP.getModel() instanceof Value) {
                    Value paramValueModel = refineValue((Value)formalP.getModel(), typedParameter, refined, classModel.getUnit());
                    paramValueModel.setInitializerParameter(refinedP);
                    paramModel = paramValueModel;
                } else {
                    Function paramFunctionModel = refineMethod(refined, typedParameter, classModel, (Function)formalP.getModel(), unit);
                    paramFunctionModel.setInitializerParameter(refinedP);
                    paramModel = paramFunctionModel; 
                }
                refinedP.setModel(paramModel);
                refinedPl.getParameters().add(refinedP);
            }
            refined.addParameterList(refinedPl);
        }
        return refined;
    }*/
    
    private Function refineMethod(
            Scope container,
            TypedReference pr,
            ClassOrInterface classModel, 
            Function formalMethod,
            Unit unit) {
        Function refined = new Function();
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
                final TypedReference typedParameter = pr.getTypedParameter(formalP);
                FunctionOrValue paramModel;
                if (formalP.getModel() instanceof Value) {
                    Value paramValueModel = refineValue((Value)formalP.getModel(), typedParameter, refined, classModel.getUnit());
                    paramValueModel.setInitializerParameter(refinedP);
                    paramModel = paramValueModel;
                } else {
                    Function paramFunctionModel = refineMethod(refined, typedParameter, classModel, (Function)formalP.getModel(), unit);
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
            Node node,
            ClassOrInterface classModel, Value formalAttribute) {
        Value refined = refineValue(formalAttribute, formalAttribute.appliedTypedReference(null, null), classModel, classModel.getUnit());
        AttributeDefinitionBuilder getterBuilder = getter(this, refined.getName(), refined);
        getterBuilder.skipField();
        getterBuilder.modifiers(modifierTransformation().getterSetter(refined, false));
        getterBuilder.getterBlock(make().Block(0, List.<JCStatement>of(makeThrowUnresolvedCompilationError(error))));
        classBuilder.attribute(getterBuilder);
        if (formalAttribute.isVariable()) {
            AttributeDefinitionBuilder setterBuilder = setter(this, node, refined.getName(), refined);
            setterBuilder.skipField();
            setterBuilder.modifiers(modifierTransformation().getterSetter(refined, false));
            setterBuilder.setterBlock(make().Block(0, List.<JCStatement>of(makeThrowUnresolvedCompilationError(error))));
            classBuilder.attribute(setterBuilder);
        }
    }

    private Value refineValue(Value formalAttribute,
            TypedReference producedValue,
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
        Type aliasedClass = model.getExtendedType();
        TypeDeclaration classOrCtor = def.getClassSpecifier().getType().getDeclarationModel();
        Constructor constructor = classOrCtor instanceof Constructor ? (Constructor)classOrCtor : null;
        classBuilder.annotations(makeAtAlias(aliasedClass, constructor));
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
            cbInstantiator = classBuilder.getContainingClassBuilder().getCompanionBuilder(ModelUtil.getClassOrInterfaceContainer(model, true));
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
            final Tree.AnyClass def, Class model, Type aliasedClass) {
        MethodDefinitionBuilder instantiator = systemMethod(this, NamingBase.getAliasInstantiatorMethodName(model));
        int f = 0;
        if (Strategy.defaultParameterMethodStatic(def.getDeclarationModel())) {
            f = STATIC;
        }
        instantiator.modifiers((modifierTransformation().classFlags(model) & ~FINAL)| f);
        for (TypeParameter tp : typeParametersOfAllContainers(model, false)) {
           instantiator.typeParameter(tp);
        }
        transformTypeParameters(instantiator, model);
        
        instantiator.resultType(new TransformedType(makeJavaType(aliasedClass), null, makeAtNonNull()));
        instantiator.annotationFlags(Annotations.MODEL_AND_USER | Annotations.IGNORE);
        // We need to reify the parameters, at least so they have reified annotations
        
        
        for (final Tree.Parameter param : def.getParameterList().getParameters()) {
            // Overloaded instantiators
            Parameter paramModel = param.getParameterModel();
            at(param);
            transformParameter(instantiator, param, paramModel, Decl.getMemberDeclaration(def, param));
        }
        instantiator.body(make().Throw(makeNewClass(makeJavaType(typeFact().getExceptionType(), JT_CLASS_NEW))));
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
        MethodDefinitionBuilder annoCtor = classBuilder.addConstructor(klass.isDeprecated());
        annoCtor.ignoreModelAnnotations();
        // constructors are never final
        annoCtor.modifiers(modifierTransformation().classFlags(klass) & ~FINAL);
        ParameterDefinitionBuilder pdb = systemParameter(this, "anno");
        pdb.type(new TransformedType(makeJavaType(klass.getType(), JT_ANNOTATION), null, makeAtNonNull()));
        annoCtor.parameter(pdb);
        
        // It's up to the caller to invoke value() on the Java annotation for a sequenced
        // annotation
        
        ListBuffer<JCExpression> args = new ListBuffer<JCExpression>();
        if (!klass.getUnit().getPackage().isLanguagePackage()
            || !classBuilder.getClassName().equals("RestrictedAnnotation")) { //ignore argument to restricted()
            for (Tree.Parameter parameter : def.getParameterList().getParameters()) {
                at(parameter);
                Parameter parameterModel = parameter.getParameterModel();
                JCExpression annoAttr = make().Apply(null, naming.makeQuotedQualIdent(naming.makeUnquotedIdent("anno"),
                        parameter.getParameterModel().getName()),
                        List.<JCExpression>nil());
                Type parameterType = parameterModel.getType();
                JCExpression argExpr;
                if (typeFact().isIterableType(parameterType)
                        && !isCeylonString(parameterType)) {
                    // Convert from array to Sequential
                    Type iteratedType = typeFact().getIteratedType(parameterType);
                    boolean nonEmpty = typeFact().isNonemptyIterableType(parameterType);
                    if (isCeylonBasicType(iteratedType)) {
                        argExpr = utilInvocation().sequentialWrapperBoxed(annoAttr);
                    } else if (Decl.isAnnotationClass(iteratedType.getDeclaration())) {
                        // Can't use Util.sequentialAnnotation becase we need to 'box'
                        // the Java annotations in their Ceylon annotation class
                        argExpr = make().Apply(null, naming.makeUnquotedIdent(naming.getAnnotationSequenceMethodName()), List.of(annoAttr));
                        ListBuffer<JCStatement> stmts = new ListBuffer<JCStatement>();
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
                                                make().Unary(JCTree.Tag.POSTINC, index.makeIdent())), 
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
                                systemMethod(this, naming.getAnnotationSequenceMethodName())
                                    .ignoreModelAnnotations()
                                    .modifiers(PRIVATE | STATIC)
                                    .resultType(new TransformedType(makeJavaType(typeFact().getSequentialType(iteratedType)), null, makeAtNonNull()))
                                    .parameter(systemParameter(this, array.getName())
                                            .type(new TransformedType(make().TypeArray(makeJavaType(iteratedType, JT_ANNOTATION)))))
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
                    if (nonEmpty) {
                        argExpr = make().TypeCast(makeJavaType(parameterType), argExpr);
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
        }
        annoCtor.body(at(def).Exec(
                make().Apply(null, naming.makeThis(), args.toList())));
    }

    private JCNewClass instantiateAnnotationClass(
            Type annotationClass,
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
        ClassDefinitionBuilder annoBuilder = klass(this, annotationName, null, false);
        
        // annotations are never explicitly final in Java
        annoBuilder.modifiers(Flags.ANNOTATION | Flags.INTERFACE | (modifierTransformation().classFlags(klass) & ~FINAL));
        annoBuilder.getInitBuilder().modifiers(modifierTransformation().classFlags(klass) & ~FINAL);
        annoBuilder.annotations(makeAtRetention(RetentionPolicy.RUNTIME));
        annoBuilder.annotations(makeAtIgnore());
        annoBuilder.annotations(expressionGen().transformAnnotations(OutputElement.ANNOTATION_TYPE, def));
        if (isSequencedAnnotation(klass)) { 
            if (getTarget().compareTo(Target.JDK1_8) >= 0) {
                annoBuilder.annotations(makeAtRepeatable(klass.getType()));
                annoBuilder.annotations(transformAnnotationConstraints(klass));
            } else {
                annoBuilder.annotations(makeAtAnnotationTarget(EnumSet.noneOf(AnnotationTarget.class)));
            }
        } else {
            annoBuilder.annotations(transformAnnotationConstraints(klass));
        }
        
        for (Tree.Parameter p : def.getParameterList().getParameters()) {
            annoBuilder.method(makeAnnotationMethod(p));
        }
        List<JCTree> result;
        if (isSequencedAnnotation(klass)) {
            result = annoBuilder.build();
            String wrapperName = Naming.suffixName(Suffix.$annotations$, klass.getName());
            ClassDefinitionBuilder sequencedBuilder = klass(this, wrapperName, null, false);
            // annotations are never explicitly final in Java
            sequencedBuilder.modifiers(Flags.ANNOTATION | Flags.INTERFACE | (modifierTransformation().classFlags(klass) & ~FINAL));
            sequencedBuilder.annotations(makeAtRetention(RetentionPolicy.RUNTIME));
            MethodDefinitionBuilder mdb = systemMethod(this, naming.getSequencedAnnotationMethodName());
            mdb.annotationFlags(Annotations.MODEL_AND_USER);
            mdb.modifiers(PUBLIC | ABSTRACT);
            mdb.resultType(new TransformedType(make().TypeArray(makeJavaType(klass.getType(), JT_ANNOTATION)), null, makeAtNonNull()));
            mdb.noBody();
            ClassDefinitionBuilder sequencedAnnotation = sequencedBuilder.method(mdb);
            sequencedAnnotation.annotations(transformAnnotationConstraints(klass));
            sequencedAnnotation.annotations(makeAtIgnore());
            result = result.appendList(sequencedAnnotation.build());
            
        } else {
            result = annoBuilder.build();
        }
        
        return result;
    }
    
    private List<JCAnnotation> makeAtRetention(RetentionPolicy retentionPolicy) {
        return List.of(
                make().Annotation(
                        make().Type(syms().retentionType), 
                        List.of(naming.makeQuotedQualIdent(make().Type(syms().retentionPolicyType), retentionPolicy.name()))));
    }
    
    private List<JCAnnotation> makeAtRepeatable(Type containerClassLiteral) {
        return List.of(
                make().Annotation(
                        make().Type(syms().repeatableType), 
                        List.of(makeClassLiteral(containerClassLiteral, JT_ANNOTATIONS))));
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
        Type constrainedType = klass.getType().getSupertype(meta);
        EnumSet<AnnotationTarget> types = EnumSet.noneOf(AnnotationTarget.class);
        if (constrainedType != null) {
            Type programElement = constrainedType.getTypeArgumentList().get(2);
            if (programElement.covers(((TypeDeclaration)typeFact().getLanguageModuleDeclarationDeclaration("InterfaceDeclaration")).getType())
                    || programElement.covers(((TypeDeclaration)typeFact().getLanguageModuleDeclarationDeclaration("ClassDeclaration")).getType())
                    || programElement.covers(((TypeDeclaration)typeFact().getLanguageModuleDeclarationDeclaration("ClassWithInitializerDeclaration")).getType())
                    || programElement.covers(((TypeDeclaration)typeFact().getLanguageModuleDeclarationDeclaration("ClassWithConstructorsDeclaration")).getType())
                    || programElement.covers(((TypeDeclaration)typeFact().getLanguageModuleDeclarationDeclaration("Package")).getType())
                    || programElement.covers(((TypeDeclaration)typeFact().getLanguageModuleDeclarationDeclaration("Module")).getType())) {
                types.add(AnnotationTarget.TYPE);
            } 
            if (programElement.covers(((TypeDeclaration)typeFact().getLanguageModuleDeclarationDeclaration("ValueDeclaration")).getType())
                    || programElement.covers(((TypeDeclaration)typeFact().getLanguageModuleDeclarationDeclaration("FunctionDeclaration")).getType())) {
                types.add(AnnotationTarget.METHOD);
                types.add(AnnotationTarget.PARAMETER);
            } 
            if (programElement.covers(((TypeDeclaration)typeFact().getLanguageModuleDeclarationDeclaration("CallableConstructorDeclaration")).getType())) {
                types.add(AnnotationTarget.CONSTRUCTOR);
            } 
            if (programElement.covers(((TypeDeclaration)typeFact().getLanguageModuleDeclarationDeclaration("ValueConstructorDeclaration")).getType())) {
                types.add(AnnotationTarget.METHOD);
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
        Type parameterType = parameter.getParameterModel().getType();
        JCExpression type = null;
        if (isScalarAnnotationParameter(parameterType)) {
            type = makeJavaType(parameterType.withoutUnderlyingType(), JT_ANNOTATION);
        } else if (isMetamodelReference(parameterType)) {
            type = make().Type(syms().stringType);
        } else if (Decl.isEnumeratedTypeWithAnonCases(parameterType)) {
            type = makeJavaClassTypeBounded(parameterType);
        } else if (typeFact().isIterableType(parameterType)) {
            Type iteratedType = typeFact().getIteratedType(parameterType);
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

    private boolean isMetamodelReference(Type parameterType) {
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
        ListBuffer<JCExpression> elements = new ListBuffer<JCExpression>();
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

    private boolean isScalarAnnotationParameter(Type parameterType) {
        return isCeylonBasicType(parameterType)
                || Decl.isAnnotationClass(parameterType.getDeclaration());
    }

    private List<JCStatement> visitClassOrInterfaceDefinition(Node def, ClassDefinitionBuilder classBuilder) {
        // Transform the class/interface members
        CeylonVisitor visitor = gen().visitor;
        
        final ListBuffer<JCTree> prevDefs = visitor.defs;
        final boolean prevInInitializer = visitor.inInitializer;
        final ClassDefinitionBuilder prevClassBuilder = visitor.classBuilder;
        final boolean prevInSynthetic = gen().expressionGen().withinSyntheticClassBody(false);
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
            gen().expressionGen().withinSyntheticClassBody(prevInSynthetic);
            naming.closeScopedSubstitutions(def.getScope());
        }
    }

    private void generateInstantiators(ClassDefinitionBuilder classBuilder, 
            Class cls, Constructor ctor, ClassDefinitionBuilder instantiatorDeclCb, 
            ClassDefinitionBuilder instantiatorImplCb, Tree.Declaration node, Tree.ParameterList pl) {
        // TODO Instantiators on companion classes
        if (ctor != null && ctor.isValueConstructor()) {
            return;
        }
        ParameterList parameterList = ctor != null ? ctor.getFirstParameterList() : cls.getParameterList();
        if (cls.isInterfaceMember()) {
            DefaultedArgumentOverload overloaded = new DefaultedArgumentInstantiator(daoAbstract, cls, ctor, instantiatorDeclCb.isCompanionBuilder());
            MethodDefinitionBuilder instBuilder = overloaded.makeOverload(
                    parameterList,
                    null,
                    cls.getTypeParameters());
            instantiatorDeclCb.method(instBuilder);
        }
        if (!cls.isInterfaceMember()
                || !cls.isFormal()) {
            DefaultedArgumentOverload overloaded = new DefaultedArgumentInstantiator(!cls.isFormal() ? new DaoThis(node, pl) : daoAbstract, 
                    cls, ctor, instantiatorImplCb.isCompanionBuilder());
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
                    null, false, expressionGen().transformAnnotations(OutputElement.FIELD, memberTree));
            classBuilder.getInitBuilder().init(make().Exec(make().Assign(naming.makeQualIdent(naming.makeThis(), decl.getName()), naming.makeName(value, Naming.NA_IDENT))));
        } else if (parameterTree instanceof Tree.ValueParameterDeclaration
                && ModelUtil.isCaptured(value)) {
            makeFieldForParameter(classBuilder, decl, memberTree);
            AttributeDefinitionBuilder adb = getter(this, decl.getName(), decl.getModel());
            adb.modifiers(classGen().modifierTransformation().getterSetter(decl.getModel(), false));
            adb.userAnnotations(expressionGen().transformAnnotations(OutputElement.GETTER, memberTree));
            classBuilder.attribute(adb);
            if (value.isVariable()) {
                AttributeDefinitionBuilder setter = setter(this, parameterTree, decl.getName(), decl.getModel());
                setter.modifiers(classGen().modifierTransformation().getterSetter(decl.getModel(), false));
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
                at(parameterTree);
                JCExpression parameterExpr = makeUnquotedIdent(Naming.getAliasedParameterName(decl));
                TypedReference typedRef = getTypedReference(value);
                TypedReference nonWideningTypedRef = nonWideningTypeDecl(typedRef);
                Type paramType = nonWideningType(typedRef, nonWideningTypedRef);
                AttributeDefinitionBuilder adb = field(this, memberTree, value.getName(), value, false);
                if (!paramType.isExactly(decl.getType())) {
                    // The parameter type follows normal erasure rules, not affected by inheritance
                    // but the attribute respects non-widening rules, so we may need to cast
                    // the parameter to the field type (see #1728)
                    parameterExpr = make().TypeCast(adb.valueFieldType(), parameterExpr);
                }
                at(parameterTree);
                BoxingStrategy exprBoxed = CodegenUtil.isUnBoxed((TypedDeclaration)member) ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED;
                adb.initialValue(parameterExpr, exprBoxed);
                classBuilder.getInitBuilder().init(adb.buildInit(true));
            }
        }
    }
    
    private void makeFieldForParameter(ClassDefinitionBuilder classBuilder,
            Parameter decl, Tree.Declaration annotated) {
        FunctionOrValue model = decl.getModel();
        AttributeDefinitionBuilder adb = field(this, annotated, decl.getName(), decl.getModel(), false);
        adb.fieldAnnotations(makeAtIgnore().prependList(expressionGen().transformAnnotations(OutputElement.FIELD, annotated)));
        adb.fieldNullability(makeNullabilityAnnotations(model));
        adb.modifiers(modifierTransformation().transformClassParameterDeclFlagsField(decl, annotated));
        BoxingStrategy exprBoxed = CodegenUtil.isUnBoxed(model) ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED;
        BoxingStrategy boxingStrategy = useJavaBox(model, model.getType())
                && javaBoxExpression(model.getType(), model.getType())? BoxingStrategy.JAVA : exprBoxed;
        JCExpression paramExpr = boxUnboxIfNecessary(naming.makeName(model, Naming.NA_IDENT_PARAMETER_ALIASED),
                exprBoxed,
                simplifyType(model.getType()),
                boxingStrategy,
                simplifyType(model.getType()));
        if (boxingStrategy == BoxingStrategy.JAVA && !isJavaString(model.getType())) {
            paramExpr = make().Conditional(
                    make().Binary(JCTree.Tag.EQ, 
                            naming.makeName(model, Naming.NA_IDENT_PARAMETER_ALIASED), 
                            makeNull()),
                    makeNull(),
                    paramExpr);
        }

        adb.initialValue(paramExpr, boxingStrategy);
        classBuilder.defs(adb.buildFields());
        classBuilder.getInitBuilder().init(adb.buildInit(true));
    }
    
    /** 
     * Transform the parameter and its annotations and add it to the given builder 
     */
    private void transformParameter(ParameterizedBuilder<?> classBuilder, 
            Tree.Parameter p, Parameter param, Tree.TypedDeclaration member) {
        FunctionOrValue model = param.getModel();
        JCExpression type = makeJavaType(model, param.getType(), 0);
        ParameterDefinitionBuilder pdb = explicitParameter(this, param);
//        pdb.at(p);
        pdb.aliasName(Naming.getAliasedParameterName(param));
        if (Naming.aliasConstructorParameterName(model)) {
            naming.addVariableSubst(model, Naming.suffixName(Suffix.$param$, param.getName()));
        }
        pdb.sequenced(param.isSequenced());
        pdb.defaulted(param.isDefaulted());
        pdb.type(new TransformedType(type, 
                makeJavaTypeAnnotations(model),
                makeNullabilityAnnotations(model)));
        pdb.modifiers(modifierTransformation().transformClassParameterDeclFlags(param));
        if (!ModelUtil.isCaptured(model)) {
            // We load the model for shared parameters from the corresponding member
            pdb.modelAnnotations(model.getAnnotations());
        }
        if (member != null) {
            pdb.userAnnotations(expressionGen().transformAnnotations(OutputElement.PARAMETER, member));
        } else if (p instanceof Tree.ParameterDeclaration &&
                Decl.isConstructor(param.getDeclaration())) {
            pdb.userAnnotations(expressionGen().transformAnnotations(OutputElement.PARAMETER, ((Tree.ParameterDeclaration)p).getTypedDeclaration()));
        }

        if (/*classBuilder instanceof ClassDefinitionBuilder
                &&*/ pdb.requiresBoxedVariableDecl()) {
            ((ClassDefinitionBuilder)classBuilder).getInitBuilder().init(pdb.buildBoxedVariableDecl());
        }
        classBuilder.parameter(pdb);
    }
    
//    private void capturedReifiedTypeParameters(ClassOrInterface model,
//            ClassDefinitionBuilder classBuilder) {
//        if (model.isStatic()) {
//            ClassOrInterface outer = (ClassOrInterface)model.getContainer();
//            capturedReifiedTypeParameters(outer, classBuilder);
//            classBuilder.reifiedTypeParameters(outer.getTypeParameters());
//        }
//    }
    
    private void transformClass(
            Tree.AnyClass def, 
            Class model, 
            ClassDefinitionBuilder classBuilder, 
            Tree.ParameterList paramList, 
            boolean generateInstantiator, 
            ClassDefinitionBuilder instantiatorDeclCb, 
            ClassDefinitionBuilder instantiatorImplCb) {
        // do reified type params first
        //java.util.List<TypeParameter> typeParameters = typeParametersOfAllContainers(model, false);
        //for(TypeParameter tp : typeParameters){
        //    classBuilder.typeParameter(tp, false);
        //}
        //capturedReifiedTypeParameters(model, classBuilder);
        classBuilder.reifiedTypeParameters(Strategy.getEffectiveTypeParameters(model));
        if (def.getParameterList() != null) {
            TransformationPlan error = errors().hasDeclarationAndMarkBrokenness(def);
            if (error instanceof ThrowerCatchallConstructor) {
                InitializerBuilder initBuilder = classBuilder.getInitBuilder();
                initBuilder.init(make().If(make().Literal(true),statementGen().makeThrowUnresolvedCompilationError(error.getErrorMessage().getMessage()), null));
            }
            for (Tree.Parameter param : def.getParameterList().getParameters()) {
                Tree.TypedDeclaration member = def != null ? Decl.getMemberDeclaration(def, param) : null;
                makeAttributeForValueParameter(classBuilder, param, member);
                makeMethodForFunctionalParameter(classBuilder, param, member);
            }
            transformClassOrCtorParameters(def, model, null, def, def.getParameterList(), false,
                    classBuilder,
                    classBuilder.getInitBuilder(), 
                    generateInstantiator, instantiatorDeclCb,
                    instantiatorImplCb);
        }
        satisfaction(def.getSatisfiedTypes(), model, classBuilder);
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
            boolean delegationConstructor,
            ClassDefinitionBuilder classBuilder,
            ParameterizedBuilder<?> constructorBuilder,
            boolean generateInstantiator, 
            ClassDefinitionBuilder instantiatorDeclCb,
            ClassDefinitionBuilder instantiatorImplCb) {
        
        for (final Tree.Parameter param : paramList.getParameters()) {
            // Overloaded instantiators
            
            Parameter paramModel = param.getParameterModel();
            Parameter refinedParam = CodegenUtil.findParamForDecl(
                    (TypedDeclaration)CodegenUtil.getTopmostRefinedDeclaration(param.getParameterModel().getModel()));
            at(param);
            Tree.TypedDeclaration member = def != null ? Decl.getMemberDeclaration(def, param) : null;
            // transform the parameter and its annotations
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
                    cbForDevaultValues = classBuilder.getContainingClassBuilder().getCompanionBuilder(ModelUtil.getClassOrInterfaceContainer(cls, true));
                    if ((constructor == null || constructor.isShared())
                            && cls.isShared()) {
                        cbForDevaultValuesDecls = classBuilder.getContainingClassBuilder();
                    }
                    break;
                default:
                    cbForDevaultValues = classBuilder.getCompanionBuilder(cls);
                }
                if (!delegationConstructor) {
                    if ((Strategy.hasDefaultParameterValueMethod(paramModel) 
                                || (refinedParam != null && Strategy.hasDefaultParameterValueMethod(refinedParam)))) { 
                        if (!generateInstantiator || Decl.equal(refinedParam, paramModel)) {
                            // transform the default value into a method
                            cbForDevaultValues.method(makeParamDefaultValueMethod(false, constructor != null ? constructor : cls, paramList, param));
                            if (cbForDevaultValuesDecls != null) {
                                cbForDevaultValuesDecls.method(makeParamDefaultValueMethod(true, constructor != null ? constructor : cls, paramList, param));
                            }
                        } else if (Strategy.hasDelegatedDpm(cls) && cls.getContainer() instanceof Class) {
                            // generate a dpm which delegates to the companion
                            java.util.List<Parameter> parameters = paramList.getModel().getParameters();
                            MethodDefinitionBuilder mdb = 
                            makeDelegateToCompanion((Interface)cls.getRefinedDeclaration().getContainer(),
                                    constructor != null ? constructor.getReference() : cls.getReference(),
                                    ((TypeDeclaration)cls.getContainer()).getType(),
                                    FINAL | (modifierTransformation().classFlags(cls) & ~ABSTRACT), 
                                    List.<TypeParameter>nil(), Collections.<java.util.List<Type>>emptyList(),
                                    paramModel.getType().getFullType(), 
                                    Naming.getDefaultedParamMethodName(cls, paramModel),
                                    parameters.subList(0, parameters.indexOf(paramModel)), 
                                    false, 
                                    Naming.getDefaultedParamMethodName(cls, paramModel),
                                    param.getParameterModel());
                            cbForDevaultValues.method(mdb);
                        }
                    }
                }
                 
                boolean addOverloadedConstructor = false;
                if (generateInstantiator) {
                    if (cls.isInterfaceMember()) {
                        
                        MethodDefinitionBuilder instBuilder = new DefaultedArgumentInstantiator(daoAbstract, cls, constructor,
                                instantiatorDeclCb.isCompanionBuilder()).makeOverload(
                                paramList.getModel(),
                                param.getParameterModel(),
                                cls.getTypeParameters());
                        instantiatorDeclCb.method(instBuilder);
                    }
                    if (!cls.isInterfaceMember() || !cls.isFormal()) {
                        MethodDefinitionBuilder instBuilder = new DefaultedArgumentInstantiator(new DaoThis(node, paramList), cls, constructor,
                                instantiatorImplCb.isCompanionBuilder()).makeOverload(
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
//                    MethodDefinitionBuilder overloadBuilder;
                    DefaultedArgumentConstructor dac;
                    if (constructor != null) {
                        dac = new DefaultedArgumentConstructor(classBuilder.addConstructor(constructor.isDeprecated()), constructor, node, paramList, delegationConstructor);
                    } else {
                        dac = new DefaultedArgumentConstructor(classBuilder.addConstructor(cls.isDeprecated()), cls, node, paramList, delegationConstructor);
                    }
//                    overloadBuilder = 
                    dac.makeOverload(
                            paramList.getModel(),
                            param.getParameterModel(),
                            cls.getTypeParameters());
                }
            
            }
        }
    }
    
    private ParameterDefinitionBuilder makeConstructorNameParameter(Constructor ctor) {
        return makeConstructorNameParameter(ctor, DeclNameFlag.QUALIFIED);
    }
    
    private ParameterDefinitionBuilder makeConstructorNameParameter(Constructor ctor, DeclNameFlag... flags) {
//        Class clz = (Class)ctor.getContainer();
        ParameterDefinitionBuilder pdb = implicitParameter(this, Naming.Unfix.$name$.toString());
        pdb.ignored();
        JCExpression type = naming.makeTypeDeclarationExpression(null, ctor, flags);
        pdb.type(new TransformedType(type, null, makeAtNullable()));
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
        serializationReferences(model, classBuilder);
        serializationGet(model, classBuilder);
        serializationSet(model, classBuilder);
        
    }
    
    private boolean hasField(Declaration member) {
        if (member instanceof Value) {
            Value value = (Value)member;
            if (!value.isTransient()
                    && !value.isFormal()
                    && !ModelUtil.isConstructor(value)
                    && ModelUtil.isCaptured(value)) {
                return true;
            }
        } /*else if (member instanceof Function) {
            Function function = (Function)member;
            
            if (function.isShortcutRefinement()
                    || (function.isDeferred() && function.isCaptured())
                    || function.isParameter() 
                       && (function.isCaptured()
                           || function.isShared()
                           || function.isActual())) {
                return true;
            }
        }*/
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
    private void serializationConstructor(Class model, 
            ClassDefinitionBuilder classBuilder) {
        MethodDefinitionBuilder ctor = classBuilder.addConstructor(model.isDeprecated());
        ctor.ignoreModelAnnotations();
        ctor.modifiers(PUBLIC);
        
        ParameterDefinitionBuilder serializationPdb = systemParameter(this, "ignored");
        serializationPdb.modifiers(FINAL);
        serializationPdb.type(new TransformedType(make().Type(syms().ceylonSerializationType), null));
        ctor.parameter(serializationPdb);
        
        for (TypeParameter tp : model.getTypeParameters()) {
            ctor.reifiedTypeParameter(tp);
        }
        
        final ListBuffer<JCStatement> stmts = new ListBuffer<JCStatement>();

        if (extendsSerializable(model)) {
            // invoke super
            ListBuffer<JCExpression> superArgs = new ListBuffer<JCExpression>();
            superArgs.add(naming.makeUnquotedIdent("ignored"));
            for (JCExpression ta : makeReifiedTypeArguments(model.getExtendedType())) {
                superArgs.add(ta);
            }
            stmts.add(make().Exec(make().Apply(null,
                    naming.makeSuper(),
                    superArgs.toList())));
        }
        buildFieldInits(model, classBuilder, stmts);
        ctor.body(stmts.toList());
    }

    protected void buildFieldInits(Class model,
            ClassDefinitionBuilder classBuilder,
            final ListBuffer<JCStatement> stmts) {
        final HashSet<String> excludeFields = new HashSet<String>();
        // initialize reified type arguments to according to parameters
        for (TypeParameter tp : model.getTypeParameters()) {
            excludeFields.add(naming.getTypeArgumentDescriptorName(tp));
            stmts.add(makeReifiedTypeParameterAssignment(tp));
        }
        
        // initialize companion instances to a new companion instance
        if (!model.getSatisfiedTypes().isEmpty()) {
            SatisfactionVisitor visitor = new SatisfactionVisitor() {
                
                @Override
                public void satisfiesDirectly(Class model, Interface iface, boolean alreadySatisfied) {
                    if (!alreadySatisfied) {
                        assignCompanion(model, iface);
                    }
                }

                @Override
                public void satisfiesIndirectly(Class model, Interface iface, boolean alreadySatisfied) {
                    if (!alreadySatisfied) {
                        assignCompanion(model, iface);
                    }
                }

                private void assignCompanion(Class model, Interface iface) {
                    if (hasImpl(iface)
                            && excludeFields.add(getCompanionFieldName(iface))) {
                        stmts.add(makeCompanionInstanceAssignment(model, iface, model.getType().getSupertype(iface)));
                    }
                }

                @Override
                public void satisfiesIndirectlyViaClass(Class model,
                        Interface iface, Class via, boolean alreadySatisfied) {
                    // don't care
                }
            };
            walkSatisfiedInterfaces(model, model.getType(), visitor);
        }
        
        // initialize attribute fields to null or a zero
        appendDefaultFieldInits(classBuilder, stmts, excludeFields);
    }

    protected void appendDefaultFieldInits(ClassDefinitionBuilder model,
            final ListBuffer<JCStatement> stmts,
            Collection<String> excludeFields) {
        for (JCVariableDecl field : model.getFields()) {
            String fieldName = field.name.toString();
            if (excludeFields != null && excludeFields.contains(fieldName)) {
                continue;
            }
            if (field.mods != null
                    && (field.mods.flags & STATIC) != 0) {
                continue;
            }
            // initialize all reference fields to null and all primitive 
            // fields to a default value.
            JCExpression nullOrZero;
            if (field.vartype instanceof JCPrimitiveTypeTree) {
                switch (((JCPrimitiveTypeTree)field.vartype).typetag) {
                case BYTE:
                case SHORT:
                case INT:
                    nullOrZero = make().Literal(0);
                    break;
                case LONG:
                    nullOrZero = make().Literal(0L);
                    break;
                case FLOAT:
                    nullOrZero = make().Literal(0.0f);
                    break;
                case DOUBLE:
                    nullOrZero = make().Literal(0.0);
                    break;
                case BOOLEAN:
                    nullOrZero = make().Literal(false);
                    break;
                case CHAR:
                    nullOrZero = make().Literal(TypeTag.CHAR, 0);
                    break;
                default:
                    throw new RuntimeException();
                }
            }else {
                nullOrZero = makeNull();
            }
            stmts.add(make().Exec(make().Assign(
                    naming.makeQualIdent(naming.makeThis(), fieldName), 
                    nullOrZero)));
        }
    }
    
    static interface SatisfactionVisitor {
        /** The class satisfies the interface directly */
        void satisfiesDirectly(Class model, Interface iface, boolean alreadySatisfied);
        /** The class satisfies the interface indirectly via interfaces only */
        void satisfiesIndirectly(Class model, Interface iface, boolean alreadySatisfied);
        /** The class satisfies the interface indirectly via the given superclass */
        void satisfiesIndirectlyViaClass(Class model, Interface iface, Class via, boolean alreadySatisfied);
    }
    
    private void walkSatisfiedInterfaces(final Class model,
            Type type, 
            SatisfactionVisitor visitor) {
        walkSatisfiedInterfacesInternal(model, type, null, visitor, new HashSet<Interface>());
    }
    
    private void walkSatisfiedInterfacesInternal(final Class model,
            Type type,
            Class via, 
            SatisfactionVisitor visitor, 
            Set<Interface> satisfiedInterfaces) {
        type = type.resolveAliases();
        

        Type ext = type.getExtendedType();
        if (ext != null) {
            // recurse up this extended class
            walkSatisfiedInterfacesInternal(model, ext, (Class)ext.getDeclaration(), visitor, satisfiedInterfaces);
        }
        
        for (Type sat : type.getSatisfiedTypes()) {
            if (sat.isIdentifiable()) {
                return;
            }
            
            Interface iface = (Interface)sat.getDeclaration();
            
            // recurse up this satisfies interface
            walkSatisfiedInterfacesInternal(model, sat, via, visitor, satisfiedInterfaces);
            
            boolean alreadySatisfied = !satisfiedInterfaces.add((Interface)model.getType().getSupertype(iface).getDeclaration());
            
            if (via != null) {
                visitor.satisfiesIndirectlyViaClass(model, iface, via, alreadySatisfied);
            } else if (model.getType().equals(type)) {
                visitor.satisfiesDirectly(model, iface, alreadySatisfied);
            } else {
                visitor.satisfiesIndirectly(model, iface, alreadySatisfied);
            }
        }
    }

    private boolean extendsSerializable(Class model) {
        return !typeFact().getObjectType().isExactly(model.getExtendedType())
                && !typeFact().getBasicType().isExactly(model.getExtendedType());
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
    private void serializationReferences(Class model,
            ClassDefinitionBuilder classBuilder) {
        MethodDefinitionBuilder mdb = systemMethod(this, Unfix.$references$.toString());
        mdb.isOverride(true);
        mdb.ignoreModelAnnotations();
        mdb.modifiers(PUBLIC);
        
        mdb.resultType(new TransformedType(make().TypeApply(naming.makeQuotedFQIdent("java.util.Collection"),
                List.<JCExpression>of(make().Type(syms().ceylonReachableReferenceType))), null, makeAtNonNull()));
        
        ListBuffer<JCStatement> stmts = new ListBuffer<JCStatement>();
        // TODO this is all static information, but the method itself needs to be 
        // callable virtually, so we should cache it somehow.
        SyntheticName r = naming.synthetic(Unfix.reference);
        if (extendsSerializable(model)) {
            // prepend the invocation of super.$serialize$()
            stmts.add(makeVar(r, 
                    make().TypeApply(naming.makeQuotedFQIdent("java.util.Collection"),
                            List.<JCExpression>of(make().Type(syms().ceylonReachableReferenceType))), 
                    make().Apply(null,
                    naming.makeQualIdent(naming.makeSuper(), Unfix.$references$.toString()),
                    List.<JCExpression>nil())));
        } else {
            stmts.add(makeVar(r, 
                    make().TypeApply(naming.makeQuotedFQIdent("java.util.Collection"),
                            List.<JCExpression>of(make().Type(syms().ceylonReachableReferenceType))), 
                    make().NewClass(null, null,
                            make().TypeApply(
                                    naming.makeQuotedFQIdent("java.util.ArrayList"),
                                    List.<JCExpression>of(make().Type(syms().ceylonReachableReferenceType))),
                            List.<JCExpression>nil(),
                            null)));
        }
        if (model.isMember()) {
            JCExpressionStatement outer = make().Exec(make().Apply(null,
                    naming.makeQualIdent(r.makeIdent(), "add"),
                    List.<JCExpression>of(
                            make().Apply(null,
                            naming.makeQualIdent(make().Type(syms().ceylonOuterImplType), "get_"), List.<JCExpression>nil()))));
            stmts.add(outer);
        }
        
        
        for (Declaration member : model.getMembers()) {
            if (hasField(member)) {
                // Obtain a ValueDeclaration
                JCExpression valueDeclaration = expressionGen()
                        .makeMemberValueOrFunctionDeclarationLiteral(
                                typeFact().getValueDeclarationType(), 
                                member, false);
                // Create a MemberImpl
                JCExpression mi = make().NewClass(null, null,
                        make().QualIdent(syms().ceylonMemberImplType.tsym),
                        List.of(valueDeclaration),
                        null);
                JCExpressionStatement attribute = make().Exec(make().Apply(null,
                        naming.makeQualIdent(r.makeIdent(), "add"),
                        List.of(mi)));
                stmts.add(attribute);
            }
        }
        stmts.add(make().Return(r.makeIdent()));
        mdb.body(stmts.toList());
        classBuilder.method(mdb);
    }
    
    private void serializationGet(Class model,
            ClassDefinitionBuilder classBuilder) {
        MethodDefinitionBuilder mdb = systemMethod(this, Unfix.$get$.toString());
        mdb.isOverride(true);
        mdb.ignoreModelAnnotations();
        mdb.modifiers(PUBLIC);
        
        ParameterDefinitionBuilder pdb = systemParameter(this, Unfix.reference.toString());
        pdb.modifiers(FINAL);
        pdb.type(new TransformedType(make().Type(syms().ceylonReachableReferenceType), null, makeAtNonNull()));
        mdb.parameter(pdb);
        
        mdb.resultType(new TransformedType(make().Type(syms().objectType), null, makeAtNonNull()));
        /*
         * public void $get$(Object reference, Object instance) {
         *     switch((String)reference) {
         *     case ("attr1")
         *           return ...;
         *     // ... other fields of this class
         *     case ("lateAttr1")
         *           if (!$init$lateAttr1) {
         *               return ceylon.language.serialization.uninitializedLateValue.get_();
         *           }
         *           return ...;
         *     case (null):
         *           return Outer.this;
         *     default:
         *           return super.get(reference);
         */
        ListBuffer<JCCase> cases = new ListBuffer<JCCase>();
        boolean[] needsLookup = new boolean[]{false};
        for (Declaration member : model.getMembers()) {
            if (hasField(member)) {
                if (member instanceof Function)
                    continue; // TODO: This class is not serializable
                ListBuffer<JCStatement> caseStmts = new ListBuffer<JCStatement>();
                if (member instanceof Value
                        && ((Value)member).isLate()) {
                    // TODO this should be encapsulated so the ADB and this
                    // code can just call something common
                    JCExpression test = field(this, null, member.getName(), (Value)member, false).buildUninitTest();
                    if (test != null) {
                        caseStmts.add(make().If(
                                test,
                                make().Return(makeLanguageSerializationValue("uninitializedLateValue")), null));
                    }
                }
                caseStmts.add(make().Return(makeSerializationGetter((Value)member)));
                cases.add(make().Case(make().Literal(member.getQualifiedNameString()), caseStmts.toList()));
            }
        }
        SyntheticName reference = naming.synthetic(Unfix.reference);
        ListBuffer<JCStatement> defaultCase = new ListBuffer<JCStatement>();
        if (extendsSerializable(model)) {
            // super.get(reference);
            defaultCase.add(make().Return(make().Apply(null,
                    naming.makeQualIdent(naming.makeSuper(), Unfix.$get$.toString()),
                    List.<JCExpression>of(reference.makeIdent()))));
        } else {
            // throw (or pass to something else to throw, based on policy)
            defaultCase.add(make().Throw(make().NewClass(null, null,
                    naming.makeQuotedFQIdent("java.lang.RuntimeException"),
                    List.<JCExpression>of(make().Literal("unknown attribute")),
                    null)));
        }
        cases.add(make().Case(null, defaultCase.toList()));
        
        ListBuffer<JCStatement> stmts = new ListBuffer<JCStatement>();
        if (needsLookup[0]) {
            // if we needed to use a lookup object to reset final fields, 
            // prepend that variable
            stmts.add(makeVar(FINAL, 
                "lookup", 
                naming.makeQualIdent(make().Type(syms().methodHandlesType), "Lookup"), 
                make().Apply(null, naming.makeQuotedFQIdent("java.lang.invoke.MethodHandles.lookup"), List.<JCExpression>nil())));
        }
        
        JCSwitch swtch = make().Switch(
                make().Apply(null,
                        naming.makeSelect(make().Apply(null,
                                naming.makeSelect(
                                        make().TypeCast(make().Type(syms().ceylonMemberType), reference.makeIdent()),
                                        "getAttribute"),
                                List.<JCExpression>nil()),
                                "getQualifiedName"),
                        List.<JCExpression>nil()),
                cases.toList());
        
        if (model.isMember()
                && !model.getExtendedType().getDeclaration().isMember()) {
            stmts.add(make().If(make().TypeTest(reference.makeIdent(),
                    make().Type(syms().ceylonOuterType)),
                    
                    make().Return(expressionGen().makeOuterExpr(((TypeDeclaration)model.getContainer()).getType())),
                    swtch));
            
        } else {
            stmts.add(swtch);
        }
    
        mdb.body(stmts.toList());
        
        classBuilder.method(mdb);
    }
    
    private JCExpression makeSerializationGetter(Value value) {
            
        JCExpression result;
        if (value.isToplevel() || value.isLate()) {// XXX duplicates logic in AttributeDefinitionBuilder
            // We use the setter for late values, since that will allocate 
            // the array if needed.
            result = make().Apply(null,
                    naming.makeQualifiedName(naming.makeThis(), value, Naming.NA_MEMBER | Naming.NA_GETTER),
                    List.<JCExpression>nil());
        } else {
            // We bypass the setter
//            if (value.isVariable()) {
                result = naming.makeQualifiedName(naming.makeThis(), value, Naming.NA_IDENT);
            /*} else {
                // The field will have final modifier, so we need some 
                // jiggery pokery to reset it.
                requiredLookup[0] = true;
                String fieldName = value.getName();
                JCExpression fieldType = makeJavaType(value.getType());//TODO probably wrong
                result = makeReassignFinalField(fieldType, fieldName, newValue);
            }*/
        }

        BoxingStrategy boxingStrategy = useJavaBox(value, value.getType()) ? BoxingStrategy.JAVA : BoxingStrategy.BOXED;
        if (boxingStrategy == BoxingStrategy.JAVA && !isJavaString(value.getType())) {
            result = make().Conditional(
                    make().Binary(JCTree.Tag.EQ, result, makeNull()), 
                    makeNull(), 
                    // FIXME!!!
                    boxType(unboxJavaType(result, value.getType()),
                            simplifyType(value.getType())));
        }else{
            result = expressionGen().applyErasureAndBoxing(result, value.getType(), 
                    !CodegenUtil.isUnBoxed(value) , 
                    boxingStrategy, value.getType());
        }
        return result;
    }
    
    private void serializationSet(Class model,
            ClassDefinitionBuilder classBuilder) {
        MethodDefinitionBuilder mdb = systemMethod(this, Unfix.$set$.toString());
        mdb.isOverride(true);
        mdb.ignoreModelAnnotations();
        mdb.modifiers(PUBLIC);
        
        ParameterDefinitionBuilder pdb = systemParameter(this, Unfix.reference.toString());
        pdb.modifiers(FINAL);
        pdb.type(new TransformedType(make().Type(syms().ceylonReachableReferenceType), null, makeAtNonNull()));
        mdb.parameter(pdb);
        
        ParameterDefinitionBuilder pdb2 = systemParameter(this, Unfix.instance.toString());
        pdb2.modifiers(FINAL);
        pdb2.type(new TransformedType(make().Type(syms().objectType), null, makeAtNonNull()));
        mdb.parameter(pdb2);

        //mdb.resultType(null, naming.makeQuotedFQIdent("java.util.Collection"));
        /*
         * public void $set$(Object reference, Object instance) {
         *     switch((String)reference) {
         *     case ("attr1")
         *           this.field1 = ...;
         *           break;
         *     // ... other fields of this class
         *     default:
         *           super.set(reference, instance);
         */
        SyntheticName reference = naming.synthetic(Unfix.reference);
        SyntheticName instance = naming.synthetic(Unfix.instance);
        
        ListBuffer<JCCase> cases = new ListBuffer<JCCase>();
        boolean[] needsLookup = new boolean[]{false};
        for (Declaration member : model.getMembers()) {
            if (hasField(member)) {
                if (member instanceof Function)
                    continue; // TODO: This class is not serializable 
                ListBuffer<JCStatement> caseStmts = new ListBuffer<JCStatement>();
                if (member instanceof Value
                        && ((Value)member).isLate()) {
                    caseStmts.add(make().If(make().TypeTest(instance.makeIdent(),
                            make().Type(syms().ceylonUninitializedLateValueType)),
                            make().Break(null), null));
                }
                caseStmts.add(makeDeserializationAssignment((Value)member, needsLookup));
                caseStmts.add(make().Break(null));
                cases.add(make().Case(make().Literal(member.getQualifiedNameString()), caseStmts.toList()));
            }
        }
        
        ListBuffer<JCStatement> defaultCase = new ListBuffer<JCStatement>();
        if (extendsSerializable(model)) {
            // super.set(reference, instance);
            defaultCase.add(make().Exec(make().Apply(null,
                    naming.makeQualIdent(naming.makeSuper(), Unfix.$set$.toString()),
                    List.<JCExpression>of(reference.makeIdent(), instance.makeIdent()))));
        } else {
            // throw (or pass to something else to throw, based on policy)
            defaultCase.add(make().Throw(make().NewClass(null, null,
                    naming.makeQuotedFQIdent("java.lang.RuntimeException"),
                    List.<JCExpression>of(make().Literal("unknown attribute")),
                    null)));
        }
        cases.add(make().Case(null, defaultCase.toList()));
        
        ListBuffer<JCStatement> stmts = new ListBuffer<JCStatement>();
        if (needsLookup[0]) {
            // if we needed to use a lookup object to reset final fields, 
            // prepend that variable
            stmts.add(makeVar(FINAL, 
                "lookup", 
                naming.makeQualIdent(make().Type(syms().methodHandlesType), "Lookup"), 
                make().Apply(null, naming.makeQuotedFQIdent("java.lang.invoke.MethodHandles.lookup"), List.<JCExpression>nil())));
        }
        
        JCSwitch swtch = make().Switch(
                make().Apply(null, naming.makeSelect(make().Apply(null, naming.makeSelect(make().TypeCast(make().Type(syms().ceylonMemberType), 
                        reference.makeIdent()), "getAttribute"),
                        List.<JCExpression>nil()), "getQualifiedName"),
                        List.<JCExpression>nil()),
                        cases.toList());
        stmts.add(make().If(make().TypeTest(reference.makeIdent(),
                make().Type(syms().ceylonMemberType)),
                swtch,
                make().Throw(make().NewClass(null, null,
                        make().Type(syms().ceylonAssertionErrorType),
                        List.<JCExpression>of(make().Binary(JCTree.Tag.PLUS,
                                make().Literal("unexpected reachable reference "), reference.makeIdent())),
                        null))));
    
        mdb.body(stmts.toList());
        
        classBuilder.method(mdb);
    }
    

    private JCStatement makeDeserializationAssignment(Value value, boolean[] requiredLookup) {
        boolean isValueType = Decl.isValueTypeDecl(simplifyType(value.getType()));
            
        Naming.SyntheticName n = naming.synthetic(Unfix.instance);
        JCExpression newValue = make().TypeCast(makeJavaType(value.getType(), JT_NO_PRIMITIVES), n.makeIdent());
        if (isValueType) {
            // FIXME: check strings
            BoxingStrategy boxingStrategy = useJavaBox(value, value.getType())
                    ? BoxingStrategy.JAVA 
                    : CodegenUtil.getBoxingStrategy(value);
            Type simpleType = simplifyType(value.getType());
            newValue = expressionGen().applyErasureAndBoxing(newValue, 
                    simpleType, true, boxingStrategy, 
                    simpleType);
            if(boxingStrategy == BoxingStrategy.JAVA
                    && isOptional(value.getType())
                    && !isJavaString(simpleType)) {
                    newValue = make().Conditional(
                            make().Binary(JCTree.Tag.EQ, 
                                    n.makeIdent(), 
                                    makeNull()),
                            makeNull(),
                            newValue);
            }
        } else {
            // We need to obtain the instance from the reference
            // but we don't need the instance to be fully deserialized
        }
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
                requiredLookup[0] = true;
                String fieldName = value.getName();
                JCExpression fieldType = makeJavaType(value.getType());//TODO probably wrong
                assignment = makeReassignFinalField(fieldType, fieldName, newValue);
            }
        }
        return assignment;
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
    
//    private JCExpression makeValueDeclaration(Value value) {
//        return expressionGen().makeMemberValueOrFunctionDeclarationLiteral(null, value, false);
//    }
    
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
            Function method = (Function)paramModel.getModel();

            java.util.List<Parameter> parameters = method.getFirstParameterList().getParameters();
            JCExpression fieldRef;
            if (method.getMemberOrParameter(typeFact(), method.getName(), null, false)!=method) {
                fieldRef = naming.makeQualifiedName(naming.makeThis(), method, Naming.NA_IDENT);
            }
            else {
                fieldRef = naming.makeName(method, Naming.NA_IDENT);
            }
            CallBuilder callBuilder = CallBuilder.instance(this).invoke(
                    naming.makeQualIdent(fieldRef, Naming.getCallableMethodName(method)));
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
        if(model.getExtendedType() == null || willEraseToObject(model.getExtendedType()) || !ModelUtil.isCeylonDeclaration(model.getExtendedType().getDeclaration()))
            classBuilder.reifiedType();
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
        java.util.List<Type> satisfiedTypes = model.getSatisfiedTypes();
        if(satisfiedTypes.size() <= 1)
            return;
        Set<Interface> satisfiedInterfaces = new HashSet<Interface>();
        for(Type interfaceDecl : model.getSatisfiedTypes()){
            collectInterfaces((Interface) interfaceDecl.getDeclaration(), satisfiedInterfaces);
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
                    Type firstInterface = satisfiedTypes.get(i);
                    Declaration member1 = firstInterface.getDeclaration().getMember(name, null, false);
                    // if we can't find it in this interface, move to the next
                    if(member1 == null)
                        continue;
                    // try to find member in other interfaces
                    for(int j=i+1;j<satisfiedTypes.size();j++){
                        Type secondInterface = satisfiedTypes.get(j);
                        Declaration member2 = secondInterface.getDeclaration().getMember(name, null, false);
                        // if we can't find it in this interface, move to the next
                        if(member2 == null)
                            continue;
                        // we have it in two separate interfaces
                        Reference typedMember1 = firstInterface.getTypedReference(member1, Collections.<Type>emptyList());
                        Reference typedMember2 = secondInterface.getTypedReference(member2, Collections.<Type>emptyList());
                        Type type1 = simplifyType(typedMember1.getType());
                        Type type2 = simplifyType(typedMember2.getType());
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
        Type satisfiedType = model.getType().getSupertype(model);
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
        }else if (member instanceof Function) {
            Function method = (Function)member;
            final TypedReference typedMember = satisfiedType.getTypedMember(method, Collections.<Type>emptyList());
            java.util.List<java.util.List<Type>> producedTypeParameterBounds = producedTypeParameterBounds(
                    typedMember, method);
            final java.util.List<TypeParameter> typeParameters = method.getTypeParameters();
            final java.util.List<Parameter> parameters = method.getFirstParameterList().getParameters();

            for (Parameter param : parameters) {

                if (Strategy.hasDefaultParameterOverload(param)) {
                    MethodDefinitionBuilder overload = new DefaultedArgumentMethodTyped(null, method(this, method), typedMember, true)
                    .makeOverload(
                            method.getFirstParameterList(),
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
                    method.getFirstParameterList().getParameters(),
                    ((Function) member).getTypeErased(),
                    null,
                    null,
                    false);
            classBuilder.method(concreteMemberDelegate);
        } else if (member instanceof Value
                || member instanceof Setter) {
            TypedDeclaration attr = (TypedDeclaration)member;
            final TypedReference typedMember = satisfiedType.getTypedMember(attr, Collections.<Type>emptyList());
            if (member instanceof Value) {
                final MethodDefinitionBuilder getterDelegate = makeDelegateToCompanion(null, 
                        typedMember,
                        model.getType(),
                        PUBLIC | ABSTRACT, 
                        Collections.<TypeParameter>emptyList(), 
                        Collections.<java.util.List<Type>>emptyList(),
                        typedMember.getType(), 
                        Naming.getGetterName(attr), 
                        Collections.<Parameter>emptyList(),
                        attr.getTypeErased(),
                        null,
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
                        Collections.<java.util.List<Type>>emptyList(),
                        typeFact().getAnythingType(), 
                        Naming.getSetterName(attr), 
                        Collections.<Parameter>singletonList(((Setter)member).getParameter()),
                        ((Setter) member).getTypeErased(),
                        null,
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
            if(innerType.isAlias()
                    && innerTypeTree != null
                    && Decl.isAncestorLocal(innerTypeTree.getDeclarationModel()))
                // for the same reason we do not generate aliases in transform(ClassOrInterface def) let's not list them
                continue;
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
        Scope scope = Decl.getNonSkippedContainer((Scope)model);
        Scope declarationScope = Decl.getFirstDeclarationContainer((Scope)model);
        boolean inlineObjectInToplevelAttr = Decl.isTopLevelObjectExpressionType(model);
        if(scope == null || (scope instanceof Package && !inlineObjectInToplevelAttr) && scope == declarationScope)
            return;
        if(scope instanceof ClassOrInterface 
                && scope == declarationScope
                && !inlineObjectInToplevelAttr
                // we do not check for types inside initialiser section which are private and not captured because we treat them as members
                && !(model instanceof Interface && Decl.hasLocalNotInitializerAncestor(model))){
            ClassOrInterface container = (ClassOrInterface) scope;
            List<JCAnnotation> atContainer = makeAtContainer(container.getType(), model.isStatic());
            classBuilder.annotations(atContainer);
        }else{
            if(model instanceof Interface)
                classBuilder.annotations(makeLocalContainerPath((Interface) model));
            Declaration declarationContainer = getDeclarationContainer(model);
            classBuilder.annotations(makeAtLocalDeclaration(model.getQualifier(), declarationContainer == null));
        }
        
    }

    private void satisfaction(Tree.SatisfiedTypes satisfied, final Class model, ClassDefinitionBuilder classBuilder) {
        Set<Interface> satisfiedInterfaces = new HashSet<Interface>();
        // start by saying that we already satisfied each interface from superclasses
        Type superClass = model.getExtendedType();
        while(superClass != null){
            for(Type interfaceDecl : superClass.getSatisfiedTypes()){
                collectInterfaces((Interface) interfaceDecl.getDeclaration(), satisfiedInterfaces);
            }
            superClass = superClass.getExtendedType();
        }
        // now satisfy each new interface
        if (satisfied != null) {
            for (Tree.StaticType type : satisfied.getTypes()) {
                try {
                    Type satisfiedType = type.getTypeModel();
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
        if(model.getExtendedType() != null){
            // reuse that Set
            satisfiedInterfaces.clear();
            for(Type interfaceDecl : model.getSatisfiedTypes()){
                collectInterfaces((Interface) interfaceDecl.getDeclaration(), satisfiedInterfaces);
            }
            if(!satisfiedInterfaces.isEmpty()){
                // sort it to facilitate test comparisons that work in JDK7 and 8
                ArrayList<Interface> sortedInterfaces = new ArrayList<Interface>(satisfiedInterfaces.size());
                sortedInterfaces.addAll(satisfiedInterfaces);
                Collections.sort(sortedInterfaces, DeclarationComparator);
                // now see if we refined them
                for(Interface iface : sortedInterfaces){
                    // skip those we can't do anything about
                    if(!supportsReified(iface) || !CodegenUtil.isCompanionClassNeeded(iface))
                        continue;
                    Type thisType = model.getType().getSupertype(iface);
                    Type superClassType = model.getExtendedType().getSupertype(iface);
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
    }

    private void collectInterfaces(Interface interfaceDecl, Set<Interface> satisfiedInterfaces) {
        if(satisfiedInterfaces.add(interfaceDecl)){
            for(Type newInterfaceDecl : interfaceDecl.getSatisfiedTypes()){
                if (!(newInterfaceDecl.isUnknown())) {
                    collectInterfaces((Interface) newInterfaceDecl.getDeclaration(), satisfiedInterfaces);
                }
            }
        }
    }
    
    /**
     * Generates companion fields ($Foo$impl) and methods
     */
    private void concreteMembersFromSuperinterfaces(final Class model,
            ClassDefinitionBuilder classBuilder, 
            Type satisfiedType, Set<Interface> satisfiedInterfaces) {
        satisfiedType = satisfiedType.resolveAliases();
        Interface iface = (Interface)satisfiedType.getDeclaration();
        if (satisfiedInterfaces.contains(iface) || iface.isIdentifiable()) {
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
        
        if(!ModelUtil.isCeylonDeclaration(iface)){
            // let's not try to implement CMI for Java interfaces
            return;
        }
        
        // For each super interface
        for (Declaration member : sortedMembers(iface.getMembers())) {
            if (member.isStatic()) continue;
            
            if (member instanceof Class) {
                Class klass = (Class)member;
                final Type typedMember = satisfiedType.getTypeMember(klass, Collections.<Type>emptyList());
                if (Strategy.generateInstantiator(member)
                        && !klass.hasConstructors()
                    && !model.isFormal()
                    && needsCompanionDelegate(model, typedMember)
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
            if (member instanceof Function) {
                Function method = (Function)member;
                final TypedReference typedMember = satisfiedType.getTypedMember(method, typesOfTypeParameters(method.getTypeParameters()));
                Declaration sub = (Declaration)model.getMember(method.getName(), getSignatureIfRequired(typedMember), false, true);
                if (sub instanceof Function/* && !sub.isAbstraction()*/) {
                    Function subMethod = (Function)sub;
                    if (subMethod.getParameterLists().isEmpty()) {
                        continue;
                    }
                    java.util.List<java.util.List<Type>> producedTypeParameterBounds = producedTypeParameterBounds(
                            typedMember, subMethod);
//                    final TypedReference refinedTypedMember = model.getType().getTypedMember(subMethod, Collections.<Type>emptyList());
                    final java.util.List<TypeParameter> typeParameters = subMethod.getTypeParameters();
                    final java.util.List<Parameter> parameters = subMethod.getFirstParameterList().getParameters();
                    boolean hasOverloads = false;
                    if (!satisfiedInterfaces.contains((Interface)method.getContainer())) {

                        for (Parameter param : parameters) {
                            if (Strategy.hasDefaultParameterValueMethod(param)
                                    && CodegenUtil.getTopmostRefinedDeclaration(param.getModel()).getContainer().equals(member)) {
                                final TypedReference typedParameter = typedMember.getTypedParameter(param);
                                // If that method has a defaulted parameter, 
                                // we need to generate a default value method
                                // which also delegates to the $impl
                                final MethodDefinitionBuilder defaultValueDelegate = makeDelegateToCompanion(iface,
                                        typedMember,
                                        model.getType(),
                                        modifierTransformation().defaultValueMethodBridge(), 
                                        typeParameters, producedTypeParameterBounds,
                                        typedParameter.getFullType(), 
                                        Naming.getDefaultedParamMethodName(method, param), 
                                        parameters.subList(0, parameters.indexOf(param)),
                                        param.getModel().getTypeErased(),
                                        null,
                                        param);
                                classBuilder.method(defaultValueDelegate);
                            }

                            if (Strategy.hasDefaultParameterOverload(param)) {
                                if ((method.isDefault() || method.isShared() && !method.isFormal())
                                        && Decl.equal(method, subMethod)) {
                                    MethodDefinitionBuilder overload = new DefaultedArgumentMethodTyped(new DaoThis((Tree.AnyMethod)null, null), method(this, subMethod), typedMember, true)
                                        .makeOverload(
                                            subMethod.getFirstParameterList(),
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
                    if (needsCompanionDelegate(model, typedMember)) {

                        final MethodDefinitionBuilder concreteMemberDelegate = makeDelegateToCompanion(iface,
                                typedMember,
                                model.getType(),
                                modifierTransformation().methodBridge(method),
                                typeParameters, 
                                producedTypeParameterBounds, 
                                typedMember.getType(), 
                                naming.selector(method), 
                                method.getFirstParameterList().getParameters(),
                                ((Function) member).getTypeErased(),
                                null,
                                null);
                        classBuilder.method(concreteMemberDelegate);
                    }

                    if (hasOverloads
                            && (method.isDefault() || method.isShared() && !method.isFormal())
                            && Decl.equal(method, subMethod)) {
                        final MethodDefinitionBuilder canonicalMethod = makeDelegateToCompanion(iface,
                                typedMember,
                                model.getType(),
                                modifierTransformation().canonicalMethodBridge(),
                                subMethod.getTypeParameters(), 
                                producedTypeParameterBounds,
                                typedMember.getType(), 
                                Naming.selector(method, Naming.NA_CANONICAL_METHOD), 
                                method.getFirstParameterList().getParameters(),
                                ((Function) member).getTypeErased(),
                                naming.selector(method),
                                null);
                        classBuilder.method(canonicalMethod);
                    }
                }
            } else if (member instanceof Value
                    || member instanceof Setter) {
                TypedDeclaration attr = (TypedDeclaration)member;
                final TypedReference typedMember = satisfiedType.getTypedMember(attr, null);
                if (needsCompanionDelegate(model, typedMember)) {
                    Setter setter = (member instanceof Setter) ? (Setter)member : null;
                    if (member instanceof Value) {
                        Value getter = (Value)member;
                        if (member instanceof JavaBeanValue) {
                            setter = ((Value) member).getSetter();
                        }
                        final MethodDefinitionBuilder getterDelegate = makeDelegateToCompanion(iface, 
                                typedMember,
                                model.getType(),
                                modifierTransformation().getterBridge(getter), 
                                Collections.<TypeParameter>emptyList(), 
                                Collections.<java.util.List<Type>>emptyList(),
                                typedMember.getType(), 
                                Naming.getGetterName(getter), 
                                Collections.<Parameter>emptyList(),
                                getter.getTypeErased(),
                                null,
                                null);
                        classBuilder.method(getterDelegate);
                    }
                    if (setter != null) {
                        final MethodDefinitionBuilder setterDelegate = makeDelegateToCompanion(iface, 
                                satisfiedType.getTypedMember(setter, null),
                                model.getType(),
                                modifierTransformation().setterBridge(setter), 
                                Collections.<TypeParameter>emptyList(), 
                                Collections.<java.util.List<Type>>emptyList(),
                                typeFact().getAnythingType(), 
                                Naming.getSetterName(attr), 
                                Collections.<Parameter>singletonList(setter.getParameter()),
                                setter.getTypeErased(),
                                null,
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
            } else {
                Reference typedMember = member instanceof TypeDeclaration 
                        ? satisfiedType.getTypeMember((TypeDeclaration)member, Collections.<Type>emptyList())
                        : satisfiedType.getTypedMember((TypedDeclaration)member, Collections.<Type>emptyList());

                if (needsCompanionDelegate(model, typedMember)) {
                    throw new BugException("unhandled concrete interface member " + member.getQualifiedNameString() + " " + member.getClass());
                }
            }
        }
        
        // Add $impl instances for the whole interface hierarchy
        satisfiedInterfaces.add(iface);
        for (Type sat : iface.getSatisfiedTypes()) {
            sat = model.getType().getSupertype(sat.getDeclaration());
            concreteMembersFromSuperinterfaces(model, classBuilder, sat, satisfiedInterfaces);
        }
        
    }
    
    private java.util.List<Type> typesOfTypeParameters(java.util.List<TypeParameter> list) {
        ArrayList<Type> result = new ArrayList<Type>(list.size());
        for (TypeParameter tp : list) {
            result.add(tp.getType());
        }
        return result;
    }
    
    private Iterable<Declaration> sortedMembers(java.util.List<Declaration> members) {
        TreeSet<Declaration> set = new TreeSet<Declaration>(DeclarationComparator);
        set.addAll(members);
        return set;
    }

    private java.util.List<java.util.List<Type>> producedTypeParameterBounds(
            final Reference typedMember, Generic subMethod) {
        java.util.List<java.util.List<Type>> producedTypeParameterBounds = new ArrayList<java.util.List<Type>>(subMethod.getTypeParameters().size());
        for (TypeParameter tp : subMethod.getTypeParameters()) {
            java.util.List<Type> satisfiedTypes = tp.getType().getSatisfiedTypes();
            ArrayList<Type> bounds = new ArrayList<>(satisfiedTypes.size());
            for (Type bound : satisfiedTypes) {
                if (typedMember instanceof Type) {
                    bounds.add(bound.substitute((Type) typedMember));
                }
                else if (typedMember instanceof TypedReference) {
                    bounds.add(bound.substitute((TypedReference) typedMember));
                }
            }
            producedTypeParameterBounds.add(bounds);
        }
        return producedTypeParameterBounds;
    }

    private void generateInstantiatorDelegate(
            ClassDefinitionBuilder classBuilder, Type satisfiedType,
            Interface iface, Class klass, Constructor ctor, Type currentType, boolean includeBody) {
        Type typeMember = satisfiedType.getTypeMember(klass, klass.getType().getTypeArgumentList());
        if (ctor != null) {
            typeMember = ctor.appliedType(typeMember, Collections.<Type>emptyList());
        }
        java.util.List<TypeParameter> typeParameters = klass.getTypeParameters();
        java.util.List<Parameter> parameters = (ctor != null ? ctor.getParameterLists() : klass.getParameterLists()).get(0).getParameters();
        long flags = modifierTransformation().instantiatorBridgeFlags(includeBody);
        
        String instantiatorMethodName = naming.getInstantiatorMethodName(klass);
        for (Parameter param : parameters) {
            if (Strategy.hasDefaultParameterValueMethod(param)
                    && !klass.isActual()) {
                final TypedReference typedParameter = typeMember.getTypedParameter(param);
                // If that method has a defaulted parameter, 
                // we need to generate a default value method
                // which also delegates to the $impl
                final MethodDefinitionBuilder defaultValueDelegate = makeDelegateToCompanion(iface,
                        typeMember,
                        currentType,
                        flags, 
                        typeParameters, 
                        producedTypeParameterBounds(typeMember, klass),
                        typedParameter.getFullType(),
                        Naming.getDefaultedParamMethodName(ctor != null ? ctor : klass, param), 
                        parameters.subList(0, parameters.indexOf(param)),
                        param.getModel().getTypeErased(),
                        null,
                        param,
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
                null,
                includeBody);
        classBuilder.method(overload);
    }

    private boolean needsCompanionDelegate(final Class model, Reference ref) {
        final boolean mostRefined;
        Declaration member = ref.getDeclaration();
        java.util.List<Type> sig = getSignatureIfRequired(ref);
        Declaration m = model.getMember(member.getName(), sig, false, true);
        if (member instanceof Setter && Decl.isGetter(m)) {
            mostRefined = member.equals(((Value)m).getSetter());
        } else {
            mostRefined = member.equals(m);
        }
        return mostRefined
                && (member.isDefault() || !member.isFormal());
    }

    private java.util.List<Type> getSignatureIfRequired(Reference ref) {
        if(requiresMemberSignatureMatch(ref.getDeclaration()))
            return ModelUtil.getSignature(ref);
        return null;
    }

    private boolean requiresMemberSignatureMatch(Declaration declaration) {
        if(declaration instanceof Function){
            java.util.List<ParameterList> parameterLists = ((Functional)declaration).getParameterLists();
            if(parameterLists != null && parameterLists.size() == 1){
                ParameterList parameterList = parameterLists.get(0);
                for(Parameter param : parameterList.getParameters()){
                    if(param.getModel().isFunctional())
                        return false;
                }
            }
            return declaration.isAbstraction() || declaration.isOverloaded();
        }
        return false;
    }

    /**
     * Generates a method which delegates to the companion instance $Foo$impl
     */
    private MethodDefinitionBuilder makeDelegateToCompanion(Interface iface,
            Reference typedMember,
            Type currentType,
            final long mods,
            final java.util.List<TypeParameter> typeParameters,
            final java.util.List<java.util.List<Type>> producedTypeParameterBounds,
            final Type methodType,
            final String methodName,
            final java.util.List<Parameter> parameters, 
            boolean typeErased,
            final String targetMethodName, 
            Parameter param) {
        return makeDelegateToCompanion(iface, typedMember, currentType, mods, typeParameters,
                producedTypeParameterBounds, methodType, methodName, parameters, typeErased, targetMethodName, param, true);
    }
    
    /**
     * Generates a method which delegates to the companion instance $Foo$impl
     */
    private MethodDefinitionBuilder makeDelegateToCompanion(Interface iface,
            Reference typedMember,
            Type currentType,
            final long mods,
            final java.util.List<TypeParameter> typeParameters,
            final java.util.List<java.util.List<Type>> producedTypeParameterBounds,
            final Type methodType,
            final String methodName,
            final java.util.List<Parameter> parameters, 
            boolean typeErased,
            final String targetMethodName,
            Parameter defaultedParam, 
            boolean includeBody) {
        final MethodDefinitionBuilder concreteWrapper = systemMethod(gen(), methodName);
        concreteWrapper.modifiers(mods);
        concreteWrapper.ignoreModelAnnotations();
        if ((mods & PRIVATE) == 0) {
            concreteWrapper.isOverride(true);
        }
        if(typeParameters != null) {
            concreteWrapper.reifiedTypeParametersFromModel(typeParameters);
        }
        Iterator<java.util.List<Type>> iterator = producedTypeParameterBounds.iterator();
        if(typeParameters != null) {
            for (TypeParameter tp : typeParameters) {
                concreteWrapper.typeParameter(tp, iterator.next());
            }
        }
        
        boolean explicitReturn = false;
        Declaration member = (defaultedParam != null ? typedMember.getTypedParameter(defaultedParam) : typedMember).getDeclaration();
        Type returnType = null;
        if (!isAnything(methodType) 
                || ((member instanceof Function || member instanceof Value) && !Decl.isUnboxedVoid(member)) 
                || (member instanceof Function && Strategy.useBoxedVoid((Function)member))) {
            explicitReturn = true;
            if(CodegenUtil.isHashAttribute(member)){
                // delegates for hash attributes are int
                concreteWrapper.resultType(new TransformedType(make().Type(syms().intType)));
                returnType = typedMember.getType();
            }else if (typedMember instanceof TypedReference
                    && defaultedParam == null) {
                TypedReference typedRef = (TypedReference) typedMember;
                
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
                TypedReference nonWideningTypedRef = gen().nonWideningTypeDecl(typedRef, currentType);
                returnType = gen().nonWideningType(typedRef, nonWideningTypedRef);
            } else if (defaultedParam != null) {
                TypedReference typedParameter = typedMember.getTypedParameter(defaultedParam);
                NonWideningParam nonWideningParam = concreteWrapper.getNonWideningParam(typedParameter, 
                        currentType.getDeclaration() instanceof Class ? WideningRules.FOR_MIXIN : WideningRules.NONE);
                returnType = nonWideningParam.nonWideningType;
                if(member instanceof Function)
                    returnType = typeFact().getCallableType(returnType);
                concreteWrapper.resultType(new TransformedType(makeJavaType(returnType, nonWideningParam.flags)));
            } else {
                concreteWrapper.resultType(new TransformedType(makeJavaType((Type)typedMember)));
                returnType = (Type) typedMember;
            }
        }
        
        ListBuffer<JCExpression> arguments = new ListBuffer<JCExpression>();
        if(typeParameters != null){
            for(TypeParameter tp : typeParameters){
                arguments.add(naming.makeUnquotedIdent(naming.getTypeArgumentDescriptorName(tp)));
            }
        }
        Declaration declaration = typedMember.getDeclaration();
        if (declaration instanceof Constructor
                && !Decl.isDefaultConstructor((Constructor)declaration)
                && defaultedParam == null) {
            concreteWrapper.parameter(makeConstructorNameParameter((Constructor)declaration));
            arguments.add(naming.makeUnquotedIdent(Unfix.$name$));
        }
        int ii = 0;
        for (Parameter param : parameters) {
            Parameter parameter;
            if (declaration instanceof Functional) {
                parameter = ((Functional)declaration).getFirstParameterList().getParameters().get(ii++);
            } else if (declaration instanceof Setter){
                parameter = ((Setter)declaration).getParameter();
            } else {
                throw BugException.unhandledDeclarationCase(declaration);
            }
            
            final TypedReference typedParameter = typedMember.getTypedParameter(parameter);
            concreteWrapper.parameter(null, param, typedParameter, null, FINAL, WideningRules.FOR_MIXIN);
            arguments.add(naming.makeName(param.getModel(), Naming.NA_MEMBER | Naming.NA_ALIASED));
        }
        if(includeBody){
            JCExpression qualifierThis = makeUnquotedIdent(getCompanionFieldName(iface));
            // if the best satisfied type is not the one we think we implement, we may need to cast
            // our impl accessor to get the expected bounds of the qualifying type
            if(explicitReturn){
                Type javaType = getBestSatisfiedType(currentType, iface);
                Type ceylonType = typedMember.getQualifyingType();
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
                        "formal member '"+declaration.getName()+"' of '"+iface.getName()+"' not implemented in class hierarchy"));
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
                        || needsRawCastForMixinSuperCall(iface, methodType)
                        || needsCastForErasedInstantiator(iface, methodName, member))
                    typeErased = true;
                expr = gen().expressionGen().applyErasureAndBoxing(expr, methodType, typeErased, 
                        exprBoxed, boxingStrategy,
                        returnType, 0);
                concreteWrapper.body(gen().make().Return(expr));
            }
        }
        return concreteWrapper;
    }

    protected boolean needsCastForErasedInstantiator(Interface iface,
            final String methodName, Declaration member) {
        return Decl.isAncestorLocal(iface) && Decl.isAncestorLocal(member)
                && methodName.endsWith(NamingBase.Suffix.$new$.toString());
    }

    private boolean isUnimplementedMemberClass(Type currentType, Reference typedMember) {
        if (typedMember instanceof Type
                && currentType.getDeclaration() instanceof Class) {// member class
            for (Reference formal : ((Class)currentType.getDeclaration()).getUnimplementedFormals()) {
                if (formal.getDeclaration().equals(typedMember.getDeclaration())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isInheritedTwiceWithDifferentTypeArguments(Type currentType, Interface iface) {
        Type firstSatisfiedType = getFirstSatisfiedType(currentType, iface);
        Type supertype = currentType.getSupertype(iface);
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
            Type satisfiedType) {
        at(null);
        
        // make sure we get the first type that java will find when it looks up
        final Type bestSatisfiedType = getBestSatisfiedType(model.getType(), iface);
        
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
     * this.$ceylon$language$Enumerable$this$ = new .ceylon.language.Enumerable$impl<.org.eclipse.ceylon.compiler.java.test.structure.klass.SerializableEnumerable>(.org.eclipse.ceylon.compiler.java.test.structure.klass.SerializableEnumerable.$TypeDescriptor$, this);
     * </pre>
     * @param classBuilder 
     * @return 
     */
    private JCExpressionStatement makeCompanionInstanceAssignment(final Class model,
            final Interface iface, final Type satisfiedType) {
        
        final Type bestSatisfiedType = getBestSatisfiedType(model.getType(), iface);
        
        JCExpression containerInstance = null;
        if(!iface.isToplevel() && !Decl.isLocal(iface)){
            // if it's a member type we need to qualify the new instance with its $impl container
            ClassOrInterface interfaceContainer = ModelUtil.getClassOrInterfaceContainer(iface, false);
            if(interfaceContainer instanceof Interface){
                ClassOrInterface modelContainer = model;
                // first try to find exactly the interface we are looking for
                while((modelContainer = ModelUtil.getClassOrInterfaceContainer(modelContainer, false)) != null
                        && !modelContainer.equals(interfaceContainer)){
                    // keep searching
                }
                // then find one that inherits it
                if(modelContainer == null){
                    modelContainer = model;
                    while((modelContainer = ModelUtil.getClassOrInterfaceContainer(modelContainer, false)) != null
                            && modelContainer.getType().getSupertype(interfaceContainer) == null){
                        // keep searching
                    }
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
        if(!iface.isToplevel() && !Decl.isLocal(iface)
                && ModelUtil.getClassOrInterfaceContainer(iface, false) instanceof Interface){
            ifaceImplType = makeJavaType(bestSatisfiedType, JT_COMPANION | JT_CLASS_NEW | JT_NON_QUALIFIED);
        } else {
            ifaceImplType = makeJavaType(bestSatisfiedType, JT_COMPANION | JT_CLASS_NEW);
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
    
    private MethodDefinitionBuilder makeCompanionAccessor(Interface iface, Type satisfiedType, 
            Class currentType, boolean forImplementor) {
        MethodDefinitionBuilder thisMethod = systemMethod(
                this, naming.getCompanionAccessorName(iface));
        thisMethod.noModelAnnotations();
        JCExpression typeExpr;
        if (!forImplementor && Decl.isAncestorLocal(iface)) {
            // For a local interface the return type cannot be a local
            // companion class, because that won't be visible at the 
            // top level, so use Object instead
            typeExpr = make().Type(syms().objectType);
        } else {
            typeExpr = makeJavaType(satisfiedType, JT_COMPANION);
        }
        thisMethod.resultType(new TransformedType(typeExpr, null, makeAtNonNull()));
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

    private Type getBestSatisfiedType(Type currentType, Interface iface) {
        Type refinedSuperType = currentType.getSupertype(iface);
        Type firstSatisfiedType = getFirstSatisfiedType(currentType, iface);
        // in the very special case of the first satisfied type having type arguments erased to Object and
        // the most refined one having free type parameters, we prefer the one with free type parameters
        // because Java prefers it and it's in range with what nonWideningType does
        Map<TypeParameter, Type> refinedTAs = refinedSuperType.getTypeArguments();
        Map<TypeParameter, Type> firstTAs = firstSatisfiedType.getTypeArguments();
        for(TypeParameter tp : iface.getTypeParameters()){
            Type refinedTA = refinedTAs.get(tp);
            Type firstTA = firstTAs.get(tp);
            if(willEraseToObject(firstTA) && isTypeParameter(refinedTA))
                return refinedSuperType;
        }
        return firstSatisfiedType;
    }

    private Type getFirstSatisfiedType(Type currentType, Interface iface) {
        Type found = null;
        TypeDeclaration currentDecl = currentType.getDeclaration();
        if (Decl.equal(currentDecl, iface)) {
            return currentType;
        }
        if(currentType.getExtendedType() != null){
            Type supertype = currentType.getSupertype(currentType.getExtendedType().getDeclaration());
            found = getFirstSatisfiedType(supertype, iface);
            if(found != null)
                return found;
        }
        for(Type superInterfaceType : currentType.getSatisfiedTypes()){
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
            } else if (decl instanceof Function) {
                // Now build a "fake" declaration for the method
                Tree.MethodDeclaration methDecl = new Tree.MethodDeclaration(null);
                Function m = (Function)decl;
                methDecl.setDeclarationModel(m);
                methDecl.setIdentifier(expr.getIdentifier());
                methDecl.setScope(op.getScope());
                methDecl.setAnnotationList(makeShortcutRefinementAnnotationTrees());
                
                Tree.SpecifierExpression specifierExpression = op.getSpecifierExpression();
                methDecl.setSpecifierExpression(specifierExpression);
                
                if(specifierExpression instanceof Tree.LazySpecifierExpression == false){
                    Tree.Expression expression = specifierExpression.getExpression();
                    Tree.Term expressionTerm = TreeUtil.unwrapExpressionUntilTerm(expression);
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
                    parameterListTrees = new ArrayList<>(m.getParameterLists().size());
                    parameterListTrees.addAll(((Tree.ParameterizedExpression)op.getBaseMemberExpression()).getParameterLists());
                    Tree.Term term = specifierExpression.getExpression().getTerm();
                    // mpl refined by single pl with anonymous functions
                    // we bring each anonymous function pl up to the mpl method
                    // and give it the given block of expr as it's specifier
                    while (term instanceof Tree.FunctionArgument
                            && m.getParameterLists().size() > 1) {
                        FunctionArgument functionArgument = (Tree.FunctionArgument)term;
                        specifierExpression.setExpression(functionArgument.getExpression());
                        parameterListTrees.addAll(functionArgument.getParameterLists());
                        term = functionArgument.getExpression().getTerm();
                    }
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
                        } else if (p.getModel() instanceof Function) {
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

    public void transform(Tree.AttributeDeclaration decl, ClassDefinitionBuilder classBuilder) {
        final Value model = decl.getDeclarationModel();
        boolean withinInterface = model.isInterfaceMember();
        Tree.SpecifierOrInitializerExpression initializer = decl.getSpecifierOrInitializerExpression();
        final boolean lazy = initializer instanceof Tree.LazySpecifierExpression;
        String attrName = decl.getIdentifier().getText();
        boolean memoized = Decl.isMemoized(decl);
        boolean isStatic = model.isStatic();
        // Only a non-formal or a concrete-non-lazy attribute has a corresponding field
        // and if a captured class parameter exists with the same name we skip this part as well
        Parameter parameter = CodegenUtil.findParamForDecl(decl);
        boolean useField = !lazy && Strategy.useField(model);
        boolean createField = !lazy 
                && !model.isFormal() 
                && Strategy.createField(parameter, model) 
                && !model.isJavaNative();
        boolean createCompanionField = !lazy 
                && withinInterface 
                && initializer != null;
        JCThrow err = null;
        JCExpression memoizedInitialValue = null;
        if (createCompanionField || createField) {
            TypedReference typedRef = getTypedReference(model);
            TypedReference nonWideningTypedRef = nonWideningTypeDecl(typedRef);
            Type nonWideningType = nonWideningType(typedRef, nonWideningTypedRef);
            
            if (Decl.isIndirect(decl)) {
                attrName = Naming.getAttrClassName(model, 0);
                nonWideningType = getGetterInterfaceType(model);
            }
            
            JCExpression initialValue = null;
            BoxingStrategy boxingStrategy = null;
            if (initializer != null) {
                Tree.Expression expression = initializer.getExpression();
                HasErrorException error = errors().getFirstExpressionErrorAndMarkBrokenness(expression.getTerm());
                int flags = CodegenUtil.downcastForSmall(expression, model) ? ExpressionTransformer.EXPR_UNSAFE_PRIMITIVE_TYPECAST_OK : 0;
                flags |= model.hasUncheckedNullType() ? ExpressionTransformer.EXPR_TARGET_ACCEPTS_NULL : 0;
                if (error != null) {
                    initialValue = null;
                    err = makeThrowUnresolvedCompilationError(error.getErrorMessage().getMessage());
                } else {
                    boxingStrategy = useJavaBox(model, nonWideningType) 
                            && javaBoxExpression(expression.getTypeModel(), nonWideningType) 
                                    ? BoxingStrategy.JAVA 
                                    : CodegenUtil.getBoxingStrategy(model);
                    initialValue = expressionGen().transformExpression(expression, 
                            boxingStrategy, 
                            isStatic && nonWideningType.isTypeParameter() 
                                ? typeFact().getAnythingType() 
                                : nonWideningType, 
                            flags);
                }
            }
            if (memoized) {
                memoizedInitialValue = initialValue;
                initialValue = makeDefaultExprForType(nonWideningType);
            }
            int flags = 0;
            
            if (!CodegenUtil.isUnBoxed(nonWideningTypedRef.getDeclaration())) {
                flags |= JT_NO_PRIMITIVES;
            }
            
            long modifiers = useField ? modifierTransformation().field(decl) : modifierTransformation().localVar(decl);
            
            // If the attribute is really from a parameter then don't generate a field
            // (makeAttributeForValueParameter() or makeMethodForFunctionalParameter() 
            //  does it in those cases)
            if (parameter == null
                    || parameter.isHidden()) {
                JCExpression type;
                if (isStatic && nonWideningType.isTypeParameter()) {
                    type = make().Type(syms().objectType);
                } else {
                    type = makeJavaType(nonWideningType, flags);
                }
                
                if (createCompanionField) {
                    classBuilder.getCompanionBuilder((TypeDeclaration)model.getContainer()).field(modifiers, attrName, type, initialValue, !useField);
                } else {
                    
                    List<JCAnnotation> annos = makeAtIgnore().prependList(expressionGen().transformAnnotations(OutputElement.FIELD, decl));
                    if (classBuilder.hasDelegatingConstructors()) {
                        annos = annos.prependList(makeAtNoInitCheck());
                    }
                    // fields should be ignored, they are accessed by the getters
                    if (err == null) {
                        // TODO This should really be using AttributeDefinitionBuilder somehow
                        if (useField) {
                            AttributeDefinitionBuilder adb = field(this, null, attrName, model, Decl.isIndirect(decl)).
                                    fieldAnnotations(annos).
                                    fieldNullability(makeNullabilityAnnotations(model)).
                                    initialValue(initialValue, boxingStrategy).
                                    fieldVisibilityModifiers(modifiers).
                                    modifiers(modifiers);
                            classBuilder.defs(adb.buildFields());
                            List<JCStatement> buildInit = adb.buildInit(false);
                            if (!buildInit.isEmpty()) {
                                if (isStatic) {
                                    classBuilder.defs(make().Block(STATIC, buildInit));
                                } else {
                                    classBuilder.getInitBuilder().init(buildInit);
                                }
                            }
                        } else if (!memoized) {
                            classBuilder.field(modifiers, attrName, type, initialValue, !useField, annos);
                            if (!isEe(model) && model.isLate() && CodegenUtil.needsLateInitField(model, typeFact())) {
                                classBuilder.field(PRIVATE | Flags.VOLATILE | Flags.TRANSIENT, Naming.getInitializationFieldName(attrName), 
                                        make().Type(syms().booleanType), 
                                        make().Literal(false), false, makeAtIgnore());
                            }
                        }
                    }
                }
            }
            
            // A shared attribute might be initialized in a for statement, so
            // we might need a def-assignment subst for it
            JCStatement outerSubs = statementGen().openOuterSubstitutionIfNeeded(
                    model, model.getType(), 0);
            if (outerSubs != null) {
                classBuilder.getInitBuilder().init(outerSubs);
            }
        }

        if (useField || withinInterface || lazy) {
            boolean generateInClassOrInterface = !withinInterface || model.isShared() || isStatic;
            boolean generateInCompanionClass = withinInterface && lazy && !isStatic;
            if (generateInClassOrInterface) {
                // Generate getter in main class or interface (when shared)
                at(decl.getType());
                AttributeDefinitionBuilder getter = makeGetter(decl, false, memoizedInitialValue);
                if (err != null) {
                    getter.getterBlock(make().Block(0, List.<JCStatement>of(err)));
                }
                classBuilder.attribute(getter);
            }
            if (generateInCompanionClass) {
                Interface container = (Interface)model.getContainer();
                // Generate getter in companion class
                classBuilder.getCompanionBuilder(container).attribute(makeGetter(decl, true, null));
            }
            if (Decl.isVariable(model) || model.isLate()) {
                if (generateInClassOrInterface) {
                    // Generate setter in main class or interface (when shared)
                    classBuilder.attribute(makeSetter(decl, false, memoizedInitialValue));
                }
                if (generateInCompanionClass) {
                    Interface container = (Interface) model.getContainer();
                    // Generate setter in companion class
                    classBuilder.getCompanionBuilder(container).attribute(makeSetter(decl, true, null));
                }
            }
        }
    }

    public AttributeDefinitionBuilder transform(AttributeSetterDefinition decl, boolean forCompanion) {
        Setter model = decl.getDeclarationModel();
        if (Strategy.onlyOnCompanion(model) && !forCompanion) {
            return null;
        }
        String name = decl.getIdentifier().getText();
        final AttributeDefinitionBuilder builder = AttributeDefinitionBuilder
                /* 
                 * We use the getter as TypedDeclaration here because this is the same type but has a refined
                 * declaration we can use to make sure we're not widening the attribute type.
                 */
            .setter(this, decl, name, model.getGetter())
            .modifiers(modifierTransformation().getterSetter(model, forCompanion));
        
        // companion class members are never actual no matter what the Declaration says
        if(forCompanion)
            builder.notActual();
        
        if (model.isClassMember() || forCompanion || model.getGetter().isStatic()) {
            JCBlock setterBlock = makeSetterBlock(model, decl.getBlock(), decl.getSpecifierExpression());
            builder.setterBlock(setterBlock);
        } else {
            builder.isFormal(true);
        }
        builder.userAnnotationsSetter(expressionGen().transformAnnotations(OutputElement.SETTER, decl));
        return builder;
    }

    public AttributeDefinitionBuilder transform(AttributeGetterDefinition decl, boolean forCompanion) {
        Value model = decl.getDeclarationModel();
        if (Strategy.onlyOnCompanion(model) && !forCompanion) {
            return null;
        }
        String name = decl.getIdentifier().getText();
        //expressionGen().transform(decl.getAnnotationList());
        final AttributeDefinitionBuilder builder = AttributeDefinitionBuilder
            .getter(this, name, model)
            .modifiers(modifierTransformation().getterSetter(model, forCompanion));
        
        // companion class members are never actual no matter what the Declaration says
        if(forCompanion)
            builder.notActual();
        
        if (model.isClassMember() || forCompanion || model.isStatic()) {
            JCBlock body = statementGen().transform(decl.getBlock());
            builder.getterBlock(body);
        } else {
            builder.isFormal(true);
        }
        builder.userAnnotations(expressionGen().transformAnnotations(OutputElement.GETTER, decl));
        return builder;    
    }

    /**
     * Encapsulates the modifiers we use for various things.
     */
    class ModifierTransformation {
        protected long declarationSharedFlags(Declaration decl){
            return decl.isShared() 
                && !Decl.isAncestorLocal(decl)
                && !decl.isPackageVisibility()
                    ? PUBLIC : 0;
        }
        
        public long jpaConstructor(Class model) {
            return PROTECTED;
        }

        public long canonicalMethodBridge() {
            return PRIVATE;
        }

        public long methodBridge(Function method) {
            return PUBLIC | (method.isDefault() ? 0 : FINAL);
        }

        public long setterBridge(Setter setter) {
            return PUBLIC | (setter.getGetter().isDefault() ? 0 : FINAL);
        }

        public long getterBridge(Value attr) {
            return PUBLIC | (attr.isDefault() ? 0 : FINAL);
        }

        public long defaultValueMethodBridge() {
            return PUBLIC | FINAL;
        }

        public long instantiatorBridgeFlags(boolean includeBody) {
            long flags = PUBLIC;
            if(includeBody)
                flags |= FINAL;
            else
                flags |= ABSTRACT;
            return flags;
        }

        public long classFlags(ClassOrInterface cdecl) {
            int result = 0;

            result |= declarationSharedFlags(cdecl);
            // aliases cannot be abstract, especially since they're just placeholders
            result |= cdecl instanceof Class && (cdecl.isAbstract() || cdecl.isFormal()) && !cdecl.isAlias() ? ABSTRACT : 0;
            result |= cdecl instanceof Interface ? INTERFACE : 0;
            // aliases are always final placeholders, final classes are also final
            result |= cdecl instanceof Class && (cdecl.isAlias() || cdecl.isFinal())  ? FINAL : 0;
            result |= cdecl.isStatic() ? STATIC : 0;

            if (isJavaStrictfp(cdecl)) {
                result |= Flags.STRICTFP;
            }
            return result;
        }
        
        public long constructor(ClassOrInterface cdecl) {
            return declarationSharedFlags(cdecl);
        }
        
        public int constructor(Constructor ctor) {
            return ctor.isShared() 
                && !Decl.isAncestorLocal(ctor) 
                && !ctor.isAbstract() 
                && !ctor.isValueConstructor()
                    ? (!ctor.isPackageVisibility() ? PUBLIC : 0) 
                    : PRIVATE;
        }

        public long typeAlias(TypeAlias decl) {
            int result = 0;

            result |= declarationSharedFlags(decl);
            result |= FINAL;
            result |= decl.isStatic() ? STATIC : 0;

            return result;
        }
        
        public long method(Function def) {
            int result = 0;

            if (def.isToplevel()) {
                result |= def.isShared() && !def.isPackageVisibility() ? PUBLIC : 0;
                result |= STATIC;
            } else if (ModelUtil.isLocalNotInitializer(def)) {
                result |= def.isShared() && !def.isPackageVisibility() ? PUBLIC : 0;
            } else {
                result |= def.isShared() ? 
                        (!def.isPackageVisibility() ? PUBLIC : 0) : 
                        (def.isStatic() && def.getContainer() instanceof Interface ? 0 : PRIVATE);
                result |= def.isFormal() && !def.isDefault() ? ABSTRACT : 0;
                result |= !(def.isFormal() || def.isDefault() || def.getContainer() instanceof Interface) ? FINAL : 0;
                result |= def.isStatic() ? STATIC : 0;
            }
            if (isJavaSynchronized(def)) {
                result |= Flags.SYNCHRONIZED;
            }
            if (isJavaStrictfp(def)) {
                result |= Flags.STRICTFP;
            }
            return result;
        }
        
        public long field(Tree.AttributeDeclaration cdecl) {
            int result = 0;

            Value val = cdecl.getDeclarationModel();
            result |= Decl.isVariable(val) || val.isLate() ? 0 : FINAL;
            result |= val.isStatic() ? STATIC : 0;
            if(!CodegenUtil.hasCompilerAnnotation(cdecl, "packageProtected"))
                result |= PRIVATE;
            
            if (isJavaTransient(val)) {
                result |= Flags.TRANSIENT;
            }
            if (isJavaVolatile(val)) {
                result |= Flags.VOLATILE;
            }
            
            return result;
        }

        public long localVar(Tree.AttributeDeclaration cdecl) {
            int result = 0;

            Value val = cdecl.getDeclarationModel();
            result |= Decl.isVariable(val) ? 0 : FINAL;
            result |= val.isStatic() ? STATIC : 0;

            return result;
        }

        /**
         * Returns the modifier flags to be used for the getter & setter for the 
         * given attribute-like declaration.  
         * @param tdecl attribute-like declaration (Value, Getter, Parameter etc)
         * @param forCompanion Whether the getter/setter is on a companion type
         * @return The modifier flags.
         */
        public long getterSetter(TypedDeclaration tdecl, boolean forCompanion) {
            if (tdecl instanceof Setter) {
                // Spec says: A setter may not be annotated shared, default or 
                // actual. The visibility and refinement modifiers of an attribute 
                // with a setter are specified by annotating the matching getter.
                tdecl = ((Setter)tdecl).getGetter();
            }
            
            int result = 0;

            result |= tdecl.isShared() ? 
                    (!tdecl.isPackageVisibility() ? PUBLIC : 0) : 
                    (tdecl.isStatic() && tdecl.getContainer() instanceof Interface ? 0 : PRIVATE);
            result |= ((tdecl.isFormal() && !tdecl.isDefault()) && !forCompanion) ? ABSTRACT : 0;
            result |= !(tdecl.isFormal() || tdecl.isDefault() || tdecl.isInterfaceMember()) || forCompanion ? FINAL : 0;
            result |= tdecl.isStatic() ? STATIC : 0;

            if (isJavaSynchronized(tdecl)) {
                result |= Flags.SYNCHRONIZED;
            }
            if (isJavaNative(tdecl)) {
                result |= Flags.NATIVE;
            }
            if (isJavaStrictfp(tdecl)) {
                result |= Flags.STRICTFP;
            }
            return result;
        }

        public long object(Value cdecl) {
            int result = 0;

            result |= FINAL;
            result |= !Decl.isAncestorLocal(cdecl) 
                    && cdecl.isShared() 
                    && !cdecl.isPackageVisibility() 
                        ? PUBLIC : 0;
            result |= cdecl.isStatic() ? STATIC : 0;
            
            if (isJavaStrictfp(cdecl)) {
                result |= Flags.STRICTFP;
            }
            return result;
        }

        public long defaultParameterMethodOverload(Function method, DaoBody daoBody) {
            long mods = method(method) & ~Flags.NATIVE;
            if (daoBody instanceof DaoAbstract == false) {
                mods &= ~ABSTRACT;
            }
            if (daoBody instanceof DaoCompanion) {
                mods |= FINAL;
            };
            return mods;
        }

        public long defaultParameterInstantiatorOverload(Class klass) {
         // remove the FINAL bit in case it gets set, because that is valid for a class decl, but
            // not for a method if in an interface
            long modifiers = classFlags(klass) & ~FINAL;
            // when refining a member class of an interface because the 
            // instantiator method is declared in the companion interface it is
            // effectively public.
            if (klass instanceof Class && klass.isActual()) {
                modifiers &= ~(PRIVATE | PROTECTED);
                modifiers |= PUBLIC;
            }
            // alias classes cannot be abstract since they're placeholders, but it's possible to have formal class aliases
            // and the instantiator method needs the abstract bit
            if(klass.isFormal() && klass.isAlias())
                modifiers |= ABSTRACT;
            return modifiers;
        }

        public long defaultParameterConstructorOverload(Class klass) {
            return constructor(klass) & (PUBLIC | PRIVATE | PROTECTED);
        }

        public long defaultParameterMethod(boolean noBody,
                //the method or class with 
                //the defaulted parameter
                Declaration container) {
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
                modifiers |= !container.isPackageVisibility() ? PUBLIC : 0;
            } else if (container == null 
                    || !container.isToplevel() && !noBody){
                modifiers |= PRIVATE;
            }
            boolean staticMethod = container != null && Strategy.defaultParameterMethodStatic(container);
            if (staticMethod) {
                // static default parameter methods should be consistently public so that if non-shared class Top and
                // shared class Bottom which extends Top both have the same default param name, we don't get an error
                // if the Bottom class tries to "hide" a static public method with a private one
                modifiers &= ~PRIVATE;
                modifiers |= STATIC | PUBLIC;
            }
            if (isJavaStrictfp(container)) {
                modifiers |= Flags.STRICTFP;
            }
            return modifiers;
        }
        
        public long transformClassParameterDeclFlags(Parameter param) {
            return param.getModel().isVariable() ? 0 : FINAL;
        }
        
        public long transformClassParameterDeclFlagsField(Parameter param, Tree.Declaration annotated) {
            long result = transformClassParameterDeclFlags(param) | PRIVATE;
            Declaration model = annotated.getDeclarationModel();
            if (isJavaTransient(model)) {
                result |= Flags.TRANSIENT;
            }
            if (isJavaVolatile(model)) {
                result |= Flags.VOLATILE;
            }
            return result;
        }
    }
    
    /** 
     * <p>In EE mode we change the modifiers:</p>
     * <ul>
     * <li>We don't generate <code>final</code> methods</li>
     * <li>The implict no-args constructor is generated as public, not protected</li>
     * </ul>
     */
    private class EeModifierTransformation extends ModifierTransformation {
        @Override
        public long jpaConstructor(Class model) {
            return model.isShared() ? PUBLIC : super.jpaConstructor(model);
        }
        @Override
        public long method(Function def) {
            long result = super.method(def);
            return result & (~FINAL);
        }
        @Override
        public long defaultParameterMethodOverload(Function method, DaoBody daoBody) {
            long result = super.defaultParameterMethodOverload(method, daoBody);
            return result & (~FINAL);
        }
        @Override
        public long getterSetter(TypedDeclaration tdecl, boolean forCompanion) {
            long result = super.getterSetter(tdecl, forCompanion);
            if (!forCompanion) {
                return result & (~FINAL);
            } else {
                return result;
            }
        }
        @Override
        public long methodBridge(Function method) {
            long result = super.methodBridge(method);
            return result & (~FINAL);
        }
        @Override
        public long setterBridge(Setter setter) {
            long result = super.setterBridge(setter);
            return result & (~FINAL);
        }
        @Override
        public long getterBridge(Value attr) {
            long result = super.getterBridge(attr);
            return result & (~FINAL);
        }
        @Override
        public long defaultValueMethodBridge() {
            long result = super.defaultValueMethodBridge();
            return result & (~FINAL);
        }
        @Override
        public long instantiatorBridgeFlags(boolean includeBody) {
            long result = super.instantiatorBridgeFlags(includeBody);
            return result & (~FINAL);
        }
        @Override
        public long defaultParameterMethod(boolean noBody, Declaration container) {
            long result = super.defaultParameterMethod(noBody, container);
            return result & (~FINAL);
        }
    }
    
    private ModifierTransformation modifierTransformation = new ModifierTransformation();
    ModifierTransformation modifierTransformation() {
        return modifierTransformation;
    }
    ModifierTransformation replaceModifierTransformation(ModifierTransformation mt) {
        ModifierTransformation old = this.modifierTransformation;
        this.modifierTransformation = mt;
        return old;
    }
    
    private AttributeDefinitionBuilder makeGetterOrSetter(Tree.AttributeDeclaration decl, boolean forCompanion,  
                                                          AttributeDefinitionBuilder builder, boolean isGetter) {
        at(decl);
        Value declarationModel = decl.getDeclarationModel();
        boolean withinInterface = declarationModel.isInterfaceMember();
        boolean isStatic = declarationModel.isStatic();
        Tree.SpecifierOrInitializerExpression specOrInit = decl.getSpecifierOrInitializerExpression();
        boolean lazy = specOrInit instanceof Tree.LazySpecifierExpression;
        if (forCompanion || lazy || withinInterface && isStatic) {
            if (specOrInit != null) {
                HasErrorException error = errors().getFirstExpressionErrorAndMarkBrokenness(specOrInit.getExpression());
                if (error != null) {
                    builder.getterBlock(make().Block(0, List.<JCStatement>of(makeThrowUnresolvedCompilationError(error))));
                } else {
                    TypedReference typedRef = getTypedReference(declarationModel);
                    TypedReference nonWideningTypedRef = nonWideningTypeDecl(typedRef);
                    Type nonWideningType = nonWideningType(typedRef, nonWideningTypedRef);
                    
                    int flags = 0;
                    if(declarationModel.hasUncheckedNullType())
                        flags |= ExpressionTransformer.EXPR_TARGET_ACCEPTS_NULL;
                    if (CodegenUtil.downcastForSmall(specOrInit.getExpression(), declarationModel))
                        flags |=  ExpressionTransformer.EXPR_UNSAFE_PRIMITIVE_TYPECAST_OK;
                    
                    JCExpression expr = expressionGen().transformExpression(specOrInit.getExpression(), 
                            CodegenUtil.getBoxingStrategy(declarationModel), 
                            nonWideningType,
                            flags);
                    expr = convertToIntIfHashAttribute(declarationModel, expr);
                    builder.getterBlock(make().Block(0, List.<JCStatement>of(make().Return(expr))));
                }
            } else {
                JCExpression accessor = naming.makeQualifiedName(
                        naming.makeQuotedThis(), 
                        declarationModel, 
                        Naming.NA_MEMBER | (isGetter ? Naming.NA_GETTER : Naming.NA_SETTER));
                
                if (isGetter) {
                    builder.getterBlock(make().Block(0, List.<JCStatement>of(make().Return(
                            make().Apply(
                                    null, 
                                    accessor, 
                                    List.<JCExpression>nil())))));
                } else {
                    List<JCExpression> args = List.<JCExpression>of(naming.makeName(declarationModel, Naming.NA_MEMBER | Naming.NA_IDENT));
                    builder.setterBlock(make().Block(0, List.<JCStatement>of(make().Exec(
                            make().Apply(
                                    null, 
                                    accessor, 
                                    args)))));
                }
                
            }
        }
        if (forCompanion) builder.notActual();
        return builder
            .modifiers(modifierTransformation().getterSetter(declarationModel, forCompanion))
            .isFormal(declarationModel.isFormal() || withinInterface && !forCompanion && !isStatic)
            .isJavaNative(declarationModel.isJavaNative());
    }
    
    private AttributeDefinitionBuilder makeGetter(Tree.AttributeDeclaration decl, boolean forCompanion, 
            JCExpression memoizedInitialValue) {
        at(decl);
        String attrName = decl.getIdentifier().getText();
        Value model = decl.getDeclarationModel();
        AttributeDefinitionBuilder getter = AttributeDefinitionBuilder
            .getter(this, attrName, model, memoizedInitialValue);
        if(!model.isInterfaceMember() 
                || (model.isShared() ^ forCompanion))
            getter.userAnnotations(expressionGen().transformAnnotations(OutputElement.GETTER, decl));
        else
            getter.ignoreAnnotations();
        
        if (Decl.isIndirect(decl)) {
            getter.getterBlock(generateIndirectGetterBlock(model));
        }
        
        return makeGetterOrSetter(decl, forCompanion, getter, true);
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

    private AttributeDefinitionBuilder makeSetter(Tree.AttributeDeclaration decl, boolean forCompanion, 
            JCExpression memoizedInitialValue) {
        at(decl);
        String attrName = decl.getIdentifier().getText();
        AttributeDefinitionBuilder setter = setter(this, decl, attrName, decl.getDeclarationModel(), memoizedInitialValue);
        setter.userAnnotationsSetter(expressionGen().transformAnnotations(OutputElement.SETTER, decl));
        return makeGetterOrSetter(decl, forCompanion, setter, false);
    }

    public List<JCTree> transformWrappedMethod(Tree.AnyMethod def, TransformationPlan plan) {
        final Function model = def.getDeclarationModel();
        if (model.isParameter()) {
            return List.nil();
        }
        naming.clearSubstitutions(model);
        // Generate a wrapper class for the method
        String name = def.getIdentifier().getText();
        ClassDefinitionBuilder builder = methodWrapper(this, name, model.isShared(), isJavaStrictfp(model));
        // Make sure it's Java Serializable (except toplevels which we never instantiate)
        if(!model.isToplevel())
            builder.introduce(make().QualIdent(syms().serializableType.tsym));

        if (Decl.isAnnotationConstructor(model)) {
            AnnotationInvocation ai = ((AnnotationInvocation)model.getAnnotationConstructor());
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
        
        if(Decl.isLocal(model) || model.isToplevel()){
            builder.annotations(makeAtLocalDeclarations(def));
        }
        if(Decl.isLocal(model)){
            builder.annotations(makeAtLocalDeclaration(model.getQualifier(), false));
        }
        builder.at(def);
        List<JCTree> result = builder.build();
        
        if (Decl.isLocal(model)) {
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
        return invocation.encode(this, new ListBuffer<JCExpression>());
    }

    private MethodDefinitionBuilder makeAnnotationMethod(Tree.Parameter parameter) {
        Parameter parameterModel = parameter.getParameterModel();
        JCExpression type = transformAnnotationMethodType(parameter);
        JCExpression defaultValue = null;
        if (parameterModel.isDefaulted()) {
            defaultValue = transformAnnotationParameterDefault(parameter);
        }
        if (parameterModel.isSequenced() && !parameterModel.isAtLeastOne()) {
            defaultValue = make().NewArray(null, null, List.<JCExpression>nil());
        }
        MethodDefinitionBuilder mdb = method(this, parameterModel.getModel(), Naming.NA_ANNOTATION_MEMBER);
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
        mdb.resultType(new TransformedType(type));
        mdb.defaultValue(defaultValue);
        mdb.noBody();
        return mdb;
    }

    public List<MethodDefinitionBuilder> transform(Tree.AnyMethod def, TransformationPlan plan, ClassDefinitionBuilder classBuilder) {
        Function model = def.getDeclarationModel();
        if (model.isParameter()) {
            return List.nil();
        }
        if (plan instanceof ThrowerMethod) {
            addRefinedThrowerMethod(classBuilder, 
                    plan.getErrorMessage().getMessage(), 
                    (Class)model.getContainer(),
                    (Function)model.getRefinedDeclaration());
            return List.<MethodDefinitionBuilder>nil();
        }
        // Transform the method body of the 'inner-most method'
        boolean prevSyntheticClassBody = expressionGen().withinSyntheticClassBody(Decl.isMpl(model)
                || ModelUtil.isLocalNotInitializer(model)
                || expressionGen().isWithinSyntheticClassBody());
        List<JCStatement> body = transformMethodBody(def);
        expressionGen().withinSyntheticClassBody(prevSyntheticClassBody);
        return transform(def, classBuilder, body);
    }

    List<MethodDefinitionBuilder> transform(Tree.AnyMethod def,
            ClassDefinitionBuilder classBuilder, List<JCStatement> body) {
        final Function model = def.getDeclarationModel();
        
        List<MethodDefinitionBuilder> result = List.<MethodDefinitionBuilder>nil();
        if (!model.isInterfaceMember() || model.isStatic()) {
            // Transform to the class
            TypedDeclaration rd = (TypedDeclaration) model.getRefinedDeclaration();
            boolean refinedResultType = !model.getType().isExactly(rd.getType());
            result = transformMethod(def, 
                    true,
                    true,
                    true,
                    transformMplBodyUnlessSpecifier(def, model, body),
                    refinedResultType 
                    && !rd.isInterfaceMember()? new DaoSuper() : new DaoThis(def, def.getParameterLists().get(0)),
                    !Strategy.defaultParameterMethodOnSelf(model)
                    && !Strategy.defaultParameterMethodStatic(model));
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
            // but only if it's shared and not java native
            if (model.isShared()
                    && !model.isJavaNative()) {
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
            final Function methodModel,
            Tree.TypeParameterList typeParameterList,
            Tree.AnyMethod node, 
            java.util.List<Tree.ParameterList> parameterLists,
            Tree.Declaration annotated,
            boolean transformMethod, boolean actual, boolean includeAnnotations, List<JCStatement> body, 
            DaoBody daoTransformation, 
            boolean defaultValuesBody) {
        
        ListBuffer<MethodDefinitionBuilder> lb = new ListBuffer<MethodDefinitionBuilder>();
        Declaration refinedDeclaration = methodModel.getRefinedDeclaration();
        
        final MethodDefinitionBuilder methodBuilder = method(this, methodModel);
        
        /*
        if (typeParameterList != null) {
            for (Tree.TypeParameterDeclaration param : typeParameterList.getTypeParameterDeclarations()) {
                TypeDeclaration container = (TypeDeclaration)param.getDeclarationModel().getContainer();
                methodBuilder.typeParameter(param.getDeclarationModel());
                //ClassDefinitionBuilder companionBuilder = methodBuilder.getCompanionBuilder(container);
                //if/(companionBuilder != null)
                //    companionBuilder.typeParameter(param);
            }
        }*/
        
        // do the reified type param arguments
        if (AbstractTransformer.supportsReified(methodModel)) {
            methodBuilder.reifiedTypeParameters(Strategy.getEffectiveTypeParameters(methodModel));
        }
        
        if (methodModel.getParameterLists().size() > 1) {
            methodBuilder.mpl(methodModel.getParameterLists());
        }
        
        boolean hasOverloads = false;
        Tree.ParameterList parameterList = parameterLists.get(0);
        int flags = 0;
        if (rawParameters(methodModel)) {
            flags |= JT_RAW;
        }
        for (final Tree.Parameter parameter : parameterList.getParameters()) {
            Parameter parameterModel = parameter.getParameterModel();
            List<JCAnnotation> annotations = null;
            if (includeAnnotations){
                Tree.TypedDeclaration typedDeclaration = Decl.getMemberDeclaration(annotated, parameter);
                // it can be null in the case of specifier refinement with no param list, but which we still optimise
                // to a real method
                // f = function(Integer param) => 2;
                if(typedDeclaration != null)
                    annotations = expressionGen().transformAnnotations(OutputElement.PARAMETER, typedDeclaration);
            }
            //methodModel.getTypedReference().getTypedParameter(parameterModel).getType()
            //parameterModel.getModel().getTypedReference().getType()
            methodBuilder.parameter(parameter, parameterModel, annotations, flags, WideningRules.CAN_WIDEN);

            if (Strategy.hasDefaultParameterValueMethod(parameterModel)
                    || Strategy.hasDefaultParameterOverload(parameterModel)) {
                if (Decl.equal(refinedDeclaration, methodModel)
                        || (!methodModel.isInterfaceMember() && body != null)
                        || methodModel.isInterfaceMember() && daoTransformation instanceof DaoCompanion == false) {
                    
                    if (daoTransformation != null && (daoTransformation instanceof DaoCompanion == false || body != null)) {
                        DaoBody daoTrans = (body == null && !methodModel.isJavaNative()) ? daoAbstract : new DaoThis(node, parameterList);
                        
                        MethodDefinitionBuilder overloadedMethod = new DefaultedArgumentMethod(daoTrans, method(this, methodModel), methodModel)
                            .makeOverload(
                                parameterList.getModel(),
                                parameter.getParameterModel(),
                                Strategy.getEffectiveTypeParameters(methodModel));
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
                && methodModel.isClassOrInterfaceMember()
                && (body != null || methodModel.isJavaNative());
        
        if (createCanonical) {
            // Creates the private "canonical" method containing the actual body
            MethodDefinitionBuilder canonicalMethod = new CanonicalMethod(daoTransformation, methodModel, body)
                .makeOverload(
                    parameterList.getModel(),
                    null,
                    Strategy.getEffectiveTypeParameters(methodModel));
            
            lb.append(canonicalMethod);
        }
        
        if (transformMethod) {
            methodBuilder.modifiers(modifierTransformation().method(methodModel) | (methodModel.isJavaNative() && !createCanonical ? Flags.NATIVE : 0));
            if (actual) {
                methodBuilder.isOverride(methodModel.isActual());
            }
            if (includeAnnotations) {
                methodBuilder.userAnnotations(expressionGen().transformAnnotations(OutputElement.METHOD, annotated));
                methodBuilder.modelAnnotations(methodModel.getAnnotations());
                if (!methodModel.isDefault() && isEe(methodModel)) {
                    methodBuilder.modelAnnotations(makeAtFinal());
                }
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
                        Strategy.getEffectiveTypeParameters(methodModel));
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
            Function model,
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
     * Constructs all but the outer-most method of a {@code Function} with 
     * multiple parameter lists 
     * @param model The {@code Function} model
     * @param body The inner-most body
     */
    List<JCStatement> transformMplBody(java.util.List<Tree.ParameterList> parameterListsTree,
            Function model,
            List<JCStatement> body) {
        Type resultType = model.getType();
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
        final Function model = def.getDeclarationModel();
        
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
            Type callableType = model.getReference().getFullType();
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
            if (!isVoid(def) || !Decl.isUnboxedVoid(model) || Strategy.useBoxedVoid((Function)model)) {
                stmt = make().Return(call);
            } else {
                stmt = make().Exec(call);
            }
            
            JCStatement result;
            if (initializingParameter == null) {
                // If the field isn't initialized by a parameter we have to 
                // cope with the possibility that it's never initialized
                final JCBinary cond = make().Binary(JCTree.Tag.EQ, makeUnquotedIdent(fieldName), makeNull());
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
        final Function model = def.getDeclarationModel();
        final Tree.Block block = def.getBlock();
        List<JCStatement> body;
        boolean prevNoExpressionlessReturn = statementGen().noExpressionlessReturn;
        Substitution substitution = null;
        JCStatement varDef = null;
        Parameter lastParameter = Decl.getLastParameterFromFirstParameterList(model);
        if(lastParameter != null
                && Decl.isJavaVariadicIncludingInheritance(lastParameter)){
            SyntheticName alias = naming.alias(lastParameter.getName());
            substitution = naming.addVariableSubst(lastParameter.getModel(), alias.getName());
            varDef = substituteSequentialForJavaVariadic(alias, lastParameter);
        }
        try {
            statementGen().noExpressionlessReturn = Decl.isMpl(model) || Strategy.useBoxedVoid(model);
            body = statementGen().transformBlock(block);
        } finally {
            statementGen().noExpressionlessReturn = prevNoExpressionlessReturn;
            if(substitution != null)
                substitution.close();
        }
        // We void methods need to have their Callables return null
        // so adjust here.
        HasErrorException error = errors().getFirstErrorBlock(block);
        if ((Decl.isMpl(model) || Strategy.useBoxedVoid(model))
                && !block.getDefinitelyReturns() && error == null) {
            if (Decl.isUnboxedVoid(model)) {
                body = body.append(make().Return(makeNull()));
            } else {
                body = body.append(make().Return(makeErroneous(block, "compiler bug: non-void method doesn't definitely return")));
            }
        }
        if(varDef != null)
            body = body.prepend(varDef);
        return body;
    }

    private JCStatement substituteSequentialForJavaVariadic(SyntheticName alias, Parameter lastParameter) {
        JCExpression seqType = makeJavaType(lastParameter.getType());
        Type seqElemType = typeFact().getIteratedType(lastParameter.getType());
        JCExpression init = javaVariadicToSequential(seqElemType, lastParameter);
        return make().VarDef(make().Modifiers(FINAL), alias.asName(), seqType , init);
    }

    List<JCStatement> transformSpecifiedMethodBody(Tree.MethodDeclaration def, SpecifierExpression specifierExpression) {
        final Function model = def.getDeclarationModel();
        Tree.MethodDeclaration methodDecl = def;
        boolean isLazy = specifierExpression instanceof Tree.LazySpecifierExpression;
        boolean returnNull = false;
        JCExpression bodyExpr;
        Tree.Term term = null;
        if (specifierExpression != null
                && specifierExpression.getExpression() != null) {
            term = TreeUtil.unwrapExpressionUntilTerm(specifierExpression.getExpression());
            HasErrorException error = errors().getFirstExpressionErrorAndMarkBrokenness(term);
            if (error != null) {
                return List.<JCStatement>of(this.makeThrowUnresolvedCompilationError(error));
            }
        }
        if (!isLazy && term instanceof Tree.FunctionArgument) {
            // Function specified with lambda: Don't bother generating a 
            // Callable, just transform the expr to use as the method body.
            Tree.FunctionArgument fa = (Tree.FunctionArgument)term;
            Type resultType = model.getType();
            returnNull = Decl.isUnboxedVoid(model);
            final java.util.List<Tree.Parameter> lambdaParams = fa.getParameterLists().get(0).getParameters();
            final java.util.List<Tree.Parameter> defParams = def.getParameterLists().get(0).getParameters();
            List<Substitution> substitutions = List.nil();
            for (int ii = 0; ii < lambdaParams.size(); ii++) {
                substitutions = substitutions.append(naming.addVariableSubst(
                        (TypedDeclaration)lambdaParams.get(ii).getParameterModel().getModel(), 
                        defParams.get(ii).getParameterModel().getName()));
            }
            List<JCStatement> body = null;
            if(fa.getExpression() != null)
                bodyExpr = gen().expressionGen().transformExpression(fa.getExpression(), 
                            returnNull ? BoxingStrategy.INDIFFERENT : CodegenUtil.getBoxingStrategy(model), 
                            resultType);
            else{
                body = gen().statementGen().transformBlock(fa.getBlock());
                // useless but satisfies branch checking
                bodyExpr = null;
            }
            for (Substitution subs : substitutions) {
                subs.close();
            }
            // if we have a whole body we're done
            if(body != null)
                return body;
        } else if (!isLazy && typeFact().isCallableType(term.getTypeModel())) {
            returnNull = isAnything(term.getTypeModel()) && term.getUnboxed();
            Function method = methodDecl.getDeclarationModel();
            boolean lazy = specifierExpression instanceof Tree.LazySpecifierExpression;
            boolean inlined = CodegenUtil.canOptimiseMethodSpecifier(term, method);
            Invocation invocation;
            if ((lazy || inlined)
                    && term instanceof Tree.MemberOrTypeExpression
                    && ((Tree.MemberOrTypeExpression)term).getDeclaration() instanceof Functional) {
                Declaration primaryDeclaration = ((Tree.MemberOrTypeExpression)term).getDeclaration();
                Reference producedReference = ((Tree.MemberOrTypeExpression)term).getTarget();
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
            invocation.setErased(CodegenUtil.hasTypeErased(term) 
                    || getReturnTypeOfCallable(term.getTypeModel()).isNothing());
            bodyExpr = expressionGen().transformInvocation(invocation);
        } else {
            Substitution substitution = null;
            JCStatement varDef = null;
            // Handle implementations of Java variadic methods
            Parameter lastParameter = Decl.getLastParameterFromFirstParameterList(model);
            if(lastParameter != null
                    && Decl.isJavaVariadicIncludingInheritance(lastParameter)){
                SyntheticName alias = naming.alias(lastParameter.getName());
                substitution = naming.addVariableSubst(lastParameter.getModel(), alias.getName());
                varDef = substituteSequentialForJavaVariadic(alias, lastParameter);
            }

            bodyExpr = expressionGen().transformExpression(model, term);
            
            if(varDef != null){
                // Turn into Let for java variadic methods
                bodyExpr = make().LetExpr(List.of(varDef), bodyExpr);
                substitution.close();
            }
            // The innermost of an MPL method declared void needs to return null
            returnNull = Decl.isUnboxedVoid(model) && Decl.isMpl(model);
        }
        
        if (CodegenUtil.downcastForSmall(term, model)) {
            bodyExpr = expressionGen().applyErasureAndBoxing(bodyExpr, term.getTypeModel(),
                    false,
                    !CodegenUtil.isUnBoxed(term), 
                    CodegenUtil.getBoxingStrategy(model), 
                    model.getType(),
                    ExpressionTransformer.EXPR_UNSAFE_PRIMITIVE_TYPECAST_OK);
        }
        
        List<JCStatement> body;
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
        if (def instanceof Function) {
            return AbstractTransformer.isAnything(((Function)def).getType());
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
            switch(Strategy.defaultParameterMethodOwner(ol.getModel())) {
            case OUTER:
            case OUTER_COMPANION:
            case SELF:
                return List.<JCExpression>nil();
            case STATIC:
                Functional f = (Functional)ol.getModel();
                return typeArguments(f instanceof Constructor ? (Class)((Constructor)f).getContainer() : f);
            default:
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
            ListBuffer<JCExpression> delegateArgs = new ListBuffer<JCExpression>();
            overloaded.appendImplicitArgumentsDelegate(typeParameterList, delegateArgs);
            ListBuffer<JCExpression> dpmArgs = new ListBuffer<JCExpression>();
            overloaded.appendImplicitArgumentsDpm(typeParameterList, dpmArgs);
            
            ListBuffer<JCStatement> vars = new ListBuffer<JCStatement>();
            
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
                                new ListBuffer<JCExpression>().appendList(dpmArgs).toList());
                    } else if (Strategy.hasEmptyDefaultArgument(parameterModel)) {
                        defaultArgument = makeEmptyAsSequential(true);
                    } else {
                        defaultArgument = makeErroneous(null, "compiler bug: parameter " + parameterModel.getName() + " has an unsupported default value");
                    }
                    Naming.SyntheticName varName = naming.temp(parameterModel.getName());
                    Type paramType = overloaded.parameterType(parameterModel);
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
            JCExpression invocation = overloaded.makeInvocation(declTree != null ? Strategy.getEffectiveTypeParameters(declTree.getDeclarationModel()) : null, args);
            Declaration model = overloaded.getModel();// TODO Yuk
            if (!isVoid(model)
                    // MPL overloads always return a Callable
                    || model instanceof Functional 
                        && Decl.isMpl((Functional) model)
                    || model instanceof Function 
                        && !(Decl.isUnboxedVoid(model))
                    || model instanceof Function 
                        && Strategy.useBoxedVoid((Function)model) 
                    || Strategy.generateInstantiator(model) 
                        && overloaded instanceof DefaultedArgumentInstantiator) {
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
            
            ListBuffer<JCExpression> args = new ListBuffer<JCExpression>();
            for (Parameter parameter : parameterList.getParameters()) {
                if (currentParameter != null && Decl.equal(parameter, currentParameter)) {
                    break;
                }
                args.add(naming.makeUnquotedIdent(parameter.getName()));
            }
            JCExpression superCall = overloaded.makeInvocation(null, args);
            /*JCMethodInvocation superCall = make().Apply(null,
                    naming.makeQualIdent(naming.makeSuper(), ((Function)overloaded.getModel()).getName()),
                    args.toList());*/
            JCExpression refinedType = makeJavaType(((Function)overloaded.getModel()).getType(), JT_NO_PRIMITIVES);
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
                overloadBuilder.parameter(null, parameter, null, 0, getWideningRules(parameter));
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
        
        protected Type parameterType(Parameter parameterModel) {
            NonWideningParam nonWideningParam = overloadBuilder.getNonWideningParam(parameterModel.getModel(), getWideningRules(parameterModel));
            Type paramType = nonWideningParam.nonWideningType;
            if (parameterModel.getModel() instanceof Function)
                paramType = typeFact().getCallableType(paramType);
            return paramType;
        }

        private WideningRules getWideningRules(Parameter parameterModel) {
            // static methods don't have to refine anything from the supertype so they're not bound to
            // widening issues
            return Strategy.defaultParameterMethodOwner(getModel()) == DefaultParameterMethodOwner.STATIC ? WideningRules.NONE : WideningRules.CAN_WIDEN;
        }

        protected abstract void initVars(Parameter currentParameter, ListBuffer<JCStatement> vars);

        protected abstract Declaration getModel();

        protected JCExpression makeInvocation(java.util.List<TypeParameter> tps, ListBuffer<JCExpression> args) {
            final JCExpression methName = makeMethodName();
            ListBuffer<JCExpression> tas = new ListBuffer<JCExpression>();
            if (tps != null) {
                for (TypeParameter tp : tps) {
                    tas.add(makeJavaType(tp.getType(), JT_TYPE_ARGUMENT));
                }
            }
            return make().Apply(
                    tas.toList(),
                    methName, 
                    args.toList());
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
        private final Function method;

        DefaultedArgumentMethod(DaoBody daoBody, MethodDefinitionBuilder mdb, Function method) {
            super(daoBody, mdb);
            this.method = method;
        }

        @Override
        protected Function getModel() {
            return method;
        }
        
        @Override
        protected long getModifiers() {
            return modifierTransformation().defaultParameterMethodOverload(method, daoBody);
        }

        @Override
        protected final JCExpression makeMethodName() {
            int flags = Naming.NA_MEMBER;
            if (method.isClassOrInterfaceMember()) {
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
        private TypedReference typedMember;
        private boolean forMixin;

        DefaultedArgumentMethodTyped(DaoBody daoBody, MethodDefinitionBuilder mdb, TypedReference typedMember, boolean forMixin) {
            super(daoBody, mdb, (Function)typedMember.getDeclaration());
            this.typedMember = typedMember;
            this.forMixin = forMixin;
        }

        @Override
        protected void resultType() {
            if (!isAnything(getModel().getType())
                    || !Decl.isUnboxedVoid(getModel())
                    || Strategy.useBoxedVoid((Function)getModel())) {
                TypedReference typedRef = (TypedReference) typedMember;
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
                final TypedReference typedParameter = typedMember.getTypedParameter(param);
                overloadBuilder.parameter(null, param, typedParameter, null, 0, forMixin ? WideningRules.FOR_MIXIN : WideningRules.CAN_WIDEN);
            }
        }
        
        @Override
        protected Type parameterType(Parameter parameter) {
            final TypedReference typedParameter = typedMember.getTypedParameter(parameter);
            NonWideningParam nonWideningParam = overloadBuilder.getNonWideningParam(typedParameter, forMixin ? WideningRules.FOR_MIXIN : WideningRules.CAN_WIDEN);
            Type paramType = nonWideningParam.nonWideningType;
            if (parameter.getModel() instanceof Function) {
                paramType = typeFact().getCallableType(paramType);
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
        
        CanonicalMethod(DaoBody daoBody, MethodDefinitionBuilder mdb, Function method) {
            super(daoBody, mdb, method);
            useBody = false;
        }
        
        CanonicalMethod(DaoBody daoBody, Function method, List<JCStatement> body) {
            super(daoBody, method(ClassTransformer.this, method, Naming.NA_CANONICAL_METHOD), method);
            this.body = body;
            useBody = true;
        }
        
        @Override
        protected long getModifiers() {
            long mods = super.getModifiers();
            if (useBody) {
                mods = mods & ~PUBLIC & ~FINAL | PRIVATE;
            }
            mods = mods & ~Flags.SYNCHRONIZED;
            if (getModel().isJavaNative()) {
                mods |= Flags.NATIVE;
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
        private final boolean delegationConstructor;

        DefaultedArgumentClass(DaoBody daoBody, MethodDefinitionBuilder mdb, Class klass, Constructor constructor, boolean delegationConstructor) {
            super(daoBody, mdb);
            this.klass = klass;
            this.constructor = constructor;
            this.delegationConstructor = delegationConstructor;
        }
        
        @Override
        protected final Declaration getModel() {
            return constructor != null ? constructor : klass;
        }
        
        @Override
        protected void appendImplicitParameters(java.util.List<TypeParameter> typeParameterList) {
            super.appendImplicitParameters(typeParameterList);
            if (constructor != null) {
                if (delegationConstructor) {
                    overloadBuilder.parameter(makeConstructorNameParameter(constructor, DeclNameFlag.QUALIFIED, DeclNameFlag.DELEGATION));
                } else if (!Decl.isDefaultConstructor(constructor)){
                    overloadBuilder.parameter(makeConstructorNameParameter(constructor));
                }
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
            DefaultParameterMethodOwner owner = Strategy.defaultParameterMethodOwner(klass);
            if (owner != DefaultParameterMethodOwner.STATIC
                    && owner != DefaultParameterMethodOwner.OUTER
                    && owner != DefaultParameterMethodOwner.OUTER_COMPANION
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
            switch(Strategy.defaultParameterMethodOwner(getModel())) {
            case OUTER:
            case OUTER_COMPANION: {
                // if we're refining a class we can't declare new default values, so we should get
                // them from the instance rather than the outer interface impl
                if(getModel().isActual() && getModel().getContainer() instanceof Interface)
                    return naming.makeUnquotedIdent("$this");
                return null;
            }
            case SELF:
                return null;
            case STATIC:
                return null;
            default:
                if (daoBody instanceof DaoCompanion) {
                    return null;
                }
                return companionInstanceName.makeIdent();
            }
        }
    }
    
    /**
     * A transformation for generating overloaded <em>constructors</em> for 
     * defaulted arguments. 
     */
    class DefaultedArgumentConstructor extends DefaultedArgumentClass {

        DefaultedArgumentConstructor(MethodDefinitionBuilder mdb, Class klass, Tree.Declaration node, Tree.ParameterList pl, boolean delegationConstructor) {
            super(new DaoThis(node, pl), mdb, klass, null, delegationConstructor);
        }
        
        DefaultedArgumentConstructor(MethodDefinitionBuilder mdb, Constructor constructor, Tree.Declaration node, Tree.ParameterList pl, boolean delegationConstructor) {
            super(new DaoThis(node, pl), mdb, (Class)constructor.getContainer(), constructor, delegationConstructor);
        }
        
        @Override
        protected long getModifiers() {
            return modifierTransformation().defaultParameterConstructorOverload(klass);
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

        private boolean forCompanionClass;

        DefaultedArgumentInstantiator(DaoBody daoBody, Class klass, Constructor ctor, boolean forCompanionClass) {
            super(daoBody, systemMethod(ClassTransformer.this, naming.getInstantiatorMethodName(klass)), klass, ctor, false);
            this.forCompanionClass = forCompanionClass;
        }

        @Override
        protected long getModifiers() {
            return modifierTransformation().defaultParameterInstantiatorOverload(klass);
            
        }

        @Override
        protected JCExpression makeMethodName() {
            return naming.makeInstantiatorMethodName(daoBody.makeMethodNameQualifier(), klass);
        }

        @Override
        protected void resultType() {
            /* Not actually part of the return type */
            overloadBuilder.ignoreModelAnnotations();
            Type et = klass.getExtendedType();
            if (!forCompanionClass
                    && !klass.isAlias() 
                    && Strategy.generateInstantiator(et.getDeclaration())
                    && klass.isActual()) {
                overloadBuilder.isOverride(true);
            }
            /**/
            
            JCExpression resultType;
            Type type = klass.isAlias() ? klass.getExtendedType() : klass.getType();
            if (Strategy.isInstantiatorUntyped(klass)) {
                // We can't expose a local type name to a place it's not visible
                resultType = make().Type(syms().objectType);
            } else {
                resultType = makeJavaType(type);
            }
            overloadBuilder.resultType(new TransformedType(resultType, null, makeAtNonNull()));
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
            Type type = klass.isAlias() ? klass.getExtendedType() : klass.getType();
            type = type.resolveAliases();
            // fetch the type parameters from the klass we're instantiating itself if any
            for(Type pt : type.getTypeArgumentList()){
                args.append(makeReifiedTypeArgument(pt));
            }
            if (constructor != null
                    && !Decl.isDefaultConstructor(constructor)) {
                args.append(naming.makeUnquotedIdent(Unfix.$name$));
            }
        }

        @Override
        protected JCExpression makeInvocation(java.util.List<TypeParameter> tps, ListBuffer<JCExpression> args) {
            Type type = klass.isAlias() ? klass.getExtendedType() : klass.getType();
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
        MethodDefinitionBuilder methodBuilder = systemMethod(this, Naming.getDefaultedParamMethodName(container, parameter));
        methodBuilder.ignoreModelAnnotations();
        if (container != null && Decl.isAnnotationConstructor(container)) {
            AnnotationInvocation ac = (AnnotationInvocation)((Function)container).getAnnotationConstructor();
            AnnotationConstructorParameter acp = ac.findConstructorParameter(parameter);
            if (acp != null && acp.getDefaultArgument() != null) {
                methodBuilder.userAnnotations(acp.getDefaultArgument().makeDpmAnnotations(expressionGen()));
            }
        }
        
        methodBuilder.modifiers(modifierTransformation().defaultParameterMethod(noBody, container));
        
        if (container instanceof Constructor) {
            copyTypeParameters((Class)container.getContainer(), methodBuilder);
            methodBuilder.reifiedTypeParameters(((Class)container.getContainer()).getTypeParameters());
        } else if(container instanceof Declaration) {
            // make sure reified type parameters are accepted
            copyTypeParameters((Declaration)container, methodBuilder);
            methodBuilder.reifiedTypeParameters(Strategy.getEffectiveTypeParameters(container));
        }
        boolean staticMethod = container != null && Strategy.defaultParameterMethodStatic(container);
        WideningRules wideningRules = !staticMethod && container instanceof Class
                ? WideningRules.CAN_WIDEN : WideningRules.NONE;
        // Add any of the preceding parameters as parameters to the method
        for (Tree.Parameter p : params.getParameters()) {
            if (p.equals(currentParam)) {
                break;
            }
            at(p);
            methodBuilder.parameter(p, p.getParameterModel(), null, 0, wideningRules);
        }

        // The method's return type is the same as the parameter's type
        NonWideningParam nonWideningParam = methodBuilder.getNonWideningParam(currentParam.getParameterModel().getModel(), wideningRules); 
        methodBuilder.resultType(nonWideningParam.nonWideningDecl, nonWideningParam.nonWideningType, nonWideningParam.flags);

        // The implementation of the method
        if (noBody) {
            methodBuilder.noBody();
        } else {
            HasErrorException error = errors().getFirstExpressionErrorAndMarkBrokenness(Decl.getDefaultArgument(currentParam).getExpression());
            if (error != null) {
                methodBuilder.body(this.makeThrowUnresolvedCompilationError(error));
            } else {
                java.util.List<TypeParameter> copiedTypeParameters = null;
                if(container instanceof Generic) {
                    copiedTypeParameters = container.getTypeParameters();
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
                def.getAnonymousClass(), containingClassBuilder, ModelUtil.isLocalNotInitializer(def.getDeclarationModel()));
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
        String javaClassName = Naming.quoteClassName(name);
        ClassDefinitionBuilder objectClassBuilder = object(
                this, javaClassName, name, Decl.isLocal(klass)).forDefinition(klass);
        
        if (Strategy.introduceJavaIoSerializable(klass, typeFact().getJavaIoSerializable())) {
            objectClassBuilder.introduce(make().QualIdent(syms().serializableType.tsym));
            if (def instanceof Tree.ObjectDefinition
                    && klass.isMember()
                    && (ModelUtil.isCaptured(klass) || model.isCaptured())) {
                addWriteReplace(klass, objectClassBuilder);
            }
        }
        makeReadResolve(def, objectClassBuilder, klass, model);
        
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
            naming.closeScopedSubstitutions(def.getScope());
        }
 
        addMissingUnrefinedMembers(def, klass, objectClassBuilder);
        satisfaction(satisfiesTypes, klass, objectClassBuilder);
        serialization(klass, objectClassBuilder);
        
        if (model != null
                && model.isToplevel()
                && def instanceof Tree.ObjectDefinition) {
            // generate a field and getter
            AttributeDefinitionBuilder builder = AttributeDefinitionBuilder
                    // TODO attr build take a JCExpression className
                    .wrapped(this, model.getName(), model, true, null)
                    .userAnnotations(makeAtIgnore())
                    .userAnnotationsSetter(makeAtIgnore())
                    .immutable()
                    .initialValue(makeNewClass(naming.makeName(model, Naming.NA_FQ | Naming.NA_WRAPPER)), BoxingStrategy.BOXED)
                    .is(PUBLIC, klass.isShared())
                    .is(STATIC, true);
            if (annotated != null) {
                builder.fieldAnnotations(expressionGen().transformAnnotations(OutputElement.FIELD, annotated));
                builder.userAnnotations(expressionGen().transformAnnotations(OutputElement.GETTER, annotated));
            }            
            objectClassBuilder.defs(builder.build());
        }
        if (annotated != null) {
            objectClassBuilder.annotations(expressionGen().transformAnnotations(OutputElement.TYPE, annotated));
            objectClassBuilder.getInitBuilder().userAnnotations(expressionGen().transformAnnotations(OutputElement.CONSTRUCTOR, annotated));
        }
        
        // make sure we set the container in case we move it out
        addAtContainer(objectClassBuilder, klass);

        objectClassBuilder
            .annotations(makeAtObject())
            .satisfies(klass.getSatisfiedTypes())
            .defs(childDefs);
        objectClassBuilder.getInitBuilder().modifiers(PRIVATE);
        objectClassBuilder.addGetTypeMethod(klass.getType());
        
        if(model != null)
            objectClassBuilder
            .modelAnnotations(model.getAnnotations())
            .modifiers(modifierTransformation().object(model));
        at(def);
        List<JCTree> result = objectClassBuilder.build();
        
        if (makeLocalInstance && !model.isStatic()) {
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
            
        } else if (model != null && model.isClassOrInterfaceMember()) {
            boolean generateGetter = ModelUtil.isCaptured(model);
            JCExpression type = makeJavaType(klass.getType());
            if (generateGetter) {
                int modifiers = TRANSIENT | PRIVATE | (model.isStatic() ? STATIC : 0);
                JCExpression initialValue = makeNull();
                containingClassBuilder.field(modifiers, name, type, initialValue, false);
                AttributeDefinitionBuilder getter = AttributeDefinitionBuilder
                .getter(this, name, model)
                .modifiers(modifierTransformation().getterSetter(model, false));
                if (def instanceof Tree.ObjectDefinition) {
                    getter.userAnnotations(expressionGen().transformAnnotations(OutputElement.GETTER, ((Tree.ObjectDefinition)def)));
                }
                ListBuffer<JCStatement> stmts = new ListBuffer<JCStatement>();
                
                stmts.add(make().If(make().Binary(JCTree.Tag.EQ,
                        naming.makeUnquotedIdent(Naming.quoteFieldName(name)),
                        makeNull()), make().Exec(make().Assign(
                                naming.makeUnquotedIdent(Naming.quoteFieldName(name)),
                                makeNewClass(makeJavaType(klass.getType()), null))), null));
                stmts.add(make().Return(naming.makeUnquotedIdent(Naming.quoteFieldName(name))));
                getter.getterBlock(make().Block(0, stmts.toList()));
                result = result.appendList(getter.build());
            } else {
                int modifiers = FINAL | (model.isStatic() ? STATIC : 0);
                JCExpression initialValue = makeNewClass(makeJavaType(klass.getType()), null);
                containingClassBuilder.field(modifiers, name, type, initialValue, true);
            }
        }
        
        return result;
    }
    
    private void makeReadResolve(Node def, ClassDefinitionBuilder objectClassBuilder,
            Class cls, 
            Value model) {
        if (Strategy.addReadResolve(cls)) {
            at(def);
            MethodDefinitionBuilder readResolve = systemMethod(this, "readResolve");
            readResolve.modifiers(PRIVATE);
            readResolve.resultType(new TransformedType(make().Type(syms().objectType)));
            JCExpression apply;
            if (cls.isToplevel()) {
                apply = make().Apply(null,
                    naming.makeQualifiedName(naming.makeTypeDeclarationExpression(null, cls, DeclNameFlag.QUALIFIED), model, Naming.NA_MEMBER | Naming.NA_GETTER),
                    List.<JCExpression>nil());
            } else {
                apply = makeNull();
            }
            readResolve.body(make().Return(apply));
            objectClassBuilder.method(readResolve);
        }
    }

    /**
     * Makes a {@code main()} method which calls the given top-level method
     * @param def
     */
    private MethodDefinitionBuilder makeMainForClass(ClassOrInterface model) {
        at(null);
        List<JCExpression> arguments = List.nil();
        if(model.isAlias()){
            TypeDeclaration constr = ((ClassAlias)model).getConstructor();
            if(constr instanceof Constructor){
                // must pass the constructor name arg
                arguments = List.of(naming.makeNamedConstructorName((Constructor) constr, false));
            }
            model = (ClassOrInterface) model.getExtendedType().getDeclaration();
        }
        JCExpression nameId = makeJavaType(model.getType(), JT_RAW);
        arguments = makeBottomReifiedTypeParameters(model.getTypeParameters(), arguments);
        JCNewClass expr = make().NewClass(null, null, nameId, arguments, null);
        return makeMainMethod(model, expr);
    }
    
    /**
     * Makes a {@code main()} method which calls the given top-level method
     * @param method
     */
    private MethodDefinitionBuilder makeMainForFunction(Function method) {
        at(null);
        JCExpression qualifiedName = naming.makeName(method, Naming.NA_FQ | Naming.NA_WRAPPER | Naming.NA_MEMBER);
        List<JCExpression> arguments = makeBottomReifiedTypeParameters(method.getTypeParameters(), List.<JCExpression>nil());
        MethodDefinitionBuilder mainMethod = makeMainMethod(method, make().Apply(null, qualifiedName, arguments));
        return mainMethod;
    }
    
    private List<JCExpression> makeBottomReifiedTypeParameters(
            java.util.List<TypeParameter> typeParameters, List<JCExpression> initialArguments) {
        List<JCExpression> arguments = initialArguments;
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
        // Add call to Util.storeArgs
        methbuilder.body(make().Exec(utilInvocation().storeArgs(makeUnquotedIdent("args"))));
        // Add call to toplevel method
        methbuilder.body(make().Exec(callee));
        return methbuilder;
    }
    
    void copyTypeParameters(Declaration def, MethodDefinitionBuilder methodBuilder) {
        for (TypeParameter t : Strategy.getEffectiveTypeParameters(def)) {
                methodBuilder.typeParameter(makeTypeParameter(t, null), t.getContainer() == def ? makeAtTypeParameter(t) : null);
            }
        
    }

    public List<JCTree> transform(final Tree.TypeAliasDeclaration def) {
        final TypeAlias model = def.getDeclarationModel();
        
        // we only create types for aliases so they can be imported with the model loader
        // and since we can't import local declarations let's just not create those types
        // in that case
        if(Decl.isAncestorLocal(model))
            return List.nil();
        
        naming.clearSubstitutions(model);
        String ceylonClassName = def.getIdentifier().getText();
        final String javaClassName = Naming.quoteClassName(def.getIdentifier().getText());

        ClassDefinitionBuilder classBuilder = ClassDefinitionBuilder
                .klass(this, javaClassName, ceylonClassName, Decl.isLocal(model));

        // class alias
        classBuilder.getInitBuilder().modifiers(PRIVATE);
        classBuilder.annotations(makeAtTypeAlias(model.getExtendedType()));
        classBuilder.annotations(expressionGen().transformAnnotations(OutputElement.TYPE, def));
        classBuilder.isAlias(true);

        // make sure we set the container in case we move it out
        addAtContainer(classBuilder, model);

        transformTypeParameters(classBuilder, model);
        
        visitClassOrInterfaceDefinition(def, classBuilder);
        return classBuilder
            .modelAnnotations(model.getAnnotations())
            .modifiers(modifierTransformation().typeAlias(model))
            .satisfies(model.getSatisfiedTypes())
            .build();
    }
    
    <T> void transformTypeParameters(GenericBuilder<T> classBuilder, Declaration model) {
        java.util.List<TypeParameter> typeParameters = Strategy.getEffectiveTypeParameters(model);
        if (typeParameters != null) {
            for (TypeParameter param : typeParameters) {
                Scope cont = param.getContainer();
                if (cont instanceof TypeDeclaration) {
                    TypeDeclaration container = (TypeDeclaration)cont; 
                    classBuilder.typeParameter(param);
                    if (classBuilder instanceof ClassDefinitionBuilder) {
                        // Copy to the companion too
                        ClassDefinitionBuilder companionBuilder = ((ClassDefinitionBuilder)classBuilder).getCompanionBuilder(container);
                        if(companionBuilder != null)
                            companionBuilder.typeParameter(param);
                    }
                }
            }
        }
    }

    /**
     * Makes a named constructor
     * @param that
     * @param classBuilder
     * @param mods
     * @param ctorName
     * @param ctorBody
     * @param declFlags
     * @return
     */
    public List<JCTree> makeNamedConstructor(
            Tree.Declaration that,
            Tree.ParameterList parameterList,
            Constructor ctor,
            ClassDefinitionBuilder classBuilder,
            boolean generateInstantiator,
            int mods,
            boolean atIgnoreCtor,
            String ctorName,
            List<JCStatement> ctorBody, 
            DeclNameFlag... declFlags) {
        ListBuffer<JCTree> result = new ListBuffer<JCTree>();
        Class clz = (Class)ctor.getContainer();
        
        at(that);
        MethodDefinitionBuilder ctorDb = constructor(this, ctor.isDeprecated());
        
        ClassDefinitionBuilder decl = null;
        ClassDefinitionBuilder impl = null;
        if (generateInstantiator) {
            ClassDefinitionBuilder containingClassBuilder = classBuilder.getContainingClassBuilder();
            Scope container = clz.getContainer();
            if (container instanceof Interface) {
                decl = containingClassBuilder;
                impl = containingClassBuilder.getCompanionBuilder((Interface)container);
            } else {
                decl = containingClassBuilder;
                impl = containingClassBuilder;
            }
            generateInstantiators(classBuilder, clz, ctor, decl, impl, that, parameterList);
        }
        
        ctorDb.userAnnotations(expressionGen().transformAnnotations(OutputElement.CONSTRUCTOR, that));
        if (atIgnoreCtor) {
            ctorDb.modelAnnotations(makeAtIgnore());
        } else if (!Decl.isDefaultConstructor(ctor)) {
            ctorDb.modelAnnotations(makeAtName(ctor.getName()));
        }
        if (ctor.isValueConstructor()) {
            ctorDb.modelAnnotations(makeAtEnumerated());
        }
        
        ctorDb.modifiers(mods);
        
        for (TypeParameter tp : Strategy.getEffectiveTypeParameters(clz)) {
            ctorDb.reifiedTypeParameter(tp);
        }
        if (ctorName != null ) {
             
            // generate a constructor name class (and constant)
            transformConstructorName(classBuilder, result, ctor, clz, mods, ctorName, declFlags);
            
            // Add the name paramter
            ctorDb.parameter(makeConstructorNameParameter(ctor, declFlags));
        }
        // Add the rest of the parameters (this worries about aliasing)
        if (parameterList != null) {
            transformClassOrCtorParameters(null, (Class)ctor.getContainer(), ctor, that, 
                    parameterList, contains(declFlags, DeclNameFlag.DELEGATION),
                    classBuilder, ctorDb, generateInstantiator, decl, impl);
        }
        
        // Transformation of body has to happen after transformation of parameter so we know about parameter aliasing.
        at(that);
        ctorDb.block(make().Block(0, ctorBody));
        result.add(ctorDb.build());
        return result.toList();
    }
    
    <E extends Enum<E>> boolean contains(E[] values, E value) {
        for (E v : values) {
            if (v == value) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Make the constructor name class, and a constant
     * 
     * (Used to identify named constructors.)
     */
    protected void transformConstructorName(
            ClassDefinitionBuilder classBuilder, ListBuffer<JCTree> result,
            Constructor ctor, Class clz, int classMods, String ctorName, 
            DeclNameFlag...declFlags) {
        boolean isDelegation = contains(declFlags, DeclNameFlag.DELEGATION);
        if (classBuilder.hasGeneratedConstructorName(ctor, isDelegation)) {
            //we have already generated the little inner class
            //that represents the constructor name, since this
            //is an overloaded named constructor
            return;
        }
        
        ClassDefinitionBuilder constructorNameClass = klass(this, ctorName, null, true);
        JCVariableDecl constructorNameConst;
        
        boolean toplevel = clz.isToplevel();
        boolean memberOfToplevel = clz.isMember()
                && ((Declaration)clz.getContainer()).isToplevel();
        
        if (ctor.isValueConstructor()) {
            classMods &= ~(PRIVATE | PROTECTED | PUBLIC);
            classMods |= FINAL;
            if (toplevel) {
                classMods |= PRIVATE | STATIC;
            } else if (memberOfToplevel) {
                if (!Decl.isAncestorLocal(ctor)) {
                    classMods |= STATIC;
                    
                }
                if (!ctor.isShared()) {
                    classMods |= PRIVATE;
                }
            }
            constructorNameConst = null;
        } else {
            if (toplevel || memberOfToplevel) {
                classMods |= STATIC | FINAL;
                constructorNameConst = make().VarDef(make().Modifiers(classMods, makeAtIgnore()),
                        names().fromString(ctorName),
                        naming.makeTypeDeclarationExpression(null, ctor, declFlags), 
                        makeNull());
            } else {
                classMods &= ~(PRIVATE | PROTECTED | PUBLIC);
                constructorNameConst = null;
            }
        }
        constructorNameClass.modifiers(classMods);
        constructorNameClass.annotations(makeAtIgnore());
        constructorNameClass.annotations(makeAtConstructorName(ctor.getName(), isDelegation));
        
        List<JCTree> ctorNameClassDecl = constructorNameClass.build();
        if (toplevel) {
            result.addAll(ctorNameClassDecl);
            classBuilder.defs(constructorNameConst);
        } else {
            ClassDefinitionBuilder containingClassBuilder = classBuilder.getContainingClassBuilder();
            if (clz.isClassMember()){
                containingClassBuilder.defs(ctorNameClassDecl);
                containingClassBuilder.defs(constructorNameConst);
            } else if (clz.isInterfaceMember()){
                containingClassBuilder.getCompanionBuilder(clz).defs(ctorNameClassDecl);
                containingClassBuilder.getCompanionBuilder(clz).defs(constructorNameConst);
            } else {
                classBuilder.defs(ctorNameClassDecl);
            }
        }
    }

}