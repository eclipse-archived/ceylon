"Abstract supertype of resources whose lifecyle may be
 managed by the `try` statement."
tagged("Basic types")
shared interface Usable 
        of Destroyable | Obtainable {}