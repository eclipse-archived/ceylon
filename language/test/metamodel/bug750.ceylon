import ceylon.language.meta.declaration{...}
import ceylon.language.meta.model{
    CallableConstructor, 
    TypeApplicationException
}
import ceylon.language.meta{
    type
}


class Bug750Init<T>(String s) {
    
}
class Bug750Ctors<T> {
    shared new(String s) {}
    shared new other(String s) {} 
}
class Bug750NoDefault<T> {
    shared new other(String s) {} 
}

"doc"
shared
throws(`class Exception`, "Always")
deprecated()
class Bug750Anno() {
    throw Exception();
}


@test
shared void bug750() {
    ClassWithInitializerDeclaration bug750Init = `class Bug750Init`;
    ClassWithConstructorsDeclaration bug750Ctors= `class Bug750Ctors`;
    ClassWithConstructorsDeclaration bug750NoDefault= `class Bug750NoDefault`;
    ClassWithInitializerDeclaration bug750Anno = `class Bug750Anno`;
    CallableConstructorDeclaration ctor = `new Bug750Ctors`;
    CallableConstructorDeclaration init = bug750Init.defaultConstructor;
    
    assert(1 == bug750Init.parameterDeclarations.size);
    assert(bug750Init.getParameterDeclaration("s") exists);
    assert(!bug750Init.getParameterDeclaration("t") exists);
    
    assert(exists pl = bug750Ctors.parameterDeclarations,
        pl.size == 1);
    assert(bug750Ctors.getParameterDeclaration("s") exists);
    assert(!bug750Ctors.getParameterDeclaration("t") exists);
    assert(exists c= bug750Ctors.defaultConstructor,
        ctor == c);
    
    assert(!bug750NoDefault.parameterDeclarations exists);
    assert(!bug750NoDefault.getParameterDeclaration("s") exists);
    assert(!bug750NoDefault.defaultConstructor exists);
    
    // now test init
    assert(1 == init.parameterDeclarations.size);
    assert(init.getParameterDeclaration("s") exists);
    assert(!init.getParameterDeclaration("t") exists);
    
    assert(!init.abstract);
    assert(init.defaultConstructor);
    assert(!init.annotation);
    assert(!init.actual);
    assert(!init.default);
    assert(!init.formal);
    assert(init.shared);
    assert(!init.toplevel);
    // annotations
    value annos = bug750Anno.constructorDeclarations();
    assert(annos == bug750Anno.annotatedConstructorDeclarations<SharedAnnotation>());
    assert(annos == bug750Anno.annotatedConstructorDeclarations<DeprecationAnnotation>());
    assert(annos == bug750Anno.annotatedConstructorDeclarations<ThrownExceptionAnnotation>());
    assert(bug750Anno.annotatedConstructorDeclarations<DocAnnotation>().empty);
    assert(bug750Anno.defaultConstructor.annotated<SharedAnnotation>());
    assert(bug750Anno.defaultConstructor.annotated<DeprecationAnnotation>());
    assert(bug750Anno.defaultConstructor.annotated<ThrownExceptionAnnotation>());
    assert(!bug750Anno.defaultConstructor.annotated<DocAnnotation>());
    
    // apply
    CallableConstructor<Bug750Init<String>,[String]> appliedCtor = init.apply<Bug750Init<String>,[String]>(`String`);
    try {
        init.memberApply<Object,Bug750Init<String>,[String]>(`Object`);
        assert(false);
    } catch (TypeApplicationException e) {}
    
    //container
    assert(bug750Init == init.container);
    
    // invoke
    assert(is Bug750Init<String> inst= init.invoke([`String`], ""));
    try {
        init.memberInvoke(init, [], "");
        assert(false);
    } catch (TypeApplicationException e) {}
    
    // type parameters
    assert(!init.typeParameterDeclarations.empty);
    
    // name
    assert("" == init.name);
    
    // qualifiedName
    assert("metamodel::Bug750Init" == init.qualifiedName);
    
    Sequence<CallableConstructorDeclaration> xxx = bug750Init.constructorDeclarations();
    assert(init in xxx);
    
    CallableConstructorDeclaration? yyy = bug750Init.getConstructorDeclaration("");
    assert(yyy exists);

    // Models
    assert(exists zzz = `Bug750Init<String>`.defaultConstructor,
        appliedCtor == zzz);
    assert([`String`] == appliedCtor.parameterTypes);
    assert(init == appliedCtor.declaration);
    assert(exists aaa = appliedCtor.container,
        `Bug750Init<String>` == aaa);
    
    assert([`Bug750Init<String>`.defaultConstructor] == `Bug750Init<String>`.getCallableConstructors<[String]>());
    assert([`Bug750Init<String>`.defaultConstructor] == `Bug750Init<String>`.getCallableConstructors<Nothing>());
    assert(`Bug750Init<String>`.getCallableConstructors<[Integer]>().empty);
    assert(`Bug750Init<String>`.getCallableConstructors<[String]>(`SharedAnnotation`).empty);
    assert(`Bug750Init<String>`.getCallableConstructors<[String]>(`DocAnnotation`).empty);
    assert(`Bug750Init<String>`.getCallableConstructors<[String]>(`DeprecationAnnotation`).empty);
    assert(!`Bug750Anno`.getCallableConstructors<[]>(`SharedAnnotation`).empty);
    assert(`Bug750Anno`.getCallableConstructors<[]>(`DocAnnotation`).empty);
    assert(!`Bug750Anno`.getCallableConstructors<[]>(`DeprecationAnnotation`).empty);
    assert(!`Bug750Anno`.getCallableConstructors<[]>(`ThrownExceptionAnnotation`).empty);
    
    type(`Bug750Init<String>`.defaultConstructor);
    
    Bug750Outer().bug750();
}

class Bug750Outer() {
    "doc"
    class Bug750Init<T>(String s) {
        
    }
    class Bug750Ctors<T> {
        shared new(String s) {}
        shared new other(String s) {} 
    }
    class Bug750NoDefault<T> {
        shared new other(String s) {} 
    }
    "doc"
    shared
    throws(`class Exception`, "Always")
    deprecated()
    class Bug750Anno() {
        throw Exception();
    }
    shared void bug750() {
        ClassWithInitializerDeclaration bug750Init = `class Bug750Init`;
        ClassWithConstructorsDeclaration bug750Ctors= `class Bug750Ctors`;
        ClassWithConstructorsDeclaration bug750NoDefault= `class Bug750NoDefault`;
        ClassWithInitializerDeclaration bug750Anno = `class Bug750Anno`;
        CallableConstructorDeclaration ctor = `new Bug750Ctors`;
        CallableConstructorDeclaration init = bug750Init.defaultConstructor;
        
        assert(1 == bug750Init.parameterDeclarations.size);
        assert(bug750Init.getParameterDeclaration("s") exists);
        assert(!bug750Init.getParameterDeclaration("t") exists);
        
        assert(exists pl = bug750Ctors.parameterDeclarations,
            pl.size == 1);
        assert(bug750Ctors.getParameterDeclaration("s") exists);
        assert(!bug750Ctors.getParameterDeclaration("t") exists);
        assert(exists c= bug750Ctors.defaultConstructor,
            ctor == c);
        
        assert(!bug750NoDefault.parameterDeclarations exists);
        assert(!bug750NoDefault.getParameterDeclaration("s") exists);
        assert(!bug750NoDefault.defaultConstructor exists);
        
        // now test init
        assert(1 == init.parameterDeclarations.size);
        assert(init.getParameterDeclaration("s") exists);
        assert(!init.getParameterDeclaration("t") exists);
        
        assert(!init.abstract);
        assert(init.defaultConstructor);
        assert(!init.annotation);
        assert(!init.actual);
        assert(!init.default);
        assert(!init.formal);
        assert(init.shared);
        assert(!init.toplevel);
        //annotations
        value annos = bug750Anno.constructorDeclarations();
        assert(annos == bug750Anno.annotatedConstructorDeclarations<SharedAnnotation>());
        assert(annos == bug750Anno.annotatedConstructorDeclarations<DeprecationAnnotation>());
        assert(annos == bug750Anno.annotatedConstructorDeclarations<ThrownExceptionAnnotation>());
        assert(bug750Anno.annotatedConstructorDeclarations<DocAnnotation>().empty);
        assert(bug750Anno.defaultConstructor.annotated<SharedAnnotation>());
        assert(bug750Anno.defaultConstructor.annotated<DeprecationAnnotation>());
        assert(bug750Anno.defaultConstructor.annotated<ThrownExceptionAnnotation>());
        assert(!bug750Anno.defaultConstructor.annotated<DocAnnotation>());
        
        // apply
        try {
            CallableConstructor<Bug750Init<String>,[String]> appliedCtor = init.apply<Bug750Init<String>,[String]>(`String`);
            assert(false);
        } catch (TypeApplicationException e) {}
        
        value appliedCtor = init.memberApply<Bug750Outer,Bug750Init<String>,[String]>(`Bug750Outer`, `String`);
        
        //container
        assert(bug750Init == init.container);
        
        // invoke
        assert(is Bug750Init<String> inst= init.memberInvoke(this, [`String`], ""));
        try {
            init.invoke([`String`], "");
            assert(false);
        } catch (TypeApplicationException e) {}
        
        // type parameters
        assert(!init.typeParameterDeclarations.empty);
        
        // name
        assert("" == init.name);
        
        // qualifiedName
        assert("metamodel::Bug750Outer.Bug750Init" == init.qualifiedName);
    
        Sequence<CallableConstructorDeclaration> xxx = bug750Init.constructorDeclarations();
        assert(init in xxx);
        
        CallableConstructorDeclaration? yyy = bug750Init.getConstructorDeclaration("");
        assert(yyy exists);
        
        assert(exists zzz = `Bug750Init<String>`.defaultConstructor,
            appliedCtor == zzz);
        assert([`String`] == appliedCtor.parameterTypes);
        assert(init == appliedCtor.declaration);
        
        assert(`Bug750Init<String>` == appliedCtor.container);
        
        assert([`Bug750Init<String>`.defaultConstructor] == `Bug750Init<String>`.getCallableConstructors<[String]>());
        assert([`Bug750Init<String>`.defaultConstructor] == `Bug750Init<String>`.getCallableConstructors<Nothing>());
        assert(`Bug750Init<String>`.getCallableConstructors<[Integer]>().empty);
        assert(`Bug750Init<String>`.getCallableConstructors<[String]>(`SharedAnnotation`).empty);
        assert(`Bug750Init<String>`.getCallableConstructors<[String]>(`DocAnnotation`).empty);
        assert(`Bug750Init<String>`.getCallableConstructors<[String]>(`DeprecationAnnotation`).empty);
        assert(!`Bug750Anno`.getCallableConstructors<[]>(`SharedAnnotation`).empty);
        assert(`Bug750Anno`.getCallableConstructors<[]>(`DocAnnotation`).empty);
        assert(!`Bug750Anno`.getCallableConstructors<[]>(`DeprecationAnnotation`).empty);
        assert(!`Bug750Anno`.getCallableConstructors<[]>(`ThrownExceptionAnnotation`).empty);
        
        type(`Bug750Init<String>`.defaultConstructor);
    }
}