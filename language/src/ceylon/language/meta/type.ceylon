/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.model { ClassModel }

"Returns the closed type and model of a given instance. Since only classes
 can be instantiated, this will always be a [[ClassModel]] model."
see(function classDeclaration)
shared native ClassModel<Type,Nothing> type<out Type>(Type instance)
    given Type satisfies Anything;
