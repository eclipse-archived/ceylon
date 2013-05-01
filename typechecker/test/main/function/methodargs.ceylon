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

void bla(String a, Integer* p){
    bla("a", for(i in {1, 2}) i);
    bla("a");
    bla("a", 1, 2);
    bla("a", *[1, 2]);
}
void bli(Integer* p){
    bli(for(i in {1, 2}) i);
    bli();
    bli(1, 2);
    bli(*[1, 2]);
}

void testIndirectSpread() {
    value f = function(Integer a, Integer b=2, Integer* c) => 1;
    f(*[1]);
    f(1, *[]);
    f(1, *[2]);
    f(*[1, 2]);
    f(1, 2, *[]);
    f(1, 2, *[3]);
    f(1, 2, 3, *[]);
    f(*[1, 2, 3]);
}

void testIndirectWithUnknownParamTypes() {
    Callable<Anything, Nothing> f4 = function (String a, Integer b) => 2;
    @error f4();
    @error f4(1);
    @error f4(1, 3);

    Callable<Anything, Nothing[]> f5 = function (Integer* a) => 2;
    f5();
    @error f5(1);
    @error f5(1, 3);
}