import ceylon.language.metamodel { Attribute }

shared interface AttributeDeclaration
        satisfies Declaration {
    
    shared formal Attribute<Anything> apply(Anything instance = null);
    
    shared formal OpenType type;
}