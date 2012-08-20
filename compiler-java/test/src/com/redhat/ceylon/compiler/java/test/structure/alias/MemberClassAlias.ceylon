shared class MemberClassAlias_Foo(){
    shared class Member(){}
    shared class MemberClassAlias() = Member;
}

// FIXME: that seems wrong, pending https://github.com/ceylon/ceylon-spec/issues/379
//shared class MemberClassAlias() = MemberClassAlias_Foo.Member;

@nomodel
void memberClassAliasMethod(){
    // I think this is a bug in the typechecker: https://github.com/ceylon/ceylon-spec/issues/380
    value foo = MemberClassAlias_Foo().MemberClassAlias();
}