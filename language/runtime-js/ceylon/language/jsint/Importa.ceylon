/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.declaration { Import, Module }
import ceylon.language { AnnotationType = Annotation }

native class Importa(name, version, shared Module _cont, shared Anything _anns) satisfies Import {
    shared actual String name;
    shared actual String version;
    shared actual native Boolean shared;
    shared actual native Boolean optional;
    shared actual native Module container;
    shared actual native Boolean annotated<Annotation>()
            given Annotation satisfies AnnotationType;
    shared actual String string => "import ``name``/``version``";
}
