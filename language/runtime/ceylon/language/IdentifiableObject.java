package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 1)
@Class(extendsType="ceylon.language.Object")
@SatisfiedTypes("ceylon.language.Identifiable")
public abstract class IdentifiableObject implements Identifiable {

    @Override
    public boolean equals(@Name("that") @TypeInfo("ceylon.language.Object") 
    java.lang.Object that) {
        return Identifiable$impl._equals(this, that);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
}