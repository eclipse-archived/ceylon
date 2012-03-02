@nomodel
void f<T>(T t) given T(String s){
}
@nomodel
class C(String s){
}
@nomodel
void m() {
    f(C);
}