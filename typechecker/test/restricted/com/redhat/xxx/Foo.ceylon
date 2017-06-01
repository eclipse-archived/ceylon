shared class Foo() {
    restricted(`module com.redhat.yyy`) 
    shared void bar() {}
    restricted(`module`,`module com.redhat.yyy`) 
    shared void baz() {}
    bar();
}