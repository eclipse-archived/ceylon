

package org.eclipse.ceylon.cmr.api;

import org.eclipse.ceylon.model.cmr.ArtifactResult;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class DependencyContextImpl implements DependencyContext {
    private final ArtifactResult result;
    private boolean ignoreInner;
    private boolean ignoreExternal;

    DependencyContextImpl(ArtifactResult result, boolean ignoreInner, boolean ignoreExternal) {
        this.result = result;
        this.ignoreInner = ignoreInner;
        this.ignoreExternal = ignoreExternal;
    }

    @Override
    public ArtifactResult result() {
        return result;
    }

    public boolean ignoreInner() {
        return ignoreInner;
    }

    public boolean ignoreExternal() {
        return ignoreExternal;
    }
}
