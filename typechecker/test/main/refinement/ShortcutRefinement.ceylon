class ReallyIndirectlyBroken() extends Concrete() {
    $error name = "Trompon";
}

class AlsoReallyIndirectlyBroken() extends Concrete() {
    $error shared actual String name = "Trompon";
}

abstract class Abstract() {
    shared formal String name;
}

class Concrete() extends Abstract() {
    name = "Trompon";
    print(name);
}

$error class Broken() extends Abstract() {
    void method() {
        $error name = "Trompon";
    }
}

class IndirectlyBroken() extends Concrete() {
    $error name = "Trompon";
}

class AlsoIndirectlyBroken() extends Concrete() {
    $error shared actual String name = "Trompon";
}

class BadlyTyped() extends Abstract() {
    $error name = 1;
}

class BadlyUsed() extends Abstract() {
    $error print(name);
    name = "Tompon";
}

class BadlyDuped() extends Abstract() {
    name = "Gavin";
    $error name = "Tako";
}

class ReallyBadlyDuped() extends Abstract() {
    $error name = "Gavin";
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
    $error x = "hello";
    y = "goodbye";
}

Float sq(Float x) {
    return x*x;
}

abstract class OtherAbstract() {
    shared formal Float sqr(Float x);
}

class OtherReallyIndirectlyBroken() extends OtherConcrete() {
    $error sqr = sq;
}

class OtherAlsoReallyIndirectlyBroken() extends OtherConcrete() {
    $error shared actual Float sqr(Float x) { return x^2; }
}

class OtherConcrete() extends OtherAbstract() {
    sqr = sq;
    sqr(1.0);
}

class OtherBadlyTyped() extends OtherAbstract() {
    $error sqr = print;
}

class OtherBadlyUsed() extends OtherAbstract() {
    $error sqr(2.0);
    sqr = sq;
}

class OtherBadlyDuped() extends OtherAbstract() {
	sqr = sq;
    $error sqr = sq;
}

abstract class X<T>() {
    shared formal Anything foo(T s);
    shared formal T bar;
    shared formal Object baz(String s(Integer i));
    shared formal String qux<S>();
    shared formal Anything fum(String string = "hello");
    shared formal Anything fo(String* strings);
    shared formal void fee();
}

class Y() extends X<String>() {
    foo = (String s) => print(s.uppercased);
    bar = "hello";
    baz = (String(Integer) s) => s(0);
    $error qux = () => "hello";
    fum = (String s) => print(s);
    fo = (String* ss) => print(", ".join(ss));
    $error fee = void () => 0;
}

class Z() extends X<String>() {
    foo(String s) => print(s.uppercased);
    bar = "hello";
    baz(String s(Integer i)) => s(0);
    $error qux() => "hello";
    fum(String s) => print(s);
    fo(String* ss) => print(", ".join(ss));
    $error fee() => 0;
}

class W() extends X<String>() {
    foo($error Integer s) => print(s.float);
    $error bar = 1;
    baz($error String s(String ss)) => s("");
    $error qux() => 1;
    fum($error Integer s) => print(s);
    fo($error Integer* ss) => print(", ".join(ss*.string));
    $error fee(Integer i) => print(i);
}

void testxy() {
	$type:"String" value b = Y().bar;
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
    shared formal Integer f();
    shared formal String s;
    shared formal void x();
}
interface Below satisfies Above {
    f() => 1;
    s => "";
    x() => print("hello");
}
interface Below2 satisfies Above {
    shared actual Integer f() { return 1; }
    shared actual String s { return ""; }
    shared actual void x() { print("hello"); }
}
interface BrokenBelow satisfies Above {
    void g() {}
    $error f = g;
    $error s = "";
    $error x() => "hello";
}
object below satisfies Below {}
object below2 satisfies Below2 {}
$error object brokenBelow satisfies Below&Below2 {}

class Count1(Integer x) {
    string = "Count 'x'";
    equals(Object that) => that is Count1|Count2;
    hash => x;
}
class Count2(Integer x) extends Object() {
    string = "Count 'x'";
    equals(Object that) => that is Count1|Count2;
    hash => x;
}

interface MyInt {
    shared formal void f(String s);
}
class MyCla() satisfies MyInt {
    f = print;
}
interface MySubint satisfies MyInt {
    $error f = print; //TODO: unnecessary error * revisit this?
}

abstract class SuperWithVariable() {
    shared variable Integer count1 = 0;
    shared default variable Integer count2 = 0;
    shared formal variable Integer count3;
}

class SubWithAssignment() 
        extends SuperWithVariable() {
    $error count1 = 5;
    $error count2 = 10;
    $error count3 = 15;
}

abstract class WithInnerSubClass() {
    shared formal String name;
    class SubClass() extends WithInnerSubClass() {
        name => "Gavin";
    }
}

abstract class WithInnerSubClass2() {
    shared variable default String name="";
    class SubClass() extends WithInnerSubClass2() {
        $error name = "Gavin";
        $error name = "Tom";
    }
}

abstract class WithInnerSubClass3() {
    shared variable formal String name;
    class SubClass() extends WithInnerSubClass2() {
        $error name = "Gavin";
        $error name = "Tom";
    }
}

class Bug1(String s) {
    equals = s.equals;
    string=equals("Y") then "Yay!" else "Nay...";
}

class Bug2(String s) {
    equals(Object that) => s.equals(that);
    string=equals("Y") then "Yay!" else "Nay...";
}

class Bug3(String s) {
    $error equals => s.equals;
}


abstract class Super0() {
    shared formal String foo(String x)(Integer i);
}
class Mid1() extends Super0() {
    shared actual default String foo(String x)(Integer i) => "";
}
class OkSub1() extends Super0() {
    foo(String x)(Integer i) => x;
}
class OkSub2() extends Super0() {
    foo(String x) => (Integer i) => x;
}
class OkSub3() extends Super0() {
    foo = (String x)(Integer i) => x;
}
class OkSub4() extends Mid1() {
    foo(String x)(Integer i) => x;
}
class OkSub5() extends Mid1() {
    foo(String x) => (Integer i) => x;
}
class OkSub6() extends Mid1() {
    foo = (String x)(Integer i) => x;
}
class BadSub1() extends Super0() {
    $error foo(String x) => x;
}
class BadSub2() extends Mid1() {
    $error foo(String x) => x;
}
class BadSub3() extends Super0() {
    $error foo = (Integer i) => x;
}
class BadSub4() extends Mid1() {
    $error foo = (Integer i) => x;
}

void moreRefinements() {
    class X() {
        shared default String foo()(String s) => s;
    }
    
    class Y() extends X() {
        $error shared actual String(String) foo() => (String s) => s;
    }
    
    class Z() extends X() {
        foo() => (String s) => s;
    }
    
    class W() extends X() {
        foo = ()(String s) => s;
    }
}

void withQualifiedType() {
    interface Foo {
        shared class Bar() {}
        shared formal Bar bar;
    }
    
    class Baz() satisfies Foo {
        Foo foo = nothing; 
        bar => foo.bar;  
    }
}

class WithConstructor 
        satisfies Category<Boolean> {
    shared new () {}
    contains(Boolean element) => true;
}


interface WithGeneric {
    shared formal String generic<T>()(String x);
}

interface WithNonGeneric {
    shared formal String nongeneric()(String x);
}

class RefinesGeneric() satisfies WithGeneric {
    generic = <T>()(String x) => x;
}

class RefinesGenericWrong() satisfies WithGeneric {
    $error generic = <T>()(String x) given T satisfies Object => x;
}

class RefinesGenericBad() satisfies WithGeneric {
    $error generic = ()(String x) => x;
}

class RefinesNonGeneric() satisfies WithNonGeneric {
    nongeneric = ()(String x) => x;
}

class RefinesNonGenericWrong() satisfies WithNonGeneric {
    $error nongeneric = ()(Integer x) => x.string;
}

class RefinesNonGenericBad() satisfies WithNonGeneric {
    $error nongeneric = <T>()(String x) => x;
}

abstract class WithFunctionAndValueAbstract() {
    shared formal String fun(String str);
    shared formal String(String) val;
}

class WithFunctionAndValue() 
        extends WithFunctionAndValueAbstract() {
    fun(String str) => str;
    val = identity<String>;
}

class WithValueAndFunction() 
        extends WithFunctionAndValueAbstract() {
    fun = identity<String>;
    val(String str) => str;
}

class WithValueAndFunctionShouldWork() 
        extends WithFunctionAndValueAbstract() {
    $error //fixme
    shared actual String(String) fun = identity<String>;
    $error //fixme
    shared actual String val(String str) => str;
}

interface WithGenericFormal {
    shared formal Value method<Value>
            (Value something);
}

object impl satisfies WithGenericFormal {
    shared actual Value method<Value>
            (Value something) 
            => something;
}

class WithShortcutAssignment()
        satisfies WithGenericFormal {
    
    method = impl.method;
    
    Float float = method(1.0);
}

// fixme when named "WithShortcutRefinement", tests pass on mac but not linux
//$error class WithShortcutRefinement()
$error class WithShortcutRefinement2()
        satisfies WithGenericFormal {
    
    $error method<X,Y>(X something) 
            => impl.method(something);
    
    $error Float method = method(1.0);
}
