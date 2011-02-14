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
    
}