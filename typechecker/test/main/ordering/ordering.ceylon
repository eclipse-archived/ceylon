
class SubSub() extends Sub() {
    void xx() {
        Inner inner = Inner();
    }
}
class Sub() extends Super() {}
class Super() {
    shared class Inner() {}
}

interface DownDown satisfies Down {
    void xx() {
        Inner inner = Inner();
    }
}
interface Down satisfies Up {}
interface Up {
    shared class Inner() {}
}

void test2() {
    OthCat2.In zzzz;
}
interface Cat2<in T> {
    shared default class In() {}
}
interface OthCat2 satisfies Cat2<String> {}

void test3() {
    <SubCat3&Cat3<Integer>>.In xxxx;
}
interface Cat3<in T> {
    shared default class In() {}
}
interface SubCat3 satisfies Cat3<Float> {
    shared actual class In() extends super.In() {}
}

@error class CX() extends CY() {
    shared String hello = "hello";
}
@error class CY() extends CX() {}


class Foo() extends Baz() {
    shared actual class Inner()
            extends super.Inner() {}
}

class Baz() extends Bar() {}

class Bar() {
    shared default class Inner() {}
}

interface II {
    shared formal class Inner() {}
}

void foo() {
    A.Inner type;
}

alias A => II;


void moretest() {
    S<String>.Inner si1 = S5().Inner();
    S<String>.Inner si2 = S4().Inner();
    S<String>.Inner si3 = S3().Inner();
}

class S4() extends S3() {}
class S5() extends S4() {
    void xx() {
        Inner inner = Inner();
        String str = Inner().nawt;
    }
}
class S3() extends S2() {}
class S2() extends S1() {}
class S1() extends S<String>() {}
class S<T>() {
    shared class Inner() {
        shared T nawt=>nothing;
    }
}
