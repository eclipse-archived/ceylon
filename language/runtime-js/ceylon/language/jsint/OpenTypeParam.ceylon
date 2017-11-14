/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.declaration {
  TypeParameter, OpenType, Variance, NestableDeclaration
}

shared native class OpenTypeParam(cntnr, _fname) satisfies TypeParameter {
    shared Object cntnr;
    shared actual native NestableDeclaration container;
    shared String _fname;
    shared native actual Boolean defaulted;
    shared native actual OpenType? defaultTypeArgument;
    shared native actual Variance variance;
    shared native actual OpenType[] satisfiedTypes;
    shared native actual OpenType[] caseTypes;
    shared native actual Boolean equals(Object other);
    shared native actual String qualifiedName;
    name=_fname[0:(_fname.firstOccurrence('$') else _fname.size)];
    shared actual String string=>"given " + qualifiedName;
    shared actual Integer hash =>string.hash;
}
