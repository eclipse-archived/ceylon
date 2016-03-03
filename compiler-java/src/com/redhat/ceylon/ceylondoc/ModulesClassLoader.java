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

import java.util.LinkedList;
import java.util.List;

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
        if(jdkProvider.isJDKModule(moduleName)){
            return jdkProvider.isJDKPackage(moduleName, name);
        }
        return jars.packageExists(module, name);
    }

    public List<String> getPackageList(Module module, String name) {
        String moduleName = module.getNameAsString();
        if(jdkProvider.isJDKModule(moduleName)){
            return getJDKPackageList(name);
        }
        return jars.getPackageList(module, name);
    }

    private List<String> getJDKPackageList(String name) {
    	List<String> ret = new LinkedList<String>();
    	String prefix = name+".";
    	for(String pkg : jdkProvider.getJDKPackageList()){
    		if(pkg.equals(name) || pkg.startsWith(prefix))
    			ret.add(pkg);
    	}
    	return ret;
    }
}