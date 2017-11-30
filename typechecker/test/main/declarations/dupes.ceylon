class ClassWithDupeMembers() {
    $error shared void fun() {}
    $error shared void fun(String string) {}
    String name = "";
    $error String name => "";
}

class SuperclassWithDupeMembers() {
    shared default void foo() {}
    $error shared default void bar() {}
    $error shared default void bar(String string) {}
}

class SubclassWithDupeMembers() 
        extends SuperclassWithDupeMembers() {
    $error shared actual void foo() {}
    $error shared actual void foo(String string) {}
    shared actual void bar() {}
    shared actual void bar(String string) {}
}