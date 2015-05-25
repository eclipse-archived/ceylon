"Exposes the instances directly reachable from a given instance."
shared sealed interface References/*<Instance>*/
    // could be generic 
        satisfies {<ReachableReference/*<Instance>*/->Anything>*} {
    "The instance"
    shared formal Object/*<Instance>*/ instance;
    "The references that are reachable from the [[instance]]."
    shared formal Iterable<ReachableReference/*<Instance>*/> references;
    // TODO need to decide whether an uninitialized late attribute should be 
    // included (with Member.referred returning some special unit type, or 
    // maybe another case of ReachableReference UninitializedLateReference)
    // or excluded (which reduces cachability).
}