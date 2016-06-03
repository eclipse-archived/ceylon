package com.redhat.ceylon.compiler.java.runtime.metamodel.meta;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.runtime.metamodel.decl.CallableConstructorDeclarationImpl;
import com.redhat.ceylon.compiler.java.runtime.metamodel.decl.ClassDeclarationImpl;
import com.redhat.ceylon.compiler.java.runtime.metamodel.decl.ClassWithInitializerDeclarationConstructor;
import com.redhat.ceylon.compiler.java.runtime.metamodel.decl.ValueConstructorDeclarationImpl;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;

import ceylon.language.AssertionError;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.declaration.CallableConstructorDeclaration;
import ceylon.language.meta.declaration.ClassDeclaration;
import ceylon.language.meta.declaration.ValueConstructorDeclaration;
import ceylon.language.meta.model.CallableConstructor;
import ceylon.language.meta.model.Class;
import ceylon.language.meta.model.FunctionModel;
import ceylon.language.meta.model.MemberClassCallableConstructor;
import ceylon.language.meta.model.ValueModel;

public class MemberClassImpl<Container, Type, Arguments extends Sequential<? extends Object>> 
    extends ClassOrInterfaceImpl<Type>
    implements ceylon.language.meta.model.MemberClass<Container, Type, Arguments> {

    @Ignore
    public final TypeDescriptor $reifiedContainer;
    @Ignore
    public final TypeDescriptor $reifiedArguments;

    public MemberClassImpl(@Ignore TypeDescriptor $reifiedContainer,
                       @Ignore TypeDescriptor $reifiedType,
                       @Ignore TypeDescriptor $reifiedArguments, 
                       com.redhat.ceylon.model.typechecker.model.Type producedType) {
        super($reifiedType, producedType);
        this.$reifiedArguments = $reifiedArguments;
        this.$reifiedContainer = $reifiedContainer;
    }

    private Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> parameterTypes;

    protected boolean hasConstructors() {
        com.redhat.ceylon.model.typechecker.model.Class decl = (com.redhat.ceylon.model.typechecker.model.Class) producedType.getDeclaration();
        return decl.hasConstructors();
    }
    
    protected boolean hasEnumerated() {
        com.redhat.ceylon.model.typechecker.model.Class decl = (com.redhat.ceylon.model.typechecker.model.Class) producedType.getDeclaration();
        return decl.hasEnumerated();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected void init() {
        super.init();
        com.redhat.ceylon.model.typechecker.model.Class decl = (com.redhat.ceylon.model.typechecker.model.Class) producedType.getDeclaration();

        // anonymous classes don't have constructors
        // local classes have constructors but if they capture anything it will get extra parameters that nobody knows about
        // FIXME: so we really want to disallow that in the metamodel?
        if(!decl.isAnonymous() 
                && !Metamodel.isLocalType(decl)
                // classes with constructors and no default constructor get Nothing
                && decl.getParameterList() != null){
            // get a list of produced parameter types
            java.util.List<com.redhat.ceylon.model.typechecker.model.Type> parameterProducedTypes = Metamodel.getParameterProducedTypes(decl.getParameterList().getParameters(), producedType);
            this.parameterTypes = Metamodel.getAppliedMetamodelSequential(parameterProducedTypes);
        }else{
            this.parameterTypes = (Sequential) empty_.get_();
        }
    }
    
    @TypeInfo("ceylon.language.meta.model::MemberClassCallableConstructor<Container, Type, Arguments>"
            + "|ceylon.language::Null")
    public MemberClassCallableConstructor<Container, Type, Arguments> getDefaultConstructor() {
        if (hasConstructors() || hasEnumerated()) {
            return (MemberClassCallableConstructor<Container, Type, Arguments>)getConstructor($reifiedArguments, "");    
        } else {
            return new MemberClassInitializerConstructor($reifiedContainer, $reifiedType, $reifiedArguments, this);
        }
    }
    
    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call$() {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call$(Object arg0) {
        return new ClassImpl<Type, Arguments>($reifiedType, $reifiedArguments, super.producedType, getContainer(), arg0);
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call$(Object arg0, Object arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call$(Object arg0, Object arg1, Object arg2) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call$(Object... args) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public short $getVariadicParameterIndex$() {
        return -1;
    }
    
    @Override
    @TypeInfo("ceylon.language.meta.declaration::ClassDeclaration")
    public ClassDeclaration getDeclaration() {
        return (ClassDeclaration) super.getDeclaration();
    }
    
    @Override
    @TypeInfo("ceylon.language.meta.model::Type<ceylon.language::Anything>")
    public ceylon.language.meta.model.Type<? extends Object> getDeclaringType() {
        return Metamodel.getAppliedMetamodel(producedType.getQualifyingType());
    }

    @Ignore
    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(MemberClassImpl.class, $reifiedContainer, $reifiedType, $reifiedArguments);
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $callvariadic$() {
        return $callvariadic$(empty_.get_());
    }
    
    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $callvariadic$(
            Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $callvariadic$(
            Object arg0, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $callvariadic$(
            Object arg0, Object arg1, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $callvariadic$(
            Object arg0, Object arg1, Object arg2, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $callvariadic$(
            Object... argsAndVarargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $callvariadic$(Object arg0) {
        return $callvariadic$(arg0, empty_.get_());
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $callvariadic$(Object arg0,
            Object arg1) {
        return $callvariadic$(arg0, arg1, empty_.get_());
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $callvariadic$(Object arg0,
            Object arg1, Object arg2) {
        return $callvariadic$(arg0, arg1, arg2, empty_.get_());
    }

    @Override
    public Class<? extends Type, ? super Arguments> bind(@TypeInfo("ceylon.language::Object") @Name("container") java.lang.Object container){
        return (Class<? extends Type, ? super Arguments>) Metamodel.bind(this, this.producedType.getQualifyingType(), container);
    }

    /*@TypeInfo("ceylon.language::Sequential<ceylon.language.meta.model::Type<ceylon.language::Anything>>")
    @Override
    public ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> getParameterTypes(){
        checkInit();
        return parameterTypes;
    }*/

    @Override
    public int hashCode() {
        int result = 1;
        result = 37 * result + getDeclaringType().hashCode();
        result = 37 * result + getDeclaration().hashCode();
        result = 37 * result + getTypeArgumentWithVariances().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof ceylon.language.meta.model.MemberClass == false)
            return false;
        ceylon.language.meta.model.MemberClass<?, ?, ?> other = (ceylon.language.meta.model.MemberClass<?, ?, ?>) obj;
        return getDeclaration().equals(other.getDeclaration())
                && getDeclaringType().equals(other.getDeclaringType())
                && getTypeArgumentWithVariances().equals(other.getTypeArgumentWithVariances());
    }

    @Override
    @TypeInfo("ceylon.language.meta.model::Type<ceylon.language::Anything>")
    public ceylon.language.meta.model.Type<?> getContainer(){
        return getDeclaringType();
    }
    
    @Override
    @TypeParameters({@TypeParameter(
            value="Arguments",
            satisfies="ceylon.language::Anything[]")})
    @TypeInfo("ceylon.language.meta.model::MemberClassCallableConstructor<Container,Type,Arguments>|ceylon.language.meta.model::MemberClassValueConstructor<Container,Type>|ceylon.language::Null")
    public <Arguments extends Sequential<? extends Object>>java.lang.Object getConstructor(
            @Ignore
            TypeDescriptor $reified$Arguments,
            @Name("name")
            String name) {
        checkInit();
        final ceylon.language.meta.declaration.Declaration ctor = ((ClassDeclarationImpl)declaration).getConstructorDeclaration(name);
        if (ctor instanceof CallableConstructorDeclaration) {
            if (((CallableConstructorDeclarationImpl)ctor).declaration.isShared()) {
                return getDeclaredConstructor($reified$Arguments, name); 
            } else {
                return null;
            }
        }
        else if (ctor instanceof ValueConstructorDeclaration) {
            if (((ValueConstructorDeclarationImpl)ctor).declaration.isShared()) {
                return getDeclaredConstructor($reified$Arguments, name);
            } else {
                return null;
            }
        }
        return null;
    }
    
    @Override
    @TypeParameters({@TypeParameter(
            value="Arguments",
            satisfies="ceylon.language::Anything[]")})
    @TypeInfo("ceylon.language.meta.model::MemberClassCallableConstructor<Container,Type,Arguments>|ceylon.language.meta.model::MemberClassValueConstructor<Container,Type>|ceylon.language::Null")
    public <Arguments extends Sequential<? extends Object>>java.lang.Object getDeclaredConstructor(
            @Ignore
            TypeDescriptor $reified$Arguments,
            @Name("name")
            String name) {
        checkInit();
        final ceylon.language.meta.declaration.Declaration ctor = ((ClassDeclarationImpl)declaration).getConstructorDeclaration(name);
        if(ctor == null)
            return null;
        if (ctor instanceof CallableConstructorDeclaration) {
            if (ctor instanceof ClassWithInitializerDeclarationConstructor) {
                return new MemberClassInitializerConstructor<>(
                        this.$reifiedContainer, this.$reifiedType, $reified$Arguments, 
                        this);
            }
            CallableConstructorDeclarationImpl callableCtor = (CallableConstructorDeclarationImpl)ctor;
            // anonymous classes don't have parameter lists
            //TypeDescriptor actualReifiedArguments = Metamodel.getTypeDescriptorForArguments(declaration.declaration.getUnit(), (Functional)callableCtor.constructor, this.producedType);
    
            // This is all very ugly but we're trying to make it cheaper and friendlier than just checking the full type and showing
            // implementation types to the user, such as AppliedMemberClass
            //Metamodel.checkReifiedTypeArgument("getConstructor", "Constructor<$1,$2>",
            //        // this line is bullshit since it's always true, but otherwise we can't substitute the error message above :(
            //        Variance.OUT, this.producedType, $reifiedType,
            //        Variance.IN, Metamodel.getProducedType(actualReifiedArguments), $reifiedArguments);
            //return new AppliedConstructor<Type,Args>(this.$reifiedType, actualReifiedArguments, this, constructorType, ctor, this.instance);
            com.redhat.ceylon.model.typechecker.model.Reference reference;
            if (callableCtor.declaration instanceof Function) {
                reference = ((Function)callableCtor.declaration).appliedTypedReference(producedType, null);
            } else if (callableCtor.declaration instanceof com.redhat.ceylon.model.typechecker.model.Class) {
                reference = ((com.redhat.ceylon.model.typechecker.model.Class)callableCtor.declaration).appliedReference(producedType, null);
            } else if (callableCtor.declaration instanceof com.redhat.ceylon.model.typechecker.model.Constructor) {
                reference = ((com.redhat.ceylon.model.typechecker.model.Constructor)callableCtor.declaration).appliedReference(producedType, null);
            } else {
                throw Metamodel.newModelError("Unexpect declaration " +callableCtor.declaration);
            }
            return new MemberClassCallableConstructorImpl<Container,Type,Sequential<? extends java.lang.Object>>(
                    $reifiedContainer, 
                    this.$reifiedType,
                    $reified$Arguments,
                    reference, 
                    callableCtor, 
                    this);
        } else if (ctor instanceof ValueConstructorDeclaration){
            ValueConstructorDeclarationImpl callableCtor = (ValueConstructorDeclarationImpl)ctor;
            //com.redhat.ceylon.model.typechecker.model.Type constructorType = callableCtor.constructor.appliedType(this.producedType, Collections.<com.redhat.ceylon.model.typechecker.model.Type>emptyList());
            TypedDeclaration val = (TypedDeclaration)callableCtor.constructor.getContainer().getDirectMember(callableCtor.constructor.getName(), null, false);
            return new MemberClassValueConstructorImpl<Container, Type>(
                    $reifiedContainer, // <Container>
                    this.$reifiedType, // <Get>
                    callableCtor,
                    val.getTypedReference(),
                    this);
        } else {
            throw new AssertionError("Constructor neither CallableConstructorDeclaration nor ValueConstructorDeclaration");
        }
        /*checkInit();
        final FreeConstructor ctor = (FreeConstructor)((FreeClass)declaration).getConstructorDeclaration(name);
        if(ctor == null)
            return null;
        return new AppliedMemberClassConstructor($reifiedContainer, this.$reifiedType, reified$Arguments, this, ctor.constructor.appliedType(this.producedType, Collections.<com.redhat.ceylon.model.typechecker.model.Type>emptyList()), ctor);*/
    }

    @Override
    public <Arguments extends Sequential<? extends Object>> Sequential<? extends FunctionModel<Type, Arguments>> getCallableConstructors(
            TypeDescriptor reified$Arguments) {
        return getCallableConstructors(reified$Arguments, (Sequential)empty_.get_());
    }
    
    @Override
    public <Arguments extends Sequential<? extends Object>> Sequential<? extends FunctionModel<Type, Arguments>> getCallableConstructors(
            TypeDescriptor reified$Arguments,
            @Sequenced
            ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<? extends java.lang.annotation.Annotation>> annotations) {
        return Metamodel.getConstructors(this, true, true, reified$Arguments, annotations);
    }
    
    @Override
    public <Arguments extends Sequential<? extends Object>> Sequential<? extends FunctionModel<Type, Arguments>> getDeclaredCallableConstructors(
            TypeDescriptor reified$Arguments) {
        return getDeclaredCallableConstructors(reified$Arguments, (Sequential)empty_.get_());
    }
    
    @Override
    public <Arguments extends Sequential<? extends Object>> Sequential<? extends FunctionModel<Type, Arguments>> getDeclaredCallableConstructors(
            TypeDescriptor reified$Arguments,
            @Sequenced
            ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<? extends java.lang.annotation.Annotation>> annotations) {
        return Metamodel.getConstructors(this, false, true, reified$Arguments, annotations);
    }
    
    @Override
    public Sequential<? extends ValueModel<Type, java.lang.Object>> getValueConstructors() {
        return getValueConstructors((Sequential)empty_.get_());
    }
    
    @Override
    public Sequential<? extends ValueModel<Type, java.lang.Object>> getValueConstructors(
            @Sequenced
            ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<? extends java.lang.annotation.Annotation>> annotations) {
        return Metamodel.getConstructors(this, true, false, null, annotations);
    }
    
    @Override
    public Sequential<? extends ValueModel<Type, java.lang.Object>> getDeclaredValueConstructors() {
        return getDeclaredValueConstructors((Sequential)empty_.get_());
    }
    
    @Override
    public Sequential<? extends ValueModel<Type, java.lang.Object>> getDeclaredValueConstructors(
            @Sequenced
            ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<? extends java.lang.annotation.Annotation>> annotations) {
        return Metamodel.getConstructors(this, false, false, null, annotations);
    }
}
