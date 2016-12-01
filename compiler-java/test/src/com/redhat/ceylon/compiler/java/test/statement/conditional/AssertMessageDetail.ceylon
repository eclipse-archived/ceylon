@noanno
void assertMessageDetailGeneric<Element>() {
    // is conditions
    Anything one = 1;
    try {
        assert(is String one);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\texpression has type ceylon.language::Integer which is not a subtype of ceylon.language::String"));
    }
    try {
        assert(is Element null);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\texpression has type ceylon.language::null which is not a subtype of ceylon.language::Integer"));
    }
    try {
        assert(!is Integer one);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\texpression has type ceylon.language::Integer"));
    }
    try {
        assert(!is Element one);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\texpression has type ceylon.language::Integer"));
    }
    try {
        assert(!is Integer one);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\texpression has type ceylon.language::Integer"));
    }
    
    // is expressions
    try {
        assert(one is String);
    } catch (AssertionError e) {
        print(e.message);
        assert(e.message.contains("\texpression has type ceylon.language::Integer which is not a subtype of ceylon.language::String"));
    }
    try {
        assert("" is Element);
    } catch (AssertionError e) {
        assert(e.message.contains("\texpression has type ceylon.language::String which is not a subtype of ceylon.language::Integer"));
    }
}
@noanno
void assertMessageDetail() {
    assertMessageDetailGeneric<Integer>();
}