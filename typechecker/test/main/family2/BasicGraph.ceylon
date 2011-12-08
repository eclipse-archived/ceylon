class BasicGraph() extends Graph<BasicGraph,Node,Edge>() {
    
    shared actual class Node() 
            extends super.Node() {}
    
    shared actual class Edge(Node n1, Node n2) 
            extends super.Edge(n1, n2) {}
    
}