/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"A declaration that can have type parameters."
shared sealed interface GenericDeclaration {
    
    "The list of type parameters declared on this generic declaration."
    shared formal TypeParameter[] typeParameterDeclarations;
    
    "Finds a type parameter by name. Returns `null` if not found."
    shared formal TypeParameter? getTypeParameterDeclaration(String name);
}