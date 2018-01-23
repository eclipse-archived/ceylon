/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"A collection in which each distinct element occurs at most 
 once. Two non-[[identical|Identifiable]] values are 
 considered distinct only if they are unequal, according to
 their own definition of [[value equality|Object.equals]].
 
 A `Set` is a [[Collection]] of its elements. Sets may not
 have [[null|Null]] elements.
 
 A new `Set` may be obtained by calling the function [[set]].
     
     value words = set { \"hello\", \"world\" };
     value greetings = set { \"hello\", \"goodbye\", \"hola\", \"adios\" };
 
 The [[emptySet]] is a `Set` with no elements.
 
 Sets may be the subject of the binary union, intersection, 
 and complement operators `|`, `&`, and `~`.
 
     value greetingsInWords = words & greetings;
     value allWords = words | greetings;
 
 An implementation of `Set` may compare elements for 
 equality using [[Object.equals]] or [[Comparable.compare]]."
tagged("Collections")
see (function package.set, value emptySet)
shared interface Set<out Element=Object>
        satisfies Collection<Element>
        given Element satisfies Object {
    
    "The fundamental operation for `Set`s. Determines if the
     given value belongs to this set."
    shared actual default Boolean contains(Object element)
            => super.contains(element);
    
    "A shallow copy of this set, that is, a set with the
     same elements as this set, which do not change if the
     elements of this set change."
    shared actual default Set<Element> clone() 
            => package.set(this);
    
    "Determines if this set is a superset of the given 
     `Set`, that is, if this set contains all of the 
     elements in the given set."
    shared default Boolean superset(Set<> set) {
        for (element in set) {
            if (!contains(element)) {
                return false;
            }
        }
        else {
            return true;
        }
    }
    
    "Determines if this set is a subset of the given `Set`, 
     that is, if the given set contains all of the elements 
     in this set."
    shared default Boolean subset(Set<> set) {
        for (element in this) {
            if (!element in set) {
                return false;
            }
        }
        else {
            return true;
        }
    }
    
    distinct => this;
    
    shared actual {Element*} 
    defaultNullElements<Default>(Default defaultValue)
            given Default satisfies Object => this;
    
    "Returns a new `Set` containing all the elements of this 
     set and all the elements of the given `Set`.
     
     For example:
     
         set { \"hello\", \"world\" } | set { 1, 2, \"hello\" }
     
     Produces the set `{ \"hello\", \"world\", 1, 2 }` of 
     type `Set<String|Integer>`.
     
     Note that it is possible for two sets of disjoint 
     element type to be considered to have elements in 
     common. For example, since \`1==1.0\` 
     [[evaluates to true|Integer.equals]], 
     the expression 
     
         set { 1 } | set { 1.0 }
     
     produces the set `{ 1 }`."
    shared default 
    Set<Element|Other> union<Other>(Set<Other> set)
            given Other satisfies Object 
            => package.set(chain(set));
    
    "Returns a new `Set` containing only the elements that 
     are present in both this set and the given `Set` and 
     that are instances of the intersection `Element&Other` 
     of the element types of the two sets.
     
     For example:
     
         set { \"hello\", \"world\" } & set { 1, 2, \"hello\" }
     
     Produces the set `{ \"hello\" }` of type `Set<String>`.
     
     Note that, according to this definition, and even 
     though `1==1.0` [[evaluates to true|Integer.equals]], 
     the expression
     
         set { 1 } & set { 1.0 }
     
     produces the empty set `{}`."
    shared default 
    Set<Element&Other> intersection<Other>(Set<Other> set)
            given Other satisfies Object
            => package.set(filter((e) => e in set)
                    .narrow<Other>());
    
    "Returns a new `Set` containing all the elements in this 
     set that are not contained in the given `Set`.
     
     For example:
     
         set { \"hello\", \"world\" } ~ set { 1, 2, \"hello\" }
     
     Produces the set `{ \"world\" }` of type `Set<String>`."
    shared default 
    Set<Element> complement<Other>(Set<Other> set)
            given Other satisfies Object 
            => package.set(filter((e) => !e in set));
    
    "Returns a new `Set` containing only the elements 
     contained in either this set or the given `Set`, but no 
     element contained in both sets."
    shared default 
    Set<Element|Other> exclusiveUnion<Other>(Set<Other> set)
            given Other satisfies Object 
            => package.set(filter((e) => !e in set)
                    .chain(set.filter((e) => !e in this)));
    
    "Two `Set`s are considered equal if they have the same 
     size and if every element of the first set is also an 
     element of the second set, as determined by 
     [[contains]]. Equivalently, a set is equal to a second 
     set if it is both a subset and a superset of the second
     set."
    shared actual default Boolean equals(Object that) {
        if (is Set<> that, that.size==size) {
            for (element in this) {
                if (!element in that) {
                    return false;
                }
            }
            else {
                return true;
            }
        }
        return false;
    }
    
    shared actual default Integer hash {
        variable Integer hashCode = 0;
        for (elem in this) {
            hashCode += elem.hash;
        }
        return hashCode;
    }
        
}

"Create a new immutable [[Set]] containing every element 
 produced by the given [[stream]], resolving items with
 duplicate keys according to the given [[function|choosing]].
 
 For example:
 
     set { 0, 1, 1, 2, 3, 3, 3 }
 
 produces the set `{ 0, 1, 2, 3 }`.
 
 This is an eager operation and the resulting set does not 
 reflect changes to the given [[stream]]."
see(value Iterable.distinct)
since("1.2.0")
shared Set<Element> set<Element>(
    "The stream of elements."
    {Element*} stream,
    "A function that chooses between duplicate elements. By 
     default, the element that occurs _earlier_ in the 
     stream is chosen."
    Element choosing(Element earlier, Element later) 
            => earlier)
        given Element satisfies Object
        => IterableSet(stream, choosing);

class IterableSet<Element>(
    {Element*} stream,
    Element choosing(Element earlier, Element later))
        extends Object()
        satisfies Set<Element>
        given Element satisfies Object {
    
    value elements =
            stream.summarize(identity,
                (Element? current, element)
                        => if (exists current)
                        then choosing(current, element)
                        else element);
    
    contains(Object element) => elements.defines(element);
    
    iterator() => elements.keys.iterator();
    
    size => elements.size;
    
    empty => elements.empty;
    
    clone() => this;
    
}

"An immutable [[Set]] with no elements."
tagged("Collections")
shared object emptySet 
        extends Object() 
        satisfies Set<Nothing> {
    
    shared actual 
    Set<Other> union<Other>(Set<Other> set)
            given Other satisfies Object
            => set;
    
    shared actual 
    Set<Nothing> intersection<Other>(Set<Other> set)
            given Other satisfies Object
            => this;
    
    shared actual 
    Set<Other> exclusiveUnion<Other>(Set<Other> set)
            given Other satisfies Object
            => set;
    
    shared actual 
    Set<Nothing> complement<Other>(Set<Other> set)
            given Other satisfies Object
            => this;
    
    subset(Set<> set) => true;
    superset(Set<> set) => set.empty;
    
    clone() => this;
    iterator() => emptyIterator;
    size => 0;
    empty => true;
    
    contains(Object element) => false;
    containsAny({Object*} elements) => false;
    containsEvery({Object*} elements) => elements.empty;
    
    count(Boolean selecting(Nothing element)) => 0;
    any(Boolean selecting(Nothing element)) => false;
    every(Boolean selecting(Nothing element)) => true;
    
    shared actual 
    Null find(Boolean selecting(Nothing element)) 
            => null;
    
    shared actual 
    Null findLast(Boolean selecting(Nothing element))
            => null;
    
    skip(Integer skipping) => this;
    take(Integer taking) => this;
    by(Integer step) => this;
    
    shared actual 
    void each(void step(Nothing element)) {}
    
}
