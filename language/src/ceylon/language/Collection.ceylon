/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"An iterable collection of elements of finite [[size]], with 
 a well-defined notion of [[value equality|equals]]. 
 `Collection` is the abstract supertype of [[List]], [[Map]], 
 and [[Set]].
 
 A `Collection` forms a [[Category]] of its elements, and is 
 [[Iterable]]. The elements of a collection are not
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
see (interface List, interface Map, interface Set)
tagged("Collections")
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
    
    "Return `true` if the given object is an element of this 
     collection. In this default implementation, and in most 
     refining implementations, return `false` otherwise. An 
     acceptable refining implementation may return `true` 
     for objects which are not elements of the collection, 
     but this is not recommended. (For example, the 
     `contains()` method of `String` returns `true` for any 
     substring of the string.)"
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
     according to the order in which each distinct element 
     of this collection is first produced by its iterator.
     No permutation is repeated.
     
     Two elements are considered distinct if either:
     
     - they are both instances of `Object`, and are 
       [[unequal|Object.equals]], or
     - one element is an `Object` and the other is `null`."
    since("1.2.0")
    shared {[Element+]*} permutations 
            => object satisfies {[Element+]*} {
        value multiset =
            outer
            .indexed
            .group((_->item) => item else nullElement)
            .items
            .sort((x,y) => x.first.key<=>y.first.key)
            .indexed
            .flatMap((index->entries) 
                => entries.map((_->item) => index->item));
        
        empty => multiset.empty;
        
        iterator() 
                => object satisfies Iterator<[Element+]> {
            value elements = Array(multiset);
            value size = elements.size;
            
            variable value initial = true;
            
            shared actual [Element+]|Finished next() {
                if (initial) {
                    initial = false;
                }
                else if (exists i -> [key->_, __]
                        = elements.paired.locateLast(
                            ([m->_, n->__]) => m < n)) {
                    assert (exists j
                        = elements.lastIndexWhere(
                            (k->_) => k > key));
                    elements.swap(i,j);
                    for (k in 0 : (size-i-1)/2) {
                        elements.swap(i+1+k, size-1-k);
                    }
                }
                else {
                    return finished;
                }
                return
                    if (nonempty permutation 
                        = elements*.item) 
                    then permutation 
                    else finished;
            }
        };
    };
    
    "The combinations of elements of this collection, of the
     given positive [[size|length]], as a stream of nonempty 
     [[sequences|Sequence]]. That is, a stream producing 
     every distinct selection of `length` elements of this 
     collection.
     
     For example,
     
         \"ABCD\".combinations(2).map(String)
     
     is the stream of strings
     `{ \"AB\", \"AC\", \"AD\", \"BC\", \"BD\", \"CD\" }`.
     
     If this collection is empty, the resulting stream is
     empty.
     
     The combinations are enumerated lexicographically
     according to the order in which each distinct element 
     of this collection is first produced by its iterator.
     No combination is repeated.
     
     Two elements are considered distinct if either:
     
     - they are both instances of `Object`, and are 
       [[unequal|Object.equals]], or
     - one element is an `Object` and the other is `null`."
    throws (class AssertionError, 
            "if [[length]] is nonpositive or if `length` is
             larger than the number of distinct elements of
             this collection")
    since("1.3.0")
    shared {[Element+]*} combinations(
                "The number of distinct elements in each
                 combination"
                Integer length) {
        "length must be strictly positive"
        assert (length>0);
        return object satisfies {[Element+]*} {
            value distinctElements = outer.distinct;
            
            empty => outer.empty;
            
            iterator()
                    => object satisfies Iterator<[Element+]> {
                
                value elements = distinctElements.sequence();
                value size = elements.size;
                
                "length is larger than the number of distinct elements"
                assert (length <= size);
                
                value selection = Array(0:length);
                variable value done = elements.empty;
                
                shared actual [Element+]|Finished next() {
                    if (done) {
                        return finished;
                    }
                    value current = selection.collect((i) {
                        if (exists e 
                            = elements.getFromFirst(i)) {
                            return e;
                        }
                        else {
                            assert (is Element null);
                            return null;
                        }
                    });
                    assert (nonempty current);
                    
                    variable value i = length-1;
                    while (true) {
                        if (i<0) {
                            done = true;
                            break;
                        }
                        assert (exists s = selection[i]);
                        if (s == size-length+i) {
                            i--;
                        }
                        else {
                            variable value j = s;
                            while (i<length) {
                                selection[i++] = ++j;
                            }
                            break;
                        }
                    }
                    
                    return current;
                }
                
            };
            
        };
    }
    
}

"Used by [[Collection.permutations]] to group nulls together."
object nullElement {}
