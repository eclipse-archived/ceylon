/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Sort the given elements according to their 
 [[natural order|Comparable]], returning a new 
 [[sequence|Sequential]].
 
 Note that [[Iterable.sort]] may be used to sort any stream
 according to a given comparator function."
see (interface Comparable,
     function Iterable.sort)
tagged("Streams", "Comparisons")
shared [Element+] | []&Iterable<Element,Absent> 
sort<Element,Absent>(
        "The unsorted stream of elements."
        Iterable<Element,Absent> elements) 
        given Element satisfies Comparable<Element> 
        given Absent satisfies Null
        => elements.sort(increasing);