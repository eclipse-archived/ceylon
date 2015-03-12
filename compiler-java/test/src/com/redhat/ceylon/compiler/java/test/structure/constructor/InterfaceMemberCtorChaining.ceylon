@noanno
interface InterfaceMemberCtorChaining {
    shared class Member {
        shared new (Integer i) {
        }
        shared new Other(Integer i) {
        }
    }
}
@noanno
interface InterfaceMemberCtorChainingSub satisfies InterfaceMemberCtorChaining {
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