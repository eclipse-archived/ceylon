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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class JDKPackageList {

    private final static String JDK7 = "resources/package-list.jdk7";
    private final static String JDK7_ORACLE = "resources/package-list.oracle.jdk7";
    
    public static Set<String> jdkPackages;
    public static Set<String> jdkOraclePackages;
    
    private static synchronized void loadPackageList() {
        if(jdkPackages != null)
            return;
        jdkPackages = loadPackageList(JDK7);
        jdkOraclePackages = loadPackageList(JDK7_ORACLE);
    }
    
    private static Set<String> loadPackageList(String file) {
        try{
            // not thread-safe, but that's OK because the caller is thread-safe
            Set<String> jdkPackages = new HashSet<String>();
            InputStream inputStream = JDKPackageList.class.getResourceAsStream(file);
            if(inputStream == null){
                throw new RuntimeException("Failed to read JDK package list file from "+file+": your compiler is broken.");
            }
            BufferedReader bis = new BufferedReader(new InputStreamReader(inputStream, "ASCII"));
            String pkg;
            while((pkg = bis.readLine()) != null){
                if(!pkg.isEmpty())
                    jdkPackages.add(pkg);
            }
            bis.close();
            // sanity check
            if(jdkPackages.size() == 0)
                throw new RuntimeException("Failed to read JDK package list file from "+file+"(empty package set): your compiler is broken.");
            return Collections.unmodifiableSet(jdkPackages);
        }catch(IOException x){
            throw new RuntimeException("Failed to read JDK package list file from "+file+": your compiler is broken.", x);
        }
    }
    
    public static boolean isJDKPackage(String pkg){
        loadPackageList();
        return jdkPackages.contains(pkg);
    }

    public static boolean isOracleJDKPackage(String pkg){
        loadPackageList();
        return jdkOraclePackages.contains(pkg);
    }
}
