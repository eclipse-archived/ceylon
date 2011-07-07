abstract class Graph<G,N,E>() of G
    given N satisfies Node
    given E satisfies Edge 
    given G satisfies Graph<G,N,E> {
    
    shared formal class Edge(N n1, N n2) of E {
        shared N n1 = n1;
        shared N n2 = n2;
    }

    shared formal class Node() of N {
        shared default Boolean touches(E edge) {
            return edge.n1 == this || 
                   edge.n2 == this;
        }
    }

}