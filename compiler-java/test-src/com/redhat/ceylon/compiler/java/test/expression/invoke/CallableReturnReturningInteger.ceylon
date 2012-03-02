@nomodel
Integer foo() {
    return 1;
}
@nomodel
Callable<Integer> bar() {
    return foo;
}