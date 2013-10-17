package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Anything;
import ceylon.language.meta.model.Variable;
import ceylon.language.meta.model.VariableAttribute;
import ceylon.language.meta.declaration.SetterDeclaration;
import ceylon.language.meta.declaration.VariableDeclaration;
import ceylon.language.meta.declaration.VariableDeclaration$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;


@Ceylon(major = 6)
@Class
@SatisfiedTypes("ceylon.language.meta.declaration::Variable")
public class FreeVariable
        extends FreeValue
        implements VariableDeclaration {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(FreeVariable.class);
    
    public FreeVariable(com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration value) {
        super(value);
    }
    
    @Ignore
    @Override
    public VariableDeclaration$impl $ceylon$language$meta$declaration$VariableDeclaration$impl() {
        return null;
    }

    @Override
    public boolean getVariable(){
        return true;
    }

    @TypeInfo("ceylon.language::Anything")
    @Override
    public Object set(@TypeInfo("ceylon.language::Anything") @Name("newValue") Object newValue){
        return apply(Anything.$TypeDescriptor$).unsafeSet(newValue);
    }

    @TypeInfo("ceylon.language::Anything")
    @Override
    public Object memberSet(@Name("container") @TypeInfo("ceylon.language::Object") Object container,
            @TypeInfo("ceylon.language::Anything") @Name("newValue") Object newValue){
        ceylon.language.meta.model.Type<?> containerType = Metamodel.getAppliedMetamodel(Metamodel.getTypeDescriptor(container));
        return memberApply(TypeDescriptor.NothingType, Anything.$TypeDescriptor$, containerType).bind(container).unsafeSet(newValue);
    }

    @Override
    public int hashCode() {
        int result = 1;
        java.lang.Object container = getContainer();
        result = 37 * result + (container == null ? 0 : container.hashCode());
        result = 37 * result + getName().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof ceylon.language.meta.declaration.VariableDeclaration == false)
            return false;
        ceylon.language.meta.declaration.VariableDeclaration other = (ceylon.language.meta.declaration.VariableDeclaration) obj;
        if(!Util.eq(other.getContainer(), getContainer()))
            return false;
        return getName().equals(other.getName());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    @TypeInfo("ceylon.language.meta.model::Variable<Type>")
    @TypeParameters({
        @TypeParameter("Type"),
    })
    public <Type> ceylon.language.meta.model.Variable<Type> apply(@Ignore TypeDescriptor $reifiedType){
        return (Variable<Type>) super.apply($reifiedType);
    }

    @SuppressWarnings("unchecked")
    @TypeInfo("ceylon.language.meta.model::VariableAttribute<Container,Type>")
    @TypeParameters({
        @TypeParameter("Container"),
        @TypeParameter("Type"),
    })
    @Override
    public <Container, Type>
        ceylon.language.meta.model.VariableAttribute<Container, Type> memberApply(
                @Ignore TypeDescriptor $reifiedContainer,
                @Ignore TypeDescriptor $reifiedType,
                @Name("containerType") ceylon.language.meta.model.Type<? extends Container> containerType){
        return (VariableAttribute<Container, Type>) super.memberApply($reifiedContainer, $reifiedType, containerType);
    }


    @Ignore
    @Override
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
    
    @TypeInfo("ceylon.language.meta.declaration::SetterDeclaration")
    @Override
    public SetterDeclaration getSetter() {
        // FIXME: let's not allocate all the time
        return new FreeSetter(this);
    }
    
}
