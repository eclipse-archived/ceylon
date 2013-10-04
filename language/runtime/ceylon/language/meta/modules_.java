package ceylon.language.meta;

import ceylon.language.Sequential;
import ceylon.language.meta.declaration.Module;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Object;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;

@Ceylon(major = 5)
@Object
public final class modules_ {
    
    private modules_() {}
    @Ignore
    private static final modules_ value = new modules_();
    
    @Ignore
    public static modules_ get_() {
        return value;
    }
    
    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.declaration::Module>")
    public Sequential<? extends Module> getList(){
        return Metamodel.getModuleList();
    }
    
    @TypeInfo("ceylon.language.meta.declaration::Module|ceylon.language::Null")
    public Module find(@Name("name") String name, @Name("version") String version){
        return Metamodel.findLoadedModule(name, version);
    }

    @TypeInfo("ceylon.language.meta.declaration::Module|ceylon.language::Null")
    public Module getDefault(){
        return Metamodel.getDefaultModule();
    }
}
