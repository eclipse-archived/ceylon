class Branch<T>(Tree<T> left, Tree<T> right) 
        extends Tree<T>() {
    shared Tree<T> left = left;
    shared Tree<T> right = right;
}
