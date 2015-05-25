
import ceylon.language.meta.declaration {
    ValueDeclaration
}
"A context representing serialization of many objects to a 
 single output stream. 
 
 The serialization library obtains an instance by calling 
 [[serialization]] and then uses
 [[references]] to traverse the instances reachable from the 
 instance(s) being serialized.
 
 It is the serialization library's responsibility to 
 manage object identity and handle cycles in the graph 
 of object references. For example a serialization library 
 that produced a hierarchical format might ignore identity 
 when an instance is encountered multiple times 
 (resulting in duplicate subtrees in the output), and 
 simply throw an exception if it encountered a cycle. 
 "
shared sealed
interface SerializationContext {
    // could be generic
    "Obtain the references of the given instance."
    shared formal References references(Anything instance);
}



