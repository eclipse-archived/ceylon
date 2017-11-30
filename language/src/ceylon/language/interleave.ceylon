/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Given one or more argument [[streams|iterables]], return a
 stream containing elements of the given streams. The 
 elements are ordered first according to their position in 
 the argument stream, and then according to the stream in 
 which they occur. The resulting stream contains exactly the 
 same number of elements from each stream.
 
 For example, the expression
 
     interleave(1..5, \"-+\".cycled)
 
 results in the stream 
 `{ 1, '-', 2, '+', 3, '-', 4, '+', 5, '-' }`."
see (function Iterable.interpose)
tagged("Streams")
since("1.1.0")
shared Iterable<Element,Absent>
        interleave<Element,Absent>
        (Iterable<Element,Absent>+ iterables) 
        given Absent satisfies Null 
        => object satisfies Iterable<Element,Absent> {
    
    size => min { for (it in iterables) it.size } * iterables.size;
    
    empty => package.any { for (it in iterables) it.empty };
    
    iterator() => object satisfies Iterator<Element> {
        value iterators 
                = iterables.collect((Iterable<Element> it) 
            => it.iterator());
        variable value which = 0;
        shared actual Element|Finished next() {
            assert (exists iter = iterators[which]);
            if (!is Finished next = iter.next()) {
                if (++which>=iterators.size) {
                    which = 0;
                }
                return next;
            }
            else {
                return finished;
            }
        }
    };
    
};