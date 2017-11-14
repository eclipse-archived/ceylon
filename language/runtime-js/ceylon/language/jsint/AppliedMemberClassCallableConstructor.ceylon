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
  CallableConstructorDeclaration, TypeParameter
}
import ceylon.language.meta.model {
  MemberClassCallableConstructor, MemberClass, ClassModel, CallableConstructor,
  ClosedType=Type, TypeArgument
}

shared native class AppliedMemberClassCallableConstructor<in Container=Nothing, out Type=Object, in Arguments=Nothing>()
    satisfies MemberClassCallableConstructor<Container,Type,Arguments>
//        satisfies FunctionModel<Type, Arguments> & Qualified<CallableConstructor<Type, Arguments>, Container>
    given Arguments satisfies Anything[] {

  shared native actual CallableConstructorDeclaration declaration;
  shared native actual MemberClass<Container, Type> type;
  shared native actual ClassModel<Type> container;
  shared native actual CallableConstructor<Type, Arguments> bind(Anything container);
  //Generic
  shared native actual Map<TypeParameter, ClosedType<>> typeArguments;
  shared native actual ClosedType<Anything>[] typeArgumentList;
  shared actual native Map<TypeParameter, TypeArgument> typeArgumentWithVariances;
  shared actual native TypeArgument[] typeArgumentWithVarianceList;
  //Functional
  shared native actual ClosedType<Anything>[] parameterTypes;

  shared native actual String string;
  shared native actual Boolean equals(Object other);
  shared native actual Integer hash;
}
