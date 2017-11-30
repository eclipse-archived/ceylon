class MissingActualAttributesSub<A>() 
        extends MissingActualAttributes<A>(){}
object missingActualAttributesSub 
        extends MissingActualAttributes<Integer>(){}

shared void missingActualAttributesUsage() {
    void isExpectedError(Throwable t) {
        assert(t.message.endsWith("not implemented in class hierarchy"));
    }
    for (c in {MissingActualAttributes<Integer>(), 
                MissingActualAttributesSub<Integer>(),
                missingActualAttributes,
                missingActualAttributesSub}) {
        print(c);
        try {
            print(c.simpleAttribute);
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
        try {
            print(c.tpAttributeA);
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
        try {
            print(c.tpAttributeB);
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
        try{
            c.variableTpAttributeA = 1;
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
        try {
            c.variableTpAttributeB = "";
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
    }
    print("OK");
}
