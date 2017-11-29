/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
native class PartialImpl(Object id) extends Partial(id) {
    shared native actual void instantiate();
    shared native actual void initialize<Id>(DeserializationContextImpl<Id> context)
            given Id satisfies Object;
}
