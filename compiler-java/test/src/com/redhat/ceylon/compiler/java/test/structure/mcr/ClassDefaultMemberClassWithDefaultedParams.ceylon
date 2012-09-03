@nomodel
class ClassDefaultMemberClassWithDefaultedParams() {
    shared default class Member(Integer i = 1) {
    }
    shared Member m1() {
        Member{};
        return Member();
    }
}
@nomodel
class ClassDefaultMemberClassWithDefaultedParams_sub() extends ClassDefaultMemberClassWithDefaultedParams() {
    shared actual class Member(Integer i) extends super.Member(i) {
    }
    shared Member m2() {
        Member{};
        return Member();
    }
}