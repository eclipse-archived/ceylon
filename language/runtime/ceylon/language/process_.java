package ceylon.language;

import java.io.IOException;
import java.util.Locale;
import java.util.TimeZone;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Object;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 5) @Object
public final class process_ {
	
    java.lang.String newline = System.lineSeparator();

    /*@Ignore
    private static final class PropertiesMap implements Map<String, String> {
        
        private Properties props;
        
        private PropertiesMap(Properties props) {
            this.props = props;
        }

        @Override
        public String get(java.lang.Object key) {
            if (key instanceof String) {
                return String.instance(props.getProperty(((String)key).value));
            }
            else {
                return null;
            }
        }

        @Override
        public boolean defines(java.lang.Object key) {
            if (key instanceof String) {
                return props.containsKey(((String)key).value);
            }
            else {
                return false;
            }
        }

        @Override
        public boolean definesEvery(Iterable<? extends java.lang.Object> keys) {
            return Correspondence$impl._definesEvery(this, keys);
        }

        @Override
        public boolean definesEvery() {
            return Correspondence$impl._definesEvery(this, $empty.getEmpty());
        }

        @Override
        public Iterable<? extends java.lang.Object> definesEvery$keys() {
            return $empty.getEmpty();
        }

        @Override
        public boolean definesAny(Iterable<? extends java.lang.Object> keys) {
            return Correspondence$impl._definesAny(this, keys);
        }

        @Override
        public boolean definesAny() {
            return Correspondence$impl._definesAny(this, $empty.getEmpty());
        }

        @Override
        public Iterable<? extends java.lang.Object> definesAny$keys() {
            return $empty.getEmpty();
        }

        @Override
        public Iterable<? extends String, ? extends java.lang.Object> items(
                Iterable<? extends java.lang.Object> keys) {
            return Correspondence$impl._items(this, keys);
        }

        @Override
        public Iterable<? extends String, ? extends java.lang.Object> items() {
            return Correspondence$impl._items(this, $empty.getEmpty());
        }

        @Override
        public Iterable<? extends java.lang.Object> items$keys() {
            return $empty.getEmpty();
        }

        @Override
        public boolean getEmpty() {
            return props.isEmpty();
        }

        @Override
        public boolean contains(java.lang.Object element) {
            if (element instanceof Entry) {
                Entry entry = (Entry) element;
                String value = get(entry.getKey());
                return value!=null && value.equals(entry.getItem());
            }
            return false;
        }

        @Override
        public Iterator<? extends Entry<? extends String, ? extends String>> getIterator() {
            return new Iterator<Entry<? extends String,? extends String>>() {
                java.util.Iterator<java.util.Map.Entry<java.lang.Object, java.lang.Object>> iter = props.entrySet().iterator();
                @Override
                public java.lang.Object next() {
                    if (iter.hasNext()) {
                        java.util.Map.Entry<java.lang.Object, java.lang.Object> entry = iter.next();
                        return new Entry(String.instance((java.lang.String) entry.getKey()),
                                String.instance((java.lang.String) entry.getValue()));
                    }
                    else {
                        return exhausted.getExhausted();
                    }
                }
            };
        }

        @Override
        public long getSize() {
            return props.size();
        }

        @Override
        public boolean containsEvery(Iterable<?> elements) {
            return Category$impl._containsEvery(this, elements);
        }

        @Override
        public boolean containsEvery() {
            return Category$impl._containsEvery(this, $empty.getEmpty());
        }

        @Override
        public Iterable<?> containsEvery$elements() {
            return $empty.getEmpty();
        }

        @Override
        public boolean containsAny(Iterable<?> elements) {
            return Category$impl._containsAny(this, elements);
        }

        @Override
        public boolean containsAny() {
            return Category$impl._containsAny(this, $empty.getEmpty());
        }

        @Override
        public Iterable<?> containsAny$elements() {
            return $empty.getEmpty();
        }

        @Override
        public Collection<? extends Entry<? extends String, ? extends String>> getClone() {
            return this;
        }

        @Override
        public long count(java.lang.Object element) {
            return contains(element) ? 1 : 0;
        }

        @Override
        public Set<? extends String> getKeys() {
            return Map$impl._getKeys(this);
        }

        @Override
        public Collection<? extends String> getValues() {
            return Map$impl._getValues(this);
        }

        @Override
        public Map<? extends String, ? extends Set<? extends String>> getInverse() {
            return Map$impl._getInverse(this);
        }
        
        @Override
        public java.lang.String toString() {
            return props.toString();
        }
    }*/

    @SuppressWarnings("unchecked")
    private Sequential<? extends String> args = (Sequential)empty_.get_();
    
    @TypeInfo("ceylon.language::Sequential<ceylon.language::String>")
    public Sequential<? extends String> getArguments() {
        return args;
    }
    
    @Ignore
    public void setupArguments(java.lang.String[] args) {
    	if (args.length>0) {
	        java.lang.Object[] newArgs = new java.lang.Object[args.length];
	        for (int i = 0; i < args.length; i++) {
	            newArgs[i] = String.instance(args[i]);
	        }
	        this.args = ArraySequence.<String>instance(String.$TypeDescriptor, newArgs);
    	}
    }
    
//    shared Entries<String,String> switches { throw; }

//    shared Entries<String,String> properties { throw; }

    public void writeLine(@Name("line") java.lang.String s) {
        java.lang.System.out.println(s);
    }
    
    public void write(@Name("string") java.lang.String s) {
        java.lang.System.out.print(s);
    }
    
    public void flush() {
        java.lang.System.out.flush();
    }
    
    public void writeErrorLine(@Name("line") java.lang.String s) {
        java.lang.System.err.println(s);
    }
    
    public void writeError(@Name("string") java.lang.String s) {
        java.lang.System.err.print(s);
    }
    
    public void flushError() {
        java.lang.System.err.flush();
    }
    
    public java.lang.String readLine() {
        try {
            return new java.io.BufferedReader( 
                    new java.io.InputStreamReader(java.lang.System.in))
                .readLine();
        } 
        catch (IOException e) {
            throw new Exception(String.instance("could not read line from standard input"), e);
        }
    }
    
    public long getMilliseconds() {
    	return System.currentTimeMillis();
    }
    
    public long getNanoseconds() {
        return System.nanoTime();
    }
    
    public void exit(long code) {
    	System.exit((int) code);
    }
    
    /*@TypeInfo("ceylon.language::Map<ceylon.language::String, ceylon.language::String>")
    public Map<? extends String, ? extends String> getProperties() {
        return new PropertiesMap(System.getProperties());
    }*/
    
    /*@TypeInfo("ceylon.language::Map<ceylon.language::String, ceylon.language::String>")
    public Map<? extends String, ? extends String> getNamedArguments() {
        Properties props = new Properties();
        Iterator<? extends String> iterator = args.getIterator();
        java.lang.Object next;
        while ((next = iterator.next()) instanceof String) {
            java.lang.String arg = ((String) next).value;
            if (arg.startsWith("-")) {
                java.lang.String name;
                java.lang.String value; 
                int i = arg.indexOf('=');
                if (i>0) {
                    name = arg.substring(1, i);
                    value = arg.substring(i+1);
                }
                else {
                    name = arg.substring(1);
                    value = "";
                }
                props.setProperty(name, value);
            }
        }
        return new PropertiesMap(props);
    }*/
    
    @TypeInfo("ceylon.language::Null|ceylon.language::String")
    public String namedArgumentValue(@Name("name") java.lang.String name) {
        if (name.isEmpty()) return null;
        Iterator<? extends String> iterator = args.iterator();
        java.lang.Object next;
        while ((next = iterator.next()) instanceof String) {
            java.lang.String arg = ((String) next).value;
            if (arg.startsWith("-" + name + "=") || 
                    arg.startsWith("--" + name + "=")) {
                return String.instance(arg.substring(arg.indexOf('=')+1));
            }
            if (arg.equals("-" + name) || 
                    arg.equals("--" + name)) {
                java.lang.Object val = iterator.next();
                if (val instanceof String) {
                    java.lang.String result = ((String) val).value;
                    return String.instance(result.startsWith("-") ? null : result);
                }
            }
        }
        return null;
    }
    
    public boolean namedArgumentPresent(@Name("name") java.lang.String name) {
        if (name.isEmpty()) return false;
        Iterator<? extends String> iterator = args.iterator();
        java.lang.Object next;
        while ((next = iterator.next()) instanceof String) {
            java.lang.String arg = ((String) next).value;
            if (arg.startsWith("-" + name + "=") || 
                    arg.startsWith("--" + name + "=") || 
                    arg.equals("-" + name) || 
                    arg.equals("--" + name)) {
                return true;
            }
        }
        return false;
    }
    
    @TypeInfo("ceylon.language::Null|ceylon.language::String")
    public String propertyValue(@Name("name") java.lang.String name) {
        if (name.isEmpty()) {
            return null;
        }
        else {
            java.lang.String property = System.getProperty(name);
            return property==null ? null : String.instance(property);
        }
    }
    
    public java.lang.String getNewline() {
        return newline;
    }
    
    public java.lang.String getVm() {
        return "jvm";
    }
    
    public java.lang.String getVmVersion() {
        return System.getProperty("java.specification.version");
    }
    
    public java.lang.String getOs() {
        java.lang.String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("win") >= 0) {
            return "windows";
        } else if (os.indexOf("mac") >= 0) {
            return "mac";
        } else if (os.indexOf("linux") >= 0) {
            return "linux";
        } else if (os.indexOf("nix") >= 0 || os.indexOf("sunos") >= 0) {
            return "unix";
        } else {
            return "other";
        }
    }
    
    public java.lang.String getOsVersion() {
        return System.getProperty("os.version");
    }
    
    public java.lang.String getLocale() {
        return Locale.getDefault().toLanguageTag();
    }
    
    public int getTimezoneOffset() {
        return TimeZone.getDefault().getOffset(getMilliseconds());
    }
    
    @Override
    public java.lang.String toString() {
    	return "process";
    }
    
    private process_() {}
    private static final process_ value = new process_();
    
    public static process_ get_() {
        return value;
    }
}
