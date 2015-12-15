package com.redhat.ceylon.model.cmr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.redhat.ceylon.model.loader.Java9ModuleReader;
import com.redhat.ceylon.model.loader.Java9ModuleReader.Java9Module;
import com.redhat.ceylon.model.loader.Java9ModuleReader.Java9ModuleDependency;

/**
 * Hide JDK impl details here.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class JDKUtils {
    
    public enum JDK {
        JDK7("package-list.jdk7", "package-list.oracle.jdk7", "7"),
        JDK8("package-list.jdk8", "package-list.oracle.jdk8", "8"),
        JDK9("package-list.jdk9", "package-list.oracle.jdk9", "9");
        
        public final String packageList;
        public final String packageListOracle;
        public final String version;
        
        private JDK(String packageList, String packageListOracle, String version) {
            this.packageList = packageList;
            this.packageListOracle = packageListOracle;
            this.version = version;
        }
        
        public boolean providesVersion(String version){
            if(this.version.equals(version))
                return true;
            // also provides every smaller version
            EnumSet<JDK> smaller = EnumSet.range(JDK7, this);
            // we want strictly smaller
            smaller.remove(this);
            for(JDK smallerJDK : smaller){
                if(smallerJDK.version.equals(version))
                    return true;
            }
            return false;
        }

        public boolean isLowerVersion(String version) {
            EnumSet<JDK> smaller = EnumSet.range(JDK7, this);
            // we want strictly smaller
            smaller.remove(this);
            for(JDK smallerJDK : smaller){
                if(smallerJDK.version.equals(version))
                    return true;
            }
            return false;
        }
    }
    
    public final static JDK jdk;
    
    static {
        String version = System.getProperty("java.version");
        if(version != null){
            if(version.startsWith("1.9")){
                jdk = JDK.JDK9;
            }else if(version.startsWith("1.8")){
                jdk = JDK.JDK8;
            }else{
                jdk = JDK.JDK7;
            }
        }else{
            jdk = JDK.JDK7;
        }
    }

    private static Map<String, Tuple> jdkModules;
    private static Map<String, Tuple> jdkOracleModules;
    private static HashMap<String, String> jdkPackageToModule;
    private static Map<String,String> java8To9ModuleAliases = new HashMap<>();
    
    static{
        java8To9ModuleAliases.put("javax.annotation", "java.annotation.common");
        java8To9ModuleAliases.put("java.auth", "java.security.sasl");
        java8To9ModuleAliases.put("java.auth.kerberos", "java.security.jgss");
        java8To9ModuleAliases.put("java.jdbc", "java.sql");
        java8To9ModuleAliases.put("java.jdbc.rowset", "java.sql.rowset");
        java8To9ModuleAliases.put("javax.script", "java.scripting");
        java8To9ModuleAliases.put("javax.xml", "java.xml");
        java8To9ModuleAliases.put("javax.xmldsig", "java.xml.crypto");
    }

    private static class Tuple {
        private Set<String> packages;
        private Set<String> paths;

        private Tuple() {
            packages = new HashSet<String>();
            paths = new HashSet<String>();
        }

        boolean isEmpty() {
            return packages.isEmpty();
        }

        Tuple finish() {
            packages = Collections.unmodifiableSet(packages);
            paths = Collections.unmodifiableSet(paths);
            return this;
        }
    }

    private static synchronized void loadPackageList() {
        if (jdkModules != null)
            return;
        jdkModules = loadModularPackageList(jdk.packageList);
        jdkOracleModules = loadModularPackageList(jdk.packageListOracle);
        jdkPackageToModule = new HashMap<String,String>();
        for(Entry<String, Tuple> entry : jdkModules.entrySet()){
            for(String pkg : entry.getValue().packages)
                jdkPackageToModule.put(pkg, entry.getKey());
        }
        for(Entry<String, Tuple> entry : jdkOracleModules.entrySet()){
            for(String pkg : entry.getValue().packages)
                jdkPackageToModule.put(pkg, entry.getKey());
        }
    }

    private static Map<String, Tuple> loadModularPackageList(String file) {
        try {
            // not thread-safe, but that's OK because the caller is thread-safe
            Map<String, Tuple> jdkPackages = new HashMap<String, Tuple>();
            InputStream inputStream = JDKUtils.class.getResourceAsStream(file);
            if (inputStream == null) {
                throw new RuntimeException("Failed to read JDK package list file from " + file + ": your Ceylon installation is broken.");
            }
            BufferedReader bis = new BufferedReader(new InputStreamReader(inputStream, "ASCII"));
            Tuple tuple = null;
            String moduleName = null;
            String pkg;
            while ((pkg = bis.readLine()) != null) {
                // strip comments
                int commentStart = pkg.indexOf('#');
                if (commentStart > -1)
                    pkg = pkg.substring(0, commentStart);
                // strip whitespace
                pkg = pkg.trim();
                // ignore empty lines
                if (pkg.isEmpty())
                    continue;
                // see if we start a new module
                if (pkg.startsWith("=")) {
                    String name = pkg.substring(1).trim();
                    if (name.isEmpty())
                        throw new RuntimeException("Failed to read JDK module list file from " + file + ": module has empty name");
                    // close previous module
                    if (tuple != null) {
                        // save previous module
                        jdkPackages.put(moduleName, tuple.finish());
                    }
                    // start the new module
                    moduleName = name;
                    tuple = new Tuple();
                    continue;
                }
                // add a package to the current module
                if (tuple == null)
                    throw new RuntimeException("Failed to read JDK module list file from " + file + ": adding package to undefined module");

                tuple.packages.add(pkg);
                tuple.paths.add(pkg.replace('.', '/'));
            }
            bis.close();
            // close previous module
            if (tuple != null) {
                // save previous module
                jdkPackages.put(moduleName, tuple.finish());
            }
            // sanity check
            if (jdkPackages.size() == 0)
                throw new RuntimeException("Failed to read JDK package list file from " + file + "(empty package set): your Ceylon installation is broken.");
            return Collections.unmodifiableMap(jdkPackages);
        } catch (IOException x) {
            throw new RuntimeException("Failed to read JDK package list file from " + file + ": your Ceylon installation is broken.", x);
        }
    }

    public static boolean isJDKModule(String mod) {
        loadPackageList();
        return jdkModules.containsKey(mod);
    }

    public static boolean isJDKPackage(String mod, String pkg) {
        loadPackageList();
        Tuple tuple = jdkModules.get(mod);
        return tuple != null && tuple.packages.contains(pkg);
    }

    public static boolean isJDKAnyPackage(String pkg) {
        loadPackageList();
        for (Tuple tuple : jdkModules.values()) {
            if (tuple.packages.contains(pkg))
                return true;
        }
        return false;
    }

    public static Set<String> getJDKModuleNames() {
        loadPackageList();
        return jdkModules.keySet();
    }

    public static Set<String> getJDKPackagesByModule(String module) {
        loadPackageList();
        Tuple tuple = jdkModules.get(module);
        return tuple != null ? tuple.packages : null;
    }

    public static Set<String> getJDKPathsByModule(String module) {
        loadPackageList();
        Tuple tuple = jdkModules.get(module);
        return tuple != null ? tuple.paths : null;
    }

    public static boolean isOracleJDKModule(String pkg) {
        loadPackageList();
        return jdkOracleModules.containsKey(pkg);
    }

    public static boolean isOracleJDKPackage(String mod, String pkg) {
        loadPackageList();
        Tuple tuple = jdkOracleModules.get(mod);
        return tuple != null && tuple.packages.contains(pkg);
    }

    public static boolean isOracleJDKAnyPackage(String pkg) {
        loadPackageList();
        for (Tuple tuple : jdkOracleModules.values()) {
            if (tuple.packages.contains(pkg))
                return true;
        }
        return false;
    }

    public static Set<String> getOracleJDKModuleNames() {
        loadPackageList();
        return jdkOracleModules.keySet();
    }

    public static Set<String> getOracleJDKPackagesByModule(String module) {
        loadPackageList();
        Tuple tuple = jdkOracleModules.get(module);
        return tuple != null ? tuple.packages : null;
    }

    public static Set<String> getOracleJDKPathsByModule(String module) {
        loadPackageList();
        Tuple tuple = jdkOracleModules.get(module);
        return tuple != null ? tuple.paths : null;
    }

    public static String getJDKModuleNameForPackage(String pkgName) {
        loadPackageList();
        return jdkPackageToModule.get(pkgName);
    }
    
    public static void main(String[] args) throws IOException {
        
//        FileSystem fileSystem = FileSystems.getFileSystem(URI.create("jrt:/"));
//        Path modules = fileSystem.getPath("/modules");
//        for(Path module : Files.newDirectoryStream(modules)){
//            System.err.println("="+module.getFileName());
//            Path moduleInfo = module.resolve("module-info.class");
//            try(InputStream is = Files.newInputStream(moduleInfo)){
//                Java9Module java9Module = Java9ModuleReader.getJava9Module(is);
//                for(Java9ModuleDependency dep : java9Module.dependencies){
//                    System.err.println("# imports "+(dep.shared?"public ":"")+dep.module);
//                }
//                SortedSet<String> exports = new TreeSet<>();
//                exports.addAll(java9Module.exports);
//                for(String pkg : exports){
//                    System.err.println(pkg);
//                }
//                
//            }
//            System.err.println();
//        }
        loadPackageList();
        Set<String> modules = new TreeSet<>();
        Map<String,String> renames = new HashMap<>();
        for(String module : jdkModules.keySet()){
            if(module.startsWith("javax.")){
                String newName = "java."+module.substring(6);
                renames.put(newName, module);
                modules.add(newName);
            }else{
                modules.add(module);
            }
        }
        modules.addAll(jdkOracleModules.keySet());
        for(String module : modules){
            System.err.println("="+module);
            String realModuleName = module;
            if(renames.containsKey(module)){
                realModuleName = renames.get(module);
                System.err.println("# -> "+realModuleName);
            }
            Set<String> packages = new TreeSet<>();
            if(isJDKModule(realModuleName))
                packages.addAll(getJDKPackagesByModule(realModuleName));
            else
                packages.addAll(getOracleJDKPackagesByModule(realModuleName));
            for(String pkg : packages){
                System.err.println(pkg);
            }
            System.err.println();
        }
    }

    public static String getJava9ModuleNameFromEarlier(String name) {
        return java8To9ModuleAliases.get(name);
    }

    public static String getJava9ModuleName(String name, String version) {
        // we can't check if it's a JDK module name because that'd only work for JDK<9, since
        // it's not a Java 9 module name, so when running on Java 9 it would not match
        if(JDK.JDK9.isLowerVersion(version)){
            String alias = getJava9ModuleNameFromEarlier(name);
            if(alias != null)
                return alias;
        }
        return name;
    }
}
