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
  OpenClassType, ClassDeclaration, OpenInterfaceType,
  TypeParameter, OpenType, OpenTypeArgument
}

shared native class FreeClass(declaration)
        satisfies OpenClassType {
    shared actual ClassDeclaration declaration;
    shared native actual OpenClassType? extendedType;
    shared native actual OpenInterfaceType[] satisfiedTypes;
    shared native actual Map<TypeParameter, OpenType> typeArguments;
    shared native actual OpenType[] typeArgumentList;
    shared native actual Map<TypeParameter, OpenTypeArgument> typeArgumentWithVariances;
    shared native actual OpenTypeArgument[] typeArgumentWithVarianceList;
    shared native actual Boolean equals(Object other);
    shared native actual String string;
    shared actual Integer hash => string.hash;
}
