/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.declaration{
  ValueConstructorDeclaration
}
import ceylon.language.meta.model {
  MemberClassValueConstructor, MemberClass, ClassModel, ValueConstructor
}

shared native class AppliedMemberClassValueConstructor<in Container=Nothing, out Type=Object>()
    satisfies MemberClassValueConstructor<Container,Type> {
//        satisfies ValueModel<Type, Set> & Qualified<ValueConstructor<Type, Set>, Container> {
    
  shared native actual ValueConstructorDeclaration declaration;
  shared native actual MemberClass<Container, Type> type;
  shared native actual ClassModel<Type> container;
  //throws(`class StorageException`,
  //    "If this attribute is not stored at runtime, for example if it is neither shared nor captured.")
  shared native actual ValueConstructor<Type> bind(Anything container);

  shared native actual String string;
  shared native actual Boolean equals(Object other);
  shared native actual Integer hash;
}
