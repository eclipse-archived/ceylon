package com.redhat.ceylon.cmr.ceylon;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.NavigableMap;
import java.util.NavigableSet;
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

/**
 * Class that can be used to copy modules from one repository to another.
 * Specific artifact types can be selected for copying while others 
 * will be skipped. It's also possible to recursively copy all dependencies.
 * 
 * NB: Feed back gives information about the total number of modules to copy
 * and the current number (to be) copied. This information is not entirely
 * correct because the total gets updated when new dependencies get found.
 * This is done because retrieving dependencies is actually one of the
 * slowest parts of the entire copy process. If we'd do it beforehand there
 * not be much sense in having a progress report at all.
 * @author Tako Schotanus
 */
public class ModuleCopycat {
    private RepositoryManager srcRepoman;
    private RepositoryManager dstRepoman;
    private CopycatFeedback feedback;
    private Logger log;
    
    private Set<String> copiedModules;
    private int count;
    private int maxCount;

    public static interface CopycatFeedback {
        void beforeCopyModule(ArtifactContext ac, int count, int max) throws Exception;
        void afterCopyModule(ArtifactContext ac, int count, int max) throws Exception;
        void beforeCopyArtifact(ArtifactContext ac, File archive, int count, int max) throws Exception;
        void afterCopyArtifact(ArtifactContext ac, File archive, int count, int max) throws Exception;
        void notFound(ArtifactContext ac) throws Exception;
    }
    
    /**
     * Set up the object with the given source and destination repositories
     * @param srcRepoman The source repository to copy from
     * @param dstRepoman The destination repository to copy to
     */
    public ModuleCopycat(RepositoryManager srcRepoman, RepositoryManager dstRepoman) {
        this(srcRepoman, dstRepoman, null);
    }
    
    /**
     * Set up the object with the given source and destination repositories and callback
     * interface to receive feedback on progress and errors
     * @param srcRepoman The source repository to copy from
     * @param dstRepoman The destination repository to copy to
     * @param feedback Instance of ModuleCopycat.CopycatFeedback for receiving feedback
     */
    public ModuleCopycat(RepositoryManager srcRepoman, RepositoryManager dstRepoman, CopycatFeedback feedback) {
        this(srcRepoman, dstRepoman, new CMRJULLogger(), feedback);
    }
    
    /**
     * Set up the object with the given source and destination repositories and callback
     * interface to receive feedback on progress and errors
     * @param srcRepoman The source repository to copy from
     * @param dstRepoman The destination repository to copy to
     * @param log The logger to use
     * @param feedback Instance of ModuleCopycat.CopycatFeedback for receiving feedback
     */
    public ModuleCopycat(RepositoryManager srcRepoman, RepositoryManager dstRepoman, Logger log, CopycatFeedback feedback) {
        assert(srcRepoman != null);
        assert(dstRepoman != null);
        assert(log != null);
        this.srcRepoman = srcRepoman;
        this.dstRepoman = dstRepoman;
        this.feedback = feedback;
        this.log = log;
        this.copiedModules = new HashSet<String>();
    }
    
    /**
     * Method to copy the given module from the source repository to the destination repository.
     * The context's "suffixes" will be used to determine which artifact types will be copied.
     * If the `isFetchSingleArtifact` is not set all the module's dependencies will also be copied.
     * Repeated call to this method with the same module/version will only result in a single copy!  
     * @param context The ArtifactContext containing the information on the module to copy
     * @throws Exception Can throw RepositoryException or any exception that was thrown by code
     * in the "feedback" callback interface
     */
    public void copyModule(ArtifactContext context) throws Exception {
        count = 0;
        maxCount = 1;
        copyModuleInternal(context);
    }
    
    private void copyModuleInternal(ArtifactContext context) throws Exception {
        assert(context != null);
        if (!JDKUtils.isJDKModule(context.getName()) && !JDKUtils.isOracleJDKModule(context.getName())) {
            String module = ModuleUtil.makeModuleName(context.getName(), context.getVersion());
            if (!copiedModules.add(module)) {
                // Faking a copy here for feedback because it was already done and we never copy twice
                if (feedback != null) {
                    feedback.beforeCopyModule(context, count++, maxCount);
                }
                if (feedback != null) {
                    feedback.afterCopyModule(context, count, maxCount);
                }
                return;
            }
            Collection<ModuleVersionDetails> versions = getModuleVersions(srcRepoman, context.getName(), context.getVersion(), ModuleQuery.Type.ALL, null, null);
            if (!versions.isEmpty()) {
                ModuleVersionDetails ver = versions.iterator().next();
                if (feedback != null) {
                    feedback.beforeCopyModule(context, count++, maxCount);
                }
                ArtifactResult results[] = srcRepoman.getArtifactResults(context);
                int artCnt = 0;
                for (ArtifactResult r : results) {
                    if (feedback != null) {
                        feedback.beforeCopyArtifact(context, r.artifact(), artCnt++, results.length);
                    }
                    copyArtifact(context, r.artifact());
                    if (feedback != null) {
                        feedback.afterCopyArtifact(context, r.artifact(), artCnt, results.length);
                    }
                }
                if (feedback != null) {
                    feedback.afterCopyModule(context, count, maxCount);
                }
                if (!context.isFetchSingleArtifact()) {
                    maxCount += countNonJdkDeps(ver.getDependencies());
                    for (ModuleInfo dep : ver.getDependencies()) {
                        ModuleSpec depModule = new ModuleSpec(dep.getName(), dep.getVersion());
                        ArtifactContext depContext = context.copy();
                        depContext.setName(depModule.getName());
                        depContext.setVersion(depModule.getVersion());
                        copyModuleInternal(depContext);
                    }
                }
            } else {
                if (feedback != null) {
                    feedback.notFound(context);
                }
            }
        }
    }

    private int countNonJdkDeps(NavigableSet<ModuleInfo> dependencies) {
        int cnt = 0;
        for (ModuleInfo dep : dependencies) {
            if (!JDKUtils.isJDKModule(dep.getName()) && !JDKUtils.isOracleJDKModule(dep.getName())) {
                cnt++;
            }
        }
        return cnt;
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
    
    private void copyArtifact(ArtifactContext orgac, File archive) throws Exception {
        ArtifactContext ac = orgac.copy();
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
