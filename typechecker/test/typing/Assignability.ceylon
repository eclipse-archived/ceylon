class Assignability() {
    
    class X() {}
    class Y() {}
    
    void method(X arg1, Y arg2) {}
    
    method(X(),Y());
    
    @error method(Y(), Y());
    @error method(X(), X());
    
    this.X();
    
    @error X(Y());
    @error method(X());
    @error method(X(),Y(),this);
    
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
    
}