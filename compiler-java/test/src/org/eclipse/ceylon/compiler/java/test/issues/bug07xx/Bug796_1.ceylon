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
abstract class Bug796_Graph<N,E>() 
    given N satisfies Node
    given E satisfies Edge {

    "An Edge joins two Nodes. This is an 
     abstract type with a concrete
     implementation for each implementation
     of the family"
    shared formal class Edge(n1, n2) of E {
        shared N n1;
        shared N n2;
        shared Boolean touches(N node) {
            return n1==node || n2==node;
        }
    }

    "A Node. This is an abstract type with
     a concrete implementation for each 
     implementation of the family"
    shared formal class Node() of N {
        shared default Boolean touches(E edge) {
            return edge.touches(this of N);
        }
    }

}

class Bug796_BasicGraph() extends Bug796_Graph<Node,Edge>() {

    shared actual class Node() 
            extends super.Node() {}

    shared actual class Edge(Node n1, Node n2) 
            extends super.Edge(n1, n2) {}

}

class Bug796_OnOffGraph() extends Bug796_Graph<Node,Edge>() {

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
