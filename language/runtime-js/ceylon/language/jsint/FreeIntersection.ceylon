/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.declaration { OpenIntersection, OpenType }

native class FreeIntersection(satisfiedTypes) satisfies OpenIntersection {
    shared actual List<OpenType> satisfiedTypes;
    shared actual native Boolean equals(Object other);
    shared actual native String string;
    shared actual native Integer hash;
}
