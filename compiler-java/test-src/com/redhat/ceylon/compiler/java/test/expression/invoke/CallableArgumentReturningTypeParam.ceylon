@nomodel
void f<T>(T foo()) given T satisfies Numeric<T> {
}
@nomodel
X bar<X>() {
    throw;
}
@nomodel
Integer baz() {
    return 1;
}
@nomodel
void m() {
    f<Integer>(bar<Integer>);
    f<Integer>(baz);
}
