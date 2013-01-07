class Foo() {}
class Bar() extends Foo() {}

class Super() {
    shared default class Inner() => Foo();
}

class Sub1() extends Super() {
    shared actual class Inner() => Bar();
}
class Sub2() extends Super() {
    shared actual class Inner() => Foo();
}
class Sub3() extends Super() {
    shared actual class Inner() extends super.Inner() {}
}
class Sub4() extends Super() {
    shared actual class Inner() extends Foo() {}
}
class Sub5() extends Super() {
    shared actual class Inner() extends Bar() {}
}
class Sub6() extends Super() {
    @error shared actual class Inner() extends Basic() {}
}
