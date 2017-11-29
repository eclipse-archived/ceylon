/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"The default superclass when no superclass is explicitly
 specified using `extends`. For the sake of convenience, 
 this class inherits [[Identifiable]] along with its
 [[default definition|Identifiable.equals]] of value 
 equality. Classes which aren't `Identifiable` should 
 directly extend [[Object]]."
by ("Gavin")
tagged("Basic types")
shared abstract class Basic() 
        extends Object() satisfies Identifiable {}