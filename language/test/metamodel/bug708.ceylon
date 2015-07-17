import ceylon.language.meta.model {...}

class Bug708 {
    shared new nnew() {}
    shared class Member {
        shared new nnew() {}
    }
}

@test
shared void bug708() {
    Function<Bug708, []> constructor1 = `Bug708.nnew`;
    assert(!constructor1.container exists);
    Method<Bug708, Bug708.Member, []> constructor2 = `Bug708.Member.nnew`;
    assert(is Class<Bug708.Member> clazz2 = constructor2.container);
}
