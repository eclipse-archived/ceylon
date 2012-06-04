package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
@Class(extendsType="ceylon.language.IdentifiableObject")
public class StringBuilder {
    
    final java.lang.StringBuilder builder = new java.lang.StringBuilder();
    
    @Override
    public final synchronized java.lang.String toString() {
        return builder.toString();
    }
    
    public final synchronized StringBuilder append(@Name("string") java.lang.String string) {
        builder.append(string);
        return this;
    }
    
    public final synchronized StringBuilder appendAll(@Sequenced @Name("strings") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.String>")
    Iterable<? extends String> strings) {
        java.lang.Object elem;
        for (Iterator<? extends String> iter=strings.getIterator(); !((elem = iter.next()) instanceof Finished);) {
            builder.append(elem);
        }
        return this;
    }
    @Ignore
    public final synchronized StringBuilder appendAll() {
        return appendAll($empty.getEmpty());
    }
    
    public final synchronized StringBuilder appendCharacter(@Name("character") 
    @TypeInfo("ceylon.language.Character") int character) {
        builder.append(java.lang.Character.toChars(character));
        return this;
    }
    
    public StringBuilder appendNewline() {
    	builder.append('\n');
    	return this;
    }
    
    public StringBuilder appendSpace() {
    	builder.append(' ');
    	return this;
    }
    
}
