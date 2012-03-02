@nomodel
void foo(Integer i, String s, Boolean b, Character c) {
}
@nomodel
Callable<Void, Integer, String, Boolean, Character> bar() {
    return foo;
}
