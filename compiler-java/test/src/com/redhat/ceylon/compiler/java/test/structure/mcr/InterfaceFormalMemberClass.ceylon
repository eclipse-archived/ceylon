@nomodel
interface InterfaceFormalMemberClass {
    shared formal class Member() {
    }
    shared void m1(InterfaceFormalMemberClass qual) {
        Member{};
        Member();
        this.Member{};
        this.Member();
        qual.Member{};
        qual.Member();
    }
}
@nomodel
class InterfaceFormalMemberClass_sub() satisfies InterfaceFormalMemberClass {
    shared actual class Member() extends InterfaceFormalMemberClass::Member() {
    }
    shared void m2(InterfaceFormalMemberClass_sub qual) {
        Member{};
        Member();
        this.Member{};
        this.Member();
        qual.Member{};
        qual.Member();
    }
}