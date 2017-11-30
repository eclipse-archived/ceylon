/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Given a [[stream|Iterable]] of streams, return a 
 transposed stream of streams, where the nth element 
 of the transposed stream is a stream comprising the 
 nth elements of the given streams, padding shorter 
 streams with the [[given value|padding]]. 
 For example,
 
     transpose { padding=null; \"hello\", \"world\", 1..3 }
 
 produces the stream 
 `{ { 'h', 'w', 1 }, { 'e', 'o', 2 }, { 'l', 'r', 3 }, 
  { 'l', 'l', null }, { 'o', 'd', null } }`."
since("1.4.0")
shared {[Element+]*} transpose<Element,Absent>
        ("A value used to pad shorter streams."
         Element padding,
         "The streams to be transposed."
         Iterable<{Element*},Absent> streams)
        given Absent satisfies Null
        => object satisfies {[Element+]*} {
    iterator() => object satisfies Iterator<[Element+]> {
        value iterators 
                = streams.collect((stream) => stream.iterator());
        shared actual [Element+]|Finished next() {
            if (nonempty iterators) {
                variable value allFinished = true;
                value elements 
                        = iterators.collect((iter) {
                            if (!is Finished next = iter.next()) {
                                allFinished = false;
                                return next;
                            }
                            else {
                                return padding;
                            }
                        });
                if (!allFinished) {
                    return elements;
                }
            }
            return finished;
        }
    };
};
