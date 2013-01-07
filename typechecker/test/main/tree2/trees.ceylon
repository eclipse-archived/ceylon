void printTree<T>(Tree<T> tree) 
        given T satisfies Value {
    switch (tree)
    case (is Branch<T>) {
        printTree(tree.left);
        printTree(tree.right);
        @type:"Branch<T>" value v = tree;
    }
    case (is Leaf<T>) {
        print(tree.val);
        @type:"Leaf<T>" value v = tree;
    }
}

class ConcreteBranch(left, right) 
        satisfies Branch<String> {
    shared actual Tree<String> left;
    shared actual Tree<String> right;
}

@error
class BrokenBranch(left, right) 
        satisfies AbstractBranch<String> {
    shared actual Tree<String> left;
    shared actual Tree<String> right;
}

class ConcreteLeaf(String s)
        satisfies Leaf<String> {
    shared actual String val { return s; }
}

@error
class BrokenLeafBranch(left, right, String s) 
        satisfies Branch<String> & Leaf<String> {
    shared actual Tree<String> left;
    shared actual Tree<String> right;
    shared actual String val { return s; }    
}

@error
class BrokenTree() 
        satisfies Tree<String> {}

void testTree() {
    value tree = ConcreteBranch { 
        left = ConcreteLeaf("hello");
        right = ConcreteLeaf("world"); 
    };
    printTree(tree);
    printTree(tree.left);
    printTree(tree.right);
}
