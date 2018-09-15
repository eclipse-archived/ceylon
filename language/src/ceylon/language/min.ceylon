/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Given a stream of [[Comparable]] values, return the 
 smallest value in the stream, or `null` if the stream is
 empty.
 
 For any nonempty stream `it`, `min(it)` evaluates to the 
 first element of `it` such that for every element `e` of 
 `it`, `min(it) <= e`.
 
 Any value `x` which violates the reflexivity requirement of
 [[Object.equals]] such that `x!=x` is skipped, unless it is
 the last element in the stream. Thus, for a stream of 
 [[Float]]s, `min()` will not return an
 [[undefined value|Float.undefined]] unless every element of
 the stream is undefined."
see (interface Comparable, 
     function max,
     function smallest, 
     function Integer.min,
     function Float.min)
tagged("Comparisons", "Streams")
shared native Absent|Value min<Value,Absent>
        (Iterable<Value,Absent> values) 
        given Value satisfies Comparable<Value>
        given Absent satisfies Null;

shared native("js") Absent|Value min<Value,Absent>
        (Iterable<Value,Absent> values) 
        given Value satisfies Comparable<Value>
        given Absent satisfies Null {
    value it = values.iterator();
    if (!is Finished first = it.next()) {
        variable value min = first;
        while (min!=min, //quick test for NaN
              !is Finished val = it.next()) {
            min = val;
        }
        while (!is Finished val = it.next()) {
            if (val<min) {
                min = val;
            }
        }
        return min;
    }
    else {
        "iterable must be empty"
        assert (is Absent null);
        return null;
    }
}

shared native("jvm") Absent|Value min<Value,Absent>
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
        variable Integer min = first;
        while (is Integer val = it.next()) {
            if ((val of Integer) < min) {
                min = val;
            }
        }
        assert (is Value result = min);
        return result;
    }
    case (is Float) {
        variable Float min = first;
        while (min!=min,
               is Float val = it.next()) {
            min = val;
        }
        while (is Float val = it.next()) {
            if ((val of Float) < min) {
                min = val;
            }
        }
        assert (is Value result = min);
        return result;
    }
    else {
        variable value min = first;
        //exactly reproduce behavior on JS above
        while (min!=min,
              !is Finished val = it.next()) {
            min = val;
        }
        while (!is Finished val = it.next()) {
            if (val<min) {
                min = val;
            }
        }
        return min;
    }
}