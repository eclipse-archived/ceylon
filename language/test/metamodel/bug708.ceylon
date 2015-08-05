import ceylon.language.meta.model {...}

class Bug708 {
    shared new nnew() {}
    shared class Member {
        shared new nnew() {}
    }
}

@test
shared void bug708() {
    CallableConstructor<Bug708, []> constructor1 = `Bug708.nnew`;
    assert(!constructor1.container exists);
    MemberClassCallableConstructor<Bug708, Bug708.Member, []> constructor2 = `Bug708.Member.nnew`;
    assert(`Bug708` == constructor2.container);
}
