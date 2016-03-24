shared void bug599f(String* s1) {}

@test
shared void bug599() {
    `bug599f`();
    `bug599f`.apply();
}
