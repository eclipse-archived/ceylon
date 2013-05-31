import ceylon.language.metamodel { Attribute }

shared interface AttributeDeclaration
        satisfies TopLevelOrMemberDeclaration {
    
    shared formal Attribute<Anything> apply(Anything instance = null);
    
    shared formal OpenType type;
}