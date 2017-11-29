/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"A type capable of representing the values [[true]] and
 [[false]] of Boolean logic."
by ("Gavin")
tagged("Basic types")
shared native abstract class Boolean
        of true | false {
    
    "The `Boolean` value of the given string representation 
     of a boolean value, or `null` if the string does not 
     represent a boolean value.
     
     Recognized string representations are the strings
     `\"true\"` and `\"false\"`."
    tagged("Basic types")
    since("1.3.1")
    shared static Boolean|ParseException parse(String string)
            => parseBooleanInternal(string);
    
    shared new () {}
}

"A value representing truth in Boolean logic."
tagged("Basic types")
shared native object true
        extends Boolean() {
    string => "true";
    hash => 1231;
}

"A value representing falsity in Boolean logic."
tagged("Basic types")
shared native object false
        extends Boolean() {
    string => "false";
    hash => 1237;
}
