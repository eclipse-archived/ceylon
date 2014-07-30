"Abstract supertype of resources whose lifecyle may be
 managed by the `try` statement."
shared interface Usable 
        of Destroyable | Obtainable {}