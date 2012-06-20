class CorrespondenceImpl<Key,Item>() satisfies Correspondence<Key,Item> 
    given Key satisfies Object
    given Item satisfies Object {
    shared actual Item? item(Key key) {
        return bottom;
    }
}