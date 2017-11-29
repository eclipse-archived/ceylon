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
    NestableDeclaration
}

"The root of all models. There are several types of models:
 
 - [[ClassOrInterface]]
 - [[FunctionModel]]
 - [[ValueModel]]
 "
shared sealed interface Model 
            of ClassOrInterface<>
             | FunctionModel<> 
             | ValueModel<> 
        satisfies Declared {
    
    "The container type of this model, or `null` if this is a toplevel model."
    shared actual formal Type<>? container;
    
    "The declaration for this model."
    shared actual formal NestableDeclaration declaration;
}
