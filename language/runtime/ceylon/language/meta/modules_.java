package ceylon.language.meta;

import static com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel.findLoadedModule;
import static com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel.getDefaultModule;
import static com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel.getModuleList;
import ceylon.language.Sequential;
import ceylon.language.meta.declaration.Module;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Object;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 8)
@Object
public final class modules_ implements ReifiedType {
    
    @Ignore
    public static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(modules_.class);
    
    private modules_() {}
    
    @Ignore
    private static final modules_ value = new modules_();
    
    @Ignore
    public static modules_ get_() {
        return value;
    }
    
    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.declaration::Module>")
    public Sequential<? extends Module> getList() {
        return getModuleList();
    }
    
    @TypeInfo("ceylon.language::Null|ceylon.language.meta.declaration::Module")
    public Module find(@Name("name") String name, 
    	               @Name("version") String version) {
        return findLoadedModule(name, version);
    }

    @TypeInfo("ceylon.language::Null|ceylon.language.meta.declaration::Module")
    public Module getDefault() {
        return getDefaultModule();
    }

    @Ignore
    @Override
    public TypeDescriptor $getType$(){
        return $TypeDescriptor$;
    }
}
