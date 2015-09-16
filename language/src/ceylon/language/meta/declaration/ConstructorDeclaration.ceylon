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

"""Abstraction over [[callable constructors|CallableConstructorDeclaration]]
   and [[value constructors|ValueConstructorDeclaration]]
"""
shared interface ConstructorDeclaration
        of CallableConstructorDeclaration|ValueConstructorDeclaration 
        satisfies NestableDeclaration {
    
}

