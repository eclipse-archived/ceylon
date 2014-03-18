package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.isNameMatching;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isOverloadedVersion;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isResolvable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.redhat.ceylon.compiler.typechecker.context.ProducedTypeCache;

public class Module 
        implements Referenceable, Annotated, Comparable<Module> {

    public static final String LANGUAGE_MODULE_NAME = "ceylon.language";
    public static final String DEFAULT_MODULE_NAME = "default";

    private List<String> name;
    private String version;
    private int major;
    private int minor;
    private List<Package> packages = new ArrayList<Package>();
    private List<ModuleImport> imports = new ArrayList<ModuleImport>();
    private Module languageModule;
    private boolean available;
    private boolean isDefault;
    private List<Annotation> annotations = new ArrayList<Annotation>();
    private Unit unit;
    private String memoisedName;
    private ProducedTypeCache cache = new ProducedTypeCache();
    private String signature;

    /**
     * Whether or not the module is available in the
     * source path or the repository
     */
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<Package> getPackages() {
        return packages;
    }

    public List<ModuleImport> getImports() {
        return Collections.unmodifiableList(imports);
    }
    
    public void addImport(ModuleImport modImport) {
        imports.add(modImport);
    }
    
    public Module getLanguageModule() {
        return languageModule;
    }
    
    public void setLanguageModule(Module languageModule) {
        this.languageModule = languageModule;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    
    /**
     * Get all packages belonging to this module
     * and all shared packages belonging to 
     * modules transitively imported by this
     * module. 
     */
    public List<Package> getAllPackages() {
        List<Package> list = new ArrayList<Package>();
        list.addAll(getPackages());
        addSharedPackagesOfTransitiveDependencies(list, new HashSet<String>());
        return list;
    }
    
    private void addSharedPackagesOfTransitiveDependencies(List<Package> list, 
            Set<String> alreadyScannedModules) {
        for (ModuleImport mi: getImports()) {
            Module importedModule = mi.getModule();
            if (alreadyScannedModules.add(importedModule.getNameAsString())) {
                for (Package p: importedModule.getPackages()) {
                    if (p.isShared()) {
                        list.add(p);
                    }
                }
                importedModule.addSharedPackagesOfTransitiveDependencies(list, 
                        alreadyScannedModules);
            }
        }
    }
    
    public Map<String, DeclarationWithProximity> getAvailableDeclarations(String startingWith) {
    	Map<String, DeclarationWithProximity> result = new TreeMap<String, DeclarationWithProximity>();
    	for (Package p: getAllPackages()) {
    		String packageName = p.getNameAsString();
			boolean isLanguageModule = packageName.equals(LANGUAGE_MODULE_NAME);
			boolean isDefaultPackage = packageName.isEmpty();
			if (!isDefaultPackage) {
    			for (Declaration d: p.getMembers()) {
    				try {
    					if (isResolvable(d) && d.isShared() && 
    							!isOverloadedVersion(d) &&
    							isNameMatching(startingWith, d)) {
    						result.put(d.getQualifiedNameString(), 
    								new DeclarationWithProximity(d, 
    										isLanguageModule ? 200 : 250, 
    										!isLanguageModule));
    					}
    				}
    				catch (Exception e) {}
    			}
    		}
        }
        return result;
    }

    protected boolean isJdkModule(String moduleName) {
        // overridden by subclasses
        return false;
    }

    protected boolean isJdkPackage(String moduleName, String packageName) {
        // overridden by subclasses
        return false;
    }

    List<Package> getAllKnownPackages() {
        List<Package> list = new ArrayList<Package>();
        list.addAll(packages);
        for (ModuleImport mi: imports) {
            list.addAll(mi.getModule().getPackages());
        }
        return list;
    }

    public Package getDirectPackage(String name) {
        for (Package pkg: packages) {
            if ( pkg.getQualifiedNameString().equals(name) ) {
                return pkg;
            }
        }
        return null;
    }
    
    public Package getPackage(String name) {
        Package pkg = getDirectPackage(name);
        if(pkg != null)
            return pkg;
        for (ModuleImport mi: imports) {
            pkg = mi.getModule().getDirectPackage(name);
            if(pkg != null)
                return pkg;
        }
        return null;
    }
    
    public Package getRootPackage() {
    	return getPackage(getNameAsString());
    }
    
    public String getNameAsString() {
        if(memoisedName == null){
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < name.size(); i++) {
                sb.append(name.get(i));
                if (i < name.size() - 1) {
                    sb.append('.');
                }
            }
            memoisedName = sb.toString();
        }
        return memoisedName;
    }

    @Override
    public String toString() {
        return "Module[" + getNameAsString() + ", " + getVersion() + "]";
    }
    
    /**
     * Is this the default module hosting all units outside of an explicit module
     */
    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public boolean isJava() {
        return false;
    }
    
    @Override
    public Unit getUnit() {
    	return unit;
    }
    
    public void setUnit(Unit unit) {
		this.unit = unit;
	}

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    @Override
    public int compareTo(Module other) {
        if(this == other)
            return 0;
        // default first
        if(isDefault())
            return -1;
        int cmp = this.getNameAsString().compareTo(other.getNameAsString());
        if(cmp != 0)
            return cmp;
        // we don't care about how versions are compared, we just care that the order is consistent
        return this.getVersion().compareTo(other.getVersion());
    }

    public ProducedTypeCache getCache(){
        return cache;
    }

    public void clearCache(TypeDeclaration declaration) {
        ProducedTypeCache cache = getCache();
        if(cache != null){
            cache.clearForDeclaration(declaration);
        }
        // FIXME: propagate to modules that import this module transitively
        // Done in the IDE JDTModule
    }
    
    public String getSignature() {
        if(signature == null){
            if(isDefault())
                signature = getNameAsString();
            else
                signature = getNameAsString() + "/" + getVersion();
        }
        return signature;
    }

    @Override
    public int hashCode() {
        return getSignature().hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null || obj instanceof Module == false)
            return false;
        Module b = (Module) obj;
        return getSignature().equals(b.getSignature());
    }
}
