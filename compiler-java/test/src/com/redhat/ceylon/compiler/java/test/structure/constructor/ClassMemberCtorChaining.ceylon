@noanno
class ClassMemberCtorChaining() {
    shared class Member {
        shared new Member(Integer i) {
        }
        shared new Other(Integer i) {
        }
    }
}
@noanno
class ClassMemberCtorChainingSub() extends ClassMemberCtorChaining() {
    shared class Sub extends ClassMemberCtorChaining.Member {
        shared new Sub(Integer i) extends super.Member(i) {
        }
        shared new Sub2(Integer i) extends ClassMemberCtorChaining.Member(i) {
        }
        shared new Sub3(Integer i) extends Member(i) {
        }
        shared new Sub4(Integer i) extends super.Member(i) {
        }
        //shared new Other(Integer i) extends super.Other(i) {
        //}
    }
}