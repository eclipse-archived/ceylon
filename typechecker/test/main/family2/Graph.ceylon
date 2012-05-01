abstract class Graph<G,N,E>() of G
    given N satisfies Node
    given E satisfies Edge 
    given G satisfies Graph<G,N,E> {
    
    shared formal class Edge(n1, n2) of E {
        shared N n1;
        shared N n2;
        shared Boolean touches(N node) {
            return n1==node || n2==node;
        }
    }

    shared formal class Node() of N {
        shared default Boolean touches(E edge) {
            return edge.touches(this);
        }
    }

}