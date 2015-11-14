package com.redhat.ceylon.model.typechecker.model;

import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isNameMatching;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isOverloadedVersion;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isResolvable;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.redhat.ceylon.common.Backends;
import com.redhat.ceylon.model.typechecker.context.TypeCache;

public class Module 
        implements Referenceable, Annotated, Comparable<Module> {

    public static final String LANGUAGE_MODULE_NAME = "ceylon.language";
    public static final String DEFAULT_MODULE_NAME = "default";

    private List<String> name;
    private String version;
    private int major;
    private int minor;
    private List<Package> packages = 
            new ArrayList<Package>();
    private List<ModuleImport> imports = 
            new ArrayList<ModuleImport>();
    private Module languageModule;
    private boolean available;
    private boolean isDefault;
    private List<Annotation> annotations = 
            new ArrayList<Annotation>();
    private Unit unit;
    private String memoisedName;
    private TypeCache cache = new TypeCache();
    private String signature;
    private List<ModuleImport> overridenImports = null;
    private Backends nativeBackends = Backends.ANY;

    public Module() {}
    
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
     * Get all packages belonging to this module and modules 
     * transitively imported by this module that are visible
     * to this module. 
     */
    public List<Package> getAllVisiblePackages() {
        List<Package> list = 
                new ArrayList<Package>(getPackages());
        addVisiblePackagesOfTransitiveDependencies(list, 
                new HashSet<String>(), true);
        return list;
    }
    
    private void addVisiblePackagesOfTransitiveDependencies(
            List<Package> list, 
            Set<String> alreadyScannedModules, 
            boolean firstLevel) {
        for (ModuleImport mi: getImports()) {
            if (firstLevel || mi.isExport()) {
                Module importedModule = mi.getModule();
                String imn = importedModule.getNameAsString();
                if (alreadyScannedModules.add(imn)) {
                    for (Package p: 
                            importedModule.getPackages()) {
                        if (p.isShared()) {
                            list.add(p);
                        }
                    }
                    importedModule.addVisiblePackagesOfTransitiveDependencies(
                            list, alreadyScannedModules, false);
                }
            }
        }
    }
    
    /**
     * Get all packages belonging to this module and modules 
     * transitively imported by this module, including 
     * packages that aren't visible to this module. 
     */
    public List<Package> getAllReachablePackages() {
        List<Package> list = new ArrayList<Package>();
        list.addAll(getPackages());
        addAllPackagesOfTransitiveDependencies(list, 
                new HashSet<String>());
        return list;
    }

    private void addAllPackagesOfTransitiveDependencies(
            List<Package> list, 
            Set<String> alreadyScannedModules) {
        for (ModuleImport mi: getImports()) {
            Module importedModule = mi.getModule();
            String imn = importedModule.getNameAsString();
            if (alreadyScannedModules.add(imn)) {
                for (Package p: importedModule.getPackages()) {
                    list.add(p);
                }
                importedModule.addVisiblePackagesOfTransitiveDependencies(
                        list, alreadyScannedModules, false);
            }
        }
    }
    
    public Map<String, DeclarationWithProximity> 
    getAvailableDeclarations(String startingWith, 
            int proximity) {
        Map<String, DeclarationWithProximity> result = 
                new TreeMap<String,DeclarationWithProximity>();
        for (Package p: getAllVisiblePackages()) {
            String packageName = 
                    p.getNameAsString();
            boolean isLanguageModule = 
                    packageName.equals(LANGUAGE_MODULE_NAME);
            boolean isDefaultPackage = 
                    packageName.isEmpty();
            if (!isDefaultPackage) {
                for (Declaration d: p.getMembers()) {
                    try {
                        if (isResolvable(d) && 
                                d.isShared() && 
                                !isOverloadedVersion(d) &&
                                isNameMatching(startingWith, d)) {
                            String name = d.getName();
                            boolean isSpecialValue = 
                                    isLanguageModule &&
                                        name.equals("true") || 
                                        name.equals("false") || 
                                        name.equals("null");
                            boolean isSpecialType = 
                                    isLanguageModule &&
                                        name.equals("String") ||
                                        name.equals("Integer") ||
                                        name.equals("Float") ||
                                        name.equals("Character") ||
                                        name.equals("Boolean") ||
                                        name.equals("Byte") ||
                                        name.equals("Object") ||
                                        name.equals("Anything");
                            int prox;
                            if (isSpecialValue) {
                                prox = -1;
                            }
                            else if (isSpecialType) {
                                //just less than toplevel
                                //declarations of the package
                                prox = proximity+2;
                            }
                            else if (isLanguageModule) {
                                //just less than toplevel
                                //declarations of the package
                                prox = proximity+3;
                            }
                            else {
                                //unimported declarations
                                //that may be imported
                                prox = proximity+4;
                            }
                            result.put(d.getQualifiedNameString(), 
                                    new DeclarationWithProximity(d, 
                                            prox, !isLanguageModule));
                        }
                    }
                    catch (Exception e) {}
                }
            }
        }
        if ("Nothing".startsWith(startingWith)) {
            result.put("Nothing", 
                    new DeclarationWithProximity(
                            new NothingType(unit),
                            //same as other "special" 
                            //language module declarations
                            proximity+2));
        }
        return result;
    }

    protected boolean isJdkModule(String moduleName) {
        // overridden by subclasses
        return false;
    }

    protected boolean isJdkPackage(String moduleName, 
            String packageName) {
        // overridden by subclasses
        return false;
    }

    public Package getDirectPackage(String name) {
        for (Package pkg: packages) {
            if (pkg.getQualifiedNameString().equals(name)) {
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
        if (memoisedName == null){
            StringBuilder sb = new StringBuilder();
            for (int i=0, s=name.size(); i<s; i++) {
                sb.append(name.get(i));
                if (i < name.size()-1) {
                    sb.append('.');
                }
            }
            memoisedName = sb.toString();
        }
        return memoisedName;
    }

    @Override
    public String toString() {
        return "module " + getNameAsString() + 
                " \"" + getVersion() + "\"";
    }
    
    /**
     * Is this the default module hosting all units outside 
     * of an explicit module
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

    public boolean isNative() {
        return !getNativeBackends().none();
    }
    
    public Backends getNativeBackends() {
        return nativeBackends;
    }
    
    public void setNativeBackends(Backends backends) {
        this.nativeBackends=backends;
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
        if (this == other) {
            return 0;
        }
        // default first
        if (isDefault()) {
            return -1;
        }
        String name = this.getNameAsString();
        String otherName = other.getNameAsString();
        int cmp = name.compareTo(otherName);
        if (cmp != 0) {
            return cmp;
        }
        // we don't care about how versions are compared, we 
        // just care that the order is consistent
        String version = this.getVersion();
        String otherVersion = other.getVersion();
        return version.compareTo(otherVersion);
    }

    public TypeCache getCache(){
        return cache;
    }

    public void clearCache(TypeDeclaration declaration) {
        TypeCache cache = getCache();
        if (cache != null){
            cache.clearForDeclaration(declaration);
        }
        // FIXME: propagate to modules that import this module transitively
        // Done in the IDE JDTModule
    }
    
    public String getSignature() {
        if (signature == null) {
            if (isDefault()) {
                signature = getNameAsString();
            }
            else {
                signature = getNameAsString() + 
                        "/" + getVersion();
            }
        }
        return signature;
    }

    public List<ModuleImport> getOverridenImports() {
        return overridenImports == null ? null :
            unmodifiableList(overridenImports);
    }

    public boolean overrideImports(
            List<ModuleImport> newModuleImports) {
        if (overridenImports == null 
                && newModuleImports != null) {
            overridenImports  = imports;
            imports = newModuleImports;
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getSignature().hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Module) {
            Module b = (Module) obj;
            return getSignature().equals(b.getSignature());
        }
        else {
            return false;
        }
    }
}
