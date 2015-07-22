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

shared alias ConstructorDeclaration
        => CallableConstructorDeclaration|ValueConstructorDeclaration;

