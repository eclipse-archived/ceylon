package ceylon.modules.api.runtime;

import com.redhat.ceylon.cmr.api.ArtifactResult;

/**
 * @author Matej Lazar
 * @author Ales Justin
 */
public interface LogChecker {
    /**
     * Return true of false if you can determine if the dependency is logging module.
     * With false you can short-cut default checker.
     * Or return null if you cannot determine if this is logging module.
     *
     * @param dependency the current dependency
     * @return null if not determined, otherwise true or false
     */
    Boolean match(ArtifactResult dependency);
}
