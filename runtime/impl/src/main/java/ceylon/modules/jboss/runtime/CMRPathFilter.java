

package ceylon.modules.jboss.runtime;

import org.jboss.modules.filter.PathFilter;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class CMRPathFilter implements PathFilter {
    org.eclipse.ceylon.model.cmr.PathFilter filter;

    public CMRPathFilter(org.eclipse.ceylon.model.cmr.PathFilter filter) {
        this.filter = filter;
    }

    public boolean accept(String path) {
        return filter.accept(path);
    }
}
