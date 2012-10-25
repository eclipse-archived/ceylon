/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted
 * by the author tags.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redhat.ceylon.cmr.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Hide JDK impl details here.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class JDKUtils {
    private final static String JDK7 = "package-list.jdk7";
    private final static String JDK7_ORACLE = "package-list.oracle.jdk7";

    private static Map<String, Set<String>> jdkModules;
    private static Map<String, Set<String>> jdkOracleModules;

    private static synchronized void loadPackageList() {
        if (jdkModules != null)
            return;
        jdkModules = loadModularPackageList(JDK7);
        jdkOracleModules = loadModularPackageList(JDK7_ORACLE);
    }

    private static Map<String, Set<String>> loadModularPackageList(String file) {
        try {
            // not thread-safe, but that's OK because the caller is thread-safe
            Map<String, Set<String>> jdkPackages = new HashMap<String, Set<String>>();
            InputStream inputStream = JDKUtils.class.getResourceAsStream(file);
            if (inputStream == null) {
                throw new RuntimeException("Failed to read JDK package list file from " + file + ": your Ceylon installation is broken.");
            }
            BufferedReader bis = new BufferedReader(new InputStreamReader(inputStream, "ASCII"));
            Set<String> module = null;
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
                    if (module != null) {
                        if (module.isEmpty())
                            throw new RuntimeException("Failed to read JDK module list file from " + file + ": module " + moduleName + " is empty");
                        // save previous module
                        jdkPackages.put(moduleName, Collections.unmodifiableSet(module));
                    }
                    // start the new module
                    moduleName = name;
                    module = new HashSet<String>();
                    continue;
                }
                // add a package to the current module
                if (module == null)
                    throw new RuntimeException("Failed to read JDK module list file from " + file + ": adding package to undefined module");
                module.add(pkg);
            }
            bis.close();
            // close previous module
            if (module != null) {
                if (module.isEmpty())
                    throw new RuntimeException("Failed to read JDK module list file from " + file + ": module " + moduleName + " is empty");
                // save previous module
                jdkPackages.put(moduleName, Collections.unmodifiableSet(module));
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
        Set<String> packages = jdkModules.get(mod);
        return packages != null && packages.contains(pkg);
    }

    public static boolean isJDKAnyPackage(String pkg) {
        loadPackageList();
        for (Set<String> packages : jdkModules.values()) {
            if (packages.contains(pkg))
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
        return jdkModules.get(module);
    }

    public static boolean isOracleJDKModule(String pkg) {
        loadPackageList();
        return jdkOracleModules.containsKey(pkg);
    }

    public static boolean isOracleJDKPackage(String mod, String pkg) {
        loadPackageList();
        Set<String> packages = jdkOracleModules.get(mod);
        return packages != null && packages.contains(pkg);
    }

    public static boolean isOracleJDKAnyPackage(String pkg) {
        loadPackageList();
        for (Set<String> packages : jdkOracleModules.values()) {
            if (packages.contains(pkg))
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
        return jdkOracleModules.get(module);
    }
}
