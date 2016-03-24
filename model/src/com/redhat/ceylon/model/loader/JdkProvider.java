package com.redhat.ceylon.model.loader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipException;

import com.redhat.ceylon.model.cmr.JDKUtils;
import com.redhat.ceylon.model.typechecker.model.Module;

public class JdkProvider {
    private boolean alternateJdk;
    private Map<String, Set<String>> jdkModules = new HashMap<String, Set<String>>();
    private Module module;
    private String name;

    /**
     * Creates a JdkProvider loaded from the given module/jar
     */
    public JdkProvider(String name, String version, Module module, File jar) throws ZipException, IOException{
        this.module = module;
        this.name = name;
        loadJar(jar);
        alternateJdk = true;
    }

    /**
     * Creates a JdkProvider which uses the current running JVM's JDK as detected.
     */
    public JdkProvider() {
    }

    private void loadJar(File jar) throws ZipException, IOException {
        Set<String> packages = JvmBackendUtil.listPackages(jar, null);
        for(String pkg : packages){
            String jdkModuleName = JDKUtils.getJDKModuleNameForPackage(pkg);
            if(jdkModuleName != null){
                Set<String> modulePackages = jdkModules.get(jdkModuleName);
                if(modulePackages == null){
                    modulePackages = new HashSet<String>();
                    jdkModules.put(jdkModuleName, modulePackages);
                }
                modulePackages.add(pkg);
            }
        }
    }

    public boolean isJDKModule(String module) {
        if(alternateJdk)
            return jdkModules.containsKey(module);
        return JDKUtils.isJDKModule(module) || JDKUtils.isOracleJDKModule(module);
    }

    public boolean isJDKPackage(String pkg) {
        if(alternateJdk){
            for(Set<String> packages : jdkModules.values()){
                if(packages.contains(pkg)){
                    return true;
                }
            }
        }
        return JDKUtils.isJDKAnyPackage(pkg) || JDKUtils.isOracleJDKAnyPackage(pkg);
    }

    public boolean isJDKPackage(String module, String pkg) {
        if(alternateJdk){
            Set<String> packages = jdkModules.get(module);
            return packages != null && packages.contains(pkg);
        }
        return JDKUtils.isJDKPackage(module, pkg) || JDKUtils.isOracleJDKPackage(module, pkg);
    }

    public Set<String> getJDKModuleNames() {
        if(alternateJdk){
            return jdkModules.keySet();
        }
        Set<String> modules = new HashSet<String>();
        modules.addAll(JDKUtils.getJDKModuleNames());
        modules.addAll(JDKUtils.getOracleJDKModuleNames());
        return modules;
    }

    public String getJDKVersion() {
        if(alternateJdk){
            // FIXME???
            return "7";
        }
        return JDKUtils.jdk.version;
    }

    public String getJDKModuleNameForPackage(String pkg) {
        if(alternateJdk){
            for(Entry<String, Set<String>> entry : jdkModules.entrySet()){
                if(entry.getValue().contains(pkg)){
                    return entry.getKey();
                }
            }
        }
        return JDKUtils.getJDKModuleNameForPackage(pkg);
    }

    public boolean isImplementationSpecificJDKPackage(String pkg) {
        if(alternateJdk){
            // FIXME: perhaps we consider android.* stuff?
            return false;
        }
        return JDKUtils.isOracleJDKAnyPackage(pkg);
    }

    public List<String> getJDKPackageList() {
        List<String> ret = new ArrayList<>(50);
        for(String module : getJDKModuleNames()){
            for(String pkg : getJDKPackages(module)){
                ret.add(pkg);
            }
        }
        return ret;
    }

    public Set<String> getJDKPackages(String module) {
        if(alternateJdk){
            return jdkModules.get(module);
        }
        if(JDKUtils.isJDKModule(module))
            return JDKUtils.getJDKPackagesByModule(module);
        if(JDKUtils.isOracleJDKModule(module))
            return JDKUtils.getOracleJDKPackagesByModule(module);
        return Collections.emptySet();
    }
    
    public Module getJdkContainerModule(){
        return module;
    }

    public String getJdkContainerModuleName() {
        return name;
    }

    public boolean isAlternateJdk() {
        return alternateJdk ;
    }
}
