package com.redhat.ceylon.compiler.typechecker.context;

import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.common.BackendSupport;
import com.redhat.ceylon.common.Backends;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleSourceMapper;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Identifier;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Unit;

public class TypecheckerUnit extends Unit implements BackendSupport {
    private Set<Identifier> unresolvedReferences = new HashSet<Identifier>();
    private Package javaLangPackage;
	private ModuleSourceMapper moduleSourceMapper;

    public TypecheckerUnit() {
    }
    
    public TypecheckerUnit(Iterable<Module> modules, ModuleSourceMapper moduleSourceMapper){
    	this.moduleSourceMapper = moduleSourceMapper;
        for (Module m : modules) {
            if ("java.base".equals(m.getNameAsString())) {
                javaLangPackage = m.getPackage("java.lang");
                break;
            }
        }
    }

    public Set<Identifier> getUnresolvedReferences() {
        return unresolvedReferences;
    }

    private Set<Declaration> missingNativeImplementations = new HashSet<Declaration>();

    public Set<Declaration> getMissingNativeImplementations() {
        return missingNativeImplementations;
    }
    
    private Backends supportedBackends = Backends.ANY;

    @Override
    public Backends getSupportedBackends() {
        return supportedBackends;
    }
    
    public void setSupportedBackends(Backends backends) {
        supportedBackends = backends;
    }
    
    /** 
     * Override this because it's possible to see java.lang.Iterable 
     * (for example) without a dependency on java.base when importing 
     * a Java modules that uses it. 
     */
    @Override
    protected Package getJavaLangPackage() {
        return javaLangPackage != null ? javaLangPackage : super.getJavaLangPackage();
    }

	public ModuleSourceMapper getModuleSourceMapper() {
		return moduleSourceMapper;
	}
}
