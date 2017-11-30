/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Thrown when declarations are applied with invalid or incompatible type arguments.
 Also throw when trying to apply member declarations with no containers, or toplevel
 declarations with a container.
 
 For example if you try to apply `Foo` with `String`, hoping to get a `Foo<String>`
 but the type parameter for `Foo` only accepts types that satisfy `Numeric`.
 "
shared class TypeApplicationException(String message) extends Exception(message){}
