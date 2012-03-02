@nomodel
void f(void foo(Integer i, String s)) {
}
@nomodel
void bar(Integer i, String s) {
}
@nomodel
void m() {
    f(bar);
}
