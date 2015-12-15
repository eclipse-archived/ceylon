package ceylon.modules.bootstrap.loader;

import java.util.HashSet;
import java.util.Set;

import org.jboss.modules.DependencySpec;
import org.jboss.modules.LocalModuleFinder;
import org.jboss.modules.ModuleFinder;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.ModuleSpec;

import com.redhat.ceylon.model.cmr.JDKUtils;

public class InitialModuleLoader extends ModuleLoader {
    private static final DependencySpec JDK_DEPENDENCY;
    private static final Set<String> JDK_MODULE_NAMES;

    static{
        Set<String> jdkPaths = new HashSet<>();
        JDK_MODULE_NAMES = new HashSet<>();
        // JDK
        for (String module : JDKUtils.getJDKModuleNames()) {
            Set<String> paths = JDKUtils.getJDKPathsByModule(module);
            jdkPaths.addAll(paths);
            JDK_MODULE_NAMES.add(module);
        }
        // Oracle
        for (String module : JDKUtils.getOracleJDKModuleNames()) {
            Set<String> paths = JDKUtils.getOracleJDKPathsByModule(module);
            JDK_MODULE_NAMES.add(module);
            jdkPaths.addAll(paths);
        }
        // always exported implicitely
        JDK_DEPENDENCY = DependencySpec.createSystemDependencySpec(jdkPaths, true);
    }

    public InitialModuleLoader() {
        super(new ModuleFinder[] { new LocalModuleFinder() });
    }
    
    @Override
    protected ModuleSpec findModule(ModuleIdentifier module) throws ModuleLoadException {
    	if(JDK_MODULE_NAMES.contains(module.getName())){
            ModuleSpec.Builder builder = ModuleSpec.build(module);
            builder.addDependency(JDK_DEPENDENCY);
            return builder.create();
    	}
    	return super.findModule(module);
    }
}
