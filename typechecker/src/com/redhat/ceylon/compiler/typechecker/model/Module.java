package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.List;

public class Module {

    public static final String DEFAULT_MODULE_NAME = "default";

    private List<String> name;
    private String version;
    private List<Package> packages = new ArrayList<Package>();
    private List<ModuleImport> imports = new ArrayList<ModuleImport>();
    private Module languageModule;
    private boolean available;
    private String license;
    private String doc;
    private List<String> authors = new ArrayList<String>();
    private boolean isDefault;

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

    /**
     * Return the matching package amongst the dependency tree.
     *
     * With Visibility.VISIBLE, only return truly visible matching package from the module:
     * - local packages
     * - shared packages of dependent modules
     * - shared packages of exported submodules
     *
     * With Visibility.ALL, return matching package from the module from all its
     * dependencies recursively relgardless of visibility rules
     */
    public Package getPackage(String name, Visibility visibility) {
        //all local packages are visible to the module
        for(Package pkg : getPackages()) {
            if ( pkg.getQualifiedNameString().equals(name) ) {
                return pkg;
            }
        }
        //all first level modules are imported and visible
        for (ModuleImport modImport : getImports()) {
            Package pkg = getMatchingPackage(name, visibility, modImport);
            if(pkg!=null) {
                return pkg;
            }
        }
        return null;
    }

    private Package getMatchingPackage(String name, Visibility visibility, ModuleImport modImport) {
        Module module = modImport.getModule();
        if (module.getNameAsString().startsWith(name)) {
            //we may have a winner
            for (Package modPkg : module.getPackages()) {
                if (modPkg.getQualifiedNameString().equals(name)) {
                    if (visibility==Visibility.ALL||modPkg.isShared()) {
                        return modPkg;
                    }
                }
            }
        }
        Package deepPackage = getPackageFromSecondLevelOrMoreModuleImportList(name, visibility, module.getImports());
        if (deepPackage!=null) {
            return deepPackage;
        }
        return null;
    }

    /**
     * Return matching package directly owned by the module
     * Inherited packages are not considered.
     */
    public Package getDirectPackage(String name) {
        for (Package pkg: packages) {
            if ( pkg.getQualifiedNameString().equals(name) ) {
                return pkg;
             }
        }
        return null;
    }

    private Package getPackageFromSecondLevelOrMoreModuleImportList(String name, Visibility visibility, List<ModuleImport> imports) {
        for(ModuleImport modImport : imports) {
            if (visibility==Visibility.ALL||modImport.isExport()) {
                Package matchingPackage = getMatchingPackage(name, visibility, modImport);
                if (matchingPackage!=null) {
                    return matchingPackage;
                }
            }
        }
        return null;
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
    
    public String getDoc() {
        return doc;
    }
    
    public void setDoc(String doc) {
        this.doc = doc;
    }
    
    public String getLicense() {
        return license;
    }
    
    public void setLicense(String license) {
        this.license = license;
    }
    
    public List<String> getAuthors() {
		return authors;
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

    public static enum Visibility {
        ALL,
        VISIBLE
    }
}
