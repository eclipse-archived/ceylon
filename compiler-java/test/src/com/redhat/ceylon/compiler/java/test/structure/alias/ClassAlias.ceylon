shared class ClassAlias_Foo(){}

shared class ClassAlias() = ClassAlias_Foo();

@nomodel
void classAliasMethod(){
    value foo = ClassAlias();
}