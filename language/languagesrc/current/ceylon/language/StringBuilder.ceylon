shared class StringBuilder() {
    shared actual String string { throw; }
    shared void append(String string) { throw; }
    shared void appendAll(String... strings) { throw; }
    shared void appendCharacter(Character character) { throw; }
}