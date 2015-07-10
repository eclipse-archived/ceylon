


"A member instance referring to its outer instance."
shared sealed interface Outer /*<Instance>*/
        satisfies ReachableReference/*<Instance>*/ {
    "The outer instance of the given member [[instance]]."
    shared actual formal Object referred(/*<Instance>*/Object instance);
}