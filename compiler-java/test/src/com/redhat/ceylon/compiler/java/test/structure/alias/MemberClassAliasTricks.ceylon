shared class MemberClassAliasTricks_Foo(Integer a = 1, Integer b = 2){
    
    shared class MemberClassAliasToToplevel(Integer a, Integer b) => MemberClassAliasTricks_Foo(a,b);
    shared class MemberClassAliasToToplevel2(Integer a, Integer b) => MemberClassAliasToToplevel(a,b);

    shared class Member(Integer a = 1, Integer b = 2){
        shared class MemberClassAliasToEnclosingMemberClass(Integer a, Integer b) => Member(a,b);

        void test(){
            value m1 = MemberClassAliasToEnclosingMemberClass(1,2);
        }
    }

    void test(){
        value m1 = MemberClassAliasToToplevel(1,2);
        value m2 = MemberClassAliasToToplevel2(1,2);
        value m3 = Member(1,2).MemberClassAliasToEnclosingMemberClass(3,4);
    }
}

@nomodel
void memberClassAliasTricksMethod(){
    value foo1 = MemberClassAliasTricks_Foo(1,2).MemberClassAliasToToplevel(3,4);
    value foo2 = MemberClassAliasTricks_Foo(1,2).MemberClassAliasToToplevel2(3,4);
}