import ceylon.language.model {
    Annotated
}

"Model of the setter of a `VariableDeclaration`."
shared interface SetterDeclaration
        satisfies Annotated {

    "The variable this setter is for."
    shared formal VariableDeclaration variable;
    
}
