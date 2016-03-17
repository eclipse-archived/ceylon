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

public class SynopsesSection implements Documentation {
    
    private String title;
    
    private List<Synopsis> synopses = Collections.emptyList();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Synopsis> getSynopses() {
        return synopses;
    }

    public void setSynopses(List<Synopsis> synopses) {
        this.synopses = synopses;
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.startSynopses(this);
        for (Synopsis synopsis : synopses) {
            synopsis.accept(visitor);
        }
        visitor.endSynopses(this);
    }
}
