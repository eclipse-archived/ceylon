/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Rethrows an exception without declaring it. This is used as a cheap replacement for
 Unsafe.throwException() in our Java Util class, used by the metamodel."
by ("Stephane Epardaud")
shared void rethrow(Throwable x){
    throw x;
}