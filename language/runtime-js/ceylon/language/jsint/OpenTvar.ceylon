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
    OpenTypeVariable, TypeParameter
}

native class OpenTvar(declaration) satisfies OpenTypeVariable {
    shared actual TypeParameter declaration;
    shared actual native Boolean equals(Object other);
    // WARNING failure to make these lazy causes infinite recursion. I suppose the declaration
    // can refer to us when we get its qualified name?
    string=>declaration.qualifiedName;
    hash=>string.hash;
}

