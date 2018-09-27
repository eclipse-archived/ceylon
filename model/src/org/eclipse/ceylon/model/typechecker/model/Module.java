/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.typechecker.model;

import static java.util.Collections.unmodifiableList;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.formatPath;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isNameMatching;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isOverloadedVersion;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isResolvable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.ceylon.common.Backends;
import org.eclipse.ceylon.model.typechecker.context.TypeCache;

public class Module 
        implements Referenceable, Annotated, 
                   Comparable<Module> {

    public static final String LANGUAGE_MODULE_NAME = "ceylon.language";
    public static final String DEFAULT_MODULE_NAME = "default";

    private List<String> name;
    private String version;
    private int jvmMajor;
    private int jvmMinor;
    private int jsMajor;
    private int jsMinor;
    private List<Package> packages = 
            new ArrayList<Package>();
    private List<ModuleImport> imports = 
            new ArrayList<ModuleImport>();
    private Module languageModule;
    private boolean available;
    private List<Annotation> annotations = 
            new ArrayList<Annotation>();
    private Unit unit;
    private String nameAsString;
    private TypeCache cache = new TypeCache();
    private String signature;
    private List<ModuleImport> overridenImports = null;
    private Backends nativeBackends = Backends.ANY;
    private Map<ClassOrInterface, Set<Class>> services = null;
    private String groupId;
    private String artifactId;
    private String classifier;

    private LanguageModuleCache languageModuleCache = null;

    public Module() {}
    
    public LanguageModuleCache getLanguageModuleCache() {
        if (languageModuleCache == null) {
            assert(isLanguageModule());
            languageModuleCache = new LanguageModuleCache(this);
        }
        return languageModuleCache;
    }
    
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
        this.nameAsString = null;
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
    
    public boolean isLanguageModule() {
        List<String> name = getName();
        return name.size()==2
            && name.get(0)
                .equals("ceylon")
            && name.get(1)
                .equals("language");
    }
    
    /**
     * Is this the default module hosting all units outside 
     * of an explicit module
     */
    public boolean isDefaultModule() {
        List<String> name = getName();
        return name.size()==1
            && name.get(0)
                .equals(DEFAULT_MODULE_NAME);
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
                importedModule.addAllPackagesOfTransitiveDependencies(
                        list, alreadyScannedModules);
            }
        }
    }
    
    /**
     * Avoid scanning the whole universe of transitive dependencies
     * when we don't even have an initial prefix.
     */
    private List<Package> getPackagesToScan(String startingWith) {
        if (startingWith.isEmpty()) {
            List<Package> packs = getPackages();
            List<Package> langPacks = getLanguageModule().getPackages();
            List<Package> list =
                    new ArrayList<Package>(
                        packs.size() + langPacks.size());
            list.addAll(packs);
            list.addAll(langPacks);
            return list;
        }
        else {
            return getAllVisiblePackages();
        }
    }

    public Map<String, DeclarationWithProximity>
    getAvailableDeclarationsInCurrentModule(Unit unit, 
            String startingWith, int proximity, 
            Cancellable canceller) {
        return getAvailableDeclarationsInternal(unit, 
                startingWith, proximity, canceller, 
                getPackages());
    }

    private Map<String, DeclarationWithProximity>
    getAvailableDeclarationsInternal(Unit unit, 
            String startingWith, int proximity, 
            Cancellable canceller, List<Package> packages) {
        Map<String, DeclarationWithProximity> result =
                new TreeMap<String,DeclarationWithProximity>();
        for (Package p: packages) {
            if (canceller != null
                    && canceller.isCancelled()) {
                return Collections.emptyMap();
            }
            boolean isLanguagePackage =
                    p.isLanguagePackage();
            boolean isDefaultPackage =
                    p.isDefaultPackage();
            if (!isDefaultPackage) {
                for (Declaration d: p.getMembers()) {
                    if (canceller != null
                            && canceller.isCancelled()) {
                        return Collections.emptyMap();
                    }
                    try {
                        if (isResolvable(d) &&
                                d.isShared() &&
                                !isOverloadedVersion(d) &&
                                isNameMatching(startingWith, d)) {
                            result.put(
                                    //use qualified name here, in order
                                    //to distinguish unimported declarations
                                    //with same name in different packages
                                    d.getQualifiedNameString(),
                                    new DeclarationWithProximity(d,
                                            getUnimportedProximity(proximity, 
                                                isLanguagePackage, 
                                                d.getName()),
                                            !isLanguagePackage));
                        }
                    }
                    catch (Exception e) {}
                }
            }
        }
        if ("Nothing".startsWith(startingWith)) {
            result.put("Nothing",
                    new DeclarationWithProximity(
                            unit.getNothingDeclaration(),
                            //same as other "special"
                            //language module declarations
                            proximity+2));
        }
        return result;
    }

    public Map<String, DeclarationWithProximity>
    getAvailableDeclarations(Unit unit, 
            String startingWith, int proximity, 
            Cancellable canceller) {
        return getAvailableDeclarationsInternal(unit, 
                startingWith, proximity, canceller, 
                getPackagesToScan(startingWith));
    }

    public static int getUnimportedProximity(int initialProximity, 
            boolean isLanguagePackage, String name) {
        boolean isSpecialValue =
                isLanguagePackage &&
                    name.equals("true") ||
                    name.equals("false") ||
                    name.equals("null");
        boolean isSpecialType =
                isLanguagePackage &&
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
            prox = initialProximity+2;
        }
        else if (isLanguagePackage) {
            //just less than toplevel
            //declarations of the package
            prox = initialProximity+3;
        }
        else {
            //unimported declarations
            //that may be imported
            prox = initialProximity+4;
        }
        return prox;
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
        if (nameAsString == null) {
            nameAsString = formatPath(name);
        }
        return nameAsString;
    }

    @Override
    public String toString() {
        return "module " + getNameAsString() + 
                " \"" + getVersion() + "\"";
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

    @Override
    public int compareTo(Module other) {
        if (this == other) {
            return 0;
        }
        // default first
        if (isDefaultModule()) {
            return -1;
        }
        String name = this.getNameAsString();
        String otherName = other.getNameAsString();
        int cmp = name.compareTo(otherName);
        if (cmp != 0) {
            return cmp;
        }
        // we don't care about how versions are compared,  
        // we just care that the order is consistent
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
        // FIXME: propagate to modules that import this 
        // module transitively 
        // Done in the IDE JDTModule
    }
    
    public String getSignature() {
        if (signature == null) {
            if (isDefaultModule()) {
                signature = getNameAsString();
            }
            else if (getVersion() != null) {
                signature = getNameAsString() + 
                        "/" + getVersion();
            }
            else {
                return getNameAsString() +
                        "/null";
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
            return getSignature()
                    .equals(b.getSignature());
        }
        else {
            return false;
        }
    }

    public int getJvmMajor() {
        return jvmMajor;
    }

    public void setJvmMajor(int jvmMajor) {
        this.jvmMajor = jvmMajor;
    }

    public int getJvmMinor() {
        return jvmMinor;
    }

    public void setJvmMinor(int jvmMinor) {
        this.jvmMinor = jvmMinor;
    }

    public int getJsMajor() {
        return jsMajor;
    }

    public void setJsMajor(int jsMajor) {
        this.jsMajor = jsMajor;
    }

    public int getJsMinor() {
        return jsMinor;
    }

    public void setJsMinor(int jsMinor) {
        this.jsMinor = jsMinor;
    }
    
    public void addService(ClassOrInterface serviceIface, Class serviceImpl) {
        if (services == null) {
            services = new HashMap<ClassOrInterface, Set<Class>>();
        }
        Set<Class> impls = services.get(serviceIface);
        if (impls == null) {
            impls = new HashSet<Class>(1);
            services.put(serviceIface, impls);
        }
        impls.add(serviceImpl);
    }
    
    public Map<ClassOrInterface, Set<Class>> getServices() {
        return services != null ? services : Collections.<ClassOrInterface, Set<Class>>emptyMap();
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    public String getArtifactId() {
        return artifactId;
    }
    
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }
    
    public String getClassifier() {
        return classifier;
    }
    
    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }
}
