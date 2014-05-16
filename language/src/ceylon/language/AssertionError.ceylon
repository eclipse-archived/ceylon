"An error that occurs due to an incorrectly writen program. 
 An instance is thrown when an assertion fails, that is, 
 when a condition in an `assert` statement evaluates to 
 false at runtime."
shared class AssertionError(String message)
        extends Error(message) {}