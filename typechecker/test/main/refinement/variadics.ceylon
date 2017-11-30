shared interface XW {
    shared formal String concat1(String* args);
    shared formal String concat2([String*] args);
}
shared class YW() satisfies XW {
    shared actual String concat1($error [String*] args) => args[0] else "";
    shared actual String concat2($error String* args) => args[0] else "";
}
shared class ZW() satisfies XW {
    concat1($error [String*] args) => "";
    concat2($error String* args) => "";
}