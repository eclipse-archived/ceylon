/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
class CycledIterator<Element>
                ({Element*} iterable, Integer times) 
        satisfies Iterator<Element> {
    
    variable Iterator<Element> iter = emptyIterator;
    variable Integer count=0;
    
    shared actual Element|Finished next() {
        if (!is Finished next = iter.next()) {
            return next;
        }
        else {
            if (count<times) {
                count++;
                iter = iterable.iterator();
            }
            else {
                iter = emptyIterator;
            }
            return iter.next();
        }
        
    }
    
    string => "``iterable``.repeat(``times``).iterator()";
    
}
