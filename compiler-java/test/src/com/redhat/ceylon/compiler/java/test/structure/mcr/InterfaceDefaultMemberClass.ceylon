@nomodel
interface InterfaceDefaultMemberClass {
    shared default class Member() {
    }
    shared void m1(InterfaceDefaultMemberClass qual) {
        Member{};
        Member();
        qual.Member{};
        qual.Member();
    }
}
@nomodel
class InterfaceMemberDefaultClass_sub() satisfies InterfaceDefaultMemberClass {
    shared actual class Member() extends InterfaceDefaultMemberClass::Member() {
    }
    shared void m2(InterfaceMemberDefaultClass_sub qual) {
        Member{};
        Member();
        qual.Member{};
        qual.Member();
    }
}