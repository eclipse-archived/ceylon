package ceylon.language;

import ceylon.language.impl.BaseCharacterList;
import ceylon.language.impl.BaseIterator;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 7)
@Class(extendsType="ceylon.language::Basic")
@SatisfiedTypes("ceylon.language::List<ceylon.language::Character>")
public final class StringBuilder extends BaseCharacterList {
    
    private java.lang.StringBuilder builder = 
            new java.lang.StringBuilder();
    
    public StringBuilder() {}
    
    public long getSize() {
        return builder.codePointCount(0, 
                builder.length());
    }
    
    @Override
    public java.lang.String toString() {
        return builder.toString();
    }
    
    public StringBuilder clear() {
        builder.setLength(0);
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
    
    public StringBuilder appendCharacter(
            @Name("character")
            @TypeInfo("ceylon.language::Character")
            int character) {
        builder.appendCodePoint(character);
        return this;
    }
    
    public StringBuilder append(
            @Name("string")
            java.lang.String string) {
        builder.append(string);
        return this;
    }
    
    public StringBuilder appendAll(
            @Name("strings")
            @TypeInfo("ceylon.language::Iterable<ceylon.language::String,ceylon.language::Null>")
            Iterable<? extends java.lang.String, ?> strings) {
        Iterator<? extends java.lang.String> it = strings.iterator();
        java.lang.Object elem;
        while (!((elem = it.next()) instanceof Finished)) {
            builder.append(elem);
        }
        return this;
    }
    
    public StringBuilder prependCharacter(
            @Name("character")
            @TypeInfo("ceylon.language::Character")
            int character) {
        builder.insert(0,
                java.lang.Character.toChars(character));
        return this;
    }
    
    public StringBuilder prepend(
            @Name("string")
            java.lang.String string) {
        builder.insert(0, string);
        return this;
    }
    
    public StringBuilder prependAll(
            @Name("strings")
            @TypeInfo("ceylon.language::Iterable<ceylon.language::String,ceylon.language::Null>")
            Iterable<? extends java.lang.String, ?> strings) {
        Iterator<? extends java.lang.String> it = strings.iterator();
        java.lang.Object elem;
        while (!((elem = it.next()) instanceof Finished)) {
            builder.insert(0, elem);
        }
        return this;
    }
    
    public StringBuilder insert(
            @Name("index")
            long index,
            @Name("string")
            java.lang.String string) {
        if (index<0) {
            throw new AssertionError("index must not be negative");
        }
        else if (index>getSize()) {
            throw new AssertionError("index must not be greater than size");
        }
        else {
            builder.insert(toBuilderIndex(index),
                    string);
        }
        return this;
    }
    
    public StringBuilder insertCharacter(
            @Name("index")
            long index,
            @Name("character")
            @TypeInfo("ceylon.language::Character")
            int character) {
        if (index<0) {
            throw new AssertionError("index must not be negative");
        }
        else if (index>getSize()) {
            throw new AssertionError("index must not be greater than size");
        }
        else {
            builder.insert(toBuilderIndex(index),
                    java.lang.Character.toChars(character));
        }
        return this;
    }
    
    public StringBuilder replace(
            @Name("index")
            long index,
            @Name("length")
            long length,
            @Name("string")
            java.lang.String string) {
        if (index<0) {
            throw new AssertionError("index must not be negative");
        }
        else if (index>getSize()) {
            throw new AssertionError("index must not be greater than size");
        }
        else if (index+length>getSize()) {
            throw new AssertionError("index+length must not be greater than size");
        }
        else if (!string.isEmpty()) {
            if (length<0) length = 0;
            int start = toBuilderIndex(index);
            int end = builder.offsetByCodePoints(start, 
                    Util.toInt(length));
            builder.replace(start, end, string);
        }
        return this;
    }
    
    public StringBuilder delete(
            @Name("index")
            long index,
            @Name("length")
            long length) {
        if (index<0) {
            throw new AssertionError("index must not be negative");
        }
        else if (index>getSize()) {
            throw new AssertionError("index must not be greater than size");
        }
        else if (index+length>getSize()) {
            throw new AssertionError("index+length must not be greater than size");
        }
        else if (length>0) {
            int start = toBuilderIndex(index);
            int end = builder.offsetByCodePoints(start, 
                    Util.toInt(length));
            builder.delete(start, end);
        }
        return this;
    }
    
    public StringBuilder deleteInitial(
            @Name("length")
            long length) {
        if (length>getSize()) {
            throw new AssertionError("length must not be greater than size");
        }
        else if (length>0) {
            builder.delete(0,
                    toBuilderIndex(length));
        }
        return this;
    }
    
    public StringBuilder deleteTerminal(
            @Name("length")
            long length) {
        if (length>getSize()) {
            throw new AssertionError("length must not be greater than size");
        }
        else if (length>0) {
            int start = toBuilderIndex(getSize() - length);
            builder.delete(start, builder.length());
        }
        return this;
    }
    
    public java.lang.String substring(
            @Name("index")
            long index,
            @Name("length")
            long length) {
        if (index<0) {
            throw new AssertionError("index must not be negative");
        }
        else if (index>getSize()) {
            throw new AssertionError("index must not be greater than size");
        }
        else if (index+length>getSize()) {
            throw new AssertionError("index+length must not be greater than size");
        }
        else if (length>0) {
            int start = toBuilderIndex(index);
            int end = builder.offsetByCodePoints(start, 
                    Util.toInt(length));
            return builder.substring(start, end);
        }
        else {
            return "";
        }
    }
    
    private int toBuilderIndex(long index) {
        return builder.offsetByCodePoints(0,
                Util.toInt(index));
    }
    
    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = 
            TypeDescriptor.klass(StringBuilder.class);
    
    @Override @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language::Character")
    public Character getFromFirst(@Name("index") long index) {
        if (index<0 || index>getSize()) {
            return null;
        }
        else {
            return Character.instance(builder.codePointAt(toBuilderIndex(index)));
        }
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language::Integer")
    public Integer getLastIndex() {
        if (builder.length()==0) {
            return null;
        }
        else {
            return Integer.instance(getSize()-1);
        }
    }
    
    public StringBuilder reverseInPlace() {
        builder.reverse();
        return this;
    }
    
    @Override
    public Iterator<? extends Character> iterator() {
        return new StringIterator(builder);
    }
    
    @Ignore
    private static class StringIterator 
            extends BaseIterator<Character> 
            implements ReifiedType {
        
        private static final TypeDescriptor $TypeDescriptor$ = 
                TypeDescriptor.klass(StringIterator.class);
        
        final java.lang.StringBuilder builder;
        
        public StringIterator(final java.lang.StringBuilder builder) {
            super(Character.$TypeDescriptor$);
            this.builder = builder;
        }
        
        private int offset = 0;
        
        @Override
        public java.lang.Object next() {
            if (offset < builder.length()) {
                int codePoint = builder.codePointAt(offset);
                offset += java.lang.Character.charCount(codePoint);
                return Character.instance(codePoint);
            }
            else {
                return finished_.get_();
            }
        }
        
        @Override
        @Ignore
        public TypeDescriptor $getType$() {
            return $TypeDescriptor$;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        return super.equals(obj);
    }
    
}
