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
package org.eclipse.ceylon.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Path;
import org.eclipse.ceylon.common.Backend;
import org.eclipse.ceylon.common.Constants;
import org.eclipse.ceylon.common.tools.ModuleWildcardsHelper;

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
@AntDoc("A pattern used in a `<moduleset>`")
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
            Collection<String> mods = ModuleWildcardsHelper.expandWildcards(
                    getSrc(), Collections.singletonList(match), be);
            for (String mod : mods) {
                result.add(new Module(mod));
            }
        }
        return result;
    }
    
}
