shared class ClassAliasWithTypeParameters_Foo<T>(T t){}

shared class ClassAliasWithTypeParameters<T>(T t) = ClassAliasWithTypeParameters_Foo<T>(t);
shared class ClassAliasWithTypeParameters2(Integer t) = ClassAliasWithTypeParameters_Foo<Integer>(t);

@nomodel
void classAliasWithTypeParametersMethod(){
    value foo = ClassAliasWithTypeParameters<Integer>(1);
    value foo2 = ClassAliasWithTypeParameters2(1);
}