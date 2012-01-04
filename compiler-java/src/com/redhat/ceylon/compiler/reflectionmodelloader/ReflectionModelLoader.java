package com.redhat.ceylon.compiler.reflectionmodelloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import com.redhat.ceylon.compiler.modelloader.AbstractModelLoader;
import com.redhat.ceylon.compiler.modelloader.TypeParser;
import com.redhat.ceylon.compiler.modelloader.refl.ReflClass;
import com.redhat.ceylon.compiler.modelloader.refl.ReflMethod;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Modules;
import com.redhat.ceylon.compiler.typechecker.model.Unit;

public class ReflectionModelLoader extends AbstractModelLoader {

    ReflectionModelLoader(ModuleManager moduleManager, Modules modules){
        this.moduleManager = moduleManager;
        this.modules = modules;
        this.typeFactory = new Unit();
        this.typeParser = new TypeParser(this, typeFactory);
    }
    
    @Override
    public void loadStandardModules() {
        super.loadStandardModules();
        // load two packages for the language module
        Module languageModule = modules.getLanguageModule();
        findOrCreatePackage(languageModule, "ceylon.language");
        findOrCreatePackage(languageModule, "ceylon.language.descriptor");
    }
    
    @Override
    public void loadPackage(String packageName, boolean loadDeclarations) {
        // nothing to do
    }

    @Override
    public ReflClass lookupClassSymbol(String name) {
        Class<?> klass = null;
        // try in every module
        // FIXME: surely we can do faster by checking the module name
        for(Module module : modules.getListOfModules()){
            try {
                ClassLoader classLoader = ((ReflectionModule)module).getClassLoader();
                if(classLoader != null)
                    klass = classLoader.loadClass(name);
            } catch (ClassNotFoundException e) {
                // next
            }
        }
        return klass != null ? new ReflectionClass(klass) : null;
    }

    @Override
    public void addModuleToClassPath(Module module, VirtualFile artifact) {
        if(artifact == null)
            return;
        try {
            // FIXME: this will be handled by the module system
            String path = artifact.getPath();
            @SuppressWarnings("deprecation")
            URL url = new File(path).toURL();
            URLClassLoader cl = new URLClassLoader(new URL[]{url});
            ((ReflectionModule)module).setClassLoader(cl);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to load module car from "+artifact.getPath());
        }
    }

    @Override
    protected ReflClass loadClass(String pkgName, String className) {
        return lookupClassSymbol(className);
    }

    @Override
    protected boolean isOverridingMethod(ReflMethod methodSymbol) {
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
