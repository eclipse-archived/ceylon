import ceylon.language.metamodel.declaration {
    AttributeDeclaration
}

shared interface Value<out Type>
        satisfies Declaration {

    shared formal actual AttributeDeclaration declaration;
    
    shared formal Type get();
    
    shared formal AppliedType type;
}
