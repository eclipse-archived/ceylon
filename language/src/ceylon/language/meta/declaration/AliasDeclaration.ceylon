import ceylon.language.meta.model {
    Member,
    Model,
    AppliedType = Type
}

"Type alias declaration. While type aliases are erased (substituted for what they alias is a better term) from every 
 declaration that uses them during compile-time, the declaration of the type alias is still visible at run-time."
shared interface AliasDeclaration 
    satisfies NestableDeclaration & GenericDeclaration {

    "The open type that is substituted by this type alias."
    shared formal OpenType extendedType;

    /*
    FIXME: this is too shaky WRT member types, we'll figure it out later    
    shared formal AppliedType<Type> apply<Type=Anything>(AppliedType<Anything>* typeArguments);
    
    shared formal AppliedType<Type> & Member<Container, AppliedType<Type> & Model> memberApply<Container=Nothing, Type=Anything>(AppliedType<Container> containerType, AppliedType<Anything>* typeArguments);
*/
}
