/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.compiler.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JDKPackageList {

    private final static String JDK7 = "resources/package-list.jdk7";
    private final static String JDK7_ORACLE = "resources/package-list.oracle.jdk7";
    
    private static Map<String,Set<String>> jdkModules;
    private static Map<String,Set<String>> jdkOracleModules;
    
    private static synchronized void loadPackageList() {
        if(jdkModules != null)
            return;
        jdkModules = loadModularPackageList(JDK7);
        jdkOracleModules = loadModularPackageList(JDK7_ORACLE);
    }

    private static Map<String,Set<String>> loadModularPackageList(String file) {
        try{
            // not thread-safe, but that's OK because the caller is thread-safe
            Map<String, Set<String>> jdkPackages = new HashMap<String, Set<String>>();
            InputStream inputStream = JDKPackageList.class.getResourceAsStream(file);
            if(inputStream == null){
                throw new RuntimeException("Failed to read JDK package list file from "+file+": your compiler is broken.");
            }
            BufferedReader bis = new BufferedReader(new InputStreamReader(inputStream, "ASCII"));
            Set<String> module = null;
            String moduleName = null;
            String pkg;
            while((pkg = bis.readLine()) != null){
                // strip comments
                int commentStart = pkg.indexOf('#');
                if(commentStart > -1)
                    pkg = pkg.substring(0, commentStart);
                // strip whitespace
                pkg = pkg.trim();
                // ignore empty lines
                if(pkg.isEmpty())
                    continue;
                // see if we start a new module
                if(pkg.startsWith("=")){
                    String name = pkg.substring(1).trim();
                    if(name.isEmpty())
                        throw new RuntimeException("Failed to read JDK module list file from "+file+": module has empty name");
                    // close previous module
                    if(module != null){
                        if(module.isEmpty())
                            throw new RuntimeException("Failed to read JDK module list file from "+file+": module "+moduleName+" is empty");
                        // save previous module
                        jdkPackages.put(moduleName, Collections.unmodifiableSet(module));
                    }
                    // start the new module
                    moduleName = name;
                    module = new HashSet<String>();
                    continue;
                }
                // add a package to the current module
                if(module == null)
                    throw new RuntimeException("Failed to read JDK module list file from "+file+": adding package to undefined module");
                module.add(pkg);
            }
            bis.close();
            // close previous module
            if(module != null){
                if(module.isEmpty())
                    throw new RuntimeException("Failed to read JDK module list file from "+file+": module "+moduleName+" is empty");
                // save previous module
                jdkPackages.put(moduleName, Collections.unmodifiableSet(module));
            }
            // sanity check
            if(jdkPackages.size() == 0)
                throw new RuntimeException("Failed to read JDK package list file from "+file+"(empty package set): your compiler is broken.");
            return Collections.unmodifiableMap(jdkPackages);
        }catch(IOException x){
            throw new RuntimeException("Failed to read JDK package list file from "+file+": your compiler is broken.", x);
        }
    }

    public static boolean isJDKModule(String mod){
        loadPackageList();
        return jdkModules.containsKey(mod);
    }

    public static boolean isJDKPackage(String mod, String pkg){
        loadPackageList();
        Set<String> packages = jdkModules.get(mod);
        return packages != null && packages.contains(pkg);
    }

    public static boolean isJDKAnyPackage(String pkg){
        loadPackageList();
        for(Set<String> packages : jdkModules.values()){
            if(packages.contains(pkg))
                return true;
        }
        return false;
    }

    public static Set<String> getJDKModuleNames() {
        loadPackageList();
        return jdkModules.keySet();
    }

    public static Map<String,Set<String>> getJDKPackagesByModule() {
        loadPackageList();
        return jdkModules;
    }

    public static boolean isOracleJDKModule(String pkg){
        loadPackageList();
        return jdkOracleModules.containsKey(pkg);
    }

    public static boolean isOracleJDKPackage(String mod, String pkg){
        loadPackageList();
        Set<String> packages = jdkOracleModules.get(mod);
        return packages != null && packages.contains(pkg);
    }

    public static boolean isOracleJDKAnyPackage(String pkg){
        loadPackageList();
        for(Set<String> packages : jdkOracleModules.values()){
            if(packages.contains(pkg))
                return true;
        }
        return false;
    }

    public static Set<String> getOracleJDKModuleNames() {
        loadPackageList();
        return jdkOracleModules.keySet();
    }

    public static Map<String,Set<String>> getOracleJDKPackagesByModule() {
        loadPackageList();
        return jdkOracleModules;
    }

}
