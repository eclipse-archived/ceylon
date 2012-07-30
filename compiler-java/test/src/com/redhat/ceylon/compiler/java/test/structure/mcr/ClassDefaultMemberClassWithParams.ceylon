@nomodel
class ClassDefaultMemberClassWithParams() {
    shared default class Member(Integer i) {
    }
    shared Member m1() {
        Member{i=1;};
        return Member(1);
    }
}
@nomodel
class ClassDefaultMemberClassWithParams_sub() extends ClassDefaultMemberClassWithParams() {
    shared actual class Member(Integer i) extends ClassDefaultMemberClassWithParams.Member(i) {
    }
    shared Member m2() {
        Member{i=2;};
        return Member(2);
    }
}