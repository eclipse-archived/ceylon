package ceylon.language;

import java.io.IOException;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.Object;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@Ceylon @Object //TODO: extendedType=Object
public class process extends ceylon.language.Object {
	
    @SuppressWarnings("unchecked")
    private Iterable<? extends String> args = $empty.getEmpty();
    
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.String>")
    public Iterable<? extends String> getArguments() {
        return args;
    }
    
    public void setupArguments(java.lang.String[] args) {
    	if (args.length>0) {
	        String[] newArgs = new String[args.length];
	        for (int i = 0; i < args.length; i++) {
	            newArgs[i] = String.instance(args[i]);
	        }
	        this.args = new ArraySequence<String>(newArgs);
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
    
    public void writeErrorLine(@Name("line") java.lang.String s) {
        java.lang.System.err.println(s);
    }
    
    public void writeError(@Name("string") java.lang.String s) {
        java.lang.System.err.print(s);
    }
    
    public java.lang.String readLine() {
        try {
            return new java.io.BufferedReader( 
                    new java.io.InputStreamReader(java.lang.System.in))
                .readLine();
        } 
        catch (IOException e) {
            throw new Exception("could not read line from standard input", e);
        }
    }
    
    public long getMilliseconds() {
    	return System.currentTimeMillis();
    }
    
    public void exit(long code) {
    	System.exit((int) code);
    }
    
    @Override
    public java.lang.String toString() {
    	return "process";
    }
    
    private process() {}
    private static final process value = new process();
    
    public static process getProcess() {
        return value;
    }
}
