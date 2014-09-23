import ceylon.language.meta.declaration { TypeParameter }

native class TpMap<out V>(shared dynamic m, shared dynamic ord)
        satisfies Map<TypeParameter,V>
        given V satisfies Object {
    
    shared actual Boolean equals(Object other) => (super of Map<TypeParameter,V>).equals(other);
    shared actual Integer hash => (super of Map<TypeParameter,V>).hash;
    shared actual TpMap<V> clone() => this;
    shared native actual Iterator<TypeParameter->V> iterator() {
        native object miter satisfies Iterator<TypeParameter->V> {
            shared variable Integer idx=-1;
            shared actual native <TypeParameter->V>|Finished next();
        }
        return miter;
    }
    shared actual native V get(Object k);
    shared actual native Boolean defines(Object key);
}
