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
 * An {@link ArgumentParser} for {@link ToolModel}s. This is useful for tools 
 * which operate on other tool's models, such as help tools and tool 
 * documentation generators. 
 * @author tom
 */
public class ToolModelArgumentParser implements EnumerableParser<ToolModel<?>>{

    private ToolLoader loader;

    protected ToolModelArgumentParser(ToolLoader loader) {
        this.loader = loader;
    }
    
    ToolLoader getToolLoader() {
        return loader;
    }

    @Override
    public ToolModel<?> parse(String argument, Tool tool) {
        ToolModel<Tool> model = loader.loadToolModel(argument);
        if (model == null) {
            throw new IllegalArgumentException(argument);
        }
        return model;
    }

    @Override
    public Iterable<String> possibilities() {
        return loader.getToolNames();
    }
    
}
