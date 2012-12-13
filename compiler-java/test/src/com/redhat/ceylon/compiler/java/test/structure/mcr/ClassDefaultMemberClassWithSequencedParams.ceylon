@nomodel
class ClassDefaultMemberClassWithSequencedParams() {
    shared default class Member(Integer... i) {
    }
    shared Member m1() {
        Member{i=1; i=2; i=3;};
        Member{};
        Member();
        return Member(1, 2, 3);
    }
}
@nomodel
class ClassDefaultMemberClassWithSequencedParams_sub() extends ClassDefaultMemberClassWithSequencedParams() {
    shared actual class Member(Integer... i) extends super.Member(i...) {
    }
    shared Member m2() {
        Member{i=2; i=3; i=4;};
        Member{};
        Member();
        return Member(2, 3, 4);
    }
}