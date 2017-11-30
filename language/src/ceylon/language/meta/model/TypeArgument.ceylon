/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.declaration {
    Variance
}
import ceylon.language.meta.model {
    ClosedType=Type
}
"A tuple representing a type argument and its use-site variance."
since("1.2.0")
shared alias TypeArgument => [ClosedType<>,Variance];
