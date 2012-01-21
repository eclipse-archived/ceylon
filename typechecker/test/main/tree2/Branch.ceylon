interface Branch<out T> satisfies Tree<T> {
    shared formal Tree<T> left;
    shared formal Tree<T> right;
}
