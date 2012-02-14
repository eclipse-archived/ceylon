/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted
 * by the author tags.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redhat.ceylon.cmr.maven;

import com.redhat.ceylon.cmr.api.Logger;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.ResolutionException;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

import java.io.File;

/**
 * Aether utils.
 * <p/>
 * We actually use JBoss ShrinkWrap Resolver.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class AetherUtils {

    private Logger log;
    private static MavenDependencyResolver resolver;

    AetherUtils(Logger log) {
        this.log = log;
    }

    File getDependency(String groupId, String artifactId, String version) {
        final String coordinates = groupId + ":" + artifactId + ":" + version;
        try {
            final File[] files = getResolver().artifact(coordinates).exclusion("*").resolveAsFiles();
            return (files != null && files.length == 1) ? files[0] : null;
        } catch (ResolutionException e) {
            log.debug("Could not resolve artifact [" + coordinates + "] : " + e);
            return null;
        }
    }

    private static File getMavenSettings() {
        String path = System.getProperty("maven.repo.local");
        if (path != null) {
            File file = new File(path, "settings.xml");
            if (file.exists())
                return file;
        }

        path = System.getProperty("user.home");
        if (path != null) {
            File file = new File(path, ".m2/settings.xml");
            if (file.exists())
                return file;
        }

        path = System.getenv("M2_HOME");
        if (path != null) {
            File file = new File(path, "conf/settings.xml");
            if (file.exists())
                return file;
        }

        throw new IllegalArgumentException("No known default path to Maven settings.xml!");
    }

    private static MavenDependencyResolver getResolver() {
        if (resolver == null) {
            final File settingsXml = getMavenSettings();
            resolver = DependencyResolvers.use(MavenDependencyResolver.class).configureFrom(settingsXml.getPath());
        }
        return resolver;
    }
}
