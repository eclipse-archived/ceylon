

package org.eclipse.ceylon.cmr.api;

import org.eclipse.ceylon.model.cmr.ArtifactResult;


/**
 * Abstract dependency resolver.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractDependencyResolver implements DependencyResolver {
    @Override
    public ModuleInfo resolve(ArtifactResult result, Overrides overrides) {
        return resolve(new DependencyContextImpl(result, false, false), overrides);
    }
}

