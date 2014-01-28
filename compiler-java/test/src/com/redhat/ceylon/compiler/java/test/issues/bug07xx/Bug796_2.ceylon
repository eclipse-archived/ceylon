/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
void bug796() {
    value bg = Bug796_BasicGraph();
    value bn1 = bg.Node();
    value bn2 = bg.Node();
    value be = bg.Edge(bn1,bn2);
    bn1.touches(be);
    
    value og = Bug796_OnOffGraph();
    value on1 = og.Node();
    value on2 = og.Node();
    value oe = og.Edge(on1,on2);
    on1.touches(oe);

    Boolean method<Node,Edge>(Node n, Edge e) 
            given Node satisfies Bug796_Graph<Node,Edge>.Node 
            given Edge satisfies Bug796_Graph<Node,Edge>.Edge {
        return n.touches(e);
    }

    Boolean b = method(bn1,be);

    Edge method2<Node,Edge>(Bug796_Graph<Node, Edge> g) 
            given Node satisfies Bug796_Graph<Node,Edge>.Node 
            given Edge satisfies Bug796_Graph<Node,Edge>.Edge {
        Node n1 = g.Node() of Node;
        Node n2 = g.Node() of Node;
        return g.Edge(n1,n2) of Edge;
    }

    Bug796_OnOffGraph.Edge e2 = method2(Bug796_OnOffGraph());

    Bug796_Graph<Bug796_BasicGraph.Node, Bug796_BasicGraph.Edge>.Node nn = Bug796_BasicGraph().Node();
    Bug796_BasicGraph.Node nnn1 = nn of Bug796_BasicGraph.Node;
    Bug796_Graph<Bug796_BasicGraph.Node, Bug796_BasicGraph.Edge>.Node nn2 = nnn1;
    print(nnn1);

    Bug796_Graph<Bug796_BasicGraph.Node, Bug796_BasicGraph.Edge> gbg = Bug796_BasicGraph();
    Bug796_BasicGraph.Node bgn = gbg.Node() of Bug796_BasicGraph.Node;
    Bug796_BasicGraph.Edge bge = gbg.Edge(bgn,bgn) of Bug796_BasicGraph.Edge;
    print(bgn);
    print(bge);
}