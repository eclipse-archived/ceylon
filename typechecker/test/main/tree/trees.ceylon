void printTree<T>(Tree<T> tree) 
        given T satisfies Object {
    switch (tree)
    case (is Branch<T>) {
        printTree(tree.left);
        printTree(tree.right);
    }
    case (is Leaf<T>) {
        print(tree.val);
    }
}

void testTree() {
    Tree<String> tree = Branch { left = Leaf("hello");
                                 right = Leaf("world"); };
    printTree(tree);
}
