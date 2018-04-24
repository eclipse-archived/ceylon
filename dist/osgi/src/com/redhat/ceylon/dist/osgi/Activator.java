/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.dist.osgi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;
import org.osgi.framework.wiring.BundleWire;
import org.osgi.framework.wiring.BundleWiring;

import org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.cmr.ArtifactResultType;
import org.eclipse.ceylon.model.cmr.Exclusion;
import org.eclipse.ceylon.model.cmr.ModuleScope;
import org.eclipse.ceylon.model.cmr.PathFilter;
import org.eclipse.ceylon.model.cmr.Repository;
import org.eclipse.ceylon.model.cmr.RepositoryException;
import org.eclipse.ceylon.model.cmr.VisibilityType;
import org.eclipse.ceylon.model.loader.ContentAwareArtifactResult;

public class Activator implements BundleActivator {

    private static class BundleArtifactResult implements ContentAwareArtifactResult {
        Bundle bundle;
        
        public BundleArtifactResult(Bundle bundle) {
            this.bundle = bundle;
        }
        
        @Override
        public VisibilityType visibilityType() {
            return VisibilityType.STRICT;
        }
        
        @Override
        public Repository repository() {
            return null;
        }
        
        public ClassLoader getClassLoader() {
            BundleWiring wiring = bundle.adapt(BundleWiring.class);
            try {
                return wiring.getClassLoader();
            } catch(ClassCastException e) {
                // to solve a problem in Kepler with system bundles throwing a CCE
            }
            return null;
        }
        
        @Override
        public String version() {
            ClassLoader  bundleClassLoader = getClassLoader();
            if (bundleClassLoader != null) {
                try {
                    Class<?> moduleClass = bundleClassLoader.loadClass(bundle.getSymbolicName() + ".$module_");
                    if (moduleClass != null) {
                        org.eclipse.ceylon.compiler.java.metadata.Module moduleAnnotation =
                                moduleClass.getAnnotation(org.eclipse.ceylon.compiler.java.metadata.Module.class);
                        if (moduleAnnotation != null) {
                            String ceylonVersion = moduleAnnotation.version();
                            if (ceylonVersion != null) {
                                return ceylonVersion;
                            }                            
                        }
                    }
                } catch(ClassNotFoundException e) {
                    // to solve a problem in Kepler with system bundles throwing a CCE
                }
            }

            return bundle.getVersion().toString();
        }
        @Override
        public ArtifactResultType type() {
            return ArtifactResultType.CEYLON;
        }
        @Override
        public String repositoryDisplayString() {
            return null;
        }
        @Override
        public String groupId() {
            return null;
        }
        @Override
        public String artifactId() {
            return null;
        }
        @Override
        public String classifier() {
            return null;
        }
        @Override
        public String namespace() {
            return null;
        }
        @Override
        public String name() {
            return bundle.getSymbolicName();
        }
        @Override
        public boolean exported() {
            return false;
        }
        @Override
        public boolean optional() {
            return false;
        }
        @Override
        public List<ArtifactResult> dependencies() throws RepositoryException {
            List<ArtifactResult> results = new ArrayList<>();
            BundleWiring wiring = bundle.adapt(BundleWiring.class);
            for (BundleWire dep : wiring.getRequiredWires(null)) {
                if (! "org.eclipse.ceylon.dist".equals(dep.getProviderWiring().getBundle().getSymbolicName())) {
                    results.add(new BundleArtifactResult(dep.getProviderWiring().getBundle()));
                }
            }
            return results;
        }
        
        @Override
        public File artifact() throws RepositoryException {
            return null;
        }

        @Override
        public PathFilter filter(){
            return null;
        }

        private String getPackageName(String name) {
            int lastSlash = name.lastIndexOf('/');
            if(lastSlash == -1)
                return "";
            return name.substring(0, lastSlash);
        }

        @Override
        public Collection<String> getPackages() {
            Set<String> packages = new HashSet<>();
            BundleWiring wiring = bundle.adapt(BundleWiring.class);
            for (String resource : wiring.listResources("/", "*", 
                    BundleWiring.LISTRESOURCES_RECURSE | BundleWiring.LISTRESOURCES_LOCAL)) {
                if (! resource.endsWith("/")) {
                    packages.add(getPackageName(resource));
                }
            }
            return packages;
        }

        @Override
        public Collection<String> getEntries() {
            Set<String> entries = new HashSet<>();
            BundleWiring wiring = bundle.adapt(BundleWiring.class);
            for (String resource : wiring.listResources("/", "*", 
                    BundleWiring.LISTRESOURCES_RECURSE | BundleWiring.LISTRESOURCES_LOCAL)) {
                if (! resource.endsWith("/")) {
                    entries.add(resource);
                }
            }
            return entries;
        }

        @Override
        public byte[] getContents(String path) {
            URL url = bundle.getResource(path);
            if (url != null) {
                InputStream is;
                try {
                    is = url.openStream();
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    try {
                        byte[] buffer = new byte[1024];
                        int read = 0;
                        while ((read = is.read(buffer)) > 0) {
                            os.write(buffer, 0, read);
                        }
                    } finally {
                        try {
                            is.close();
                        } catch (IOException e) {
                        }
                        try {
                            os.close();
                        } catch (IOException e) {
                        }
                    }
                    return os.toByteArray();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            return new byte[0];
        }

        @Override
        public URI getContentUri(String path) {
            try {
                URL url = bundle.getResource(path);
                return url.toURI();
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }

        @Override
        public List<String> getFileNames(String path) {
            BundleWiring wiring = bundle.adapt(BundleWiring.class);
            path += "/";
            return Arrays.asList(wiring.listResources(path, "*", BundleWiring.LISTRESOURCES_LOCAL).toArray(new String[]{}));
        }

        @Override
        public ModuleScope moduleScope() {
            return ModuleScope.COMPILE;
        }

        @Override
        public List<Exclusion> getExclusions() {
            return null;
        }
    }

    
    @Override
    public void start(BundleContext context) throws Exception {
        Bundle bundle = context.getBundle();
        try {
            loadBundleAsModule(bundle);
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void loadBundleAsModule(Bundle bundle) {
        BundleArtifactResult artifactResult = new BundleArtifactResult(bundle);

        if (Metamodel.loadModule(artifactResult.name(), artifactResult.version(), 
                artifactResult, artifactResult.getClassLoader())) {
            for (ArtifactResult dependency : artifactResult.dependencies()) {
                if (dependency instanceof BundleArtifactResult) {
                    Bundle childBundle = ((BundleArtifactResult) dependency).bundle;
                    if (childBundle != null) {
                        loadBundleAsModule(childBundle);
                    }
                }
            }
        }
    }

    @Override
    public void stop(BundleContext context) throws Exception {
    }

}
