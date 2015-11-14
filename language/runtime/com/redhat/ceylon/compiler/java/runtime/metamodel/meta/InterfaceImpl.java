package com.redhat.ceylon.compiler.java.runtime.metamodel.meta;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.metamodel.decl.InterfaceDeclarationImpl;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 8)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
    })
public class InterfaceImpl<Type> 
    extends ClassOrInterfaceImpl<Type>
    implements ceylon.language.meta.model.Interface<Type> {

    private Object instance;
    private ceylon.language.meta.model.Type<?> container;

    // FIXME: get rid of duplicate instantiations of AppliedInterfaceType when the type in question has no type parameters
    public InterfaceImpl(@Ignore TypeDescriptor $reifiedType, 
                            com.redhat.ceylon.model.typechecker.model.Type producedType,
                            ceylon.language.meta.model.Type<?> container, Object instance) {
        super($reifiedType, producedType);
        this.container = container;
        this.instance = instance;
    }
    
    @Override
    @TypeInfo("ceylon.language.meta.declaration::InterfaceDeclaration")
    public ceylon.language.meta.declaration.InterfaceDeclaration getDeclaration() {
        return (InterfaceDeclarationImpl) super.getDeclaration();
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
        if(obj instanceof InterfaceImpl == false)
            return false;
        InterfaceImpl<?> other = (InterfaceImpl<?>) obj;
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
        checkInit();
        return TypeDescriptor.klass(InterfaceImpl.class, $reifiedType);
    }
}
