package ceylon.language.descriptor;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
public class Package {
    private final ceylon.language.Quoted name;
    
    
    public Package(@Name("name")
    @TypeInfo("ceylon.language.Quoted")
    ceylon.language.Quoted name, @Name("shared")
    @Defaulted
    @TypeInfo("ceylon.language.Boolean")
    boolean shared, @Name("doc")
    @Defaulted
    @TypeInfo("ceylon.language.String")
    java.lang.String doc, @Name("by")
    @Defaulted
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.String>")
    ceylon.language.Iterable<? extends ceylon.language.String> by) {
        this.name = name;
        this.shared = shared;
        this.doc = doc;
        this.by = by;
    }
    
    @TypeInfo("ceylon.language.Quoted")
    public final ceylon.language.Quoted getName() {
        return name;
    }
    
    private final boolean shared;
    
    @TypeInfo("ceylon.language.Boolean")
    public final boolean getShared() {
        return shared;
    }
    
    private final java.lang.String doc;
    
    @TypeInfo("ceylon.language.String")
    public final java.lang.String getDoc() {
        return doc;
    }
    private final ceylon.language.Iterable<? extends ceylon.language.String> by;
    
    @TypeInfo("ceylon.language.Empty|ceylon.language.Sequence<ceylon.language.String>")
    public final ceylon.language.Iterable<? extends ceylon.language.String> getBy() {
        return by;
    }
 
    @TypeInfo("ceylon.language.String")
    public final java.lang.String toString() {
        return new java.lang.StringBuilder().append("Package[").append(name.toString()).append("]").toString();
    }
    
    @Ignore
    public static boolean $init$shared(
    final ceylon.language.Quoted name) {
        return false;
    }
    
    @Ignore
    public static java.lang.String $init$doc(
    final ceylon.language.Quoted name, 
    final boolean shared) {
        return "";
    }
    
    @Ignore
    public static ceylon.language.Iterable<? extends ceylon.language.String> $init$by(
    final ceylon.language.Quoted name, 
    final boolean shared, 
    final java.lang.String doc) {
        return ceylon.language.$empty.getEmpty();
    }
}