package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Class;
import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@Class(extendsType="ceylon.language.Object")
@SatisfiedTypes("ceylon.language.Equality")
public class Quoted {
    public Quoted(@TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.Character>") ceylon.language.Iterable<? extends ceylon.language.Character> characters){
    }
    
    private Quoted(java.lang.String value) {
    }

    public static Quoted instance(java.lang.String value){
        return new Quoted(value);
    }
}
