import ceylon.language.meta.model{
    Type, 
    CallableConstructor, 
    ValueConstructor,
    Method, 
    Value, 
    Attribute,
    IncompatibleTypeException, 
    StorageException,
    MemberClassCallableConstructor,
    MemberClassValueConstructor
}

/*"A constructor declaration."
shared sealed interface ConstructorDeclaration
        of CallableConstructorDeclaration|ValueConstructorDeclaration 
        satisfies AnnotatedDeclaration {
    
}*/

shared alias ConstructorDeclaration
        => CallableConstructorDeclaration|ValueConstructorDeclaration;

shared sealed interface CallableConstructorDeclaration 
        satisfies FunctionalDeclaration {
    
    "True if the constructor has an [[abstract|ceylon.language::abstract]] annotation."
    shared formal Boolean abstract;
    
    "Whether this is the default constructor. The default constructor of a class is the constructor with no name."
    shared formal Boolean defaultConstructor;
    
    "The class this constructor constructs"
    shared actual formal ClassDeclaration container;
    
    shared actual formal Object invoke(Type<>[] typeArguments, Anything* arguments);
    
    shared actual formal Object memberInvoke(Object container, Type<>[] typeArguments, Anything* arguments);
    
    "Applies the given closed type arguments to the declaration of the class 
     enclosing this constructor declaration, returning a function model 
     for the constructor"
    shared actual formal CallableConstructor<Result,Arguments> apply<Result=Object,Arguments=Nothing>(Type<>* typeArguments)
            given Arguments satisfies Anything[];
    
    "Applies the given closed type arguments to the declaration of the member class 
     enclosing this constructor declaration, returning a method model 
     for the constructor"
    shared actual formal MemberClassCallableConstructor<Container,Result,Arguments> memberApply<Container=Nothing,Result=Object,Arguments=Nothing>(Type<Object> containerType, Type<>* typeArguments)
            given Arguments satisfies Anything[];
}
shared sealed interface ValueConstructorDeclaration 
        satisfies ValueDeclaration & NestableDeclaration {
    
    "The class this constructor constructs"
    shared actual formal ClassDeclaration container; 

    "Reads the current value of this toplevel value."
    shared actual default Object get()
            => apply<Object>().get();
    
    "Reads the current value of this attribute on the given container instance."
    throws(`class IncompatibleTypeException`, "If the specified container is not compatible with this attribute.")
    throws(`class StorageException`,
        "If this attribute is not stored at runtime, for example if it is neither shared nor captured.")
    shared actual default Object memberGet(Object container)
            => memberApply<Nothing, Object>(`Nothing`).bind(container).get();
    
    "Applies the given closed type arguments to the declaration of the class 
     enclosing this constructor declaration, returning a model 
     for the constructor"
    shared formal ValueConstructor<Result> constructorApply<Result=Object>();
    
    "Applies the given closed type arguments to the declaration of the member class 
     enclosing this constructor declaration, returning a model 
     for the constructor"
    shared formal MemberClassValueConstructor<Container,Result> memberConstructorApply<Container=Nothing,Result=Object>(Type<Object> containerType);
    
    /*"Sets the current value of this toplevel value."
    shared actual default void set(Nothing newValue) {
        throw;
    }
    
    "Sets the current value of this attribute on the given container instance."
    throws(`class IncompatibleTypeException`, "If the specified container or new value type is not compatible with this attribute.")
    throws(`class StorageException`,
        "If this attribute is not stored at runtime, for example if it is neither shared nor captured.")
    shared actual formal void memberSet(Object container, Nothing newValue) {
        throw;
    }*/
    
    /*shared actual formal Value<Get,Nothing> apply<Get,Set>();
    
    shared actual formal Attribute<Container,Get,Nothing> memberApply<Container,Get,Set>(Type<Object> containerType);*/
}