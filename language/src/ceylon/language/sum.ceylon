/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Given a nonempty stream of [[Summable]] values, return the 
 sum of the values.
 
     {Float+} values = ... ;
     Float total = sum(values);
 
 For the case of a possibly-empty stream, form a nonempty 
 stream starting with the zero element (the [[additive 
 identity|Summable]]).
 
     {Float*} values = ... ;
     Float total = sum { 0.0, *values };
 
 For the case of a stream of `Integer`s, `Float`s, or
 `String`s, prefer [[Integer.sum]], [[Float.sum]], or
 [[String.sum]]."
see (function product, 
     function Integer.sum,
     function Float.sum,
     function String.sum)
tagged("Streams", "Numbers")
shared native Value sum<Value>({Value+} values) 
        given Value satisfies Summable<Value>;

shared native("js") Value sum<Value>({Value+} values) 
        given Value satisfies Summable<Value> {
    value it = values.iterator();
    assert (!is Finished first = it.next());
    variable value sum = first;
    while (!is Finished val = it.next()) {
        sum += val;
    }
    return sum;
}

shared native("jvm") Value sum<Value>({Value+} values) 
        given Value satisfies Summable<Value> {
    value it = values.iterator();
    switch (first = it.next())
    case (is Integer) {
        // unbox; don't infer type Value&Integer
        variable Integer sum = first;
        while (is Integer val = it.next()) {
            Integer unboxed = val;
            sum += unboxed;
        }
        assert (is Value result = sum);
        return result;
    }
    case (is Float) {
        // unbox; don't infer type Value&Float
        variable Float sum = first;
        while (is Float val = it.next()) {
            Float unboxed = val;
            sum += unboxed;
        }
        assert (is Value result = sum);
        return result;
    }
    case (is String) {
        value sum = StringBuilder();
        sum.append(first);
        while (is String val = it.next()) {
            sum.append(val);
        }
        assert (is Value result = sum.string);
        return result;
    }
    case (is Finished) {
        assert (false);
    }
    else {
        variable value sum = first;
        while (!is Finished val = it.next()) {
            sum += val;
        }
        return sum;
    }
}