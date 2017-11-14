

package ceylon.modules.jboss.runtime;

import ceylon.modules.Configuration;

import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.jboss.modules.ModuleLoader;

/**
 * Default Ceylon Modules runtime.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class JBossRuntime extends AbstractJBossRuntime {
    protected ModuleLoader createModuleLoader(Configuration conf) throws Exception {
        RepositoryManager repository = createRepository(conf);
        return new CeylonModuleLoader(repository, conf.autoExportMavenDependencies);
    }
}
