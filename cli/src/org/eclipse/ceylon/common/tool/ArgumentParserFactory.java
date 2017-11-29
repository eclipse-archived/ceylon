/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.tool;

import java.util.ServiceLoader;

public abstract class ArgumentParserFactory {

    private static final ServiceLoader<ArgumentParserFactory> parserFactories;
    
    static {
        parserFactories = ServiceLoader.load(ArgumentParserFactory.class, ArgumentParserFactory.class.getClassLoader());
    }
    
    public abstract ArgumentParser<?> forClass(Class<?> setterType, ToolLoader toolLoader, boolean isSimpleType);
    
    public static ArgumentParser<?> instance(Class<?> setterType, ToolLoader toolLoader, boolean isSimpleType) {
        for (ArgumentParserFactory parserFactory : parserFactories) {
            ArgumentParser<?> parser = parserFactory.forClass(setterType, toolLoader, isSimpleType);
            if (parser != null) {
                return parser;
            }
        }
        return null;
    }
    
}
