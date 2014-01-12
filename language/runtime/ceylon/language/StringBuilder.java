package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 6)
@Class
public class StringBuilder implements ReifiedType {
    
    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(StringBuilder.class);

    final java.lang.StringBuilder builder = new java.lang.StringBuilder();
    
    public StringBuilder() {}
    
    @Override
    public final java.lang.String toString() {
        return builder.toString();
    }

    @TypeInfo("ceylon.language::StringBuilder")
    public final StringBuilder append(@Name("string") java.lang.String string) {
        builder.append(string);
        return this;
    }
    
    @TypeInfo("ceylon.language::StringBuilder")
    public final StringBuilder appendAll(@Name("strings") 
    @TypeInfo("ceylon.language::Iterable<ceylon.language::String>")
    Iterable<? extends String,?> strings) {
        java.lang.Object elem;
        for (Iterator<? extends String> iter=strings.iterator(); 
                !((elem = iter.next()) instanceof Finished);) {
            builder.append(elem);
        }
        return this;
    }
    
    @TypeInfo("ceylon.language::StringBuilder")
    public final StringBuilder appendCharacter(@Name("character") 
    @TypeInfo("ceylon.language::Character") int character) {
        builder.append(java.lang.Character.toChars(character));
        return this;
    }
    
    @TypeInfo("ceylon.language::StringBuilder")
    public final StringBuilder appendNewline() {
    	builder.append('\n');
    	return this;
    }
    
    @TypeInfo("ceylon.language::StringBuilder")
    public final StringBuilder appendSpace() {
    	builder.append(' ');
    	return this;
    }

    @TypeInfo("ceylon.language::StringBuilder")
    public final StringBuilder reset() {
        builder.setLength(0);
        return this;
    }

    @TypeInfo("ceylon.language::StringBuilder")
    public final StringBuilder insert(
            @Name("index") @TypeInfo("ceylon.language::Integer") int index,
            @Name("string") @TypeInfo(value="ceylon.language::String")
            java.lang.String string) {
        if (index < 0) index = 0;
        if (index >= builder.length()) {
            builder.append(string);
        } else {
            builder.insert(index, string);
        }
        return this;
    }

    @TypeInfo("ceylon.language::StringBuilder")
    public final StringBuilder insertCharacter(
            @Name("index") @TypeInfo("ceylon.language::Integer") int index,
            @Name("character") @TypeInfo(value="ceylon.language::Character")
            int character) {
        if (index < 0) index = 0;
        char[] chars = java.lang.Character.toChars(character);
        if (index >= builder.length()) {
            builder.append(chars);
        } else {
            builder.insert(index, chars);
        }
        return this;
    }

    @TypeInfo("ceylon.language::StringBuilder")
    public final StringBuilder deleteInitial(
    		@Name("length") @TypeInfo("ceylon.language::Integer") int length) {
    	return delete(0, length);
    }
    
    @TypeInfo("ceylon.language::StringBuilder")
    public final StringBuilder deleteTerminal(
    		@Name("length") @TypeInfo("ceylon.language::Integer") int length) {
    	return delete(builder.length()-length, length);
    }
    
    @TypeInfo("ceylon.language::StringBuilder")
    public final StringBuilder delete(
            @Name("index") @TypeInfo("ceylon.language::Integer") int index,
            @Defaulted @Name("length") @TypeInfo("ceylon.language::Integer") int length) {
        if (index < 0) {
            index = 0;
        } else if (index > builder.length()) {
            return this;
        }
        if (length == 1) {
            builder.deleteCharAt(index);
        } else if (length > 0) {
            builder.delete(index, index+length);
        }
        return this;
    }
    
    @Ignore
    public final StringBuilder delete(int index) {
        return delete(index, 1);
    }
    
    @Ignore
    public final int delete$length(int index) {
    	return 1;
    }

    @TypeInfo("ceylon.language::Integer")
    public int getSize() {
        return builder.length();
    }

    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
}
