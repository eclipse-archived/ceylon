import static {
    Map {
        $error Entry
    },
    Int {
        parse
    }
}


interface Map<U,V> {
    shared static class Entry(shared U key, shared V item) {}
}

class Int {
    shared static Int parse(String string) => Int(string.size);
    shared new (Integer i) {}
}