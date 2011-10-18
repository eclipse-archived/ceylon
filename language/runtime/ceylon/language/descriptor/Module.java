package ceylon.language.descriptor;

import ceylon.language.Iterable;

import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.Sequenced;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

public class Module {
    private ceylon.language.Quoted name;
    private ceylon.language.Quoted version;
    private ceylon.language.String doc;
    private Iterable<? extends ceylon.language.String> authors;
    private ceylon.language.Quoted license;
    private Iterable<? extends ceylon.language.descriptor.Import> dependencies;

    public Module(@Name("name") ceylon.language.Quoted name, 
            @Name("version") ceylon.language.Quoted version, 
            @TypeInfo("ceylon.language.Nothing|ceylon.language.String") @Name("doc") 
              ceylon.language.String doc, 
            @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.String>") @Name("authors") 
              ceylon.language.Iterable<? extends ceylon.language.String> authors, 
            @TypeInfo("ceylon.language.Nothing|ceylon.language.Quoted") @Name("license") 
              ceylon.language.Quoted license,
            @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.descriptor.Import>") @Name("dependencies") @Sequenced 
              ceylon.language.Iterable<? extends ceylon.language.descriptor.Import> dependencies){
        this.name = name;
        this.version = version;
        this.doc = doc;
        this.authors = authors;
        this.license = license;
        this.dependencies = dependencies;
    }

    public ceylon.language.Quoted getName() {
        return name;
    }

    public ceylon.language.Quoted getVersion() {
        return version;
    }

    public ceylon.language.String getDoc() {
        return doc;
    }

    public Iterable<? extends ceylon.language.String> getAuthors() {
        return authors;
    }

    public ceylon.language.Quoted getLicense() {
        return license;
    }

    public Iterable<? extends ceylon.language.descriptor.Import> getDependencies() {
        return dependencies;
    }
}
