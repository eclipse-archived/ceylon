class OnOffGraph() extends Graph<Node,Edge>() {
    
    shared actual class Node() 
            extends super.Node() {
        shared actual Boolean touches(Edge edge) {
            return edge.enabled && super.touches(edge);
        }
    }
    
    shared abstract class OnOffEdge(Node n1, Node n2, Boolean initiallyEnabled) 
            extends super.Edge(n1, n2) {
        shared variable Boolean enabled = initiallyEnabled;
    }
            
    shared actual class Edge(Node n1, Node n2) 
            extends /*super.Edge(n1, n2)*/OnOffEdge(n1, n2, true) {}

}