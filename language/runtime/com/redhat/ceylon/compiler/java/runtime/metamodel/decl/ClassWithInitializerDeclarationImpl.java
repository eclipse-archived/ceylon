package com.redhat.ceylon.compiler.java.runtime.metamodel.decl;

import ceylon.language.DeprecationAnnotation;
import ceylon.language.Sequence;
import ceylon.language.Sequential;
import ceylon.language.SharedAnnotation;
import ceylon.language.Singleton;
import ceylon.language.ThrownExceptionAnnotation;
import ceylon.language.empty_;
import ceylon.language.meta.declaration.CallableConstructorDeclaration;
import ceylon.language.meta.model.Type;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Predicates;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Predicates.Predicate;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.model.typechecker.model.Declaration;

@Ceylon(major = 8)
@com.redhat.ceylon.compiler.java.metadata.Class
public class ClassWithInitializerDeclarationImpl 
        extends ClassDeclarationImpl 
        implements ceylon.language.meta.declaration.ClassWithInitializerDeclaration {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(ClassWithInitializerDeclarationImpl.class);
    
    public ClassWithInitializerDeclarationImpl(com.redhat.ceylon.model.typechecker.model.Class declaration) {
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
    
    @TypeInfo("ceylon.language.meta.declaration::ConstructorDeclaration[]")
    @Override
    public Sequence<? extends CallableConstructorDeclaration> constructorDeclarations() {
        return new Singleton<CallableConstructorDeclaration>(CallableConstructorDeclaration.$TypeDescriptor$, getDefaultConstructor());
    }
    
    @TypeInfo("ceylon.language.meta.declaration::ConstructorDeclaration[]")
    @Override
    public <A extends java.lang.annotation.Annotation> Sequential<? extends CallableConstructorDeclaration> annotatedConstructorDeclarations(TypeDescriptor reified$Annotation) {
        Type<?> at = Metamodel.getAppliedMetamodel(reified$Annotation);
        if (at.subtypeOf(Metamodel.getAppliedMetamodel(
                TypeDescriptor.union(SharedAnnotation.$TypeDescriptor$,
                        DeprecationAnnotation.$TypeDescriptor$,
                        ThrownExceptionAnnotation.$TypeDescriptor$)))) {
            Predicate<Declaration> p = Predicates.isDeclarationAnnotatedWith(reified$Annotation, at);
            p.accept(declaration);
            return constructorDeclarations();
        } else {
            return (Sequential)empty_.get_();
        }
    }
    
    @Override
    public CallableConstructorDeclaration getDefaultConstructor() {
        return new ClassWithInitializerDeclarationConstructor((com.redhat.ceylon.model.typechecker.model.Class)declaration);
    }
    
    @TypeInfo("ceylon.language.meta.declaration::CallableConstructorDeclaration")
    @Override
    public ceylon.language.meta.declaration.CallableConstructorDeclaration getConstructorDeclaration(String name) {
        return name.isEmpty() ? getDefaultConstructor() : null;
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
    
    @Override
    @Ignore
    public java.lang.annotation.Annotation[] $getJavaAnnotations$() {
        java.lang.reflect.Constructor<?> ctor = Metamodel.getJavaConstructor((com.redhat.ceylon.model.typechecker.model.Class)declaration, null);
        java.lang.annotation.Annotation[] classAnnos = super.$getJavaAnnotations$();
        java.lang.annotation.Annotation[] ctorAnnos;
        if (ctor != null) {
            ctorAnnos = ctor.getAnnotations();
        } else {
            ctorAnnos = new java.lang.annotation.Annotation[0];
        }
        java.lang.annotation.Annotation[] result = new java.lang.annotation.Annotation[classAnnos.length + ctorAnnos.length];
        System.arraycopy(classAnnos, 0, result, 0, classAnnos.length);
        System.arraycopy(ctorAnnos, 0, result, classAnnos.length, ctorAnnos.length);
        return result;
    }
}
