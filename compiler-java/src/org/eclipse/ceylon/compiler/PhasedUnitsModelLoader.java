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

package org.eclipse.ceylon.compiler;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import org.eclipse.ceylon.compiler.typechecker.analyzer.ModuleSourceMapper;
import org.eclipse.ceylon.compiler.typechecker.context.PhasedUnits;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.loader.JdkProvider;
import org.eclipse.ceylon.model.loader.LoaderJULLogger;
import org.eclipse.ceylon.model.loader.impl.reflect.ReflectionModelLoader;
import org.eclipse.ceylon.model.loader.mirror.ClassMirror;
import org.eclipse.ceylon.model.loader.mirror.MethodMirror;
import org.eclipse.ceylon.model.loader.model.AnnotationProxyClass;
import org.eclipse.ceylon.model.loader.model.AnnotationProxyMethod;
import org.eclipse.ceylon.model.loader.model.LazyFunction;
import org.eclipse.ceylon.model.loader.model.LazyModule;
import org.eclipse.ceylon.model.typechecker.model.Module;
import org.eclipse.ceylon.model.typechecker.model.Modules;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.UnknownType;
import org.eclipse.ceylon.model.typechecker.model.UnknownType.ErrorReporter;
import org.eclipse.ceylon.model.typechecker.util.ModuleManager;

/**
 * A model loader which uses Java reflection.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
// FIXME: we're still using a flat classpath here
public class PhasedUnitsModelLoader extends ReflectionModelLoader {

    ModulesClassLoader classLoader;
    Set<Module> modulesAddedToClassPath = new HashSet<Module>();
    private Callable<PhasedUnits> getPhasedUnits;

    public PhasedUnitsModelLoader(ModuleManager moduleManager, Modules modules, Callable<PhasedUnits> getPhasedUnits, boolean bootstrapCeylon){
        super(moduleManager, modules, new LoaderJULLogger());
        // FIXME: this probably needs to support alternate JDKs
        this.jdkProvider = new JdkProvider();
        this.classLoader = new ModulesClassLoader(PhasedUnitsModelLoader.class.getClassLoader(), jdkProvider);
        this.getPhasedUnits = getPhasedUnits;
        this.isBootstrap = bootstrapCeylon;
    }

    @Override
    protected boolean needsLocalDeclarations() {
        return false;
    }
    
    @Override
    protected boolean needsPrivateMembers() {
        return false;
    }
    
    @Override
    public boolean isModuleInClassPath(Module module) {
        return modulesAddedToClassPath.contains(module);
    }
    
    @Override
    protected Class<?> loadClass(Module module, String name) {
        Class<?> klass = null;
        try {
            klass = classLoader.loadClass(name);
        } catch (ClassNotFoundException|NoClassDefFoundError e) {
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
        return new ModuleErrorAttacherRunnable(getPasedUnits().getModuleSourceMapper(), module, message);
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
    
    private PhasedUnits getPasedUnits() {
        try {
            return getPhasedUnits.call();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
