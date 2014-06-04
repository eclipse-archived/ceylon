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
import java.net.URI;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.FileSet;

import com.redhat.ceylon.common.Constants;

/**
 * Generates a set of modules by scanning a source directory for ceylon modules.
 * {@code <sourcemodules>} is supported as a subelement of {@code <moduleset>}.
 */
public class SourceModules extends ProjectComponent {

    private File dir;

    public void setDir(File dir) {
        this.dir = dir;
    }
    
    // TODO filters by module name, supported backends (transitive)
    
    public Set<Module> getModules() {
        if (this.dir == null) {
            this.dir = getProject().resolveFile(Constants.DEFAULT_SOURCE_DIR);
        }
        FileSet fs = new FileSet();
        fs.setDir(this.dir);
        // TODO Handle default module
        fs.setIncludes("**/" + Constants.MODULE_DESCRIPTOR);
        DirectoryScanner ds = fs.getDirectoryScanner(getProject());
        String[] files = ds.getIncludedFiles();
        log("<sourcemodules> found files " + Arrays.toString(files), Project.MSG_VERBOSE);
        URI base = dir.toURI();
        LinkedHashSet<Module> result = new LinkedHashSet<Module>();
        for (String file : files) {
            URI uri = new File(this.dir, file).getParentFile().toURI();
            log("<sourcemodules> file " + file + "=> uri " + uri, Project.MSG_VERBOSE);
            String moduleName = base.relativize(uri).getPath().replace('/', '.');
            if (moduleName.endsWith(".")) {
                moduleName = moduleName.substring(0, moduleName.length()-1);
            }
            log("<sourcemodules> file " + file + "=> moduleName " + moduleName, Project.MSG_VERBOSE);
            Module mav = new Module();
            mav.setName(moduleName);
            String version = new ModuleDescriptorReader(mav.getName(), dir).getModuleVersion();
            log("<sourcemodules> file " + file + "=> module " + moduleName+"/"+version, Project.MSG_VERBOSE);
            mav.setVersion(version);
            result.add(mav);
        }
        return result;
    }
    
}
