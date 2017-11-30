@noanno
shared class CtorDelegationWithDp {
    shared new (String name = "") {}
    shared new foo extends CtorDelegationWithDp(""){}
    shared new bar extends CtorDelegationWithDp(){}
    shared new baz() extends CtorDelegationWithDp(""){}
    shared new qux() extends CtorDelegationWithDp(){}
}