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
package com.redhat.ceylon.ceylondoc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.loader.JdkProvider;
import com.redhat.ceylon.model.loader.impl.reflect.CachedTOCJars;
import com.redhat.ceylon.model.typechecker.model.Module;

/**
 * Class loader which looks into a list of jar files
 */
class ModulesClassLoader extends ClassLoader {

    private CachedTOCJars jars = new CachedTOCJars();
	private JdkProvider jdkProvider;
    
    public ModulesClassLoader(ClassLoader parent, JdkProvider jdkProvider) {
        super(parent);
        this.jdkProvider = jdkProvider;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String path = name.replace('.', '/').concat(".class");
        byte[] contents = jars.getContents(path);
        if(contents != null)
            return defineClass(name, contents, 0, contents.length);
        return super.findClass(name);
    }

    public void addJar(ArtifactResult artifact, Module module, boolean skipContents) {
        jars.addJar(artifact, module, skipContents);
    }

    public boolean packageExists(Module module, String name) {
        String moduleName = module.getNameAsString();
        if(JDKUtils.isJDKModule(moduleName)){
            return JDKUtils.isJDKPackage(moduleName, name);
        }
        if(JDKUtils.isOracleJDKModule(moduleName)){
            return JDKUtils.isOracleJDKPackage(moduleName, name);
        }
        return jars.packageExists(module, name);
    }

    public List<String> getPackageList(Module module, String name) {
        String moduleName = module.getNameAsString();
        if(JDKUtils.isJDKModule(moduleName)
                || JDKUtils.isOracleJDKModule(moduleName)){
            return getJDKPackageList(name);
        }
        return jars.getPackageList(module, name);
    }

    private List<String> getJDKPackageList(String name) {
        // Currently limit to rt.jar to avoid scanning everything
        // Try the boot classpath first, since that's where rt.jar is supposed to sit
        File rtJar = findRtJar("sun.boot.class.path");
        if(rtJar == null)
            rtJar = findRtJar("java.class.path");
        if(rtJar == null)
            return Collections.emptyList();
        String path = name.replace('.', '/') + "/";
        List<String> ret = new LinkedList<String>();

        try(JarInputStream is = new JarInputStream(new FileInputStream(rtJar))){

            JarEntry entry;
            while( (entry = is.getNextJarEntry()) != null) {
                String entryName = entry.getName();
                if(entryName.endsWith(".class") && entryName.startsWith(path)) {
                    ret.add(entryName);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to load rt.jar", e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load rt.jar", e);
        }
        return ret;
    }

    private File findRtJar(String property) {
        String path = System.getProperty(property);
        if(path == null)
            return null;
        for(String entry : path.split(File.pathSeparator)){
            File file = new File(entry);
            if(file.exists() && file.isFile() && file.getName().toLowerCase().equals("rt.jar")){
                return file;
            }
        }
        return null;
    }

}