shared void missingActualClassesUsage() {
    void isExpectedError(Throwable t) {
        assert(t.message.endsWith("not implemented in class hierarchy"));
    }
    for (c in {MissingActualClasses<Integer>(), missingActualClasses}) {
        print(c);
        
        try {
            c.ISimpleClassNullary();
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
        try {
            c.CSimpleClassNullary();
            throw;
        } catch (Throwable e) {
            isExpectedError(e);
        }
    }
    print("OK");
}
