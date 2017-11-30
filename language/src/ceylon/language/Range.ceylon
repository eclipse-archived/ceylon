/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language {
    makeSpan=span,
    makeMeasure=measure
}

"A range of adjacent [[Enumerable]] values. Two values of an
 enumerable type are considered _adjacent_ if their 
 [[offset|Enumerable.offset]] is of unit or zero magnitude. 
 Thus, a `Range` is a list of values where for every integer 
 `index` where both `index` and `index+1` fall within the 
 indices of the range:
 
     range[index+1].offset(range[index]).magnitude <= 1
 
 A range is always nonempty, containing at least one value.
 Thus, it is a [[Sequence]].
 
 A sequence of adjacent values may be represented very 
 efficiently in memory, either:
 
 - via its endpoints, `first..last`, or 
 - via an endpoint and length, `first:size`.
 
 Furthermore, certain operations are much more efficient 
 than for other lists, assuming that the enumerable type has 
 efficient [[neighbour|Enumerable.neighbour]] and
 [[offset|Enumerable.offset]] functions.
 
 The functions [[ceylon.language::span]] and 
 [[ceylon.language::measure]], and corresponding operators 
 `..` and `:` are used to create new instances of `Range`."
see (function makeSpan,
    function makeMeasure)
tagged("Sequences")
shared sealed
abstract serializable
class Range<Element>()
        of Span<Element> 
         | Measure<Element>
        extends Object()
        satisfies [Element+]
        given Element satisfies Enumerable<Element> {
    
    "Determines if this range includes the given value."
    shared formal Boolean containsElement(Element element);
    
    "Determines if this range includes the given range."
    shared formal Boolean includesRange(Range<Element> range);
    
    contains(Object element) 
            => if (is Element element) 
            then containsElement(element)
            else false;
    
    "Returns a range of the same length and type as this
        range, with its endpoints shifted by the given number 
        of elements, where:
        
        - a negative [[shift]] measures 
          [[decrements|Ordinal.predecessor]], and 
        - a positive `shift` measures 
          [[increments|Ordinal.successor]]."
    since("1.1.0")
    shared formal Range<Element> shifted(Integer shift);
    
    "Determines if the range is increasing, that is, if
     successors occur after predecessors."
    since("1.1.0")
    shared formal Boolean increasing;
    
    "Determines if the range is decreasing, that is, if
     predecessors occur after successors."
    shared formal Boolean decreasing;
    
    "Returns the range itself, since a range cannot contain 
     null elements."
    shared actual Range<Element> coalesced => this;
    
    "Returns the range itself, since a range cannot contain 
     duplicate elements."
    shared actual Range<Element> distinct => this;
}
