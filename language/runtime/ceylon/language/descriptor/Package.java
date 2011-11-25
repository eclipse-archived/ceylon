package ceylon.language.descriptor;

import ceylon.language.Iterable;

import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;
import com.redhat.ceylon.compiler.metadata.java.Name;

public class Package {
    private ceylon.language.Quoted name;
    private boolean shared;
    private ceylon.language.String doc;
    private Iterable<? extends ceylon.language.String> authors;

    public Package(@Name("name") ceylon.language.Quoted name, 
    		@Name("shared") boolean shared,
            @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.String>") @Name("by") 
            ceylon.language.Iterable<? extends ceylon.language.String> authors, 
            @TypeInfo("ceylon.language.Nothing|ceylon.language.String") @Name("doc")
            ceylon.language.String doc){
        this.name = name;
        this.shared = shared;
        this.doc = doc;
        this.authors = authors;
    }

    public ceylon.language.Quoted getName() {
        return name;
    }
    
    public boolean getShared() {
		return shared;
	}

    public ceylon.language.String getDoc() {
        return doc;
    }
    
    public Iterable<? extends ceylon.language.String> getAuthors() {
		return authors;
	}

}
