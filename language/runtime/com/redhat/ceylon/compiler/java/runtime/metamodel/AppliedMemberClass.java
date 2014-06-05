package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.declaration.ClassDeclaration;
import ceylon.language.meta.model.Class;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

public class AppliedMemberClass<Container, Type, Arguments extends Sequential<? extends Object>> 
    extends AppliedClassOrInterface<Type>
    implements ceylon.language.meta.model.MemberClass<Container, Type, Arguments> {

    @Ignore 
    final TypeDescriptor $reifiedContainer;
    @Ignore
    final TypeDescriptor $reifiedArguments;

    AppliedMemberClass(@Ignore TypeDescriptor $reifiedContainer,
                       @Ignore TypeDescriptor $reifiedType,
                       @Ignore TypeDescriptor $reifiedArguments, 
                       ProducedType producedType) {
        super($reifiedType, producedType);
        this.$reifiedArguments = $reifiedArguments;
        this.$reifiedContainer = $reifiedContainer;
    }

    private Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> parameterTypes;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected void init() {
        super.init();
        com.redhat.ceylon.compiler.typechecker.model.Class decl = (com.redhat.ceylon.compiler.typechecker.model.Class) producedType.getDeclaration();

        // anonymous classes don't have constructors
        // local classes have constructors but if they capture anything it will get extra parameters that nobody knows about
        // FIXME: so we really want to disallow that in the metamodel?
        if(!decl.isAnonymous() && !Metamodel.isLocalType(decl)){
            // get a list of produced parameter types
            java.util.List<ProducedType> parameterProducedTypes = Metamodel.getParameterProducedTypes(decl.getParameterList().getParameters(), producedType);
            this.parameterTypes = Metamodel.getAppliedMetamodelSequential(parameterProducedTypes);
        }else{
            this.parameterTypes = (Sequential) empty_.get_();
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
        return new AppliedClass<Type, Arguments>($reifiedType, $reifiedArguments, super.producedType, getContainer(), arg0);
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
        return TypeDescriptor.klass(AppliedMemberClass.class, $reifiedContainer, $reifiedType, $reifiedArguments);
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

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends Type, ? super Arguments> bind(@TypeInfo("ceylon.language::Object") @Name("container") java.lang.Object container){
        return (Class<? extends Type, ? super Arguments>) Metamodel.bind(this, this.producedType.getQualifyingType(), container);
    }

    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.model::Type<ceylon.language::Anything>>")
    @Override
    public ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> getParameterTypes(){
        checkInit();
        return parameterTypes;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 37 * result + getDeclaringType().hashCode();
        result = 37 * result + getDeclaration().hashCode();
        result = 37 * result + getTypeArguments().hashCode();
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
                && getTypeArguments().equals(other.getTypeArguments());
    }

    @Override
    @TypeInfo("ceylon.language.meta.model::Type<ceylon.language::Anything>")
    public ceylon.language.meta.model.Type<?> getContainer(){
        return getDeclaringType();
    }
}
