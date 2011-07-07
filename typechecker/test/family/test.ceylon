void testGraph() {
    value bg = BasicGraph();
    value bn1 = bg.Node();
    value bn2 = bg.Node();
    value be = bg.Edge(bn1,bn2);
    bn1.touches(be);
    value og = OnOffGraph();
    value on1 = og.Node();
    value on2 = og.Node();
    value oe = og.Edge(on1,on2);
    on1.touches(oe);
    
    @error bn1.touches(oe);
    @error on1.touches(be);
    @error bg.Edge(on1,on2);
    @error og.Edge(bn1,bn2);
    
    Boolean method<Node,Edge>(Node n, Edge e) 
            given Node satisfies Graph<Node,Edge>.Node 
            given Edge satisfies Graph<Node,Edge>.Edge {
        return n.touches(e);
    }
    
    Boolean b = method(bn1,be);
    @error method(bn1,oe);
    
    Edge method2<Node,Edge>(Graph<Node, Edge> g) 
            given Node satisfies Graph<Node,Edge>.Node 
            given Edge satisfies Graph<Node,Edge>.Edge {
        Node n1 = g.Node();
        Node n2 = g.Node();
        return g.Edge(n1,n2);
    }
    
    //TODO: get rid of explicit type args once we 
    //      improve the type arg inference algorithm
    OnOffGraph.Edge e = method2<OnOffGraph.Node,OnOffGraph.Edge>(OnOffGraph());
}