/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"The Ceylon language module containing the core definitions 
 referred to by the [language specification][spec], along 
 with some basic functionality of use to most programs:
 
 - The [[root package|package ceylon.language]] defines 
   general-purpose functionality including support for 
   [[numbers|Numeric]] and [[character strings|String]], 
   [[streams|Iterable]] and [[sequences|Sequential]], 
   [[exceptions|Throwable]], and [[null values|Null]].
 - The Ceylon _metamodel_ is defined in 
   [[package ceylon.language.meta]] and its subpackages 
   [[package ceylon.language.meta.model]] and 
   [[package ceylon.language.meta.declaration]], which
   define interfaces for interacting with applied types and 
   unapplied type declarations respectively.
 
 This module defines an abstraction over the basic 
 facilities of the Java or JavaScript virtual machine, 
 containing only functionality that can be easily 
 implemented on both platforms. Thus, certain functionality, 
 for example, concurrency, for which there is no common
 virtual machine-agnostic model, is not covered by the
 language module.
 
 The language module is an implicit dependency of every
 other Ceylon module, and may not be explicitly imported.
 
 [spec]: http://ceylon-lang.org/documentation/current/spec"
label ("Ceylon Language Module")
by ("Gavin King", "Tom Bentley", "Tako Schotanus",
   "Stephane Epardaud", "Enrique Zamudio")
license ("http://www.apache.org/licenses/LICENSE-2.0.html")
module ceylon.language maven:"org.ceylon-lang" "1.3.4-SNAPSHOT"/*@CEYLON_VERSION@*/ {
    native("jvm") import java.base "7";
}
