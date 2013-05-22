import ceylon.language.metamodel {
    Annotated
}

"Model of the setter of a `Variable`."
shared interface Setter 
        satisfies Annotated {

    "The variable this setter is for."
    shared formal Variable variable;
    
}
