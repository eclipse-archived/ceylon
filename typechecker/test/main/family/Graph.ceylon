"A type family representing pairs
 of Edge and Node types."
by ("Gavin")
abstract class Graph<N,E>() 
    given N satisfies Node
    given E satisfies Edge {
    
    "An Edge joins two Nodes. This is an 
     abstract type with a concrete
     implementation for each implementation
     of the family"
    shared formal class Edge(n1, n2) of E {
        shared N n1;
        shared N n2;
        shared Boolean touches(N node) =>
                n1==node || n2==node;
    }
    
    "A Node. This is an abstract type with
     a concrete implementation for each 
     implementation of the family"
    shared formal class Node() of N {
        shared default Boolean touches(E edge) =>
                edge.touches(this of N);
    }

}