@nomodel
class ClassDefaultMemberClassReference() {
    shared default class Member() {
    }
    void m(ClassDefaultMemberClassReference.Member() f) {
        f();
    }
    void m2(ClassDefaultMemberClassReference i) {
        m(Member);
        m(i.Member);
    }
}