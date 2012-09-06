@nomodel
void memberClassAliasFromModelLoader(){
    value a = MemberClassAlias_Foo().MemberClassAlias();
    value b = MemberClassAliasWithParameters_Foo().MemberClassAlias(1, "a");
    value c = MemberClassAliasWithTypeParameters_Foo<Integer>(1).MemberClassAlias1<Integer>(1);
    value d = MemberClassAliasWithTypeParameters_Foo<Integer>(1).MemberClassAlias2(1);
    value e = MemberClassAliasWithTypeParameters_Foo<Integer>(1).MemberClassAlias3(1);
}