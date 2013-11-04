/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.common.tools.help.model;

import java.util.Stack;

import com.redhat.ceylon.common.tool.SubtoolModel;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;

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
