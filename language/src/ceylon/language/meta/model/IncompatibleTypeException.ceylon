/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Thrown when you invoke metamodel methods with invalid or incompatible type arguments.
 
 For example if you try to get an attribute from a class and expect an attribute of `String`
 type but it is an attribute of `Integer` type.
 "
shared class IncompatibleTypeException(String message) extends Exception(message){}
