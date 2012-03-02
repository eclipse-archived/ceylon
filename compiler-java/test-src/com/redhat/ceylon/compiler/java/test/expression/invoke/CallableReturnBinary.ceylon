@nomodel
void foo(Integer i, String s) {
}
@nomodel
Callable<Void, Integer, String> bar() {
    return foo;
}
