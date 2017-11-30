/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"""The abstract supertype of all types representing definite 
   values. Any two values which are assignable to `Object` 
   may be compared for value equality using the `==` and `!=` 
   operators, even if the values are of different concrete 
   type:
   
       true == false
       1 == "hello world"
       "hello"+" "+"world" == "hello world"
       Singleton("hello world") == ["hello world"]
   
   However, since [[Null]] is not a subtype of `Object`, the
   value [[null]] cannot be compared to any other value 
   using the `==` operator. Thus, value equality is not 
   defined for optional types. This neatly bypasses the 
   problem of deciding the value of the expression 
   `null==null`, which is simply illegal.
   
   A concrete subclass of `Object` must refine [[equals]] 
   and [[hash]] (or inherit concrete refinements), providing 
   a concrete definition of value equality for the class.
   
   In extreme cases it is acceptable for two values to be
   equal even when they are not instances of the same class.
   For example, the [[Integer]] value `1` and the [[Float]]
   value `1.0` are considered equal. Except in these extreme
   cases, instances of different classes are considered
   unequal."""
see (class Basic, class Null)
by ("Gavin")
tagged("Basic types")
shared abstract class Object() 
        extends Anything() {
    
    "Determine if two values are equal.
     
     For any two non-null objects `x` and `y`, `x.equals(y)`
     may be written as:
     
         x == y 
     
     Implementations should respect the constraints that:
     
     - if `x===y` then `x==y` (reflexivity), 
     - if `x==y` then `y==x` (symmetry), 
     - if `x==y` and `y==z` then `x==z` (transitivity).
     
     Furthermore it is recommended that implementations
     ensure that if `x==y` then `x` and `y` have the same 
     concrete class.
     
     A class which explicitly refines `equals()` is said to 
     support _value equality_, and the equality operator 
     `==` is considered much more meaningful for such 
     classes than for a class which simply inherits the
     default implementation of _identity equality_ from
     [[Identifiable]].
     
     Note that an implementation of `equals()` that always
     returns [[false]] does satisfy the constraints given
     above, as long as the class does _not_ inherit 
     [[Identifiable]]. Therefore, in very rare cases where 
     there is no reasonable definition of value equality for 
     a class, for example, [[function references|Callable]], 
     it is acceptable for `equals()` to be defined to return 
     `false` for every argument."
    shared formal Boolean equals(Object that);
    
    "The hash value of the value, which allows the value to 
     be an element of a hash-based set or key of a
     hash-based map. Implementations must respect the
     constraint that:
     
     - if `x==y` then `x.hash==y.hash`.
     
     Therefore, a class which refines [[equals]] must also
     refine `hash`.
     
     In general, `hash` values vary between platforms and
     between executions of the same program.
     
     Note that when executing on a Java Virtual Machine, the 
     64-bit [[Integer]] value returned by an implementation 
     of `hash` is truncated to a 32-bit integer value by 
     taking the exclusive disjunction of the 32 lowest-order
     bits with the 32 highest-order bits, before returning
     the value to the caller."
    see (function identityHash)
    shared formal Integer hash;
    
    "A developer-friendly string representing the instance. 
     Concatenates the name of the concrete class of the 
     instance with the `hash` of the instance. Subclasses 
     are encouraged to refine this implementation to produce 
     a more meaningful representation."
    shared default String string
            => className(this) + "@" + 
               Integer.format(hash, #10);
    
}