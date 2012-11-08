shared class ClassAliasWithParameters_Foo(Integer i, String s = "foo"){}

// FIXME: are you allowed to redefine defaults?
shared class ClassAliasWithParameters(Integer i, String s) = ClassAliasWithParameters_Foo(i,s);

@nomodel
void classAliasWithParametersMethod(){
    value foo = ClassAliasWithParameters(1, "a");
}