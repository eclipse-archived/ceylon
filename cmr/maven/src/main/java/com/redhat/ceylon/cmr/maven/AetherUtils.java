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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.resolution.VersionResolutionException;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.artifact.JavaScopes;
import org.eclipse.aether.util.repository.AuthenticationBuilder;
import org.eclipse.aether.util.repository.DefaultAuthenticationSelector;
import org.eclipse.aether.util.repository.DefaultMirrorSelector;
import org.eclipse.aether.util.repository.DefaultProxySelector;
import org.eclipse.aether.version.Version;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactOverrides;
import com.redhat.ceylon.cmr.api.CmrRepository;
import com.redhat.ceylon.cmr.api.DependencyOverride;
import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
import com.redhat.ceylon.cmr.api.ModuleVersionArtifact;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionResult;
import com.redhat.ceylon.cmr.api.Overrides;
import com.redhat.ceylon.cmr.api.PathFilterParser;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.impl.AbstractArtifactResult;
import com.redhat.ceylon.cmr.impl.LazyArtifactResult;
import com.redhat.ceylon.cmr.impl.NodeUtils;
import com.redhat.ceylon.cmr.spi.Node;
import com.redhat.ceylon.common.log.Logger;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.ArtifactResultType;
import com.redhat.ceylon.model.cmr.ImportType;
import com.redhat.ceylon.model.cmr.PathFilter;
import com.redhat.ceylon.model.cmr.RepositoryException;

/**
 * Aether utils.
 * <p/>
 * We actually use JBoss ShrinkWrap Resolver.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class AetherUtils {

    private Logger log;
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
        	        	RepositoryPolicy releasePolicy = new RepositoryPolicy(repoReleasePolicy.isEnabled(), repoReleasePolicy.getUpdatePolicy(), 
        	        			repoReleasePolicy.getChecksumPolicy());
        	        	remoteRepo.setReleasePolicy(releasePolicy );
        	        }
        	        
        	        org.apache.maven.settings.RepositoryPolicy repoSnapshotPolicy = repo.getSnapshots();
        	        if(repoSnapshotPolicy != null){
        	        	RepositoryPolicy snapshotPolicy = new RepositoryPolicy(repoSnapshotPolicy.isEnabled(), repoSnapshotPolicy.getUpdatePolicy(), 
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
    

    AetherUtils(Logger log, boolean offline, int timeout) {
        this.log = log;
        this.timeout = timeout;
        this.offline = offline;
        settingsXml = MavenUtils.getDefaultMavenSettings();
    }

    private DependencyNode getDependencies(String groupId, String artifactId, String version, boolean fetchSingleArtifact) 
    		throws org.eclipse.aether.RepositoryException{
    	return getDependencies(groupId, artifactId, version, null, "jar", fetchSingleArtifact);
    }
    
    private DependencyNode getDependencies(String groupId, String artifactId, String version, 
    		String classifier, String extension, boolean fetchSingleArtifact) 
    				throws org.eclipse.aether.RepositoryException{
    	
        RepositorySystem repoSystem = newRepositorySystem();
        DefaultRepositorySystemSession session = newSession( repoSystem );
        List<RemoteRepository> repos = configureSession(repoSystem, session);

        DefaultArtifact artifact = new DefaultArtifact( groupId, artifactId, classifier, extension, version);
        final Dependency dependency = new Dependency( artifact, JavaScopes.COMPILE );

        if(!fetchSingleArtifact){
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

        	return repoSystem.resolveDependencies( session, dependencyRequest ).getRoot();
        }else{
            ArtifactRequest artifactRequest = new ArtifactRequest();
            artifactRequest.setArtifact(artifact);
        	artifactRequest.setRepositories(repos);

        	Artifact resultArtifact = repoSystem.resolveArtifact(session, artifactRequest).getArtifact();
        	return new DefaultDependencyNode(resultArtifact);
        }
    }

    private List<Version> resolveVersionRange(String groupId, String artifactId, String versionRange) throws org.eclipse.aether.RepositoryException{
        RepositorySystem repoSystem = newRepositorySystem();
        DefaultRepositorySystemSession session = newSession( repoSystem );
        List<RemoteRepository> repos = configureSession(repoSystem, session);

        Artifact artifact = new DefaultArtifact( groupId, artifactId, "jar", versionRange );

        VersionRangeRequest rangeRequest = new VersionRangeRequest();
        rangeRequest.setArtifact( artifact );
        rangeRequest.setRepositories(repos);

        VersionRangeResult rangeResult = repoSystem.resolveVersionRange( session, rangeRequest );

        return rangeResult.getVersions();
    }

    List<org.apache.maven.model.Dependency> getDependencies(File pomXml, String name, String version) throws IOException {
    	MavenXpp3Reader reader = new MavenXpp3Reader();
    	Model model;
    	try(FileReader fileReader = new FileReader(pomXml)){
    		model = reader.read(fileReader);
    	} catch (XmlPullParserException e) {
    		throw new IOException(e);
		}
    	return model.getDependencies();
    }

    List<org.apache.maven.model.Dependency> getDependencies(InputStream pomXml, String name, String version) throws IOException {
    	MavenXpp3Reader reader = new MavenXpp3Reader();
    	Model model;
		try {
			model = reader.read(pomXml);
		} catch (XmlPullParserException e) {
			throw new IOException(e);
		}
    	return model.getDependencies();
    }

    void overrideSettingsXml(String settingsXml) {
        if (settingsXml != null) {
            this.settingsXml = settingsXml;
        }
    }

    File findDependency(Node node) {
        final ArtifactResult result = findDependencies(null, node, true);
        return (result != null) ? result.artifact() : null;
    }

    ArtifactResult findDependencies(RepositoryManager manager, Node node) {
        return findDependencies(manager, node, null);
    }

    String[] nameToGroupArtifactIds(String name){
        final int p = name.lastIndexOf(":");
        if (p == -1) {
            return null;
        }
        final String groupId = name.substring(0, p);
        final String artifactId = name.substring(p + 1);
        return new String[]{groupId, artifactId};
    }
    
    private ArtifactResult findDependencies(RepositoryManager manager, Node node, Boolean fetchSingleArtifact) {
        final ArtifactContext ac = ArtifactContext.fromNode(node);
        if (ac == null)
            return null;

        final String name = ac.getName();
        String[] groupArtifactIds = nameToGroupArtifactIds(name);
        if (groupArtifactIds == null) {
            return null;
        }
        final String groupId = groupArtifactIds[0];
        final String artifactId = groupArtifactIds[1];
        final String version = ac.getVersion();

        String repositoryDisplayString = NodeUtils.getRepositoryDisplayString(node);
        CmrRepository repository = NodeUtils.getRepository(node);

        if (CeylonUtils.arrayContains(ac.getSuffixes(), ArtifactContext.LEGACY_SRC)) {
            return fetchWithClassifier(repository, groupId, artifactId, version, "sources", repositoryDisplayString);
        }

        return fetchDependencies(manager, repository, groupId, artifactId, version, fetchSingleArtifact != null ? fetchSingleArtifact : ac.isIgnoreDependencies(), repositoryDisplayString);
    }

    private ArtifactResult fetchDependencies(RepositoryManager manager, CmrRepository repository, 
    		String groupId, String artifactId, String version, 
    		boolean fetchSingleArtifact, String repositoryDisplayString) {
    	
        Overrides overrides = repository.getRoot().getService(Overrides.class);
        ArtifactOverrides ao = null;
        log.debug("Overrides: "+overrides);
        ArtifactContext context = getArtifactContext(groupId, artifactId, version, null, null);
        if(overrides != null){
            ao = overrides.getArtifactOverrides(context);
            log.debug(" ["+context+"] => "+ao);
        }
        // entire replacement
        ArtifactContext replacementContext = null;
        if (ao != null && ao.getReplace() != null) {
            replacementContext = ao.getReplace().getArtifactContext();
        }else if(overrides != null){
            replacementContext = overrides.replace(context);
        }
        if(replacementContext != null){
            log.debug(String.format("[Maven-Overrides] Replacing %s with %s.", context, replacementContext));
            // replace fetched dependency
            String[] nameToGroupArtifactIds = nameToGroupArtifactIds(replacementContext.getName());
            if(nameToGroupArtifactIds != null){
                groupId = nameToGroupArtifactIds[0];
                artifactId = nameToGroupArtifactIds[1];
                version = replacementContext.getVersion();
                // new AO
                context = getArtifactContext(groupId, artifactId, version, null, null);
                ao = overrides.getArtifactOverrides(context);
            }
        }
        // version replacement
        if(overrides != null && overrides.isVersionOverridden(context)){
            version = overrides.getVersionOverride(context);
            context.setVersion(version);
        }

        final String name = toCanonicalForm(groupId, artifactId);
        final String coordinates = toCanonicalForm(name, version);
        try {
        	DependencyNode info = getDependencies(groupId, artifactId, version, fetchSingleArtifact);
            if (info == null) {
                log.debug("No artifact found: " + coordinates);
                return null;
            }

            final SingleArtifactResult result;
            if (fetchSingleArtifact) {
                result = new SingleArtifactResult(repository, name, version, info.getArtifact().getFile(), repositoryDisplayString);
            } else {
                final List<ArtifactResult> dependencies = new ArrayList<>();

                for (DependencyNode dep : info.getChildren()) {
                    final Artifact dCo = dep.getArtifact();
                    String dGroupId = dCo.getGroupId();
                    String dArtifactId = dCo.getArtifactId();
                    String dVersion = dCo.getVersion();
                    boolean export = false;
                    boolean optional = dep.getDependency().isOptional();
                    boolean isCeylon = false;
                    ArtifactContext dContext = null;
                    if(overrides != null)
                        dContext = getArtifactContext(dGroupId, dArtifactId, dVersion, null, null);

                    if (overrides != null) {
                        if (overrides.isRemoved(dContext) 
                                || (ao != null && ao.isRemoved(dContext))) {
                            log.debug(String.format("[Maven-Overrides] Removing %s from %s.", dCo, context));
                            continue; // skip dependency
                        }
                        if (ao != null && ao.isAddedOrUpdated(dContext)) {
                            log.debug(String.format("[Maven-Overrides] Replacing %s from %s.", dCo, context));
                            continue; // skip dependency
                        }
                        ArtifactContext replace = overrides.replace(dContext);
                        if(replace != null){
                            dContext = replace;
                            String[] groupArtifactIds = nameToGroupArtifactIds(replace.getName());
                            if(groupArtifactIds == null){
                                isCeylon = true;
                            }else{
                                dGroupId = groupArtifactIds[0];
                                dArtifactId = groupArtifactIds[1];
                            }
                            dVersion = replace.getVersion();
                        }
                        if(ao != null){
                            if(ao.isShareOverridden(dContext))
                                export = ao.isShared(dContext);
                            if(ao.isOptionalOverridden(dContext))
                                optional = ao.isOptional(dContext);
                        }
                    }

                    // do we have a version update?
                    if(overrides != null && overrides.isVersionOverridden(dContext)){
                        dVersion = overrides.getVersionOverride(dContext);
                    }
                    
                    ArtifactResult dr;
                    if(isCeylon)
                        dr = createArtifactResult(manager, dContext.getName(), dVersion, export, optional, repositoryDisplayString);
                    else
                        dr = createArtifactResult(manager, repository, dGroupId, dArtifactId, dVersion, export, optional, repositoryDisplayString);
                    dependencies.add(dr);
                }

                if (ao != null) {
                    for (DependencyOverride addon : ao.getAdd()) {
                        ArtifactContext dContext = addon.getArtifactContext();
                        String dVersion = overrides.getVersionOverride(dContext);
                        dependencies.add(createArtifactResult(manager, repository, dContext, dVersion, 
                                addon.isShared(), addon.isOptional(), repositoryDisplayString));
                        log.debug(String.format("[Maven-Overrides] Added %s to %s.", addon.getArtifactContext(), context));
                    }
                }

                result = new AetherArtifactResult(repository, name, version, info.getArtifact().getFile(), dependencies, repositoryDisplayString);
            }

            if (ao != null && ao.getFilter() != null) {
                result.setFilter(PathFilterParser.parse(ao.getFilter()));
            }

            return result;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (org.eclipse.aether.RepositoryException e) {
          log.debug("Could not resolve artifact [" + coordinates + "] : " + e);
          return null;
		}
    }

    public void search(String groupId, String artifactId, String version, ModuleVersionResult result, 
    		Overrides overrides, String repositoryDisplayString){

    	try{
        if(version == null || version.isEmpty()){
        	List<Version> versions = resolveVersionRange(groupId, artifactId, "(,)");
            for(Version co : versions){
            	String resolvedVersion = co.toString();
                if(resolvedVersion != null && !resolvedVersion.isEmpty())
                    addSearchResult(groupId, artifactId, resolvedVersion, result, overrides, repositoryDisplayString);
            }
        }else{
            try{
            	List<Version> versions = resolveVersionRange(groupId, artifactId, "["+version+",]");
                for(Version co : versions){
                	String resolvedVersion = co.toString();
                    // make sure the version matches because with maven if we ask for [1,] we also get 2.x
                    if(resolvedVersion != null && resolvedVersion.startsWith(version))
                        addSearchResult(groupId, artifactId, resolvedVersion, result, overrides, repositoryDisplayString);
                }
            }catch(VersionResolutionException x){
                // if we got a checksum error (like for jetty) we retry with a fixed version query
                addSearchResult(groupId, artifactId, version, result, overrides, repositoryDisplayString);
            }
        }
        } catch (org.eclipse.aether.RepositoryException e) {
            log.debug("Could not search for artifact versions [" + groupId+":"+artifactId+":"+version + "] : " + e);
  		}
    }

    private void addSearchResult(String groupId, String artifactId, String version, ModuleVersionResult result, Overrides overrides, String repositoryDisplayString) throws org.eclipse.aether.RepositoryException {
        ArtifactOverrides artifactOverrides = null;
        if(overrides != null){
            ArtifactContext ctx = new ArtifactContext(groupId+":"+artifactId, version);
            // see if this artifact is replaced
            ArtifactContext replaceContext = overrides.replace(ctx);
            if(replaceContext != null){
                String[] groupArtifactIds = nameToGroupArtifactIds(replaceContext.getName());
                if(groupArtifactIds == null)
                    return; // abort
                groupId = groupArtifactIds[0];
                artifactId = groupArtifactIds[1];
                version = replaceContext.getVersion();
                ctx = replaceContext;
            }else if(overrides.isVersionOverridden(ctx)){
                // perhaps its version is overridden?
                version = overrides.getVersionOverride(ctx);
                ctx.setVersion(version);
            }
            artifactOverrides = overrides.getArtifactOverrides(ctx);
        }
    	DependencyNode info = getDependencies(groupId, artifactId, version, null, "pom", false);
        if(info != null){
            StringBuilder description = new StringBuilder();
            StringBuilder licenseBuilder = new StringBuilder();
            collectInfo(info, description, licenseBuilder);
            Set<ModuleDependencyInfo> dependencies = new HashSet<>();
            Set<ModuleVersionArtifact> artifactTypes = new HashSet<>();
            artifactTypes.add(new ModuleVersionArtifact(".jar", null, null));
            Set<String> authors = new HashSet<>();
            for(DependencyNode dep : info.getChildren()){
                Artifact depCo = dep.getArtifact();
                String depName = depCo.getGroupId()+":"+depCo.getArtifactId();
                String depVersion = depCo.getVersion();
                boolean export = false;
                boolean optional = dep.getDependency().isOptional();
                if(overrides != null){
                    ArtifactContext depCtx = new ArtifactContext(depName, depCo.getVersion());
                    if(overrides.isRemoved(depCtx)
                            || (artifactOverrides != null 
                                && (artifactOverrides.isRemoved(depCtx)
                                        || artifactOverrides.isAddedOrUpdated(depCtx))))
                        continue;
                    ArtifactContext replaceCtx = overrides.replace(depCtx);
                    if(replaceCtx != null){
                        depCtx = replaceCtx;
                        depName = replaceCtx.getName();
                    }
                    if(overrides.isVersionOverridden(depCtx))
                        depVersion = overrides.getVersionOverride(depCtx);
                    if(artifactOverrides != null){
                        if(artifactOverrides.isShareOverridden(depCtx))
                            export = artifactOverrides.isShared(depCtx);
                        if(artifactOverrides.isOptionalOverridden(depCtx))
                            optional = artifactOverrides.isOptional(depCtx);
                    }
                }
                ModuleDependencyInfo moduleDependencyInfo = new ModuleDependencyInfo(depName, depVersion, optional, export);
                dependencies.add(moduleDependencyInfo);
            }
            if(artifactOverrides != null){
                for(DependencyOverride add : artifactOverrides.getAdd()){
                    ModuleDependencyInfo moduleDependencyInfo = new ModuleDependencyInfo(add.getArtifactContext().getName(), 
                            add.getArtifactContext().getVersion(), add.isOptional(), add.isShared());
                    dependencies.add(moduleDependencyInfo);
                }
            }
            ModuleVersionDetails moduleVersionDetails = new ModuleVersionDetails(groupId+":"+artifactId, version, 
                    description.length() > 0 ? description.toString() : null,
                    licenseBuilder.length() > 0 ? licenseBuilder.toString() : null,
                    authors, dependencies, artifactTypes , true, repositoryDisplayString);
            result.addVersion(moduleVersionDetails);
        }
    }

    private void collectInfo(DependencyNode info, StringBuilder description, StringBuilder licenseBuilder) {
        File pomFile = info.getArtifact().getFile();
        if(pomFile != null && pomFile.exists()){
        	try(InputStream is = new FileInputStream(pomFile)) {
        		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        		Document doc = dBuilder.parse(is);
        		doc.getDocumentElement().normalize();
        		Element root = doc.getDocumentElement();
        		collectText(root, description, "name", "description", "url");
        		Element licenses = getFirstElement(root, "licenses");
        		if(licenses != null){
        			Element license = getFirstElement(licenses, "license");
        			if(license != null){
        				collectText(license, licenseBuilder, "name", "url");
        			}
        		}
        	} catch (IOException e) {
        		// ignore, no info
        		e.printStackTrace();
        	} catch (ParserConfigurationException e) {
        		// ignore, no info
        		e.printStackTrace();
        	} catch (SAXException e) {
        		// ignore, no info
        		e.printStackTrace();
        	}
        };
    }

    private String getText(Element element, String childName){
        NodeList elems = element.getElementsByTagName(childName);
        if(elems != null && elems.getLength() > 0){
            return elems.item(0).getTextContent();
        }
        return null;
    }

    private Element getFirstElement(Element element, String childName){
        NodeList elems = element.getElementsByTagName(childName);
        if(elems != null && elems.getLength() > 0 && elems.item(0) instanceof Element){
            return (Element) elems.item(0);
        }
        return null;
    }

    private void collectText(Element element, StringBuilder builder, String... tags){
        for(String tag : tags){
            String desc = getText(element, tag);
            if(desc != null){
                if(builder.length() > 0)
                    builder.append("\n");
                builder.append(desc);
            }
        }
    }
    
    private ArtifactContext getArtifactContext(String groupId, String artifactId, String version, String packaging, String classifier){
        if(classifier != null && classifier.isEmpty())
            classifier = null;
        return Overrides.createMavenArtifactContext(groupId, artifactId, version,
                packaging, classifier);
    }

    protected ArtifactResult createArtifactResult(RepositoryManager manager, CmrRepository repository, final ArtifactContext dCo, String version, 
            final boolean shared, boolean optional, final String repositoryDisplayString) {
        String[] groupArtifactIds = nameToGroupArtifactIds(dCo.getName());
        if(groupArtifactIds == null)
            return createArtifactResult(manager, dCo.getName(), version, 
                    shared, optional, repositoryDisplayString);
        return createArtifactResult(manager, repository, groupArtifactIds[0], groupArtifactIds[1], version, 
                shared, optional, repositoryDisplayString);
    }

    protected ArtifactResult createArtifactResult(final RepositoryManager manager, CmrRepository repository, final String groupId, final String artifactId, final String dVersion, 
            final boolean shared, final boolean optional, final String repositoryDisplayString) {
        final String dName = toCanonicalForm(groupId, artifactId);

        return new MavenArtifactResult(repository, dName, dVersion, repositoryDisplayString) {
            private ArtifactResult result;

            @Override
            public ImportType importType() {
                return shared ? ImportType.EXPORT : (optional ? ImportType.OPTIONAL : ImportType.UNDEFINED);
            }

            private synchronized ArtifactResult getResult() {
                if (result == null) {
                    result = fetchDependencies(manager, (CmrRepository) repository(), groupId, artifactId, dVersion, false, repositoryDisplayString);
                }
                return result;
            }

            protected File artifactInternal() throws RepositoryException {
                return getResult().artifact();
            }

            public List<ArtifactResult> dependencies() throws RepositoryException {
                return getResult().dependencies();
            }
        };
    }

    protected ArtifactResult createArtifactResult(RepositoryManager manager, final String module, final String dVersion, 
            final boolean shared, final boolean optional, final String repositoryDisplayString) {

        return new LazyArtifactResult(manager, module, dVersion, shared ? ImportType.EXPORT : (optional ? ImportType.OPTIONAL : ImportType.UNDEFINED));
    }

    private ArtifactResult fetchWithClassifier(CmrRepository repository, String groupId, String artifactId, String version, String classifier, String repositoryDisplayString) {
        final String name = toCanonicalForm(groupId, artifactId);
        final String coordinates = toCanonicalForm(toCanonicalForm(toCanonicalForm(name, "jar"), classifier), version);
        try {
        	DependencyNode info = getDependencies(groupId, artifactId, version, classifier, "jar", true);
            if (info != null) {
                return new SingleArtifactResult(repository, name, version, info.getArtifact().getFile(), repositoryDisplayString);
            }
        } catch (org.eclipse.aether.RepositoryException e) {
        	log.debug("Could not resolve " + classifier + " for artifact [" + coordinates + "] : " + e);
		}

        log.debug("No artifact found: " + coordinates);
        return null;
    }

    static String toCanonicalForm(String groupId, String artifactId) {
        return groupId + ":" + artifactId;
    }

    private static abstract class MavenArtifactResult extends AbstractArtifactResult {
        private String repositoryDisplayString;

        protected MavenArtifactResult(CmrRepository repository, String name, String version, String repositoryDisplayString) {
            super(repository, name, version);
            this.repositoryDisplayString = repositoryDisplayString;
        }

        public ArtifactResultType type() {
            return ArtifactResultType.MAVEN;
        }

        @Override
        public String repositoryDisplayString() {
            return repositoryDisplayString;
        }
    }

    private static class SingleArtifactResult extends MavenArtifactResult {
        private File file;

        private SingleArtifactResult(CmrRepository repository, String name, String version, File file, String repositoryDisplayString) {
            super(repository, name, version, repositoryDisplayString);
            this.file = file;
        }

        protected File artifactInternal() throws RepositoryException {
            return file;
        }

        void setFilter(PathFilter filter) {
            setFilterInternal(filter);
        }

        public List<ArtifactResult> dependencies() throws RepositoryException {
            return Collections.emptyList();
        }
    }

    private static class AetherArtifactResult extends SingleArtifactResult {
        private List<ArtifactResult> dependencies;

        private AetherArtifactResult(CmrRepository repository, String name, String version, File file, List<ArtifactResult> dependencies, String repositoryDisplayString) {
            super(repository, name, version, file, repositoryDisplayString);
            this.dependencies = dependencies;
        }

        public List<ArtifactResult> dependencies() throws RepositoryException {
            return Collections.unmodifiableList(dependencies);
        }
    }
}
