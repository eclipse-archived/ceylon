package ceylon.language.descriptor;

import ceylon.language.List;
import ceylon.language.String;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 3)
public class Package {
    private final java.lang.String name;
    private final boolean shared;
    private final java.lang.String doc;
    private final List<? extends String> by;
    
    public Package(@Name("name")
    @TypeInfo("ceylon.language::String")
    java.lang.String name, @Name("shared")
    @Defaulted
    @TypeInfo("ceylon.language::Boolean")
    boolean shared, @Name("doc")
    @Defaulted
    @TypeInfo("ceylon.language::String")
    java.lang.String doc, @Name("by")
    @Defaulted
    @TypeInfo("ceylon.language::Sequential<ceylon.language::String>")
    List<? extends String> by) {
        this.name = name;
        this.shared = shared;
        this.doc = doc;
        this.by = by;
    }
    
    @TypeInfo("ceylon.language::String")
    public final java.lang.String getName() {
        return name;
    }
    
    @TypeInfo("ceylon.language::Boolean")
    public final boolean getShared() {
        return shared;
    }
    
    @TypeInfo("ceylon.language::String")
    public final java.lang.String getDoc() {
        return doc;
    }
    
    @TypeInfo("ceylon.language::Sequential<ceylon.language::String>")
    public final List<? extends String> getBy() {
        return by;
    }
 
    @TypeInfo("ceylon.language::String")
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
    public static List<? extends String> $init$by(
    final String name, 
    final boolean shared, 
    final java.lang.String doc) {
        return (List)ceylon.language.empty_.getEmpty$();
    }
}