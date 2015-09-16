import ceylon.language.meta.model {...}

class Bug708 {
    shared new nnew() {}
    shared new val {}
    shared class Member {
        shared new nnew() {}
        shared new val {}
    }
    
}

@test
shared void bug708() {
    CallableConstructor<Bug708, []> constructor1 = `Bug708.nnew`;
    assert(!constructor1.container exists);
    ValueConstructor<Bug708> constructor2 = `Bug708.val`;
    assert(!constructor2.container exists);
    
    MemberClassCallableConstructor<Bug708, Bug708.Member, []> mconstructor1 = `Bug708.Member.nnew`;
    assert(`Bug708` == mconstructor1.container);
    
    MemberClassValueConstructor<Bug708, Bug708.Member> mconstructor2 = `Bug708.Member.val`;
    assert(`Bug708` == mconstructor2.container);
}
