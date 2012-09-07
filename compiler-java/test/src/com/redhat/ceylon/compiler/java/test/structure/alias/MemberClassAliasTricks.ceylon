shared class MemberClassAliasTricks_Foo(){
    
    shared class MemberClassAliasToToplevel() = MemberClassAliasTricks_Foo;
    shared class MemberClassAliasToToplevel2() = MemberClassAliasToToplevel;

    shared class Member(){
        shared class MemberClassAliasToEnclosingMemberClass() = Member;

        void test(){
            value m1 = MemberClassAliasToEnclosingMemberClass();
        }
    }

    void test(){
        value m1 = MemberClassAliasToToplevel();
        value m2 = MemberClassAliasToToplevel2();
        value m3 = Member().MemberClassAliasToEnclosingMemberClass();
    }
}

@nomodel
void memberClassAliasTricksMethod(){
    value foo1 = MemberClassAliasTricks_Foo().MemberClassAliasToToplevel();
    value foo2 = MemberClassAliasTricks_Foo().MemberClassAliasToToplevel2();
}