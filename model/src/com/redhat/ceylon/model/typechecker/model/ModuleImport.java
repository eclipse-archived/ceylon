package com.redhat.ceylon.model.typechecker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes data specific to module imports
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class ModuleImport implements Annotated {
    private boolean optional;
    private boolean export;
    private Module module;
    private List<Annotation> annotations = new ArrayList<Annotation>();
    private ModuleImport overridenModuleImport = null;

    public ModuleImport(Module module, boolean optional, boolean export) {
        this.module = module;
        this.optional = optional;
        this.export = export;
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
            this.overridenModuleImport = new ModuleImport(module, optional, export);
            module = moduleImportOverride.getModule();
            optional = moduleImportOverride.isOptional();
            export = moduleImportOverride.isExport();
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ModuleImport");
        sb.append("{module=").append(module.getNameAsString()).append(", ").append(module.getVersion());
        sb.append(", optional=").append(optional);
        sb.append(", export=").append(export);
        sb.append('}');
        return sb.toString();
    }
}
