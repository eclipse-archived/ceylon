package ceylon.language;

import java.io.IOException;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@Ceylon @com.redhat.ceylon.compiler.metadata.java.Object
public class process extends Object {
	
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

    public void writeLine(@Name("line") @TypeInfo("ceylon.language.String") java.lang.String s) {
        java.lang.System.out.println(s);
    }
    
    public void write(@Name("string") @TypeInfo("ceylon.language.String") java.lang.String s) {
        java.lang.System.out.print(s);
    }
    
    @TypeInfo("ceylon.language.String") 
    public java.lang.String readLine() {
        try {
            return new java.io.BufferedReader( 
                    new java.io.InputStreamReader(java.lang.System.in))
                .readLine();
        } 
        catch (IOException e) {
            throw new Exception(
                    String.instance("could not read line from standard input"), 
                            e);
        }
    }
    
    public long getMilliseconds() {
    	return System.currentTimeMillis();
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
