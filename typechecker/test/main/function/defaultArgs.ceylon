class Z1() extends Y1() {
    shared actual default void foo(String str, Integer int) {}
}
class Y1() extends X1() {
    shared actual default void foo(String str, Integer int) {}
}
class X1() {
    shared default void foo(String str = "hello", Integer int=0) {}
}

void stuff() {
    Z1().foo();
    Anything(String=, Integer=) callable = Z1().foo;
}