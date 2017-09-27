@noanno
shared interface Bug5785A {
    shared class Nested {
        shared new ctor() {}
    }
    void f(){
        // uses $this
        Nested.ctor();
    }
}

@noanno
shared class Bug5785B() satisfies Bug5785A {
    shared void m() {
        // uses Bug5785A$this
        Nested.ctor();
    }
}

@noanno
void bug5785f(Bug5785A i){
    i.Nested.ctor();
}