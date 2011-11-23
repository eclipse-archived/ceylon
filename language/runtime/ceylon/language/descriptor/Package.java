package ceylon.language.descriptor;

import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

public class Package {
    private ceylon.language.Quoted name;
    private boolean shared;
    private ceylon.language.String doc;

    public Package(@Name("name") ceylon.language.Quoted name, 
    		@Name("shared") boolean shared,
            @TypeInfo("ceylon.language.Nothing|ceylon.language.String") @Name("doc")
            ceylon.language.String doc){
        this.name = name;
        this.shared = shared;
        this.doc = doc;
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

}
