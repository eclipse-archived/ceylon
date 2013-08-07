package ceylon.modules.api.runtime;

import com.redhat.ceylon.cmr.api.ArtifactResult;

/**
 * @author Matej Lazar
 */
public interface LogChecker {

    boolean match(ArtifactResult i);

}
