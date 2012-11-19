interface LocalInterfaceAlias_Toplevel{}

void localInterfaceAliasMethod(){
    interface InterfaceAlias_Foo{}
    // alias to local class
    interface InterfaceAlias1 => InterfaceAlias_Foo;
    // alias to toplevel class
    interface InterfaceAlias2 => LocalInterfaceAlias_Toplevel;
    
    class Class1() satisfies InterfaceAlias1 {}
    class Class2() satisfies InterfaceAlias2 {}
    value foo1 = Class1();
    value foo2 = Class2();
}