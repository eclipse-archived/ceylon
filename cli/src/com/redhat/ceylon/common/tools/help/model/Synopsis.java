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

import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.common.tool.ArgumentModel;
import com.redhat.ceylon.common.tool.OptionModel;


public class Synopsis {

    private String invocation;
    
    private List<?> optionsAndArguments = Collections.emptyList();
    
    public List<?> getOptionsAndArguments() {
        return optionsAndArguments;
    }

    public void setOptionsAndArguments(List<?> options) {
        this.optionsAndArguments = options;
    }

    public String getInvocation() {
        return invocation;
    }

    public void setInvocation(String invocation) {
        this.invocation = invocation;
    }

    public void accept(Visitor visitor) {
        visitor.startSynopsis(this);
        for (Object o : getOptionsAndArguments()) {
            if (o instanceof OptionModel<?>) {
                visitor.visitSynopsisOption((OptionModel<?>)o);
            } else if (o instanceof SubtoolVisitor.ToolModelAndSubtoolModel) {
                visitor.visitSynopsisSubtool((SubtoolVisitor.ToolModelAndSubtoolModel)o);
            } else if (o instanceof ArgumentModel<?>) {
                visitor.visitSynopsisArgument((ArgumentModel<?>)o);
            } 
        }
        visitor.endSynopsis(this);
    }

}
