package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ceylon.language.Anything;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.declaration.ClassDeclaration$impl;
import ceylon.language.meta.declaration.ConstructorDeclaration;
import ceylon.language.meta.declaration.FunctionOrValueDeclaration;
import ceylon.language.meta.declaration.ValueDeclaration;

import com.redhat.ceylon.compiler.java.language.ObjectArrayIterable;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Predicates.Predicate;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.ParameterList;

@Ceylon(major = 8)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeClass 
    extends FreeClassOrInterface
    implements ceylon.language.meta.declaration.ClassDeclaration {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(FreeClass.class);
    private Sequential<? extends ceylon.language.meta.declaration.FunctionOrValueDeclaration> parameters;
    private List<ConstructorDeclaration> constructors;
    
    public FreeClass(com.redhat.ceylon.model.typechecker.model.Class declaration) {
        super(declaration);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected void init() {
        super.init();
        // anonymous classes don't have parameter lists
        if(!declaration.isAnonymous()){
            com.redhat.ceylon.model.typechecker.model.Class classDeclaration = (com.redhat.ceylon.model.typechecker.model.Class)declaration;
            if(classDeclaration.isAbstraction()){
                List<Declaration> overloads = classDeclaration.getOverloads();
                if(overloads.size() == 1){
                    classDeclaration = (com.redhat.ceylon.model.typechecker.model.Class) overloads.get(0);
                }else{
                    throw Metamodel.newModelError("Class has more than one overloaded constructor");
                }
            }
            ParameterList parameterList = classDeclaration.getParameterList();
            if (parameterList != null) {
                this.parameters = FunctionalUtil.getParameters(classDeclaration);
            } else {
                this.parameters = (Sequential<? extends FunctionOrValueDeclaration>) (Sequential)empty_.get_();
            }
        }else{
            this.parameters = (Sequential<? extends FunctionOrValueDeclaration>) (Sequential)empty_.get_();
        }
        if (((Class)declaration).hasConstructors()) {
            this.constructors = new LinkedList<ConstructorDeclaration>();
            for (Declaration d : declaration.getMembers()) {
                if (d instanceof Constructor) {
                    this.constructors.add(Metamodel.<ConstructorDeclaration>getOrCreateMetamodel(d));
                }
            }
        } else {
            this.constructors = Collections.emptyList();
        }
    }
    
    @Override
    @Ignore
    public ClassDeclaration$impl $ceylon$language$meta$declaration$ClassDeclaration$impl() {
        return null;
    }

    @Override
    public boolean getAnonymous(){
        return declaration.isAnonymous();
    }

    @TypeInfo("ceylon.language.meta.declaration::ValueDeclaration|ceylon.language::Null")
    @Override
    public ceylon.language.meta.declaration.ValueDeclaration getObjectValue(){
        if(getAnonymous()){
            Object container = getContainer();
            if(container instanceof ceylon.language.meta.declaration.ClassOrInterfaceDeclaration){
                return ((ceylon.language.meta.declaration.ClassOrInterfaceDeclaration) container).getDeclaredMemberDeclaration(ValueDeclaration.$TypeDescriptor$, getName());
            }else if(container instanceof ceylon.language.meta.declaration.Package){
                return ((ceylon.language.meta.declaration.Package) container).getMember(ValueDeclaration.$TypeDescriptor$, getName());
            }
            // don't know how to find the object value decl
        }
        return null;
    }

    
    @Override
    public boolean getAnnotation(){
        return declaration.isAnnotation();
    }

    @Override
    public boolean getAbstract() {
        return ((com.redhat.ceylon.model.typechecker.model.Class)declaration).isAbstract();
    }
    
    @Override
    public boolean getSerializable() {
        return ((com.redhat.ceylon.model.typechecker.model.Class)declaration).isSerializable();
    }

    @Override
    public boolean getFinal() {
        return ((com.redhat.ceylon.model.typechecker.model.Class)declaration).isFinal();
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.declaration::FunctionOrValueDeclaration>")
    public Sequential<? extends ceylon.language.meta.declaration.FunctionOrValueDeclaration> getParameterDeclarations(){
        checkInit();
        return parameters;
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::FunctionOrValueDeclaration|ceylon.language::Null")
    public ceylon.language.meta.declaration.FunctionOrValueDeclaration getParameterDeclaration(@Name("name") String name){
        checkInit();
        return FunctionalUtil.getParameterDeclaration(this.parameters, name);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Ignore
    @Override
    public <Type, Arguments extends Sequential<? extends Object>> ceylon.language.meta.model.Class<Type, Arguments> classApply(TypeDescriptor $reifiedType,
            TypeDescriptor $reifiedArguments){
        return classApply($reifiedType, $reifiedArguments, (Sequential)empty_.get_());
    }

    @SuppressWarnings("unchecked")
    @Override
    @TypeInfo("ceylon.language.meta.model::Class<Type,Arguments>")
    @TypeParameters({
        @TypeParameter("Type"),
        @TypeParameter(value = "Arguments", satisfies = "ceylon.language::Sequential<ceylon.language::Anything>")
    })
    public <Type, Arguments extends Sequential<? extends Object>> ceylon.language.meta.model.Class<Type, Arguments> classApply(@Ignore TypeDescriptor $reifiedType,
            @Ignore TypeDescriptor $reifiedArguments,
            @Name("typeArguments") @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.model::Type<ceylon.language::Anything>>") @Sequenced Sequential<? extends ceylon.language.meta.model.Type<?>> typeArguments){
        if(!getToplevel())
            throw new ceylon.language.meta.model.TypeApplicationException("Cannot apply a member declaration with no container type: use memberApply");
        List<com.redhat.ceylon.model.typechecker.model.Type> producedTypes = Metamodel.getProducedTypes(typeArguments);
        Metamodel.checkTypeArguments(null, declaration, producedTypes);
        com.redhat.ceylon.model.typechecker.model.Reference appliedType = declaration.appliedReference(null, producedTypes);
        AppliedClass<Type, Arguments> ret = (AppliedClass<Type, Arguments>) Metamodel.getAppliedMetamodel(appliedType.getType());;
        Metamodel.checkReifiedTypeArgument("classApply", "Class<$1,$2>", Variance.OUT, appliedType.getType(), $reifiedType, 
                Variance.IN, Metamodel.getProducedType(ret.$reifiedArguments), $reifiedArguments);
        return ret;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Ignore
    @Override
    public <Container, Type, Arguments extends Sequential<? extends Object>>
        ceylon.language.meta.model.MemberClass<Container, Type, Arguments> memberClassApply(TypeDescriptor $reifiedContainer,
                                                                                       TypeDescriptor $reifiedType,
                                                                                       TypeDescriptor $reifiedArguments,
                                                                                       ceylon.language.meta.model.Type<? extends Object> containerType){
        
        return this.<Container, Type, Arguments>memberClassApply($reifiedContainer,
                                                 $reifiedType,
                                                 $reifiedArguments,
                                                 containerType,
                                                 (Sequential)empty_.get_());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @TypeInfo("ceylon.language.meta.model::MemberClass<Container,Type,Arguments>")
    @TypeParameters({
        @TypeParameter("Container"),
        @TypeParameter("Type"),
        @TypeParameter(value = "Arguments", satisfies = "ceylon.language::Sequential<ceylon.language::Anything>")
    })
    @Override
    public <Container, Type, Arguments extends Sequential<? extends Object>>
    ceylon.language.meta.model.MemberClass<Container, Type, Arguments> memberClassApply(
                @Ignore TypeDescriptor $reifiedContainer,
                @Ignore TypeDescriptor $reifiedType,
                @Ignore TypeDescriptor $reifiedArguments,
                @Name("containerType") ceylon.language.meta.model.Type<? extends Object> containerType,
                @Name("typeArguments") @Sequenced Sequential<? extends ceylon.language.meta.model.Type<?>> typeArguments){
        if(getToplevel())
            throw new ceylon.language.meta.model.TypeApplicationException("Cannot apply a toplevel declaration to a container type: use apply");

        ceylon.language.meta.model.MemberClass<Container, Type, Arguments> member 
            = (ceylon.language.meta.model.MemberClass)
                getAppliedClassOrInterface(null, null, typeArguments, containerType);
        
        // This is all very ugly but we're trying to make it cheaper and friendlier than just checking the full type and showing
        // implementation types to the user, such as AppliedMemberClass
        TypeDescriptor actualReifiedContainer = ((AppliedMemberClass)member).$reifiedContainer;
        TypeDescriptor actualReifiedArguments = ((AppliedMemberClass)member).$reifiedArguments;
        com.redhat.ceylon.model.typechecker.model.Type actualType = Metamodel.getModel((ceylon.language.meta.model.Type<?>) member);
        Metamodel.checkReifiedTypeArgument("memberApply", "Member<$1,Class<$2,$3>>&Class<$2,$3>", 
                Variance.IN, Metamodel.getProducedType(actualReifiedContainer), $reifiedContainer, 
                Variance.OUT, actualType, $reifiedType,
                Variance.IN, Metamodel.getProducedType(actualReifiedArguments), $reifiedArguments);
        return member;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Ignore
    @Override
    public ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<?>> instantiate$typeArguments(){
        return (ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<?>>)(Sequential)empty_.get_();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Ignore
    @Override
    public java.lang.Object instantiate(){
        return instantiate((ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<?>>)(Sequential)empty_.get_());
    }

    @Ignore
    @Override
    public java.lang.Object instantiate(
            ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<?>> typeArguments){
        return instantiate(typeArguments, empty_.get_());
    }

    @TypeInfo("ceylon.language::Object")
    @Override
    public java.lang.Object instantiate(
            @Name("typeArguments") @Defaulted 
            @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.model::Type<ceylon.language::Anything>>")
            ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<?>> typeArguments,
            @Name("arguments") @Sequenced @TypeInfo("ceylon.language::Sequential<ceylon.language::Anything>") 
            ceylon.language.Sequential<?> arguments){
        return classApply(Anything.$TypeDescriptor$, TypeDescriptor.NothingType, typeArguments).apply(arguments);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Ignore
    @Override
    public ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<?>> 
        memberInstantiate$typeArguments(java.lang.Object container){
        return (ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<?>>)(Sequential)empty_.get_();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Ignore
    @Override
    public java.lang.Object memberInstantiate(java.lang.Object container){
        return memberInstantiate(container, (ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<?>>)(Sequential)empty_.get_());
    }

    @Ignore
    @Override
    public java.lang.Object memberInstantiate(
            java.lang.Object container,
            ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<?>> typeArguments){
        return memberInstantiate(container, typeArguments, empty_.get_());
    }

    @SuppressWarnings("unchecked")
    @TypeInfo("ceylon.language::Object")
    @Override
    public java.lang.Object memberInstantiate(
            @Name("container") @TypeInfo("ceylon.language::Object")
            java.lang.Object container,
            @Name("typeArguments") @Defaulted 
            @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.model::Type<ceylon.language::Anything>>")
            ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<?>> typeArguments,
            @Name("arguments") @Sequenced @TypeInfo("ceylon.language::Sequential<ceylon.language::Anything>") 
            ceylon.language.Sequential<?> arguments){
        ceylon.language.meta.model.Type<?> containerType = Metamodel.getAppliedMetamodel(Metamodel.getTypeDescriptor(container));
        return memberClassApply(TypeDescriptor.NothingType, Anything.$TypeDescriptor$, TypeDescriptor.NothingType, 
                containerType, typeArguments).bind(container).apply(arguments);
    }

    @Override
    public int hashCode() {
        return Metamodel.hashCode(this, "class");
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof FreeClass == false)
            return false;
        return Metamodel.equalsForSameType(this, (FreeClass)obj);
    }

    @Override
    public String toString() {
        return "class "+super.toString();
    }
    
    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
    
    @TypeInfo("ceylon.language.meta.declaration::ConstructorDeclaration|ceylon.language::Null")
    public ConstructorDeclaration getConstructorDeclaration(
            @Name("name")
            String name) {
        checkInit();
        for (ConstructorDeclaration ctor : this.constructors) {
            if (ctor.getName().equals(name)) {
                return ctor;
            }
        }
        return null;
    }
    
    @TypeInfo("ceylon.language.meta.declaration::ConstructorDeclaration|ceylon.language::Null")
    public ConstructorDeclaration getDefaultConstructorDeclaration() {
        return getConstructorDeclaration("");
    }
    
    @Ignore
    private Sequential<? extends ConstructorDeclaration> filterConstructors(Predicate predicate) {
        if (predicate == Predicates.false_()) {
            return (Sequential)empty_.get_();
        }
        checkInit();
        ArrayList<ConstructorDeclaration> ctors = new ArrayList<ConstructorDeclaration>(constructors.size());
        for(ConstructorDeclaration decl : constructors){
            if (predicate.accept(((FreeConstructor)decl).constructor)) {
                ctors.add(decl);
            }
        }
        java.lang.Object[] array = ctors.toArray(new ConstructorDeclaration[ctors.size()]);
        ObjectArrayIterable<ConstructorDeclaration> iterable = 
                new ObjectArrayIterable<ConstructorDeclaration>(ConstructorDeclaration.$TypeDescriptor$, (ConstructorDeclaration[]) array);
        return (ceylon.language.Sequential) iterable.sequence();
    }
    
    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.declaration::ConstructorDeclaration>")
    public Sequential<? extends ConstructorDeclaration> constructorDeclarations() {
        return filterConstructors(Predicates.TRUE);
    }
    
    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.declaration::ConstructorDeclaration>")
    public <A extends java.lang.annotation.Annotation> Sequential<? extends ConstructorDeclaration> annotatedConstructorDeclarations(TypeDescriptor reified$Annotation) {
        return filterConstructors(Predicates.isDeclarationAnnotatedWith(reified$Annotation));
    }
}
