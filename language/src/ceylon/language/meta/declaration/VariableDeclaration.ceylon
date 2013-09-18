import ceylon.language.meta.model { Variable, VariableAttribute, AppliedType = Type }

"Declaration of an attribute that is `variable` or has an `assign` block."
shared interface VariableDeclaration
        satisfies ValueDeclaration {
    
    "True."
    shared actual Boolean variable => true;

    see(`function ValueDeclaration.apply`)
    shared actual formal Variable<Type> apply<Type=Anything>();

    see(`function ValueDeclaration.memberApply`)
    shared actual formal VariableAttribute<Container, Type> memberApply<Container=Nothing, Type=Anything>(AppliedType<Container> containerType);

    "Returns the setter declaration for this variable.
     
     For modelling purposes `variable` reference 
     values have a SetterDeclaration even though there is no 
     such setter explicit in the source code."
    shared formal SetterDeclaration setter;
}

