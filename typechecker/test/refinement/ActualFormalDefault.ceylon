@error shared actual void x() {}
@error shared formal void y();
@error shared default void z() {}

@error shared abstract object w {}
@error shared abstract void a() {}
@error shared abstract String h = "hello";
@error shared abstract interface A {}

abstract class Formal() {
    shared formal String s;
    shared formal void x();
    shared formal class C() {}
    shared formal Object o;
}

abstract class BadFormal() {
    @error shared formal class B();
    @error shared formal interface I1 {}
    @error shared formal interface I2;
    @error shared formal object o1 {}
    //@error shared formal object o2;
}

class Actual() extends Formal() {
    shared actual String s = "hello";
    shared actual void x() {}
    shared actual class C() extends super.C() {}
    @error shared actual interface I {}
    shared actual object o {}
}

class Default() {
    shared default class C() {}
    shared default String s = "hi";
    shared default void x() {}
}

class BadDefault() {
    @error shared default interface I {}
    @error shared default object o {}
}

class Actual2() extends Default() {
    shared actual String s = "hello";
    shared actual void x() { super.x(); }
    shared actual class C() extends super.C() {}
    @error shared actual interface I {}
    @error shared actual object o {}
}

