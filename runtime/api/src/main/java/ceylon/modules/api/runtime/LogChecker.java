package ceylon.modules.api.runtime;

import java.util.List;

import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.ModuleInfo;

/**
 * This allows for external checkers to replace current log module dependency.
 * e.g. JBoss Logging can unify all logging configuration -- same as it's done in WildFly
 *
 * @author Matej Lazar
 * @author Ales Justin
 */
public interface LogChecker {
    /**
     * Return list of module infos that replace this current logging dependency.
     * Or return null if you cannot determine if this is a logging module.
     *
     * @param dependency the current dependency
     * @return null if not determined, otherwise list of module info dependencies
     * @throws java.lang.IllegalArgumentException if empty list is returned
     */
    List<ModuleInfo> handle(ArtifactResult dependency);
}
