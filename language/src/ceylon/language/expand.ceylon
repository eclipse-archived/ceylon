/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Given a [[stream|iterables]] whose elements are also 
 streams, return a new stream with all elements of every 
 nested stream. If there are no nested streams, or if all of
 the nested streams are empty, return an empty stream.
 
 For example, the expression
 
     expand { 1..3, {5}, \"hi\" }
 
 results in the stream `{ 1, 2, 3, 5, 'h', 'i' }` which has
 the type `{Integer|Character*}`."
see (function Iterable.flatMap,
     function concatenate, 
     function Iterable.chain)
tagged("Streams")
since("1.1.0")
shared Iterable<Element,OuterAbsent|InnerAbsent>
        expand<Element,OuterAbsent,InnerAbsent>
        (Iterable<Iterable<Element,InnerAbsent>,OuterAbsent> iterables)
        given OuterAbsent satisfies Null
        given InnerAbsent satisfies Null
        => { for (it in iterables) for (val in it) val };
