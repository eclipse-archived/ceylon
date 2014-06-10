
class SubSub() extends Sub() {
    void xx() {
        Inner inner = Inner();
    }
}
class Sub() extends Super() {}
class Super() {
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
