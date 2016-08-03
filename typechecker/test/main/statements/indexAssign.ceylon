
class DummyCorrespondence<Key, Item>()
    satisfies Correspondence<Key, Item> &
        CorrespondenceMutator<Key, Item> &
        Ranged<Key,Item,DummyCorrespondence<Key, Item>>
    given Key satisfies Object {
    
    defines(Key key) => false;
    get(Key key) => null;
    
    shared actual void set(Key key, Item item) {}
    
    span(Key from, Key to) => nothing;
    spanFrom(Key from) => nothing;
    spanTo(Key to) => nothing;
    measure(Key from, Integer length) => nothing;
    iterator() => nothing;
}

void test(DummyCorrespondence<Integer, String> dc) {
    dc[3] = "baz";
    @error dc[1..3] = "baz";
}
