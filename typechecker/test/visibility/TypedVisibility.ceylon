class X() {}
shared class Y() {}

@error shared X x = X();
shared Y y = Y();
@error shared Class.Z cz = Class().Z();
shared SharedClass.Z scz = SharedClass().Z();

class Class() {
    class W() {}
    shared class Z() {}
    
    shared X x = X();
    shared Y y = Y();
    shared W w = W();
    shared Z z = Z();
}

shared class SharedClass() {
    class W() {}
    shared class Z() {}
    
    @error shared X x = X();
    shared Y y = Y();
    @error shared W w = W();
    shared Z z = Z();
    
    class Inner() {
        shared W w = W();
        shared X x = X();
        shared Y y = Y();
        shared Z z = Z();
    }
    
    shared class SharedInner() {
        @error shared W w = W();
        @error shared X x = X();
        shared Y y = Y();
        shared Z z = Z();
    }
}