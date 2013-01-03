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
package com.redhat.ceylon.tools.help.model;

import org.tautua.markdownpapers.ast.Node;

import com.redhat.ceylon.common.tool.OptionModel;
import com.redhat.ceylon.common.tool.OptionModel.ArgumentType;

public class Option implements Documentation {

    private OptionModel<?> option;
    
    private Node description;

    public OptionModel<?> getOption() {
        return option;
    }

    public void setOption(OptionModel<?> option) {
        this.option = option;
    }

    public Node getDescription() {
        return description;
    }

    public void setDescription(Node description) {
        this.description = description;
    }

    public String getLongName() {
        String longName = option.getLongName() != null ? "--" + option.getLongName() : null;
        return longName;
    }

    public String getShortName() {
        String shortName = option.getShortName() != null ? "-" + option.getShortName() : null;
        return shortName;
    }

    public String getArgumentName() {
        String argumentName = option.getArgumentType() == ArgumentType.NOT_ALLOWED ? null : option.getArgument().getName();
        return argumentName;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitOption(this);
    }
    
}
