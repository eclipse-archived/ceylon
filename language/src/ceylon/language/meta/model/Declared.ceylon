/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.declaration{
    Declaration,Package
}
"A model element that has a declaration."
since("1.2.0")
shared sealed interface Declared {
    
    "The declaration model of this model."
    shared formal Declaration declaration;
    
    "The container type of this model, or `null` if this is a toplevel model."
    shared formal Type<>|Package? container;
}