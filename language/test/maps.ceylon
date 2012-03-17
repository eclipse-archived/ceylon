// tests for Map interface
// Not much functionality to test here, as concrete interface members are not
// supported yet. But we can test if all members can be implemented, everything
// compiles, types are correct, etc.

interface MapTestBase<out Key, out Item> satisfies Map<Key, Item>
            given Key satisfies Object
            given Item satisfies Object {
    shared formal Entry<Key, Item>[] entries;
}

class MapTest<Key, Item>(Key->Item... entries)
            satisfies MapTestBase<Key, Item>
            given Key satisfies Object
            given Item satisfies Object {
    shared actual Entry<Key, Item>[] entries = entries;
    shared actual Boolean equals(Object other) {
        if (is MapTestBase<Object, Object> other) {
            return other.entries == entries;
        }
        return false;
    }
    shared actual Integer hash { return entries.hash; }
    shared actual Integer size { return entries.size; }
    shared actual Boolean empty { return entries.empty; }
    shared actual MapTest<Key, Item> clone { return this; }
    shared actual Iterator<Key->Item> iterator { return entries.iterator; }
    shared actual Item? item(Object key) {
        for (e in entries) {
            if (e.key == key) { return e.item; }
        }
        return null;
    }
    shared actual Item?[] items(Object... keys) {
        value sb = SequenceBuilder<Item?>();
        for (k in keys) { sb.append(item(k)); }
        return sb.sequence;
    }
    shared actual Boolean defines(Object key) {
        for (e in entries) {
            if (e.key == key) { return true; }
        }
        return false;
    }
    shared actual Boolean definesAny(Object... keys) {
        for (k in keys) {
            if (defines(k)) { return true; }
        }
        return false;
    }
    shared actual Boolean definesEvery(Object... keys) {
        for (k in keys) {
            if (!defines(k)) { return false; }
        }
        return true;
    }
    shared actual Boolean contains(Object element) {
        if (is Object->Object element) {
            if (exists it = item(element.key)) { return it == element.item; }
        }
        return false;
    }
    shared actual Boolean containsAny(Object... element) {
        for (e in element) {
            if (contains(e)) { return true; }
        }
        return false;
    }
    shared actual Boolean containsEvery(Object... element) {
        for (e in element) {
            if (!contains(e)) { return false; }
        }
        return true;
    }
    shared actual Integer count(Object element) {
        return contains(element) then 1 else 0;
    }
    shared actual Set<Key> keys {
        return bottom; //TODO
    }
    shared actual Collection<Item> values {
        return bottom; //TODO
    }
    shared actual Map<Item, Set<Key>> inverse {
        return bottom; //TODO
    }
}

void testMaps() {
    //TODO
}