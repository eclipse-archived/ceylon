String get() => "hello";
class X() {}

class Outer() {
    Integer get() => 0;
    class X() {}
    class Inner() {
        Float get() => 0.0;
        class X() {}
        @type:"Float" get();
        @type:"Float" this.get();
        @type:"Integer" outer.get();
        @type:"String" package.get();
        @type:"Outer.Inner.X" X();
        @type:"Outer.Inner.X" this.X();
        @type:"Outer.X" outer.X();
        @type:"X" package.X();
    }
}