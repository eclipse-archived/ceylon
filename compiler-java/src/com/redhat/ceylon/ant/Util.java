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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;

import com.redhat.ceylon.launcher.CeylonClassLoader;
import com.redhat.ceylon.launcher.ClassLoaderSetupException;
import com.redhat.ceylon.launcher.Launcher;
import com.redhat.ceylon.launcher.LauncherUtil;

public class Util {
    /**
     * Name of the project reference in which we cache the class loader
     */
    private static final String CEYLON_CLASSLOADER_REFERENCE = "ceylon.classloader";
    
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
        String scriptName = getScriptName("ceylon");
        if(defaultValue != null){
            if(!defaultValue.exists())
                throw new BuildException("Failed to find '"+scriptName+"' executable in "+defaultValue.getPath());
            if(!defaultValue.canExecute())
                throw new BuildException("Cannot execute '"+scriptName+"' executable in "+defaultValue.getPath()+" (not executable)");
            return defaultValue.getAbsolutePath();
        }
        File ceylonHome = null;
        try {
            ceylonHome = LauncherUtil.determineHome();
        } catch (URISyntaxException e) {
            throw new BuildException("Failed to determine Ceylon home", e);
        }
        if(ceylonHome == null)
            throw new BuildException("Failed to find Ceylon home, specify the ceylon.home property or set the CEYLON_HOME environment variable");
        // now try to find the executable
        File script = new File(new File(ceylonHome, "bin"), scriptName);
        if(!script.exists())
            throw new BuildException("Failed to find '"+scriptName+"' executable in "+ceylonHome);
        if(!script.canExecute())
            throw new BuildException("Cannot execute '"+scriptName+"' executable in "+ceylonHome+" (not executable)");
        return script.getAbsolutePath();
    }

    // duplicated from /ceylon-common/src/com/redhat/ceylon/common/FileUtil.java because it's not in Ant's classpath :(
    public static boolean isChildOfOrEquals(File parent, File child){
        // doing a single comparison is likely cheaper than walking up to the root
        try {
            String parentPath = parent.getCanonicalPath();
            String childPath = child.getCanonicalPath();
            if(parentPath.equals(childPath))
                return true;
            // make sure we compare with a separator, otherwise /foo would be considered a parent of /foo-bar
            if(!parentPath.endsWith(File.separator))
                parentPath += File.separator;
            return childPath.startsWith(parentPath);
        } catch (IOException e) {
            return false;
        }
        
    }
    
    public static CeylonClassLoader getCeylonClassLoaderCachedInProject(final Project project) throws ClassLoaderSetupException {
        Object classLoader = project.getReference(CEYLON_CLASSLOADER_REFERENCE);
        if(classLoader != null){
            CeylonClassLoader oldLoader = (CeylonClassLoader) classLoader;
            // make sure it's still valid
            try{
                List<File> classPath = CeylonClassLoader.getClassPath();
                if(oldLoader.hasSignature(CeylonClassLoader.getClassPathSignature(classPath))){
                    // compatible
                    return oldLoader;
                }else{
                    project.log("Needs a new class loader: cp changed!", Project.MSG_VERBOSE);
                    CeylonClassLoader loader = CeylonClassLoader.newInstance(classPath);
                    project.addReference(CEYLON_CLASSLOADER_REFERENCE, loader);
                    return loader;
                }
            }catch(FileNotFoundException x){
                throw new ClassLoaderSetupException(x);
            } catch (URISyntaxException x) {
                throw new ClassLoaderSetupException(x);
            } catch (MalformedURLException x) {
                throw new ClassLoaderSetupException(x);
            }
        }
        CeylonClassLoader loader = Launcher.getClassLoader();
        project.addReference(CEYLON_CLASSLOADER_REFERENCE, loader);
        // only add the build listed once, even if we change the class loader later
        project.addBuildListener(new BuildListener(){

            @Override
            public void buildFinished(BuildEvent arg0) {
                project.log("Build done, cleaning up Ceylon class loader", Project.MSG_VERBOSE);
                // make sure we get the latest one
                Object reference = project.getReference(CEYLON_CLASSLOADER_REFERENCE);
                project.getReferences().remove(CEYLON_CLASSLOADER_REFERENCE);
                if(reference instanceof CeylonClassLoader){
                    ((CeylonClassLoader) reference).clearCache();
                }
            }

            @Override
            public void buildStarted(BuildEvent arg0) {
            }

            @Override
            public void messageLogged(BuildEvent arg0) {
            }

            @Override
            public void targetFinished(BuildEvent arg0) {
            }

            @Override
            public void targetStarted(BuildEvent arg0) {
            }

            @Override
            public void taskFinished(BuildEvent arg0) {
            }

            @Override
            public void taskStarted(BuildEvent arg0) {
            }
        });
        return loader;
    }
}
