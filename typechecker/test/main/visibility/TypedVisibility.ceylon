class X() {}
shared class Y() {}

class Class() {
    class W() {}
    shared class Z() {}
    
    shared X x = X();
    shared Y y = Y();
    @error shared W w = W();
    shared Z z = Z();
    
    shared void vx(X x) {}
    shared void vy(Y y) {}
    shared void vw(@error W w) {}
    shared void vz(Z z) {}

    shared class Vx(X x) {}
    shared class Vy(Y y) {}
    shared class Vw(@error W w) {}
    shared class Vz(Z z) {}
}

shared class SharedClass() {
    class W() {}
    shared class Z() {}
    
    @error shared X x = X();
    shared Y y = Y();
    @error shared W w = W();
    shared Z z = Z();
    
    shared void vx(@error X x) {}
    shared void vy(Y y) {}
    shared void vw(@error W w) {}
    shared void vz(Z z) {}

    shared class Vx(@error X x) {}
    shared class Vy(Y y) {}
    shared class Vw(@error W w) {}
    shared class Vz(Z z) {}

    class Inner() {
        shared W w = W();
        shared X x = X();
        shared Y y = Y();
        shared Z z = Z();
        
        shared void vx(X x) {}
        shared void vy(Y y) {}
        shared void vw(W w) {}
        shared void vz(Z z) {}

        shared class Vx(X x) {}
        shared class Vy(Y y) {}
        shared class Vw(W w) {}
        shared class Vz(Z z) {}
    }
    
    shared class SharedInner() {
        @error shared W w = W();
        @error shared X x = X();
        shared Y y = Y();
        shared Z z = Z();
        
        shared void vx(@error X x) {}
        shared void vy(Y y) {}
        shared void vw(@error W w) {}
        shared void vz(Z z) {}

        shared class Vx(@error X x) {}
        shared class Vy(Y y) {}
        shared class Vw(@error W w) {}
        shared class Vz(Z z) {}
    }

}

@error shared X x = X();
X x2 = X();
shared Y y = Y();
@error shared Class.Z cz = Class().Z();
Class.Z cz2 = Class().Z();
shared SharedClass.Z scz = SharedClass().Z();
@error shared Class.W? cw = null;
@error shared SharedClass.W? scw = null;

shared void vx(@error X x) {}
shared void vy(Y y) {}
shared void vcz(@error Class.Z cz) {}
shared void vscz(SharedClass.Z scz) {}

@error shared X getX() { return X(); }
@error shared Class.Z getZ() { return Class().Z(); }
