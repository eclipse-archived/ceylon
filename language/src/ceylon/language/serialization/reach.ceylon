"A native way to find out about the references an instance holds
 and to get an instance."
native object reach {
    shared native Iterator<ReachableReference> references(Anything instance);
    shared native Anything get(Anything instance, ReachableReference ref);
}