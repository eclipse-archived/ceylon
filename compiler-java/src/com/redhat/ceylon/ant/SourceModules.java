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
            this.dir = getProject().resolveFile("source");
        }
        FileSet fs = new FileSet();
        fs.setDir(this.dir);
        // TODO Handle default module
        fs.setIncludes("**/module.ceylon");
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
            String version = new ModuleDescriptorReader(mav, dir).getModuleVersion();
            log("<sourcemodules> file " + file + "=> module " + moduleName+"/"+version, Project.MSG_VERBOSE);
            mav.setVersion(version);
            result.add(mav);
        }
        return result;
    }
    
}
