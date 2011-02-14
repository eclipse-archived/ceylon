class Assignability() {
    
    class X() {}
    class Y() {}
    
    X x1 = X();
    @error X x2 = Y();
    
    X x3;
    x3 = X();
    
    X x4;
    @error x4 = Y();
    
}