/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"A [[List]] which can be efficiently searched for 
 occurrences of a given element, or for inclusions of a 
 given sublist of elements. This interface provides 
 operations for finding:
 
 - _occurrences_ of a single value in the list, and
 - _inclusions_ of a given sublist of values in the list.
 
 Occurrences and inclusions are identified by a list index
 at which the value or sublist of values occurs in the list. 
 In the case of an inclusion, it is the index of the first 
 matching value from the sublist.
 
 Inclusions may overlap. For example:
 
     \"yoyoyoyoyo\".inclusions(\"yoy\")
 
 produces the stream `{ 0, 2, 4, 6 }`.
 
 An empty list is considered to be included at every index,
 including the index [[size]] at the very end of the 
 list. Thus:
 
     \"hello\".inclusions(\"\")
 
 produces the stream `{ 0, 1, 2, 3, 4, 5 }`, with 6 
 inclusions in a string of length 5. 
 
 In particular:
 
 - `\"\".inclusions(\"x\")` is the empty stream `{}`, but
 - `\"\".inclusions(\"\")` is the stream `{ 0 }`."
see (class String, class Array)
since("1.2.0")
shared interface SearchableList<Element> 
        satisfies List<Element> {
    
    "Determines if the given [[value|element]] occurs at the 
     given index in this list."
    shared default 
    Boolean occursAt(
        "The index at which the value might occur."
        Integer index, 
        "The value. If null, it is considered to occur
         at any index in this list with a null element."
        Element element)
            => let (elem = getFromFirst(index))
            if (exists element, exists elem) 
            then elem == element 
            else (0 <= index < size)
                && !element exists && !elem exists;
    
    "The indexes in this list at which the given 
     [[value|element]] occurs."
    shared default 
    {Integer*} occurrences(
        "The value. If null, it is considered to occur
         at any index in this list with a null element."
        Element element,
        "The smallest index to consider."
        Integer from = 0,
        "The number of indexes to consider."
        Integer length = size-from)
            //performance of comprehension was 
            //worse than hand-written iterable
            //=> { for (i in from:length) 
            //     if (occursAt(i, element)) i };
            => object satisfies Iterable<Integer> {
        value len = outer.size;
        value max = 
                from+length>len 
                    then len else from+length;
        iterator() 
                => object satisfies Iterator<Integer> {
            variable value index = from;
            shared actual Integer|Finished next() {
                while (index<max) {
                    if (occursAt(index, element)) {
                        return index++;
                    }
                    else {
                        index++;
                    }
                }
                return finished;
            }
        };
        shared actual Integer size {
            variable value size = 0;
            for (index in from:max-from) {
                if (occursAt(index,element)) {
                    size++;
                }
            }
            return size;
        }
        empty => occurs(element, from, length);
        first => firstOccurrence(element, from, length);
        last => lastOccurrence(element, from, length);
    };
    
    "Determines if the given [[value|element]] occurs as an 
     element of this list, at any index that falls within
     the segment `from:length` defined by the optional 
     [[starting index|from]] and [[length]]."
    shared default 
    Boolean occurs(
        "The value. If null, it is considered to occur
         at any index in this list with a null element."
        Element element,
        "The smallest index to consider."
        Integer from = 0,
        "The number of indexes to consider."
        Integer length = size-from)
            => firstOccurrence(element, from, length) exists;
    
    "The first index in this list at which the given 
     [[value|element]] occurs, that falls within the segment 
     `from:length` defined by the optional 
     [[starting index|from]] and [[length]]."
    shared default 
    Integer? firstOccurrence(
        "The value. If null, it is considered to occur
         at any index in this list with a null element."
        Element element,
        "The smallest index to consider."
        Integer from = 0,
        "The number of indexes to consider."
        Integer length = size-from) {
        variable value start = from;
        variable value len = length;
        if (start < 0) {
            len += start;
            start = 0;
        }
        if (len > size - start) {
            len = size - start;
        }
        for (index in start:len) {
            if (occursAt(index,element)) {
                return index;
            }
        }
        else {
            return null;
        }
    }
    
    "The last index in this list at which the given 
     [[value|element]] occurs, that falls within the range 
     `size-length-from:length` defined by the optional 
     [[starting index|from]], interpreted as a reverse index 
     counting from the _end_ of the list, and [[length]]."
    shared default 
    Integer? lastOccurrence(
        "The value. If null, it is considered to occur
         at any index in this list with a null element."
        Element element,
        "The smallest index to consider, interpreted as
         a reverse index counting from the _end_ of the 
         list, where `0` is the last element of the list, 
         and `size-1` is the first element of the list."
        Integer from = 0,
        "The number of indexes to consider."
        Integer length = size-from) {
        //TODO: refine reversed to return a SearchableList
        // => reversed.firstOccurrence(element, from, length)
        variable value start = from;
        variable value len = length;
        if (start < 0) {
            len += start;
            start = 0;
        }
        if (len > size - start) {
            len = size - start;
        }
        for (index in (size-len-start:len).reversed) {
            if (occursAt(index,element)) {
                return index;
            }
        }
        else {
            return null;
        }
    }
    
    "Determine if the given [[list|sublist]] occurs as a 
     sublist at the given index of this list."
    shared default 
    Boolean includesAt(
        "The index at which the [[sublist]] might occur."
        Integer index, 
        List<Element> sublist) {
        if (0 <= index <= size-sublist.size) {
            variable value i = index;
            for (element in sublist) {
                if (exists element) {
                    if (!occursAt(i, element)) {
                        return false;
                    }
                }
                else {
                    assert (is Element null);
                    if (!occursAt(i, null)) {
                        return false;
                    }
                }
                i++;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }
    
    "The indexes in this list at which the given 
     [[list|sublist]] occurs as a sublist, that are greater 
     than or equal to the optional [[starting index|from]]."
    shared default 
    {Integer*} inclusions(List<Element> sublist,
        "The smallest index to consider." 
        Integer from = 0) 
            //performance of comprehension was 
            //worse than hand-written iterable
            /*=> { for (i in from:size+1-from-sublist.size) 
                 if (includesAt(i, sublist)) i };*/
            => object satisfies Iterable<Integer> {
        value len = outer.size;
        iterator() 
                => object satisfies Iterator<Integer> {
            value max = len+1-sublist.size;
            variable value index = from;
            shared actual Integer|Finished next() {
                while (index<max) {
                    if (includesAt(index, sublist)) {
                        return index++;
                    }
                    else {
                        index++;
                    }
                }
                return finished;
            }
        };
        shared actual Integer size {
            variable value size = 0;
            for (index in from:len-from+1-sublist.size) {
                if (includesAt(index,sublist)) {
                    size++;
                }
            }
            return size;
        }
        empty => includes(sublist, from);
        first => firstInclusion(sublist, from);
        last => lastInclusion(sublist, from);
    };
    
    "Determine if the given [[list|sublist]] occurs as a 
     sublist at some index in this list, at any index that 
     is greater than or equal to the optional 
     [[starting index|from]]."
    shared default 
    Boolean includes(List<Element> sublist,
        "The smallest index to consider."
        Integer from = 0) 
            => firstInclusion(sublist, from) exists;
        
    "The first index in this list at which the given 
     [[list|sublist]] occurs as a sublist, that is greater 
     than or equal to the optional [[starting index|from]]."
    shared default 
    Integer? firstInclusion(List<Element> sublist,
        "The smallest index to consider." 
        Integer from = 0) {
        for (index in from:size-from+1-sublist.size) {
            if (includesAt(index,sublist)) {
                return index;
            }
        }
        else {
            return null;
        }
    }
    
    "The last index in this list at which the given 
     [[list|sublist]] occurs as a sublist, that falls within 
     the range `0:size-from+1-sublist.size` defined by the 
     optional [[starting index|from]], interpreted as a 
     reverse index counting from the _end_ of the list."
    shared default 
    Integer? lastInclusion(List<Element> sublist,
        "The smallest index to consider, interpreted as
         a reverse index counting from the _end_ of the 
         list, where `0` is the last element of the list, 
         and `size-1` is the first element of the list."
        Integer from = 0) {
        //TODO: refine reversed to return a SearchableList
        // => reversed.firstInclusion(element, from, length)
        for (index in (0:size-from+1-sublist.size).reversed) {
            if (includesAt(index,sublist)) {
                return index;
            }
        }
        else {
            return null;
        }
    }
    
}