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
    GettableDeclaration,
    NestableDeclaration
}
import ceylon.language.meta.model {
    ClosedType = Type
}

"""A value model represents the model of a Ceylon value that you can inspect.
   
   A value model can be either a toplevel [[Value]] or a member [[Attribute]].
 """
shared sealed interface ValueModel<out Get=Anything, in Set=Nothing>
        satisfies Model {

    "This value's declaration."
    shared formal actual NestableDeclaration&GettableDeclaration declaration;
    
    "This value's closed type."
    shared formal ClosedType<Get> type;
}
