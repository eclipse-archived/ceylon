import ceylon.language.meta.declaration {
    ValueDeclaration
}
"A means via which one instance can refer to another."
shared interface ReachableReference/*<Instance>*/ // Reachable
        of Member|Element|Outer {
    "The [[referred]] instance reachable from the given [[instance]]."
    shared formal Anything referred(Object/*<Instance>*/ instance);
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
"An [[Array]] instance referring to another instance via one 
 of its elements."
shared sealed interface Element /*<Instance>*/
        satisfies ReachableReference /*<Instance>*/{
    "The index of the element in the Array which makes the reference."
    shared formal Integer index;
}
"A member instance referring to its outer instance."
shared sealed interface Outer /*<Instance>*/
        satisfies ReachableReference/*<Instance>*/ {
    "The outer instance of the given member [[instance]]."
    shared actual formal Object referred(/*<Instance>*/Object instance);
}

