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

import java.util.Collections;
import java.util.List;

import org.tautua.markdownpapers.ast.Node;

public class OptionsSection implements Documentation {
    
    private Node title;
    
    private List<Option> options = Collections.emptyList();
    
    private List<OptionsSection> subsections = Collections.emptyList();

    public Node getTitle() {
        return title;
    }

    public void setTitle(Node title) {
        this.title = title;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
    
    public List<OptionsSection> getSubsections() {
        return subsections;
    }

    public void setSubsections(List<OptionsSection> subsections) {
        this.subsections = subsections;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.startOptions(this);
        for (Option option : options) {
            option.accept(visitor);
        }
        for (OptionsSection subsection : subsections) {
            subsection.accept(visitor);
        }
        visitor.endOptions(this);
    }

}
