package ceylon.language.descriptor;

import ceylon.language.Iterable;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 3)
public class Package {
    private final String name;
    
    
    public Package(@Name("name")
    @TypeInfo("ceylon.language.String")
    String name, @Name("shared")
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
    
    @TypeInfo("ceylon.language.String")
    public final String getName() {
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
    final String name) {
        return false;
    }
    
    @Ignore
    public static java.lang.String $init$doc(
    final String name, 
    final boolean shared) {
        return "";
    }
    
    @Ignore
    public static ceylon.language.Iterable<? extends ceylon.language.String> $init$by(
    final String name, 
    final boolean shared, 
    final java.lang.String doc) {
        return (Iterable)ceylon.language.empty_.getEmpty();
    }
}