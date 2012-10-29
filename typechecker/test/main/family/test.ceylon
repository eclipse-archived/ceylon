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
    
    OnOffGraph.Edge e2 = method2(OnOffGraph());

    Edge method3<ActualGraph, Node, Edge>(ActualGraph g) 
            given ActualGraph satisfies Graph<Node,Edge> 
            given Node satisfies ActualGraph.Node 
            given Edge satisfies ActualGraph.Edge {
    	Node n1 = g.Node();
    	Node n2 = g.Node();
        return g.Edge(n1,n2);
    }

    OnOffGraph.Edge e3 = method3(OnOffGraph());

    //it would be nice to make this work, if only we
    //could resolve the circularity in this constraint:
    //"given ActualGraph satisfies Graph<ActualGraph.Node,ActualGraph.Edge>"
    /*ActualGraph.Edge method4<ActualGraph>(ActualGraph g) 
            given ActualGraph satisfies Graph<ActualGraph.Node,ActualGraph.Edge> {
        ActualGraph.Node n1 = g.Node();
        ActualGraph.Node n2 = g.Node();
        return g.Edge(n1,n2);
    }*/

    Graph<BasicGraph.Node, BasicGraph.Edge>.Node nn = BasicGraph().Node();
    BasicGraph.Node nnn1 = nn;
    @error OnOffGraph.Node nnn2 = nn;
    @error Graph<OnOffGraph.Node, OnOffGraph.Edge>.Node nn3 = nnn1;
    Graph<BasicGraph.Node, BasicGraph.Edge>.Node nn2 = nnn1;
    
    Graph<OnOffGraph.Node, OnOffGraph.Edge>.Edge ee = OnOffGraph().Edge(on1, on2);
    @error BasicGraph.Edge eee1 = ee;
    OnOffGraph.Edge eee2 = ee;
    Graph<OnOffGraph.Node, OnOffGraph.Edge>.Edge ee3 = eee2;
    @error Graph<BasicGraph.Node, BasicGraph.Edge>.Edge ee2 = eee2;
    
    Graph<BasicGraph.Node, BasicGraph.Edge> gbg = BasicGraph();
    BasicGraph.Node bgn = gbg.Node();
    BasicGraph.Edge bge = gbg.Edge(bgn,bgn);
}