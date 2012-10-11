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

package com.redhat.ceylon.compiler.loader.impl.reflect;

import java.io.File;

import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.compiler.java.util.Timer;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.AbstractModelLoader;
import com.redhat.ceylon.compiler.loader.TypeParser;
import com.redhat.ceylon.compiler.loader.impl.reflect.mirror.ReflectionClass;
import com.redhat.ceylon.compiler.loader.impl.reflect.mirror.ReflectionMethod;
import com.redhat.ceylon.compiler.loader.mirror.ClassMirror;
import com.redhat.ceylon.compiler.loader.mirror.MethodMirror;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Modules;
import com.redhat.ceylon.compiler.typechecker.model.Unit;

/**
 * A model loader which uses Java reflection.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class ReflectionModelLoader extends AbstractModelLoader {

    ModulesClassLoader classLoader = new ModulesClassLoader();

    public ReflectionModelLoader(ModuleManager moduleManager, Modules modules){
        this.moduleManager = moduleManager;
        this.modules = modules;
        this.typeFactory = new Unit();
        this.typeParser = new TypeParser(this, typeFactory);
        this.timer = new Timer(false);
    }
    
    @Override
    public void loadStandardModules() {
        super.loadStandardModules();
        // load two packages for the language module
        Module languageModule = modules.getLanguageModule();
        findOrCreatePackage(languageModule, AbstractModelLoader.CEYLON_LANGUAGE);
        findOrCreatePackage(languageModule, "ceylon.language.descriptor");
    }
    
    @Override
    public boolean loadPackage(String packageName, boolean loadDeclarations) {
        String quotedPackage = Util.quoteJavaKeywords(packageName);
        return classLoader.packageExists(quotedPackage);
    }

    @Override
    public ClassMirror lookupNewClassMirror(String name) {
        Class<?> klass = null;
        if (lastPartHasLowerInitial(name)) {
            klass = loadClass(name+"_");
        } else {
            klass = loadClass(name);
        }
        return klass != null ? new ReflectionClass(klass) : null;
    }

    private Class<?> loadClass(String name) {
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
        if(artifact == null)
            return;
        File file = artifact.artifact();
        classLoader.addJar(file);
    }

    @Override
    protected String assembleJavaClass(String javaClass, String packageName) {
        // strip the java class name of its package part
        if(!packageName.isEmpty())
            javaClass = javaClass.substring(packageName.length()+1); // pkg + dot
        // now replace every dot in the name part with $
        javaClass = javaClass.replace('.', '$');
        // assemble back
        if(packageName.isEmpty())
            return javaClass;
        return packageName + "." + javaClass;
    }
    
    @Override
    protected boolean isOverridingMethod(MethodMirror methodSymbol) {
        return ((ReflectionMethod)methodSymbol).isOverridingMethod();
    }

    @Override
    protected void logError(String message) {
        System.err.println("ERROR: "+message);
    }

    @Override
    protected void logWarning(String message) {
        System.err.println("WARNING: "+message);
    }

    @Override
    protected void logVerbose(String message) {
        System.err.println("NOTE: "+message);
    }

}
