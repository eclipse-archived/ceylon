package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.isNameMatching;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isOverloadedVersion;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isResolvable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Module 
        implements Referenceable, Annotated {

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

    public List<Package> getSharedPackages() {
        List<Package> list = new ArrayList<Package>();
        for (Package p: packages) {
        	if (p.isShared()) {
        		list.add(p);
        	}
        }
        return list;
    }

    public List<ModuleImport> getImports() {
        return imports;
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

    public List<Package> getAllPackages() {
        List<Package> list = new ArrayList<Package>();
        list.addAll(packages);
        for (ModuleImport mi: imports) {
            list.addAll(mi.getModule().getSharedPackages());
        }
        return list;
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
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < name.size(); i++) {
            sb.append(name.get(i));
            if (i < name.size() - 1) {
                sb.append('.');
            }
        }
        return sb.toString();
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
    
}
