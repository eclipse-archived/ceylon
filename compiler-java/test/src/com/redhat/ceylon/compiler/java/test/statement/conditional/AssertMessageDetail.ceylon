@noanno
void assertMessageDetailGeneric<Element>() {
    Object one = 1;
    try {
        assert(is String one);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\texpression has type ceylon.language::Integer rather than ceylon.language::String"));
    }
    try {
        assert(is Element null);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\texpression has type ceylon.language::null rather than ceylon.language::Object"));
    }
}
@noanno
void assertMessageDetail() {
    assertMessageDetailGeneric<Object>();
}