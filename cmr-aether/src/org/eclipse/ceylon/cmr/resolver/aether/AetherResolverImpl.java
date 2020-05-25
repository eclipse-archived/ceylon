/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.resolver.aether;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.eclipse.ceylon.aether.apache.maven.model.Model;
import org.eclipse.ceylon.aether.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.eclipse.ceylon.aether.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.ceylon.aether.apache.maven.settings.Activation;
import org.eclipse.ceylon.aether.apache.maven.settings.Mirror;
import org.eclipse.ceylon.aether.apache.maven.settings.Profile;
import org.eclipse.ceylon.aether.apache.maven.settings.Proxy;
import org.eclipse.ceylon.aether.apache.maven.settings.Repository;
import org.eclipse.ceylon.aether.apache.maven.settings.Server;
import org.eclipse.ceylon.aether.apache.maven.settings.Settings;
import org.eclipse.ceylon.aether.apache.maven.settings.building.DefaultSettingsBuilder;
import org.eclipse.ceylon.aether.apache.maven.settings.building.DefaultSettingsBuilderFactory;
import org.eclipse.ceylon.aether.apache.maven.settings.building.DefaultSettingsBuildingRequest;
import org.eclipse.ceylon.aether.apache.maven.settings.building.SettingsBuildingException;
import org.eclipse.ceylon.aether.apache.maven.settings.building.SettingsBuildingRequest;
import org.eclipse.ceylon.aether.apache.maven.settings.building.SettingsBuildingResult;
import org.eclipse.ceylon.aether.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.ceylon.aether.eclipse.aether.ConfigurationProperties;
import org.eclipse.ceylon.aether.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.ceylon.aether.eclipse.aether.RepositorySystem;
import org.eclipse.ceylon.aether.eclipse.aether.artifact.Artifact;
import org.eclipse.ceylon.aether.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.ceylon.aether.eclipse.aether.collection.CollectRequest;
import org.eclipse.ceylon.aether.eclipse.aether.collection.DependencyCollectionContext;
import org.eclipse.ceylon.aether.eclipse.aether.collection.DependencySelector;
import org.eclipse.ceylon.aether.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.ceylon.aether.eclipse.aether.graph.DefaultDependencyNode;
import org.eclipse.ceylon.aether.eclipse.aether.graph.Dependency;
import org.eclipse.ceylon.aether.eclipse.aether.graph.DependencyFilter;
import org.eclipse.ceylon.aether.eclipse.aether.graph.DependencyNode;
import org.eclipse.ceylon.aether.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.ceylon.aether.eclipse.aether.repository.LocalRepository;
import org.eclipse.ceylon.aether.eclipse.aether.repository.RemoteRepository;
import org.eclipse.ceylon.aether.eclipse.aether.repository.RepositoryPolicy;
import org.eclipse.ceylon.aether.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.ceylon.aether.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.ceylon.aether.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.ceylon.aether.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.ceylon.aether.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.ceylon.aether.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.ceylon.aether.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.ceylon.aether.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.ceylon.aether.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.ceylon.aether.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.ceylon.aether.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.ceylon.aether.eclipse.aether.util.artifact.JavaScopes;
import org.eclipse.ceylon.aether.eclipse.aether.util.repository.AuthenticationBuilder;
import org.eclipse.ceylon.aether.eclipse.aether.util.repository.DefaultAuthenticationSelector;
import org.eclipse.ceylon.aether.eclipse.aether.util.repository.DefaultMirrorSelector;
import org.eclipse.ceylon.aether.eclipse.aether.util.repository.DefaultProxySelector;
import org.eclipse.ceylon.aether.eclipse.aether.version.Version;

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
    private String rootFolderOverride;

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
        
        // if we have a root folder, don't read settings at all
        if(rootFolderOverride == null){
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
        String localRepository = rootFolderOverride;
        if(localRepository == null)
            localRepository = set.getLocalRepository();
        if(localRepository == null)
                localRepository = System.getProperty("user.home")+File.separator+".m2"+File.separator+"repository";
        else if (! new File(localRepository).isAbsolute() && currentDirectory != null)
            localRepository = new File(new File(currentDirectory), localRepository).getAbsolutePath();
        LocalRepository localRepo = new LocalRepository( localRepository );

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
                    proxySelector.add(
                            new org.eclipse.ceylon.aether.eclipse.aether.repository.Proxy(
                                    proxy.getProtocol(), proxy.getHost(), proxy.getPort(), 
                                    auth.build() ), 
                            proxy.getNonProxyHosts());
                }
        }
        session.setProxySelector(proxySelector);
        
        // set up remote repos
        List<RemoteRepository> repos = new ArrayList<>();
        
        RemoteRepository central = new RemoteRepository.Builder( "central", "default", "https://repo1.maven.org/maven2/" ).build();
        repos.add(central);
        
        Set<String> activeProfiles = new HashSet<>();
        activeProfiles.addAll(set.getActiveProfiles());
        for(Profile profile : set.getProfiles()){
            Activation activation = profile.getActivation();
            if(activation != null){
                if(activation.isActiveByDefault())
                    activeProfiles.add(profile.getId());
            }
        }
        for(String profileId : activeProfiles){
            Profile profile = set.getProfilesAsMap().get(profileId);
            if(profile != null){
                addReposFromProfile(repos, profile);
            }
        }
        
        // connection settings
        session.setConfigProperty(ConfigurationProperties.CONNECT_TIMEOUT, timeout);
        session.setOffline(offline || set.isOffline());
        
        session.setLocalRepositoryManager( system.newLocalRepositoryManager( session, localRepo ) );

        return repos;
    }
    
    private void addReposFromProfile(List<RemoteRepository> repos, Profile profile) {
        for(Repository repo : profile.getRepositories()){
            RemoteRepository.Builder remoteRepo = new RemoteRepository.Builder( repo.getId(), repo.getLayout(), repo.getUrl() );

            // policies
            org.eclipse.ceylon.aether.apache.maven.settings.RepositoryPolicy repoReleasePolicy = repo.getReleases();
            if(repoReleasePolicy != null){
                String updatePolicy = repoReleasePolicy.getUpdatePolicy();
                // This is the default anyway and saves us a message on STDERR
                if(updatePolicy == null || updatePolicy.isEmpty())
                    updatePolicy = RepositoryPolicy.UPDATE_POLICY_NEVER;
                RepositoryPolicy releasePolicy = new RepositoryPolicy(repoReleasePolicy.isEnabled(), updatePolicy, 
                        repoReleasePolicy.getChecksumPolicy());
                remoteRepo.setReleasePolicy(releasePolicy );
            }
            
            org.eclipse.ceylon.aether.apache.maven.settings.RepositoryPolicy repoSnapshotPolicy = repo.getSnapshots();
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
    

    public AetherResolverImpl(String currentDirectory, String settingsXml, String rootFolderOverride, boolean offline, int timeout) {
        this.currentDirectory = currentDirectory;
        this.timeout = timeout;
        this.offline = offline;
        this.settingsXml = settingsXml;
        this.rootFolderOverride = rootFolderOverride;
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

        Artifact pomResultArtifact = null;
        // I don't think POMs with a classifier exist, so let's not set it
        DefaultArtifact pomArtifact = new DefaultArtifact( groupId, artifactId, null, "pom", version);
        if(extension == null){
            try {
                pomResultArtifact = resolveSingleArtifact(repoSystem, session, repos, pomArtifact);
            } catch (ArtifactResolutionException e) {
                throw new AetherException(e);
            }
            if(pomResultArtifact != null){
                extension = findExtension(pomResultArtifact.getFile());
            }
            if(extension == null
                    // we only support jar/aar. ear/war/bundle will resolve as jar anyway
                    || (!extension.equals("jar") && !extension.equals("aar")))
                extension = "jar";
        }
        DefaultArtifact artifact = new DefaultArtifact( groupId, artifactId, classifier, extension, version);
        DependencyNode ret = null;
        
        if(!fetchSingleArtifact){
            try {
                ret = resolveArtifactWithDependencies(repoSystem, session, repos, artifact);
            } catch (DependencyResolutionException e) {
                if(!isTimeout(e) && pomResultArtifact == null)
                    throw new AetherException(e);
                // try a jar-less module
            }
            if(ret == null){
                try {
                    ret = resolveArtifactWithDependencies(repoSystem, session, repos, pomArtifact);
                } catch (DependencyResolutionException e) {
                    throw new AetherException(e);
                }
            }
        }else{

            Artifact resultArtifact;
            try {
                resultArtifact = resolveSingleArtifact(repoSystem, session, repos, artifact);
            } catch (ArtifactResolutionException e) {
                if(!isTimeout(e) && pomResultArtifact == null)
                    throw new AetherException(e);
                else // go with a jar-less module
                    resultArtifact = pomResultArtifact;
            }
            ret = new DefaultDependencyNode(resultArtifact);
        }
        
        return ret == null ? null : new DependencyNodeDependencyDescriptor(this, ret);
    }

    private boolean isTimeout(Throwable e) {
        while(e.getCause() != null)
            e = e.getCause();
        // we can't use its real type since we don't import it
        return e.getClass().getSimpleName().endsWith(".ConnectTimeoutException");
    }

    private DependencyNode resolveArtifactWithDependencies(RepositorySystem repoSystem, 
            DefaultRepositorySystemSession session, 
            List<RemoteRepository> repos, 
            DefaultArtifact artifact) 
            throws DependencyResolutionException {
        
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
                if(myEquals(ctx.getDependency(), dependency))
                    return this;
                return NoChildSelector;
            }

            @Override
            public boolean selectDependency(Dependency dep) {
                // Not System, though we could support it
                return JavaScopes.COMPILE.equals(dep.getScope())
                    || JavaScopes.RUNTIME.equals(dep.getScope())
                    || JavaScopes.PROVIDED.equals(dep.getScope())
                    // TEST is useless ATM and is nothing but trouble
//                  || JavaScopes.TEST.equals(dep.getScope())
                            ;
            }
        });

        return repoSystem.resolveDependencies( session, dependencyRequest ).getRoot();
    }

    private Artifact resolveSingleArtifact(RepositorySystem repoSystem, 
            DefaultRepositorySystemSession session, 
            List<RemoteRepository> repos, 
            DefaultArtifact artifact) throws ArtifactResolutionException {
        
        ArtifactRequest artifactRequest = new ArtifactRequest();
        artifactRequest.setArtifact(artifact);
        artifactRequest.setRepositories(repos);

        return repoSystem.resolveArtifact(session, artifactRequest).getArtifact();
    }

    private boolean myEquals(Dependency a, Dependency b) {
        if(a == null && b == null)
            return true;
        if(a == null || b == null)
            return false;
        return myEquals(a.getArtifact(), b.getArtifact())
            && a.isOptional() == b.isOptional()
            && Objects.equals(a.getScope(), b.getScope());
    }

    private boolean myEquals(Artifact a, Artifact b) {
        if(a == null && b == null)
            return true;
        if(a == null || b == null)
            return false;
        // Don't use Artifact.equals because it compares Properties which we don't want
        return Objects.equals(a.getArtifactId(), b.getArtifactId())
            && Objects.equals(a.getGroupId(), b.getGroupId())
            && Objects.equals(a.getVersion(), b.getVersion())
            && Objects.equals(a.getClassifier(), b.getClassifier())
            && Objects.equals(a.getExtension(), b.getExtension())
            && Objects.equals(a.getFile(), b.getFile());
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
