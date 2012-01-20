void printTree<T>(Tree<T> tree) 
        given T satisfies Object {
    switch (tree)
    case (is Branch<T>) {
        printTree(tree.left);
        printTree(tree.right);
        @type["Branch<T>"] value v = tree;
    }
    case (is Leaf<T>) {
        print(tree.val);
        @type["Leaf<T>"] value v = tree;
    }
}

class ConcreteBranch(Tree<String> left, Tree<String> right) 
        satisfies Branch<String> {
    shared actual Tree<String> left = left;
    shared actual Tree<String> right = right;
}

class ConcreteLeaf(String s)
        satisfies Leaf<String> {
    shared actual String val { return s; }
}

@error 
class BrokenLeafBranch(Tree<String> left, Tree<String> right, String s) 
        satisfies Branch<String> & Leaf<String> {
    shared actual Tree<String> left = left;
    shared actual Tree<String> right = right;
    shared actual String val { return s; }    
}

void testTree() {
    ConcreteBranch tree { 
        left = ConcreteLeaf("hello");
        right = ConcreteLeaf("world"); 
    }
    printTree(tree);
    printTree(tree.left);
    printTree(tree.right);
}
