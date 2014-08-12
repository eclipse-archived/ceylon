package com.redhat.ceylon.cmr.ceylon;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.NavigableMap;
import java.util.Set;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.JDKUtils;
import com.redhat.ceylon.cmr.api.ModuleInfo;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.api.ModuleVersionQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionResult;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.impl.CMRJULLogger;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.log.Logger;
import com.redhat.ceylon.common.tools.ModuleSpec;

public class ModuleCopycat {
    private RepositoryManager srcRepoman;
    private RepositoryManager dstRepoman;
    private CopycatFeedback feedback;
    private Logger log;
    
    private Set<String> copiedModules;

    public static interface CopycatFeedback {
        void copyModule(String moduleName, String moduleVersion) throws Exception;
        void copyArtifact(ArtifactContext ac, File archive) throws Exception;
        void notFound(String moduleName, String moduleVersion) throws Exception;
    }
    
    public ModuleCopycat(RepositoryManager srcRepoman, RepositoryManager dstRepoman) {
        this(srcRepoman, dstRepoman, null);
    }
    
    public ModuleCopycat(RepositoryManager srcRepoman, RepositoryManager dstRepoman, CopycatFeedback feedback) {
        this(srcRepoman, dstRepoman, new CMRJULLogger(), feedback);
    }
    
    public ModuleCopycat(RepositoryManager srcRepoman, RepositoryManager dstRepoman, Logger log, CopycatFeedback feedback) {
        this.srcRepoman = srcRepoman;
        this.dstRepoman = dstRepoman;
        this.feedback = feedback;
        this.log = log;
        this.copiedModules = new HashSet<String>();
    }
    
    public void copyModule(ArtifactContext context) throws Exception {
        String module = ModuleUtil.makeModuleName(context.getName(), context.getVersion());
        if (!copiedModules.add(module)) {
            return;
        }
        if (!JDKUtils.isJDKModule(context.getName()) && !JDKUtils.isOracleJDKModule(context.getName())) {
            Collection<ModuleVersionDetails> versions = getModuleVersions(srcRepoman, context.getName(), context.getVersion(), ModuleQuery.Type.ALL, null, null);
            if (!versions.isEmpty()) {
                ModuleVersionDetails ver = versions.iterator().next();
                if (feedback != null) {
                    feedback.copyModule(context.getName(), context.getVersion());
                }
                ArtifactResult results[] = srcRepoman.getArtifactResults(context);
                for (ArtifactResult r : results) {
                    copyArtifact(context, r.artifact());
                }
                if (!context.isFetchSingleArtifact()) {
                    for (ModuleInfo dep : ver.getDependencies()) {
                        ModuleSpec depModule = new ModuleSpec(dep.getName(), dep.getVersion());
                        ArtifactContext depContext = context.copy();
                        depContext.setName(depModule.getName());
                        depContext.setVersion(depModule.getVersion());
                        copyModule(depContext);
                    }
                }
            } else {
                if (feedback != null) {
                    feedback.notFound(context.getName(), context.getVersion());
                }
            }
        }
    }

    private Collection<ModuleVersionDetails> getModuleVersions(RepositoryManager repoMgr, String name, String version, ModuleQuery.Type type, Integer binaryMajor, Integer binaryMinor) {
        ModuleVersionQuery query = new ModuleVersionQuery(name, version, type);
        if (binaryMajor != null) {
            query.setBinaryMajor(binaryMajor);
        }
        if (binaryMinor != null) {
            query.setBinaryMinor(binaryMinor);
        }
        ModuleVersionResult result = repoMgr.completeVersions(query);
        NavigableMap<String, ModuleVersionDetails> versionMap = result.getVersions();
        return versionMap.values();
    }
    
    private void copyArtifact(ArtifactContext ac, File archive) throws Exception {
        if (feedback != null) {
            feedback.copyArtifact(ac, archive);
        }
        // Make sure we set the correct suffix for the put
        String suffix = ArtifactContext.getSuffixFromFilename(archive.getName());
        ac.setSuffixes(suffix);
        // Store the artifact
        dstRepoman.putArtifact(ac, archive);
        // SHA1 it if required
        if(!ArtifactContext.isDirectoryName(ac.getSingleSuffix())) {
            signArtifact(ac, archive);
        }
    }

    private void signArtifact(ArtifactContext context, File jarFile){
        String sha1 = ShaSigner.sha1(jarFile, log);
        if(sha1 != null){
            File shaFile = ShaSigner.writeSha1(sha1, log);
            if(shaFile != null){
                try{
                    ArtifactContext sha1Context = context.getSha1Context();
                    dstRepoman.putArtifact(sha1Context, shaFile);
                }finally{
                    shaFile.delete();
                }
            }
        }
    }
}
