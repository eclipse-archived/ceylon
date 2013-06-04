package ceylon.language.metamodel;

import ceylon.language.Sequential;
import ceylon.language.metamodel.declaration.Module;

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
    public static modules_ $get() {
        return value;
    }
    
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel.declaration::Module>")
    public Sequential<? extends Module> getList(){
        return Metamodel.getModuleList();
    }
    
    @TypeInfo("ceylon.language.metamodel.declaration::Module|ceylon.language::Null")
    public Module find(@Name("name") String name, @Name("version") String version){
        return Metamodel.findLoadedModule(name, version);
    }
}
