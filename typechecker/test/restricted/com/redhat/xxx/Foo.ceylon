shared class Foo() {
    restricted(`module org.eclipse.yyy`) 
    shared void bar() {}
    restricted(`module`,`module org.eclipse.yyy`) 
    shared void baz() {}
    bar();
}