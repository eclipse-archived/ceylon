/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.tools.help.model;

import java.util.Stack;

import org.eclipse.ceylon.common.tool.SubtoolModel;
import org.eclipse.ceylon.common.tool.ToolLoader;
import org.eclipse.ceylon.common.tool.ToolModel;

public abstract class SubtoolVisitor {

    public static class ToolModelAndSubtoolModel {
        private final ToolModel<?> model;
        private final SubtoolModel<?> subtoolModel;

        public ToolModelAndSubtoolModel(ToolModel<?> model, SubtoolModel<?> subtoolModel) {
            this.model = model;
            this.subtoolModel = subtoolModel;
        }

        public String getName() {
            return model.getName();
        }
        
        public ToolModel<?> getModel() {
            return model;
        }

        public SubtoolModel<?> getSubtoolModel() {
            return subtoolModel;
        }

        public String toString() {
            return model.getName();
        }
    }
    
    protected final ToolModel<?> root;
    protected final Stack<ToolModelAndSubtoolModel> ancestors = new Stack<ToolModelAndSubtoolModel>();

    public SubtoolVisitor(ToolModel<?> root) {
        this.root = root;
    }
    
    public void accept() {
        visit(root, null);
        
        if (root.getSubtoolModel() != null) {
            accept(root, root.getSubtoolModel());
        }
    }

    protected abstract void visit(ToolModel<?> model, SubtoolModel<?> subtoolModel);

    protected void accept(ToolModel<?> parent, SubtoolModel<?> subtoolModel) {
        ancestors.push(new ToolModelAndSubtoolModel(parent, subtoolModel));
        ToolLoader subtoolLoader = subtoolModel.getToolLoader();
        for (String toolName : subtoolLoader.getToolNames()) {
            ToolModel<?> model = subtoolLoader.loadToolModel(toolName);
            visit(model, subtoolModel);
            if (model.getSubtoolModel() != null) {
                accept(model, model.getSubtoolModel());
            }
        }
        ancestors.pop();
    }
    
}
