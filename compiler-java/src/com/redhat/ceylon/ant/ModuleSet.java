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

package com.redhat.ceylon.ant;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;

/**
 * Runtime representation of a {@code <moduleset>} element, which can be 
 * defined at the top level of an ant project and referred to by other tasks.
 */
@AntDoc("A `<moduleset>` containing a number of `<sourcemodule>`s "
        + "and/or `<module>` subelements. It can be defined at the top level "
        + "and then used by reference using the `refid` attribute, "
        + "so you don't have to repeat the same list of modules all the time.")
public class ModuleSet extends DataType {

    private final LinkedHashSet<Module> modules = new LinkedHashSet<Module>();
    private final LinkedHashSet<Pattern> patterns = new LinkedHashSet<Pattern>();
    
    @Override
    @AntDoc("A reference to a [`<moduleset>`](../ant/#_moduleset) defined elsewhere.")
    public void setRefid(Reference reference) {
        super.setRefid(reference);
    }
    @AntDocIgnore
    public void addConfiguredModule(Module module) {
        this.modules.add(module);
    }
    @AntDocIgnore
    public void addConfiguredSourceModules(SourceModules sourceModules) {
        this.modules.addAll(sourceModules.getModules());
    }
    
    @AntDocIgnore
    public void addConfiguredModuleSet(ModuleSet moduleset) {
        this.modules.addAll(moduleset.getModules());
    }
    @AntDocIgnore
    public void addConfiguredPattern(Pattern pattern) {
        this.patterns.add(pattern);
    }
    
    public Set<Module> getModules() {
        LinkedHashSet<Module> result = new LinkedHashSet<Module>();        
        result.addAll(this.modules);
        if (getRefid() != null) {
            ModuleSet referredModuleSet = (ModuleSet)getProject().getReference(getRefid().getRefId());    
            result.addAll(referredModuleSet.getModules());
        }
        for (Pattern p : patterns) {
            result.addAll(p.getModules());
        }
        return result;
    }

    @Override
    public String toString() {
        return "Modules: " + getModules().toString();
    }
}
