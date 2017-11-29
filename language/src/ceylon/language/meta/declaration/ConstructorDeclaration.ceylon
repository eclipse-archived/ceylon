/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"""Abstraction over [[callable constructors|CallableConstructorDeclaration]]
   and [[value constructors|ValueConstructorDeclaration]]
"""
since("1.2.0")
shared interface ConstructorDeclaration
        of CallableConstructorDeclaration|ValueConstructorDeclaration 
        satisfies NestableDeclaration {
    
}

