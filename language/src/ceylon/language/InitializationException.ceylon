"Thrown when a problem was detected with value initialization.
 
 Possible problems include:
 
 * when a value could not be initialized due to recursive access during initialization, 
 * an attempt to use a `late` value before it was initialized, 
 * an attempt to assign to a `late` but non-`variable` value after it was initialized."
see (`late`)
shared class InitializationException(String description)
        extends Exception(description, null) {}