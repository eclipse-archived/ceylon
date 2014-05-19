/*
 * Copyright 2014 Red Hat inc. and third party contributors as noted
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

package com.redhat.ceylon.cmr.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.redhat.ceylon.cmr.api.AbstractDependencyResolver;
import com.redhat.ceylon.cmr.api.DependencyContext;
import com.redhat.ceylon.cmr.api.DependencyResolver;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.spi.Node;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class OSGiDependencyResolver extends AbstractDependencyResolver {
    private static final Logger log = Logger.getLogger(OSGiDependencyResolver.class.getName());
    static final DependencyResolver INSTANCE = new OSGiDependencyResolver();

    public Set<ModuleInfo> resolve(DependencyContext context) {
        if (context.ignoreInner() == false) {
            InputStream stream = IOUtils.findDescriptor(context.result(), JarFile.MANIFEST_NAME);
            if (stream != null) {
                try {
                    return resolveFromInputStream(stream);
                } finally {
                    IOUtils.safeClose(stream);
                }
            }
        }
        return null;
    }

    public Set<ModuleInfo> resolveFromFile(File file) {
        return null;
    }

    public Set<ModuleInfo> resolveFromInputStream(InputStream stream) {
        if (stream == null) {
            return null;
        }

        try {
            Manifest manifest = new Manifest(stream);
            Attributes attributes = manifest.getMainAttributes();
            String requireBundle = attributes.getValue("Require-Bundle");
            if (requireBundle == null) {
                if (log.isLoggable(Level.INFO)) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    manifest.write(baos);
                    log.info(String.format("No OSGi Required-Bundle attribute, main-attributes: %s", new String(baos.toByteArray())));
                }
                return Collections.emptySet();
            } else {
                return parseRequireBundle(requireBundle);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            IOUtils.safeClose(stream);
        }
    }

    public Node descriptor(Node artifact) {
        return null;
    }

    private Set<ModuleInfo> parseRequireBundle(String requireBundle) {
        Set<ModuleInfo> infos = new HashSet<>();
        String[] bundles = requireBundle.split(",");
        for (String bundle : bundles) {
            infos.add(parseModuleInfo(bundle));
        }
        return infos;
    }

    // very simplistic osgi parsing ...
    private ModuleInfo parseModuleInfo(String bundle) {
        int p = bundle.indexOf(';');
        String name = (p < 0) ? bundle : bundle.substring(0, p);
        String version = "0.0.0";
        boolean optional = false;
        boolean shared = false;
        if (p > 0) {
            String[] parameters = bundle.substring(p + 1).split(";");
            for (String param : parameters) {
                if (param.startsWith("bundle-version=")) {
                    version = param.substring("bundle-version=".length());
                } else if (param.equals("visibility:=reexport")) {
                    shared = true;
                } else if (param.equals("resolution:=optional")) {
                    optional = true;
                }
            }
        }
        return new ModuleInfo(name, version, optional, shared);
    }
}
