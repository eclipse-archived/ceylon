/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"A declaration.
 
 There are only two types of declarations:
 
 - [[AnnotatedDeclaration]]s such as modules, packages, classes or functions, and
 - [[TypeParameter]] declarations."
shared sealed interface Declaration of AnnotatedDeclaration
                              | TypeParameter {
    
    "The name of this declaration. For example, the [[Declaration]] class is named \"Declaration\"."
    shared formal String name;
    
    "The qualified name of this declaration. This includes the container qualified name. For
     example, the [[Declaration]] class' qualified name is \"ceylon.language.meta.declaration::Declaration\"."
    shared formal String qualifiedName;
}
