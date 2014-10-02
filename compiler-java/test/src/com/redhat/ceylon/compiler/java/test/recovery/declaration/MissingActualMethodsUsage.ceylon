class MissingActualMethodsSub<A>() 
        extends MissingActualMethods<A>(){}
object missingActualMethodsSub 
        extends MissingActualMethods<Integer>(){}

shared void missingActualMethodsUsage() {
    void isExpectedError(Throwable t) {
        assert(t.message.endsWith("not implemented in class hierarchy"));
    }
    for (c in {MissingActualMethods<Integer>(), 
                MissingActualMethodsSub<Integer>(), 
                missingActualMethods,
                missingActualMethodsSub}) {
        print(c);
        
        try {
            c.simpleMethodNullary();
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
        try {
            c.tpMethodANullary();
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
        try {
            c.tpMethodBNullary();
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
        try {
            c.tpMethodCNullary<Boolean>();
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
        try {
            c.simpleMethodUnary("");
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
        try {
            c.sequencedMethodUnary("", "");
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
        try {
            c.sequencedMethodUnary("");
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
        try {
            c.sequencedMethodUnary();
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
        try {
            c.nonEmptySequencedMethodUnary("", "");
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
        try {
            c.nonEmptySequencedMethodUnary("");
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
        try {
            c.tpMethodAUnary(1);
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
        try {
            c.tpMethodBUnary("");
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
        try {
            c.tpMethodCUnary<Boolean>(true);
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
        try {
            c.methodMpl<Boolean>(1);
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
        try {
            c.simpleMethodFunctional(void(String s) => s.initial(0));
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
        try {
            c.tpMethodFunctionalA(function(Integer s) => "");
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
        try {
            c.tpMethodFunctionalB(function(String s) => 1);
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
        try {
            c.tpMethodFunctionalC<Boolean>(function(Boolean s) => "");
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
    }
    print("OK");
}
