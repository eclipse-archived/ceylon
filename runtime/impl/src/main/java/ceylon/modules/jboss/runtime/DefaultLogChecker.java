package ceylon.modules.jboss.runtime;

import java.util.HashSet;
import java.util.Set;

import ceylon.modules.api.runtime.LogChecker;
import com.redhat.ceylon.cmr.api.ArtifactResult;

/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
public class DefaultLogChecker implements LogChecker {
    static LogChecker INSTANCE = new DefaultLogChecker();

    private static Set<String> loggingModules = new HashSet<>();

    static {
        //TODO check module names
        loggingModules.add("org.jboss.logging");
        loggingModules.add("org.apache.log4j");
        loggingModules.add("org.slf4j");
        loggingModules.add("java.logging");
    }

    public Boolean match(ArtifactResult dependency) {
        String moduleName = dependency.name();
        return loggingModules.contains(moduleName);
    }
}
