package ceylon.language.descriptor;

import ceylon.language.Iterable;
import ceylon.language.$empty;

import com.redhat.ceylon.compiler.metadata.java.Defaulted;
import com.redhat.ceylon.compiler.metadata.java.Ignore;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.Sequenced;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

public class Module {
    private ceylon.language.Quoted name;
    private ceylon.language.Quoted version;
    private java.lang.String doc;
    private Iterable<? extends ceylon.language.String> by;
    private ceylon.language.Quoted license;
    private Iterable<? extends ceylon.language.descriptor.Import> dependencies;

    public Module(@Name("name") ceylon.language.Quoted name, 
            @Name("version") ceylon.language.Quoted version, 
            @Name("doc") @Defaulted java.lang.String doc, 
            @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.String>") 
            @Name("by") @Defaulted ceylon.language.Iterable<? extends ceylon.language.String> by, 
            @TypeInfo("ceylon.language.Nothing|ceylon.language.Quoted") 
            @Name("license") @Defaulted ceylon.language.Quoted license,
            @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.descriptor.Import>") 
            @Name("dependencies") @Sequenced 
            ceylon.language.Iterable<? extends ceylon.language.descriptor.Import> dependencies){
        this.name = name;
        this.version = version;
        this.doc = doc;
        this.by = by;
        this.license = license;
        this.dependencies = dependencies;
    }

    public ceylon.language.Quoted getName() {
        return name;
    }

    public ceylon.language.Quoted getVersion() {
        return version;
    }

    public java.lang.String getDoc() {
        return doc;
    }

    public Iterable<? extends ceylon.language.String> getBy() {
        return by;
    }

    public ceylon.language.Quoted getLicense() {
        return license;
    }

    public Iterable<? extends ceylon.language.descriptor.Import> getDependencies() {
        return dependencies;
    }
    
    @Ignore
    public static final class Module$impl {
        
        public Module$impl() {
            super();
        }
        
        static java.lang.String $init$doc(ceylon.language.descriptor.Module $this) {
            return "";
        }
        
        static Iterable<? extends ceylon.language.String> $init$by(ceylon.language.descriptor.Module $this) {
            return $empty.getEmpty();
        }
        
        static ceylon.language.Quoted $init$license(ceylon.language.descriptor.Module $this) {
            return null;
        }
    }
}
