package ceylon.language.descriptor;

import ceylon.language.Sequential;
import ceylon.language.String;
import ceylon.language.empty_;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 3)
public class Module {
    private final java.lang.String name;
    private final java.lang.String version;
    private final java.lang.String doc;
    private final Sequential<? extends String> by;
    private final java.lang.String license;
    private final Sequential<? extends Import> dependencies;
    
    @TypeInfo("ceylon.language::String")
    public final java.lang.String getName() {
        return name;
    }
    
    @TypeInfo("ceylon.language::String")
    public final java.lang.String getVersion() {
        return version;
    }
    
    @TypeInfo("ceylon.language::String")
    public final java.lang.String getDoc() {
        return doc;
    }
    
    @TypeInfo("ceylon.language::Sequential<ceylon.language::String>")
    public final Sequential<? extends String> getBy() {
        return by;
    }
    
    @TypeInfo("ceylon.language::Nothing|ceylon.language::String")
    public final java.lang.String getLicense() {
        return license;
    }
    
    @TypeInfo("ceylon.language::Sequential<ceylon.language.descriptor::Import>")
    public final Sequential<? extends Import> getDependencies() {
        return dependencies;
    }
    
    @TypeInfo("ceylon.language::String")
    public final java.lang.String toString() {
        return new java.lang.StringBuilder().append("Module[").append(name.toString()).append("/").append(version.toString()).append("]").toString();
    }
    
    public Module(@Name("name")
    @TypeInfo("ceylon.language::String")
    java.lang.String name, @Name("version")
    @TypeInfo("ceylon.language::String")
    java.lang.String version, @Name("doc")
    @Defaulted
    @TypeInfo("ceylon.language::String")
    java.lang.String doc, @Name("by")
    @Defaulted
    @TypeInfo("ceylon.language::Sequential<ceylon.language::String>")
    Sequential<? extends String> by, @Name("license")
    @Defaulted
    @TypeInfo("ceylon.language::Nothing|ceylon.language::String")
    java.lang.String license, @Name("dependencies")
    @Sequenced
    @TypeInfo("ceylon.language::Sequential<ceylon.language.descriptor::Import>")
    Sequential<? extends Import> dependencies) {
        this.name = name;
        this.version = version;
        this.doc = doc;
        this.by = by;
        this.license = license;
        this.dependencies = dependencies;
    }
    
    @Ignore
    public Module(
            java.lang.String name, 
            java.lang.String version, 
            java.lang.String doc, 
            Sequential<? extends String> by, 
            java.lang.String license) {
        this(name, version, doc, by, license, (Sequential)empty_.getEmpty$());
    }
    
    @Ignore
    public Module(
            java.lang.String name, 
            java.lang.String version, 
            java.lang.String doc, 
            Sequential<? extends String> by) {
        this(name, version, doc, by, null, (Sequential)empty_.getEmpty$());
    }
    
    @Ignore
    public Module(
            java.lang.String name, 
            java.lang.String version, 
            java.lang.String doc) {
        this(name, version, doc, (Sequential)empty_.getEmpty$(), null, (Sequential)empty_.getEmpty$());
    }
    
    @Ignore
    public Module(
            java.lang.String name, 
            java.lang.String version) {
        this(name, version, "", (Sequential)empty_.getEmpty$(), null, (Sequential)empty_.getEmpty$());
    }
    
    @Ignore
    public static final java.lang.String $init$doc(
            final java.lang.String name,
            final java.lang.String version) {
        return "";
    }
    
    @Ignore
    public static final Sequential<? extends String> $init$by(
            final java.lang.String name, 
            final java.lang.String version, 
            final java.lang.String doc) {
        return (Sequential)empty_.getEmpty$();
    }
    
    @Ignore
    public static final String $init$license(
            final java.lang.String name, 
            final java.lang.String version, 
            final java.lang.String doc, 
            final Sequential<? extends String> by) {
        return null;
    }
    
    @Ignore
    public static final Sequential<? extends Import> $init$dependencies(
            final java.lang.String name, 
            final java.lang.String version, 
            final java.lang.String doc, 
            final Sequential<? extends String> by,
            String license) {
        return (Sequential)empty_.getEmpty$();
    }

}

