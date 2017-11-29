/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.model { Type, UnionType }

native class AppliedUnionType<out Union=Anything>(shared Anything tipo, caseTypes) satisfies UnionType<Union> {
  shared actual List<Type<Union>> caseTypes;

  shared native actual Boolean typeOf(Anything instance);
  shared native actual Boolean supertypeOf(Type<Anything> type);
  shared native actual Boolean exactly(Type<Anything> type);
  shared native actual String string;
  shared native actual Integer hash;
  shared native actual Boolean equals(Object other);

  shared actual native Type<Union|Other> union<Other>(Type<Other> other);
  shared actual native Type<Union&Other> intersection<Other>(Type<Other> other);
}
