shared class MemberClassAlias_Foo(){
    shared class Member(){}
    
    shared class MemberClassAlias() = Member();

    void test(){
        value m = MemberClassAlias();
    }
}

@nomodel
void memberClassAliasMethod(){
    value foo = MemberClassAlias_Foo().MemberClassAlias();
}