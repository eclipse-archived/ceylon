package com.redhat.ceylon.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Path;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.tools.ModuleWildcardsHelper;

/**
 * Represents a pattern to be used in a ModuleSet.
 * 
 * The <code>match</code> attribute can be a string
 * representing part of a module name and should
 * either terminate with an asterisk (*) or only
 * consist of a single asterisk.
 * 
 * The optional <code>backend</code> attribute can
 * be used to filter the result depending for which
 * backend the module is meant. Can have the values
 * <code>jvm</code> or <code>js</code>.
 * 
 * @author Tako Schotanus
 */
public class Pattern extends DataType {
    private String match;
    private String backend;
    private Path src;

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }
    
    public String getBackend() {
        return backend;
    }

    public void setBackend(String backend) {
        this.backend = backend;
    }

    /**
     * Set the source directories to find the source Java and Ceylon files.
     * @param src the source directories as a path
     */
    public void setSrc(Path src) {
        if (this.src == null) {
            this.src = src;
        } else {
            this.src.append(src);
        }
    }

    public void setSource(Path src) {
        if (this.src == null) {
            this.src = src;
        } else {
            this.src.append(src);
        }
    }
    
    public static class Src {
        String value;
        
        public void setValue(String value) {
            this.value = value;
        }

        public void addText(String value) {
            this.value = value;
        }
    }
    
    public void addConfiguredSrc(Src src) {
        Path p = new Path(getProject(), src.value);
        if (this.src == null) {
            this.src = p;
        } else {
            this.src.append(p);
        }
    }

    public void addConfiguredSource(Src src) {
        Path p = new Path(getProject(), src.value);
        if (this.src == null) {
            this.src = p;
        } else {
            this.src.append(p);
        }
    }

    public List<File> getSrc() {
        if (this.src == null) {
            return Collections.singletonList(getProject().resolveFile(Constants.DEFAULT_SOURCE_DIR));
        }
        String[] paths = this.src.list();
        ArrayList<File> result = new ArrayList<File>(paths.length);
        for (String path : paths) {
            result.add(getProject().resolveFile(path));
        }
        return result;
    }
    
    public Set<Module> getModules() {
        LinkedHashSet<Module> result = new LinkedHashSet<Module>(); 
        if (match != null) {
            Backend be = null;
            if (backend != null) {
                be = Backend.fromAnnotation(backend);
                if (be == null) {
                    throw new RuntimeException("Unknown backend for pattern: " + backend);
                }
            }
            List<String> mods = ModuleWildcardsHelper.expandWildcards(
                    getSrc(), Collections.singletonList(match), be);
            for (String mod : mods) {
                result.add(new Module(mod));
            }
        }
        return result;
    }
    
}
