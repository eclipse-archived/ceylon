package com.redhat.ceylon.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.redhat.ceylon.common.ant.Module;

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
            return getProject().resolveFile("source");
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
        final ModuleDescriptorReader reader = new ModuleDescriptorReader(module, getSrc());
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
