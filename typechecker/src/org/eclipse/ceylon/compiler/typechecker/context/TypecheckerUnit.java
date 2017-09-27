package org.eclipse.ceylon.compiler.typechecker.context;

import org.eclipse.ceylon.compiler.typechecker.analyzer.ModuleSourceMapper;
import org.eclipse.ceylon.model.loader.JdkProvider;
import org.eclipse.ceylon.model.typechecker.model.Package;
import org.eclipse.ceylon.model.typechecker.model.Unit;

public class TypecheckerUnit extends Unit {

    private Package javaLangPackage;
    private JdkProvider jdkProvider;

    public TypecheckerUnit(ModuleSourceMapper moduleSourceMapper) {
        this.jdkProvider = moduleSourceMapper!=null ?
                moduleSourceMapper.getJdkProvider() : null;
    }
    
    public TypecheckerUnit(
            String theFilename,
            String theRelativePath,
            String theFullPath,
            Package thePackage) {
        setFilename(theFilename);
        setRelativePath(theRelativePath);
        setFullPath(theFullPath);
        setPackage(thePackage);
    }

    public void setJavaLangPackage(Package javaLangPackage) {
        this.javaLangPackage = javaLangPackage;
    }
    
    /** 
     * Override this because it's possible to see java.lang.Iterable 
     * (for example) without a dependency on java.base when importing 
     * a Java modules that uses it. 
     */
    @Override
    public Package getJavaLangPackage() {
        return javaLangPackage != null ? javaLangPackage : 
            super.getJavaLangPackage();
    }
    
    @Override
    public boolean isJdkPackage(String name) {
        return jdkProvider!=null ? 
                jdkProvider.isJDKPackage(name) : 
                super.isJdkPackage(name);
    }
}
