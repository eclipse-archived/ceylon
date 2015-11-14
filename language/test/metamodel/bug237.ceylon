@test
shared void bug237(){
    class A() {}
    class B() extends A(){}

    void bar<R>(B arg) {
        assert(is R arg);
    }

    bar<A>(B());
}
