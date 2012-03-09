@nomodel
class Foo<T>() {
    shared void bar(T? t) {
    }
}
@nomodel 
void m() {
    Foo<Object> f = Foo<Object>();
    Object o = 1;
    f.bar(o);
    f.bar{t=o;};
}