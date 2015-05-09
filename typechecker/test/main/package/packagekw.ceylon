import ceylon.language { Int=Integer }

String get() => "hello";
class X() {}

package.X x = package.X();

Int i1 = 1;
package.Int i2 = package.Int();

class Outer() {
    Integer get() => 0;
    class X() {}
    class Y() extends package.X() {}
    class Z() extends X() {}
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
    @error X y1 = Y();
    package.X y2 = Y();
    X z1 = Z();
    @error package.X z2 = Z();
}

@error Anything p_0 = package;
@error Anything p_1 = package();
@error Anything p_2 = (package);

void run<X>(X x) {
    package.run("hello");
    package.run<String>("hello");
    package.run<X>(x);
}