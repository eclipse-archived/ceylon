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
 [[function|resulting]] to the [[item|Entry.item]] 
 of a given [[Entry]], discarding its `key`.
     
     Map<String,List<Item>> map = ... ;
     {Item?*} topItems = map.map(forItem(List<Item>.first));"
see (function forKey)
tagged("Functions")
shared Result forItem<Item,Result>(Result resulting(Item item))
            (Object->Item entry)
        => resulting(entry.item);
