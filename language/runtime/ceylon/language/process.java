package ceylon.language;

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
        String[] newArgs = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            newArgs[i] = String.instance(args[i]);
        }
        this.args = new ArraySequence<String>(newArgs);
    }
    
//    shared Entries<String,String> switches { throw; }

//    shared Entries<String,String> properties { throw; }

    public void writeLine(@Name("s") @TypeInfo("ceylon.language.String") java.lang.String s) {
        java.lang.System.out.println(s);
    }
    
    public void write(@Name("s") @TypeInfo("ceylon.language.String") java.lang.String s) {
        java.lang.System.out.print(s);
    }
    
//  shared String readLine() { throw; }
    
    private process() {
    }
    private static final process value = new process();
    
    public static process getProcess() {
        return value;
    }
}
