/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"""A _tuple_ is a typed linked list. Each instance of 
   `Tuple` represents the value and type of a single link.
   The attributes `first` and `rest` allow us to retrieve a 
   value from the list without losing its static type 
   information.
   
       value point = Tuple(0.0, Tuple(0.0, Tuple("origin", [])));
       Float x = point.first;
       Float y = point.rest.first;
       String label = point.rest.rest.first;
   
   Usually, we abbreviate code involving tuples.
   
       [Float,Float,String] point = [0.0, 0.0, "origin"];
       Float x = point[0];
       Float y = point[1];
       String label = point[2];
   
   A list of types enclosed in brackets is an abbreviated 
   tuple type. An instance of `Tuple` may be constructed by 
   surrounding a value list in brackets:
   
       [String,String] words = ["hello", "world"];
   
   The index operator with a literal integer argument is a 
   shortcut for a chain of evaluations of `rest` and 
   `first`. For example, `point[1]` means `point.rest.first`.
   
   A _terminated_ tuple type is a tuple where the type of
   the last link in the chain is `Empty`. An _unterminated_ 
   tuple type is a tuple where the type of the last link
   in the chain is `Sequence` or `Sequential`. Thus, a 
   terminated tuple type has a length that is known
   statically. For an unterminated tuple type only a lower
   bound on its length is known statically.
   
   Here, `point` is an unterminated tuple:
   
       String[] labels = ... ;
       [Float,Float,String*] point = [0.0, 0.0, *labels];
       Float x = point[0];
       Float y = point[1];
       String? firstLabel = point[2];
       String[] allLabels = point[2...];"""
by ("Gavin")
tagged("Sequences", "Basic types", "Collections")
shared final serializable native 
class Tuple<out Element, out First, out Rest = []>
        (first, rest)
        extends Object()
        satisfies [Element+]
        given First satisfies Element
        given Rest satisfies Element[] {
    
    "The first element of this tuple. (The head of the 
     linked list.)"
    shared actual native 
    First first;
    
    "A tuple with the elements of this tuple, except for the
     first element. (The tail of the linked list.)"
    shared actual native 
    Rest rest;
    
    shared actual native 
    Integer lastIndex => rest.size;
    
    shared actual native 
    Integer size => 1 + rest.size;
    
    shared actual native 
    Element? getFromFirst(Integer index) 
            => switch (index <=> 0)
            case (smaller) null
            case (equal) first
            case (larger) rest.getFromFirst(index - 1);
    
    "The last element of this tuple."
    shared actual native 
    Element last 
            => if (nonempty rest)
            then rest.last
            else first;
    
    shared actual native 
    Element[] measure(Integer from, Integer length) {
        if (length <= 0) {
            return [];
        }
        value realFrom = from < 0 then 0 else from;
        if (realFrom == 0) {
            return length == 1 
                    then [first]
                    else rest[0 : length+realFrom-1]
                            .withLeading(first);
        }
        return rest[realFrom-1 : length];
    }
    
    shared actual native 
    Element[] span(Integer from, Integer end) {
        if (from < 0 && end < 0) {
            return [];
        }
        value realFrom = from < 0 then 0 else from;
        value realEnd = end < 0 then 0 else end;
        return realFrom <= realEnd 
            then this[from : realEnd-realFrom+1]
            else this[realEnd : realFrom-realEnd+1]
                        .reversed.sequence();
    }
    
    shared actual native 
    Element[] spanTo(Integer to)
            => to<0 then [] else this[0..to];
    
    shared actual native 
    Element[] spanFrom(Integer from)
            => from<size then this[from..lastIndex] else [];
    
    "This tuple."
    shared actual native 
    Tuple<Element,First,Rest> clone() => this;
    
    since("1.3.3")
    shared actual native 
    [Element+] tuple() => super.tuple();
    
    shared actual native 
    Iterator<Element> iterator() 
            => object
            satisfies Iterator<Element> {
        variable Element[] current = outer;
        shared actual Element|Finished next() {
            if (nonempty c = current) {
                current = c.rest;
                return c.first;
            }
            else {
                return finished;
            }
        }
        string => "``outer.string``.iterator()";
    };
    
    "Determine if the given value is an element of this
     tuple."
    shared actual native 
    Boolean contains(Object element) 
            => if (exists first, first == element)
            then true
            else element in rest;
    
    "Return a new tuple that starts with the specified
     [[element]], followed by the elements of this tuple."
    shared actual native
    Tuple<Element|Other,Other,Tuple<Element,First,Rest>>
    withLeading<Other>(
            "The first element of the resulting tuple."
            Other element)
            => Tuple(element, this);
    
    "Return a new tuple containing the elements of this 
     tuple, followed by the given [[element]]."
    shared actual native
    [First,Element|Other+] withTrailing<Other>(
            "The last element of the resulting tuple."
            Other element) 
            => Tuple(first, rest.withTrailing(element));
    
    "Return a tuple containing the elements of this 
     tuple, followed by the given [[elements]]."
    shared actual native
    [First,Element|Other*] append<Other>(
            "The list of elements to be appended."
            Other[] elements)
            => Tuple(first, rest.append(elements));
}

"Efficiently repackage the given array as a [[Tuple]]."
throws (class AssertionError, 
        "if the given array is empty")
native [Element+] arrayToTuple<Element>(Array<Element> array) {
    variable Element[] tuple = [];
    for (element in array.reversed) {
        tuple = [element, *tuple];
    }
    "array must not be empty"
    assert (nonempty result = tuple);
    return result;
}
