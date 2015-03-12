@noanno
class ClassMemberCtorChaining() {
    shared class Member {
        shared new (Integer i) {
        }
        shared new Other(Integer i) {
        }
    }
}
@noanno
class ClassMemberCtorChainingSub() extends ClassMemberCtorChaining() {
    shared class Sub extends super.Member {
        shared new (Integer i) extends Member(i) {
        }
        shared new Sub3(Integer i) extends Member(i) {
        }
        shared new Sub4(Integer i) extends Member(i) {
        }
        
        shared new Other(Integer i) extends super.Other(i) {
        }
    }
}