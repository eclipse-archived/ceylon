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

public class DescribedSection implements Documentation {

    public static enum Role {
        DESCRIPTION,
        ADDITIONAL
    }
    
    private Role role;
    
    private Node title;
    
    private Object about;
    
    private Node description;
    
    private List<DescribedSection> subsections = Collections.emptyList();

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Node getTitle() {
        return title;
    }

    public void setTitle(Node title) {
        this.title = title;
    }

    public Node getDescription() {
        return description;
    }

    public void setDescription(Node content) {
        this.description = content;
    }

    public Object getAbout() {
        return about;
    }

    public void setAbout(Object about) {
        this.about = about;
    }

    public List<DescribedSection> getSubsections() {
        return subsections;
    }

    public void setSubsections(List<DescribedSection> subsections) {
        this.subsections = subsections;
    }

    @Override
    public void accept(Visitor visitor) {
        switch (role) {
        case DESCRIPTION:
            visitor.visitDescription(this);
            break;
        case ADDITIONAL:
            visitor.visitAdditionalSection(this);
            break;
        default:
            throw new RuntimeException();
        }
    }
    
}
