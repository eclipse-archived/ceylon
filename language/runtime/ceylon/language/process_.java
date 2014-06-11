package ceylon.language;

import java.io.IOException;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Object;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 7) @Object
public final class process_ {

    private java.io.BufferedReader stdinReader = null;
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    private Sequential<? extends String> args = (Sequential) empty_.get_();
    
    @TypeInfo("ceylon.language::Sequential<ceylon.language::String>")
    public Sequential<? extends String> getArguments() {
        return args;
    }
    
    @Ignore
    public void setupArguments(java.lang.String[] args) {
        if (args.length>0) {
            Array<String> newArgs = new Array<String>(String.$TypeDescriptor$, args.length, String.instance(""));
            for (int i = 0; i < args.length; i++) {
                newArgs.set(i, String.instance(args[i]));
            }
            this.args = new ArraySequence<String>(String.$TypeDescriptor$, newArgs);
        }
    }
    
    public void writeLine(@Name("line") @Defaulted java.lang.String line) {
        java.lang.System.out.println(line);
    }
    
    @Ignore
    public void writeLine() {
        writeLine(writeErrorLine$line());
    }
    
    @Ignore
    public static java.lang.String writeLine$line() {
        return "";
    }
    
    public void write(@Name("string") java.lang.String string) {
        java.lang.System.out.print(string);
    }
    
    public void flush() {
        java.lang.System.out.flush();
    }
    
    public void writeErrorLine(@Name("line") @Defaulted java.lang.String line) {
        java.lang.System.err.println(line);
    }
    
    @Ignore
    public void writeErrorLine() {
        writeErrorLine(writeErrorLine$line());
    }
    
    @Ignore
    public static java.lang.String writeErrorLine$line() {
        return "";
    }
    
    public void writeError(@Name("string") java.lang.String string) {
        java.lang.System.err.print(string);
    }
    
    public void flushError() {
        java.lang.System.err.flush();
    }
    
    @TypeInfo("ceylon.language::Null|ceylon.language::String")
    public ceylon.language.String readLine() {
        try {
            if (stdinReader == null) {
                stdinReader = new java.io.BufferedReader( 
                        new java.io.InputStreamReader(java.lang.System.in));
            }
            java.lang.String read = stdinReader.readLine();
            return read == null ? null : ceylon.language.String.instance(read);
        } 
        catch (IOException e) {
            throw new Exception(String.instance("could not read line from standard input"), e);
        }
    }
    
    public void exit(@Name("code") long code) {
        System.exit(Util.toInt(code));
    }
    
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
