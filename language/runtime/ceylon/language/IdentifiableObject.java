package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Class;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@SatisfiedTypes("ceylon.language.Equality")
@Class(extendsType="ceylon.language.Object")
public abstract class IdentifiableObject extends Object  {

    public boolean equals(@Name("that") @TypeInfo("ceylon.language.Equality") java.lang.Object that) {
        //TODO: this is broken:   
        if (that instanceof IdentifiableObject ) {
            return this == that;
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    @Override
    public java.lang.String toString() {
        return super.toString();
    }
}