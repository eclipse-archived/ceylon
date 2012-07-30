@nomodel
abstract class ClassFormalMemberClass() {
    shared formal class Member() {
    }
    shared void m1(ClassFormalMemberClass qual) {
        Member{};
        Member();
        qual.Member{};
        qual.Member();
    }
}
@nomodel
class ClassFormalMemberClass_sub() extends ClassFormalMemberClass() {
    shared actual class Member() extends ClassFormalMemberClass.Member() {
    }
    shared void m2(ClassFormalMemberClass_sub qual) {
        Member{};
        Member();
        qual.Member{};
        qual.Member();
    }
}