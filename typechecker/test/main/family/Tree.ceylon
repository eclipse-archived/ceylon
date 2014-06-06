abstract class Tree<ActualNode, ActualEdge>() 
        extends Graph<ActualNode, ActualEdge>()
        given ActualNode satisfies Node
        given ActualEdge satisfies Edge {

    shared actual formal class Node() 
            of ActualNode 
            extends super.Node(){
        //tree specific stuff...
    }

    shared actual formal class Edge(ActualNode start, ActualNode end) 
            of ActualEdge 
            extends super.Edge(start, end){
    }
}