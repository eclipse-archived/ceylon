"An immutable [[Sequence]] implemented using an [[Array]]. 
 Where possible, copying of the underlying array is avoided."
see (`class SequenceBuilder`, `class SequenceAppender`)
//TODO: Reimplement this class in Ceylon!
shared final native class ArraySequence<out Element>({Element+} elements) 
        satisfies [Element+] {
    
    shared native actual Element last;
    
    shared native actual Element first;
    
    shared native actual Integer size;
    
    shared native actual Integer lastIndex;
    
    shared native actual Element[] rest; 
    
    shared native actual Integer count(Boolean selecting(Element element)); 
    
    shared native actual Boolean contains(Object element);
    
    shared actual ArraySequence<Element> clone() => this;
    
    shared native actual Element? get(Integer index);
    
    shared native actual Iterator<Element> iterator();
    
    shared native actual Boolean defines(Integer key);
    
    shared actual Boolean equals(Object that) 
            => (super of List<Element>).equals(that);
    
    shared actual Integer hash 
            => (super of List<Element>).hash;
    
    shared native actual ArraySequence<Element> reversed;

    shared native actual Element[] span(Integer from, Integer to);
    
    shared native actual Element[] spanFrom(Integer from);
    
    shared native actual Element[] spanTo(Integer to);
    
    shared native actual Element[] segment(Integer from, Integer length);
    
}