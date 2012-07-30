@nomodel
class ClassDefaultMemberClass() {
    shared default class Member() {
    }
    shared void m1(ClassDefaultMemberClass qual) {
        Member{};
        Member();
        qual.Member();
        qual.Member{};
    }
}
@nomodel
class ClassDefaultMemberClass_sub() extends ClassDefaultMemberClass() {
    shared actual class Member() extends ClassDefaultMemberClass.Member() {
    }
    shared void m2(ClassDefaultMemberClass_sub qual) {
        Member{};
        Member();
        qual.Member();
        qual.Member{};
    }
}