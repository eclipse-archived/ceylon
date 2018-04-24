/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"An [[Iterator]] that returns the elements of two
 [[Iterable]]s, as if they were chained together."
see (function Iterable.chain)
by ("Enrique Zamudio")
tagged("Streams")
class ChainedIterator<out Element,out Other>
                ({Element*} first, {Other*} second) 
        satisfies Iterator<Element|Other> {
    
    variable Iterator<Element|Other> iter = first.iterator();
    variable value more = true;
    
    shared actual Element|Other|Finished next() {
        value element = iter.next();
        if (more && element is Finished) {
            iter = second.iterator();
            more = false;
            return iter.next();
        }
        else {
            return element;
        }
    }
    
    string => "``first``.chain(``second``).iterator()";
}
