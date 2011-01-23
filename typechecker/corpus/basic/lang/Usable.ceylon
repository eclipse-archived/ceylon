shared interface Usable {

    doc "Called before entry into a |try| block."
    shared formal void begin();
    
    doc "Called before normal exit from a |try| block."
    shared formal void end();

    doc "Called before exit from a |try| block when an
         exception occurs."
    shared default void fail(Exception e) {
        end();
    }
    
}