@nomodel
class C() {
}
@nomodel
void f(C foo()) {
}
@nomodel
void m() {
    f(C);
}
