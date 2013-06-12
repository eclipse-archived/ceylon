"An exception that occurs when an assertion fails, that
 is, when a condition in an `assert` statement evaluates
 to false at runtime."
shared class AssertionException(String message)
        extends Exception(message) {}