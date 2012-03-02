@nomodel
class C() {
    Integer f() {
        return 1;
    }
    Callable<Integer> m() {
        return f;
    }
    Callable<Integer> m2() {
        return this.f;
    }
    Callable<Integer> m3(C c) {
        return c.f;
    }
    
}
