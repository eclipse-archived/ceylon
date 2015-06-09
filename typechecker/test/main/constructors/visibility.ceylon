interface Local {}

shared class WithBrokenVisibility {
    shared new withBrokenVisibility(@error Local param) {}
    shared new brokenConstructor(@error Local param) {}
    new okConstructor(Local param) {}
}