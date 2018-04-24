/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.maven;

import java.io.File;
import java.io.FileInputStream;
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

import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.api.ArtifactOverrides;
import org.eclipse.ceylon.cmr.api.CmrRepository;
import org.eclipse.ceylon.cmr.api.DependencyOverride;
import org.eclipse.ceylon.cmr.api.MavenArtifactContext;
import org.eclipse.ceylon.cmr.api.ModuleDependencyInfo;
import org.eclipse.ceylon.cmr.api.ModuleVersionArtifact;
import org.eclipse.ceylon.cmr.api.ModuleVersionDetails;
import org.eclipse.ceylon.cmr.api.ModuleVersionResult;
import org.eclipse.ceylon.cmr.api.Overrides;
import org.eclipse.ceylon.cmr.api.PathFilterParser;
import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.cmr.ceylon.CeylonUtils;
import org.eclipse.ceylon.cmr.impl.AbstractArtifactResult;
import org.eclipse.ceylon.cmr.impl.LazyArtifactResult;
import org.eclipse.ceylon.cmr.impl.MavenRepository;
import org.eclipse.ceylon.cmr.impl.NodeUtils;
import org.eclipse.ceylon.cmr.resolver.aether.AetherException;
import org.eclipse.ceylon.cmr.resolver.aether.AetherResolver;
import org.eclipse.ceylon.cmr.resolver.aether.AetherResolverImpl;
import org.eclipse.ceylon.cmr.resolver.aether.DependencyDescriptor;
import org.eclipse.ceylon.cmr.resolver.aether.ExclusionDescriptor;
import org.eclipse.ceylon.cmr.spi.Node;
import org.eclipse.ceylon.common.Backends;
import org.eclipse.ceylon.common.log.Logger;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.cmr.ArtifactResultType;
import org.eclipse.ceylon.model.cmr.Exclusion;
import org.eclipse.ceylon.model.cmr.ModuleScope;
import org.eclipse.ceylon.model.cmr.PathFilter;
import org.eclipse.ceylon.model.cmr.RepositoryException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Aether utils.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class AetherUtils {

    private Logger log;
    private AetherResolver impl;

    AetherUtils(Logger log, String settingsXml, String rootFolderOverride, boolean offline, int timeout, String currentDirectory) {
        this.log = log;
        if(settingsXml == null)
            settingsXml = MavenUtils.getDefaultMavenSettings();
        impl = new AetherResolverImpl(currentDirectory, settingsXml, rootFolderOverride, offline, timeout);
    }

    File findDependency(Node node) {
        final ArtifactResult result = findDependencies(null, node, true);
        return (result != null) ? result.artifact() : null;
    }

    public File getLocalRepositoryBaseDir() {
        return impl.getLocalRepositoryBaseDir();
    }

    ArtifactResult findDependencies(RepositoryManager manager, Node node) {
        return findDependencies(manager, node, null);
    }

    String[] nameToGroupArtifactIds(String name){
        final String groupId;
        final String artifactId;
        final String classifier;
        
        final int p = name.lastIndexOf(":");
        if (p == -1) {
            return null;
        }
        final int q = name.substring(0, p).lastIndexOf(":");
        if (q == -1) {
            groupId = name.substring(0, p);
            artifactId = name.substring(p + 1);
            classifier = null;
        }
        else {
            groupId = name.substring(0, q);
            artifactId = name.substring(q + 1, p);
            classifier = name.substring(p + 1);
        }
        return new String[]{groupId, artifactId, classifier};
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
        String groupId = groupArtifactIds[0];
        String artifactId = groupArtifactIds[1];
        String classifier = groupArtifactIds[2];
        String version = ac.getVersion();

        String repositoryDisplayString = NodeUtils.getRepositoryDisplayString(node);
        CmrRepository repository = NodeUtils.getRepository(node);

        if (CeylonUtils.arrayContains(ac.getSuffixes(), ArtifactContext.LEGACY_SRC)) {
            classifier = "sources";
        }
        
        return fetchDependencies(manager, repository, groupId, artifactId, classifier, version, 
                fetchSingleArtifact != null ? fetchSingleArtifact : ac.isIgnoreDependencies(), 
                        repositoryDisplayString);
    }

    private ArtifactResult fetchDependencies(RepositoryManager manager, CmrRepository repository, 
            String groupId, String artifactId, String classifier, String version,
            boolean fetchSingleArtifact, String repositoryDisplayString) {
        
        Overrides overrides = repository.getRoot().getService(Overrides.class);
        ArtifactOverrides ao = null;
        log.debug("Overrides for "+canonicalForm(groupId, artifactId, classifier, version)+" in "+overrides.getSource());
        ArtifactContext context = getArtifactContext(groupId, artifactId, classifier, version);
        if(overrides != null){
            ao = overrides.getArtifactOverrides(context);
            log.debug(ao!=null ? "-> found overrides" : "-> no overrides");
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
                classifier = nameToGroupArtifactIds[2];
                version = replacementContext.getVersion();
                // new AO
                context = getArtifactContext(groupId, artifactId, classifier, version);
                ao = overrides.getArtifactOverrides(context);
            }
        }
        // version replacement
        if(overrides != null && overrides.isVersionOverridden(context)){
            version = overrides.getVersionOverride(context);
            context.setVersion(version);
        }
        if(ao != null && ao.hasVersion()){
            version = ao.getVersion();
            context.setVersion(version);
            log.debug("Using version "+version);
        }
        // classifier replacement
        if(ao != null && ao.hasClassifier()){
            classifier = ao.getClassifier();
            log.debug("Using classifier "+classifier);
        }

        final String name = MavenUtils.moduleName(groupId, artifactId, classifier);
        final String coordinates = canonicalForm(groupId, artifactId, classifier, version); //only used for messages
        try {
            DependencyDescriptor info = impl.getDependencies(groupId, artifactId, version, classifier, null, fetchSingleArtifact);
            if (info == null) {
                log.debug("No artifact found: " + coordinates);
                return null;
            }

            final SingleArtifactResult result;
            if (fetchSingleArtifact) {
                result = new SingleArtifactResult(repository, name, version, groupId, artifactId, classifier,
                        info.getFile(), repositoryDisplayString);
            } else {
                final List<ArtifactResult> dependencies = new ArrayList<>();

                for (DependencyDescriptor dep : info.getDependencies()) {
                    String dGroupId = dep.getGroupId();
                    String dArtifactId = dep.getArtifactId();
                    String dClassifier = dep.getClassifier();
                    String dVersion = dep.getVersion();
                    boolean export = false;
                    boolean optional = dep.isOptional();
                    boolean isCeylon = false;
                    ModuleScope scope = toModuleScope(dep);
                    ArtifactContext dContext = null;
                    if(overrides != null)
                        dContext = getArtifactContext(dGroupId, dArtifactId, dClassifier, dVersion);

                    if (overrides != null) {
                        if (overrides.isRemoved(dContext) 
                                || (ao != null && ao.isRemoved(dContext))) {
                            log.debug(String.format("[Maven-Overrides] Removing %s from %s.", dep, context));
                            continue; // skip dependency
                        }
                        if (ao != null && ao.isAddedOrUpdated(dContext)) {
                            log.debug(String.format("[Maven-Overrides] Replacing %s from %s.", dep, context));
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
                                dClassifier = groupArtifactIds[2];
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
                        dr = createArtifactResult(manager, dContext.getNamespace(), dContext.getName(), dVersion, 
                                export, optional, scope, repositoryDisplayString);
                    else
                        dr = createArtifactResult(manager, repository, dGroupId, dArtifactId, dClassifier, dVersion, 
                                export, optional, scope, repositoryDisplayString,
                                dep.getExclusions());
                    dependencies.add(dr);
                }

                if (ao != null) {
                    for (DependencyOverride addon : ao.getAdd()) {
                        ArtifactContext dContext = addon.getArtifactContext();
                        String dVersion = overrides.getVersionOverride(dContext);
                        dependencies.add(createArtifactResult(manager, repository, dContext, dVersion, 
                                addon.isShared(), addon.isOptional(), ModuleScope.COMPILE, repositoryDisplayString, null));
                        log.debug(String.format("[Maven-Overrides] Added %s to %s.", addon.getArtifactContext(), context));
                    }
                }

                result = new AetherArtifactResult(repository, name, version, groupId, artifactId, classifier,
                        info.getFile(), dependencies, repositoryDisplayString);
            }

            if (ao != null && ao.getFilter() != null) {
                result.setFilter(PathFilterParser.parse(ao.getFilter()));
            }

            return result;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (AetherException e) {
          log.debug("Could not resolve artifact [" + coordinates + "] : " + e);
          return null;
        }
    }

    public static ModuleScope toModuleScope(DependencyDescriptor dep) {
        if(dep.isRuntimeScope())
            return ModuleScope.RUNTIME;
        if(dep.isTestScope())
            return ModuleScope.TEST;
        if(dep.isProvidedScope())
            return ModuleScope.PROVIDED;
        return ModuleScope.COMPILE;
    }

    public void search(String groupId, String artifactId, String version, 
            boolean exactVersionMatch, ModuleVersionResult result, 
            Overrides overrides, String repositoryDisplayString){

        try{
            if(version == null || version.isEmpty()){
                List<String> versions = impl.resolveVersionRange(groupId, artifactId, "(,)");
                for(String resolvedVersion : versions){
                    if(resolvedVersion != null && !resolvedVersion.isEmpty())
                        addSearchResult(groupId, artifactId, resolvedVersion, result, overrides, repositoryDisplayString);
                }
            }else if(exactVersionMatch){
                addSearchResult(groupId, artifactId, version, result, overrides, repositoryDisplayString);
            }else{
                List<String> versions = impl.resolveVersionRange(groupId, artifactId, "["+version+",]");
                for(String resolvedVersion : versions){
                    // make sure the version matches because with maven if we ask for [1,] we also get 2.x
                    if(resolvedVersion != null && resolvedVersion.startsWith(version))
                        addSearchResult(groupId, artifactId, resolvedVersion, result, overrides, repositoryDisplayString);
                }
            }
        } catch (AetherException e) {
            log.debug("Could not search for artifact versions [" + groupId+":"+artifactId+":"+version + "] : " + e);
        }
    }

    private void addSearchResult(String groupId, String artifactId, String version, ModuleVersionResult result, 
            Overrides overrides, String repositoryDisplayString) 
                    throws AetherException {
        ArtifactOverrides artifactOverrides = null;
        String classifier = null;
        if(overrides != null){
            ArtifactContext ctx = new ArtifactContext(MavenRepository.NAMESPACE, groupId+":"+artifactId, version);
            // see if this artifact is replaced
            ArtifactContext replaceContext = overrides.replace(ctx);
            if(replaceContext != null){
                String[] groupArtifactIds = nameToGroupArtifactIds(replaceContext.getName());
                if(groupArtifactIds == null)
                    return; // abort
                groupId = groupArtifactIds[0];
                artifactId = groupArtifactIds[1];
                classifier = groupArtifactIds[2];
                version = replaceContext.getVersion();
                ctx = replaceContext;
            }else if(overrides.isVersionOverridden(ctx)){
                // perhaps its version is overridden?
                version = overrides.getVersionOverride(ctx);
                ctx.setVersion(version);
            }
            artifactOverrides = overrides.getArtifactOverrides(ctx);
        }
        DependencyDescriptor info = impl.getDependencies(groupId, artifactId, version, classifier, "pom", false);
        if(info != null){
            StringBuilder description = new StringBuilder();
            StringBuilder licenseBuilder = new StringBuilder();
            collectInfo(info, description, licenseBuilder);
            Set<ModuleDependencyInfo> dependencies = new HashSet<>();
            Set<ModuleVersionArtifact> artifactTypes = new HashSet<>();
            artifactTypes.add(new ModuleVersionArtifact(".jar", null, null));
            Set<String> authors = new HashSet<>();
            for(DependencyDescriptor dep : info.getDependencies()){
                String namespace = MavenRepository.NAMESPACE;
                String depName = MavenUtils.moduleName(dep.getGroupId(),dep.getArtifactId(),dep.getClassifier());
                String depVersion = dep.getVersion();
                boolean export = false;
                boolean optional = dep.isOptional();
                if(overrides != null){
                    ArtifactContext depCtx = new ArtifactContext(namespace, depName, dep.getVersion());
                    if(overrides.isRemoved(depCtx)
                            || (artifactOverrides != null 
                                && (artifactOverrides.isRemoved(depCtx)
                                        || artifactOverrides.isAddedOrUpdated(depCtx))))
                        continue;
                    ArtifactContext replaceCtx = overrides.replace(depCtx);
                    if(replaceCtx != null){
                        depCtx = replaceCtx;
                        namespace = replaceCtx.getNamespace();
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
                ModuleDependencyInfo moduleDependencyInfo = new ModuleDependencyInfo(
                        namespace, depName, depVersion,
                        optional, export, 
                        Backends.JAVA, 
                        toModuleScope(dep));
                dependencies.add(moduleDependencyInfo);
            }
            if(artifactOverrides != null){
                for(DependencyOverride add : artifactOverrides.getAdd()){
                    ArtifactContext ac = add.getArtifactContext();
                    ModuleDependencyInfo moduleDependencyInfo = new ModuleDependencyInfo(
                            ac.getNamespace(),
                            ac.getName(), 
                            ac.getVersion(),
                            add.isOptional(), 
                            add.isShared(),
                            Backends.JAVA,
                            ModuleScope.COMPILE);
                    dependencies.add(moduleDependencyInfo);
                }
            }
            ModuleVersionDetails moduleVersionDetails = new ModuleVersionDetails(
                    MavenRepository.NAMESPACE,
                    groupId+":"+artifactId, version, 
                    groupId, artifactId, 
                    null,
                    description.length() > 0 ? description.toString() : null,
                    licenseBuilder.length() > 0 ? licenseBuilder.toString() : null,
                    authors, dependencies, artifactTypes , true, repositoryDisplayString);
            result.addVersion(moduleVersionDetails);
        }
    }

    private void collectInfo(DependencyDescriptor info, StringBuilder description, StringBuilder licenseBuilder) {
        File pomFile = info.getFile();
        if(pomFile != null && pomFile.exists()){
            try(InputStream is = new FileInputStream(pomFile)) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(is);
                doc.getDocumentElement().normalize();
                Element root = doc.getDocumentElement();
                MavenUtils.collectText(root, description, "name", "description", "url");
                Element licenses = MavenUtils.getFirstElement(root, "licenses");
                if(licenses != null){
                    Element license = MavenUtils.getFirstElement(licenses, "license");
                    if(license != null){
                        MavenUtils.collectText(license, licenseBuilder, "name", "url");
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

    private ArtifactContext getArtifactContext(String groupId, String artifactId, String classifier, String version){
        if(classifier != null && classifier.isEmpty())
            classifier = null;
        return new MavenArtifactContext(groupId, artifactId, classifier, version, null);
    }

    protected ArtifactResult createArtifactResult(RepositoryManager manager, CmrRepository repository, 
            final ArtifactContext dCo, String version, 
            final boolean shared, boolean optional, ModuleScope scope, final String repositoryDisplayString,
            List<ExclusionDescriptor> exclusions) {
        String[] groupArtifactIds = nameToGroupArtifactIds(dCo.getName());
        if(groupArtifactIds == null)
            return createArtifactResult(manager, dCo.getNamespace(), dCo.getName(), version, 
                    shared, optional, scope, repositoryDisplayString);
        String groupId = groupArtifactIds[0];
        String artifactId = groupArtifactIds[1];
        String classifier = groupArtifactIds[2];
        return createArtifactResult(manager, repository, groupId, artifactId, classifier, version, 
                shared, optional, scope, repositoryDisplayString, exclusions);
    }

    protected ArtifactResult createArtifactResult(final RepositoryManager manager, CmrRepository repository, 
            final String groupId, final String artifactId, final String classifier, final String dVersion, 
            final boolean shared, final boolean optional, final ModuleScope scope, final String repositoryDisplayString,
            final List<ExclusionDescriptor> exclusions) {
        
        final String dName = MavenUtils.moduleName(groupId, artifactId, classifier);

        return new MavenArtifactResult(repository, dName, dVersion, groupId, artifactId, classifier, repositoryDisplayString) {
            private ArtifactResult result;
            
            {
                if(exclusions != null){
                    List<Exclusion> ret = new ArrayList<>(exclusions.size());
                    for (ExclusionDescriptor xDescr : exclusions) {
                        ret.add(new Exclusion(xDescr.getGroupId(), xDescr.getArtifactId()));
                    }
                    setExclusions(ret);
                }
            }

            @Override
            public boolean exported() {
                return shared;
            }
            
            @Override
            public boolean optional() {
                return optional;
            }

            @Override
            public ModuleScope moduleScope() {
                return scope;
            }
            
            private synchronized ArtifactResult getResult() {
                if (result == null) {
                    result = fetchDependencies(manager, (CmrRepository) repository(), groupId, artifactId, classifier, dVersion, false, repositoryDisplayString);
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

    protected ArtifactResult createArtifactResult(RepositoryManager manager, String namespace, final String module, final String dVersion,
            final boolean shared, final boolean optional, ModuleScope scope, final String repositoryDisplayString) {
        return new LazyArtifactResult(manager, namespace, module, dVersion, shared, optional, scope);
    }

    private static String canonicalForm(String groupId, String artifactId, String classifier, String version) {
        return classifier==null || classifier.isEmpty() ?
                groupId + ":" + artifactId + ":" + version :
                groupId + ":" + artifactId + ":" + classifier + ":" + version;
    }

    private static abstract class MavenArtifactResult extends AbstractArtifactResult {
        private String repositoryDisplayString;
        private String artifactId;
        private String groupId;
        private String classifier;

        protected MavenArtifactResult(CmrRepository repository, 
                String name, String version, String groupId, String artifactId, 
                String classifier, String repositoryDisplayString) {
            super(repository, MavenRepository.NAMESPACE, name, version);
            this.repositoryDisplayString = repositoryDisplayString;
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.classifier = classifier;
        }

        @Override
        public String groupId() {
            return groupId;
        }
        
        @Override
        public String artifactId() {
            return artifactId;
        }
        
        @Override
        public String classifier() {
            return classifier;
        }
        
        @Override
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

        private SingleArtifactResult(CmrRepository repository, String name, String version,
                String groupId, String artifactId, String classifier,
                File file, String repositoryDisplayString) {
            super(repository, name, version, groupId, artifactId, classifier, repositoryDisplayString);
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

        private AetherArtifactResult(CmrRepository repository, String name, String version,
                String groupId, String artifactId, String classifier,
                File file, List<ArtifactResult> dependencies, String repositoryDisplayString) {
            super(repository, name, version, groupId, artifactId, classifier, file, repositoryDisplayString);
            this.dependencies = dependencies;
        }

        public List<ArtifactResult> dependencies() throws RepositoryException {
            return Collections.unmodifiableList(dependencies);
        }
    }

    public DependencyDescriptor getDependencies(InputStream stream, String name, String version) throws IOException {
        return impl.getDependencies(stream, name, version);
    }

    public DependencyDescriptor getDependencies(File pomXml, String name, String version) throws IOException {
        return impl.getDependencies(pomXml, name, version);
    }
}
