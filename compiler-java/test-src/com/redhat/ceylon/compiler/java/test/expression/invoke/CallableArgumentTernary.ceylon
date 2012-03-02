@nomodel
void f(void foo(Integer i, String s, Boolean c)) {
}
@nomodel
void bar(Integer i, String s, Boolean b) {
}
@nomodel
void m() {
    f(bar);
}
