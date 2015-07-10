import ceylon.language.meta.declaration {
    ValueDeclaration
}

shared abstract class UninitializedLateValue() of uninitializedLateValue {}
shared object uninitializedLateValue extends UninitializedLateValue() {}

"An instance referring to another instance via a reference attribute."
shared sealed interface Member // or Reference
        satisfies ReachableReference {
    "The attribute making the reference."
    shared formal ValueDeclaration attribute;
    "The [[referred]] instance reachable from the given [[instance]]."
    shared actual formal Anything|UninitializedLateValue referred(Object/*<Instance>*/ instance);
    
}