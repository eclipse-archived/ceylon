/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Declaration which has an open type."
shared sealed interface TypedDeclaration {
    
    "The open type for this declaration. For example, the open type for `List<T> f<T>()` is `List<T>`."
    shared formal OpenType openType;
}