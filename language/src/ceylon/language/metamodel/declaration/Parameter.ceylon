import ceylon.language.metamodel{Annotated}

shared interface Parameter 
        satisfies Annotated {
    
    shared formal String name;
    
    shared formal OpenType type;
}