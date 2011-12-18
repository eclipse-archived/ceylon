package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Class;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.Sequenced;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@Ceylon
@Class(extendsType="ceylon.language.IdentifiableObject")
public class StringBuilder {
    
    final java.lang.StringBuilder builder = new java.lang.StringBuilder();
    
    @Override
    public final synchronized java.lang.String toString() {
        return builder.toString();
    }
    
    public final synchronized void append(@Name("string") java.lang.String string) {
        builder.append(string);
    }
    
    public final synchronized void appendAll(@Sequenced @Name("strings") 
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.String>")
    Iterable<? extends String> strings) {
        for (Iterator<? extends String> iter=strings.getIterator(); iter!=null; iter=iter.getTail()) {
            builder.append(iter.getHead());
        }
    }
    
    public final synchronized void appendCharacter(@Name("character") 
    @TypeInfo("ceylon.language.Character") int character) {
        builder.append(java.lang.Character.toChars(character));
    }
    
    public void appendNewline() {
    	builder.append('\n');
    }
    
}
