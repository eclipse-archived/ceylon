"A nonempty, immutable sequence of values. The type 
 `Sequence<Element>`, may be abbreviated `[Element+]`.
 
 Given a possibly-empty sequence of type `[Element*]`, 
 the `if (nonempty ...)` construct, or, alternatively,
 the `assert (nonempty ...)` construct, may be used to 
 narrow to a nonempty sequence type:
 
     [Integer*] nums = ... ;
     if (nonempty nums) {
         Integer first = nums.first;
         Integer max = max(nums);
         [Integer+] squares = nums.collect((Integer i) => i**2));
         [Integer+] sorted = nums.sort(byIncreasing((Integer i) => i));
     }
 
 Operations like `first`, `max()`, `collect()`, and 
 `sort()`, which polymorphically produce a nonempty or 
 non-null output when given a nonempty input are called 
 _emptiness-preserving_."
see (`interface Empty`)
by ("Gavin")
shared interface Sequence<out Element>
        satisfies Element[] & 
                  {Element+} &
                  Cloneable<[Element+]> {
    
    "The index of the last element of the sequence."
    see (`value Sequence.size`)
    shared actual formal Integer lastIndex;
    
    "The first element of the sequence, that is, the
         element with index `0`."
    shared actual formal Element first;

    "The last element of the sequence, that is, the
         element with index `sequence.lastIndex`."
    shared actual formal Element last;
    
    "Returns `false`, since every `Sequence` contains at
         least one element."
    shared actual Boolean empty => false;
    
    "Reverse this sequence, returning a new nonempty
         sequence."
    shared actual formal [Element+] reversed;
    
    "This sequence."
    shared default actual [Element+] sequence => this;
    
    "The rest of the sequence, without the first 
         element."
    shared actual formal Element[] rest;
    
    "A nonempty sequence containing the elements of this
     container, sorted according to a function 
     imposing a partial order upon the elements."
    shared default actual [Element+] sort(
            "The function comparing pairs of elements."
            Comparison comparing(Element x, Element y)) {
        value s = internalSort(comparing, this);
        //TODO: fix internalSort() and remove this assertion
        assert (nonempty s);
        return s;
    }

    "A nonempty sequence containing the results of 
     applying the given mapping to the elements of this
     sequence."
    shared default actual [Result+] collect<Result>(
            "The transformation applied to the elements."
            Result collecting(Element element)) {
        value s = map(collecting).sequence;
        assert (nonempty s);
        return s;
    }
    
    shared actual default [Element+] clone => this;
    
    shared actual default String string => (super of Sequential<Element>).string;
    
    shared actual default Boolean shorterThan(Integer length) 
            => (super of List<Element>).shorterThan(length);
    
    shared actual default Boolean longerThan(Integer length) 
            => (super of List<Element>).longerThan(length);
    
    shared default actual Element? findLast(
            Boolean selecting(Element elem))
            => (super of List<Element>).findLast(selecting);
    
    shared actual default Element[] repeat(Integer times)
            => (super of Element[]).repeat(times);
    
}
