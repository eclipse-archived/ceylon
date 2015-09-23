package ceylon.language;

import java.util.Locale;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.language.AbstractCallable;
import com.redhat.ceylon.compiler.java.language.StringTokens;
import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.FunctionalParameter;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.Transient;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.ValueType;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

import ceylon.language.impl.BaseIterator;

@Ceylon(major = 8)
@Class(extendsType="ceylon.language::Object", 
       basic = false, identifiable = false)
@SatisfiedTypes({"ceylon.language::SearchableList<ceylon.language::Character>",
                 "ceylon.language::Comparable<ceylon.language::String>",
                 "ceylon.language::Summable<ceylon.language::String>",
                 "ceylon.language::Ranged<ceylon.language::Integer,ceylon.language::Character,ceylon.language::String>"})
@ValueType
@Annotations({
    @Annotation(
            value = "doc",
            arguments = {"A string of characters..."}),
    @Annotation(
            value = "by",
            arguments = {"Gavin"}),
    @Annotation("shared"),
    @Annotation("final")})
public final class String
    implements Comparable<String>, SearchableList<Character>,
               Summable<String>, ReifiedType {
    
    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = 
            TypeDescriptor.klass(String.class);
    
    @Ignore
    public final java.lang.String value;
    
    @Ignore @Override
    public Comparable$impl<String> $ceylon$language$Comparable$impl() {
        return new Comparable$impl<String>(String.$TypeDescriptor$, this);
    }

    @SuppressWarnings("rawtypes")
    public String(@Name("characters")
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Character,ceylon.language::Null>")
    final Iterable<? extends Character, ?> characters) {
        if (characters instanceof String) {
            value = ((String)characters).value;
        } else {
            java.lang.String s = null;
            if (characters instanceof Array.ArrayIterable) {
                s = ((Array.ArrayIterable) characters).stringValue();
            }
            if (s != null) {
                value = s;
            } else {
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                java.lang.Object $tmp;
                for (Iterator<? extends Character> $val$iter$0 = characters.iterator(); 
                        !(($tmp = $val$iter$0.next()) instanceof Finished);) {
                    sb.appendCodePoint(((Character)$tmp).codePoint);
                }
                value = sb.toString();
            }
        }
    }

    @Ignore
    public String(final java.lang.String string) {
        value = string;
    }

    @Override
    @Transient
    public java.lang.String toString() {
        return value;
    }

    @Ignore
    public static java.lang.String toString(java.lang.String value) {
        return value;
    }

    @Ignore
    public static ceylon.language.String instance(java.lang.String s) {
        if (s==null) return null;
        return new String(s);
    }

    @Ignore
    public static ceylon.language.String instanceJoining(java.lang.String... strings) {
        StringBuffer buf = new StringBuffer();
        for (java.lang.String s: strings)
            buf.append(s);
        return instance(buf.toString());
    }

    @Ignore
    public static ceylon.language.String instanceJoining(String... strings) {
        StringBuffer buf = new StringBuffer();
        for (String s: strings)
            buf.append(s.value);
        return instance(buf.toString());
    }

    public java.lang.String getUppercased() {
        return getUppercased(value);
    }

    @Ignore
    public static java.lang.String getUppercased(java.lang.String value) {
        return value.toUpperCase(Locale.ROOT);
    }

    public java.lang.String getLowercased() {
        return getLowercased(value);
    }

    @Ignore
    public static java.lang.String getLowercased(java.lang.String value) {
        return value.replace("\u0130", "i\u0307") //workaround for bug
                    .toLowerCase(Locale.ROOT);
    }

    @Override
    public boolean equals(@Name("that") java.lang.Object that) {
        if (that instanceof String) {
            String s = (String) that;
            return value.equals(s.value);
        }
        else {
            return false;
        }
    }

    @Ignore
    public static boolean equals(java.lang.String value, 
            java.lang.Object that) {
        if (that instanceof String) {
            String s = (String) that;
            return value.equals(s.value);
        }
        else {
            return false;
        }
    }

    public boolean equalsIgnoringCase(
            @Name("that") java.lang.String that) {
        return value.equalsIgnoreCase(that);
    }

    @Ignore
    public static boolean equalsIgnoringCase(
            java.lang.String value, 
            java.lang.String that) {
        return value.equalsIgnoreCase(that);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Ignore
    public static int hashCode(java.lang.String value) {
        return value.hashCode();
    }

    private static Comparison comparison(int c) {
        return c < 0 ? smaller_.get_() :
            (c == 0 ? equal_.get_() : larger_.get_());
    }

    @Override
    public Comparison compare(@Name("other") String other) {
        return comparison(value.compareTo(other.value));
    }

    @Ignore
    public static Comparison compare(java.lang.String value, 
            java.lang.String otherValue) {
        return comparison(value.compareTo(otherValue));
    }
    
    public Comparison compareIgnoringCase(
            @Name("other") java.lang.String other) {
        return comparison(value.compareToIgnoreCase(other));
    }

    @Ignore
    public static Comparison compareIgnoringCase(
            java.lang.String value, 
            java.lang.String otherValue) {
        return comparison(value.compareToIgnoreCase(otherValue));
    }

    @Override
    public String plus(@Name("other") String other) {
        return instance(value + other.value);
    }

    @Ignore
    public static java.lang.String plus(java.lang.String value, 
            java.lang.String otherValue) {
        return value + otherValue;
    }

    @Override
    @TypeInfo("ceylon.language::Integer")
    public long getSize() {
        //TODO: should we cache this value in an instvar?
        // But remember that we'll mostly be using the static verion
        // of this method! So an instvar won't help much.
        return value.codePointCount(0, value.length());
    }

    @Ignore
    public static long getSize(java.lang.String value) {
        return value.codePointCount(0, value.length());
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language::Integer")
    @Transient
    public Integer getLastIndex() {
        long length = getSize();
        return (length == 0) ? null : Integer.instance(length - 1);
    }

    @Ignore
    public static Integer getLastIndex(java.lang.String value) {
        long length = getSize(value);
        return (length == 0) ? null : Integer.instance(length - 1);
    }

    @Override
    public boolean getEmpty() {
        return value.isEmpty();
    }

    @Ignore
    public static boolean getEmpty(java.lang.String value) {
        return value.isEmpty();
    }

//    @Override
//    @TypeInfo("ceylon.language::Null|ceylon.language::Character")
//    public Character get(@Name("index") Integer key) {
//        return getFromFirst(value, key.longValue());
//    }

    @Ignore
    public static Character get(java.lang.String value, long key) {
        return getFromFirst(value, key);
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language::Character")
    public Character getFromLast(@Name("index") long key) {
        return getFromLast(value, key);
    }

    @Ignore
    public static Character getFromLast(java.lang.String value, long key) {
        int index = Util.toInt(key);
        int codePoint;
        try {
            int offset = value.offsetByCodePoints(value.length(), -index-1);
            codePoint = value.codePointAt(offset);
        }
        catch (IndexOutOfBoundsException e) {
            return null;
        }
        return Character.instance(codePoint);
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language::Character")
    public Character getFromFirst(@Name("index") long key) {
        return getFromFirst(value, key);
    }

    @Ignore
    public static Character getFromFirst(java.lang.String value, long key) {
        int index = Util.toInt(key);
        int codePoint;
        try {
            int offset = value.offsetByCodePoints(0, index);
            codePoint = value.codePointAt(offset);
        }
        catch (IndexOutOfBoundsException e) {
            return null;
        }
        return Character.instance(codePoint);
    }

    @Override
    public boolean defines(@Name("index") Integer key) {
        long index = key.longValue();
        return index >= 0 && index < getSize();
    }

    @Ignore
    public static boolean defines(java.lang.String value, long key) {
        return key >= 0 && key < getSize(value);
    }
    
    /*@Override
    @TypeInfo("ceylon.language::Entry<ceylon.language::Boolean,ceylon.language::Null|ceylon.language::Character>")
    public Entry<? extends Boolean, ? extends Character> lookup(@Name("index") Integer index) {
        return lookup(value, index.value);
    }

    @Ignore
    public static Entry<? extends Boolean,? extends Character> 
    lookup(java.lang.String value, long index) {
        Character item = getFromFirst(value, index);
        return new Entry<Boolean,Character>(
                Boolean.$TypeDescriptor$,
                Character.$TypeDescriptor$,
                Boolean.instance(item!=null), 
                item);
    }*/

    @Override
    @Transient
    public Sequential<? extends ceylon.language.Integer> getKeys() {
        return getKeys(value);
    }

    @Ignore
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Sequential<? extends ceylon.language.Integer> 
    getKeys(java.lang.String value) {
        long size = value.length();
        if (size==0) {
            return (Sequential) empty_.get_();
        }
        else {
            return new Span<Integer>(Integer.$TypeDescriptor$, 
                    Integer.instance(0), Integer.instance(size-1));
        }
    }
    
    @Ignore
    public static java.lang.Object indexes(java.lang.String value) {
        return getKeys(value);
    }

    @Ignore
    public static boolean definesEvery(java.lang.String value, 
            Iterable<? extends Integer,?> keys) {
        //TODO: inefficient ... better to cache the result
        //      of getSize()
        // TODO We're still boxing here!
        return instance(value).definesEvery(keys);
    }

    @Ignore
    public static boolean definesEvery(java.lang.String value) {
        return true;
    }

    @Ignore
    public static boolean definesAny(java.lang.String value, 
            Iterable<? extends Integer, ?> keys) {
        //TODO: inefficient ... better to cache the result
        //      of getSize()
        // TODO We're still boxing here!
        return instance(value).definesAny(keys);
    }

    @Ignore
    public static boolean definesAny(java.lang.String value) {
        return false;
    }
    
    @Ignore
    public static <Absent extends Null> 
    Iterable<? extends Character, ? extends Absent>
    getAll(TypeDescriptor $reifiedAbsent, java.lang.String value, 
            Iterable<? extends Integer, Absent> keys) {
        // TODO We're still boxing here!
        return instance(value).getAll($reifiedAbsent,keys);
    }
    
    @Ignore
    public static boolean occurs(java.lang.String value,
            int element) {
        return occurs(value, element, 0);
    }
    
    @Ignore
    public static boolean occurs(java.lang.String value,
            int element, long from) {
        return occurs(value, element, from, 
                java.lang.Integer.MAX_VALUE);
    }
    
    @Ignore
    public static boolean occurs(java.lang.String value, 
            int element, long from, long length) {
        if (from>=value.length() || length<=0) {
            return false;
        }
        if (from<0) {
            length+=from;
            from = 0;
        }
        int start;
        try {
            start = value.offsetByCodePoints(0, (int)from);
        }
        catch (IndexOutOfBoundsException e) {
            return false;
        }
        int index = value.indexOf(element, start);
        return index>=0 && index<length+from;
    }
    
    @Override
    public boolean occurs(@Name("element") 
            Character element,
            @Defaulted @Name("from") long from,
            @Defaulted @Name("length") long length) {
        return occurs(value, element.codePoint, from, length);
    }
    
    @Ignore
    public static boolean occursAt(java.lang.String value, 
            long index, int element) {
        if (index<0 || index>=value.length()) {
            return false;
        }
        try {
            int offset;
            try {
                offset = value.offsetByCodePoints(0, (int)index);
            }
            catch (IndexOutOfBoundsException e) {
                return false;
            }
            return element == value.codePointAt(offset);
        }
        catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
    
    @Override
    public boolean occursAt(
            @Name("index") long index, 
            @Name("element") Character element) {
        return occursAt(value, index, element.codePoint);
    }
    
    @Ignore
    public static Iterable<? extends Integer, ?> 
    occurrences(java.lang.String value, int element) {
        return instance(value).occurrences(new Character(element));
    }

    @Ignore
    public static Iterable<? extends Integer, ?> 
    occurrences(java.lang.String value, int element,
            long from) {
        return instance(value).occurrences(new Character(element), from);
    }

    @Ignore
    public static long 
    countOccurrences(java.lang.String value, 
            int element) {
        return countOccurrences(value, element, 0);
    }
    
    @Ignore
    public static long 
    countOccurrences(java.lang.String value, 
            int element, long from) {
        return countOccurrences(value, element, from, 
                java.lang.Integer.MAX_VALUE);
    }
    
    @Ignore
    public static long 
    countOccurrences(java.lang.String value, 
            int element, 
            long from, long length) {
        if (from>=value.length() || length<=0) {
            return 0;
        }
        if (from<0) {
            length+=from;
            from = 0;
        }
        int i;
        try {
            i = value.offsetByCodePoints(0, (int)from);
        }
        catch (IndexOutOfBoundsException e) {
            return 0;
        }
        int count = 0;
        while (true) {
            i = value.indexOf(element, i);
            if (i<0 || i>=from+length) {
                return count;
            }
            else {
                count++;
                i++;
            }
        }
    }
    
    @Override
    public long countOccurrences(
            @Name("sublist")
            Character element,
            @Defaulted @Name("from") long from,
            @Defaulted @Name("length") long length) {
        return countOccurrences(value, element.codePoint, from, length);
    }

    @Ignore
    public static boolean includes(java.lang.String value, 
            List<? extends Character> sublist) {
        return includes(value, sublist, 0);
    }

    @Ignore
    public static boolean includes(java.lang.String value, 
            List<? extends Character> sublist, long from) {
        if (from>value.length()) {
            return false;
        }
        if (from<0) {
            from = 0;
        }
        if (sublist instanceof String) {
            String string = (String) sublist;
            int offset;
            try {
                offset = value.offsetByCodePoints(0, (int)from);
            }
            catch (IndexOutOfBoundsException e) {
                return value.isEmpty();
            }
            int index = value.indexOf(string.value, offset);
            return index >= 0;
        }
        else {
            return instance(value).includes(sublist, from);
        }
    }

    @Override
    public boolean includes(
            @Name("sublist") List<? extends Character> sublist,
            @Defaulted @Name("from") long from) {
        if (sublist instanceof String) {
            return includes(value, sublist, from);
        }
        else {
            return $ceylon$language$SearchableList$impl().includes(sublist, from);
        }
    }
    
    @Ignore
    public static boolean includesAt(java.lang.String value, 
            long index, List<? extends Character> sublist) {
        if (index<0 || index>value.length()) {
            return false;
        }
        if (sublist instanceof String) {
            String string = (String) sublist;
            java.lang.String str = string.value;
            try {
                int offset;
                try {
                    offset = value.offsetByCodePoints(0, (int)index);
                }
                catch (IndexOutOfBoundsException e) {
                    return sublist.getEmpty();
                }
                return value.regionMatches(offset, str, 0, str.length());
            }
            catch (IndexOutOfBoundsException e) {
                return false;
            }
        }
        else {
            return instance(value).includesAt(index, sublist);
        }
    }
    
    @Override
    public boolean includesAt(@Name("index") long index, 
            @Name("sublist") List<? extends Character> sublist) {
        if (sublist instanceof String) {
            return includesAt(value, index, sublist);
        }
        else {
            return $ceylon$language$SearchableList$impl().includesAt(index, sublist);
        }
    }
    
    @Ignore
    public static Iterable<? extends Integer, ?> 
    inclusions(java.lang.String value, 
            List<? extends Character> substring) {
        return instance(value).inclusions(substring);
    }
    
    @Ignore
    public static Iterable<? extends Integer, ?> 
    inclusions(java.lang.String value, 
            List<? extends Character> substring,
            long from) {
        return instance(value).inclusions(substring, from);
    }
    
    @Ignore
    public static long 
    countInclusions(java.lang.String value, 
            List<? extends Character> sublist) {
        return countInclusions(value, sublist, 0);
    }
    
    @Ignore
    public static long 
    countInclusions(java.lang.String value, 
            List<? extends Character> sublist,
            long from) {
        if (from>value.length()) {
            return 0;
        }
        if (from<0) {
            from = 0;
        }
        if (sublist instanceof String) {
            String string = (String) sublist;
            java.lang.String str = string.value;
            int i;
            try {
                i = value.offsetByCodePoints(0, (int)from);
            }
            catch (IndexOutOfBoundsException e) {
                return 0;
            }
            int count = 0;
            while (true) {
                i = value.indexOf(str, i);
                if (i<0) {
                    return count;
                }
                else {
                    i++;
                    count++;
                }
            }
        }
        else {
            return instance(value).countInclusions(sublist, from);
        }
    }
    
    @Override
    public long countInclusions(
            @Name("sublist") List<? extends Character> sublist,
            @Defaulted @Name("from") long from) {
        if (sublist instanceof String) {
            return countInclusions(value, sublist, from);
        }
        else {
            return $ceylon$language$SearchableList$impl().countInclusions(sublist, from);
        }
    }
    
    @Ignore
    public static Integer firstInclusion(java.lang.String value, 
            List<? extends Character> sublist) {
        return firstInclusion(value, sublist, 0);
    }

    @Ignore
    public static Integer firstInclusion(java.lang.String value, 
            List<? extends Character> sublist,
            long from) {
        if (from>value.length()) {
            return null;
        }
        if (from<0) {
            from = 0;
        }
        if (sublist instanceof String) {
            String string = (String) sublist;
            int start = value.offsetByCodePoints(0, (int)from);
            int index = value.indexOf(string.value, start);
            if (index >= 0) {
                return Integer.instance(value.codePointCount(0, index));
            } else {
                return null;
            }
        }
        else {
            return instance(value).firstInclusion(sublist, from);
        }
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language::Integer")
    public Integer firstInclusion(
            @Name("sublist") List<? extends Character> sublist,
            @Defaulted @Name("from") long from) {
        if (sublist instanceof String) {
            return firstInclusion(value, sublist, from);
        }
        else {
            return $ceylon$language$SearchableList$impl().firstInclusion(sublist, from);
        }
    }
    
    public static Integer lastInclusion(java.lang.String value, 
    		List<? extends Character> sublist) {
        return lastInclusion(value, sublist, 0);
    }
    
    public static Integer lastInclusion(java.lang.String value, 
            List<? extends Character> sublist, long from) {
        if (from>value.length()) {
            return null;
        }
        if (from<0) {
            from = 0;
        }
        if (sublist instanceof String) {
            String string = (String) sublist;
            int start;
            try {
                start = 
                        value.offsetByCodePoints(value.length(), 
                                -(int)from 
                                - Util.toInt(sublist.getSize()));
            }
            catch (java.lang.IndexOutOfBoundsException e) {
                return null;
            }
            int index = value.lastIndexOf(string.value, start);
            if (index >= 0) {
                int result = value.codePointCount(0, index);
                return Integer.instance(result);
            } else {
                return null;
            }
        }
        else {
            return instance(value).lastInclusion(sublist, from);
        }
    }
    
    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language::Integer")
    public Integer lastInclusion(
            @Name("sublist") List<? extends Character> sublist,
            @Defaulted @Name("from") long from) {
        if (sublist instanceof String) {
            return lastInclusion(value, sublist, from);
        }
        else {
            return $ceylon$language$SearchableList$impl().lastInclusion(sublist, from);
        }
    }
    
    @Ignore
    public static Integer firstOccurrence(java.lang.String value,
            int element) {
        return firstOccurrence(value, element, 0);
    }
    
    @Ignore
    public static Integer firstOccurrence(java.lang.String value,
            int element, long from) {
        return firstOccurrence(value, element, from, 
                java.lang.Integer.MAX_VALUE);
    }
    
    @Ignore
    public static Integer firstOccurrence(java.lang.String value, 
            int element,
            long from, long length) {
        if (from>=value.length() || length<=0) {
            return null;
        }
        if (from<0) {
            length+=from;
            from = 0;
        }
        int start;
        try {
            start = value.offsetByCodePoints(0, (int)from);
        }
        catch (java.lang.IndexOutOfBoundsException e) {
            return null;
        }
        int index = value.indexOf(element, start);
        if (index >= 0) {
            int result = value.codePointCount(0, index);
            if (result>=from+length) {
                return null;
            }
            return Integer.instance(result);
        } else {
            return null;
        }
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language::Integer")
    public Integer firstOccurrence(
            @Name("element") Character element,
            @Defaulted @Name("from") long from,
            @Defaulted @Name("length") long length) {
        return firstOccurrence(value, element.codePoint, from, length);
    }
    
    @Ignore
    public static Integer lastOccurrence(java.lang.String value,
            int element) {
        return lastOccurrence(value, element, 0);
    }
    
    @Ignore
    public static Integer lastOccurrence(java.lang.String value,
            int element, long from) {
        return lastOccurrence(value, element, from, 
                java.lang.Integer.MAX_VALUE);
    }
    
    @Ignore
    public static Integer lastOccurrence(java.lang.String value, 
            int element, long from, long length) {
        if (from>=value.length() || length<=0) {
            return null;
        }
        if (from<0) {
            length+=from;
            from = 0;
        }
        int start;
        try {
            start = 
                    value.offsetByCodePoints(value.length(), 
                            -(int)from - 1);
        }
        catch (java.lang.IndexOutOfBoundsException e) {
            return null;
        }
        int index = 
                value.lastIndexOf(element, start);
        if (index >= 0) {
            int dist = value.codePointCount(index, value.length());
            if (dist>from+length) {
                return null;
            }
            int result = value.codePointCount(0, index);
            return Integer.instance(result);
        }
        else {
            return null;
        }
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language::Integer")
    public Integer lastOccurrence(
            @Name("element") Character element,
            @Defaulted @Name("from") long from,
            @Defaulted @Name("length") long length) {
        return lastOccurrence(value, element.codePoint, from, length);
    }
    
    @Override
    @TypeInfo("ceylon.language::Iterator<ceylon.language::Character>")
    public Iterator<Character> iterator() {
        return new StringIterator(value);
    }

    
    @Ignore
    public static Iterator<Character> iterator(final java.lang.String value) {
        return new StringIterator(value);
    }
    
    @Ignore
    private static class StringIterator 
            extends BaseIterator<Character> 
            implements ReifiedType {
        
        private static TypeDescriptor $TypeDescriptor$ = 
                TypeDescriptor.klass(StringIterator.class);
        
        final java.lang.String value;
        
        public StringIterator(final java.lang.String value) {
            super(Character.$TypeDescriptor$);
            this.value = value;
        }
        
        private int offset = 0;
        
        @Override
        public java.lang.Object next() {
            if (offset < value.length()) {
                int codePoint = value.codePointAt(offset);
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
    public boolean contains(@Name("element") java.lang.Object element) {
        if (element instanceof String) {
            return value.indexOf(((String)element).value) >= 0;
        }
        else if (element instanceof Character) {
            return value.indexOf(((Character)element).intValue()) >= 0;
        }
        else {
            return false;
        }
    }

    @Ignore
    public static boolean contains(java.lang.String value, 
            java.lang.Object element) {
        if (element instanceof String) {
            return value.indexOf(((String)element).value) >= 0;
        }
        else if (element instanceof Character) {
            return value.indexOf(((Character)element).intValue()) >= 0;
        }
        else {
            return false;
        }
    }
    
    @Override
    public boolean startsWith(@Name("substring") List<?> substring) {
        if (substring instanceof String) {
            return value.startsWith(((String)substring).value);
        }
        else {
            return $ceylon$language$List$impl().startsWith(substring);
        }
    }

    @Ignore
    public static boolean startsWith(java.lang.String value, 
            List<?> substring) {
        if (substring instanceof String) {
            return value.startsWith(((String)substring).value);
        }
        else {
            return instance(value).startsWith(substring);
        }
    }
    
    @Override
    public boolean endsWith(@Name("substring") List<?> substring) {
        if (substring instanceof String) {
            return value.endsWith(((String)substring).value);
        }
        else {
            return $ceylon$language$List$impl().endsWith(substring);
        }
    }

    @Ignore
    public static boolean endsWith(java.lang.String value, 
            List<?> substring) {
        if (substring instanceof String) {
            return value.endsWith(((String)substring).value);
        }
        else {
            return instance(value).endsWith(substring);
        }
    }

    @Ignore
    public static boolean containsAny(java.lang.String value, 
            Iterable<?,?> elements) {
        // TODO We're still boxing here!
        return instance(value).containsAny(elements);
    }
    
    @Ignore
    public static boolean containsEvery(java.lang.String value, 
            Iterable<?,?> elements) {
        // TODO We're still boxing here!
        return instance(value).containsEvery(elements);
    }

    public boolean longerThan(@TypeInfo("ceylon.language::Integer")
    @Name("length") long length) {
        try {
            value.offsetByCodePoints(0, Util.toInt(length+1));
            return true;
        }
        catch (IndexOutOfBoundsException iobe) {
            return false;
        }
    }

    @Ignore
    public static boolean longerThan(java.lang.String value, 
            long length) {
        try {
            value.offsetByCodePoints(0, Util.toInt(length+1));
            return true;
        }
        catch (IndexOutOfBoundsException iobe) {
            return false;
        }
    }

    public boolean shorterThan(@TypeInfo("ceylon.language::Integer")
    @Name("length") long length) {
        try {
            value.offsetByCodePoints(0, Util.toInt(length));
            return false;
        }
        catch (IndexOutOfBoundsException iobe) {
            return true;
        }
    }

    @Ignore
    public static boolean shorterThan(java.lang.String value, 
            long length) {
        try {
            value.offsetByCodePoints(0, Util.toInt(length));
            return false;
        }
        catch (IndexOutOfBoundsException iobe) {
            return true;
        }
    }

    @Transient
    public java.lang.String getTrimmed() {
        return getTrimmed(value);
    }

    @Ignore
    public static java.lang.String getTrimmed(java.lang.String value) {
        // Don't use value.trim() because that has a definition of ws that is 
        // inconsistent with ceylon.language::Character.whitespace
        return internalTrim(value, WHITESPACE);
    }
    
    @Override
    public String trimLeading(
            @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Character,ceylon.language::Character,ceylon.language::Empty>>")
            @Name("trimming")@FunctionalParameter("(element)")
            Callable<? extends Boolean> characters) {
        return instance(trimLeading(value, characters));
    }
    @Ignore
    public static java.lang.String trimLeading(java.lang.String value, 
            Callable<? extends Boolean> characters) {
        int from = 0;
        while (from < value.length()) {
            int c = java.lang.Character.codePointAt(value, from);
            if (!characters.$call$(Character.instance(c)).booleanValue()) {
                break;
            }
            from += java.lang.Character.charCount(c);
        }
        return value.substring(from);
    }
    
    @Override
    public String trimTrailing(
            @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Character,ceylon.language::Character,ceylon.language::Empty>>")
            @Name("trimming")@FunctionalParameter("(element)")
            Callable<? extends Boolean> characters) {
        return instance(trimTrailing(value, characters));
    }
    @Ignore
    public static java.lang.String trimTrailing(java.lang.String value, 
            Callable<? extends Boolean> characters) {
        int to = value.length();
        while (to > 0) {
            int c = java.lang.Character.codePointBefore(value, to);
            if (!characters.$call$(Character.instance(c)).booleanValue()) {
                break;
            }
            to -= java.lang.Character.charCount(c);
        }
        return value.substring(0, to);
    }
    
    @Override
    public String trim(
            @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Character,ceylon.language::Character,ceylon.language::Empty>>")
            @Name("trimming")@FunctionalParameter("(element)")
            Callable<? extends Boolean> characters) {
        return instance(trim(value, characters));
    }
    
    @Ignore
    public static java.lang.String trim(java.lang.String value, 
            Callable<? extends Boolean> characters) {
        return internalTrim(value, characters);
    }
    
    @Ignore
    private static java.lang.String internalTrim(java.lang.String value,
            Callable<? extends Boolean> characters) {
        int from = 0;
        while (from < value.length()) {
            int c = java.lang.Character.codePointAt(value, from);
            if (!characters.$call$(Character.instance(c)).booleanValue()) {
                break;
            }
            from += java.lang.Character.charCount(c);
        }
        int to = value.length();
        while (to > from) {
            int c = java.lang.Character.codePointBefore(value, to);
            if (!characters.$call$(Character.instance(c)).booleanValue()) {
                break;
            }
            to -= java.lang.Character.charCount(c);
        }
        return value.substring(from, to);
    }

    public java.lang.String getNormalized() {
        return getNormalized(value);
    }

    @Ignore
    public static java.lang.String getNormalized(java.lang.String value) {
        java.lang.StringBuilder result = 
                new java.lang.StringBuilder(value.length());
        boolean previousWasWhitespace=false;
        for (int i=0;i<value.length();) {
            int c = java.lang.Character.codePointAt(value, i);
            boolean isWhitespace = java.lang.Character.isWhitespace(c);
            if (!isWhitespace) {
                result.appendCodePoint(c);
            }
            else if (!previousWasWhitespace) {
                result.append(" ");
            }
            previousWasWhitespace = isWhitespace;
            i+=java.lang.Character.charCount(c);
        }
        // TODO Should be able to figure out the indices to 
        //      substring on while iterating
        return getTrimmed(result.toString());
    }

    @Override
    public String initial(@TypeInfo("ceylon.language::Integer")
    @Name("length") long length) {
        return instance(initial(value, length));
    }

    @Ignore
    public static java.lang.String initial(java.lang.String value, 
            long length) {
        if (length <= 0) {
            return "";
        } else if (length >= getSize(value)) {
            return value;
        } else {
            int offset = value.offsetByCodePoints(0, Util.toInt(length));
            return value.substring(0, offset);
        }
    }

    @Override
    public String terminal(@Name("length") long length) {
        return instance(terminal(value, length));
    }

    @Ignore
    public static java.lang.String terminal(java.lang.String value, 
            long length) {
        if (length <= 0) {
            return "";
        } else if (length >= getSize(value)) {
            return value;
        } else {
            int offset = value.offsetByCodePoints(0, 
                    Util.toInt(value.length()-length));
            return value.substring(offset, value.length());
        }
    }

    public java.lang.String join(@Name("objects") 
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Object,ceylon.language::Null>")
    Iterable<? extends java.lang.Object,?> objects) {
        return join(value, objects);
    }

    @Ignore
    public static java.lang.String join(java.lang.String value, 
            Iterable<? extends java.lang.Object, ?> objects) {
        java.lang.StringBuilder result = new java.lang.StringBuilder();
        Iterator<? extends java.lang.Object> it = objects.iterator();
        java.lang.Object elem = it.next();
        if (!(elem instanceof Finished)) {
            result.append(elem);
            if (value.isEmpty()) {
                while (!((elem = it.next()) instanceof Finished)) {
                    result.append(elem);
                }
            }
            else {
                while (!((elem = it.next()) instanceof Finished)) {
                    result.append(value).append(elem);
                }
            }
        }
        return result.toString();
    }

    @Ignore
    public java.lang.String join() {
        return "";
    }

    @Ignore
    public static java.lang.String join(java.lang.String value) {
        return "";
    }

    @Override
    public String measure(@Name("from") final Integer from,
                          @Name("length") final long length) {
        return instance(measure(value, from.longValue(), length));
    }

    @Ignore
    public static java.lang.String measure(java.lang.String value, 
            final long from, final long length) {
        long fromIndex = from;
        long len = getSize(value);
        if (fromIndex >= len || length <= 0) {
            return "";
        }
        long resultLength;
        if (fromIndex + length > len) {
            resultLength = len - fromIndex;
        }
        else {
            resultLength = length;
        }
        int start = value.offsetByCodePoints(0, Util.toInt(fromIndex));
        int end = value.offsetByCodePoints(start, Util.toInt(resultLength));
        return value.substring(start, end);
    }

    @Override
    public String span(@Name("from") final Integer from,
                       @Name("to") final Integer to) {
        return instance(span(value, from.longValue(), to.longValue()));
    }

    @Override
    public String spanFrom(@Name("from") final Integer from) {
        return instance(spanFrom(value, from.longValue()));
    }
    
    @Ignore
    public static java.lang.String spanFrom(java.lang.String value, 
            long from) {
        long len = getSize(value);
        if (len == 0) {
            return "";
        }
        if (from >= len) {
            return "";
        }
        long toIndex = len - 1;
        if (from < 0) {
            from = 0;
        }
        int start = value.offsetByCodePoints(0, Util.toInt(from));
        int end = value.offsetByCodePoints(start, 
                Util.toInt(toIndex - from + 1));
        return value.substring(start, end);
    }
    
    @Ignore
    public static List<? extends Character> sublist(java.lang.String value, long from, long to) {
        return instance(value).sublist(from, to);
    }
    
    @Ignore
    public static List<? extends Character> sublistTo(java.lang.String value, long to) {
        return instance(value).sublistTo(to);
    }
    
    @Ignore
    public static List<? extends Character> sublistFrom(java.lang.String value, long from) {
        return instance(value).sublistFrom(from);
    }
    
    @Override
    public String spanTo(@Name("to") final Integer to) {
        return instance(spanTo(value, to.longValue()));
    }
    
    @Ignore
    public static java.lang.String spanTo(java.lang.String value, 
            final long to) {
        long len = getSize(value);
        if (len == 0) {
            return "";
        }
        long toIndex = to;
        if (toIndex < 0) {
            return "";
        }
        if (toIndex >= len) {
            toIndex = len - 1;
        }
        int start = 0;
        int end = value.offsetByCodePoints(start, Util.toInt(toIndex + 1));
        return value.substring(start, end);
    }
    

    @Ignore
    public static java.lang.String span(java.lang.String value, 
            long from, long to) {
        long len = getSize(value);
        if (len == 0) {
            return "";
        }
        boolean reverse = to < from;
        if (reverse) {
            long _tmp = to;
            to = from;
            from = _tmp;
        }
        if (to < 0 || from >= len) {
            return "";
        }
        if (to >= len) {
            to = len - 1;
        }
        if (from < 0) {
            from = 0;
        }
        int start = value.offsetByCodePoints(0, Util.toInt(from));
        int end = value.offsetByCodePoints(start, 
                Util.toInt(to - from + 1));
        java.lang.String result = value.substring(start, end);
        return reverse ? getReversed(result) : result;
    }
    
    @Override
    public String getReversed() {
        return instance(getReversed(value));
    }
    
    @Ignore
    public static java.lang.String getReversed(java.lang.String value) {
        long len = getSize(value);
        if (len < 2) {
            return value;
        }
        // FIXME: this would be better to directly build the Sequence<Character>
        java.lang.StringBuilder builder 
            = new java.lang.StringBuilder(value.length());
        int offset = value.length();
        while (offset > 0) {
            int c = value.codePointBefore(offset);
            builder.appendCodePoint(c);
            offset -= java.lang.Character.charCount(c);
        }
        return builder.toString();
    }

    @Override
    public String repeat(@Name("times") long times) {
        return instance(repeat(value, times));
    }

    @Ignore
    public static java.lang.String repeat(java.lang.String value, 
            long times) {
        int len = value.length();
        if (times<=0 || len==0) return "";
        if (times==1) return value;
        java.lang.StringBuilder builder = 
        		new java.lang.StringBuilder(Util.toInt(len*times));
        for (int i=0; i<times; i++) {
            builder.append(value);
        }
        return builder.toString();
    }

    public java.lang.String replace(
            @Name("substring") java.lang.String substring,
            @Name("replacement") java.lang.String replacement) {
        return replace(value, substring, replacement);
    }

    @Ignore
    public static java.lang.String replace(java.lang.String value, 
            java.lang.String substring, java.lang.String replacement) {
        int index = value.indexOf(substring);
        if (index<0) return value;
        java.lang.StringBuilder builder = 
                new java.lang.StringBuilder(value);
        while (index>=0) {
            builder.replace(index, index+substring.length(), replacement);
            index = builder.indexOf(substring, index+replacement.length());
        }
        return builder.toString();
    }
    
    public java.lang.String replaceFirst(
            @Name("substring") java.lang.String substring,
            @Name("replacement") java.lang.String replacement) {
        return replaceFirst(value, substring, replacement);
    }

    @Ignore
    public static java.lang.String replaceFirst(java.lang.String value, 
            java.lang.String substring, java.lang.String replacement) {
        int index = value.indexOf(substring);
        if (index<0) {
            return value;
        }
        else {
            return value.substring(0,index) + replacement + 
                    value.substring(index+substring.length());
        }
    }

    public java.lang.String replaceLast(
            @Name("substring") java.lang.String substring,
            @Name("replacement") java.lang.String replacement) {
        return replaceLast(value, substring, replacement);
    }

    @Ignore
    public static java.lang.String replaceLast(java.lang.String value, 
            java.lang.String substring, java.lang.String replacement) {
        int index = value.lastIndexOf(substring);
        if (index<0) {
            return value;
        }
        else {
            return value.substring(0,index) + replacement + 
                    value.substring(index+substring.length());
        }
    }

    @TypeInfo("ceylon.language::Iterable<ceylon.language::String,ceylon.language::Nothing>")
    public Iterable<? extends String, ?> split(
            @TypeInfo(value="ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Character,ceylon.language::Character,ceylon.language::Empty>>")
            @Defaulted
            @Name("splitting") 
            @FunctionalParameter("(ch)") 
            Callable<? extends Boolean> splitting,
            @Defaulted
            @Name("discardSeparators") 
            boolean discardSeparators,
            @Defaulted
            @Name("groupSeparators") 
            boolean groupSeparators) {
        if (value.isEmpty()) {
            return new Singleton<String>(String.$TypeDescriptor$, this);
        }
        return new StringTokens(value, splitting, 
                !discardSeparators, groupSeparators);
    }

    @Ignore
    public static Iterable<? extends String, ?> 
    split(java.lang.String value,
            Callable<? extends Boolean> splitting,
            boolean discardSeparators,
            boolean groupSeparators) {
        if (value.isEmpty()) {
            return new Singleton<String>(String.$TypeDescriptor$, 
                    instance(value));
        }
        return new StringTokens(value, splitting, 
                !discardSeparators, groupSeparators);
    }

    @Ignore
    public Iterable<? extends String, ?> 
    split(Callable<? extends Boolean> splitting,
            boolean discardSeparators) {
        return split(splitting, discardSeparators, 
                split$groupSeparators(splitting, discardSeparators));
    }

    @Ignore
    public static Iterable<? extends String, ?> 
    split(java.lang.String value,
            Callable<? extends Boolean> splitting,
            boolean discardSeparators) {
        return split(value, splitting, discardSeparators, 
                split$groupSeparators(splitting, discardSeparators));
    }

    @Ignore
    public Iterable<? extends String, ?> split(
            Callable<? extends Boolean> splitting) {
        return split(splitting, split$discardSeparators(splitting));
    }

    @Ignore
    public static Iterable<? extends String, ?> 
    split(java.lang.String value,
            Callable<? extends Boolean> splitting) {
        return split(value, splitting, split$discardSeparators(splitting));
    }

    @Ignore
    public Iterable<? extends String, ?> split() {
        return split(split$splitting());
    }

    @Ignore
    public static Iterable<? extends String, ?> 
    split(java.lang.String value) {
        return split(value, split$splitting());
    }

    @Ignore
    public static Callable<? extends Boolean> split$splitting(){
        return WHITESPACE;
    }

    @Ignore
    public static boolean split$discardSeparators(java.lang.Object separator){
        return true;
    }

    @Ignore
    public static boolean split$groupSeparators(java.lang.Object separator, 
            boolean discardSeparators){
        return true;
    }
    
    @SuppressWarnings("rawtypes")
    @TypeInfo("ceylon.language::Tuple<ceylon.language::String,ceylon.language::String,ceylon.language::Tuple<ceylon.language::String,ceylon.language::String,ceylon.language::Empty>>")
    @Override
    public Sequence slice(@Name("index") long index) {
        return slice(value,index);
    }
    
    @Ignore
    @SuppressWarnings("rawtypes")
    public static Sequence slice(java.lang.String value, long index) {
        java.lang.String first;
        java.lang.String second;
        if (index<=0) {
            first = "";
            second = value;
        }
        else if (index>=value.length()) {
            first = value;
            second = "";
        }
        else {
            int intIndex =
                    value.offsetByCodePoints(0, 
                            Util.toInt(index));
            first = value.substring(0,intIndex);
            second = value.substring(intIndex);
        }
        return new Tuple(String.$TypeDescriptor$, 
                new String[] { instance(first), 
                        instance(second) });
    }
    
    @Ignore
    private static Callable<Boolean> WHITESPACE = 
    new AbstractCallable<Boolean>(Boolean.$TypeDescriptor$, 
            TypeDescriptor.klass(Tuple.class, Character.$TypeDescriptor$, 
                    Character.$TypeDescriptor$, Empty.$TypeDescriptor$),
            "whitespace", (short)-1) {
        @Override
        public Boolean $call$(java.lang.Object ch) {
            return Boolean.instance(((Character) ch).getWhitespace());
        }
    };
    
    @Ignore
    private static Callable<Boolean> NEWLINES = 
    new AbstractCallable<Boolean>(Boolean.$TypeDescriptor$, 
            TypeDescriptor.klass(Tuple.class, Character.$TypeDescriptor$, 
                    Character.$TypeDescriptor$, Empty.$TypeDescriptor$),
            "newlines", (short)-1) {
        @Override
        public Boolean $call$(java.lang.Object ch) {
            return Boolean.instance(((Character) ch).intValue()=='\n');
        }
    };
    
    @Ignore
    private static Callable<Boolean> RETURNS = 
    new AbstractCallable<Boolean>(Boolean.$TypeDescriptor$, 
            TypeDescriptor.klass(Tuple.class, Character.$TypeDescriptor$, 
                    Character.$TypeDescriptor$, Empty.$TypeDescriptor$),
            "returns", (short)-1) {
        @Override
        public Boolean $call$(java.lang.Object ch) {
            return Boolean.instance(((Character) ch).intValue()=='\r');
        }
    };
    
    private static Callable<String> TRIM_RETURNS =
    new AbstractCallable<String>($TypeDescriptor$,
            TypeDescriptor.klass(Tuple.class, $TypeDescriptor$, 
                    $TypeDescriptor$, Empty.$TypeDescriptor$),
            "", (short)-1) {
        @Override
        public String $call$(java.lang.Object str) {
            return instance(trimTrailing(((String)str).value, RETURNS));
        }
    };
    
    private static Callable<String> CONCAT_LINES_WITH_BREAKS =
    new AbstractCallable<String>($TypeDescriptor$,
            TypeDescriptor.klass(Tuple.class, 
                    TypeDescriptor.klass(Sequence.class,$TypeDescriptor$), 
                    TypeDescriptor.klass(Sequence.class,$TypeDescriptor$), 
                    Empty.$TypeDescriptor$),
                "", (short)-1) {
        @Override
        public String $call$(java.lang.Object seq) {
            @SuppressWarnings("unchecked")
            Sequence<? extends String> strings = 
                    (Sequence<? extends String>) seq;
            java.lang.String str = strings.getFirst().value;
            if (strings.getSize()>1) {
                str +=  strings.getFromFirst(1).value;
            }
            return instance(str);
        }
    };
    
    @TypeInfo("ceylon.language::Iterable<ceylon.language::String>")
    @Transient
    public Iterable<? extends String, ?> getLines() {
        return split(NEWLINES, true, false).map($TypeDescriptor$, TRIM_RETURNS);
    }

    @Ignore
    public static Iterable<? extends String, ?> 
    getLines(java.lang.String value) {
        return split(value, NEWLINES, true, false).map($TypeDescriptor$, TRIM_RETURNS);
    }

    @TypeInfo("ceylon.language::Iterable<ceylon.language::String>")
    @Transient
    public Iterable<? extends String, ?> getLinesWithBreaks() {
        return split(NEWLINES, false, false).partition(2)
                .map($TypeDescriptor$, CONCAT_LINES_WITH_BREAKS);
    }

    @Ignore
    public static Iterable<? extends String, ?> 
    getLinesWithBreaks(java.lang.String value) {
        return split(value, NEWLINES, false, false).partition(2)
                .map($TypeDescriptor$, CONCAT_LINES_WITH_BREAKS);
    }

    @Override
    public String $clone() {
        return this;
    }

    @Ignore
    public static java.lang.String $clone(java.lang.String value) {
        return value;
    }

    @Ignore
    public static <Result> Iterable<? extends Result, ?> 
    map(@Ignore TypeDescriptor $reifiedResult, java.lang.String value, 
            Callable<? extends Result> f) {
        return instance(value).map($reifiedResult, f);
    }
    
    @Ignore
    public static <Result, OtherAbsent> Iterable<? extends Result, ?> 
    flatMap(@Ignore TypeDescriptor $reified$Result, @Ignore TypeDescriptor $reified$OtherAbsent, java.lang.String value, 
            Callable<? extends Iterable<? extends Result, ? extends OtherAbsent>> collecting) {
        return instance(value).flatMap($reified$Result, $reified$OtherAbsent, collecting);
    }

    @SuppressWarnings("rawtypes")
    @Ignore
    public static <Type> Iterable 
    narrow(@Ignore TypeDescriptor $reifiedType, java.lang.String value) {
        return instance(value).narrow($reifiedType);
    }

    @Ignore
    public static Sequential<? extends Character> 
    select(java.lang.String value, 
            Callable<? extends Boolean> f) {
        return instance(value).select(f);
    }

    @Transient
    @Override
    public String getCoalesced() {
        return this; //Can't have null characters
    }

    @Ignore
    public static java.lang.String 
    getCoalesced(java.lang.String value) {
        return value;
    }

    @Override @Ignore
    public <Default> Iterable<?,?> 
    defaultNullElements(@Ignore TypeDescriptor $reifiedDefault, 
            Default defaultValue) {
        return this;
    }

    @Ignore
    public static <Default> Iterable<?,?> 
    defaultNullElements(@Ignore TypeDescriptor $reifiedDefault, 
            java.lang.String string, Default defaultValue) {
        return instance(string);
    }

    @Override
    public String getRest() {
        return value.isEmpty() ? this :
            instance(value.substring(value.offsetByCodePoints(0, 1)));
    }

    @Ignore
    public static java.lang.String getRest(java.lang.String value) {
        return value.isEmpty() ? "" :
            value.substring(value.offsetByCodePoints(0, 1));
    }
    
    @Ignore
    public static Iterable<? extends Character,?> 
    getExceptLast(java.lang.String value) {
    	return instance(value).getExceptLast();
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language::Character")
    public Character getFirst() {
        return getFirst(value);
    }

    @Ignore
    public static Character getFirst(java.lang.String value) {
        if (value.isEmpty()) {
            return null;
        } else {
            return Character.instance(value.codePointAt(0));
        }
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language::Character")
    public Character getLast() {
        return getLast(value);
    }

    @Ignore
    public static Character getLast(java.lang.String value) {
        if (value.isEmpty()) {
            return null;
        } else {
            return Character.instance(value.codePointBefore(value.length()));
        }
    }

    @Ignore
    public static Sequential<? extends Character> 
    sequence(java.lang.String value) {
        return instance(value).sequence();
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language::Character")
    public Character find(
            @Name("selecting")
            @FunctionalParameter("(element)")
            @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Character,ceylon.language::Character,ceylon.language::Empty>>")
            Callable<? extends Boolean> f) {
        return find(value,f);
    }
    
    @Ignore
    public static Character find(java.lang.String value, 
            Callable<? extends Boolean> f) {
        for (int offset = 0; offset < value.length();) {
            int codePoint = value.codePointAt(offset);
            Character ch = Character.instance(codePoint);
            if(f.$call$(ch).booleanValue()) {
                return ch;
            }
            offset += java.lang.Character.charCount(codePoint);
        }
        return null;
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language::Character")
    public Character findLast(
            @Name("selecting")
            @FunctionalParameter("(element)")
            @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Character,ceylon.language::Character,ceylon.language::Empty>>")
            Callable<? extends Boolean> f) {
        return findLast(value,f);
    }
    
    @Ignore
    public static Character findLast(java.lang.String value, 
            Callable<? extends Boolean> f) {
        Character result = null;
        for (int offset = 0; offset < value.length();) {
            int codePoint = value.codePointAt(offset);
            Character ch = Character.instance(codePoint);
            if(f.$call$(ch).booleanValue()) {
                result = ch;
            }
            offset += java.lang.Character.charCount(codePoint);
        }
        return result;
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language::Entry<ceylon.language::Integer,ceylon.language::Character>")
    public Entry<? extends Integer,? extends Character> locate(
            @Name("selecting")
            @FunctionalParameter("(element)")
            @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Character,ceylon.language::Character,ceylon.language::Empty>>")
            Callable<? extends Boolean> f) {
        return locate(value,f);
    }
    
    @Ignore
    public static Entry<? extends Integer,? extends Character> 
    locate(java.lang.String value, 
            Callable<? extends Boolean> f) {
        int index = 0;
        for (int offset = 0; offset < value.length();) {
            int codePoint = value.codePointAt(offset);
            Character ch = Character.instance(codePoint);
            if(f.$call$(ch).booleanValue()) {
                return new Entry<Integer,Character>(
                        Integer.$TypeDescriptor$, Character.$TypeDescriptor$,
                        Integer.instance(index), ch);
            }
            offset += java.lang.Character.charCount(codePoint);
            index++;
        }
        return null;
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language::Entry<ceylon.language::Integer,ceylon.language::Character>")
    public Entry<? extends Integer,? extends Character> locateLast(
            @Name("selecting")
            @FunctionalParameter("(element)")
            @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Character,ceylon.language::Character,ceylon.language::Empty>>")
            Callable<? extends Boolean> f) {
        return locateLast(value,f);
    }
    
    @Ignore
    public static Entry<? extends Integer,? extends Character> 
    locateLast(java.lang.String value, 
            Callable<? extends Boolean> f) {
        Character result = null;
        int resultIndex = -1;
        int index = 0;
        for (int offset = 0; offset < value.length();) {
            int codePoint = value.codePointAt(offset);
            Character ch = Character.instance(codePoint);
            if (f.$call$(ch).booleanValue()) {
                result = ch;
                resultIndex = index;
            }
            offset += java.lang.Character.charCount(codePoint);
            index++;
        }
        return result==null ? null : 
            new Entry<Integer,Character>(
                Integer.$TypeDescriptor$, Character.$TypeDescriptor$,
                Integer.instance(resultIndex), result);
    }
    
    @Ignore
    public static Iterable<? extends Entry<? extends Integer, ? extends Character>, ? extends java.lang.Object> 
    locations(java.lang.String value,
            Callable<? extends Boolean> selecting) {
        return instance(value).locations(selecting);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Ignore
    public static Sequential<? extends Character> 
    sort(java.lang.String value, Callable<? extends Comparison> f) {
        if (value.isEmpty()) {
            return (Sequential)empty_.get_();
        } else {
            return instance(value).sort(f);
        }
    }
    
    @Ignore
    public static java.lang.Object max(java.lang.String value, 
            Callable<? extends Comparison> comparing) {
        return instance(value).max(comparing);
    }

    @Ignore
    public static Iterable<? extends Integer, ?> 
    indexesWhere(java.lang.String value, Callable<? extends Boolean> f) {
        return instance(value).indexesWhere(f);
    }
    
    @Ignore
    public static Integer 
    firstIndexWhere(java.lang.String value, Callable<? extends Boolean> f) {
        return instance(value).firstIndexWhere(f);
    }
    
    @Ignore
    public static Integer 
    lastIndexWhere(java.lang.String value, Callable<? extends Boolean> f) {
        return instance(value).lastIndexWhere(f);
    }
    
    @Ignore
    public static Iterable<? extends Character, ?> 
    filter(java.lang.String value, Callable<? extends Boolean> f) {
        return instance(value).filter(f);
    }

    @Ignore
    public static <Result> Sequential<? extends Result> 
    collect(@Ignore TypeDescriptor $reifiedResult, 
            java.lang.String value, Callable<? extends Result> f) {
        return instance(value).collect($reifiedResult, f);
    }

    @Ignore
    public static <Result> Callable<? extends Result> 
    fold(@Ignore TypeDescriptor $reifiedResult, 
            java.lang.String value, Result ini) {
        return instance(value).fold($reifiedResult, ini);
    }
    
    @Ignore
    public static <Result> 
    Callable<? extends Iterable<? extends Result,? extends java.lang.Object>> 
    scan(@Ignore TypeDescriptor $reifiedResult, 
            java.lang.String value, Result ini) {
        return instance(value).scan($reifiedResult, ini);
    }
    
    @Override
    @TypeInfo("Result|ceylon.language::Character|ceylon.language::Null")
    @TypeParameters(@TypeParameter("Result"))
    public <Result> java.lang.Object 
    reduce(@Ignore TypeDescriptor $reifiedResult, 
            @Name("accumulating")
            @FunctionalParameter("(partial,element)")
            @TypeInfo("ceylon.language::Callable<Result,ceylon.language::Tuple<Result|ceylon.language::Character,Result|ceylon.language::Character,ceylon.language::Tuple<ceylon.language::Character,ceylon.language::Character,ceylon.language::Empty>>>")
            Callable<? extends Result> f) {
        return reduce($reifiedResult,value,f);
    }
    
    @Ignore
    public static <Result> java.lang.Object 
    reduce(@Ignore TypeDescriptor $reifiedResult, 
            java.lang.String value, 
            Callable<? extends Result> f) {
        if (value.isEmpty()) {
            return null;
        }
        int initial = value.codePointAt(0);
        java.lang.Object partial = Character.instance(initial);
        for (int offset = java.lang.Character.charCount(initial); 
                offset < value.length();) {
            int codePoint = value.codePointAt(offset);
            partial = f.$call$(partial, Character.instance(codePoint));
            offset += java.lang.Character.charCount(codePoint);
        }
        return partial;
    }

    @Override
    @TypeInfo(declaredVoid=true, 
              value="ceylon.language::Anything")
    public java.lang.Object each(
            @Name("step")
            @FunctionalParameter("!(element)")
            @TypeInfo("ceylon.language::Callable<ceylon.language::Anything,ceylon.language::Tuple<ceylon.language::Character,ceylon.language::Character,ceylon.language::Empty>>")
            Callable<? extends java.lang.Object> step) {
        return each(value,step);
    }
    
    @Ignore
    public static java.lang.Object each(java.lang.String value, 
            Callable<? extends java.lang.Object> f) {
        for (int offset = 0; offset < value.length();) {
            int codePoint = value.codePointAt(offset);
            f.$call$(Character.instance(codePoint));
            offset += java.lang.Character.charCount(codePoint);
        }
        return null;
    }

    @Override
    @TypeInfo("ceylon.language::Boolean")
    public boolean any(
            @Name("selecting")
            @FunctionalParameter("(element)")
            @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Character,ceylon.language::Character,ceylon.language::Empty>>")
            Callable<? extends Boolean> f) {
        return any(value,f);
    }
    
    @Ignore
    public static boolean any(java.lang.String value, 
            Callable<? extends Boolean> f) {
        for (int offset = 0; offset < value.length();) {
            int codePoint = value.codePointAt(offset);
            if (f.$call$(Character.instance(codePoint)).booleanValue()) {
                return true;
            }
            offset += java.lang.Character.charCount(codePoint);
        }
        return false;
    }

    @Override
    @TypeInfo("ceylon.language::Boolean")
    public boolean every(
            @Name("selecting")
            @FunctionalParameter("(element)")
            @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Character,ceylon.language::Character,ceylon.language::Empty>>")
            Callable<? extends Boolean> f) {
        return every(value,f);
    }
    
    @Ignore
    public static boolean every(java.lang.String value, 
            Callable<? extends Boolean> f) {
        for (int offset = 0; offset < value.length();) {
            int codePoint = value.codePointAt(offset);
            if(!f.$call$(Character.instance(codePoint)).booleanValue()) {
                return false;
            }
            offset += java.lang.Character.charCount(codePoint);
        }
        return true;
    }

    @Ignore
    public static Iterable<? extends Character, ?>
    skip(java.lang.String value, long skip) {
        return instance(value).skip(skip);
    }
    
    @Ignore
    public static Iterable<? extends Character, ?>
    take(java.lang.String value, long take) {
        return instance(value).take(take);
    }
    
    @Ignore
    public static Iterable<? extends Character, ?> 
    takeWhile(java.lang.String value, Callable<? extends Boolean> take) {
        return instance(value).takeWhile(take);
    }
    
    @Ignore
    public static Iterable<? extends Character, ?> 
    skipWhile(java.lang.String value, Callable<? extends Boolean> skip) {
        return instance(value).skipWhile(skip);
    }
    
    @Ignore
    public static Iterable<? extends Character, ?> 
    by(java.lang.String value, long step) {
        return instance(value).by(step);
    }

    @Override
    @TypeInfo("ceylon.language::Integer")
    public long count(
            @Name("selecting")
            @FunctionalParameter("(element)")
            @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Character,ceylon.language::Character,ceylon.language::Empty>>")
            Callable<? extends Boolean> f) {
        return count(value,f);
    }
    
    @Ignore
    public static long count(java.lang.String value, 
            Callable<? extends Boolean> f) {
        int count = 0;
        for (int offset = 0; offset < value.length();) {
            int codePoint = value.codePointAt(offset);
            if(f.$call$(Character.instance(codePoint)).booleanValue()) {
                count++;
            }
            offset += java.lang.Character.charCount(codePoint);
        }
        return count;
    }

    @Ignore
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Iterable<? extends Entry<? extends Integer, ? extends Character>, ?> 
    getIndexed(java.lang.String value) {
        if (value.isEmpty()) {
            return (Iterable) instance(value);
        } else {
            return instance(value).getIndexed();
        }
    }

    @Ignore
    @SuppressWarnings("rawtypes")
    public static <Other,Absent> Iterable 
    chain(@Ignore TypeDescriptor $reifiedOther, 
            @Ignore TypeDescriptor $reifiedOtherAbsent, 
            java.lang.String value, 
            Iterable<? extends Other, ? extends Absent> other) {
        if (value.isEmpty()) {
            return other;
        }
        else {
            return instance(value).chain($reifiedOther, 
                    $reifiedOtherAbsent, other);
        }
    }
    
    @Ignore
    @SuppressWarnings("rawtypes")
    public static <Other> Iterable 
    follow(@Ignore TypeDescriptor $reifiedOther, 
            java.lang.String value, Other other) {
        return instance(value).follow($reifiedOther, other);    
    }
    
    @Ignore
    public static <Other> Iterable<? extends java.lang.Object, ? extends java.lang.Object> 
    interpose(@Ignore TypeDescriptor $reifiedOther, 
            java.lang.String value, Other other) {
        return instance(value).interpose($reifiedOther, other);    
    }
    
    @Ignore
    @SuppressWarnings("rawtypes")
    public static <Other> Iterable
    interpose(@Ignore TypeDescriptor $reifiedOther, 
            java.lang.String value, Other other, long step) {
        return instance(value).interpose($reifiedOther, other, step);    
    }
    
    @Ignore
    @SuppressWarnings("rawtypes")
    public static <Other, OtherAbsent> Iterable
    product(@Ignore TypeDescriptor $reified$Other,
            @Ignore TypeDescriptor $reified$OtherAbsent,
            java.lang.String value, 
            Iterable<? extends Other, ? extends OtherAbsent> other) {
        return instance(value).product($reified$Other, $reified$OtherAbsent, other);
    }
    
    @Ignore @SuppressWarnings({ "rawtypes" })
    public static <Other>List patch(@Ignore TypeDescriptor $reifiedOther, 
            java.lang.String value, List<? extends Other> list, long from, long length) {
        return instance(value).patch($reifiedOther, list, from, length);
    }

    @Ignore @SuppressWarnings({ "rawtypes" })
    public static <Other>List patch(@Ignore TypeDescriptor $reifiedOther, 
            java.lang.String value, List<? extends Other> list, long from) {
        return instance(value).patch($reifiedOther, list, from, 0);
    }

    @Ignore
    public static Iterable<? extends Character,?> 
    getCycled(java.lang.String value) {
        return instance(value).getCycled();
    }
    
    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }

    public static boolean largerThan(java.lang.String value, 
            String other) {
        return value.compareTo(other.value)>0;
    }

    public static boolean largerThan(java.lang.String value,
                java.lang.String other) {
        return value.compareTo(other)>0;
    }
    
    @Override
    public boolean largerThan(@Name("other") String other) {
        return value.compareTo(other.value)>0;
    }

    public static boolean notSmallerThan(java.lang.String value, 
            String other) {
        return value.compareTo(other.value)>=0;
    }

    public static boolean notSmallerThan(java.lang.String value,
                java.lang.String other) {
        return value.compareTo(other)>=0;
    }

    @Override
    public boolean notSmallerThan(@Name("other") String other) {
        return value.compareTo(other.value)>=0;
    }

    public static boolean smallerThan(java.lang.String value, 
            String other) {
        return value.compareTo(other.value)<0;
    }

    public static boolean smallerThan(java.lang.String value,
                java.lang.String other) {
        return value.compareTo(other)<0;
    }

    @Override
    public boolean smallerThan(@Name("other") String other) {
        return value.compareTo(other.value)<0;
    }

    public static boolean notLargerThan(java.lang.String value, 
            String other) {
        return value.compareTo(other.value)<=0;
    }

    public static boolean notLargerThan(java.lang.String value,
                java.lang.String other) {
        return value.compareTo(other)<=0;
    }

    @Override
    public boolean notLargerThan(@Name("other") String other) {
        return value.compareTo(other.value)<=0;
    }

    @Ignore
    public static <Result,Args extends Sequential<? extends java.lang.Object>> Callable<? extends Iterable<? extends Result, ?>> 
    spread(@Ignore TypeDescriptor $reifiedResult,
          @Ignore TypeDescriptor $reifiedArgs, 
          java.lang.String value, Callable<? extends Callable<? extends Result>> method) {
        return instance(value).spread($reifiedResult, $reifiedArgs, method);
    }
    
    public java.lang.String pad(
            @Name("size") 
            long size, 
            @Name("character") 
            @TypeInfo("ceylon.language::Character") 
            @Defaulted 
            int character) {
        return pad(value, size, character);
    }
    
    @Ignore
    public java.lang.String pad(long size) {
        return pad(value, size, pad$character(size));
    }
    
    @Ignore
    public static java.lang.String pad(java.lang.String value, long size) {
        return pad(value, size, pad$character(size));
    }
    
    @Ignore
    public static int pad$character(long size) {
        return ' ';
    }
    
    @Ignore
    public static java.lang.String pad(java.lang.String value, long size, int character) {
        int length = value.length();
        if (size<=length) return value;
        long leftPad = (size-length)/2;
        long rightPad = leftPad + (size-length)%2;
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        for (int i=0;i<leftPad;i++) {
            builder.appendCodePoint(character);
        }
        builder.append(value);
        for (int i=0;i<rightPad;i++) {
            builder.appendCodePoint(character);
        }
        return builder.toString();
    }
    
    public java.lang.String padLeading(
            @Name("size") 
            long size, 
            @Name("character") 
            @TypeInfo("ceylon.language::Character") 
            @Defaulted 
            int character) {
        return padLeading(value, size, character);
    }
    
    @Ignore
    public java.lang.String padLeading(long size) {
        return padLeading(value, size, padLeading$character(size));
    }
    
    @Ignore
    public static java.lang.String padLeading(java.lang.String value, long size) {
        return padLeading(value, size, padLeading$character(size));
    }
    
    @Ignore
    public static int padLeading$character(long size) {
        return ' ';
    }
    
    @Ignore
    public static java.lang.String padLeading(java.lang.String value, long size, int character) {
        int length = value.length();
        if (size<=length) return value;
        long leftPad = size-length;
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        for (int i=0;i<leftPad;i++) {
            builder.appendCodePoint(character);
        }
        builder.append(value);
        return builder.toString();
    }
    
    public java.lang.String padTrailing(
            @Name("size") 
            long size, 
            @Name("character") 
            @TypeInfo("ceylon.language::Character") 
            @Defaulted 
            int character) {
        return padTrailing(value, size, character);
    }
    
    @Ignore
    public java.lang.String padTrailing(long size) {
        return padTrailing(value, size, padTrailing$character(size));
    }
    
    @Ignore
    public static java.lang.String padTrailing(java.lang.String value, long size) {
        return padTrailing(value, size, padTrailing$character(size));
    }
    
    @Ignore
    public static int padTrailing$character(long size) {
        return ' ';
    }
    
    @Ignore
    public static java.lang.String padTrailing(java.lang.String value, long size, int character) {
        int length = value.length();
        if (size<=length) return value;
        long rightPad = size-length;
        java.lang.StringBuilder builder = new java.lang.StringBuilder(value);
        for (int i=0;i<rightPad;i++) {
            builder.appendCodePoint(character);
        }
        return builder.toString();
    }
    
    @Ignore
    public static Iterable getPaired(java.lang.String value) {
        return instance(value).getPaired();
    }
    
    @Ignore
    public static Iterable<? extends Sequence<? extends Character>,? extends java.lang.Object> 
    partition(java.lang.String value, long length) {
    	return instance(value).partition(length);
    }
    
    @Ignore
    public long copyTo$sourcePosition(Array<Character> destination){
        return 0;
    }

    @Ignore
    public long copyTo$destinationPosition(Array<Character> destination, 
            long sourcePosition){
        return 0;
    }

    @Ignore
    public long copyTo$length(Array<Character> destination, 
            long sourcePosition, long destinationPosition){
        return Math.min(value.length()-sourcePosition,
                destination.getSize()-destinationPosition);
    }

    @Ignore
    public static void copyTo(java.lang.String value, Array<Character> destination){
        copyTo(value, destination, 0, 0);
    }

    @Ignore
    public static void copyTo(java.lang.String value, Array<Character> destination, long sourcePosition){
        copyTo(value, destination, sourcePosition, 0);
    }

    @Ignore
    public static void copyTo(java.lang.String value, Array<Character> destination, 
            long sourcePosition, long destinationPosition){
        copyTo(value, destination, 
                sourcePosition, destinationPosition, 
                Math.min(value.length()-sourcePosition,
                        destination.getSize()-destinationPosition));
    }

    @Ignore
    public void copyTo(Array<Character> destination){
        copyTo(value, destination, 0, 0);
    }

    @Ignore
    public void copyTo(Array<Character> destination, long sourcePosition){
        copyTo(value, destination, sourcePosition, 0);
    }

    @Ignore
    public void copyTo(Array<Character> destination, 
            long sourcePosition, long destinationPosition){
        copyTo(value, destination, 
                sourcePosition, destinationPosition, 
                copyTo$length(destination, sourcePosition, destinationPosition));
    }

    @Ignore
    public static void copyTo(java.lang.String value,
            Array<Character> destination, 
            long sourcePosition, 
            long destinationPosition, 
            long length){
        int count = 0;
        int dest = Util.toInt(destinationPosition);
        for (int index = value.offsetByCodePoints(0,Util.toInt(sourcePosition)); 
                count<length;) {
            int codePoint = value.codePointAt(index);
            ((int[])destination.toArray())[count+dest] = codePoint;
            index += java.lang.Character.charCount(codePoint);
            count++;
        }
    }

    public void copyTo(@Name("destination") Array<Character> destination, 
                       @Name("sourcePosition") @Defaulted long sourcePosition, 
                       @Name("destinationPosition") @Defaulted long destinationPosition, 
                       @Name("length") @Defaulted long length){
        copyTo(value, destination, sourcePosition, destinationPosition, length);
    }
    
    
    @Ignore
    public static Iterable<? extends Sequence<? extends Character>, ? extends java.lang.Object>
    getPermutations(java.lang.String value) {
        return instance(value).getPermutations();
    }
    
    @Ignore
    public static <Group> Iterable<? extends Entry<? extends Group, ? extends Sequence<? extends Character>>, ? extends java.lang.Object>
    group(java.lang.String value, TypeDescriptor $reifiedGroup, Callable<? extends Group> fun) {
        return instance(value).group($reifiedGroup, fun);
    }
    
    @Ignore
    public static Iterable<? extends Character, ? extends java.lang.Object>
    getDistinct(java.lang.String value) {
        return instance(value).getDistinct();
    }

    //WARNING: pure boilerplate from here on!
    
    @Override @Ignore
    public Collection$impl<? extends Character> $ceylon$language$Collection$impl() {
        return new Collection$impl<Character>
                (Character.$TypeDescriptor$, this);
    }

    @Override @Ignore
    public Iterable<? extends Sequence<? extends Character>, ? extends java.lang.Object> getPermutations() {
        return $ceylon$language$Collection$impl().getPermutations();
    }

    @Override @Ignore
    public Iterable$impl<? extends Character, ? extends java.lang.Object> $ceylon$language$Iterable$impl() {
        return new Iterable$impl<Character, java.lang.Object>
                (Character.$TypeDescriptor$, Null.$TypeDescriptor$, this);
    }

    @Override @Ignore
    public Iterable<? extends Character, ? extends java.lang.Object> by(long step) {
        return $ceylon$language$Iterable$impl().by(step);
    }

    @Override @Ignore
    public <Other, OtherAbsent> Iterable chain(TypeDescriptor arg0, TypeDescriptor arg1,
            Iterable<? extends Other, ? extends OtherAbsent> arg2) {
        return $ceylon$language$Iterable$impl().chain(arg0, arg1, arg2);
    }

    @Override @Ignore
    public Iterable<? extends Character, ? extends java.lang.Object> filter(Callable<? extends Boolean> arg0) {
        return $ceylon$language$Iterable$impl().filter(arg0);
    }

    @Override @Ignore
    public <Result, OtherAbsent> Iterable flatMap(TypeDescriptor arg0, TypeDescriptor arg1,
            Callable<? extends Iterable<? extends Result, ? extends OtherAbsent>> arg2) {
        return $ceylon$language$Iterable$impl().flatMap(arg0, arg1, arg2);
    }

    @Override @Ignore
    public <Result> Callable<? extends Result> fold(TypeDescriptor arg0, Result arg1) {
        return $ceylon$language$Iterable$impl().fold(arg0, arg1);
    }

    @Override @Ignore
    public <Other> Iterable follow(TypeDescriptor arg0, Other arg1) {
        return $ceylon$language$Iterable$impl().follow(arg0, arg1);
    }

    @Override @Ignore
    public Iterable<? extends Character, ? extends java.lang.Object> getCycled() {
        return $ceylon$language$Iterable$impl().getCycled();
    }

    @Override @Ignore
    public Iterable<? extends Character, ? extends java.lang.Object> getDistinct() {
        return $ceylon$language$Iterable$impl().getDistinct();
    }

    @Override @Ignore
    public Iterable<? extends Character, ? extends java.lang.Object> getExceptLast() {
        return $ceylon$language$Iterable$impl().getExceptLast();
    }

    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Character>, ? extends java.lang.Object> getIndexed() {
        return $ceylon$language$Iterable$impl().getIndexed();
    }

    @Override @Ignore
    public Iterable<? extends Sequence<? extends Character>, ? extends java.lang.Object> getPaired() {
        return $ceylon$language$Iterable$impl().getPaired();
    }

    @Override @Ignore
    public <Group> Map<? extends Group, ? extends Sequence<? extends Character>> group(TypeDescriptor arg0,
            Callable<? extends Group> arg1) {
        return $ceylon$language$Iterable$impl().group(arg0, arg1);
    }

    @Override @Ignore
    public java.lang.Object indexes() {
        return $ceylon$language$Iterable$impl().indexes();
    }

    @Override @Ignore
    public <Other> Iterable interpose(TypeDescriptor arg0, Other arg1) {
        return $ceylon$language$Iterable$impl().interpose(arg0, arg1);
    }

    @Override @Ignore
    public <Other> Iterable interpose(TypeDescriptor arg0, Other arg1, long arg2) {
        return $ceylon$language$Iterable$impl().interpose(arg0, arg1, arg2);
    }

    @Override @Ignore
    public <Other> long interpose$step(TypeDescriptor arg0, Other arg1) {
        return $ceylon$language$Iterable$impl().interpose$step(arg0, arg1);
    }

    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Character>, ? extends java.lang.Object> locations(
            Callable<? extends Boolean> arg0) {
        return $ceylon$language$Iterable$impl().locations(arg0);
    }

    @Override @Ignore
    public <Result> Iterable<? extends Result, ? extends java.lang.Object> map(TypeDescriptor arg0,
            Callable<? extends Result> arg1) {
        return $ceylon$language$Iterable$impl().map(arg0, arg1);
    }

    @Override @Ignore
    public java.lang.Object max(Callable<? extends Comparison> arg0) {
        return $ceylon$language$Iterable$impl().max(arg0);
    }

    @Override @Ignore
    public <Type> Iterable narrow(TypeDescriptor arg0) {
        return $ceylon$language$Iterable$impl().narrow(arg0);
    }

    @Override @Ignore
    public Iterable<? extends Sequence<? extends Character>, ? extends java.lang.Object> partition(long arg0) {
        return $ceylon$language$Iterable$impl().partition(arg0);
    }

    @Override @Ignore
    public <Other, OtherAbsent> Iterable product(TypeDescriptor arg0, TypeDescriptor arg1,
            Iterable<? extends Other, ? extends OtherAbsent> arg2) {
        return $ceylon$language$Iterable$impl().product(arg0, arg1, arg2);
    }

    @Override @Ignore
    public <Result> Callable<? extends Iterable<? extends Result, ? extends java.lang.Object>> scan(TypeDescriptor arg0,
            Result arg1) {
        return $ceylon$language$Iterable$impl().scan(arg0, arg1);
    }

    @Override @Ignore
    public Sequential<? extends Character> select(Callable<? extends Boolean> arg0) {
        return $ceylon$language$Iterable$impl().select(arg0);
    }

    @Override @Ignore
    public Sequential<? extends Character> sequence() {
        return $ceylon$language$Iterable$impl().sequence();
    }

    @Override @Ignore
    public Iterable<? extends Character, ? extends java.lang.Object> skip(long arg0) {
        return $ceylon$language$Iterable$impl().skip(arg0);
    }

    @Override @Ignore
    public Iterable<? extends Character, ? extends java.lang.Object> skipWhile(Callable<? extends Boolean> arg0) {
        return $ceylon$language$Iterable$impl().skipWhile(arg0);
    }

    @Override @Ignore
    public Sequential<? extends Character> sort(Callable<? extends Comparison> arg0) {
        return $ceylon$language$Iterable$impl().sort(arg0);
    }

    @Override @Ignore
    public <Result, Args extends Sequential<? extends java.lang.Object>> Callable<? extends Iterable<? extends Result, ? extends java.lang.Object>> spread(
            TypeDescriptor arg0, TypeDescriptor arg1, Callable<? extends Callable<? extends Result>> arg2) {
        return $ceylon$language$Iterable$impl().spread(arg0, arg1, arg2);
    }

    @Override @Ignore
    public Iterable<? extends Character, ? extends java.lang.Object> take(long arg0) {
        return $ceylon$language$Iterable$impl().take(arg0);
    }

    @Override @Ignore
    public Iterable<? extends Character, ? extends java.lang.Object> takeWhile(Callable<? extends Boolean> arg0) {
        return $ceylon$language$Iterable$impl().takeWhile(arg0);
    }

    @Override @Ignore
    public Category$impl<? super java.lang.Object> $ceylon$language$Category$impl() {
        return new Category$impl<java.lang.Object>(Object.$TypeDescriptor$, this);
    }

    @Override @Ignore
    public boolean containsAny(Iterable<? extends java.lang.Object, ? extends java.lang.Object> arg0) {
        return $ceylon$language$Category$impl().containsAny(arg0);
    }

    @Override @Ignore
    public boolean containsEvery(Iterable<? extends java.lang.Object, ? extends java.lang.Object> arg0) {
        return $ceylon$language$Category$impl().containsEvery(arg0);
    }

    @Override @Ignore
    public Correspondence$impl<? super Integer, ? extends Character> $ceylon$language$Correspondence$impl() {
        return new Correspondence$impl<Integer,Character>(Integer.$TypeDescriptor$, Character.$TypeDescriptor$, this);
    }

    @Override @Ignore
    public boolean definesAny(Iterable<? extends Integer, ? extends java.lang.Object> arg0) {
        return $ceylon$language$Correspondence$impl().definesAny(arg0);
    }

    @Override @Ignore
    public boolean definesEvery(Iterable<? extends Integer, ? extends java.lang.Object> arg0) {
        return $ceylon$language$Correspondence$impl().definesEvery(arg0);
    }

    @Override @Ignore
    public <Absent> Iterable<? extends Character, ? extends Absent> getAll(TypeDescriptor arg0,
            Iterable<? extends Integer, ? extends Absent> arg1) {
        return $ceylon$language$Correspondence$impl().getAll(arg0, arg1);
    }

    @Override @Ignore
    public List$impl<? extends Character> $ceylon$language$List$impl() {
        return new List$impl<Character>(Character.$TypeDescriptor$, this);
    }

    @Override @Ignore
    public <Result> Sequential<? extends Result> collect(TypeDescriptor arg0, Callable<? extends Result> arg1) {
        return $ceylon$language$List$impl().collect(arg0, arg1);
    }

    @Override @Ignore
    public Integer firstIndexWhere(Callable<? extends Boolean> arg0) {
        return $ceylon$language$List$impl().firstIndexWhere(arg0);
    }

    @Override @Ignore
    public Character get(Integer index) {
        //NOTE THIS IMPORTANT PERFORMANCE OPTIMIZATION
        return getFromFirst(value, index.value);
    }

    @Override @Ignore
    public Iterable<? extends Integer, ? extends java.lang.Object> indexesWhere(Callable<? extends Boolean> arg0) {
        return $ceylon$language$List$impl().indexesWhere(arg0);
    }

    @Override @Ignore
    public Integer lastIndexWhere(Callable<? extends Boolean> arg0) {
        return $ceylon$language$List$impl().lastIndexWhere(arg0);
    }

    @Override @Ignore
    public <Other> List patch(TypeDescriptor arg0, List<? extends Other> arg1) {
        return $ceylon$language$List$impl().patch(arg0, arg1);
    }

    @Override @Ignore
    public <Other> List patch(TypeDescriptor arg0, List<? extends Other> arg1, long arg2) {
        return $ceylon$language$List$impl().patch(arg0, arg1, arg2);
    }

    @Override @Ignore
    public <Other> List patch(TypeDescriptor arg0, List<? extends Other> arg1, long arg2, long arg3) {
        return $ceylon$language$List$impl().patch(arg0, arg1, arg2, arg3);
    }

    @Override @Ignore
    public <Other> long patch$from(TypeDescriptor arg0, List<? extends Other> arg1) {
        return $ceylon$language$List$impl().patch$from(arg0, arg1);
    }

    @Override @Ignore
    public <Other> long patch$length(TypeDescriptor arg0, List<? extends Other> arg1, long arg2) {
        return $ceylon$language$List$impl().patch$length(arg0, arg1, arg2);
    }
    
    @Override @Ignore
    public List<? extends Character> sublist(long arg0, long arg1) {
        return $ceylon$language$List$impl().sublist(arg0, arg1);
    }

    @Override @Ignore
    public List<? extends Character> sublistFrom(long arg0) {
        return $ceylon$language$List$impl().sublistFrom(arg0);
    }

    @Override @Ignore
    public List<? extends Character> sublistTo(long arg0) {
        return $ceylon$language$List$impl().sublistTo(arg0);
    }
    
    @Override @Ignore
    public SearchableList$impl<Character> $ceylon$language$SearchableList$impl() {
        return new SearchableList$impl<Character>(Character.$TypeDescriptor$, this);
    }

//    @Override
//    public boolean startsWith(List<? extends java.lang.Object> sublist) {
//        return $ceylon$language$SearchableList$impl().startsWith(sublist);
//    }
//
//    @Override
//    public boolean endsWith(List<? extends java.lang.Object> sublist) {
//        return $ceylon$language$SearchableList$impl().endsWith(sublist);
//    }
    
    @Override @Ignore
    public long countInclusions(List<? extends Character> arg0) {
        return $ceylon$language$SearchableList$impl().countInclusions(arg0);
    }

    @Override @Ignore
    public long countInclusions$from(List<? extends Character> arg0) {
        return $ceylon$language$SearchableList$impl().countInclusions$from(arg0);
    }

    @Override @Ignore
    public long countOccurrences(Character arg0) {
        return $ceylon$language$SearchableList$impl().countOccurrences(arg0);
    }

    @Override @Ignore
    public long countOccurrences(Character arg0, long arg1) {
        return $ceylon$language$SearchableList$impl().countOccurrences(arg0, arg1);
    }

    @Override @Ignore
    public long countOccurrences$from(Character arg0) {
        return $ceylon$language$SearchableList$impl().countOccurrences$from(arg0);
    }

    @Override @Ignore
    public long countOccurrences$length(Character arg0, long arg1) {
        return $ceylon$language$SearchableList$impl().countOccurrences$length(arg0, arg1);
    }

    @Override @Ignore
    public Integer firstInclusion(List<? extends Character> arg0) {
        return $ceylon$language$SearchableList$impl().firstInclusion(arg0);
    }

    @Override @Ignore
    public long firstInclusion$from(List<? extends Character> arg0) {
        return $ceylon$language$SearchableList$impl().firstInclusion$from(arg0);
    }

    @Override @Ignore
    public Integer firstOccurrence(Character arg0) {
        return $ceylon$language$SearchableList$impl().firstOccurrence(arg0);
    }

    @Override @Ignore
    public Integer firstOccurrence(Character arg0, long arg1) {
        return $ceylon$language$SearchableList$impl().firstOccurrence(arg0, arg1);
    }

    @Override @Ignore
    public long firstOccurrence$from(Character arg0) {
        return $ceylon$language$SearchableList$impl().firstOccurrence$from(arg0);
    }

    @Override @Ignore
    public long firstOccurrence$length(Character arg0, long arg1) {
        return $ceylon$language$SearchableList$impl().firstOccurrence$length(arg0,arg1);
    }

    @Override @Ignore
    public boolean includes(List<? extends Character> arg0) {
        return $ceylon$language$SearchableList$impl().includes(arg0);
    }

    @Override @Ignore
    public long includes$from(List<? extends Character> arg0) {
        return $ceylon$language$SearchableList$impl().includes$from(arg0);
    }

    @Override @Ignore
    public Iterable<? extends Integer, ? extends java.lang.Object> inclusions(List<? extends Character> arg0) {
        return $ceylon$language$SearchableList$impl().inclusions(arg0);
    }

    @Override @Ignore
    public Iterable<? extends Integer, ? extends java.lang.Object> inclusions(List<? extends Character> arg0,
            long arg1) {
        return $ceylon$language$SearchableList$impl().inclusions(arg0, arg1);
    }

    @Override @Ignore
    public long inclusions$from(List<? extends Character> arg0) {
        return $ceylon$language$SearchableList$impl().inclusions$from(arg0);
    }

    @Override @Ignore
    public Integer lastInclusion(List<? extends Character> arg0) {
        return $ceylon$language$SearchableList$impl().lastInclusion(arg0);
    }

    @Override @Ignore
    public long lastInclusion$from(List<? extends Character> arg0) {
        return $ceylon$language$SearchableList$impl().lastInclusion$from(arg0);
    }

    @Override @Ignore
    public Integer lastOccurrence(Character arg0) {
        return $ceylon$language$SearchableList$impl().lastOccurrence(arg0);
    }

    @Override @Ignore
    public Integer lastOccurrence(Character arg0, long arg1) {
        return $ceylon$language$SearchableList$impl().lastOccurrence(arg0, arg1);
    }

    @Override @Ignore
    public long lastOccurrence$from(Character arg0) {
        return $ceylon$language$SearchableList$impl().lastOccurrence$from(arg0);
    }

    @Override @Ignore
    public long lastOccurrence$length(Character arg0, long arg1) {
        return $ceylon$language$SearchableList$impl().lastOccurrence$length(arg0, arg1);
    }

    @Override @Ignore
    public Iterable<? extends Integer, ? extends java.lang.Object> occurrences(Character arg0) {
        return $ceylon$language$SearchableList$impl().occurrences(arg0);
    }

    @Override @Ignore
    public Iterable<? extends Integer, ? extends java.lang.Object> occurrences(Character arg0, long arg1) {
        return $ceylon$language$SearchableList$impl().occurrences(arg0,arg1);
    }

    @Override @Ignore
    public Iterable<? extends Integer, ? extends java.lang.Object> occurrences(Character arg0, long arg1, long arg2) {
        return $ceylon$language$SearchableList$impl().occurrences(arg0,arg1,arg2);
    }

    @Override @Ignore
    public long occurrences$from(Character arg0) {
        return $ceylon$language$SearchableList$impl().occurrences$from(arg0);
    }

    @Override @Ignore
    public long occurrences$length(Character arg0, long arg1) {
        return $ceylon$language$SearchableList$impl().occurrences$length(arg0,arg1);
    }

    @Override @Ignore
    public boolean occurs(Character arg0) {
        return $ceylon$language$SearchableList$impl().occurs(arg0);
    }

    @Override @Ignore
    public boolean occurs(Character arg0, long arg1) {
        return $ceylon$language$SearchableList$impl().occurs(arg0,arg1);
    }

    @Override @Ignore
    public long occurs$from(Character arg0) {
        return $ceylon$language$SearchableList$impl().occurs$from(arg0);
    }

    @Override @Ignore
    public long occurs$length(Character arg0, long arg1) {
        return $ceylon$language$SearchableList$impl().occurs$length(arg0,arg1);
    }
}
