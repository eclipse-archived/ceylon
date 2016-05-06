package com.redhat.ceylon.compiler.typechecker.context;

import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleSourceMapper;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Unit;

public class TypecheckerUnit extends Unit {

    private Package javaLangPackage;
	private ModuleSourceMapper moduleSourceMapper;

    public TypecheckerUnit(Iterable<Module> modules, 
            ModuleSourceMapper moduleSourceMapper){
    	this.moduleSourceMapper = moduleSourceMapper;
        for (Module m : modules) {
            if ("java.base".equals(m.getNameAsString())) {
                javaLangPackage = m.getPackage("java.lang");
                break;
            }
        }
    }
    
    /** 
     * Override this because it's possible to see java.lang.Iterable 
     * (for example) without a dependency on java.base when importing 
     * a Java modules that uses it. 
     */
    @Override
    protected Package getJavaLangPackage() {
        return javaLangPackage != null ? javaLangPackage : 
            super.getJavaLangPackage();
    }

	public ModuleSourceMapper getModuleSourceMapper() {
		return moduleSourceMapper;
	}
}
