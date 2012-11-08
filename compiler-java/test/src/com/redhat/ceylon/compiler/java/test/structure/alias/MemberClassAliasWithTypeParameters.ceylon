shared class MemberClassAliasWithTypeParameters_Foo<C>(C c){
    shared class Member<T>(T t){}
    
    shared class MemberClassAlias1<S>(S s) = Member<S>(s);
    shared class MemberClassAlias2(Integer s) = Member<Integer>(s);
    shared class MemberClassAlias3(C s) = Member<C>(s);

    void test(){
        value m1 = MemberClassAlias1<Integer>(1);
        value m2 = MemberClassAlias2(1);
        value m3 = MemberClassAlias3(c);
    }
}

@nomodel
void memberClassAliasWithTypeParametersMethod(){
    value foo1 = MemberClassAliasWithTypeParameters_Foo<Integer>(1).MemberClassAlias1<Integer>(1);
    value foo2 = MemberClassAliasWithTypeParameters_Foo<Integer>(1).MemberClassAlias2(1);
    value foo3 = MemberClassAliasWithTypeParameters_Foo<Integer>(1).MemberClassAlias3(1);
}