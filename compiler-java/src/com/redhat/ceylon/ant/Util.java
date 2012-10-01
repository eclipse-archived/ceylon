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
import org.apache.tools.ant.Project;

public class Util {
    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().indexOf("windows") > -1;

    public static String getScriptName(String name) {
        if(IS_WINDOWS)
            return name + ".bat";
        return name;
    }

    public static String quoteParameter(String param) {
        if (IS_WINDOWS) {
            return "\"" + param + "\"";
        }
        return param;
    }

    /**
     * Tries to find the given script either user-specified or detected
     */
    public static String findCeylonScript(File defaultValue, Project project) {
        if (defaultValue != null
                && (defaultValue.getName().contains("ceylonc")
                        || defaultValue.getName().contains("ceylond"))) {
            project.log("The Ceylon ant tasks now use the ceylon tool for " +
                    "their executable attribute", Project.MSG_WARN);
        }
        String scriptName = "cey";
        if(defaultValue != null){
            if(!defaultValue.exists())
                throw new BuildException("Failed to find '"+scriptName+"' executable in "+defaultValue.getPath());
            if(!defaultValue.canExecute())
                throw new BuildException("Cannot execute '"+scriptName+"' executable in "+defaultValue.getPath()+" (not executable)");
            return defaultValue.getAbsolutePath();
        }
        // try to guess from the "ceylon.home" project property
        String ceylonHome = project.getProperty("ceylon.home");
        if(ceylonHome == null || ceylonHome.isEmpty()){
            // try again from the CEYLON_HOME env var
            ceylonHome = System.getenv("CEYLON_HOME");
        }
        if(ceylonHome == null || ceylonHome.isEmpty())
            throw new BuildException("Failed to find Ceylon home, specify the ceylon.home property or set the CEYLON_HOME environment variable");
        // now try to find the executable
        String scriptPath = ceylonHome + File.separatorChar + "bin" + File.separatorChar + getScriptName(scriptName);
        File script = new File(scriptPath);
        if(!script.exists())
            throw new BuildException("Failed to find '"+scriptName+"' executable in "+ceylonHome);
        if(!script.canExecute())
            throw new BuildException("Cannot execute '"+scriptName+"' executable in "+ceylonHome+" (not executable)");
        return script.getAbsolutePath();
    }

}
