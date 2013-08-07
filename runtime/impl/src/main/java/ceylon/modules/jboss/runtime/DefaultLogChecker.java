package ceylon.modules.jboss.runtime;

import java.util.HashSet;
import java.util.Set;

import ceylon.modules.api.runtime.LogChecker;

import com.redhat.ceylon.cmr.api.ArtifactResult;

/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
public class DefaultLogChecker implements LogChecker {

    private static Set<String> loggingModules = new HashSet<String>();

    static {
        //TODO check module names
        loggingModules.add("org.jboss.logging");
        loggingModules.add("org.apache.log4j");
        loggingModules.add("org.slf4j");
        loggingModules.add("java.logging");
    }

    @Override
    public boolean match(ArtifactResult i) {
        String moduleName = i.name();
        return loggingModules.contains(moduleName);
    }
}
