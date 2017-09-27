String bug6253_intersection<T>() => `T&Object`.string;
String bug6253_union<T>() => `T|Object`.string;

shared void bug6253run() {
    assert("ceylon.language::String&ceylon.language::Object"==bug6253_intersection<String>());
    assert("ceylon.language::String|ceylon.language::Object"==bug6253_union<String>());
}