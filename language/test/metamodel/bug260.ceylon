@test
shared void bug260(){
    class Foo(shared String str) {}
    assert(`Foo.str`.type.string == "ceylon.language::String");
    assert(`Foo`.string == "metamodel::Foo");
    assert(`class Foo`.openType.string == "metamodel::bug260.Foo");
}
