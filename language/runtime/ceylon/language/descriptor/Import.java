package ceylon.language.descriptor;

import com.redhat.ceylon.compiler.metadata.java.Name;

public class Import {
    private ceylon.language.Quoted name;
    private ceylon.language.Quoted version;
    private boolean optional;
    private boolean export;

    public Import(@Name("name") ceylon.language.Quoted name, 
            @Name("version") ceylon.language.Quoted version, 
            @Name("optional") boolean optional, 
            @Name("export") boolean export) {
        this.name = name;
        this.version = version;
        this.optional = optional;
        this.export = export;
    }

    public ceylon.language.Quoted getName() {
        return name;
    }

    public ceylon.language.Quoted getVersion() {
        return version;
    }

    public boolean getOptional() {
        return optional;
    }

    public boolean getExport() {
        return export;
    }
}
