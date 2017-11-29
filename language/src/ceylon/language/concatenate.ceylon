/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Given zero or more argument [[streams|iterables]], return a 
 new sequence containing all elements of every given stream.
 The elements of the resulting stream are ordered first 
 according to the stream in which they occur, and then 
 according to where they occur in that stream. If there are 
 no arguments, or if none of the argument streams contain 
 any elements, return the empty sequence.
 
 For example, the expression
 
     concatenate(1..3, [0.0], {\"hello\", \"world\"})
 
 results in the sequence `[1, 2, 3, 0.0, \"hello\", \"world\"]`
 which has the type `[Integer|Float|String+]`.
 
 To concatentate `String`s, use [[String.sum]]. When a lazy
 stream is desired, use [[expand]]."
see (function expand, 
     function Iterable.chain,
     function Sequential.append,
     function String.sum)
tagged("Streams")
shared [Element+] | []&Iterable<Element,Absent> 
concatenate<Element,Absent>(
        "The streams to concatenate."
        Iterable<Element,Absent>+ iterables)
        given Absent satisfies Null
        => expand(iterables).sequence();
