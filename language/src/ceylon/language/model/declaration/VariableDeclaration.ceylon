import ceylon.language.model { Variable, VariableAttribute, AppliedType = Type }

"Model of an attribute that is `variable` or has an `assign` block."
shared interface VariableDeclaration
        satisfies ValueDeclaration {
    
    shared actual formal Variable<Type> apply<Type=Anything>();

    shared actual formal VariableAttribute<Container, Type> memberApply<Container=Nothing, Type=Anything>(AppliedType<Container> containerType);

    "Returns a model of the setter of this variable.
     
     For modelling purposes `variable` reference 
     values have a SetterDeclaration even though there is no 
     such setter explicit in the source code."
    shared formal SetterDeclaration setter;
}

