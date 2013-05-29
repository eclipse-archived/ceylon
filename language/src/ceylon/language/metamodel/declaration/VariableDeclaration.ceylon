"Model of an attribute that is `variable` or has an `assign` block."
shared interface VariableDeclaration
        satisfies AttributeDeclaration {
    
    "Returns a model of the setter of this variable.
     
     For modelling purposes `variable` reference 
     values have a SetterDeclaration even though there is no 
     such setter explicit in the source code."
    shared formal SetterDeclaration setter;
}

