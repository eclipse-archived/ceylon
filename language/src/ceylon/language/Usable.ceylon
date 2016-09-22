"Abstract supertype of resources whose lifecyle may be
 managed by the `try` statement."
tagged("Basic types")
since("1.1.0")
shared interface Usable 
        of Destroyable | Obtainable {}