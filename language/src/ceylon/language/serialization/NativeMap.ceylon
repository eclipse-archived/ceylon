"Need a map-like thing, but can't use java.util.HashMap directly, not ceylon.collection::HashMap"
interface NativeMap<Key,Element> {
    shared formal Element? get(Key id);
    shared formal void put(Key id, Element instanceOrPartial);
    shared formal Boolean contains(Key id);
    shared formal {Key*} keys;
    shared formal {Element*} items;
    shared formal Integer size;
}
