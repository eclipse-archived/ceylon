@nomodel
void f(Integer foo()) {
}
@nomodel
Integer bar() {
    return 1;
}
@nomodel
void m() {
    f(bar);
}
