
"A setter declaration for a variable `ValueDeclaration`."
shared interface SetterDeclaration
        satisfies Annotated {

    "The variable value this setter is for."
    shared formal ValueDeclaration variable;
    
}
