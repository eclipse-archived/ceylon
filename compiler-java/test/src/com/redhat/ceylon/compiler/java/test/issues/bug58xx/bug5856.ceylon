@noanno
class Bug5856() {
    shared abstract class Graph< ActualNode, ActualEdge>()
            given ActualNode satisfies Node
            given ActualEdge satisfies Edge
    {
        shared formal class Edge(ActualNode node1, ActualNode node2) of ActualEdge {
            shared default Boolean touches(ActualNode node) {
                return node == node1 || node == node2;
            }
        }
        
        shared formal class Node() of ActualNode {
            shared default Boolean touches(ActualEdge edge) {
                return edge.touches(this of ActualNode);
            }
        }
    }
    
    class BasicGraph<T>() extends Graph<Node, Edge>()
    {
        shared actual class Edge(Node node1, Node node2)
                extends super.Edge(node1, node2)
        {}
        
        shared actual class Node()
                extends super.Node()
        {}
    }
}