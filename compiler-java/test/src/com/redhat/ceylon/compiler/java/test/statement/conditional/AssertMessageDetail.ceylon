@noanno
void assertMessageDetailGeneric<Element>(Element zero) {
    // is conditions
    Anything one = 1;
    try {
        assert(is String one);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\t\texpression has type ceylon.language::Integer which is not a subtype of ceylon.language::String"));
    }
    try {
        assert(is Element null);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\t\texpression has type ceylon.language::null which is not a subtype of ceylon.language::Integer"));
    }
    try {
        assert(!is Integer one);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\t\texpression has type ceylon.language::Integer"));
    }
    try {
        assert(!is Element one);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\t\texpression has type ceylon.language::Integer"));
    }
    try {
        assert(!is Integer one);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\t\texpression has type ceylon.language::Integer"));
    }
    
    // is expressions
    try {
        assert(one is String);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\t\texpression has type ceylon.language::Integer which is not a subtype of ceylon.language::String"));
    }
    try {
        assert("" is Element);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\t\texpression has type ceylon.language::String which is not a subtype of ceylon.language::Integer"));
    }
    
    assert(is Object zero);
    
    //equals
    try {
        assert("foo" == "bar");
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\t\tleft-hand expression is foo"));
        assert(e.message.contains("\t\tright-hand expression is bar"));
    }
    
    try {
        assert("foo" == zero);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\t\tleft-hand expression is foo"));
        assert(e.message.contains("\t\tright-hand expression is 0"));
        assert(e.message.contains("\t\tleft-hand expression has type ceylon.language::String"));
        assert(e.message.contains("\t\tright-hand expression has type ceylon.language::Integer"));
    }
    try {
        assert(zero == 1);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\t\tleft-hand expression is 0"));
        assert(e.message.contains("\t\tright-hand expression is 1"));
    }
    try {
        assert(1 == zero);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\t\tleft-hand expression is 1"));
        assert(e.message.contains("\t\tright-hand expression is 0"));
    }
    
    // !=
    try {
        assert(1 != 1);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\t\tleft-hand expression is 1"));
        assert(e.message.contains("\t\tright-hand expression is 1"));
    }
    try {
        assert(!(1 == 1));
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\t\tleft-hand expression is 1"));
        assert(e.message.contains("\t\tright-hand expression is 1"));
    }
    try {
        assert(!(1 != 0));
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\t\tleft-hand expression is 1"));
        assert(e.message.contains("\t\tright-hand expression is 0"));
    }
    
    // TODO ee-mode boxes
    try {
        small value smallInt = 0;
        assert(1 == smallInt);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\t\tleft-hand expression is 1"));
        assert(e.message.contains("\t\tright-hand expression is 0"));
    }
    
    try {
        Integer? x = 0;
        Integer|Float y = 1;
        assert(exists x, is Integer y, x==y);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\t\tleft-hand expression is 0"));
        assert(e.message.contains("\t\tright-hand expression is 1"));
    }
    
    // comparisons
    try {
        assert(1<0);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\t\tleft-hand expression is 1"));
        assert(e.message.contains("\t\tright-hand expression is 0"));
    }
    try {
        assert(1<=0);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\t\tleft-hand expression is 1"));
        assert(e.message.contains("\t\tright-hand expression is 0"));
    }
    try {
        assert(0>1);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\t\tleft-hand expression is 0"));
        assert(e.message.contains("\t\tright-hand expression is 1"));
    }
    try {
        assert(0>=1);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\t\tleft-hand expression is 0"));
        assert(e.message.contains("\t\tright-hand expression is 1"));
    }
    
    // within
    try {
        assert(1<2<2);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\t\tmiddle expression is 2"));
        assert(e.message.contains("\t\tright-hand expression is 2"));
    }
    
    try {
        assert(1<1<2);
        throw;
    } catch (AssertionError e) {
        assert(e.message.contains("\t\tleft-hand expression is 1"));
        assert(e.message.contains("\t\tmiddle expression is 1"));
    }
}
@noanno
void assertMessageDetail() {
    assertMessageDetailGeneric<Integer>(0);
}