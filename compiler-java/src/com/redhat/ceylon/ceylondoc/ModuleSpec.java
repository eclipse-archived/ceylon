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
package com.redhat.ceylon.ceylondoc;

import java.util.LinkedList;
import java.util.List;

public class ModuleSpec {
    public String name, version;
    
    public ModuleSpec(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public static List<ModuleSpec> parse(List<String> moduleSpecs){
        List<ModuleSpec> modules = new LinkedList<ModuleSpec>();
        for(String moduleSpec : moduleSpecs){
            int sep = moduleSpec.indexOf("/");
            String name = sep != -1 ? moduleSpec.substring(0, sep) : moduleSpec;
            String version = sep != -1 ? moduleSpec.substring(sep+1) : null;
            modules.add(new ModuleSpec(name, version));
        }
        return modules;
    }
}
