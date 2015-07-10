import ceylon.language.serialization{RealizableReference}

"A native way to find out about the references an instance holds
 and to get an instance."
shared native object reach {
    shared native Iterator<ReachableReference> references(Anything instance);
    shared native Anything getAnything(Anything instance, ReachableReference ref);
    shared native Object getObject(Anything instance, ReachableReference ref);
}