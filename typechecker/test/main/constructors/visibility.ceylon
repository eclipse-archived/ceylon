interface Local {}

shared class WithBrokenVisibility {
    shared new WithBrokenVisibility(@error Local param) {}
    shared new BrokenConstructor(@error Local param) {}
    new OkConstructor(Local param) {}
}