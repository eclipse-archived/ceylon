/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Given a stream of [[Comparable]] values, return the largest 
 value in the stream, or `null` if the stream is empty.
 
 For any nonempty stream `it`, `max(it)` evaluates to the 
 first element of `it` such that for every element `e` of 
 `it`, `max(it) >= e`.
 
 Any value `x` which violates the reflexivity requirement of
 [[Object.equals]] such that `x!=x` is skipped, unless it is
 the last element in the stream. Thus, for a stream of 
 [[Float]]s, `max()` will not return an
 [[undefined value|Float.undefined]] unless every element of
 the stream is undefined.
 
 Note that [[Iterable.max]] may be used to find the largest 
 value in any stream, as determined by a given comparator 
 function."
see (interface Comparable, 
     function min, 
     function largest,
     function Iterable.max, 
     function Integer.max,
     function Float.max)
tagged("Comparisons", "Streams")
shared native Absent|Value max<Value,Absent>
        (Iterable<Value,Absent> values) 
        given Value satisfies Comparable<Value>
        given Absent satisfies Null;

shared native("js") Absent|Value max<Value,Absent>
        (Iterable<Value,Absent> values) 
        given Value satisfies Comparable<Value>
        given Absent satisfies Null {
    value it = values.iterator();
    if (!is Finished first = it.next()) {
        variable value max = first;
        while (max!=max, //quick test for NaN
              !is Finished val = it.next()) {
            max = val;
        }
        while (!is Finished val = it.next()) {
            if (val>max) {
                max = val;
            }
        }
        return max;
    }
    else {
        "iterable must be empty"
        assert (is Absent null);
        return null;
    }
}

shared native("jvm") Absent|Value max<Value,Absent>
        (Iterable<Value,Absent> values)
        given Value satisfies Comparable<Value>
        given Absent satisfies Null {
    
    value it = values.iterator();
    switch (first = it.next())
    case (is Finished) {
        "iterable must be empty"
        assert (is Absent null);
        return null;
    }
    case (is Integer) {
        variable Integer max = first;
        while (is Integer val = it.next()) {
            if ((val of Integer) > max) {
                max = val;
            }
        }
        assert (is Value result = max);
        return result;
    }
    case (is Float) {
        variable Float max = first;
        while (max!=max,
               is Float val = it.next()) {
            max = val;
        }
        while (is Float val = it.next()) {
            if ((val of Float) > max) {
                max = val;
            }
        }
        assert (is Value result = max);
        return result;
    }
    else {
        variable value max = first;
        //exactly reproduce behavior on JS above
        while (max!=max,
              !is Finished val = it.next()) {
            max = val;
        }
        while (!is Finished val = it.next()) {
            if (val>max) {
                max = val;
            }
        }
        return max;
    }
}
