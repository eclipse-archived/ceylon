package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Sequential;
import ceylon.language.Singleton;
import ceylon.language.empty_;
import ceylon.language.meta.declaration.CallableConstructorDeclaration;
import ceylon.language.meta.declaration.ConstructorDeclaration;
import ceylon.language.meta.declaration.FunctionOrValueDeclaration;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.ParameterList;

@Ceylon(major = 8)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeClassWithInitializer 
        extends FreeClass 
        implements ceylon.language.meta.declaration.ClassWithInitializerDeclaration {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(FreeClassWithInitializer.class);
    
    public FreeClassWithInitializer(com.redhat.ceylon.model.typechecker.model.Class declaration) {
        super(declaration);
    }

    @Override
    @Ignore
    public ceylon.language.meta.declaration.ClassWithInitializerDeclaration$impl 
    $ceylon$language$meta$declaration$ClassWithInitializerDeclaration$impl() {
        return new ceylon.language.meta.declaration.ClassWithInitializerDeclaration$impl(this);
    }
    
    @Ignore
    @Override
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
    
    @TypeInfo("ceylon.language::ConstructorDeclaration[]")
    @Override
    public Sequential<? extends ConstructorDeclaration> constructorDeclarations() {
        return new Singleton<ConstructorDeclaration>(ConstructorDeclaration.$TypeDescriptor$, getDefaultConstructor());
    }
    
    @TypeInfo("ceylon.language::ConstructorDeclaration[]")
    @Override
    public <A extends java.lang.annotation.Annotation> Sequential<? extends ConstructorDeclaration> annotatedConstructorDeclarations(TypeDescriptor reified$Annotation) {
        // TODO test the single ctor against the preditace and return a singleton or not
        return (Sequential)empty_.get_();
    }
    
    public CallableConstructorDeclaration getDefaultConstructor() {
        return new FreeInitializerConstructor((com.redhat.ceylon.model.typechecker.model.Class)declaration);
    }
    
    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.declaration::FunctionOrValueDeclaration>")
    public Sequential<? extends ceylon.language.meta.declaration.FunctionOrValueDeclaration> getParameterDeclarations(){
        return super.getParameterDeclarations();
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::FunctionOrValueDeclaration|ceylon.language::Null")
    public ceylon.language.meta.declaration.FunctionOrValueDeclaration getParameterDeclaration(@Name("name") String name){
        return super.getParameterDeclaration(name);
    }

}
