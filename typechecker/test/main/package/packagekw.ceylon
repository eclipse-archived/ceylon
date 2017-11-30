import ceylon.language { Int=Integer }
import ceylon.language.meta.declaration {
  Package, Module
}

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
        $type:"Float" get();
        $type:"Float" this.get();
        $type:"Integer" outer.get();
        $type:"String" package.get();
        $type:"Outer.Inner.X" X();
        $type:"Outer.Inner.X" this.X();
        $type:"Outer.X" outer.X();
        $type:"X" package.X();
    }
    $error X y1 = Y();
    package.X y2 = Y();
    X z1 = Z();
    $error package.X z2 = Z();
}

Package p_1 = package;
Package p_2 = (package);

Module m_1 = module;
Module m_2 = (module);

void run<X>(X x) {
    package.run("hello");
    package.run<String>("hello");
    package.run<X>(x);
}

object t extends Bool() {}
object f extends Bool() {}
abstract class Bool() 
        of package.t
         | package.f {
    shared Boolean t => this==package.t;
    shared Boolean f => this==package.f;
}