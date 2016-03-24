import ordering.foo {
    Foo
}
shared class Bar(){}

shared void foo(Foo<> f){}

shared void bar(){
    foo(Foo(Bar()));    
}
