import ceylon.language.meta.model {
    Gettable,
    Member,
    Qualified,
    ValueModel,  
    AppliedType = Type, 
    IncompatibleTypeException, 
    StorageException 
}

"""Abstraction over declarations from which a value can be obtained, namely
 
   * [[ValueDeclaration]] which abstracts over values and attributes 
   * [[ValueConstructorDeclaration]] which represents value constructors
"""
shared sealed interface GettableDeclaration {
    
    /*"Applies this value declaration in order to obtain a value model. 
     See [this code sample](#toplevel-sample) for an example on how to use this."
    throws(`class IncompatibleTypeException`, "If the specified `Get` or `Set` type arguments are not compatible with the actual result.")
    shared formal ValueModel<Get>&Gettable<Get> gettableApply<Get=Anything>();
    
    "Applies the given closed container type to this attribute declaration in order to obtain an attribute model. 
     See [this code sample](#member-sample) for an example on how to use this."
    throws(`class IncompatibleTypeException`, "If the specified `Container`, `Get` or `Set` type arguments are not compatible with the actual result.")
    shared formal ValueModel<Get>&Qualified<ValueModel<Get>, Container> memberGettableApply<Container=Nothing, Get=Anything>(AppliedType<Object> containerType);*/
    
    "Reads the current value of this toplevel value."
    shared formal Anything get();
    
    "Reads the current value of this attribute on the given container instance."
    throws(`class IncompatibleTypeException`, "If the specified container is not compatible with this attribute.")
    throws(`class StorageException`,
        "If this attribute is not stored at runtime, for example if it is neither shared nor captured.")
    shared formal Anything memberGet(Object container);

}