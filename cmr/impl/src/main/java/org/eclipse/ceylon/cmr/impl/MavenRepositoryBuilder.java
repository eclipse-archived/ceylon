

package org.eclipse.ceylon.cmr.impl;

import java.io.File;
import java.lang.reflect.Method;

import org.eclipse.ceylon.cmr.api.CmrRepository;
import org.eclipse.ceylon.cmr.api.RepositoryBuilder;
import org.eclipse.ceylon.common.FileUtil;
import org.eclipse.ceylon.common.log.Logger;

/**
 * Repository builder for AetherRepository
 *
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
public class MavenRepositoryBuilder implements RepositoryBuilder {

    private static final String AETHER_REPOSITORY_CLASS = "org.eclipse.ceylon.cmr.maven.AetherRepository";

    @Override
    public String absolute(File cwd, String token) {
        if (token.equals("aether") || token.equals("aether:") || token.equals("aether:/#")
                || token.equals("mvn") || token.equals("mvn:") || token.equals("mvn:/#")
                || token.equals("maven") || token.equals("maven:") || token.equals("maven:/#")) {
            return token;
        } else if (token.startsWith("aether:")) {
            return absolute(cwd, token, "aether:");
        } else if (token.startsWith("mvn:")) {
            return absolute(cwd, token, "mvn:");
        } else if (token.startsWith("maven:")) {
            return absolute(cwd, token, "maven:");
        } else {
            return null;
        }
    }

    private String absolute(File cwd, String token, String prefix) {
        token = token.substring(prefix.length());
        File f = FileUtil.absoluteFile(FileUtil.applyCwd(cwd, new File(token)));
        token = f.getAbsolutePath();
        return prefix + token;
    }

    @Override
    public CmrRepository[] buildRepository(String token) throws Exception {
        return buildRepository(token, EMPTY_CONFIG);
    }

    @Override
    public CmrRepository[] buildRepository(String token, RepositoryBuilderConfig config) throws Exception {
        if (token.equals("aether") || token.equals("aether:") || token.equals("aether:/#")
                || token.equals("mvn") || token.equals("mvn:") || token.equals("mvn:/#")
                || token.equals("maven") || token.equals("maven:") || token.equals("maven:/#")) {
            return createMavenRepository(token, null, config);
        } else if (token.startsWith("aether:")) {
            return createMavenRepository(token, "aether:", config);
        } else if (token.startsWith("mvn:")) {
            return createMavenRepository(token, "mvn:", config);
        } else if (token.startsWith("maven:")) {
            return createMavenRepository(token, "maven:", config);
        } else {
            return null;
        }
    }

    private CmrRepository[] createMavenRepository(String token, String prefix, RepositoryBuilderConfig config) throws Exception {
        String settingsXml = null;
        if (prefix != null) {
            String settings = token.substring(prefix.length());
            // backwards compat: ignore overrides from here, previously located after | symbol
            int p = settings.indexOf("|");
            if (p < 0) {
                settingsXml = settings;
            } else {
                settingsXml = settings.substring(0, p);
            }
        }
        Class<?> aetherRepositoryClass = Class.forName(AETHER_REPOSITORY_CLASS);
        Method createRepository = aetherRepositoryClass.getMethod("createRepository", Logger.class, String.class, boolean.class, int.class, String.class);
        CmrRepository repo = (CmrRepository)createRepository.invoke(null, config.log, settingsXml, config.offline, config.timeout, config.currentDirectory);
        return new CmrRepository[] { repo };
    }

    public static CmrRepository createMavenRepository(String rootRepository, RepositoryBuilderConfig config) throws Exception {
        Class<?> aetherRepositoryClass = Class.forName(AETHER_REPOSITORY_CLASS);
        Method createRepository = aetherRepositoryClass.getMethod("createRepository", Logger.class, String.class, String.class, boolean.class, int.class, String.class);
        return (CmrRepository)createRepository.invoke(null, config.log, null, rootRepository, config.offline, config.timeout, config.currentDirectory);
    }
}
