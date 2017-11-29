/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"The `Boolean` value of the given string representation of a 
 boolean value, or `null` if the string does not represent a 
 boolean value.
 
 Recognized values are `\"true\"`, `\"false\"`."
tagged("Basic types")
see (function Boolean.parse)
shared Boolean? parseBoolean(String? string) 
        => if (exists string,
               is Boolean result
                   = parseBooleanInternal(string))
        then result
        else null;

Boolean|ParseException parseBooleanInternal(String string)
        => switch (string)
        case ("true") true
        case ("false") false
        else ParseException("illegal format for Boolean");