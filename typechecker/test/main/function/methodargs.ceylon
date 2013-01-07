void testShortcutMethodSpec(){
    Float f(Float g(Float x));
    f(Float g(Float x)) => g(1.0);
    f {
        g(Float x) => x*2.0;
    };
    f {
        g = (Float x) => x*2.0;
    };
    f {
        function g(Float x) => x*2.0;
    };
    //f {
    //    @error value g(Float x) => x*2.0;
    //};
    f {
        Float g(Float x) => x*2.0;
    };
    f {
        @error g(Integer x) => x*2.0;
    };
    f {
        @error g = (Integer x) => x*2.0;
    };
    f {
        @error function g(Integer x) => x*2.0;
    };
    f {
        @error g(Float x) => x.integer*2;
    };
    f {
        @error g = (Float x) => x.integer*2;
    };
    f {
        @error function g(Float x) => x.integer*2;
    };
    
    Float h(Float x);
    h(Float x) => x;
    h {
        x => 5.0;
    };
    h {
        value x => 5.0;
    };
    h {
        @error function x => 5.0;
    };
    h {
        Float x => 5.0;
    };
    h {
        @error x => 5;
    };
}

void bla(String a, Integer... p){
    bla("a", for(i in {}) i);
    bla("a");
    bla("a", 1, 2);
    bla("a", [1, 2]...);
}
void bli(Integer... p){
    bli(for(i in {}) i);
    bli();
    bli(1, 2);
    bli([1, 2]...);
}