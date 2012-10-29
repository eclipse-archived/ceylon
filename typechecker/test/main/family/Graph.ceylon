doc "A type family representing pairs
     of Edge and Node types."
by "Gavin King"
abstract class Graph<N,E>() 
    given N satisfies Node
    given E satisfies Edge {
    
    doc "An Edge joins two Nodes. This is an 
         abstract type with a concrete
         implementation for each implementation
         of the family"
    shared formal class Edge(n1, n2) of E {
        shared N n1;
        shared N n2;
        shared Boolean touches(N node) {
            return n1==node || n2==node;
        }
    }
    
    doc "A Node. This is an abstract type with
         a concrete implementation for each 
         implementation of the family"
    shared formal class Node() of N {
        shared default Boolean touches(E edge) {
            return edge.touches(this);
        }
    }

}