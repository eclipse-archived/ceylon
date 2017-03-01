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

import java.lang.reflect.Method;
import java.util.Date;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.redhat.ceylon.launcher.CeylonClassLoader;


/**
 * Ant task to extract information from a Ceylon module descriptor and 
 * set ant properties with it
 * @author tom
 */
@AntDoc("Task which set an ant property to the OSGI bundle version\n"
      + "corresponding to a Ceylon version passed as task parameter.\n"
      + "\n"
      + "<!-- lang: xml -->\n"
      + "    <target name=\"descriptor\" depends=\"ceylon-ant-taskdefs\">\n"
      + "      <ceylon-osgi-version\n"
      + "            ceylonVersion=\"1.3.3-SNAPSHOT\"\n"
      + "            addTimeStamp=\"true\"\n"
      + "      />\n"
      + "      <echo message=\"Osgi Version: ${ceylonOsgiVersion}\" />\n"
      + "    </target>\n")
public class CeylonOsgiVersionTask extends Task {

    private String ceylonVersionProperty;
    private boolean addTimeStampProperty;
    private String propertyName;
    private CeylonClassLoader loader;
    private Method fromCeylonVersionMethod;
    private Method withTimestampMethod;

    @AntDoc("The Ceylon version from the OSGI version should be built.")
    @Required
    public void setCeylonVersion(String ceylonVersion) {
        this.ceylonVersionProperty = ceylonVersion;
    }
    
    @AntDoc("Flag to indicate if the timestamp should be suffixed to the built OSGI version")
    @Required
    public void setAddTimeStamp(boolean addTimeStamp){
        this.addTimeStampProperty = addTimeStamp;
    }
    
    @AntDoc("The name of the property that will receive the OSGI version.")
    @Required
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
    
    
    /**
     * Executes the task.
     * @exception BuildException if an error occurs
     */
    @Override
    public void execute() throws BuildException {
        Java7Checker.check();
        try {
            if (loader == null) {
                loader = Util.getCeylonClassLoaderCachedInProject(getProject());
            }
            
            if (fromCeylonVersionMethod == null) {
                Class<?> klass = loader.loadClass("com.redhat.ceylon.model.loader.OsgiVersion");
                Class<?>[] parameterTypes = { String.class };
                fromCeylonVersionMethod = klass.getDeclaredMethod("fromCeylonVersion", parameterTypes);
            }

            if (withTimestampMethod == null) {
                Class<?> klass = loader.loadClass("com.redhat.ceylon.model.loader.OsgiVersion");
                Class<?>[] parameterTypes = { String.class, Date.class };
                withTimestampMethod = klass.getDeclaredMethod("withTimestamp", parameterTypes);
            }

            String osgiVersion = (String) fromCeylonVersionMethod.invoke(null, ceylonVersionProperty);
            if (addTimeStampProperty) {
                osgiVersion = (String) withTimestampMethod.invoke(null, osgiVersion, new Date());
            }
            log("Setting " + propertyName + " = " + osgiVersion + " based on value of the Ceylon version: " + ceylonVersionProperty);
            getProject().setProperty(propertyName, osgiVersion);
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }
}
