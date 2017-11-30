/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"The singleton closed type for [[Nothing|ceylon.language::nothing]]."
shared object nothingType satisfies Type<Nothing> {
    
    string => "Nothing";
    
    typeOf(Anything instance) => false;
    
    exactly(Type<> type) => type == nothingType;
    
    supertypeOf(Type<> type) => exactly(type);
    
    subtypeOf(Type<> type) => true;
    
    shared actual Type<Other> union<Other>(Type<Other> type) => type;
    
    shared actual Type<Nothing> intersection<Other>(Type<Other> type) => this;
}
