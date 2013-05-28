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

package com.redhat.ceylon.compiler.java.loader.model;

import java.util.List;

import com.redhat.ceylon.compiler.java.loader.CeylonEnter;
import com.redhat.ceylon.compiler.java.loader.CeylonModelLoader;
import com.redhat.ceylon.compiler.loader.model.LazyModuleManager;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.model.Module;

public class CompilerModuleManager extends LazyModuleManager {

    private com.sun.tools.javac.util.Context context;
    private CeylonEnter ceylonEnter;
    private CeylonModelLoader modelLoader;

    public CompilerModuleManager(Context ceylonContext, com.sun.tools.javac.util.Context context) {
        super(ceylonContext);
        this.context = context;
    }

    @Override
    protected Module createModule(List<String> moduleName, String version) {
        CompilerModule module = new CompilerModule(context);
        module.setName(moduleName);
        module.setVersion(version);
        setupIfJDKModule(module);
        return module;
    }

    public CeylonEnter getCeylonEnter() {
        if (ceylonEnter == null) {
            ceylonEnter = CeylonEnter.instance(context);
        }
        return ceylonEnter;
    }

    protected CeylonModelLoader getModelLoader() {
        if (modelLoader == null) {
            modelLoader = (CeylonModelLoader) CeylonModelLoader.instance(context);
        }
        return modelLoader;
    }
}
