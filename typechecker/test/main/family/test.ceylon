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
        value n1 = g.Node();
        value n2 = g.Node();
        switch (n1) case (is Node) {
            switch (n2) case (is Node) {
                value e = g.Edge(n1,n2);
                switch (e) case (is Edge) {
                    return e;
                }
            }
        }
    }
    
    OnOffGraph.Edge e2 = method2(OnOffGraph());

    Edge method3<ActualGraph, Node, Edge>(ActualGraph g) 
            given ActualGraph satisfies Graph<Node,Edge> 
            given Node satisfies ActualGraph.Node 
            given Edge satisfies ActualGraph.Edge {
        value n1 = g.Node();
        value n2 = g.Node();
        switch (n1) case (is Node) {
            switch (n2) case (is Node) {
                value e = g.Edge(n1,n2);
                switch (e) case (is Edge) {
                    return e;
                }
            }
        }
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
    switch (nn) case (is BasicGraph.Node) {
        BasicGraph.Node nnn1 = nn;
        switch (nnn1) case (is Graph<BasicGraph.Node, BasicGraph.Edge>.Node) {
            Graph<BasicGraph.Node, BasicGraph.Edge>.Node nn2 = nnn1;
        }
        @error switch (nnn1) case (is Graph<OnOffGraph.Node, OnOffGraph.Edge>.Node) {
            Graph<OnOffGraph.Node, OnOffGraph.Edge>.Node nn3 = nnn1;
        }
    }
    @error switch (nn) case (is OnOffGraph.Node) {
        OnOffGraph.Node nnn2 = nn;
    }
    
    Graph<OnOffGraph.Node, OnOffGraph.Edge>.Edge ee = OnOffGraph().Edge(on1, on2);
    @error switch (ee) case (is BasicGraph.Edge) {
        BasicGraph.Edge eee1 = ee;
    }
    switch (ee) case (is OnOffGraph.Edge) {
        OnOffGraph.Edge eee2 = ee;
        switch (eee2) case (is Graph<OnOffGraph.Node, OnOffGraph.Edge>.Edge) {
            Graph<OnOffGraph.Node, OnOffGraph.Edge>.Edge ee3 = eee2;
        }
        @error switch (eee2) case (is Graph<BasicGraph.Node, BasicGraph.Edge>.Edge) {
            Graph<BasicGraph.Node, BasicGraph.Edge>.Edge ee2 = eee2;
        }
    }
    
    Graph<BasicGraph.Node, BasicGraph.Edge> gbg = BasicGraph();
    value gbgn = gbg.Node();
    switch (gbgn) case (is BasicGraph.Node) {
        BasicGraph.Node bgn = gbgn;
        value gbge = gbg.Edge(bgn,bgn);
        switch (gbge) case (is BasicGraph.Edge) {
            BasicGraph.Edge bge = gbge;
        }
    }
}