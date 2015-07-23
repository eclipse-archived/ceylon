shared sealed interface Gettable<out Get=Anything> {
    
    "Reads the current value for this value binding. Note that in the case of getter
     values, this can throw if the getter throws."
    throws(`class StorageException`,
        "If this attribute is not stored at runtime, for example if it is neither shared nor captured.")
    shared formal Get get();
}