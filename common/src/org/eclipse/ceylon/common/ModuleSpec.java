package org.eclipse.ceylon.common;

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
    public static final ModuleSpec DEFAULT_MODULE = new ModuleSpec(null, "default", "");
    
    private final String namespace; 
    private final String name; 
    private final String version;
    
    public ModuleSpec(String namespace, String name, String version) {
        this.namespace = namespace != null ? namespace.trim() : null;
        this.name = name.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException(CommonMessages.msg("modspec.name.missing"));
        }
        this.version = version.trim();
    }
    
    /** The module namespace */
    public String getNamespace() {
        return namespace;
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
        return ModuleUtil.makeModuleName(namespace, name, version);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
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
        if (namespace == null) {
            if (other.namespace != null)
                return false;
        } else if (!namespace.equals(other.namespace))
            return false;
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
        int result = MiscUtil.compare(namespace, o.namespace);
        if (result == 0) {
            result = MiscUtil.compare(name, o.name);
            if (result == 0) {
                result = CeylonVersionComparator.compareVersions(version, o.version);
            }
        }
        return result;
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
        
        String namespace = ModuleUtil.getNamespaceFromUri(name);
        name = ModuleUtil.getModuleNameFromUri(name);
        
        String version = sep != -1 && sep < moduleSpec.length() - 1 ? moduleSpec.substring(sep+1) : "";
        version = version.trim();
        if (name.equals(DEFAULT_MODULE.name)) {
            if (contains(options, Option.DEFAULT_MODULE_PROHIBITED)) {
                throw new IllegalArgumentException(CommonMessages.msg(
                        "modspec.default.prohibited"));
            }
            if (version.equals(DEFAULT_MODULE.version)) {
                return DEFAULT_MODULE;
            }
            throw new IllegalArgumentException(CommonMessages.msg(
                    "modspec.default.no.version"));
        }
        if (version.isEmpty() 
                && contains(options, Option.VERSION_REQUIRED)) {
            throw new IllegalArgumentException(CommonMessages.msg(
                    "modspec.version.required", moduleSpec));
        } else if ((!version.isEmpty()  || sep != -1)
                && contains(options, Option.VERSION_PROHIBITED)) {
            throw new IllegalArgumentException(CommonMessages.msg(
                    "modspec.version.prohibited", moduleSpec));
        }
        ModuleSpec spec = new ModuleSpec(namespace, name, version);
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
