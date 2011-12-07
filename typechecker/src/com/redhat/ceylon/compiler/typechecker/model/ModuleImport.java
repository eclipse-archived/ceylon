package com.redhat.ceylon.compiler.typechecker.model;

/**
 * Describes data specific to module imports
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class ModuleImport {
    private final boolean optional;
    private final boolean export;
    private final Module module;

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
