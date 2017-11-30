/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"""Abstract supertype of objects that contain other values, 
   called *elements*, where it is possible to efficiently 
   determine if a given value is an element. A `Category`
   may not be finite, and its elements may not even be
   countable. Thus, unlike [[streams|Iterable]], the 
   elements of a generic `Category` are not iterable.
   
   `Category` models a mathematical set, but is distinct 
   from the [[Set]] collection type which represents finite
   sets.
   
   The `in` operator may be used to determine if a value
   belongs to a `Category`:
   
       if (69 in 0..100) { ... }
       assert (key->item in { for (n in 0..100) n.string->n**2 });
   
   An object may be a `Category` of two different disjoint
   element types. For example, [[String]] is a `Category`
   of its `Character`s and of its substrings.
   
       if ("hello" in "hello world") { ... }
       assert ('.' in string);
   
   Every meaningful `Category` is formed from elements with
   some equivalence relation. Ordinarily, that equivalence
   relation is [[value equality|Object.equals]]. Thus,
   ordinarily, `x==y` implies that `x in cat == y in cat`.
   But this contract is not required since it is possible to 
   form a meaningful `Category` using a different 
   equivalence relation. For example, an `IdentitySet` is a 
   meaningful `Category`, where the equivalence relation is
   [[identity equality|Identifiable]].
   
   Since [[Null]] is not considered to have any meaningful
   equivalence relation, a `Category` may not contain the
   [[null value|null]].
   
   Note that even though `Category<Element>` is declared
   contravariant in its [[element type|Element]], most types
   that inherit `Category` are covariant in their element
   type, and therefore satisfy `Category<Object>`, resulting
   in some loss of typesafety. For such types, [[contains]] 
   should return `false` for any value that is not an 
   instance of the element type. For example, `String` is a 
   `Category<Object>`, not a `Category<Character|String>`,
   and `x in string` evaluates to `false` for every `x` that
   is not a `String` or `Character`."""
by ("Gavin")
tagged("Collections")
shared interface Category<in Element=Object>
        given Element satisfies Object {
    
    "Returns `true` if the given value belongs to this
     `Category`, that is, if it is an element of this
     `Category`, or `false` otherwise.
     
     For any instance `c` of `Category`, `c.contains(element)` 
     may be written using the `in` operator:
     
         element in c
     
     For most `Category`s, the following relationship is 
     satisfied by every pair of elements `x` and `y`:
     
     - if `x==y`, then `x in category == y in category`
     
     However, it is possible to form a useful `Category` 
     consistent with some other equivalence relation, for 
     example `===`. Therefore implementations of `contains()` 
     which do not satisfy this relationship are tolerated."
    see (function containsEvery, function containsAny)
    shared formal Boolean contains(Element element);
    
    "Returns `true` if every one of the given values belongs 
     to this `Category`, or `false` otherwise."
    see (function contains, function containsAny)
    shared default 
    Boolean containsEvery({Element*} elements) {
        for (element in elements) {
            if (!contains(element)) {
                return false;
            }
        }
        else {
            return true;
        }
    }

    "Returns `true` if any one of the given values belongs 
     to this `Category`, or `false` otherwise."
    see (function contains, function containsEvery)
    shared default 
    Boolean containsAny({Element*} elements) {
        for (element in elements) {
            if (contains(element)) {
                return true;
            }
        }
        else {
            return false;
        }
    }

}
