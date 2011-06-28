shared abstract class Entry<out Key, out Element>(Key key, Element element)
        extends Object()
        satisfies Equality
        given Key satisfies Equality
        given Element satisfies Equality {
    
    doc "The key used to access the entry."
    shared Key key = key;
    
    doc "The value associated with the key."
    shared Element element = element;
    
    shared actual Integer hash = key.hash/2 + element.hash/2; //TODO: really should be xor
    
    shared actual Boolean equals(Equality that) {
        if (is Entry<Equality,Equality> that) {
            return this.key==that.key && this.element==that.element;
        }
        else {
            return false;
        }
    }
    
}