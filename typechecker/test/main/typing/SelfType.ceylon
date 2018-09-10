//interface Aaa satisfies Comparable<Ccc|Aaa> {}
//interface Ccc satisfies Comparable<Ccc|Aaa> {}

$error:"contravariant type parameter may not act as self type"
shared abstract class Comparable2<in Other>() of Other
        given Other satisfies Comparable2<Other> {
    
    shared formal Integer compare(Other that);
    
    shared Integer reverseCompare(Other that) { 
        return that.compare(this of Other);
    }
    
}

class Foo(i)
        extends Comparable2<Foo>() {
    Integer i;
    shared actual Integer compare(Foo that) {
        return this.i - that.i;
    }
}

$error class Bar()
       extends Comparable2<Foo>() {
    shared actual Integer compare(Foo that) {
        $error return that.i;
    }
}

void test() {
    Comparable2<Foo> foo = Foo(+1);
    foo.compare(foo of Foo);
    Foo foo2 = foo of Foo;
}

class SelfTypeEquivalence1() {
    interface Self<T> of T {}
    class X() satisfies Self<X> {}
    interface Inv<T> {}
    Inv<Self<X>> l1 { throw; }
    Inv<X> l2 { throw; }
//    Inv<Self<X>> l3 = l2;
//    Inv<X> l4 = l1;
}

class SelfTypeEquivalence2() {
    interface Self<T> of T {}
    class X() {}
    interface Inv<T> {}
    Inv<Self<X>> l1 { throw; }
    Inv<X> l2 { throw; }
    $error Inv<Self<X>> l3 = l2;
    $error Inv<X> l4 = l1;
}

class SelfTypeEquivalence3() {
    interface Self<T> of T {}
    class X() {}
    interface Inv<out T> {}
    Inv<Self<X>> l1 { throw; }
    Inv<X> l2 { throw; }
//    $error Inv<Self<X>> l3 = l2;
//    Inv<X> l4 = l1;
}

class SelfTypeEquivalence4() {
    interface Self<T> of T {}
    class X() {}
    interface Inv<in T> {}
    Inv<Self<X>> l1 { throw; }
    Inv<X> l2 { throw; }
//    Inv<Self<X>> l3 = l2;
//    $error Inv<X> l4 = l1;
}

abstract class Aa() {}
$error abstract class Bb() extends Comparable2<Bb&Aa>() {}
$error abstract class Cc() extends Comparable2<Cc|Aa>() {}
$error abstract class Zz() extends Comparable2<Bb|Cc|Aa>() {}

abstract class A() extends Comparable2<C|A>() {}
abstract class C() extends Comparable2<C|A>() {}

void testOf(Comparable2<C|A> comp, Anything vd) {
    A|C ac = comp of C|A;
    Object? maybe = vd of Object|Null;
}

$error:"contravariant type parameter may not act as self type"
interface Comp<in T> of T
        given T satisfies Comp<T> {
    shared formal Comparison compare(T other);
}

$error:"contravariant type parameter may not act as self type"
interface Scal<in T> of T
        satisfies Comp<T> 
        given T satisfies Scal<T> {}

class CompBar() satisfies Comp<CompBar> {
    shared actual Comparison compare(CompBar other) {
        return nothing;
    }
}

$error class CompFoo() satisfies Comp<CompBar> {
    shared actual Comparison compare(CompBar other) {
        return nothing;
    }
}

$error class Broken<T>() of T 
        satisfies Comparable<T> 
        given T satisfies Comparable<T>{
    shared actual Comparison compare(T other) {
        return nothing;
    }
}

abstract class OkSelfTypeWithUpperBound<T>() 
        given T satisfies Object {
    class Inner() of T {
        shared String size = "big";
    }
    T t = Inner() of T;
    Integer size = t.hash;
}

abstract class BrokenSelfTypeWithUpperBound<T>() 
        given T satisfies String {
    $error class Inner() of T {
        shared String size = "big";
    }
    T t = Inner() of T;
    Integer size = t.size;
}

class Universe() {
    Integer count = 0;
    class Outer() extends Universe() {
        String name = "Gavin";
        $error class Inner() extends Outer() {
            void fun(Outer o) {
                print(this.name);
                print(this.count);
                print(outer.name);
                print(o.name);
                print(outer.count);
                print(o.count);
            }
        }
    }
}
