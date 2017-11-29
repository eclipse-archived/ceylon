/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Represents failure of an attempt to parse a string
 representation."
since("1.3.1")
see(function Integer.parse, 
    function Float.parse, 
    function Boolean.parse)
shared class ParseException(String message)
    extends Exception(message) {}