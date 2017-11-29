/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"An open type.
 
 An open type is a type which may contain unbound type variables, such as `List<T>`."
shared sealed interface OpenType of OpenClassOrInterfaceType
                       | OpenTypeVariable
                       | OpenUnion
                       | OpenIntersection
                       | nothingType {}
