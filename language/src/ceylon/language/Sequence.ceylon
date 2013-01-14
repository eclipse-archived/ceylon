doc "A nonempty, immutable sequence of values. A sequence of
     values may be formed using braces:
     
         value worlds = { \"hello\", \"world\" };
         value cubes = { for (n in 0..100) n**3 };
     
     The type `Sequential<Element>`, abbreviated `[Element*]`
     or `Element[]`, represents a possibly-empty sequence. 
     The `if (nonempty ...)` construct may be used to obtain 
     an instance of `Sequence<Element>`, abbreviated 
     `[Element+]` from a possibly-empty sequence:
     
         Integer[] nums = ... ;
         if (nonmpty nums) {
             Integer first = nums.first;
             Integer max = max(nums);
             [Integer+] squares = nums.collect((Integer i) i**2));
             [Integer+] sorted = nums.sort(byIncreasing((Integer i) i));
         }
     
     Operations like `first`, `max()`, `collect()`, and 
     `sort()`, which polymorphically produce a nonempty
     or non-null output when given a nonempty input are 
     called _emptiness-preserving_."
see (Empty)
by "Gavin"
shared native interface Sequence<out Element>
        satisfies Element[] & 
                  {Element+} &
                  Cloneable<[Element+]> {
    
    shared actual default Integer size=>List::size;
    shared actual default Iterator<Element> iterator =>
            List::iterator;
    shared actual default Boolean contains(Object element) =>
            Collection::contains(element);
    shared actual default Element? findLast(
            Boolean selecting(Element elem)) => 
                    List::findLast(selecting);
                
    doc "The index of the last element of the sequence."
    see (size)
    shared actual formal Integer lastIndex;
    
    doc "The first element of the sequence, that is, the
         element with index `0`."
    shared actual formal Element first;

    doc "The last element of the sequence, that is, the
         element with index `sequence.lastIndex`."
    shared actual formal Element last;
    
    doc "The rest of the sequence, without the first 
         element."
    shared actual formal Element[] rest;
        
    doc "Returns `false`, since every `Some` contains at
         least one element."
    shared actual Boolean empty => false;
    
    doc "Reverse this sequence, returning a new nonempty
         sequence."
    shared actual formal [Element+] reversed;
    
    doc "This sequence."
    shared default actual [Element+] sequence => this;
    
    doc "A nonempty sequence containing the elements of this
         container, sorted according to a function 
         imposing a partial order upon the elements."
    shared default actual native [Element+] sort(
            doc "The function comparing pairs of elements."
            Comparison? comparing(Element x, Element y));

    doc "A nonempty sequence containing the results of 
         applying the given mapping to the elements of this
         sequence."
    shared default actual [Result+] collect<Result>(
            doc "The transformation applied to the elements."
            Result collecting(Element element)) {
        value s = map(collecting).sequence;
        assert (nonempty s);
        return s;
    }

    /*shared actual formal Element[] span(Integer from,
                                        Integer? to);
                                        
    shared actual formal Element[] segment(Integer from,
                                           Integer length);*/
                                           
    /*shared formal [Value+] append<Value>(Value* elements)
            given Value abstracts Element;
    
    shared formal [Value+] prepend<Value>(Value* elements)
            given Value abstracts Element;*/

}
