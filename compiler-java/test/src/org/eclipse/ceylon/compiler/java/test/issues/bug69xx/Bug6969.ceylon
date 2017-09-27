shared void bug6969() {
    value ceylonClass = true then `Integer` else `String`;
    Bug6969Java.foo(ceylonClass);
}