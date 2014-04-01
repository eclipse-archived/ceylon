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

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.impl.JULLogger;
import com.redhat.ceylon.compiler.loader.impl.reflect.ReflectionModelLoader;
import com.redhat.ceylon.compiler.loader.mirror.ClassMirror;
import com.redhat.ceylon.compiler.loader.model.LazyModule;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Modules;

/**
 * A model loader which uses Java reflection.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
// FIXME: we're still using a flat classpath here
public class CeylonDocModelLoader extends ReflectionModelLoader {

    ModulesClassLoader classLoader = new ModulesClassLoader(CeylonDocModelLoader.class.getClassLoader());
    Set<Module> modulesAddedToClassPath = new HashSet<Module>();

    public CeylonDocModelLoader(ModuleManager moduleManager, Modules modules){
        super(moduleManager, modules, new JULLogger());
    }

    @Override
    protected boolean needsLocalDeclarations() {
        return false;
    }
    
    @Override
    protected Class<?> loadClass(Module module, String name) {
        Class<?> klass = null;
        try {
            klass = classLoader.loadClass(name);
        } catch (ClassNotFoundException e) {
            // ignore
        }
        return klass;
    }

    @Override
    public void addModuleToClassPath(final Module module, ArtifactResult artifact) {
        // don't add the same module more than once
        if(artifact == null || !modulesAddedToClassPath.add(module))
            return;
        File file = artifact.artifact();
        // do not load classes from it if it's the language module, since it's already in our ClassLoader and
        // that would create multiple instances of the same class 
        classLoader.addJar(file, module == modules.getLanguageModule());
        log.debug("Adding jar to classpath: "+file);
        if(module instanceof LazyModule){
            ((LazyModule) module).loadPackageList(artifact);
        }
    }

    @Override
    protected List<String> getPackageList(Module module, String packageName) {
        return classLoader.getPackageList(packageName);
    }

    @Override
    protected boolean packageExists(Module module, String packageName) {
        return classLoader.packageExists(packageName);
    }

    @Override
    protected Module findModuleForClassMirror(ClassMirror classMirror) {
        String pkgName = getPackageNameForQualifiedClassName(classMirror);
        return lookupModuleByPackageName(pkgName);
    }
}
