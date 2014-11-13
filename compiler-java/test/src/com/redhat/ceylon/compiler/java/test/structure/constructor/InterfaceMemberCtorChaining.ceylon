@noanno
interface InterfaceMemberCtorChaining {
    shared class Member {
        shared new Member(Integer i) {
        }
        shared new Other(Integer i) {
        }
    }
    shared class Foo(Integer i){}
}
@noanno
interface InterfaceMemberCtorChainingSub satisfies InterfaceMemberCtorChaining {
    shared class Bar(Integer i) extends InterfaceMemberCtorChaining.Foo(i){}
    shared class Sub extends InterfaceMemberCtorChaining.Member {
        shared new Sub(Integer i) extends super.Member(i) {
        }
        //shared new Sub2(Integer i) extends ClassMemberCtorChaining.Member(i) {
        //}
        shared new Sub3(Integer i) extends Member(i) {
        }
        shared new Sub4(Integer i) extends super.Member(i) {
        }
        //shared new Other(Integer i) extends super.Other(i) {
        //}
    }
    
}