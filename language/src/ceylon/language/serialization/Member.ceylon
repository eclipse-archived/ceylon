import ceylon.language.meta.declaration {
    ValueDeclaration
}

"An instance referring to another instance via a reference attribute."
shared sealed interface Member // or Reference
        satisfies ReachableReference {
    "The attribute making the reference."
    shared formal ValueDeclaration attribute;
    
    "The [[referred]] instance reachable from the given [[instance]].
     
     Note: If this member refers to a `late` declaration and the 
     attribute of the given instance has not been initialized this 
     method will return [[uninitializedLateValue]]."
    shared actual formal Anything referred(Object/*<Instance>*/ instance);
    
}

"The type of [[uninitializedLateValue]]."
shared abstract class UninitializedLateValue() of uninitializedLateValue {}

"A singleton used to indicate that a `late` [[Member]] of a particular 
 instance has not been initialized."
shared object uninitializedLateValue extends UninitializedLateValue() {}

