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

package com.redhat.ceylon.ant;

import java.util.LinkedList;
import java.util.List;

import org.apache.tools.ant.types.Commandline;

/**
 * Ant task wrapping the {@code ceylon import-jar} tool
 * @author tom
 */
public class CeylonImportJarAntTask extends OutputRepoUsingCeylonAntTask {

    public static class Package {
        String pkg;
        
        public void setPackage(String pkg) {
            this.pkg = pkg;
        }

        public void addText(String value) {
            this.pkg = value;
        }
    }

    public static class MissingDependencyPackages {
        String module;
        List<Package> packages;
        
        public void setModule(String module) {
            this.module = module;
        }

        public void addConfiguredPackage(Package pkg) {
            this.packages.add(pkg);
        }
    }

    public CeylonImportJarAntTask() {
        super("import-jar");
    }

    @Override
    protected String getFailMessage() {
        return "import-jar failed";
    }
    
    private String module;
    private String jar;
    private String sourceJar;
    private String descriptor;
    private boolean force;
	private List<MissingDependencyPackages> missingDependenciesPackages = new LinkedList<>();

    /** Adds an list of packages to be considered as part of the specified module if it's missing */
    public void addConfiguredMissingDependencyPackages(MissingDependencyPackages missingDependencyPackages) {
        this.missingDependenciesPackages .add(missingDependencyPackages);
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getJar() {
        return jar;
    }

    public void setJar(String jar) {
        this.jar = jar;
    }

    public String getSourceJar() {
        return sourceJar;
    }

    public void setSourceJar(String sourceJar) {
        this.sourceJar = sourceJar;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public boolean getForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    @Override
    protected void completeCommandline(Commandline cmd) {
        super.completeCommandline(cmd);
        
        if (force) {
            appendOption(cmd, "--force");
        }
        
        if (descriptor != null) {
            appendOptionArgument(cmd, "--descriptor", getDescriptor());
        }
        
        if (module != null) {
            cmd.createArgument().setValue(getModule());
        }
        
        for(MissingDependencyPackages missingDependencyPackages : missingDependenciesPackages){
        	StringBuffer packages = new StringBuffer();
        	for(Package pkg : missingDependencyPackages.packages){
        		if(packages.length() > 0){
        			packages.append(",");
        		}
        		packages.append(pkg);
        	}
        	appendOptionArgument(cmd, "--missing-dependency-packages", missingDependencyPackages.module + "=" + packages.toString());
        }
        
        if (jar != null) {
            cmd.createArgument().setValue(getJar());
        }

        if (sourceJar != null) {
            cmd.createArgument().setValue(getSourceJar());
        }
    }

}
