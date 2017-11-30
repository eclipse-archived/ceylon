@noanno
class ClassMemberCtorChaining() {
    shared class Member {
        shared new (Integer i) {
        }
        shared new other(Integer i) {
        }
    }
}
@noanno
class ClassMemberCtorChainingSub() extends ClassMemberCtorChaining() {
    shared class Sub extends super.Member {
        shared new (Integer i) extends Member(i) {
        }
        shared new sub3(Integer i) extends Member(i) {
        }
        shared new sub4(Integer i) extends Member(i) {
        }
        
        shared new other(Integer i) extends super.other(i) {
        }
    }
}