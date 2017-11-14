/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.model {
  Value, ClosedType=Type
}
import ceylon.language.meta.declaration {
  ValueDeclaration
}

shared native class AppliedValue<out Get=Anything, in Set=Nothing>() satisfies Value<Get,Set> {
  shared native actual Get get();
  shared native actual void set(Set newValue);
  shared native actual void setIfAssignable(Anything newValue);
  shared native actual default ValueDeclaration declaration;
  shared native actual ClosedType<Get> type;
  shared native actual ClosedType<Anything>? container;
  shared native actual String string;
  shared native actual Integer hash;
  shared native actual Boolean equals(Object other);
}
