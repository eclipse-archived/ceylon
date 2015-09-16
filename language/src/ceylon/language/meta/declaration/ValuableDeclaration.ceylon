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
 
   * [[ValueDeclaration]]s which abstracts over values and attributes 
   * [[ValueConstructorDeclaration]]s which represents value constructors
"""
shared sealed interface ValueableDeclaration {
    "Applies this value declaration in order to obtain a value model. 
     See [this code sample](#toplevel-sample) for an example on how to use this."
    throws(`class IncompatibleTypeException`, "If the specified `Get` or `Set` type arguments are not compatible with the actual result.")
    shared formal ValueModel<Get, Set>&Gettable<Get> apply<Get=Anything, Set=Nothing>();
    
    "Applies the given closed container type to this attribute declaration in order to obtain an attribute model. 
     See [this code sample](#member-sample) for an example on how to use this."
    throws(`class IncompatibleTypeException`, "If the specified `Container`, `Get` or `Set` type arguments are not compatible with the actual result.")
    shared formal ValueModel<Get, Set>&Qualified<ValueModel<Get,Set>, Container>  memberApply<Container=Nothing, Get=Anything, Set=Nothing>(AppliedType<Object> containerType);
    
    "Reads the current value of this toplevel value."
    shared default Anything get()
            => apply<Anything, Nothing>().get();
    
    "Reads the current value of this attribute on the given container instance."
    throws(`class IncompatibleTypeException`, "If the specified container is not compatible with this attribute.")
    throws(`class StorageException`,
        "If this attribute is not stored at runtime, for example if it is neither shared nor captured.")
    shared formal Anything memberGet(Object container);
            //=> memberApply<Nothing, Anything, Nothing>(`Nothing`).bind(container).get();

}