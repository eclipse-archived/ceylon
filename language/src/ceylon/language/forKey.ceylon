/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"A function that returns the result of applying the given 
 [[function|resulting]] to the [[key|Entry.key]] of
 a given [[Entry]], discarding its `item`.
     
     Map<String,List<Item>> map = ... ;
     {String*} uppercaseKeys = map.map(forKey(String.uppercased));"
see (function forItem)
tagged("Functions")
shared Result forKey<Key,Result>(Result resulting(Key key))
            (Key->Anything entry)
        given Key satisfies Object 
        => resulting(entry.key);
