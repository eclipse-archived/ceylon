/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"The root package of the Ceylon language module, functioning
 as the core of the [Ceylon platform][sdk], and covering the 
 following areas of functionality:
 
 - the foundational types [[Anything]], [[Object]], and 
   [[Null]], and a [[logical boolean|Boolean]] type,
 - abstractions of [[numeric|Numeric]] types, along with
   basic types representing [[integral|Integer]] and 
   [[floating point|Float]] values,
 - [[characters|Character]] and [[character strings|String]],
 - [[unsigned bytes|Byte]],
 - support for functional programming with 
   [[streams|Iterable]],
 - abstract interfaces for unmodifiable [[lists|List]],
   [[sets|Set]], and [[maps|Map]],
 - a low-level abstraction of native [[arrays|Array]],
 - immutable [[sequences|Sequential]], [[ranges|Range]], and
   [[tuples|Tuple]], which provide the foundation for 
   representing [[function types|Callable]] and lists of
   function arguments,
 - generic higher-order functions, including for function 
   [[composition|compose]] and [[partial application|curry]],
 - [[exceptions|Throwable]] and support for management of
   heavyweight [[destroyable|Destroyable]] and 
   [[obtainable|Obtainable]] objects,
 - support for loading [[resources|Resource]] packaged with
   a module,
 - access to information about the current 
   [[virtual machine|runtime]], [[system|system]],
   [[process]], and [[operating system|system]], and
 - support for definition of [[annotations|Annotation]].
 
 Finally, this module defines the [[most useful and
 interesting void function of all time ever|print]].
 
 Declarations belonging to this package need not be 
 explicitly imported by other source files.
 
 [sdk]: https://modules.ceylon-lang.org/categories/SDK"
by ("Gavin King", "Tom Bentley", "Tako Schotanus", 
    "Stephane Epardaud", "Enrique Zamudio")
shared package ceylon.language;
