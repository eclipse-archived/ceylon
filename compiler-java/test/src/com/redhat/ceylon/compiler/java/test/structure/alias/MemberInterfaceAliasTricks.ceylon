shared interface MemberInterfaceAliasTricks_Foo{
    
    shared class MemberClass(){}

    shared interface MemberInterface {
        shared class MemberClassAliasToEnclosingMemberClass() = MemberClass();

        void test(){
            value m1 = MemberClassAliasToEnclosingMemberClass();
        }
    }

    void test(MemberInterface x){
        value m1 = x.MemberClassAliasToEnclosingMemberClass();
    }
}

shared class MemberInterfaceAliasTricksImpl() satisfies MemberInterfaceAliasTricks_Foo {
    shared class MemberInterfaceImpl() satisfies MemberInterface {}
}
shared interface A {
    shared class Foo(){}
}
shared class AImpl() satisfies A {}