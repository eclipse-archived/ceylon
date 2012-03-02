@nomodel
class C() {
}
@nomodel
void f(void foo(C c)) {
}
@nomodel
void bar(C c) {
}
@nomodel
void m() {
    f(bar);
}