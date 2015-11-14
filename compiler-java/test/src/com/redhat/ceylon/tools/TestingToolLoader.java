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
package com.redhat.ceylon.tools;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;
import java.util.Vector;

import com.redhat.ceylon.common.tool.ToolException;
import com.redhat.ceylon.common.tools.CeylonToolLoader;

public class TestingToolLoader extends CeylonToolLoader {
    
    private boolean includeParentServices;

    public TestingToolLoader(ClassLoader loader, boolean includeParentServices) {
        super(loader);
        this.includeParentServices = includeParentServices;
    }

    /**
     * Make sure we don't look at the current user's path for script plugins
     */
    @Override
    protected Set<String> getPathPlugins() {
        return Collections.emptySet();
    }
    
    @Override
    protected Enumeration<URL> getServiceMeta() {
        Vector<URL> result = new Vector<>();
        if (includeParentServices) {
            Enumeration<URL> resources = super.getServiceMeta();
            while (resources.hasMoreElements()) {
                result.add(resources.nextElement());
            }
        }
        
        try {
            Enumeration<URL> resources = loader.getResources(CeylonExampleTool.class.getName().replace(".", "/")+".properties");
            while (resources.hasMoreElements()) {
                result.add(resources.nextElement());
            }
        } catch (IOException e) {
            throw new ToolException(e);
        }
        
        return result.elements();
    }
}