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


shared sealed interface ValueConstructorDeclaration 
        satisfies ValueableDeclaration & ConstructorDeclaration {
    
    "The class this constructor constructs"
    shared actual formal ClassDeclaration container; 
    
    shared actual formal ValueConstructor<Result,Set> apply<Result=Anything, Set=Nothing>();
    
    shared actual formal MemberClassValueConstructor<Container, Result, Set> memberApply
            <Container=Nothing, Result=Anything, Set=Nothing>
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