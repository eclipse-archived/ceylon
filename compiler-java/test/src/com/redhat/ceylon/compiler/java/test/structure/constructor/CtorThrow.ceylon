@nomodel
class CtorThrow {
    checker.note("1");
    shared new () {
        checker.note("2");
    }
    shared new throws() {
        checker.note("2");
        throw Exception();
    }
    shared new throwIfTrue() {
        if (true) {
            checker.note("2");
            throw;
        }
    }
    shared new throwIfFalse() {
        if (false) {
            throw;
        }
        checker.note("2");
    }
    shared new throwWhileTrue() {
        while (true) {
            checker.note("2");
            throw;
        }
    }
    shared new throwWhileFalse() {
        while (false) {
            throw;
        }
        checker.note("2");
    }
    shared new assertFalse() {
        checker.note("2");
        assert(false);
    }
    shared new assertFalseIfTrue() {
        if (true) {
            checker.note("2");
            assert(false);
        }
    }
    shared new assertFalseIfFalse() {
        if (false) {
            assert(false);
        }
        checker.note("2");
    }
    shared new assertFalseFor() {
        for (x in [1]) {
            checker.note("2");
            assert(false);
        }
    }
    checker.note("3");
}
void ctorThrow() {
    try {
        checker.reset();
        CtorThrow();
    } catch (Throwable t) {
        
    } finally {
        checker.check("[1, 2, 3]");
    }
    
    try {
        checker.reset();
        CtorThrow.throws();
    } catch (Throwable t) {
        
    } finally {
        checker.check("[1, 2]");
    }
    
    try {
        checker.reset();
        CtorThrow.throwIfTrue();
    } catch (Throwable t) {
        
    } finally {
        checker.check("[1, 2]");
    }
    
    try {
        checker.reset();
        CtorThrow.throwIfFalse();
    } catch (Throwable t) {
        
    } finally {
        checker.check("[1, 2, 3]");
    }
    
    try {
        checker.reset();
        CtorThrow.throwWhileTrue();
    } catch (Throwable t) {
        
    } finally {
        checker.check("[1, 2]");
    }
    
    try {
        checker.reset();
        CtorThrow.throwWhileFalse();
    } catch (Throwable t) {
        
    } finally {
        checker.check("[1, 2, 3]");
    }
    
    try {
        checker.reset();
        CtorThrow.assertFalse();
    } catch (Throwable t) {
        
    } finally {
        checker.check("[1, 2]");
    }
    
    try {
        checker.reset();
        CtorThrow.assertFalseIfTrue();
    } catch (Throwable t) {
        
    } finally {
        checker.check("[1, 2]");
    }
    
    try {
        checker.reset();
        CtorThrow.assertFalseIfFalse();
    } catch (Throwable t) {
        
    } finally {
        checker.check("[1, 2, 3]");
    }
    
    try {
        checker.reset();
        CtorThrow.assertFalseFor();
    } catch (Throwable t) {
        
    } finally {
        checker.check("[1, 2]");
    }
}