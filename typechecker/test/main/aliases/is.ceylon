void testis() {
    Object baz = Qux();
    if (is Bar baz) {
        @type:"Bar" value b = baz;
    }
    Bar b = Qux();
}

interface Foo<out T> {}
interface Bar => Foo<String>;
class Baz() satisfies Bar {}
class Qux() => Baz();

Int i = 1;
abstract class Int(Integer i) => Integer(i);


Y y = Y();
YY yy = YY();
class X() {}
class Y() extends X() {}
class YY() => X();