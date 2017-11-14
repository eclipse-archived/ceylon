/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.declaration { Package, Import, Module }
import ceylon.language.meta.model { ClassOrInterface }
import ceylon.language{AnnotationType = Annotation}

shared native class Modulo(shared Anything meta) satisfies Module {
    shared actual native String name;
    qualifiedName = name;
    shared actual native String version;
    shared actual native Package[] members;
    shared actual native Import[] dependencies;
    shared actual native Package? findPackage(String name);
    shared actual native Package? findImportedPackage(String name);
    shared actual native Resource? resourceByPath(String path);
    shared actual native Annotation[] annotations<out Annotation>()
        given Annotation satisfies AnnotationType;
    shared actual native Boolean annotated<Annotation>()
            given Annotation satisfies AnnotationType;
    string = "module " + name + "/" + version;
    shared actual native {Service*} findServiceProviders<Service>(ClassOrInterface<Service> service) => empty;
}
