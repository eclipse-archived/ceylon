package com.redhat.ceylon.dist.osgi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.ArtifactResultType;
import com.redhat.ceylon.cmr.api.ImportType;
import com.redhat.ceylon.cmr.api.RepositoryException;
import com.redhat.ceylon.cmr.api.VisibilityType;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.loader.ContentAwareArtifactResult;

public class Activator implements BundleActivator {

	private class BundleArtifactResult implements ContentAwareArtifactResult {
		BundleWiring wiring;
		
		public BundleArtifactResult(BundleWiring bundle) {
			this.wiring = bundle;
		}
		
		@Override
		public VisibilityType visibilityType() {
			return VisibilityType.STRICT;
		}
		
		@Override
		public String version() {
			return wiring.getBundle().getVersion().toString();
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
		public String name() {
			return wiring.getBundle().getSymbolicName();
		}
		@Override
		public ImportType importType() {
			return ImportType.UNDEFINED;
		}
		@Override
		public List<ArtifactResult> dependencies() throws RepositoryException {
			List<ArtifactResult> results = new ArrayList<>();
			for (BundleWire dep : wiring.getRequiredWires(null)) {
				if (! "ceylon.dist.osgi".equals(dep.getProviderWiring().getBundle().getSymbolicName())) {
					results.add(new BundleArtifactResult(dep.getProviderWiring()));
				}
			}
			return results;
		}
		
		@Override
		public File artifact() throws RepositoryException {
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
            URL url = wiring.getBundle().getResource(path);
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
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return new byte[0];
        }

        @Override
        public List<String> getFileNames(String path) {
            path += "/";
            return Arrays.asList(wiring.listResources(path, "*", BundleWiring.LISTRESOURCES_LOCAL).toArray(new String[]{}));
        }
	}

	
	@Override
	public void start(BundleContext context) throws Exception {
        Bundle bundle = context.getBundle();
        BundleWiring wiring = bundle.adapt(BundleWiring.class);
        String symbolicName = bundle.getSymbolicName();
        Version version = bundle.getVersion();
        String versionString = new StringBuilder("")
                                    .append(version.getMajor())
                                    .append('.')
                                    .append(version.getMinor())
                                    .append('.')
                                    .append(version.getMicro())
                                    .toString();
                                    
        
        final ClassLoader  bundleClassLoader = wiring.getClassLoader();
        Metamodel.loadModule(symbolicName, versionString, 
        		new BundleArtifactResult(wiring), bundleClassLoader);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

}
