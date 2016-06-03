package com.redhat.ceylon.compiler.java.runtime.metamodel.decl;

import ceylon.language.Sequential;
import ceylon.language.empty_;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 8)
@com.redhat.ceylon.compiler.java.metadata.Class
public class InterfaceDeclarationImpl 
    extends ClassOrInterfaceDeclarationImpl
    implements ceylon.language.meta.declaration.InterfaceDeclaration {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(InterfaceDeclarationImpl.class);
    
    public InterfaceDeclarationImpl(com.redhat.ceylon.model.typechecker.model.Interface declaration) {
        super(declaration);
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
                                                                                    ceylon.language.meta.model.Type<? extends Object> containerType){
        
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
                @Name("containerType") ceylon.language.meta.model.Type<? extends Object> containerType,
                @Name("typeArguments") @Sequenced Sequential<? extends ceylon.language.meta.model.Type<?>> typeArguments){
        return (ceylon.language.meta.model.MemberInterface<Container, Type>) super.memberApply($reifiedContainer, $reifiedType, containerType, typeArguments);
    }

    @Override
    public int hashCode() {
        return Metamodel.hashCode(this, "interface");
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof InterfaceDeclarationImpl == false)
            return false;
        return Metamodel.equalsForSameType(this, (InterfaceDeclarationImpl)obj);
    }

    @Override
    public String toString() {
        return "interface "+super.toString();
    }
    
    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
}
