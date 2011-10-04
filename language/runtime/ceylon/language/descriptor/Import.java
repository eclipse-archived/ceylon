package ceylon.language.descriptor;

import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

public class Import {
    private ceylon.language.Quoted name;
    private ceylon.language.Quoted version;
    private ceylon.language.Boolean optional;
    private ceylon.language.Boolean export;

    public Import(@Name("name") ceylon.language.Quoted name, 
            @Name("version") ceylon.language.Quoted version, 
            @TypeInfo("ceylon.language.Nothing|ceylon.language.Boolean") @Name("optional") ceylon.language.Boolean optional, 
            @TypeInfo("ceylon.language.Nothing|ceylon.language.Boolean") @Name("export") ceylon.language.Boolean export) {
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

    public ceylon.language.Boolean getOptional() {
        return optional;
    }

    public ceylon.language.Boolean getExport() {
        return export;
    }
}
