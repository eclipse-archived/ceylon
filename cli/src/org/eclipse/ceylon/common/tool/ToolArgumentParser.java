/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.tool;


/**
 * An {@link ArgumentParser} for {@link Tool}s. This is used for subtools
 * @author tom
 */
public class ToolArgumentParser implements EnumerableParser<Tool>{

    private ToolLoader loader;

    protected ToolArgumentParser(ToolLoader loader) {
        this.loader = loader;
    }
    
    ToolLoader getToolLoader() {
        return loader;
    }

    @Override
    public Tool parse(final String argument, Tool tool) {
        Tool instance = loader.instance(argument, tool);
        if (instance == null) {
            throw new IllegalArgumentException(argument);
        }
        return instance;
    }

    @Override
    public Iterable<String> possibilities() {
        return loader.getToolNames();
    }
    
}
