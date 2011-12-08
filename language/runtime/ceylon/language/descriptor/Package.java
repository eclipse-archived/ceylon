package ceylon.language.descriptor;

import ceylon.language.Iterable;

import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

public class Package {
    private ceylon.language.Quoted name;
    private boolean shared;
    private java.lang.String doc;
    private Iterable<? extends ceylon.language.String> by;

    public Package(@Name("name") ceylon.language.Quoted name, 
    		@Name("shared") boolean shared,
    		@Name("doc") java.lang.String doc,
            @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.String>") 
            @Name("by") ceylon.language.Iterable<? extends ceylon.language.String> by){
        this.name = name;
        this.shared = shared;
        this.doc = doc;
        this.by = by;
    }

    public ceylon.language.Quoted getName() {
        return name;
    }
    
    public boolean getShared() {
		return shared;
	}

    public java.lang.String getDoc() {
        return doc;
    }
    
    public Iterable<? extends ceylon.language.String> getBy() {
		return by;
	}

}
