
"A means via which one instance can refer to another."
shared interface ReachableReference/*<Instance>*/ // Reachable
        of Member|Element|Outer {
    "The [[referred]] instance reachable from the given [[instance]]."
    shared formal Anything referred(Object/*<Instance>*/ instance);
}


