class ReallyIndirectlyBroken() extends Concrete() {
    @error name = "Trompon";
}

class AlsoReallyIndirectlyBroken() extends Concrete() {
    @error shared actual String name = "Trompon";
}

abstract class Abstract() {
    shared formal String name;
}

class Concrete() extends Abstract() {
    name = "Trompon";
    print(name);
}

@error class Broken() extends Abstract() {
    void method() {
        @error name = "Trompon";
    }
}

class IndirectlyBroken() extends Concrete() {
    @error name = "Trompon";
}

class AlsoIndirectlyBroken() extends Concrete() {
    @error shared actual String name = "Trompon";
}

class BadlyTyped() extends Abstract() {
    @error name = 1;
}

class BadlyUsed() extends Abstract() {
    @error print(name);
    name = "Tompon";
}

class BadlyDuped() extends Abstract() {
    name = "Gavin";
    @error name = "Tako";
}

class ReallyBadlyDuped() extends Abstract() {
    @error name = "Gavin";
    shared actual String name = "Tako";
}

class OK() extends Abstract() {
    name = "Trompon";
    print(name);
    value x = 0.0;
    shared String getName() {
        return name;
    }
}

abstract class AlsoAbstract() {
    shared formal variable String x;
    shared default String y = "";
}

class AlsoBroken() extends AlsoAbstract() {
    @error x = "hello";
    y = "goodbye";
}

Float sq(Float x) {
    return x*x;
}

abstract class OtherAbstract() {
    shared formal Float sqr(Float x);
}

class OtherReallyIndirectlyBroken() extends OtherConcrete() {
    @error sqr = sq;
}

class OtherAlsoReallyIndirectlyBroken() extends OtherConcrete() {
    @error shared actual Float sqr(Float x) { return x**2; }
}

class OtherConcrete() extends OtherAbstract() {
    sqr = sq;
    sqr(1.0);
}

class OtherBadlyTyped() extends OtherAbstract() {
    @error sqr = print;
}

class OtherBadlyUsed() extends OtherAbstract() {
    @error sqr(2.0);
    sqr = sq;
}

class OtherBadlyDuped() extends OtherAbstract() {
	sqr = sq;
    @error sqr = sq;
}

abstract class X<T>() {
    shared formal void foo(T s);
    shared formal T bar;
    shared formal Object baz(String s(Integer i));
    shared formal String qux<S>();
    shared formal void fum(String string = "hello");
    shared formal void fo(String... strings);
}

class Y() extends X<String>() {
    foo = (String s) print(s.uppercased);
    bar = "hello";
    baz = (String(Integer) s) s(0);
    @error qux = () "hello";
    fum = (String s) print(s);
    fo = (String... ss) print(", ".join(ss...));
}

void testxy() {
	@type:"String" value b = Y().bar;
	Y().foo("hello");
	Y().fum();
	Y().fo("x", "y", "z");
}

class FatArrowRefinement(name) 
        extends Object() 
        satisfies Comparable<FatArrowRefinement> {
    String name;
    hash => string.hash;
    equals(Object that) => string==that.string;
    shared actual String string => name;
    compare(FatArrowRefinement other) => name<=>other.name;
}

interface Above {
    shared formal void f();
    shared formal String s;
}
interface Below satisfies Above {
    f() => print(s);
    s => "";
}
interface Below2 satisfies Above {
    shared actual void f() { print(s); }
    shared actual String s { return ""; }
}
interface BrokenBelow satisfies Above {
    void g() {}
    @error f = g;
    @error s = "";
}
object below satisfies Below {}
object below2 satisfies Below2 {}
@error object brokenBelow satisfies Below&Below2 {}

class Count1(Integer x) {
    string = "Count " x "";
    equals(Object that) => that is Count1|Count2;
    hash => x;
}
class Count2(Integer x) extends Object() {
    string = "Count " x "";
    equals(Object that) => that is Count1|Count2;
    hash => x;
}
