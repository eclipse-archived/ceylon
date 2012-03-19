doc "Represents a fixed-size collection which may or may not be empty."
shared interface FixedSized<out Element> 
        of None<Element> | Some<Element>
        satisfies Collection<Element> {

    doc "The first element. This should be the same value as
         `ordered.iterator.head`."
    shared default Element? first {
        if (is Element first = iterator.next()) {
            return first;
        }
        else {
            return null;
        }
    }
    
}