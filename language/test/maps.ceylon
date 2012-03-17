import java.util { JHashMap = HashMap }

// tests for Map interface
// Not much functionality to test here, as concrete interface members are not
// supported yet. But we can test if all members can be implemented, everything
// compiles, types are correct, etc.

interface MapTestBase<out Key, out Item> satisfies Map<Key, Item>
            given Key satisfies Object
            given Item satisfies Object {
    shared formal Object underlyingMap;
}

class MapTest<Key, Item>(Key->Item... entries)
            satisfies MapTestBase<Key, Item>
            given Key satisfies Object
            given Item satisfies Object {
    value jmap = JHashMap<Key, Item>();
    for (e in entries) { jmap.put(e.key, e.item); }
    shared actual Object underlyingMap = jmap;
    shared actual Boolean equals(Object other) {
        if (is MapTestBase<Object, Object> other) {
            return jmap.equals(other.underlyingMap);
        }
        return false;
    }
    shared actual Integer hash { return jmap.hash; }
    shared actual Integer size { return jmap.size(); }
    shared actual Boolean empty { return jmap.empty; }
    shared actual MapTest<Key, Item> clone {
        value copy = MapTest<Key, Item>();
        copy.jmap.putAll(jmap);
        return copy;
    }
    shared actual Iterator<Key->Item> iterator {
        object iter satisfies Iterator<Key->Item> {
            value jit = jmap.entrySet().iterator();
            shared actual Key->Item|Finished next() {
                if (jit.hasNext()) {
                    value e = jit.next();
                    return e.key->e.\ivalue;
                }
                return exhausted;
            }
        }
        return iter;
    }
    shared actual Item? item(Object key) { return jmap.get(key); }
    shared actual Item?[] items(Object... keys) {
        value sb = SequenceBuilder<Item?>();
        for (k in keys) { sb.append(jmap.get(k)); }
        return sb.sequence;
    }
    shared actual Boolean defines(Object key) { return jmap.containsKey(key); }
    shared actual Boolean definesAny(Object... keys) {
        for (k in keys) {
            if (jmap.containsKey(k)) { return true; }
        }
        return false;
    }
    shared actual Boolean definesEvery(Object... keys) {
        for (k in keys) {
            if (!jmap.containsKey(k)) { return false; }
        }
        return true;
    }
    shared actual Boolean contains(Object element) {
        if (is Object->Object element) {
            Item? it = jmap.get(element.key);
            if (exists it) { return it == element.item; }
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