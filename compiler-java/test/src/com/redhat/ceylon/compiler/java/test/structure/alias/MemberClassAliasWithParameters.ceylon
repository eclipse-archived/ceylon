shared class MemberClassAliasWithParameters_Foo(){
    shared class Member(Integer i, String s = "foo"){}
    shared class MemberClassAlias(Integer i, String s) = Member(i,s);
    
    void test(){
        value m = MemberClassAlias(1, "a");
    }
}

@nomodel
void memberClassAliasWithParametersMethod(){
    value foo = MemberClassAliasWithParameters_Foo().MemberClassAlias(1, "a");
}