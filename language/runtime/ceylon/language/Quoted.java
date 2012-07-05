package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;

@Ceylon(major = 2)
@Class(extendsType="ceylon.language.Object")
public class Quoted {
	
	final java.lang.String value;
	
    /*public Quoted(@Name("characters")
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Character>") 
    Iterable<? extends ceylon.language.Character> characters){
    }*/
    
    private Quoted(java.lang.String value) {
    	this.value = value;
    }

    @Ignore
    public static Quoted instance(java.lang.String value) {
        return new Quoted(value);
    }
    
    @Override
    public java.lang.String toString() {
        return value;
    }
}
