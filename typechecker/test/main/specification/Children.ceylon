class List<Element>() {
    shared void add(Element elem) {}
}

class Graph1() {
    List<Node> nodes = List<Node>();
    class Node() {
        @error nodes.add(this);    //compiler error (this reference in initializer)
    }
}

class Graph7() {
    List<Node> nodes;
    Node createNode() {
        @error Node node = Node(); //compiler error (forward reference in initializer)
        @error nodes.add(node);    //compiler error (not definitely specified)
        return node;
    }
    nodes = List<Node>();
    class Node() {}
}

class Graph5() {
    void do(Anything exp) {}
    List<Node> nodes = List<Node>();
    class Node() {
        @error do(nodes.add(this));    //compiler error (this reference in initializer)
    }
}

class Graph2() {
    class Node() {}
    Node createNode() {
        Node node = Node();
        @error nodes.add(node);    //compiler error (forward reference in initializer)
        return node;
    }
    List<Node> nodes = List<Node>();
}

class Graph6() {
    void do(Anything exp) {}
    class Node() {}
    Node createNode() {
        Node node = Node();
        @error do(nodes.add(node));    //compiler error (forward reference in initializer)
        return node;
    }
    List<Node> nodes = List<Node>();
}

class Graph4() {
    class Node() {}
    Node createNode() {
        Node node = Node();
        @error this.nodes.add(node);    //compiler error (forward reference in initializer)
        return node;
    }
    List<Node> nodes = List<Node>();
}

class Graph3() {
    List<Node> nodes = List<Node>();
    Node createNode() {
        Node node = Node();
        nodes.add(node);
        return node;
    }
    class Node() {}
}

class SuperGraph() {
    shared default class Node() {}
    shared List<Node> nodes = List<Node>();
}

class Graph8() extends SuperGraph() {
    Node createNode() {
        Node node = Node();
        nodes.add(node);
        return node;
    }
}

class Graph9() extends SuperGraph() {
    Node createNode() {
        @error Node node = Node();
        @error nodes.add(node);
        return node;
    }
    createNode();
}

class Graph10() extends SuperGraph() {
    shared actual class Node() 
            extends super.Node() {}
    Node createNode() {
        Node node = Node();
        nodes.add(node);
        return node;
    }
}

class Graph11() extends SuperGraph() {
    @error shared actual class Node() 
            extends super.Node() {}
    Node createNode() {
        Node node = Node();
        @error nodes.add(node);
        return node;
    }
    createNode();
}

