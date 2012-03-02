@nomodel
void f(void foo(Integer i)) {
}
@nomodel
void bar(Integer i) {
}
@nomodel
void m() {
    f(bar);
}
