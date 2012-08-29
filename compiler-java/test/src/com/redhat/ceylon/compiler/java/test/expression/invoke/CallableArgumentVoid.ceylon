@nomodel
class CallableArgumentVoid() {
    void f(Void() g) {
        g();
    }
    void m(){}
    shared default void m2(){}
    shared default Void m3(){return 1;}
    void callsite() {
        f(m);
        f(m2);
        f(m3);
    }
}