package com.redhat.ceylon.model.typechecker.model;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.common.Backend;

/**
 * Describes data specific to module imports
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class ModuleImport implements Annotated {
    private boolean optional;
    private boolean export;
    private Module module;
    private String nativeBackend;
    private List<Annotation> annotations = new ArrayList<Annotation>();
    private ModuleImport overridenModuleImport = null;

    public ModuleImport(Module module, boolean optional, boolean export) {
        this(module, optional, export, (String)null);
    }

    public ModuleImport(Module module, boolean optional, boolean export, Backend backend) {
        this(module, optional, export, backend != null ? backend.nativeAnnotation : null);
    }

    public ModuleImport(Module module, boolean optional, boolean export, String nativeBackend) {
        this.module = module;
        this.optional = optional;
        this.export = export;
        this.nativeBackend = nativeBackend;
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
        return getNative() != null;
    }
    
    public String getNative() {
        return nativeBackend;
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
            this.overridenModuleImport = new ModuleImport(module, optional, export, nativeBackend);
            module = moduleImportOverride.getModule();
            optional = moduleImportOverride.isOptional();
            export = moduleImportOverride.isExport();
            nativeBackend = moduleImportOverride.getNative();
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (export) sb.append("shared ");
        if (optional) sb.append("optional ");
        if (nativeBackend!=null) {
            sb.append("native(")
              .append(nativeBackend)
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
