shared interface None<out Element> 
        satisfies FixedSized<Element> {
    shared actual Nothing first {
        return null;
    }
}