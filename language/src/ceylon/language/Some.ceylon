shared interface Some<out Element> 
        satisfies FixedSized<Element> {
    shared actual default Element first {
        if (is Element first = iterator.next()) {
            return first;
        }
        else {
            throw;
        }
    }
}