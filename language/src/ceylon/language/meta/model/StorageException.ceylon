/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Thrown when you try to read attributes that were not shared nor captured and had no
 physical storage allocated, so do not exist at runtime.
 
 For example if you try to read the attribute from this class:
 
     shared class Foo(){
         Integer x = 2;
     }
 
 This will not work because `x` is neither shared nor captured and so it is just not
 retained in the runtime instances of `Foo`.
 "
since("1.2.0")
shared class StorageException(String message) extends Exception(message){}
