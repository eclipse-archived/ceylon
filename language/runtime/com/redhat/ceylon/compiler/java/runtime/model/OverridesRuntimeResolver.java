package com.redhat.ceylon.compiler.java.runtime.model;

import org.jboss.modules.ModuleIdentifier;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.Overrides;

/**
 * Implements {@link RuntimeResolver} using {@link Overrides} with 
 * the facility to store an instance in a {@code ThreadLocal}.
 */
public class OverridesRuntimeResolver implements RuntimeResolver {

    private static final ThreadLocal<RuntimeResolver> tl = new ThreadLocal<RuntimeResolver>();
    
    private Overrides overrides;

    public OverridesRuntimeResolver(Overrides overrides) {
        this.overrides = overrides;
    }
    
    protected ModuleIdentifier findOverride(ModuleIdentifier mi) {
        final ArtifactContext context = new ArtifactContext(mi.getName(), mi.getSlot(), ArtifactContext.CAR, ArtifactContext.JAR);
        ArtifactContext override = overrides.applyOverrides(context);
        return ModuleIdentifier.create(override.getName(), override.getVersion());
    }
    
    @Override
    public String resolveVersion(String moduleName, String moduleVersion) {
        if ("default".equals(moduleName)
                && moduleVersion == null) {
            // JBoss Modules turns default/null into default:main
            return null;
        }
        return findOverride(ModuleIdentifier.create(moduleName, moduleVersion)).getSlot();
    }
    
    /**
     * The {@link RuntimeModuleManager} needs to obtain 
     * a {@link RuntimeResolver} from somewhere, but it cannot simply 
     * be passed in. To solve this we have the runtime container, 
     * such as the {@code Main} API or the 
     * ToolProvider API, (which knows about the overrides) call 
     * {@link #installInThreadLocal()} before executing the module
     * and the {@code RuntimeModuleManager} can then recover 
     * it using {@link #getFromThreadLocal()}.
     */
    public void installInThreadLocal() {
        tl.set(this);
    }
    
    public static RuntimeResolver getFromThreadLocal() {
        return tl.get();
    }

}
