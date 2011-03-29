shared interface OpenCorrespondence<in U, V>
        satisfies Correspondence<U, V>
        given U satisfies Equality<U>
        given V satisfies Equality<V> {

    shared actual Settable<V?> value(U key);

    doc "Add the given entries, overriding any definitions that
         already exist."
    throws (UndefinedKeyException
            -> "if a value can not be defined for one of the
                given keys")
    shared default void define(Entry<U, V>... definitions) {
        for (U key->V value in definitions) {
            this[key] := value;
        }
    }

    doc "Assign a value to the given key. Return the previous value
         for the key, or |null| if there was no value defined."
    throws (UndefinedKeyException
            -> "if a value can not be defined for the given key")
    shared default V? define(U key -> V value) {
        Settable<V?> definition = this[key];
        V? result = definition;
        definition := value;
        return result;
    }

}