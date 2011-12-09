package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Class;
import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;

@Ceylon
@Class(extendsType="ceylon.language.Object")
@SatisfiedTypes("ceylon.language.Equality")
public class Quoted {
	
	final java.lang.String value;
	
    /*public Quoted(@Name("characters")
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Character>") 
    Iterable<? extends ceylon.language.Character> characters){
    }*/
    
    private Quoted(java.lang.String value) {
    	this.value = value;
    }

    public static Quoted instance(java.lang.String value){
        return new Quoted(value);
    }
    
    @Override
    public java.lang.String toString() {
        return value;
    }
}
