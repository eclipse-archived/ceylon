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
    InterfaceDeclaration
}

"An interface model represents the model of a Ceylon interface that you can inspect.
 
 An interface model can be either a toplevel [[Interface]] or a member [[MemberInterface]].
 "
shared sealed interface InterfaceModel<out Type=Anything>
    satisfies ClassOrInterface<Type> {
    
    "The declaration model of this class, 
     which is necessarily an [[InterfaceDeclaration]]."
    shared formal actual InterfaceDeclaration declaration;
}
