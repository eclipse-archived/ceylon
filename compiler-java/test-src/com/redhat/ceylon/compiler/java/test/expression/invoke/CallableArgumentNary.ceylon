@nomodel
void f(void foo(Integer i, String s, Boolean b, Character c)) {
}
@nomodel
void bar(Integer i, String s, Boolean b, Character c) {
}
@nomodel
void m() {
    f(bar);
}
