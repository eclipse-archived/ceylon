@noanno
void assertNotIsException() {
    try {
        assert(!is ParseException float = Float.parse("brown"));
        assert(false);
    } catch (AssertionError e) {
        assert(is ParseException c=e.cause);
        assert("illegal format for Float: unexpected character 'b'"==c.message);
    }
    
    try {
        assert(!is ParseException float1 = Float.parse("brown"),
            !is ParseException float2 = Float.parse("1.0"));
    } catch (AssertionError e) {
        assert(is ParseException c=e.cause);
        assert("illegal format for Float: unexpected character 'b'"==c.message);
    }
    
    try {
        assert(!is ParseException float1 = Float.parse("1.0"),
                !is ParseException float2 = Float.parse("mauve"));
    } catch (AssertionError e) {
        assert(is ParseException c=e.cause);
        assert("illegal format for Float: unexpected character 'm'"==c.message);
    }
}