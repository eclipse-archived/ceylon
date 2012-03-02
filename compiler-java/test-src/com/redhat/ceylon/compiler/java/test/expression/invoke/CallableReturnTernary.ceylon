@nomodel
void foo(Integer i, String s, Boolean b) {
}
@nomodel
Callable<Void, Integer, String, Boolean> bar() {
    return foo;
}
