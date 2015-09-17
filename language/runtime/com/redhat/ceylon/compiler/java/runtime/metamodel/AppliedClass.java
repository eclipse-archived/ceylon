package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.ArrayList;
import java.util.Collections;

import ceylon.language.Array;
import ceylon.language.AssertionError;
import ceylon.language.Entry;
import ceylon.language.Iterable;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.declaration.AnnotatedDeclaration;
import ceylon.language.meta.declaration.CallableConstructorDeclaration;
import ceylon.language.meta.declaration.NestableDeclaration;
import ceylon.language.meta.declaration.ValueConstructorDeclaration;
import ceylon.language.meta.model.Applicable;
import ceylon.language.meta.model.CallableConstructor;
import ceylon.language.meta.model.FunctionModel;
import ceylon.language.meta.model.InvocationException;
import ceylon.language.meta.model.ValueConstructor;
import ceylon.language.meta.model.ValueModel;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.language.ObjectArrayIterable;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor.Nothing;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.Reference;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;

@Ceylon(major = 8)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
    @TypeParameter(value = "Arguments", variance = Variance.IN, satisfies = "ceylon.language::Sequential<ceylon.language::Anything>"),
    })
public class AppliedClass<Type, Arguments extends Sequential<? extends Object>> 
    extends AppliedClassOrInterface<Type>
    implements ceylon.language.meta.model.Class<Type, Arguments>, DefaultValueProvider {

    @Ignore
    final TypeDescriptor $reifiedArguments;
    private Object instance;
    private final ceylon.language.meta.model.Type<?> container;
    private volatile boolean initialized = false;
    private ConstructorDispatch<Type,Arguments> dispatch = null;
    
    // FIXME: get rid of duplicate instantiations of AppliedClassType when the type in question has no type parameters
    public AppliedClass(@Ignore TypeDescriptor $reifiedType, 
                        @Ignore TypeDescriptor $reifiedArguments,
                        com.redhat.ceylon.model.typechecker.model.Type producedType, 
                        ceylon.language.meta.model.Type<?> container, Object instance) {
        super($reifiedType, producedType);
        this.$reifiedArguments = $reifiedArguments;
        this.container = container;
        this.instance = instance;
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::ClassDeclaration")
    public ceylon.language.meta.declaration.ClassDeclaration getDeclaration() {
        return (ceylon.language.meta.declaration.ClassDeclaration) super.getDeclaration();
    }

    protected boolean hasConstructors() {
        com.redhat.ceylon.model.typechecker.model.Class decl = (com.redhat.ceylon.model.typechecker.model.Class) producedType.getDeclaration();
        return decl.hasConstructors();
    }
    
    protected boolean hasEnumerated() {
        com.redhat.ceylon.model.typechecker.model.Class decl = (com.redhat.ceylon.model.typechecker.model.Class) producedType.getDeclaration();
        return decl.hasEnumerated();
    }
    
    void checkConstructor() {
        if(((FreeClass)declaration).getAbstract())
        throw new InvocationException("Abstract class cannot be instantiated");
        if(((FreeClass)declaration).getAnonymous())
        throw new InvocationException("Object class cannot be instantiated");
    }
    
    ConstructorDispatch<Type, Arguments> getDispatch() {
        if (!initialized) {
            synchronized(this) {
                if (!initialized) {
                    checkConstructor();
                    Reference reference;
                    if (!hasConstructors() && !hasEnumerated()) {
                        reference = producedType;
                    } else {
                        reference = ((com.redhat.ceylon.model.typechecker.model.Class)declaration.declaration).getDefaultConstructor().appliedReference(producedType, null);
                    }
                    this.dispatch = new ConstructorDispatch<Type,Arguments>(
                            reference,
                            this, null,
                            ((Class)producedType.getDeclaration()).getFirstParameterList().getParameters(), 
                            instance);
                    this.initialized = true;
                }
            }
        }
        return dispatch;
    }
    
    @Ignore
    @Override
    public Type $call$() {
        ConstructorDispatch<Type, Arguments> dc = getDispatch();
        if (dc != null) {
            return dc.$call$();
        } else {
            throw new AssertionError("class lacks a default constructor");
        }
    }

    @Ignore
    @Override
    public Type $call$(Object arg0) {
        ConstructorDispatch<Type, Arguments> dc = getDispatch();
        if (dc != null) {
            return dc.$call$(arg0);
        } else {
            throw new AssertionError("class lacks a default constructor");
        }
    }

    @Ignore
    @Override
    public Type $call$(Object arg0, Object arg1) {
        ConstructorDispatch<Type, Arguments> dc = getDispatch();
        if (dc != null) {
            return dc.$call$(arg0, arg1);
        } else {
            throw new AssertionError("class lacks a default constructor");
        }
    }

    @Ignore
    @Override
    public Type $call$(Object arg0, Object arg1, Object arg2) {
        ConstructorDispatch<Type, Arguments> dc = getDispatch();
        if (dc != null) {
            return dc.$call$(arg0, arg1, arg2);
        } else {
            throw new AssertionError("class lacks a default constructor");
        }
    }

    @Ignore
    @Override
    public Type $call$(Object... args) {
        ConstructorDispatch<Type, Arguments> dc = getDispatch();
        if (dc != null) {
            return dc.$call$((Object[])args);
        } else {
            throw new AssertionError("class lacks a default constructor");
        }
    }
    
    @Override
    @Ignore
    public Type $callvariadic$() {
        return $call$();
    }
    
    @Override
    @Ignore
    public Type $callvariadic$(Sequential<?> varargs) {
        return $call$(varargs);
    }

    @Override
    @Ignore
    public Type $callvariadic$(Object arg0,
            Sequential<?> varargs) {
        return $call$(arg0, varargs);
    }

    @Override
    @Ignore
    public Type $callvariadic$(Object arg0,
            Object arg1, Sequential<?> varargs) {
        return $call$(arg0, arg1, varargs);
    }

    @Override
    @Ignore
    public Type $callvariadic$(Object arg0,
            Object arg1, Object arg2, Sequential<?> varargs) {
        return $call$(arg0, arg1, arg2, varargs);
    }

    @Override
    @Ignore
    public Type $callvariadic$(Object... argsAndVarargs) {
        return $call$((Object[])argsAndVarargs);
    }

    @Override
    @Ignore
    public Type $callvariadic$(Object arg0) {
        return $call$(arg0, empty_.get_());
    }

    @Override
    @Ignore
    public Type $callvariadic$(Object arg0, Object arg1) {
        return $call$(arg0, arg1, empty_.get_());
    }

    @Override
    @Ignore
    public Type $callvariadic$(Object arg0, Object arg1, Object arg2) {
        return $call$(arg0, arg1, arg2, empty_.get_());
    }

    @Ignore
    @Override
    public short $getVariadicParameterIndex$() {
        ConstructorDispatch<Type, Arguments> dc = getDispatch();
        if (dc != null) {
            return dc.$getVariadicParameterIndex$();
        } else {
            throw new AssertionError("class lacks a default constructor");
        }
    }

    @Ignore
    @Override
    public Type apply(){
        return apply(empty_.get_());
    }

    
    @Override
    public int hashCode() {
        int result = 1;
        // in theory, if our instance is the same, our containing type should be the same
        // and if we don't have an instance we're a toplevel and have no containing type
        result = 37 * result + (instance == null ? 0 : instance.hashCode());
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
        if(obj instanceof AppliedClass == false)
            return false;
        AppliedClass<?,?> other = (AppliedClass<?,?>) obj;
        // in theory, if our instance is the same, our containing type should be the same
        // and if we don't have an instance we're a toplevel and have no containing type
        return getDeclaration().equals(other.getDeclaration())
                && Util.eq(instance, other.instance)
                && getTypeArgumentWithVariances().equals(other.getTypeArgumentWithVariances());
    }

    @Override
    @TypeInfo("ceylon.language.meta.model::Type<ceylon.language::Anything>|ceylon.language::Null")
    public ceylon.language.meta.model.Type<?> getContainer(){
        return container;
    }

    @Ignore
    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(AppliedClass.class, $reifiedType, $reifiedArguments);
    }
    
    @Override
    @TypeInfo("ceylon.language.meta.model::CallableConstructor<Type,Arguments>|ceylon.language.meta.model::Class<Type,Arguments>|ceylon.language::Null")
    public Applicable<Type, Arguments> getDefaultConstructor() {
        if (hasConstructors() || hasEnumerated()) {
            Object ctor = getConstructor($reifiedArguments, "");
            if (ctor instanceof CallableConstructor) {
                return ((CallableConstructor<Type, Arguments>)ctor);
            } else {
                return null;
            }
        } else {
            return this;
        }
    }
    
    @Override
    @TypeParameters(@TypeParameter(value="Arguments", satisfies="ceylon.language::Sequential<ceylon.language::Anyything>"))
    @TypeInfo("ceylon.language.meta.model::CallableConstructor<Type,Arguments>|ceylon.language.meta.model::ValueConstructor<Type>|ceylon.language::Null")
    public <Arguments extends Sequential<? extends Object>> java.lang.Object getConstructor(
            @Ignore
            TypeDescriptor $reified$Arguments,
            @Name("name")
            String name) {
        checkInit();
        final ceylon.language.meta.declaration.Declaration ctor = ((FreeClass)declaration).getConstructorDeclaration(name);
        if (ctor instanceof CallableConstructorDeclaration) {
            if (((FreeCallableConstructor)ctor).declaration.isShared()) {
                return getDeclaredConstructor($reified$Arguments, name); 
            } else {
                return null;
            }
        }
        else if (ctor instanceof ValueConstructorDeclaration) {
            if (((FreeValueConstructor)ctor).declaration.isShared()) {
                return getDeclaredConstructor($reified$Arguments, name);
            } else {
                return null;
            }
        }
        return null;
    }
    
    @Override
    @TypeParameters(@TypeParameter(value="Arguments", satisfies="ceylon.language::Sequential<ceylon.language::Anyything>"))
    @TypeInfo("ceylon.language.meta.model::CallableConstructor<Type,Arguments>|ceylon.language.meta.model::ValueConstructor<Type>|ceylon.language::Null")
    public <Arguments extends Sequential<? extends Object>> java.lang.Object getDeclaredConstructor(
            @Ignore
            TypeDescriptor $reified$Arguments,
            @Name("name")
            String name) {
        checkInit();
        final ceylon.language.meta.declaration.Declaration ctor = ((FreeClass)declaration).getConstructorDeclaration(name);
        if(ctor == null)
            return null;
        if (ctor instanceof CallableConstructorDeclaration) {
            FreeCallableConstructor callableCtor = (FreeCallableConstructor)ctor;
            com.redhat.ceylon.model.typechecker.model.Type constructorType = callableCtor.constructor.appliedType(this.producedType, Collections.<com.redhat.ceylon.model.typechecker.model.Type>emptyList());
            // anonymous classes don't have parameter lists
            //TypeDescriptor actualReifiedArguments = Metamodel.getTypeDescriptorForArguments(declaration.declaration.getUnit(), (Functional)callableCtor.constructor, this.producedType);
    
            // This is all very ugly but we're trying to make it cheaper and friendlier than just checking the full type and showing
            // implementation types to the user, such as AppliedMemberClass
            //Metamodel.checkReifiedTypeArgument("getConstructor", "Constructor<$1,$2>",
            //        // this line is bullshit since it's always true, but otherwise we can't substitute the error message above :(
            //        Variance.OUT, this.producedType, $reifiedType,
            //        Variance.IN, Metamodel.getProducedType(actualReifiedArguments), $reifiedArguments);
            //return new AppliedConstructor<Type,Args>(this.$reifiedType, actualReifiedArguments, this, constructorType, ctor, this.instance);
            //Reference reference = ((Function)callableCtor.declaration).getReference();
            Reference reference;
            if (callableCtor.declaration instanceof Function) {
                reference = ((Function)callableCtor.declaration).appliedTypedReference(producedType, null);
            } else if (callableCtor.declaration instanceof com.redhat.ceylon.model.typechecker.model.Class) {
                reference = ((com.redhat.ceylon.model.typechecker.model.Class)callableCtor.declaration).appliedReference(producedType, null);
            } else if (callableCtor.declaration instanceof com.redhat.ceylon.model.typechecker.model.Constructor) {
                reference = ((com.redhat.ceylon.model.typechecker.model.Constructor)callableCtor.declaration).appliedReference(producedType, null);
            } else {
                throw Metamodel.newModelError("Unexpect declaration " +callableCtor.declaration);
            }
            AppliedCallableConstructor<Type, Sequential<? extends Object>> appliedConstructor = new AppliedCallableConstructor<Type,Sequential<? extends java.lang.Object>>(
                    this.$reifiedType, 
                    $reified$Arguments,
                    reference, 
                    callableCtor, 
                    this, null);
            Metamodel.checkReifiedTypeArgument("apply", "CallableConstructor<$1,$2>", 
                    Variance.OUT, producedType, $reifiedType, 
                    Variance.IN, Metamodel.getProducedTypeForArguments(
                            declaration.declaration.getUnit(), 
                            (Functional)callableCtor.declaration, reference), $reified$Arguments);
            return appliedConstructor;
        } else if (ctor instanceof ValueConstructorDeclaration){
            FreeValueConstructor callableCtor = (FreeValueConstructor)ctor;
            com.redhat.ceylon.model.typechecker.model.Type constructorType = callableCtor.constructor.appliedType(this.producedType, Collections.<com.redhat.ceylon.model.typechecker.model.Type>emptyList());
            TypedDeclaration val = (TypedDeclaration)callableCtor.constructor.getContainer().getDirectMember(callableCtor.constructor.getName(), null, false);
            return new AppliedValueConstructor<Type,java.lang.Object>(
                    this.$reifiedType,
                    TypeDescriptor.NothingType,
                    callableCtor, val.getTypedReference(),
                    this, null);
        } else {
            throw new AssertionError("Constructor neither CallableConstructorDeclaration nor ValueConstructorDeclaration");
        }
    }

    @Override
    public Type apply(Sequential<? extends Object> arguments) {
        return getDispatch().apply(arguments);
    }

    @Override
    public Type namedApply(
            Iterable<? extends Entry<? extends ceylon.language.String, ? extends Object>, ? extends Object> arguments) {
        return getDispatch().namedApply(arguments);
    }

    @Override
    public Object getDefaultParameterValue(Parameter parameter,
            Array<Object> values, int collectedValueCount) {
        return getDispatch().getDefaultParameterValue(parameter, values, collectedValueCount);
    }
    
    private Sequential getConstructors(boolean justShared,
            TypeDescriptor $reified$Arguments,
            ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<? extends java.lang.annotation.Annotation>> annotations) {
        ArrayList<Object> ctors = new ArrayList<>();
        com.redhat.ceylon.model.typechecker.model.Type reifiedArguments = $reified$Arguments == null ? null : Metamodel.getProducedType($reified$Arguments);
        TypeDescriptor[] annotationTypeDescriptors = Metamodel.getTypeDescriptors(annotations);
        for (ceylon.language.meta.declaration.Declaration d : ((FreeClass)declaration).constructors()) {
            Declaration dd = null;
            if (d instanceof FreeCallableConstructor) {
                dd = ((FreeCallableConstructor)d).declaration;
            } else if (d instanceof FreeValueConstructor) {
                dd = ((FreeValueConstructor)d).declaration;
            }
            // ATM this is an AND WRT annotation types: all must be present
            if(!hasAllAnnotations((AnnotatedDeclaration)d, annotationTypeDescriptors))
                continue;
            if (reifiedArguments != null) {
                // CallableConstructor need a check on the <Arguments>
                Reference producedReference = dd.appliedReference(producedType, Collections.<com.redhat.ceylon.model.typechecker.model.Type>emptyList());
                com.redhat.ceylon.model.typechecker.model.Type argumentsType = Metamodel.getProducedTypeForArguments(
                        dd.getUnit(), 
                        (Functional) dd, 
                        producedReference);
                if(!reifiedArguments.isSubtypeOf(argumentsType))
                    continue;
            }
            if (!justShared || (d instanceof NestableDeclaration
                    && ((NestableDeclaration)d).getShared())) {
                ctors.add(getDeclaredConstructor(TypeDescriptor.NothingType, d.getName()));
            }
        }
        
        Object[] array = ctors.toArray(new Object[ctors.size()]);
        ObjectArrayIterable<ceylon.language.meta.declaration.Declaration> iterable = 
                new ObjectArrayIterable<ceylon.language.meta.declaration.Declaration>(
                        TypeDescriptor.union(
                                TypeDescriptor.klass(FunctionModel.class, this.$reifiedType, TypeDescriptor.NothingType),
                                TypeDescriptor.klass(ValueModel.class, this.$reifiedType, TypeDescriptor.NothingType)),
                        (Object[]) array);
        return (ceylon.language.Sequential) iterable.sequence();
    }
    
    @SuppressWarnings({ "hiding" })
    private <Container,Type,Arguments extends Sequential<? extends Object>> void addConstructorIfCompatible(@Ignore TypeDescriptor $reifiedContainer,
            @Ignore TypeDescriptor $reifiedType,
            @Ignore TypeDescriptor $reifiedArguments,
            ArrayList<ceylon.language.meta.model.Method<? super Container,? extends Type,? super Arguments>> members,
            FreeFunction decl, com.redhat.ceylon.model.typechecker.model.Type qualifyingType, 
            AppliedClassOrInterface<Container> containerMetamodel,
            com.redhat.ceylon.model.typechecker.model.Type reifiedType, 
            com.redhat.ceylon.model.typechecker.model.Type reifiedArguments){
        // now the types
        Reference producedReference = decl.declaration.appliedReference(qualifyingType, Collections.<com.redhat.ceylon.model.typechecker.model.Type>emptyList());
        com.redhat.ceylon.model.typechecker.model.Type type = producedReference.getType();
        if(!type.isSubtypeOf(reifiedType))
            return;
        com.redhat.ceylon.model.typechecker.model.Type argumentsType = Metamodel.getProducedTypeForArguments(decl.declaration.getUnit(), (Functional) decl.declaration, producedReference);
        if(!reifiedArguments.isSubtypeOf(argumentsType))
            return;
        // it's compatible!
        members.add(decl.<Container,Type,Arguments>memberApply($reifiedContainer, $reifiedType, $reifiedArguments, containerMetamodel));
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
        return getConstructors(true, reified$Arguments, annotations);
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
        return getConstructors(false, reified$Arguments, annotations);
    }
    
    
    @Override
    public Sequential<? extends ValueModel<Type, java.lang.Object>> getValueConstructors() {
        return getValueConstructors((Sequential)empty_.get_());
    }
    
    @Override
    public Sequential<? extends ValueModel<Type, java.lang.Object>> getValueConstructors(
            @Sequenced
            ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<? extends java.lang.annotation.Annotation>> annotations) {
        return getConstructors(true, null, annotations);
    }
    
    @Override
    public Sequential<? extends ValueModel<Type, java.lang.Object>> getDeclaredValueConstructors() {
        return getDeclaredValueConstructors((Sequential)empty_.get_());
    }
    
    @Override
    public Sequential<? extends ValueModel<Type, java.lang.Object>> getDeclaredValueConstructors(
            @Sequenced
            ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<? extends java.lang.annotation.Annotation>> annotations) {
        return getConstructors(false, null, annotations);
    }
    
    @Override
    @TypeInfo("ceylon.language::Sequential<Type>")
    public ceylon.language.Sequential<? extends Type> getCaseValues(){
        Class classDecl = (Class)declaration.declaration;
        if (classDecl.hasEnumerated()) {
            // if enumerated => not abstract => can't have type cases,
            // can only possibly have value constructor cases
            if (classDecl.getCaseTypes() == null 
                    || classDecl.getCaseTypes().isEmpty()) {
                // it's not a closed enum of value constructors
                return (Sequential)empty_.get_();
            }
            ArrayList<Type> ctors = new ArrayList<>();
            for (ceylon.language.meta.declaration.Declaration d : ((FreeClass)declaration).constructors()) {
                Declaration dd = null;
                if (d instanceof FreeCallableConstructor) {
                    continue;
                } else if (d instanceof FreeValueConstructor) {
                    dd = ((FreeValueConstructor)d).declaration;
                }
                // ATM this is an AND WRT annotation types: all must be present
                ctors.add(((ValueConstructor<Type, ?>)getDeclaredConstructor(TypeDescriptor.NothingType, d.getName())).get());
            }
            
            Object[] array = ctors.toArray(new Object[ctors.size()]);
            ObjectArrayIterable<ceylon.language.meta.declaration.Declaration> iterable = 
                    new ObjectArrayIterable<ceylon.language.meta.declaration.Declaration>(
                            this.$reifiedType,
                            (Object[]) array);
            return (ceylon.language.Sequential) iterable.sequence();
        } else {
            return super.getCaseValues();
        }
    }
}
