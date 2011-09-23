package ceylon.language.descriptor;

import ceylon.language.Iterable;

import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

public class Module {
    private ceylon.language.Quoted name;
    private ceylon.language.Quoted version;
    private String doc;
    private Iterable<? extends String> authors;
    private ceylon.language.Quoted license;

    public Module(@Name("name") ceylon.language.Quoted name, 
            @Name("version") ceylon.language.Quoted version, 
            @TypeInfo("ceylon.language.Nothing|ceylon.language.String") @Name("doc") 
              java.lang.String doc, 
            @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.String>") @Name("authors") 
              ceylon.language.Iterable<? extends java.lang.String> authors, 
            @TypeInfo("ceylon.language.Nothing|ceylon.language.Quoted") @Name("license") 
    ceylon.language.Quoted license){
        this.name = name;
        this.version = version;
        this.doc = doc;
        this.authors = authors;
        this.license = license;
    }

    public ceylon.language.Quoted getName() {
        return name;
    }

    public ceylon.language.Quoted getVersion() {
        return version;
    }

    public String getDoc() {
        return doc;
    }

    public Iterable<? extends String> getAuthors() {
        return authors;
    }

    public ceylon.language.Quoted getLicense() {
        return license;
    }
}
