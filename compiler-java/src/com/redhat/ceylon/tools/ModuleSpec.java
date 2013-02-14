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
package com.redhat.ceylon.tools;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * A command-line module specification, with methods for parsing/validating etc.
 * 
 * For example:
 * <pre>
 *   com.example.foo/1.0
 *   com.example.bar
 *   DEFAULT
 * </pre>
 */
public class ModuleSpec implements Comparable<ModuleSpec> {
    
    public static enum Option {
        VERSION_REQUIRED,
        VERSION_PROHIBITED,
        DEFAULT_MODULE_PROHIBITED
    }
    
    /** The default module */
    public static final ModuleSpec DEFAULT_MODULE = new ModuleSpec("default", "");
    
    private final String name; 
    private final String version;
    
    public ModuleSpec(String name, String version) {
        this.name = name.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException(CeylonToolMessages.msg("modspec.name.missing"));
        }
        this.version = version.trim();
    }
    
    /** The module name */
    public String getName() {
        return name;
    }

    /** The module version, or the empty string if the module spec did not include a version */
    public String getVersion() {
        return version;
    }
    
    /** Whether this module spec includes a version */
    public boolean isVersioned() {
        return !version.isEmpty();
    }

    /** The formatted module spec */
    public String toString() {
        if (version == null
                || version.isEmpty()) {
            return name;
        }
        return name + "/" + version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ModuleSpec other = (ModuleSpec) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }
    
    /**
     * Compare first case sensitively on the name, and if equal then compare 
     * the versions case sensitively
     */
    @Override
    public int compareTo(ModuleSpec o) {
        int cmp = this.name.compareTo(o.name);
        if (cmp == 0) {
            if (this.version != null) {
                cmp = this.version.compareTo(o.version);
            } else {
                cmp = o.version == null ? 0 : -1;
            }
        }
        return cmp;
    }

    /**
     * Parse each of the module spec in the given iterable, according to the 
     * given version option and return the {@code ModuleSpec}s in a list.
     * @param moduleSpecs
     * @param option Parsing options
     * @return
     */
    public static List<ModuleSpec> parseEachList(Iterable<String> moduleSpecs, Option... option){
        List<ModuleSpec> modules = new LinkedList<ModuleSpec>();
        for(String moduleSpec : moduleSpecs){
            ModuleSpec spec = parse(moduleSpec, option);
            modules.add(spec);
        }
        return modules;
    }
    
    /**
     * Parse each of the module spec in the given iterable, according to the 
     * given version option and return the {@code ModuleSpec}s in a set.
     * @param moduleSpecs
     * @param options Parsing options
     * @return
     */
    public static LinkedHashSet<ModuleSpec> parseEachSet(Iterable<String> moduleSpecs, Option... options){
        LinkedHashSet<ModuleSpec> modules = new LinkedHashSet<ModuleSpec>();
        return parseEach(moduleSpecs, modules, options);
    }

    private static LinkedHashSet<ModuleSpec> parseEach(
            Iterable<String> moduleSpecs, 
            LinkedHashSet<ModuleSpec> into,
            Option... options) {
        for(String moduleSpec : moduleSpecs){
            ModuleSpec spec = parse(moduleSpec, options);
            into.add(spec);
        }
        return into;
    }

    /**
     * Parse a module spec according to the given version option.
     * @param moduleSpec
     * @param options Parsing options
     * @return The module spec
     * @throws IllegalArgumentException If the given moduleSpec is invalid
     */
    public static ModuleSpec parse(String moduleSpec, Option... options) {
        int sep = moduleSpec.indexOf("/");
        
        String name = sep != -1 ? moduleSpec.substring(0, sep) : moduleSpec;
        name = name.trim();
        String version = sep != -1 && sep < moduleSpec.length() - 1 ? moduleSpec.substring(sep+1) : "";
        version = version.trim();
        if (name.equals(DEFAULT_MODULE.name)) {
            if (contains(options, Option.DEFAULT_MODULE_PROHIBITED)) {
                throw new IllegalArgumentException(CeylonToolMessages.msg(
                        "modspec.default.prohibited"));
            }
            if (version.equals(DEFAULT_MODULE.version)) {
                return DEFAULT_MODULE;
            }
            throw new IllegalArgumentException(CeylonToolMessages.msg(
                    "modspec.default.no.version"));
        }
        if (version.isEmpty() 
                && contains(options, Option.VERSION_REQUIRED)) {
            throw new IllegalArgumentException(CeylonToolMessages.msg(
                    "modspec.version.required", moduleSpec));
        } else if ((!version.isEmpty()  || sep != -1)
                && contains(options, Option.VERSION_PROHIBITED)) {
            throw new IllegalArgumentException(CeylonToolMessages.msg(
                    "modspec.version.prohibited", moduleSpec));
        }
        ModuleSpec spec = new ModuleSpec(name, version);
        return spec;
    }
    
    private static boolean contains(Option[] options, Option opt) {
        for (Option o : options) {
            if (o == opt) {
                return true;
            }
        }
        return false;
    }
}
