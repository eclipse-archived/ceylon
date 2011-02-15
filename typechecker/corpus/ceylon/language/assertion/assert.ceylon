doc "Assert that the block evaluates to true. The block
     is executed only when assertions are enabled. If
     the block evaluates to false, throw an
     |AssertionException| with the given message."
shared void assert(Gettable<String> message, Boolean that()) {
    Boolean callerAssertionsEnabled  = ... ;
    if ( assertionsEnabled && !that() ) {
        throw AssertionException(message);
    }
}