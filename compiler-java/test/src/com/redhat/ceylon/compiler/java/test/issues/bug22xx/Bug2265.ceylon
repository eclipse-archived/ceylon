class Bug2265() {
    variable String? foo = null;
    
    shared void myFunction(String param) {
        foo = switch(param)
        case ("foo") "foo"
        else "not foo";
    }
}
void bug2265() {
    Bug2265().myFunction("foo");
}