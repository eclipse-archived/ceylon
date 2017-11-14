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

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

public class AnnotatedToolModel<T extends Tool> extends ToolModel<T> {
    private Class<T> toolClass;
    private boolean loaded;
    
    public AnnotatedToolModel(String name) {
        super(name);
    }

    public Class<T> getToolClass() {
        return toolClass;
    }

    public void setToolClass(Class<T> toolClass) {
        this.toolClass = toolClass;
    }

    private void setup() {
        if (!loaded) {
            loaded = true;
            getToolLoader().setupModel(this);
        }
    }
    
    @Override
    public Collection<OptionModel<?>> getOptions() {
        setup();
        return super.getOptions();
    }

    @Override
    public OptionModel<?> getOption(String longName) {
        setup();
        return super.getOption(longName);
    }

    @Override
    public OptionModel<?> getOptionByShort(char shortName) {
        setup();
        return super.getOptionByShort(shortName);
    }

    @Override
    public List<ArgumentModel<?>> getArguments() {
        setup();
        return super.getArguments();
    }

    @Override
    public SubtoolModel<?> getSubtoolModel() {
        setup();
        return super.getSubtoolModel();
    }

    @Override
    public List<ArgumentModel<?>> getArgumentsAndSubtool() {
        setup();
        return super.getArgumentsAndSubtool();
    }

    @Override
    public Method getRest() {
        setup();
        return super.getRest();
    }
    
    public boolean isHidden() {
        return toolClass != null && toolClass.getAnnotation(Hidden.class) != null;
    }

    @Override
    public boolean isPorcelain() {
        return super.isPorcelain() && !isHidden();
    }

    @Override
    public boolean isPlumbing() {
        return super.isPlumbing() && (toolClass == null || isHidden());
    }
}
