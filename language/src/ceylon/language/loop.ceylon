/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Produces the [[stream|Iterable]] that results from repeated
 application of the given [[function|next]] to the given
 [[first]] element of the stream, until the function first
 returns [[finished]]. If the given function never returns 
 `finished`, the resulting stream is infinite.

 For example:

     loop(0, 2.plus).takeWhile(10.largerThan)

 produces the stream `{ 0, 2, 4, 6, 8 }`."
tagged("Streams")
aliased("iterate")
since("1.1.0")
shared {Element+} loop<Element>
        ("The first element of the resulting stream."
         Element first,
         "The function that produces the next element of the
          stream, given the current element. The function may
          return [[finished]] to indicate the end of the 
          stream."
         Element|Finished next(Element element))
    => let (initial = first, nxt = next)
    object satisfies {Element+} {
        first => initial;
        empty => false;
        iterator() => object satisfies Iterator<Element> {
            variable Boolean started = false;
            variable Element|Finished current = initial;
            shared actual Element|Finished next() {
                if (!started) {
                    started = true;
                    return initial;
                }

                if (!is Finished curr = current) {
                    current = nxt(curr);
                }
                return current;
            }
        };
    };
