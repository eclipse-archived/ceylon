class MemberOverriding_Foo() {}
class MemberOverriding_Bar() extends MemberOverriding_Foo() {}

class MemberOverriding_Super() {
    shared default class Inner() = MemberOverriding_Foo;
}

class MemberOverriding_Sub1() extends MemberOverriding_Super() {
    shared actual class Inner() = MemberOverriding_Bar;
}
class MemberOverriding_Sub2() extends MemberOverriding_Super() {
    shared actual class Inner() = MemberOverriding_Foo;
}
class MemberOverriding_Sub3() extends MemberOverriding_Super() {
    shared actual class Inner() extends super.Inner() {}
}
class MemberOverriding_Sub4() extends MemberOverriding_Super() {
    shared actual class Inner() extends MemberOverriding_Foo() {}
}
class MemberOverriding_Sub5() extends MemberOverriding_Super() {
    shared actual class Inner() extends MemberOverriding_Bar() {}
}
