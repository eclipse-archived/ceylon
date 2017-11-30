/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.declaration {
    TypeParameter
}
import ceylon.language.meta.model {
    ClosedType = Type
}

"A generic model which has closed type arguments."
shared sealed interface Generic {
    "The map of type parameter declaration to type arguments for this generic model."
    shared formal Map<TypeParameter, ClosedType<>> typeArguments;
    "The list of type arguments for this generic model."
    since("1.2.0")
    shared formal ClosedType<>[] typeArgumentList;

    "The map of type parameter declaration to type arguments and use-site variance for this generic model."
    since("1.2.0")
    shared formal Map<TypeParameter, TypeArgument> typeArgumentWithVariances;
    "The list of type arguments for this generic model."
    since("1.2.0")
    shared formal TypeArgument[] typeArgumentWithVarianceList;
}