@noanno
interface InterfaceMemberCtorChaining {
    shared class Member {
        shared new (Integer i) {
        }
        shared new other(Integer i) {
        }
    }
}
@noanno
interface InterfaceMemberCtorChainingSub satisfies InterfaceMemberCtorChaining {
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