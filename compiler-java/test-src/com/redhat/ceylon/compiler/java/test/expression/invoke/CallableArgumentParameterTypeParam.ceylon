@nomodel
void f<T>(void foo(T t)) {
}
@nomodel
void bar<X>(X s) {
}
@nomodel
void baz(Integer i) {
}
@nomodel
void m() {
    f<String>(bar<String>);
    f<Integer>(baz);
}