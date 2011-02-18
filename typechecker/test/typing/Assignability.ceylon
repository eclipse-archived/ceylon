class Assignability() {
    
    class X() {}
    class Y() {}
    
    void method(X arg1, Y arg2) {}
    
    method(X(),Y());
    
    method { arg1=X(); arg2=Y(); };
    
    @error method(Y(), Y());
    @error method(X(), X());

    @error method { arg1=X(); arg2=X(); };
    @error method { arg1=Y(); arg2=Y(); };

    this.X();
    this.X{};
    
    @error X(Y());
    @error method(X());
    @error method(X(),Y(),this);
    
    @error X{ y=Y(); };
    @error method{ arg1=X(); };
    @error method{ arg1=X(); arg2=Y(); arg3=this; };
    
    @error X{ Y() };
    @error method{ arg1=X(); arg2=Y(); this };
    
    method { 
        X arg1 { return X(); }
        Y arg2 { return Y(); }
    };
    
    @error method { 
        X arg1 { return X(); }
        X arg2 { return X(); }
    };
    
    @error method { 
        X arg1 { return X(); }
    };
    
    @error method { 
        X arg1 { return X(); }
        Y arg2 { return Y(); }
        X arg3 { return X(); }
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
    
}