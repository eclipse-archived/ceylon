package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.declaration.InterfaceDeclaration$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeInterface 
    extends FreeClassOrInterface
    implements ceylon.language.meta.declaration.InterfaceDeclaration {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeInterface.class);
    
    public FreeInterface(com.redhat.ceylon.compiler.typechecker.model.Interface declaration) {
        super(declaration);
    }

    @Override
    @Ignore
    public InterfaceDeclaration$impl $ceylon$language$meta$declaration$InterfaceDeclaration$impl() {
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Ignore
    @Override
    public <Type> ceylon.language.meta.model.Interface<Type> interfaceApply(TypeDescriptor $reifiedType){
        return interfaceApply($reifiedType, (Sequential)empty_.get_());
    }

    @SuppressWarnings("unchecked")
    @Override
    @TypeInfo("ceylon.language.meta.model::Interface<Type>")
    @TypeParameters({
        @TypeParameter("Type"),
    })
    public <Type> ceylon.language.meta.model.Interface<Type> interfaceApply(TypeDescriptor $reifiedType,
            @Name("typeArguments") @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.model::Type<ceylon.language::Anything>>") @Sequenced Sequential<? extends ceylon.language.meta.model.Type<?>> typeArguments){
        return (ceylon.language.meta.model.Interface<Type>) super.apply($reifiedType, typeArguments);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Ignore
    @Override
    public <Container, Type>
        ceylon.language.meta.model.MemberInterface<Container, Type> memberInterfaceApply(TypeDescriptor $reifiedContainer,
                                                                                    TypeDescriptor $reifiedType,
                                                                                    ceylon.language.meta.model.Type<? extends Container> containerType){
        
        return this.<Container, Type>memberInterfaceApply($reifiedContainer,
                                                          $reifiedType,
                                                          containerType,
                                                          (Sequential)empty_.get_());
    }

    @SuppressWarnings("unchecked")
    @TypeInfo("ceylon.language.meta.model::MemberInterface<Container,Type>")
    @TypeParameters({
        @TypeParameter("Container"),
        @TypeParameter("Type"),
    })
    @Override
    public <Container, Type>
    ceylon.language.meta.model.MemberInterface<Container, Type> memberInterfaceApply(
                @Ignore TypeDescriptor $reifiedContainer,
                @Ignore TypeDescriptor $reifiedType,
                @Name("containerType") ceylon.language.meta.model.Type<? extends Container> containerType,
                @Name("typeArguments") @Sequenced Sequential<? extends ceylon.language.meta.model.Type<?>> typeArguments){
        return (ceylon.language.meta.model.MemberInterface<Container, Type>) super.memberApply($reifiedContainer, $reifiedType, containerType, typeArguments);
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
        if(obj instanceof ceylon.language.meta.declaration.InterfaceDeclaration == false)
            return false;
        ceylon.language.meta.declaration.InterfaceDeclaration other = (ceylon.language.meta.declaration.InterfaceDeclaration) obj;
        if(!Util.eq(other.getContainer(), getContainer()))
            return false;
        return getName().equals(other.getName());
    }

    @Override
    public String toString() {
        return "interface "+super.toString();
    }
    
    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
