package ceylon.language.descriptor;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Defaulted;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 3)
public class Import {
    private final String name;
    private final String version;
    
    @TypeInfo("ceylon.language.String")
    public final String getName() {
        return name;
    }
    
    @TypeInfo("ceylon.language.String")
    public final String getVersion() {
        return version;
    }
    private final boolean optional;
    
    @TypeInfo("ceylon.language.Boolean")
    public final boolean getOptional() {
        return optional;
    }
    private final boolean export;
    
    @TypeInfo("ceylon.language.Boolean")
    public final boolean getExport() {
        return export;
    }
    
    @TypeInfo("ceylon.language.String")
    public final java.lang.String toString() {
        return new java.lang.StringBuilder().append("Import[").append(name.toString()).append("/").append(version.toString()).append("]").toString();
    }
    
    public Import(@Name("name")
    @TypeInfo("ceylon.language.String")
    String name, @Name("version")
    @TypeInfo("ceylon.language.String")
    String version, @Name("optional")
    @Defaulted
    @TypeInfo("ceylon.language.Boolean")
    boolean optional, @Name("export")
    @Defaulted
    @TypeInfo("ceylon.language.Boolean")
    boolean export) {
        this.name = name;
        this.version = version;
        this.optional = optional;
        this.export = export;
    }
    
    @Ignore
    public static boolean $init$optional(
    final String name,
    final String version) {
        return false;
    }
    
    @Ignore
    public static boolean $init$export(
    final String name, 
    final String version, 
    final boolean optional) {
        return false;
    }

}
