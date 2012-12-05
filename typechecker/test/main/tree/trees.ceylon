void printTree<T>(Tree<T> tree) 
        given T satisfies Object {
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

@error class Extra<T>() extends Tree<T>() {}

void testTree() {
    value tree = Branch<String> { 
        left = Leaf("hello");
        right = Leaf("world"); 
    };
    printTree(tree);
    printTree(tree.left);
    printTree(tree.right);
}
