package ceylon.language.descriptor;

import ceylon.language.empty_;
import ceylon.language.Iterable;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 2)
public class Module {
    private final ceylon.language.Quoted name;
    private final ceylon.language.Quoted version;
    
    @TypeInfo("ceylon.language.Quoted")
    public final ceylon.language.Quoted getName() {
        return name;
    }
    
    @TypeInfo("ceylon.language.Quoted")
    public final ceylon.language.Quoted getVersion() {
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
    private final ceylon.language.Quoted license;
    
    @TypeInfo("ceylon.language.Nothing|ceylon.language.Quoted")
    public final ceylon.language.Quoted getLicense() {
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
    @TypeInfo("ceylon.language.Quoted")
    ceylon.language.Quoted name, @Name("version")
    @TypeInfo("ceylon.language.Quoted")
    ceylon.language.Quoted version, @Name("doc")
    @Defaulted
    @TypeInfo("ceylon.language.String")
    java.lang.String doc, @Name("by")
    @Defaulted
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.String>")
    ceylon.language.Iterable<? extends ceylon.language.String> by, @Name("license")
    @Defaulted
    @TypeInfo("ceylon.language.Nothing|ceylon.language.Quoted")
    ceylon.language.Quoted license, @Name("dependencies")
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
    ceylon.language.Quoted name, 
    ceylon.language.Quoted version, 
    java.lang.String doc, 
    ceylon.language.Iterable<? extends ceylon.language.String> by, 
    ceylon.language.Quoted license) {
        this(name, version, doc, by, license, (Iterable)empty_.getEmpty());
    }
    
    @Ignore
    public Module(
    ceylon.language.Quoted name, 
    ceylon.language.Quoted version, 
    java.lang.String doc, 
    ceylon.language.Iterable<? extends ceylon.language.String> by) {
        this(name, version, doc, by, null, (Iterable)empty_.getEmpty());
    }
    
    @Ignore
    public Module(
    ceylon.language.Quoted name, 
    ceylon.language.Quoted version, 
    java.lang.String doc) {
        this(name, version, doc, (Iterable)empty_.getEmpty(), null, (Iterable)empty_.getEmpty());
    }
    
    @Ignore
    public Module(
    ceylon.language.Quoted name, 
    ceylon.language.Quoted version) {
        this(name, version, "", (Iterable)empty_.getEmpty(), null, (Iterable)empty_.getEmpty());
    }
    
    @Ignore
    public static final java.lang.String $init$doc(
    final ceylon.language.Quoted name,
    final ceylon.language.Quoted version) {
        return "";
    }
    
    @Ignore
    public static final ceylon.language.Iterable<? extends ceylon.language.String> $init$by(
    final ceylon.language.Quoted name, 
    final ceylon.language.Quoted version, 
    final java.lang.String doc) {
        return (Iterable)ceylon.language.empty_.getEmpty();
    }
    
    @Ignore
    public static final ceylon.language.Quoted $init$license(
    final ceylon.language.Quoted name, 
    final ceylon.language.Quoted version, 
    final java.lang.String doc, 
    final ceylon.language.Iterable<? extends ceylon.language.String> by) {
        return null;
    }
    
    @Ignore
    public static final ceylon.language.Iterable<? extends Import> $init$dependencies(
            final ceylon.language.Quoted name, 
            final ceylon.language.Quoted version, 
            final java.lang.String doc, 
            final ceylon.language.Iterable<? extends ceylon.language.String> by,
            ceylon.language.Quoted license) {
        return (Iterable)empty_.getEmpty();
    }

}

