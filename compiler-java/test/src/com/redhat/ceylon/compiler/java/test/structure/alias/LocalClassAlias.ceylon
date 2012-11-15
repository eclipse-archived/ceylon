class LocalClassAlias_Toplevel(){}

void localClassAliasMethod(){
    class ClassAlias_Foo(){}
    // alias to local class
    class ClassAlias1() => ClassAlias_Foo();
    // alias to toplevel class
    class ClassAlias2() => LocalClassAlias_Toplevel();
    value foo1 = ClassAlias1();
    value foo2 = ClassAlias2();
}