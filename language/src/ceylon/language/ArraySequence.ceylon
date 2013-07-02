"An immutable `Sequence` implemented using the platform's 
 native array type. Where possible copying of the underlying 
 array is avoided."
//see (`SequenceBuilder`, `SequenceAppender`)
shared native class ArraySequence<out Element>({Element+} elements) 
        satisfies Sequence<Element> {
    
    shared native actual Element last;
    
    shared native actual Element first;
    
    shared native actual Integer size;  
    
    shared native actual Integer lastIndex;
    
    shared native actual Sequential<Element> rest; 
    
    shared native actual Integer count(Boolean(Element) selecting); 
    
    shared native actual Boolean contains(Object element);
    
    shared actual ArraySequence<Element> clone => this;
    
    shared native actual Element? get(Integer index);
    
    shared native actual Iterator<Element> iterator();
    
    shared native actual Boolean defines(Integer key);
    
    shared actual Boolean equals(Object other) => (super of List<Element>).equals(other);
    
    shared actual Integer hash => (super of List<Element>).hash;
    
    shared native actual ArraySequence<Element> reversed;

    shared native actual Sequential<Element> span(Integer from, Integer to);
    
    shared native actual Sequential<Element> spanFrom(Integer from);
    
    shared native actual Sequential<Element> spanTo(Integer to);
    
    shared native actual Sequential<Element> segment(Integer from, Integer length);
    
}