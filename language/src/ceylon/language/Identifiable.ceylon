/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"The abstract supertype of all types with a well-defined
 notion of identity. Values of type `Identifiable` may be 
 compared using the `===` operator to determine if they are 
 references to the same object instance.
 
 For the sake of convenience, this interface defines a 
 default implementation of value equality equivalent to 
 identity. Of course, subtypes are encouraged to refine this 
 implementation."
by ("Gavin")
tagged("Basic types")
shared interface Identifiable {
    
    "Identity equality comparing the identity of the two 
     values. May be refined by subtypes for which value 
     equality is more appropriate. Implementations must
     respect the constraint that if `x===y` then `x==y` 
     (equality is consistent with identity)."
    shared default actual Boolean equals(Object that)
            => if (is Identifiable that)
                    then this===that
                    else false;
    
    "The system-defined [[identity hash value|identityHash]] 
     of this instance. Subtypes which refine [[equals]] must 
     also refine `hash`, according to the general contract 
     defined by [[Object.equals]]."
    see (function identityHash)
    shared default actual Integer hash => identityHash(this);
    
}