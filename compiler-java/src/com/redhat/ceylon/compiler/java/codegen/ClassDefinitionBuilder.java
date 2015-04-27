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

import static com.sun.tools.javac.code.Flags.FINAL;
import static com.sun.tools.javac.code.Flags.INTERFACE;
import static com.sun.tools.javac.code.Flags.PRIVATE;
import static com.sun.tools.javac.code.Flags.PUBLIC;
import static com.sun.tools.javac.code.Flags.STATIC;

import com.redhat.ceylon.compiler.java.codegen.recovery.TransformationPlan;
import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Generic;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

/**
 * Builder for Java Classes. The specific properties of the "framework" of the
 * class like its name, superclass, interfaces etc can be set directly.
 * There are also three freely definable "zones" where any code can be inserted:
 * the "defs" that go at the top of the class body, the "body" that goes at
 * the bottom and the "init" the goes inside the constructor in the middle.
 * (the reason for these 3 zones is mostly historical, 2 would do just as well)
 * 
 * @author Tako Schotanus
 */
public class ClassDefinitionBuilder {
    private final AbstractTransformer gen;
    
    private final String name;
    
    private long modifiers;
    
    private InitializerBuilder initBuilder;
    
    private boolean isAlias = false;
    private boolean isLocal = false;
    
    private boolean hasConstructors = false;
    
    /** 
     * Remembers the class which we're defining, because we need this for special
     * cases in the super constructor invocation.
     */
    private ClassOrInterface forDefinition;

    private final ListBuffer<JCExpression> satisfies = ListBuffer.lb();
    private final ListBuffer<JCTypeParameter> typeParams = ListBuffer.lb();
    private final ListBuffer<JCExpression> typeParamAnnotations = ListBuffer.lb();
    
    private final ListBuffer<JCAnnotation> annotations = ListBuffer.lb();
    
    private final ListBuffer<MethodDefinitionBuilder> constructors = ListBuffer.lb();
    private final ListBuffer<JCTree> defs = ListBuffer.lb();
    private ClassDefinitionBuilder concreteInterfaceMemberDefs;
    private final ListBuffer<JCTree> also = ListBuffer.lb();
    

    private boolean built = false;
    
    private boolean isCompanion = false;
    
    private boolean isBroken = false;

    private ClassDefinitionBuilder containingClassBuilder;

    private ProducedType extendingType;

    private ProducedType thisType;

    public static ClassDefinitionBuilder klass(AbstractTransformer gen, String javaClassName, String ceylonClassName, boolean isLocal) {
        ClassDefinitionBuilder builder = new ClassDefinitionBuilder(gen, javaClassName, ceylonClassName, isLocal);
        builder.setContainingClassBuilder(gen.current());
        gen.replace(builder);
        return builder;
    }
    
    public static ClassDefinitionBuilder object(AbstractTransformer gen, String ceylonClassName, boolean isLocal) {
        return klass(gen, Naming.quoteClassName(ceylonClassName), ceylonClassName, isLocal);
    }
    
    public static ClassDefinitionBuilder methodWrapper(AbstractTransformer gen, String ceylonClassName, boolean shared) {
        final ClassDefinitionBuilder builder = new ClassDefinitionBuilder(gen, Naming.quoteClassName(ceylonClassName), null, false);
        builder.setContainingClassBuilder(gen.current());
        gen.replace(builder);
        builder.initBuilder.modifiers(PRIVATE);
        return builder
            .annotations(gen.makeAtMethod())
            .modifiers(FINAL | (shared ? PUBLIC : 0));
    }

    private ClassDefinitionBuilder(AbstractTransformer gen,  
            String javaClassName, 
            String ceylonClassName,
            boolean isLocal) {
        this.gen = gen;
        this.initBuilder = new InitializerBuilder(gen);
        this.name = javaClassName;
        this.isLocal = isLocal;
        annotations(gen.makeAtCeylon());
        
        if (ceylonClassName != null){
            if(!ceylonClassName.equals(javaClassName) || isLocal) {
                // Only add @Name if it's different from the Java name, or for local types
                // because they will have dollars inserted in their java class names
                annotations(gen.makeAtName(ceylonClassName));
            }
        }
    }
    
    void setContainingClassBuilder(ClassDefinitionBuilder containingClassBuilder) {
        this.containingClassBuilder = containingClassBuilder;
    }
    
    public String toString() {
        return "CDB for " + (isInterface() ? "interface " : "class ") + name;
    }

    ClassDefinitionBuilder getContainingClassBuilder() {
        return containingClassBuilder;
    }
    
    private ClassDefinitionBuilder getTopLevelBuilder() {
        ClassDefinitionBuilder result = this;
        while (result.getContainingClassBuilder() != null) {
            result = result.getContainingClassBuilder();
        }
        return result;
    }
    
    public List<JCTree> build() {
        if (built) {
            throw new BugException("already built");
        }
        built = true;
        ListBuffer<JCTree> defs = ListBuffer.lb();
        appendDefinitionsTo(defs);
        if (!typeParamAnnotations.isEmpty() || typeParams.size() != typeParamAnnotations.size()) {
            annotations(gen.makeAtTypeParameters(typeParamAnnotations.toList()));
        }
        
        
        JCTree.JCClassDecl klass = gen.make().ClassDef(
                gen.make().Modifiers(modifiers, getAnnotations()),
                gen.names().fromString(name),
                typeParams.toList(),
                getSuperclass(this.extendingType),
                satisfies.toList(),
                defs.toList());
        ListBuffer<JCTree> klasses = ListBuffer.<JCTree>lb();
        
        // Generate a companion class if we're building an interface
        // or the companion actually has some content 
        // (e.g. initializer with defaulted params)
        
        
        if (isInterface()) {
            if (this == getTopLevelBuilder()) {
                klasses.appendList(also.toList());
                klasses.append(klass);
                if (hasCompanion()) {
                    klasses.appendList(concreteInterfaceMemberDefs.build());
                }
            } else {
                if (hasCompanion()) {
                    klasses.appendList(concreteInterfaceMemberDefs.build());
                }
                getTopLevelBuilder().also(klass);
            }
        } else {
            klasses.appendList(also.toList());
            if (hasCompanion()) {
                klasses.appendList(concreteInterfaceMemberDefs.build());
            }
            klasses.append(klass);
        }
        
        gen.replace(getContainingClassBuilder());
        
        return klasses.toList();
    }


    private boolean isInterface() {
        return (modifiers & INTERFACE) != 0;
    }

    String getClassName() {
        return name;
    }

    private boolean hasCompanion() {
        return !isAlias
                && concreteInterfaceMemberDefs != null
                && (isInterface()
                    || !(concreteInterfaceMemberDefs.defs.isEmpty()
                    && concreteInterfaceMemberDefs.getInitBuilder().isEmptyInit()
                    && concreteInterfaceMemberDefs.constructors.isEmpty()));
    }

    private void also(JCTree also) {
        this.also.append(also);
    }

    private void appendDefinitionsTo(ListBuffer<JCTree> defs) {
        if (!isInterface()) {
            
            for (MethodDefinitionBuilder builder : constructors) {
                if (noAnnotations || ignoreAnnotations) {
                    builder.noModelAnnotations();
                }
                defs.append(builder.build());
            }
            if (!isCompanion && !hasConstructors) {
                defs.append(initBuilder.build());
            }
        }
        defs.appendList(this.defs);
    }

    private JCExpression getSuperclass(ProducedType extendedType) {
        JCExpression superclass;
        if (extendedType != null) {
            superclass = gen.makeJavaType(extendedType, CeylonTransformer.JT_EXTENDS);
            // simplify if we can
// FIXME superclass.sym can be null
//            if (superclass instanceof JCTree.JCFieldAccess 
//            && ((JCTree.JCFieldAccess)superclass).sym.type == gen.syms.objectType) {
//                superclass = null;
//            }
        } else {
            if (isInterface()) {
                // The VM insists that interfaces have java.lang.Object as their superclass
                superclass = gen.makeIdent(gen.syms().objectType);
            } else {
                superclass = null;
            }
        }
        return superclass;
    }

    private List<JCExpression> transformTypesList(java.util.List<ProducedType> types) {
        if (types == null) {
            return List.nil();
        }
        ListBuffer<JCExpression> typesList = new ListBuffer<JCExpression>();
        for (ProducedType t : types) {
            JCExpression jt = gen.makeJavaType(t, CeylonTransformer.JT_SATISFIES);
            if (jt != null) {
                typesList.append(jt);
            }
        }
        return typesList.toList();
    }
    
    public MethodDefinitionBuilder addConstructor() {
        MethodDefinitionBuilder constructor = MethodDefinitionBuilder.constructor(gen);
        this.constructors.append(constructor);
        return constructor;
    }

    public MethodDefinitionBuilder addConstructorWithInitCode() {
        MethodDefinitionBuilder constructor = addConstructor();
        constructor.body(initBuilder.build().body.stats);
        return constructor;
    }

    /*
     * Builder methods - they transform the inner state before doing the final construction
     */
    
    public ClassDefinitionBuilder modifiers(long modifiers) {
        this.modifiers = modifiers;
        if (this.concreteInterfaceMemberDefs != null) {
            this.concreteInterfaceMemberDefs.modifiers((modifiers & PUBLIC) | FINAL);
        }
        return this;
    }


    public ClassDefinitionBuilder typeParameter(String name, java.util.List<ProducedType> satisfiedTypes, java.util.List<ProducedType> caseTypes, 
                                                boolean covariant, boolean contravariant, ProducedType defaultValue, boolean addModelAnnotation) {
        typeParams.append(typeParam(name, gen.makeTypeParameterBounds(satisfiedTypes)));
        if(addModelAnnotation)
            typeParamAnnotations.append(gen.makeAtTypeParameter(name, satisfiedTypes, caseTypes, covariant, contravariant, defaultValue));
        return this;
    }

    private JCTypeParameter typeParam(String name,
            List<JCExpression> bounds) {
        return gen.make().TypeParameter(gen.names().fromString(name), bounds);
    }

    public ClassDefinitionBuilder typeParameter(TypeParameter declarationModel) {
        return typeParameter(declarationModel, true);
    }
    
    public ClassDefinitionBuilder typeParameter(TypeParameter declarationModel, boolean addModelAnnotation) {
        return typeParameter(declarationModel.getName(), 
                declarationModel.getSatisfiedTypes(),
                declarationModel.getCaseTypes(),
                declarationModel.isCovariant(),
                declarationModel.isContravariant(),
                declarationModel.getDefaultTypeArgument(),
                addModelAnnotation);
    }
    
    public ClassDefinitionBuilder typeParameter(Tree.TypeParameterDeclaration param) {
        gen.at(param);
        return typeParameter(param.getDeclarationModel());
    }

    public ClassDefinitionBuilder extending(ProducedType thisType, ProducedType extendingType, boolean hasConstructors) {
        if (!isAlias) {
            this.thisType = thisType;
            this.extendingType = extendingType;
            this.hasConstructors = hasConstructors;
        }
        return this;
    }

    public ClassDefinitionBuilder reifiedType() {
        this.satisfies.add(gen.makeReifiedTypeType());
        return this;
    }
    
    public ClassDefinitionBuilder serializable() {
        this.satisfies.add(gen.makeSerializableType());
        return this;
    }

    public ClassDefinitionBuilder satisfies(java.util.List<ProducedType> satisfies) {
        this.satisfies.addAll(transformTypesList(satisfies));
        //this.defs.addAll(appendConcreteInterfaceMembers(satisfies));
        annotations(gen.makeAtSatisfiedTypes(satisfies));
        return this;
    }

    public ClassDefinitionBuilder caseTypes(java.util.List<ProducedType> caseTypes, ProducedType ofType) {
        if (caseTypes != null || ofType != null) {
            annotations(gen.makeAtCaseTypes(caseTypes, ofType));
        }
        return this;
    }

    private boolean ignoreAnnotations = false;
    private boolean noAnnotations = false;

    /** 
     * The class will be generated with the {@code @Ignore} annotation only
     */
    public ClassDefinitionBuilder ignoreAnnotations() {
        ignoreAnnotations = true;
        return this;
    }
    
    /** 
     * The class will be generated with no annotations at all
     */
    public ClassDefinitionBuilder noAnnotations() {
        noAnnotations = true;
        return this;
    }
    
    /**
     * Adds the given annotations to this class, unless 
     * they're {@linkplain #ignoreAnnotations() ignored}
     * @see #ignoreAnnotations()
     */
    public ClassDefinitionBuilder annotations(List<JCTree.JCAnnotation> annotations) {
        this.annotations.appendList(annotations);
        return this;
    }
    
    private List<JCAnnotation> getAnnotations() {
        List<JCAnnotation> ret = List.nil();
        if (noAnnotations) {
            // nothing
        }else if (ignoreAnnotations) {
            ret = ret.prependList(gen.makeAtIgnore());
        }else{
            if (hasConstructors || thisType != null) {
                ret = ret.prependList(gen.makeAtClass(thisType, extendingType, hasConstructors));
            }
            ret = ret.prependList(this.annotations.toList());
        }
        if (isBroken) {
            ret = ret.prependList(gen.makeAtCompileTimeError());
        }
        return ret;
    }

    /**
     * Appends the attribute built by the given builder 
     * (the attribute is built without annotations if necessary).
     */
    public ClassDefinitionBuilder attribute(AttributeDefinitionBuilder adb) {
        if (adb != null) {
            defs(adb.build());
        }
        return this;
    }
    
    /**
     * Appends the method built by the given builder 
     * (the method is built without annotations if necessary).
     */
    public ClassDefinitionBuilder method(MethodDefinitionBuilder mdb) {
        if (mdb != null) {
            defs(mdb.build());
        }
        return this;
    }
    
    /**
     * Appends the methods built by the given builder 
     * (the methods are built without annotations if necessary).
     */
    public ClassDefinitionBuilder methods(List<MethodDefinitionBuilder> mdbs) {
        for (MethodDefinitionBuilder mdb : mdbs) {
            method(mdb);
        }
        return this;
    }
    
    /**
     * Appends the given tree
     */
    public ClassDefinitionBuilder defs(JCTree statement) {
        if (statement != null) {
            this.defs.append(statement);
        }
        return this;
    }
    
    /**
     * Appends the given trees.
     */
    public ClassDefinitionBuilder defs(List<JCTree> defs) {
        if (defs != null) {
            this.defs.appendList(defs);
        }
        return this;
    }

    public ClassDefinitionBuilder getCompanionBuilder(TypeDeclaration decl) {
        if (concreteInterfaceMemberDefs == null 
                // if we want a companion build for a class, allow it
                && (decl instanceof com.redhat.ceylon.compiler.typechecker.model.Class
                        // if it's an interface, let's first check if we need one
                        || CodegenUtil.isCompanionClassNeeded(decl))) {
            String className = gen.naming.getCompanionClassName(decl, false);//.replaceFirst(".*\\.", "");
            concreteInterfaceMemberDefs = new ClassDefinitionBuilder(gen, className, decl.getName(), isLocal)
                .ignoreAnnotations();
            concreteInterfaceMemberDefs.isCompanion = true;
        }
        return concreteInterfaceMemberDefs;
    }
    
    public ClassDefinitionBuilder getCompanionBuilder2(
            TypeDeclaration decl) {
        
        ClassDefinitionBuilder companionBuilder = getCompanionBuilder(decl);
        // if the interface has no need of companion, give up
        if(companionBuilder == null)
            return null;
        // make sure we get fields and init code for reified params
        if(decl instanceof Generic) {
            companionBuilder.reifiedTypeParameters(((Generic)decl).getTypeParameters());
        }
        ProducedType thisType = decl.getType();
        companionBuilder.field(PRIVATE | FINAL, 
                "$this", 
                gen.makeJavaType(thisType), 
                null, false, gen.makeAtIgnore());
        MethodDefinitionBuilder ctor = companionBuilder.addConstructorWithInitCode();
        ctor.ignoreModelAnnotations();
        if(decl instanceof Generic) {
            ctor.reifiedTypeParameters(((Generic)decl).getTypeParameters());
        }
        ctor.modifiers(decl.isShared() ? PUBLIC : 0);
        ParameterDefinitionBuilder pdb = ParameterDefinitionBuilder.implicitParameter(gen, "$this");
        pdb.type(gen.makeJavaType(thisType), null);
        // ...initialize the $this field from a ctor parameter...
        ctor.parameter(pdb);
        ListBuffer<JCStatement> bodyStatements = ListBuffer.<JCStatement>of(
                gen.make().Exec(
                        gen.make().Assign(
                                gen.makeSelect(gen.naming.makeThis(), "$this"), 
                                gen.naming.makeQuotedThis())));
        ctor.body(bodyStatements.toList());
        
        if(decl instanceof Generic
                && !((Generic)decl).getTypeParameters().isEmpty()) {
            companionBuilder.addRefineReifiedTypeParametersMethod(((Generic)decl).getTypeParameters());
        }
        return companionBuilder;
    }

    public ClassDefinitionBuilder field(int modifiers, String attrName, JCExpression type, JCExpression initialValue, boolean isLocal) {
        return field(modifiers, attrName, type, initialValue, isLocal, List.<JCTree.JCAnnotation>nil());
    }
    
    public ClassDefinitionBuilder field(int modifiers, String attrName, JCExpression type, JCExpression initialValue, boolean isLocal, 
            List<JCTree.JCAnnotation> annotations) {
        if (!isLocal) {
            // A shared or captured attribute gets turned into a class member
            Name attrNameNm = gen.names().fromString(Naming.quoteFieldName(attrName));
            defs(gen.make().VarDef(gen.make().Modifiers(modifiers, annotations), attrNameNm, type, null));
            if (initialValue != null) {
                // The attribute's initializer gets moved to the constructor
                // because it might be using locals of the initializer
                initBuilder.init(gen.make().Exec(gen.make().Assign(gen.makeSelect("this", Naming.quoteFieldName(attrName)), initialValue)));
            }
        } else {
            // Otherwise it's local to the constructor
            // Stef: pretty sure we don't want annotations on a variable defined in a constructor
            Name attrNameNm = gen.names().fromString(Naming.quoteLocalValueName(attrName));
            initBuilder.init(gen.make().VarDef(gen.make().Modifiers(modifiers), attrNameNm, type, initialValue));
        }
        return this;
    }

    public ClassDefinitionBuilder method(Tree.AnyMethod method, TransformationPlan plan) {
        methods(gen.classGen().transform(method, plan, this));
        return this;
    }

    public ClassDefinitionBuilder modelAnnotations(java.util.List<Annotation> annotations) {
        annotations(gen.makeAtAnnotations(annotations));
        return this;
    }
    
    public ClassDefinitionBuilder isAlias(boolean isAlias){
        this.isAlias = isAlias;
        return this;
    }

    public ClassDefinitionBuilder forDefinition(ClassOrInterface def) {
        this.forDefinition = def;
        this.hasConstructors = def instanceof Class && ((Class)def).hasConstructors();
        return this;
    }


    public ClassOrInterface getForDefinition() {
        return forDefinition;
    }

    public ClassDefinitionBuilder reifiedTypeParameter(Tree.TypeParameterDeclaration param) {
        TypeParameter tp = param.getDeclarationModel();
        return reifiedTypeParameter(tp);
    }


    public ClassDefinitionBuilder reifiedTypeParameter(TypeParameter tp) {
        String descriptorName = gen.naming.getTypeArgumentDescriptorName(tp);
        initBuilder.parameter(makeReifiedParameter(descriptorName));
        defs(gen.makeReifiedTypeParameterVarDecl(tp, isCompanion));
        initBuilder.init(gen.makeReifiedTypeParameterAssignment(tp));
        return this;
    }

    private ParameterDefinitionBuilder makeReifiedParameter(String descriptorName) {
        ParameterDefinitionBuilder pdb = ParameterDefinitionBuilder.implicitParameter(gen, descriptorName);
        pdb.type(gen.makeTypeDescriptorType(), List.<JCAnnotation>nil());
        pdb.modifiers(FINAL);
        if(!isCompanion)
            pdb.ignored();
        return pdb;
    }


    public ClassDefinitionBuilder addGetTypeMethod(ProducedType type){
        if (isInterface()) {
            // interfaces don't have that one
        }else{
            MethodDefinitionBuilder method = MethodDefinitionBuilder.systemMethod(gen, gen.naming.getGetTypeMethodName());
            method.modifiers(PUBLIC);
            method.resultType(List.<JCAnnotation>nil(), gen.makeTypeDescriptorType());
            method.isOverride(true);

            List<JCStatement> body = List.<JCStatement>of(gen.make().Return(gen.makeReifiedTypeArgument(type)));

            method.body(body);
            defs(method.build());
        }
        
        return this;
    }

    public void reifiedTypeParameters(java.util.List<TypeParameter> typeParameterList) {
        for(TypeParameter tp : typeParameterList) {
            reifiedTypeParameter(tp);
        }
    }
    
    public ClassDefinitionBuilder addRefineReifiedTypeParametersMethod(java.util.List<TypeParameter> typeParameterList) {
        MethodDefinitionBuilder method = MethodDefinitionBuilder.systemMethod(gen, gen.naming.getRefineTypeParametersMethodName());
        method.modifiers(PUBLIC);
        method.ignoreModelAnnotations();

        List<JCStatement> body = List.nil();
        for(TypeParameter tp : typeParameterList){
            String descriptorName = gen.naming.getTypeArgumentDescriptorName(tp);
            method.parameter(makeReifiedParameter(descriptorName));
            body = body.prepend(gen.makeReifiedTypeParameterAssignment(tp));
        }
        method.body(body);
        defs(method.build());
        return this;
    }


    public ClassDefinitionBuilder refineReifiedType(ProducedType thisType) {
        // init: $type$impl.$refine(tp1, tp2...)
        Interface iface = (Interface) thisType.getDeclaration();
        String companion = gen.naming.getCompanionFieldName(iface);
        ListBuffer<JCExpression> typeParameters = new ListBuffer<JCExpression>();
        for(ProducedType tp : thisType.getTypeArgumentList()){
            typeParameters.add(gen.makeReifiedTypeArgument(tp));
        }
        JCExpression refine = gen.make().Apply(null, gen.makeSelect(companion, gen.naming.getRefineTypeParametersMethodName()), typeParameters.toList());
        initBuilder.init(gen.make().Exec(refine));
        return this;
    }


    public void reifiedAlias(ProducedType type) {
        try (AbstractTransformer.SavedPosition savedPos = gen.noPosition()) {
            JCExpression klass = gen.makeUnerasedClassLiteral(type.getDeclaration());
            JCExpression classDescriptor = gen.make().Apply(null, gen.makeSelect(gen.makeTypeDescriptorType(), "klass"), List.of(klass));
            JCVariableDecl varDef = gen.make().VarDef(gen.make().Modifiers(PUBLIC | FINAL | STATIC, gen.makeAtIgnore()), 
                                                      gen.names().fromString(gen.naming.getTypeDescriptorAliasName()), 
                                                      gen.makeTypeDescriptorType(), 
                                                      classDescriptor);
            defs(varDef);
        }
    }
    
    public ClassDefinitionBuilder broken() {
        getTopLevelBuilder().isBroken = true;
        return this;
    }


    public void isDynamic(boolean dynamic) {
        if(dynamic)
            annotations(gen.makeAtDynamic());
    }

    public InitializerBuilder getInitBuilder() {
        return initBuilder;
    }

}
