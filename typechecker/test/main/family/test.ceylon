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
    
    Edge createMethod<Node,Edge>(Graph<Node,Edge> g, Node n1, Node n2) 
            given Node satisfies Graph<Node,Edge>.Node 
            given Edge satisfies Graph<Node,Edge>.Edge {
        return g.Edge(n1,n2) of Edge;
    }
    
    Boolean b = method(bn1,be);
    @error method(bn1,oe);
    
    alias BasicEdge => BasicGraph.Edge;
    BasicEdge nbe = createMethod(bg,bn1,bn2);
    
    Edge method2<Node,Edge>(Graph<Node, Edge> g) 
            given Node satisfies Graph<Node,Edge>.Node 
            given Edge satisfies Graph<Node,Edge>.Edge {
        Node n1 = g.Node() of Node;
        Node n2 = g.Node() of Node;
        return g.Edge(n1,n2) of Edge;
    }
    
    OnOffGraph.Edge e2 = method2(OnOffGraph());

    Edge method3<ActualGraph, Node, Edge>(ActualGraph g) 
            given ActualGraph satisfies Graph<Node,Edge> 
            @error given Node satisfies ActualGraph.Node 
            @error given Edge satisfies ActualGraph.Edge {
    	Node n1 = g.Node() of Node;
    	Node n2 = g.Node() of Node;
        return g.Edge(n1,n2) of Edge;
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
    BasicGraph.Node nnn1 = nn of BasicGraph.Node;
    @error OnOffGraph.Node nnn2 = nn of OnOffGraph.Node;
    @error Graph<OnOffGraph.Node, OnOffGraph.Edge>.Node nn3 = nnn1 of Graph<OnOffGraph.Node, OnOffGraph.Edge>;
    Graph<BasicGraph.Node, BasicGraph.Edge>.Node nn2 = nnn1 of Graph<BasicGraph.Node, BasicGraph.Edge>.Node;
    
    Graph<OnOffGraph.Node, OnOffGraph.Edge>.Edge ee = OnOffGraph().Edge(on1, on2);
    @error BasicGraph.Edge eee1 = ee of BasicGraph.Edge;
    OnOffGraph.Edge eee2 = ee of OnOffGraph.Edge;
    Graph<OnOffGraph.Node, OnOffGraph.Edge>.Edge ee3 = eee2;
    @error Graph<BasicGraph.Node, BasicGraph.Edge>.Edge ee2 = eee2;
    
    Graph<BasicGraph.Node, BasicGraph.Edge> gbg = BasicGraph();
    BasicGraph.Node bgn = gbg.Node() of BasicGraph.Node;
    BasicGraph.Edge bge = gbg.Edge(bgn,bgn) of BasicGraph.Edge;
}