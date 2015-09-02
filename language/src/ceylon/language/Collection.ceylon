"Represents an iterable collection of elements of finite 
 size, with a well-defined notion of value equality. 
 `Collection` is the abstract supertype of [[List]], 
 [[Map]], and [[Set]].
 
 A `Collection` forms a [[Category]] of its elements, and 
 is [[Iterable]]. The elements of a collection are not
 necessarily distinct when compared using [[Object.equals]].
 
 A `Collection` may be [[cloned|clone]]. If a collection is
 immutable, it is acceptable that `clone()` produce a
 reference to the collection itself. If a collection is
 mutable, `clone()` should produce a collection containing 
 references to the same elements, with the same structure as 
 the original collection&mdash;that is, it should produce a 
 shallow copy of the collection.
 
 All `Collection`s are required to support a well-defined
 notion of [[value equality|Object.equals]], but the
 definition of equality depends upon the kind of collection.
 Equality for `Map`s and `Set`s has a quite different
 definition to equality for `List`s. Instances of two 
 different kinds of collection are never equal&mdash;for
 example, a `Map` is never equal to a `List`."
see (`interface List`, `interface Map`, `interface Set`)
shared interface Collection<out Element=Anything>
        satisfies {Element*} {
    
    "A shallow copy of this collection, that is, a 
     collection with identical elements which does not
     change if this collection changes. If this collection
     is immutable, it is acceptable to return a reference to
     this collection. If this collection is mutable, a newly
     instantiated collection must be returned."
    shared formal Collection<Element> clone();
    
    "Determine if the collection is empty, that is, if it 
     has no elements."
    shared actual default Boolean empty => size==0;
    
    "Return `true` if the given object is an element of
     this collection. In this default implementation, and in 
     most refining implementations, return `false` 
     otherwise. An acceptable refining implementation may 
     return `true` for objects which are not elements of the 
     collection, but this is not recommended. (For example, 
     the `contains()` method of `String` returns `true` for 
     any substring of the string.)"
    shared actual default Boolean contains(Object element) {
        for (elem in this) {
            if (exists elem, elem==element) {
                return true;
            }
        }
        else {
            return false;
        }
    }
    
    "A string of form `\"{ x, y, z }\"` where `x`, `y`, and 
     `z` are the `string` representations of the elements of 
     this collection, as produced by the iterator of the 
     collection, or the string `\"{}\"` if this collection 
     is empty. If the collection iterator produces the value 
     `null`, the string representation contains the string 
     `\"<null>\"`."
    shared actual default String string
            => empty then "{}" else "{ ``commaList(this)`` }";
    
    "The permutations of this collection, as a stream of
     nonempty [[sequences|Sequence]]. That is, a stream
     producing every distinct ordering of the elements of
     this collection.
     
     For example,
     
         \"ABC\".permutations.map(String)
     
     is the stream of strings
     `{ \"ABC\", \"ACB\", \"BAC\", \"BCA\", \"CAB\", \"CBA\" }`.
     
     If this collection is empty, the resulting stream is
     empty.
     
     The permutations are enumerated lexicographically
     according to the order in which each
     [[distinct|Object.equals]] element of this collection
     is first produced by the iterator. No permutation is
     repeated. [[Null]] elements are treated as equal to
     each other and distinct from any [[Object]]."
    shared {[Element+]*} permutations => object satisfies {[Element+]*} {
        value multiset =
            outer
            .indexed
            .group(forItem((Element element) => element else nullElement))
            .items
            .sort(
                byIncreasing(
                    compose(
                        Entry<Integer, Element>.key,
                        Sequence<Integer->Element>.first
                    )
                )
            )
            .indexed
            .flatMap(
                (entry) => let (index->entries = entry)
                    entries.map((entry) => index->entry.item)
            );
        
        empty => multiset.empty;
        
        iterator() => object satisfies Iterator<[Element+]> {
            value elements = Array(multiset);
            value reversed
                = zipPairs(elements.keys.reversed, elements.reversed);
            value paired = reversed.paired;
            function greaterThan(Integer key)([Integer, Integer->Element] pair)
                => pair[1].key > key;
            function adjacentDecreasing([Integer, Integer->Element][2] pairs)
                => greaterThan(pairs[1][1].key)(pairs[0]);
            variable value initial = true;
            
            shared actual [Element+]|Finished next() {
                if (initial) {
                    initial = false;
                }
                else if (exists pairs = paired.find(adjacentDecreasing)) {
                    value [k, entry] = pairs[1];
                    value key = entry.key;
                    assert (exists pair = reversed.find(greaterThan(key)));
                    elements.set(k, pair[1]);
                    elements.set(pair[0], entry);
                    value from = k + 1;
                    value to = k + (elements.size - from) / 2;
                    value rest
                        = zipPairs(from..to, elements.sublist(from, to));
                    for ([i, j] in zipPairs(rest, reversed)) {
                        elements.set(i[0], j[1]);
                        elements.set(j[0], i[1]);
                    }
                }
                else {
                    return finished;
                }
                
                if (nonempty permutation = elements*.item) {
                    return permutation;
                }
                else {
                    return finished;
                }
            }
        };
    };
    
}

"Used by [[Collection.permutations]] to group nulls together."
object nullElement {}
