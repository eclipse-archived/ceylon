package com.redhat.ceylon.model.typechecker.model;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.Backends;

/**
 * Describes data specific to module imports
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class ModuleImport implements Annotated {
    private boolean optional;
    private boolean export;
    private Module module;
    private Backends nativeBackends;
    private List<Annotation> annotations = new ArrayList<Annotation>();
    private ModuleImport overridenModuleImport = null;

    public ModuleImport(Module module, boolean optional, boolean export) {
        this(module, optional, export, (Backend)null);
    }

    public ModuleImport(Module module, boolean optional, boolean export, Backend backend) {
        this(module, optional, export, backend != null ? backend.asSet() : Backends.NONE);
    }

    public ModuleImport(Module module, boolean optional, boolean export, Backends backends) {
        this.module = module;
        this.optional = optional;
        this.export = export;
        this.nativeBackends = backends;
    }

    public boolean isOptional() {
        return optional;
    }

    public boolean isExport() {
        return export;
    }

    public Module getModule() {
        return module;
    }
    
    public boolean isNative() {
        return !getNativeBackends().none();
    }
    
    public Backends getNativeBackends() {
        return nativeBackends;
    }
    
    @Override
    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public ModuleImport getOverridenModuleImport() {
        return overridenModuleImport;
    }

    public boolean override(ModuleImport moduleImportOverride) {
        if (overridenModuleImport == null
        		&& moduleImportOverride != null) {
            this.overridenModuleImport = new ModuleImport(module, optional, export, nativeBackends);
            module = moduleImportOverride.getModule();
            optional = moduleImportOverride.isOptional();
            export = moduleImportOverride.isExport();
            nativeBackends = moduleImportOverride.getNativeBackends();
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (export) sb.append("shared ");
        if (optional) sb.append("optional ");
        if (!nativeBackends.none()) {
            sb.append("native(")
              .append(nativeBackends)
              .append(") ");
        }
        sb.append("import ")
          .append(module.getNameAsString())
          .append(" \"")
          .append(module.getVersion())
          .append("\"");
        return sb.toString();
    }
}
