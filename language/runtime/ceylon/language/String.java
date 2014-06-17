package ceylon.language;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.language.AbstractCallable;
import com.redhat.ceylon.compiler.java.language.AbstractIterator;
import com.redhat.ceylon.compiler.java.language.StringInclusions;
import com.redhat.ceylon.compiler.java.language.StringTokens;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.FunctionalParameter;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.Transient;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.ValueType;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 7)
@Class(extendsType="ceylon.language::Object", basic = false, identifiable = false)
@SatisfiedTypes({"ceylon.language::List<ceylon.language::Character>",
                 "ceylon.language::Comparable<ceylon.language::String>",
                 "ceylon.language::Summable<ceylon.language::String>",
                 "ceylon.language::Ranged<ceylon.language::Integer,ceylon.language::Character,ceylon.language::String>"})
@ValueType
public final class String
    implements Comparable<String>, List<Character>,
               Summable<String>, ReifiedType {
    
    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = 
            TypeDescriptor.klass(String.class);
    
    @Ignore
    protected final Category$impl<java.lang.Object> 
    $ceylon$language$Category$this;
    @Ignore
    protected final Iterable$impl<Character,java.lang.Object> 
    $ceylon$language$Iterable$this;
    @Ignore
    protected final List$impl<Character> 
    $ceylon$language$List$this;
    @Ignore
    protected final Collection$impl<Character> 
    $ceylon$language$Collection$this;
    @Ignore
    protected final Correspondence$impl<Integer,Character> 
    $ceylon$language$Correspondence$this;
    @Ignore
    protected final Comparable$impl<String> 
    $ceylon$language$Comparable$this;

    @Ignore
    public final java.lang.String value;

    @SuppressWarnings("rawtypes")
    public String(@Name("characters")
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Character,ceylon.language::Null>")
    final Iterable<? extends Character, ?> characters) {
        this.$ceylon$language$Category$this = 
        		new Category$impl<java.lang.Object>(Object.$TypeDescriptor$,this);
        this.$ceylon$language$Iterable$this = 
        		new Iterable$impl<Character,java.lang.Object>(Character.$TypeDescriptor$, 
        				Null.$TypeDescriptor$, this);
        this.$ceylon$language$Correspondence$this = 
        		new Correspondence$impl<Integer,Character>(Integer.$TypeDescriptor$, 
        				Character.$TypeDescriptor$, this);
        this.$ceylon$language$List$this = 
        		new List$impl<Character>(Character.$TypeDescriptor$, this);
        this.$ceylon$language$Collection$this = 
        		new Collection$impl<Character>(Character.$TypeDescriptor$, this);
        this.$ceylon$language$Comparable$this = 
        		new Comparable$impl<String>(String.$TypeDescriptor$, this);
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
                    sb.append($tmp);
                }
                value = sb.toString();
            }
        }
    }

    @Ignore
    public String(final java.lang.String string) {
        this.$ceylon$language$Category$this = 
        		new Category$impl<java.lang.Object>(Object.$TypeDescriptor$,this);
        this.$ceylon$language$Iterable$this = 
        		new Iterable$impl<Character,java.lang.Object>(Character.$TypeDescriptor$, 
        		Null.$TypeDescriptor$, this);
        this.$ceylon$language$Correspondence$this = 
        		new Correspondence$impl<Integer,Character>(Integer.$TypeDescriptor$, 
        		Character.$TypeDescriptor$, this);
        this.$ceylon$language$List$this = 
        		new List$impl<Character>(Character.$TypeDescriptor$, this);
        this.$ceylon$language$Collection$this = 
        		new Collection$impl<Character>(Character.$TypeDescriptor$, this);
        this.$ceylon$language$Comparable$this = 
        		new Comparable$impl<String>(String.$TypeDescriptor$, this);
        value = string;
    }

    @Ignore
    @Override
    public Category$impl<java.lang.Object> 
    $ceylon$language$Category$impl(){
        return $ceylon$language$Category$this;
    }

    @Ignore
    @Override
    public Iterable$impl<Character,java.lang.Object> 
    $ceylon$language$Iterable$impl(){
        return $ceylon$language$Iterable$this;
    }

    @Ignore
    @Override
    public Collection$impl<Character> 
    $ceylon$language$Collection$impl(){
        return $ceylon$language$Collection$this;
    }

    @Ignore
    @Override
    public List$impl<Character> 
    $ceylon$language$List$impl(){
        return $ceylon$language$List$this;
    }

    @Ignore
    @Override
    public Correspondence$impl<Integer,Character> 
    $ceylon$language$Correspondence$impl(){
        return $ceylon$language$Correspondence$this;
    }

    @Ignore
    @Override
    public Comparable$impl<String> 
    $ceylon$language$Comparable$impl(){
        return $ceylon$language$Comparable$this;
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
    public static ceylon.language.String instance(java.lang.String... strings) {
        StringBuffer buf = new StringBuffer();
        for (java.lang.String s: strings)
            buf.append(s);
        return instance(buf.toString());
    }

    @Ignore
    public static ceylon.language.String instance(String... strings) {
        StringBuffer buf = new StringBuffer();
        for (String s: strings)
            buf.append(s.value);
        return instance(buf.toString());
    }

    public java.lang.String getUppercased() {
        return value.toUpperCase();
    }

    @Ignore
    public static java.lang.String getUppercased(java.lang.String value) {
        return value.toUpperCase();
    }

    public java.lang.String getLowercased() {
        return value.toLowerCase();
    }

    @Ignore
    public static java.lang.String getLowercased(java.lang.String value) {
        return value.toLowerCase();
    }

    @Override
    public boolean equals(@Name("that") java.lang.Object that) {
        if (that instanceof String) {
            String s = (String)that;
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
            String s = (String)that;
            return value.equals(s.value);
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Ignore
    public static int hashCode(java.lang.String value) {
        return value.hashCode();
    }

    @Override
    public Comparison compare(@Name("other") String other) {
        int c = value.compareTo(other.value);
        return (c < 0) ? smaller_.get_() :
            ((c == 0) ? equal_.get_() : larger_.get_());
    }

    @Ignore
    public static Comparison compare(java.lang.String value, 
    		java.lang.String otherValue) {
        int c = value.compareTo(otherValue);
        return (c < 0) ? smaller_.get_() :
            ((c == 0) ? equal_.get_() : larger_.get_());
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

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language::Character")
    public Character get(@Name("index") Integer key) {
        return getFromFirst(value, key.longValue());
    }

    @Ignore
    public static Character getFromLast(java.lang.String value, long key) {
        return getFromFirst(value, value.length()-1-key);
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language::Character")
    public Character getFromLast(@Name("index") long key) {
        return getFromFirst(value, value.length()-1-key);
    }

    @Ignore
    public static Character get(java.lang.String value, long key) {
        return getFromFirst(value, key);
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language::Character")
    public Character getFromFirst(@Name("index") long key) {
        return getFromFirst(value, key);
    }

    @Ignore
    public static Character getFromFirst(java.lang.String value, long key) {
        int index = Util.toInt(key);
        int length = value.length();
        if (index < 0 || index >= length) {
            return null;
        }
        int offset = value.offsetByCodePoints(0, index);
        int codePoint = value.codePointAt(offset);
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
            return new Range<Integer>(Integer.$TypeDescriptor$, 
                    Integer.instance(0), Integer.instance(size-1));
        }
    }

    @Override
    @Ignore
    public boolean definesEvery(@Name("keys")
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Integer,ceylon.language::Null>")
    Iterable<? extends Integer,?> keys) {
        //TODO: inefficient ... better to cache the result
        //      of getSize()
        return $ceylon$language$Correspondence$this.definesEvery(keys);
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

    @Override
    @Ignore
    public boolean definesAny(@Name("keys")
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Integer,ceylon.language::Null>")
    Iterable<? extends Integer,?> keys) {
        //TODO: inefficient ... better to cache the result
        //      of getSize()
        return $ceylon$language$Correspondence$this.definesAny(keys);
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

    @Override
    @Ignore
    public Sequential<? extends Character> items(@Name("keys")
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Integer,ceylon.language::Null>")
    Iterable<? extends Integer,?> keys) {
        return $ceylon$language$Correspondence$this.items(keys);
    }

    @Ignore
    public static Sequential<? extends Character> 
    items(java.lang.String value, 
    		Sequential<? extends Integer> keys) {
        // TODO We're still boxing here!
        return instance(value).items(keys);
    }

    @Ignore 
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Sequential<? extends Character> 
    items(java.lang.String value) {
        return (Sequential) empty_.get_();
    }

    @Ignore
    public static boolean occurs(java.lang.String value, 
    		java.lang.Object element) {
        if (element instanceof Character) {
            int index = value.indexOf(((Character) element).codePoint);
            return index>=0;
        }
        else {
            return instance(value).occurs(element);
        }
    }
    
    @Override
    @Ignore
    public boolean occurs(java.lang.Object element) {
        if (element instanceof Character) {
            int index = value.indexOf(((Character) element).codePoint);
            return index>=0;
        }
        else {
            return $ceylon$language$List$this.occurs(element);
        }
    }
    
    @Ignore
    public static boolean occursAt(java.lang.String value, 
    		long index, java.lang.Object element) {
        if (element instanceof Character) {
            int cp = ((Character) element).codePoint;
            Character ch = get(value,index);
            return ch!=null && ch.codePoint==cp;
        }
        else {
            return instance(value).occursAt(index, element);
        }
    }
    
    @Override
    @Ignore
    public boolean occursAt(long index, java.lang.Object element) {
        if (element instanceof Character) {
            int cp = ((Character) element).codePoint;
            Character ch = get(value,index);
            return ch!=null && ch.codePoint==cp;
        }
        else {
            return $ceylon$language$List$this.occursAt(index, element);
        }
    }
    
    @Ignore
    public static boolean includesAt(java.lang.String value, 
    		long index, List<?> sublist) {
        if (sublist instanceof String) {
            java.lang.String str = ((String) sublist).value;
            int offset = value.offsetByCodePoints(0, Util.toInt(index));
            return value.regionMatches(offset, str, 0, str.length());
        }
        else {
            return instance(value).includesAt(index, sublist);
        }
    }
    
    @Override
    @Ignore
    public boolean includesAt(long index, List<?> sublist) {
        if (sublist instanceof String) {
            java.lang.String str = ((String) sublist).value;
            int offset = value.offsetByCodePoints(0, Util.toInt(index));
            return value.regionMatches(offset, str, 0, str.length());
        }
        else {
            return $ceylon$language$List$this.includesAt(index, sublist);
        }
    }
    
    @Override
    @Ignore
    public Iterable<? extends Integer, ?> 
    inclusions(List<?> sublist) {
        if (sublist instanceof String) {
            return new StringInclusions(value, ((String) sublist).value);
        }
        else {
            return $ceylon$language$List$this.inclusions(sublist);
        }
    }

    @Ignore
    public static Iterable<? extends Integer, ?> 
    inclusions(java.lang.String value, List<?> substring) {
        if (substring instanceof String) {
            return new StringInclusions(value, ((String) substring).value);
        }
        else {
            return instance(value).occurrences(substring);
        }
    }

    @Override
    @Ignore
    public Iterable<? extends Integer, ?> 
    occurrences(java.lang.Object element) {
        return $ceylon$language$List$this.occurrences(element);
    }

    @Ignore
    public static boolean includes(java.lang.String value, 
    		List<?> sublist) {
        if (sublist instanceof String) {
            int index = value.indexOf(((String) sublist).value);
            return index >= 0;
        }
        else {
            return instance(value).includes(sublist);
        }
    }

    @Override
    @Ignore
    public boolean includes(List<?> sublist) {
        if (sublist instanceof String) {
            int index = value.indexOf(((String) sublist).value);
            return index >= 0;
        }
        else {
            return $ceylon$language$List$this.includes(sublist);
        }
    }
    
    @Ignore
    public static Integer firstInclusion(java.lang.String value, 
    		List<?> sublist) {
        if (sublist instanceof String) {
            int index = value.indexOf(((String) sublist).value);
            return index >= 0 ? 
            		Integer.instance(value.codePointCount(0, index)) : 
            			null;
        }
        else {
            return instance(value).firstInclusion(sublist);
        }
    }

    @Override
    @Ignore
    public Integer firstInclusion(List<?> sublist) {
        if (sublist instanceof String) {
            int index = value.indexOf(((String) sublist).value);
            return index >= 0 ? 
            		Integer.instance(value.codePointCount(0, index)) : 
            			null;
        }
        else {
            return $ceylon$language$List$this.firstInclusion(sublist);
        }
    }
    
    @Ignore
    public static Integer lastInclusion(java.lang.String value, 
    		List<?> sublist) {
        if (sublist instanceof String) {
            int index = value.lastIndexOf(((String) sublist).value);
            return index >= 0 ? 
            		Integer.instance(value.codePointCount(0, index)) : 
            			null;
        }
        else {
            return instance(value).lastInclusion(sublist);
        }
    }

    @Override
    @Ignore
    public Integer lastInclusion(List<?> sublist) {
        return $ceylon$language$List$this.lastInclusion(sublist);
    }
    
    @Ignore
    public static Integer firstOccurrence(java.lang.String value, 
    		java.lang.Object element) {
        if (element instanceof Character) {
            int index = value.indexOf(((Character) element).codePoint);
            return index >= 0 ? 
            		Integer.instance(value.codePointCount(0, index)) : 
            			null;
        }
        else {
            return null;
        }
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language::Integer")
    public Integer firstOccurrence(
            @Name("element") 
            @TypeInfo("ceylon.language::Anything")
            java.lang.Object element) {
        if (element instanceof Character) {
            int index = value.indexOf(((Character) element).codePoint);
            return index >= 0 ? 
            		Integer.instance(value.codePointCount(0, index)) : 
            			null;
        }
        else {
            return null;
        }
    }
    
    @Ignore
    public static Integer lastOccurrence(java.lang.String value, 
    		java.lang.Object element) {
        if (element instanceof Character) {
            int index = value.lastIndexOf(((Character) element).codePoint);
            return index >= 0 ? 
            		Integer.instance(value.codePointCount(0, index)) : 
            			null;
        }
        else {
            return null;
        }
    }

    @Override
    @TypeInfo("ceylon.language::Null|ceylon.language::Integer")
    public Integer lastOccurrence(
            @Name("element") 
            @TypeInfo("ceylon.language::Anything")
            java.lang.Object element) {
        if (element instanceof Character) {
            int index = value.lastIndexOf(((Character) element).codePoint);
            return index >= 0 ? 
            		Integer.instance(value.codePointCount(0, index)) : 
            			null;
        }
        else {
            return null;
        }
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
            extends AbstractIterator<Character> 
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
            } else {
                return finished_.get_();
            }
        }
        
        @Override
        @Ignore
        public TypeDescriptor $getType$() {
			return $TypeDescriptor$;
        }
    }
    
    @Ignore
    public static Iterable<? extends Character,?> 
    getCharacters(java.lang.String value) {
        return instance(value);
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
            return $ceylon$language$List$this.startsWith(substring);
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
            return $ceylon$language$List$this.endsWith(substring);
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

    @Override
    @Ignore
    public boolean containsAny(Iterable<?,?> elements) {
        return $ceylon$language$Category$this.containsAny(elements);
    }

    @Ignore
    public static boolean containsAny(java.lang.String value, 
    		Iterable<?,?> elements) {
        // TODO We're still boxing here!
        return instance(value).containsAny(elements);
    }

    @Override
    @Ignore
    public boolean containsEvery(Iterable<?,?> elements) {
        return $ceylon$language$Category$this.containsEvery(elements);
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

    @TypeInfo("ceylon.language::String")
    @Transient
    public java.lang.String getTrimmed() {
        return getTrimmed(value);
    }

    @Ignore
    public static java.lang.String getTrimmed(java.lang.String value) {
        // Don't use value.trim() because that has a definition of ws that is 
        // inconsistent with ceylon.language::Character.whitespace
        return internalTrim(value, 
        		new AbstractCallable<Boolean>(Boolean.$TypeDescriptor$, 
        				TypeDescriptor.klass(Tuple.class, Character.$TypeDescriptor$, 
        						Character.$TypeDescriptor$, Empty.$TypeDescriptor$),
        						"whitespace", (short)-1) {
            public Boolean $call$(java.lang.Object c) {
                return Boolean.instance(((Character) c).getWhitespace());
            }
        });
    }
    
    @Override
    @TypeInfo("ceylon.language::String")
    public String trimLeading(
            @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Character,ceylon.language::Character,ceylon.language::Empty>>")
            @Name("trimming")@FunctionalParameter("(elem)")
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
    @TypeInfo("ceylon.language::String")
    public String trimTrailing(
            @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Character,ceylon.language::Character,ceylon.language::Empty>>")
            @Name("trimming")@FunctionalParameter("(elem)")
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
    @TypeInfo("ceylon.language::String")
    public String trim(
            @TypeInfo("ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Character,ceylon.language::Character,ceylon.language::Empty>>")
            @Name("trimming")@FunctionalParameter("(elem)")
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

    @TypeInfo("ceylon.language::String")
    public java.lang.String getNormalized() {
        return getNormalized(value);
    }

    @Ignore
    public static java.lang.String getNormalized(java.lang.String value) {
        java.lang.StringBuilder result = new java.lang.StringBuilder();
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
    @TypeInfo("ceylon.language::String")
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
    @TypeInfo("ceylon.language::String")
    public String terminal(@TypeInfo("ceylon.language::Integer")
    @Name("length") long length) {
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

    @TypeInfo("ceylon.language::String")
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
            while (!((elem = it.next()) instanceof Finished)) {
                result.append(value).append(elem);
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
    @TypeInfo("ceylon.language::String")
    public String segment(@Name("from") final Integer from,
            @Name("length") final long length) {
        return instance(segment(value, from.longValue(), length));
    }

    @Ignore
    public static java.lang.String segment(java.lang.String value, 
    		final long from, final long length) {
        long fromIndex = from;
        long resultLength = length;
        long len = getSize(value);
        if (fromIndex >= len || resultLength <= 0) {
            return "";
        }
        if ((fromIndex + resultLength) > len) {
            resultLength = len - fromIndex;
        }
        int start = value.offsetByCodePoints(0, Util.toInt(fromIndex));
        int end = value.offsetByCodePoints(start, Util.toInt(resultLength));
        return value.substring(start, end);
    }

    @Override
    @TypeInfo("ceylon.language::String")
    public String span(@Name("from") final Integer from,
            @Name("to") final Integer to) {
        return instance(span(value, from.longValue(), to.longValue()));
    }

    @Override
    @TypeInfo("ceylon.language::String")
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
        java.lang.String result = value.substring(start, end);
        return result;
    }
    
    @Override
    @TypeInfo("ceylon.language::String")
    public String spanTo(@Name("to")  final Integer to) {
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
        java.lang.String result = value.substring(start, end);
        return result;
    }
    

    @Ignore
    public static java.lang.String span(java.lang.String value, 
    		long from, long toIndex) {
        long len = getSize(value);
        if (len == 0) {
            return "";
        }
        boolean reverse = toIndex < from;
        if (reverse) {
            long _tmp = toIndex;
            toIndex = from;
            from = _tmp;
        }
    	if (toIndex < 0 || from >= len) {
    		return "";
    	}
    	if (toIndex >= len) {
    		toIndex = len - 1;
    	}
    	if (from < 0) {
    		from = 0;
    	}
        int start = value.offsetByCodePoints(0, Util.toInt(from));
        int end = value.offsetByCodePoints(start, 
                Util.toInt(toIndex - from + 1));
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
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        int offset = value.length();
        while (offset > 0) {
            int c = value.codePointBefore(offset);
            builder.appendCodePoint(c);
            offset -= java.lang.Character.charCount(c);
        }
        return builder.toString();
    }

    @TypeInfo("ceylon.language::String")
    @Override
    public String repeat(
            @TypeInfo("ceylon.language::Integer")
            @Name("times") long times) {
        return instance(repeat(value, times));
    }

    @Ignore
    public static java.lang.String repeat(java.lang.String value, 
    		long times) {
        int len = value.length();
        if (times<=0 || len==0) return "";
        if (times==1) return value;
        java.lang.StringBuilder builder = new java.lang.StringBuilder(Util.toInt(len*times));
        for (int i=0; i<times; i++) {
            builder.append(value);
        }
        return builder.toString();
    }

    @TypeInfo("ceylon.language::String")
    public java.lang.String replace(
            @Name("substring") java.lang.String substring,
            @Name("replacement") java.lang.String replacement) {
        return replace(value, substring, replacement);
    }

    @Ignore
    public static java.lang.String replace(java.lang.String value, 
    		java.lang.String substring, java.lang.String replacement) {
    	int index = value.indexOf(substring);
    	while (index>=0) {
            value = value.substring(0,index) + replacement + 
    				value.substring(index+substring.length());
            index = value.indexOf(substring, 
            		index+replacement.length());
    	}
		return value;
    }

    @TypeInfo("ceylon.language::String")
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

    @TypeInfo("ceylon.language::Iterable<ceylon.language::String>")
    public Iterable<? extends String, ?> split(
            @TypeInfo(value="ceylon.language::Callable<ceylon.language::Boolean,ceylon.language::Tuple<ceylon.language::Character,ceylon.language::Character,ceylon.language::Empty>>")
            @Defaulted
            
            @Name("splitting")@FunctionalParameter("(ch)") Callable<? extends Boolean> splitting,
            @Defaulted
            @Name("discardSeparators") boolean discardSeparators,
            @Defaulted
            @Name("groupSeparators") boolean groupSeparators) {
        if (value.isEmpty()) {
            return new Singleton<String>(String.$TypeDescriptor$, this);
        }
        return new StringTokens(value, splitting, !discardSeparators, groupSeparators);
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
        return new StringTokens(value, splitting, !discardSeparators, 
        		groupSeparators);
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
        return new AbstractCallable<Boolean>(Boolean.$TypeDescriptor$, 
                TypeDescriptor.klass(Tuple.class, Character.$TypeDescriptor$, 
                		Character.$TypeDescriptor$, Empty.$TypeDescriptor$),
                "whitespace", (short)-1) {
            @Override
            public Boolean $call$(java.lang.Object ch) {
                return Boolean.instance(((Character) ch).getWhitespace());
            }
        };
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
    
    @TypeInfo("ceylon.language::Tuple<ceylon.language::String,ceylon.language::String,ceylon.language::Tuple<ceylon.language::String,ceylon.language::String,ceylon.language::Empty>>")
    public
    Tuple<String,? extends String,
    		? extends Tuple<String,? extends String,
    				? extends Sequential<? extends String>>>
    slice(@Name("index") long index) {
    	return slice(value,index);
    }
    
    @Ignore
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static
    Tuple<String,? extends String,
    		? extends Tuple<String,? extends String,
    				? extends Sequential<? extends String>>>
    slice(java.lang.String value, long index) {
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
    		int intIndex = Util.toInt(index);
            first = value.substring(0,intIndex);
    		second = value.substring(intIndex);
    	}
    	return new Tuple(String.$TypeDescriptor$, 
    			new String[] { instance(first), 
    			        instance(second) });
    }
    
    @TypeInfo("ceylon.language::Iterable<ceylon.language::String>")
    @Transient
    public Iterable<? extends String, ?> getLines() {
        return split(new AbstractCallable<Boolean>(Boolean.$TypeDescriptor$, 
                TypeDescriptor.klass(Tuple.class, Character.$TypeDescriptor$, 
                		Character.$TypeDescriptor$, Empty.$TypeDescriptor$),
                "whitespace", (short)-1) {
            @Override
            public Boolean $call$(java.lang.Object ch) {
                return Boolean.instance(((Character) ch).toString().equals("\n"));
            }
        }, true);
    }

    @Ignore
    public static Iterable<? extends String, ?> 
    getLines(java.lang.String value) {
        return split(value, new AbstractCallable<Boolean>(Boolean.$TypeDescriptor$, 
                TypeDescriptor.klass(Tuple.class, Character.$TypeDescriptor$, 
                		Character.$TypeDescriptor$, Empty.$TypeDescriptor$),
                "whitespace", (short)-1) {
            @Override
            public Boolean $call$(java.lang.Object ch) {
                return Boolean.instance(((Character) ch).toString().equals("\n"));
            }
        }, true);
    }

    @Ignore
    public static Iterable<? extends Integer, ?> 
    occurrences(java.lang.String value, java.lang.Object element) {
        return instance(value).occurrences(element);
    }

    @Override
    public String $clone() {
        return this;
    }

    @Ignore
    public static java.lang.String $clone(java.lang.String value) {
        return value;
    }

    @Override @Ignore
    public <Result> Iterable<? extends Result, ?> 
    map(@Ignore TypeDescriptor $reifiedResult, Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.map($reifiedResult, f);
    }

    @Ignore
    public static <Result> Iterable<? extends Result, ?> 
    map(@Ignore TypeDescriptor $reifiedResult, java.lang.String value, 
    		Callable<? extends Result> f) {
        return instance(value).map($reifiedResult, f);
    }

    @Override @Ignore
    public Sequential<? extends Character> 
    select(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.select(f);
    }
    @Ignore
    public static Sequential<? extends Character> 
    select(java.lang.String value, 
    		Callable<? extends Boolean> f) {
        return instance(value).select(f);
    }

    @TypeInfo("ceylon.language::String")
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

    @Override
    @Ignore
    public List<? extends Character> sublistFrom(long index) {
        return $ceylon$language$List$this.sublistFrom(index);
    }
    
    @Override
    @Ignore
    public List<? extends Character> sublistTo(long index) {
        return $ceylon$language$List$this.sublistTo(index);
    }
    
    @Override
    @Ignore
    public List<? extends Character> sublist(long from, long to) {
        return $ceylon$language$List$this.sublist(from, to);
    }
    
    @Ignore
    public static java.lang.String getRest(java.lang.String value) {
        return value.isEmpty() ? "" :
        	value.substring(value.offsetByCodePoints(0, 1));
    }

    @Override
    @Ignore
    public Character getFirst() {
        return getFromFirst(0);
    }

    @Ignore
    public static Character getFirst(java.lang.String value) {
        if (value.isEmpty()) {
            return null;
        } else {
            return get(value, 0);
        }
    }

    @Override @Ignore
    public Character getLast() {
        long length = getSize();
        if (length==0) {
            return null;
        }
        else {
            return getFromFirst(length-1);
        }
    }

    @Ignore
    public static Character getLast(java.lang.String value) {
        if (value.isEmpty()) {
            return null;
        } else {
            return get(value, getLastIndex(value).longValue());
        }
    }

    @Override
    @Ignore
    public Sequential<? extends Character> sequence() {
        return (Sequential<? extends Character>)
        		$ceylon$language$Iterable$this.sequence();
    }

    @Ignore
    public static Sequential<? extends Character> 
    sequence(java.lang.String value) {
        return instance(value).sequence();
    }

    @Override @Ignore
    public Character find(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.find(f);
    }

    @Ignore
    public static Character find(java.lang.String value, 
    		Callable<? extends Boolean> f) {
        if (value.isEmpty()) {
            return null;
        } else {
            return instance(value).find(f);
        }
    }

    @Override @Ignore
    public Character findLast(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.findLast(f);
    }

    @Ignore
    public static Character findLast(java.lang.String value, 
    		Callable<? extends Boolean> f) {
        if (value.isEmpty()) {
            return null;
        } else {
            return instance(value).findLast(f);
        }
    }

    @Override
    @Ignore
    public Sequential<? extends Character> 
    sort(Callable<? extends Comparison> f) {
        return $ceylon$language$Iterable$this.sort(f);
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

    @Override
    @Ignore
    public Iterable<? extends Integer, ?> 
    indexesWhere(Callable<? extends Boolean> f) {
        return $ceylon$language$List$this.indexesWhere(f);
    }
    
    @Ignore
    public static Iterable<? extends Integer, ?> 
    indexesWhere(java.lang.String value, Callable<? extends Boolean> f) {
        return instance(value).indexesWhere(f);
    }
    
    @Override
    @Ignore
    public Integer 
    firstIndexWhere(Callable<? extends Boolean> f) {
        return $ceylon$language$List$this.firstIndexWhere(f);
    }
    
    @Ignore
    public static Integer 
    firstIndexWhere(java.lang.String value, Callable<? extends Boolean> f) {
        return instance(value).firstIndexWhere(f);
    }
    
    @Override
    @Ignore
    public Integer 
    lastIndexWhere(Callable<? extends Boolean> f) {
        return $ceylon$language$List$this.lastIndexWhere(f);
    }
    
    @Ignore
    public static Integer 
    lastIndexWhere(java.lang.String value, Callable<? extends Boolean> f) {
        return instance(value).lastIndexWhere(f);
    }
    
    @Override
    @Ignore
    public Iterable<? extends Character, ?> 
    filter(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.filter(f);
    }
    
    @Ignore
    public static Iterable<? extends Character, ?> 
    filter(java.lang.String value, Callable<? extends Boolean> f) {
        return instance(value).filter(f);
    }
    
    @Override @Ignore
    public <Result> Sequential<? extends Result> 
    collect(@Ignore TypeDescriptor $reifiedResult, 
    		Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.collect($reifiedResult, f);
    }

    @Ignore
    public static <Result> Sequential<? extends Result> 
    collect(@Ignore TypeDescriptor $reifiedResult, 
    		java.lang.String value, Callable<? extends Result> f) {
        return instance(value).collect($reifiedResult, f);
    }

    @Override
    @Ignore
    public <Result> Result fold(@Ignore TypeDescriptor $reifiedResult, 
    		Result ini, Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.fold($reifiedResult, ini, f);
    }
    @Ignore
    public static <Result> Result fold(@Ignore TypeDescriptor $reifiedResult, 
    		java.lang.String value, Result ini, Callable<? extends Result> f) {
        if (value.isEmpty()) {
            return ini;
        } else {
            return instance(value).fold($reifiedResult, ini, f);
        }
    }

    @Override
    @Ignore
    public <Result> java.lang.Object 
    reduce(@Ignore TypeDescriptor $reifiedResult, 
    		Callable<? extends Result> f) {
        return $ceylon$language$Iterable$this.reduce($reifiedResult, f);
    }
    @Ignore
    public static <Result> java.lang.Object 
    reduce(@Ignore TypeDescriptor $reifiedResult, 
    		java.lang.String value, Callable<? extends Result> f) {
        if (value.isEmpty()) {
            return null;
        } else {
            return instance(value).reduce($reifiedResult, f);
        }
    }

    @Override @Ignore
    public boolean any(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.any(f);
    }
    @Ignore
    public static boolean any(java.lang.String value, 
    		Callable<? extends Boolean> f) {
        if (value.isEmpty()) {
            return false;
        } else {
            return instance(value).any(f);
        }
    }

    @Override @Ignore
    public boolean every(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.every(f);
    }
    
    @Ignore
    public static boolean every(java.lang.String value, 
    		Callable<? extends Boolean> f) {
        if (value.isEmpty()) {
            return false;
        } else {
            return instance(value).every(f);
        }
    }

    @Override
    public String 
    skip(@Name("skipping") long skip) {
        return instance(this.segment(Integer.instance(skip), this.getSize()));
    }
    
    @Ignore
    public static java.lang.String 
    skip(java.lang.String value, long skip) {
        if (value.isEmpty()) {
            return value;
        } else {
            return segment(value, skip, getSize(value));
        }
    }

    @Override 
    public String 
    take(@Name("taking") long take) {
        return instance(this.segment(Integer.instance(0), take));
    }
    
    @Ignore
    public static java.lang.String 
    take(java.lang.String value, long take) {
        if (value.isEmpty()) {
            return value;
        } else {
            return segment(value, 0, take);
        }
    }
    
    @Override @Ignore
    public Iterable<? extends Character, ?> 
    takeWhile(Callable<? extends Boolean> take) {
        return $ceylon$language$Iterable$this.takeWhile(take);
    }
    
    @Override @Ignore
    public Iterable<? extends Character, ?> 
    skipWhile(Callable<? extends Boolean> skip) {
        return $ceylon$language$Iterable$this.skipWhile(skip);
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
    
    @Override 
    public Iterable<? extends Character, ?> 
    by(@Name("step") long step) {
        return $ceylon$language$Iterable$this.by(step);
    }
    
    @Ignore
    public static Iterable<? extends Character, ?> 
    by(java.lang.String value, long step) {
        if (value.isEmpty()) {
            return instance(value);
        }
        else {
            return instance(new String(instance(value)
            		.by(step).sequence()));
        }
    }

    @Override @Ignore
    public long count(Callable<? extends Boolean> f) {
        return $ceylon$language$Iterable$this.count(f);
    }
    
    @Ignore
    public static long count(java.lang.String value, 
    		Callable<? extends Boolean> f) {
        if (value.isEmpty()) {
            return 0;
        } else {
            return instance(value).count(f);
        }
    }

    @Override @Ignore
    public Iterable<? extends Entry<? extends Integer, ? extends Character>, 
    		?> getIndexed() {
        return $ceylon$language$Iterable$this.getIndexed();
    }
    
    @Ignore
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Iterable<? extends Entry<? extends Integer, ? extends Character>, 
    		?> 
    getIndexed(java.lang.String value) {
        if (value.isEmpty()) {
            return (Iterable) instance(value);
        } else {
            return instance(value).getIndexed();
        }
    }

    @SuppressWarnings("rawtypes")
    @Override @Ignore 
    public <Other,Absent>Iterable 
    chain(@Ignore TypeDescriptor $reifiedOther, 
    		@Ignore TypeDescriptor $reifiedOtherAbsent, 
    		Iterable<? extends Other, ? extends Absent> other) {
        return $ceylon$language$Iterable$this.chain($reifiedOther, 
        		$reifiedOtherAbsent, other);
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
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override @Ignore 
    public <Other> Iterable 
    following(@Ignore TypeDescriptor $reifiedOther, 
    		Other other) {
        return $ceylon$language$Iterable$this.following($reifiedOther, 
        		other);
    }
    
    @Ignore
    @SuppressWarnings("rawtypes")
    public static <Other> Iterable 
    following(@Ignore TypeDescriptor $reifiedOther, 
    		java.lang.String value, Other other) {
        return instance(value).following($reifiedOther, other);    
    }
    
    @Override @Ignore
    @SuppressWarnings("rawtypes")
    public <Other> Sequence withLeading(@Ignore TypeDescriptor $reifiedOther, Other e) {
        return $ceylon$language$List$this.withLeading($reifiedOther, e);
    }

    @Ignore @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <Other>Sequence withLeading(@Ignore TypeDescriptor $reifiedOther, 
    		java.lang.String value, Other e) {
        if (value.isEmpty()) {
            return new Singleton($reifiedOther, e);
        } else {
            return instance(value).withLeading($reifiedOther, e);
        }
    }

    @Override @Ignore
    @SuppressWarnings("rawtypes")
    public <Other> Sequence withTrailing(@Ignore TypeDescriptor $reifiedOther, Other e) {
        return $ceylon$language$List$this.withTrailing($reifiedOther, e);
    }
    @Ignore @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <Other>Sequence withTrailing(@Ignore TypeDescriptor $reifiedOther, 
    		java.lang.String value, Other e) {
        if (value.isEmpty()) {
            return new Singleton($reifiedOther, e);
        } else {
            return instance(value).withTrailing($reifiedOther, e);
        }
    }

    @Override
    @Ignore
    public Iterable<? extends Character,?> getCycled() {
        return $ceylon$language$Iterable$this.getCycled();
    }
    
    @Ignore
    public static Iterable<? extends Character,?> 
    getCycled(java.lang.String value) {
        return instance(value).getCycled();
    }
    
    @Override
    @Ignore
    public Iterable<? extends Character,?> cycle(long times) {
        return $ceylon$language$Iterable$this.cycle(times);
    }
    
    @Ignore
    public static Iterable<? extends Character,?> 
    cycle(java.lang.String value, long times) {
        return instance(value).cycle(times);
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

}
