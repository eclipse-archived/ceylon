/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"An instance of [[Float]] representing positive infinity, 
 \{#221E}, the result of dividing a positive number by zero. 
 Negative infinity, -\{#221E}, the result of dividing a
 negative number by zero, is the additive inverse `-infinity`.
 
 Note that any floating-point computation that results in a
 positive value too large to be represented as a `Float` is 
 \"rounded up\" to `infinity`. Likewise, any floating-point 
 computation that yields a negative value whose magnitude is
 too large to be represented as a `Float` is \"rounded down\" 
 to `-infinity`."
tagged("Numbers")
native shared Float infinity;

native("jvm") shared Float infinity { 
    import java.lang {
        Double
    }

    return Double.positiveInfinity;
}

native("js") shared Float infinity { 
    dynamic {
        return \iInfinity;
    } 
}
