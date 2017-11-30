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
    FunctionalDeclaration,
    NestableDeclaration
}
import ceylon.language.meta.model {
    ClosedType = Type
}

"""A function model represents the model of a Ceylon function that you can inspect.
   
   A function model can be either a toplevel [[Function]], a 
   [[CallableConstructor|callable constructor]] of a toplevel Class,
   a member [[Method]] or [[CallableMemberConstructor|callable constructor]]
   of a member class.
 """
shared sealed interface FunctionModel<out Type=Anything, in Arguments=Nothing>
        satisfies Model & Generic & Functional
        given Arguments satisfies Anything[] {

    "This function's declaration."
    shared formal actual NestableDeclaration&FunctionalDeclaration declaration;

    "This function's return closed type."
    shared formal ClosedType<Type> type;
}
