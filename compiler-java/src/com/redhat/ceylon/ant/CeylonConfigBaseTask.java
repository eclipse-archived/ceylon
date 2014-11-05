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
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import com.redhat.ceylon.common.config.CeylonConfig;
import com.redhat.ceylon.common.config.CeylonConfigFinder;


/**
 * A base class for ant tasks dealing with retrieving information from the Ceylon configuration
 * @author Tako Schotanus
 */
public abstract class CeylonConfigBaseTask extends Task {

    private File dir;
    private File file;
    private boolean unparsed;

    public void setDir(File dir) {
        this.dir = dir;
    }
    
    public File getDir() {
        return dir;
    }
    
    public void setFile(File file) {
        this.file = file;
    }
    
    public File getFile() {
        return file;
    }
    
    public boolean isUnparsed() {
        return unparsed;
    }

    public void setUnparsed(boolean unparsed) {
        this.unparsed = unparsed;
    }

    protected CeylonConfig getConfig() throws BuildException {
        try {
            if (file != null) {
                if (unparsed) {
                    return CeylonConfigFinder.loadOriginalConfigFromFile(file);
                } else {
                    return CeylonConfigFinder.loadConfigFromFile(file);
                }
            } else {
                File cdir = (dir != null) ? dir : new File(".");
                return CeylonConfigFinder.loadDefaultConfig(cdir);
            }
        } catch (IOException ex) {
            throw new BuildException("Could not read configuration", ex);
        }
    }

    protected void setConfigValueAsProperty(String[] values, String propertyName) {
        if (values.length == 1) {
            setProjectProperty(propertyName, values[0]);
        } else {
            StringBuilder joinedValues = new StringBuilder();
            for (int i=0; i < values.length; i++) {
                setProjectProperty(propertyName + "_" + i, values[i]);
                if (i > 0) {
                    joinedValues.append(",");
                }
                joinedValues.append(values[i]);
            }
            setProjectProperty(propertyName, joinedValues.toString());
        }
    }
    
    protected void setProjectProperty(String propertyName, String newValue) {
        String existingValue = getProject().getProperty(propertyName);
        if (existingValue == null) {
            log("Setting '" + propertyName + "' to '" + newValue + "'", Project.MSG_VERBOSE);
            getProject().setNewProperty(propertyName, newValue);
        } else {
            log("Property '" + propertyName + "' has already been set to '" + existingValue + "'", Project.MSG_VERBOSE);
        }
    }
    
}
