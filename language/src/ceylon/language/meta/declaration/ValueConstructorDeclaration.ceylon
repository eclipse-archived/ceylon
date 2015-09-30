import ceylon.language.meta.model{
    Type, 
    CallableConstructor, 
    ValueConstructor,
    Method, 
    Value, 
    Member,
    Attribute,
    IncompatibleTypeException, 
    StorageException,
    MemberClassCallableConstructor,
    MemberClassValueConstructor
}
import ceylon.language.meta.declaration {
    CallableConstructorDeclaration
}

"""Declaration model for value constructors, for example
   
       class Currency {
           "The US Dollar"
           shared new usd {}
           // ...
       }
       
       ValueConstructorDeclaration dollars = `new Currency.usd`;
   """
see (`interface CallableConstructorDeclaration`)
shared sealed interface ValueConstructorDeclaration 
        satisfies GettableDeclaration & ConstructorDeclaration {
    
    "The class this constructor constructs"
    shared actual formal ClassDeclaration container; 
    
    shared formal ValueConstructor<Result> apply<Result=Anything>();
    
    shared formal MemberClassValueConstructor<Container, Result> memberApply
            <Container=Nothing, Result=Anything>
            (Type<Object> containerType);
    
    "Reads the current value of this toplevel value."
    shared actual default Object get()
            => apply<Object>().get();
    
    "Reads the current value of this attribute on the given container instance."
    throws(`class IncompatibleTypeException`, 
        "If the specified container is not compatible with this attribute.")
    throws(`class StorageException`,
        "If this attribute is not stored at runtime, for example if it is 
         neither shared nor captured.")
    shared actual default Object memberGet(Object container)
            => memberApply<Nothing, Object>(`Nothing`).bind(container).get();
    
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