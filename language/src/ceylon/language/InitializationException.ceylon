"Thrown when a value reference could not be initialized, 
 including when:
 
 - a toplevel value could not be initialized due to 
   recursive dependencies upon other toplevel values, 
 - an uninitialized `late` value is evaluated, 
 - an initialized `late` but non-`variable` value is
   reassigned."
see (`function late`)
shared class InitializationException(String description)
        extends Exception(description, null) {}