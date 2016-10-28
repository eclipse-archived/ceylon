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

package com.redhat.ceylon.cmr.resolver.aether;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.apache.maven.settings.Mirror;
import org.apache.maven.settings.Profile;
import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Repository;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.building.DefaultSettingsBuilder;
import org.apache.maven.settings.building.DefaultSettingsBuilderFactory;
import org.apache.maven.settings.building.DefaultSettingsBuildingRequest;
import org.apache.maven.settings.building.SettingsBuildingException;
import org.apache.maven.settings.building.SettingsBuildingRequest;
import org.apache.maven.settings.building.SettingsBuildingResult;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.ConfigurationProperties;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RequestTrace;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.DependencyCollectionContext;
import org.eclipse.aether.collection.DependencySelector;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.graph.DefaultDependencyNode;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyFilter;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.repository.RepositoryPolicy;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transfer.TransferCancelledException;
import org.eclipse.aether.transfer.TransferEvent;
import org.eclipse.aether.transfer.TransferListener;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.artifact.JavaScopes;
import org.eclipse.aether.util.repository.AuthenticationBuilder;
import org.eclipse.aether.util.repository.DefaultAuthenticationSelector;
import org.eclipse.aether.util.repository.DefaultMirrorSelector;
import org.eclipse.aether.util.repository.DefaultProxySelector;
import org.eclipse.aether.version.Version;

/**
 * Aether utils.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class AetherResolverImpl implements AetherResolver {

    private String currentDirectory;
    private int timeout;
    private boolean offline;
    private String settingsXml;

    private static RepositorySystem newRepositorySystem() {
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService( RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class );
        locator.addService( TransporterFactory.class, FileTransporterFactory.class );
        locator.addService( TransporterFactory.class, HttpTransporterFactory.class );
        
        return locator.getService( RepositorySystem.class );
    }

    private DefaultRepositorySystemSession newSession( RepositorySystem system ) {
        return MavenRepositorySystemUtils.newSession();
    }
    
    private List<RemoteRepository> configureSession(RepositorySystem system, DefaultRepositorySystemSession session){
        DefaultSettingsBuilderFactory factory = new DefaultSettingsBuilderFactory();
        DefaultSettingsBuilder builder = factory.newInstance();

        SettingsBuildingRequest settingsBuilderRequest = new DefaultSettingsBuildingRequest();
        settingsBuilderRequest.setSystemProperties(System.getProperties());
        
        // find the settings
        String settingsFile = settingsXml;
        if(settingsFile == null){
        	File userSettings = new File(System.getProperty("user.home"), ".m2/settings.xml");
        	if(userSettings.exists())
        		settingsFile = userSettings.getAbsolutePath();
        }
        if(settingsFile != null){
        	settingsBuilderRequest.setUserSettingsFile(new File(settingsXml));
        }
        
        // read it
        SettingsBuildingResult settingsBuildingResult;
		try {
			settingsBuildingResult = builder.build(settingsBuilderRequest);
		} catch (SettingsBuildingException e) {
			throw new RuntimeException(e);
		}
        Settings set = settingsBuildingResult.getEffectiveSettings();
        
        // configure the local repo
        String localRepository = set.getLocalRepository();
        if(localRepository == null)
        	localRepository = System.getProperty("user.home")+File.separator+".m2"+File.separator+"repository";
        else {
            if (! new File(localRepository).isAbsolute() && currentDirectory != null) {
                localRepository = new File(new File(currentDirectory), localRepository).getAbsolutePath();
            }
        }

        // set up authentication
        DefaultAuthenticationSelector authenticationSelector = new DefaultAuthenticationSelector();
        for(Server server : set.getServers()){
    		AuthenticationBuilder auth = new AuthenticationBuilder();
    		if(server.getUsername() != null)
    			auth.addUsername(server.getUsername());
    		if(server.getPassword() != null)
    			auth.addPassword(server.getPassword());
    		if(server.getPrivateKey() != null)
    			auth.addPrivateKey(server.getPrivateKey(), server.getPassphrase());
        	authenticationSelector.add(server.getId(), auth.build());
        }
		session.setAuthenticationSelector(authenticationSelector );
        
        // set up mirrors
        DefaultMirrorSelector mirrorSelector = new DefaultMirrorSelector();
        for(Mirror mirror : set.getMirrors()){
        	mirrorSelector.add(mirror.getId(), mirror.getUrl(), mirror.getLayout(), false, mirror.getMirrorOf(), mirror.getMirrorOfLayouts());
        }
        session.setMirrorSelector(mirrorSelector);

        // set up proxies
        DefaultProxySelector proxySelector = new DefaultProxySelector();
        for(Proxy proxy : set.getProxies()){
        	if(proxy.isActive()){
        		AuthenticationBuilder auth = new AuthenticationBuilder();
        		if(proxy.getUsername() != null)
        			auth.addUsername(proxy.getUsername());
        		if(proxy.getPassword() != null)
        			auth.addPassword(proxy.getPassword());
				org.eclipse.aether.repository.Proxy p = new org.eclipse.aether.repository.Proxy(proxy.getProtocol(), proxy.getHost(), 
						proxy.getPort(), auth.build() );
				proxySelector.add(p , proxy.getNonProxyHosts());
        	}
        }
		session.setProxySelector(proxySelector);
        
        // set up remote repos
        List<RemoteRepository> repos = new ArrayList<>();
        RemoteRepository central = new RemoteRepository.Builder( "central", "default", "http://repo1.maven.org/maven2/" ).build();
        repos.add(central);
        for(String profileId : set.getActiveProfiles()){
        	Profile profile = set.getProfilesAsMap().get(profileId);
        	if(profile != null){
        		for(Repository repo : profile.getRepositories()){
        	        RemoteRepository.Builder remoteRepo = new RemoteRepository.Builder( repo.getId(), repo.getLayout(), repo.getUrl() );

        	        // policies
        	        org.apache.maven.settings.RepositoryPolicy repoReleasePolicy = repo.getReleases();
        	        if(repoReleasePolicy != null){
        	            String updatePolicy = repoReleasePolicy.getUpdatePolicy();
                        // This is the default anyway and saves us a message on STDERR
        	            if(updatePolicy == null || updatePolicy.isEmpty())
        	                updatePolicy = RepositoryPolicy.UPDATE_POLICY_NEVER;
        	        	RepositoryPolicy releasePolicy = new RepositoryPolicy(repoReleasePolicy.isEnabled(), updatePolicy, 
        	        			repoReleasePolicy.getChecksumPolicy());
        	        	remoteRepo.setReleasePolicy(releasePolicy );
        	        }
        	        
        	        org.apache.maven.settings.RepositoryPolicy repoSnapshotPolicy = repo.getSnapshots();
        	        if(repoSnapshotPolicy != null){
                        String updatePolicy = repoSnapshotPolicy.getUpdatePolicy();
                        // This is the default anyway and saves us a message on STDERR
                        if(updatePolicy == null || updatePolicy.isEmpty())
                            updatePolicy = RepositoryPolicy.UPDATE_POLICY_NEVER;
        	        	RepositoryPolicy snapshotPolicy = new RepositoryPolicy(repoSnapshotPolicy.isEnabled(), updatePolicy, 
        	        			repoSnapshotPolicy.getChecksumPolicy());
        	        	remoteRepo.setSnapshotPolicy(snapshotPolicy);
        	        }
					
					// auth, proxy and mirrors are done in the session
        			repos.add(remoteRepo.build());
        		}
        	}
        }
        
        // connection settings
        session.setConfigProperty(ConfigurationProperties.CONNECT_TIMEOUT, timeout);
        session.setOffline(offline || set.isOffline());
        
        LocalRepository localRepo = new LocalRepository( localRepository );
        session.setLocalRepositoryManager( system.newLocalRepositoryManager( session, localRepo ) );

        return repos;
    }
    
    private static final DependencySelector NoChildSelector = new DependencySelector(){

		@Override
		public DependencySelector deriveChildSelector(DependencyCollectionContext arg0) {
			return this;
		}

		@Override
		public boolean selectDependency(Dependency arg0) {
			return false;
		}
    };
    

    public AetherResolverImpl(String currentDirectory, String settingsXml, boolean offline, int timeout) {
        this.currentDirectory = currentDirectory;
        this.timeout = timeout;
        this.offline = offline;
        this.settingsXml = settingsXml;
    }

    @Override
    public DependencyDescriptor getDependencies(String groupId, String artifactId, String version, boolean fetchSingleArtifact) 
    		throws AetherException{
        // null extension means auto-detect based on pom
    	return getDependencies(groupId, artifactId, version, null, null, fetchSingleArtifact);
    }
    
    public File getLocalRepositoryBaseDir() {
        RepositorySystem repoSystem = newRepositorySystem();
        DefaultRepositorySystemSession session = newSession( repoSystem );
        configureSession(repoSystem, session);
        return session.getLocalRepository().getBasedir();
    }
    
    @Override
    public DependencyDescriptor getDependencies(String groupId, String artifactId, String version, 
    		String classifier, String extension, boolean fetchSingleArtifact) 
    				throws AetherException{
    	
        RepositorySystem repoSystem = newRepositorySystem();
        DefaultRepositorySystemSession session = newSession( repoSystem );
        List<RemoteRepository> repos = configureSession(repoSystem, session);
        // TODO figure out how to map this to ArtifactCallback 
//        session.setTransferListener(new TransferListener(){
//
//            @Override
//            public void transferCorrupted(TransferEvent arg0) throws TransferCancelledException {
//                System.err.println("Transfer corrupted "+arg0.getResource());
//            }
//
//            @Override
//            public void transferFailed(TransferEvent arg0) {
//                System.err.println("Transfer failed "+arg0.getResource());
//            }
//
//            @Override
//            public void transferInitiated(TransferEvent arg0) throws TransferCancelledException {
//                System.err.println("Transfer initiated "+arg0.getResource());
//            }
//
//            @Override
//            public void transferProgressed(TransferEvent arg0) throws TransferCancelledException {
//                System.err.println("Transfer progressed "+arg0.getResource());
//                System.err.println("Data length: "+arg0.getDataLength());
//                System.err.println("Transferred bytes: "+arg0.getTransferredBytes());
//            }
//
//            @Override
//            public void transferStarted(TransferEvent arg0) throws TransferCancelledException {
//                System.err.println("Transfer started "+arg0.getResource());
//                
//            }
//
//            @Override
//            public void transferSucceeded(TransferEvent arg0) {
//                System.err.println("Transfer succeeded "+arg0.getResource());
//                
//            }});

        if(extension == null){
            // I don't think POMs with a classifier exist, so let's not set it
            DefaultArtifact artifact = new DefaultArtifact( groupId, artifactId, null, "pom", version);
            ArtifactRequest artifactRequest = new ArtifactRequest();
            artifactRequest.setArtifact(artifact);
            artifactRequest.setRepositories(repos);

            Artifact resultArtifact;
            try {
                resultArtifact = repoSystem.resolveArtifact(session, artifactRequest).getArtifact();
            } catch (ArtifactResolutionException e) {
                throw new AetherException(e);
            }
            if(resultArtifact != null){
                extension = findExtension(resultArtifact.getFile());
            }
            if(extension == null
                    // we only support jar/aar. ear/war/bundle will resolve as jar anyway
                    || (!extension.equals("jar") && !extension.equals("aar")))
                extension = "jar";
        }
        DefaultArtifact artifact = new DefaultArtifact( groupId, artifactId, classifier, extension, version);
        DependencyNode ret;
        
        if(!fetchSingleArtifact){
            final Dependency dependency = new Dependency( artifact, JavaScopes.COMPILE );
        	CollectRequest collectRequest = new CollectRequest();
        	collectRequest.setRepositories(repos);
        	collectRequest.setRoot( dependency );

        	DependencyRequest dependencyRequest = new DependencyRequest();
        	dependencyRequest.setCollectRequest(collectRequest);
        	dependencyRequest.setFilter(new DependencyFilter(){
        		@Override
        		public boolean accept(DependencyNode dep, List<DependencyNode> parents) {
        			return parents.size() == 0;
        		}
        	});

        	// only get first-level dependencies, of both scopes
        	session.setDependencySelector(new DependencySelector(){

        		@Override
        		public DependencySelector deriveChildSelector(DependencyCollectionContext ctx) {
        			if(ctx.getDependency().equals(dependency))
        				return this;
        			return NoChildSelector;
        		}

        		@Override
        		public boolean selectDependency(Dependency dep) {
        			return !dep.isOptional()
        					&& (JavaScopes.COMPILE.equals(dep.getScope())
        					    || JavaScopes.PROVIDED.equals(dep.getScope()));
        		}
        	});

        	try {
				ret = repoSystem.resolveDependencies( session, dependencyRequest ).getRoot();
			} catch (DependencyResolutionException e) {
				throw new AetherException(e);
			}
        }else{
            ArtifactRequest artifactRequest = new ArtifactRequest();
            artifactRequest.setArtifact(artifact);
        	artifactRequest.setRepositories(repos);

        	Artifact resultArtifact;
			try {
				resultArtifact = repoSystem.resolveArtifact(session, artifactRequest).getArtifact();
			} catch (ArtifactResolutionException e) {
				throw new AetherException(e);
			}
        	ret = new DefaultDependencyNode(resultArtifact);
        }
        
        return ret == null ? null : new DependencyNodeDependencyDescriptor(ret);
    }

    private String findExtension(File pomFile) {
        if(pomFile != null && pomFile.exists()){
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model;
            try(FileReader fileReader = new FileReader(pomFile)){
                model = reader.read(fileReader);
                return model.getPackaging();
            } catch (XmlPullParserException | IOException e) {
                return null;
            }
        };
        return null;
    }

    @Override
    public List<String> resolveVersionRange(String groupId, String artifactId, String versionRange) throws AetherException {
        RepositorySystem repoSystem = newRepositorySystem();
        DefaultRepositorySystemSession session = newSession( repoSystem );
        List<RemoteRepository> repos = configureSession(repoSystem, session);

        Artifact artifact = new DefaultArtifact( groupId, artifactId, "jar", versionRange );

        VersionRangeRequest rangeRequest = new VersionRangeRequest();
        rangeRequest.setArtifact( artifact );
        rangeRequest.setRepositories(repos);

        VersionRangeResult rangeResult;
		try {
			rangeResult = repoSystem.resolveVersionRange( session, rangeRequest );
		} catch (VersionRangeResolutionException e) {
			throw new AetherException(e);
		}
        List<String> ret = new ArrayList<>(rangeResult.getVersions().size());
        for(Version version : rangeResult.getVersions())
        	ret.add(version.toString());
        return ret;
    }

    @Override
    public DependencyDescriptor getDependencies(File pomXml, String name, String version) throws IOException {
    	MavenXpp3Reader reader = new MavenXpp3Reader();
    	Model model;
    	try(FileReader fileReader = new FileReader(pomXml)){
    		model = reader.read(fileReader);
    	} catch (XmlPullParserException e) {
    		throw new IOException(e);
		}
    	return new ModelDependencyDescriptor(model);
    }

    @Override
    public DependencyDescriptor getDependencies(InputStream pomXml, String name, String version) throws IOException {
    	MavenXpp3Reader reader = new MavenXpp3Reader();
    	Model model;
		try {
			model = reader.read(pomXml);
		} catch (XmlPullParserException e) {
			throw new IOException(e);
		}
        return new ModelDependencyDescriptor(model);
    }
}
