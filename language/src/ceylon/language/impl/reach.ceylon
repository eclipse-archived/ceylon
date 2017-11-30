/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.serialization{ReachableReference}

"A native way to find out about the references an instance holds
 and to get an instance."
shared native object reach {
    shared native Iterator<ReachableReference> references(Anything instance);
    shared native Anything getAnything(Anything instance, ReachableReference ref);
    shared native Object getObject(Anything instance, ReachableReference ref);
    string => "reach";
}