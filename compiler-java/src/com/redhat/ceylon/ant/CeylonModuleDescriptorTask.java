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

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.redhat.ceylon.common.Constants;


/**
 * Any task to extract information from a Ceylon module descriptor and 
 * set ant properties with it
 * @author tom
 */
public class CeylonModuleDescriptorTask extends Task {

    private Module module;
    private String versionProperty;
    private String nameProperty;
    private String licenseProperty;
    private File src;

    public void setSrc(File srcDir) {
        this.src = srcDir;
    }
    public File getSrc() {
        if (this.src == null) {
            return getProject().resolveFile(Constants.DEFAULT_SOURCE_DIR);
        }
        return src;
    }
    public void setModule(Module module){
        this.module = module;
    }
    public void setVersionProperty(String versionProperty) {
        this.versionProperty = versionProperty;
    }
    public void setNameProperty(String nameProperty) {
        this.nameProperty = nameProperty;
    }
    public void setLicenseProperty(String licenseProperty) {
        this.licenseProperty = licenseProperty;
    }
    
    /**
     * Executes the task.
     * @exception BuildException if an error occurs
     */
    @Override
    public void execute() throws BuildException {
        Java7Checker.check();
        final ModuleDescriptorReader reader = new ModuleDescriptorReader(module.getName(), getSrc());
        if (versionProperty != null) {
            setProjectProperty(versionProperty, reader.getModuleVersion());
        }
        if (nameProperty != null) {
            setProjectProperty(nameProperty, reader.getModuleName());
        }
        if (licenseProperty != null) {
            setProjectProperty(licenseProperty, reader.getModuleLicense());
        }
    }
    
    private void setProjectProperty(String versionProperty, String value) {
        String existingValue = getProject().getProperty(versionProperty);
        if (existingValue == null) {
            log("Setting " + versionProperty + " = " + value + " based on value in module.ceylon descriptor of module " + this.module + " in " + getSrc());
            getProject().setNewProperty(versionProperty, value);
        } else {
            log("Property " + versionProperty + " has already been set to " + existingValue);
        }
    }
    
}
