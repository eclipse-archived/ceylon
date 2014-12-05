@noanno
interface InterfaceMemberCtorChaining {
    shared class Member {
        shared new Member(Integer i) {
        }
        shared new Other(Integer i) {
        }
    }
}
@noanno
interface InterfaceMemberCtorChainingSub satisfies InterfaceMemberCtorChaining {
    shared class Sub extends super.Member {
        shared new Sub(Integer i) extends super.Member(i) {
        }
        shared new Sub3(Integer i) extends Member(i) {
        }
        shared new Sub4(Integer i) extends super.Member(i) {
        }
        
        shared new Other(Integer i) extends super.Other(i) {
        }
    }
    
}