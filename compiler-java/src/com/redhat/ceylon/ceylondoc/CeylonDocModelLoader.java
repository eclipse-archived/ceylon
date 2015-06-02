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

import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleSourceMapper;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.loader.LoaderJULLogger;
import com.redhat.ceylon.model.loader.impl.reflect.ReflectionModelLoader;
import com.redhat.ceylon.model.loader.mirror.ClassMirror;
import com.redhat.ceylon.model.loader.mirror.MethodMirror;
import com.redhat.ceylon.model.loader.model.AnnotationProxyClass;
import com.redhat.ceylon.model.loader.model.AnnotationProxyMethod;
import com.redhat.ceylon.model.loader.model.LazyFunction;
import com.redhat.ceylon.model.loader.model.LazyModule;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Modules;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.UnknownType;
import com.redhat.ceylon.model.typechecker.model.UnknownType.ErrorReporter;
import com.redhat.ceylon.model.typechecker.util.ModuleManager;

/**
 * A model loader which uses Java reflection.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
// FIXME: we're still using a flat classpath here
public class CeylonDocModelLoader extends ReflectionModelLoader {

    ModulesClassLoader classLoader = new ModulesClassLoader(CeylonDocModelLoader.class.getClassLoader());
    Set<Module> modulesAddedToClassPath = new HashSet<Module>();
    private CeylonDocTool tool;

    public CeylonDocModelLoader(ModuleManager moduleManager, Modules modules, CeylonDocTool tool){
        super(moduleManager, modules, new LoaderJULLogger());
        this.tool = tool;
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
        classLoader.addJar(artifact, module, module == modules.getLanguageModule());
        log.debug("Adding jar to classpath: "+file);
        if(module instanceof LazyModule){
            ((LazyModule) module).loadPackageList(artifact);
        }
    }

    @Override
    protected List<String> getPackageList(Module module, String packageName) {
        return classLoader.getPackageList(module, packageName);
    }

    @Override
    protected boolean packageExists(Module module, String packageName) {
        return classLoader.packageExists(module, packageName);
    }

    @Override
    public Module findModuleForClassMirror(ClassMirror classMirror) {
        String pkgName = getPackageNameForQualifiedClassName(classMirror);
        return lookupModuleByPackageName(pkgName);
    }

    @Override
    protected ErrorReporter makeModelErrorReporter(Module module, String message) {
        return new ModuleErrorAttacherRunnable(tool.getModuleSourceMapper(), module, message);
    }

    public static class ModuleErrorAttacherRunnable extends UnknownType.ErrorReporter {

        private Module module;
        private ModuleSourceMapper moduleSourceMapper;

        public ModuleErrorAttacherRunnable(ModuleSourceMapper moduleSourceMapper, Module module, String message) {
            super(message);
            this.moduleSourceMapper = moduleSourceMapper;
            this.module = module;
        }

        @Override
        public void reportError() {
            moduleSourceMapper.attachErrorToOriginalModuleImport(module, getMessage());
        }
    }

    @Override
    protected void setAnnotationConstructor(LazyFunction method, MethodMirror meth) {
        // nothing to do
    }

    @Override
    protected void makeInteropAnnotationConstructorInvocation(AnnotationProxyMethod ctor, AnnotationProxyClass klass, List<Parameter> ctorParams) {
        // nothing to do
    }
}
