package ceylon.language.descriptor;

import ceylon.language.Iterable;
import ceylon.language.empty_;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 3)
public class Module {
    private final String name;
    private final String version;
    
    @TypeInfo("ceylon.language.String")
    public final String getName() {
        return name;
    }
    
    @TypeInfo("ceylon.language.String")
    public final String getVersion() {
        return version;
    }
    private final java.lang.String doc;
    
    @TypeInfo("ceylon.language.String")
    public final java.lang.String getDoc() {
        return doc;
    }
    private final ceylon.language.Iterable<? extends ceylon.language.String> by;
    
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.String>")
    public final ceylon.language.Iterable<? extends ceylon.language.String> getBy() {
        return by;
    }
    private final String license;
    
    @TypeInfo("ceylon.language.Nothing|ceylon.language.String")
    public final String getLicense() {
        return license;
    }
    private final ceylon.language.Iterable<? extends Import> dependencies;
    
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.descriptor.Import>")
    public final ceylon.language.Iterable<? extends Import> getDependencies() {
        return dependencies;
    }
    
    @TypeInfo("ceylon.language.String")
    public final java.lang.String toString() {
        return new java.lang.StringBuilder().append("Module[").append(name.toString()).append("/").append(version.toString()).append("]").toString();
    }
    
    public Module(@Name("name")
    @TypeInfo("ceylon.language.String")
    String name, @Name("version")
    @TypeInfo("ceylon.language.String")
    String version, @Name("doc")
    @Defaulted
    @TypeInfo("ceylon.language.String")
    java.lang.String doc, @Name("by")
    @Defaulted
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.String>")
    ceylon.language.Iterable<? extends ceylon.language.String> by, @Name("license")
    @Defaulted
    @TypeInfo("ceylon.language.Nothing|ceylon.language.String")
    String license, @Name("dependencies")
    @Sequenced
    @TypeInfo("ceylon.language.Iterable<ceylon.language.descriptor.Import>")
    ceylon.language.Iterable<? extends Import> dependencies) {
        this.name = name;
        this.version = version;
        this.doc = doc;
        this.by = by;
        this.license = license;
        this.dependencies = dependencies;
    }
    
    @Ignore
    public Module(
    String name, 
    String version, 
    java.lang.String doc, 
    ceylon.language.Iterable<? extends ceylon.language.String> by, 
    String license) {
        this(name, version, doc, by, license, (Iterable)empty_.getEmpty());
    }
    
    @Ignore
    public Module(
    String name, 
    String version, 
    java.lang.String doc, 
    ceylon.language.Iterable<? extends ceylon.language.String> by) {
        this(name, version, doc, by, null, (Iterable)empty_.getEmpty());
    }
    
    @Ignore
    public Module(
    String name, 
    String version, 
    java.lang.String doc) {
        this(name, version, doc, (Iterable)empty_.getEmpty(), null, (Iterable)empty_.getEmpty());
    }
    
    @Ignore
    public Module(
    String name, 
    String version) {
        this(name, version, "", (Iterable)empty_.getEmpty(), null, (Iterable)empty_.getEmpty());
    }
    
    @Ignore
    public static final java.lang.String $init$doc(
    final String name,
    final String version) {
        return "";
    }
    
    @Ignore
    public static final ceylon.language.Iterable<? extends ceylon.language.String> $init$by(
    final String name, 
    final String version, 
    final java.lang.String doc) {
        return (Iterable)ceylon.language.empty_.getEmpty();
    }
    
    @Ignore
    public static final String $init$license(
    final String name, 
    final String version, 
    final java.lang.String doc, 
    final ceylon.language.Iterable<? extends ceylon.language.String> by) {
        return null;
    }
    
    @Ignore
    public static final ceylon.language.Iterable<? extends Import> $init$dependencies(
            final String name, 
            final String version, 
            final java.lang.String doc, 
            final ceylon.language.Iterable<? extends ceylon.language.String> by,
            String license) {
        return (Iterable)empty_.getEmpty();
    }

}

