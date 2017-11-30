/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Abstraction for models of elements which must be qualified 
 by an instance to order to be evaluated, including:
 
 * [[Attribute]]s (a Qualified `Value`), 
 * [[Method]]s  (a Qualified `Function`), 
 * [[MemberClass|member classes]]  (a Qualified `Class`) and,
 * [[MemberClassConstructor|member constructors]]  (a constructor of a Qualified `Class`).
 
 To qualify a `Qualified` metamodel instance in a type-safe way you 
 simply invoke it. Alternatively use [[bind]] if the qualifying instance's 
 type is unknown until runtime.
"
since("1.2.0")
shared sealed interface Qualified<out Kind=Anything, in Container=Nothing> 
        satisfies Kind(Container) {
    
    "Type-unsafe container binding, to be used when the container type is unknown until runtime.
     
     A null argument is only permitted for `static` members (which 
     have no container instance). In all other cases a non-null container is required.
     
     This has the same behaviour as invoking this `Member` directly, but exchanges compile-time type
     safety with runtime checks."
    throws(class IncompatibleTypeException, "If the container is not assignable to this member's container")
    shared formal Kind bind(Anything container);
}