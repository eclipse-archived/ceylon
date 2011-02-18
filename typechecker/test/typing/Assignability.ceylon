class Assignability() {
    
    class X() {}
    class Y() {}
    
    void method(X arg1, Y arg2) {}
    
    method(X(),Y());
    
    method { arg1=X(); arg2=Y(); };
    
    @error method(Y(), Y());
    @error method(X(), X());

    method { arg1=X(); @error arg2=X(); };
    method { @error arg1=Y(); arg2=Y(); };

    this.X();
    this.X{};
    
    @error X(Y());
    @error method(X());
    @error method(X(),Y(),this);
    
    X{ @error y=Y(); };
    @error method{ arg1=X(); };
    method{ arg1=X(); arg2=Y(); @error arg3=this; };
    
    X{ @error Y() };
    method{ arg1=X(); arg2=Y(); @error this };
    
    method { 
        X arg1 { return X(); }
        Y arg2 { return Y(); }
    };
    
    method { 
        X arg1 { return X(); }
        @error X arg2 { return X(); }
    };
    
    @error method { 
        X arg1 { return X(); }
    };
    
    method { 
        X arg1 { return X(); }
        Y arg2 { return Y(); }
        @error X arg3 { return X(); }
    };
    
    X x1 = X();
    
    @error X x2 = Y();
    
    X x3;
    x3 = X();
    
    X x4;
    @error x4 = Y();
    
    variable X var := X(); 
    
    X attx { return var; }
    assign attx { var := attx; }
    
    Y atty { @error return var; }
    assign atty { @error var := atty; }
    
    X methx { return var; }
    Y methy { @error return var; }
    
    void methv() { @error return var; }
    
    void methv2() {
        if (var) {
            @error return var;
        }
    }
    
    X methx2 {
        if (var) {
            return var;
        }
        else {
            return X();
        }
    }
    
    Y methy2 {
        if (var) {
            @error return var;
        }
        return Y();
    }
    
    X outerMethod() {
        Y innerMethod() {
            return Y();
        }
        return X();
    }
    
    X outerAtt {
        Y innerAtt {
            return Y();
        }
        return X();
    }
    
    for (X x in {X(), X()} ) {}
    @error for (Y y in {X(), X()} ) {}
    
    void hello(String greeting = "Hello", String name = "World") {}
    
    hello("Hi", "Gavin");
    hello("Hi");
    hello();
    @error hello("Hi", "Gavin", "Foo");
    
    hello{ name = "Gavin"; };
    hello{ greeting = "Hi"; };
    hello{ greeting = "Hi"; name = "Gavin"; };
    hello{};
    
    void hi(@error String greeting = 1, String name = "World", Natural times = 1) {}
    class Hi(@error String greeting = 23.0, String name = "World", @error Natural times = "X") {}
    
    X something = X();
    @error X? nothing = null; //not really an error!
    
    @error if (exists something) {}
    if (exists nothing) {}
    
}