abstract class Graph<N,E>() 
    given N satisfies Node
    given E satisfies Edge {
    
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