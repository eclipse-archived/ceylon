shared interface FixedSized<out Element> 
        of None<Element> | Some<Element>
        satisfies Collection<Element> {

    default doc "The first element. This should be the same value as
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