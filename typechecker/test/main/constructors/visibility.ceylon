interface Local {}

shared class WithBrokenVisibility {
    shared new withBrokenVisibility(@error Local param) {}
    shared new brokenConstructor(@error Local param) {}
    new okConstructor(Local param) {}
}

class AOrB of a|b {
    new a {}
    shared new b {}
}

shared void switchAOrB() {
    AOrB f = nothing;
    switch (f)
    case (AOrB.b) {
        @type:"AOrB" value ff = f;
    }
    else {
        @type:"AOrB" value ff = f;
    }
}