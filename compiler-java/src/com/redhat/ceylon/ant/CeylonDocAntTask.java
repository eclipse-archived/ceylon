/* Originally based on the javac task from apache-ant-1.7.1.
 * The license in that file is as follows:
 *
 *   Licensed to the Apache Software Foundation (ASF) under one or
 *   more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information regarding
 *   copyright ownership.  The ASF licenses this file to You under
 *   the Apache License, Version 2.0 (the "License"); you may not
 *   use this file except in compliance with the License.  You may
 *   obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an "AS
 *   IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *   express or implied.  See the License for the specific language
 *   governing permissions and limitations under the License.
 *
 */

/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 */
package com.redhat.ceylon.ant;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;

import com.redhat.ceylon.common.Constants;

public class CeylonDocAntTask extends LazyCeylonAntTask {
    private static final FileFilter ARTIFACT_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return true;
        }
    };

    private ModuleSet moduleset = new ModuleSet();
    private LinkSet linkset = new LinkSet();
    private Path doc;
    private boolean includeNonShared;
    private boolean includeSourceCode;
    private boolean ignoreMissingDoc;
    private boolean ignoreBrokenLink;
    
    public CeylonDocAntTask() {
        super("doc");
    }
    
    /**
     * Set the doc directories to find the doc files.
     * @param res the doc directories as a path
     */
    public void setDoc(Path res) {
        if (this.doc == null) {
            this.doc = res;
        } else {
            this.doc.append(res);
        }
    }

    public List<File> getDoc() {
        if (this.doc == null) {
            return Collections.singletonList(getProject().resolveFile(Constants.DEFAULT_DOC_DIR));
        }
        String[] paths = this.doc.list();
        ArrayList<File> result = new ArrayList<File>(paths.length);
        for (String path : paths) {
            result.add(getProject().resolveFile(path));
        }
        return result;
    }

    /**
     * Include even non-shared declarations
     */
    public void setIncludeNonShared(boolean includeNonShared) {
        this.includeNonShared = includeNonShared;
    }

    /**
     * Include source code in the documentation
     */
    public void setIncludeSourceCode(boolean includeSourceCode) {
        this.includeSourceCode = includeSourceCode;
    }

    /**
     * Do not print warnings about missing documentation.
     */
    public void setIgnoreMissingDoc(boolean ignoreMissingDoc) {
        this.ignoreMissingDoc = ignoreMissingDoc;
    }
    
    /**
     * Do not print warnings about broken links.
     */
    public void setIgnoreBrokenLink(boolean ignoreBrokenLink) {
        this.ignoreBrokenLink = ignoreBrokenLink;
    }
    
    /**
     * Adds a module to compile
     * @param module the module name to compile
     */
    public void addConfiguredModule(Module module){
        this.moduleset.addConfiguredModule(module);
    }
    
    public void addConfiguredModuleset(ModuleSet moduleset){
        this.moduleset.addConfiguredModuleSet(moduleset);
    }
    
    public void addConfiguredSourceModules(SourceModules modules){
        this.moduleset.addConfiguredSourceModules(modules);
    }

    /**
     * Adds a link for a {@code <link>} nested element
     * @param link the new link
     */
    public void addConfiguredLink(Link link) {
        linkset.addConfiguredLink(link);
    }
    /**
     * Adds a set of links for a {@code <linkset>} nested element
     * @param linkset the new link set
     */
    public void addConfiguredLinkset(LinkSet reposet) {
        this.linkset.addConfiguredLinkSet(reposet);
    }
    
    protected Set<Link> getLinkset() {
        return linkset.getLinks();
    }

    /**
     * Perform the compilation.
     */
    @Override
    protected Commandline buildCommandline() {
        LazyHelper lazyTask = new LazyHelper(this) {
            @Override
            protected File getArtifactDir(Module module) {
                File outModuleDir = new File(getOut(), module.toVersionedDir().getPath() + "/module-doc");
                return outModuleDir;
            }
            
            @Override
            protected FileFilter getArtifactFilter() {
                return ARTIFACT_FILTER;
            }

            @Override
            protected long getOldestArtifactTime(File file) {
                return file.lastModified();
            }
            
            @Override
            protected long getArtifactFileTime(Module module, File file) {
                return Long.MAX_VALUE;
            }
        };
        if (lazyTask.filterModules(moduleset.getModules())) {
            log("Everything's up to date");
            return null;
        }
        
        return super.buildCommandline();
    }
    
    protected void completeCommandline(Commandline cmd) {
        super.completeCommandline(cmd);
        
        if(includeSourceCode)
            appendOption(cmd, "--source-code");
        if(includeNonShared)
            appendOption(cmd, "--non-shared");
        if(ignoreMissingDoc)
            appendOption(cmd, "--ignore-missing-doc");
        if(ignoreBrokenLink)
            appendOption(cmd, "--ignore-broken-link");
        for (File doc : getDoc()) {
            appendOptionArgument(cmd, "--doc", doc.getAbsolutePath());
        }
        // links
        for (Link link : linkset.getLinks()) {
            log("Adding link: "+link, Project.MSG_VERBOSE);
            appendOptionArgument(cmd, "--link", link.toString());
        }
        // modules to document
        for (Module module : moduleset.getModules()) {
            log("Adding module: "+module, Project.MSG_VERBOSE);
            cmd.createArgument().setValue(module.toSpec());
        }

    }

    @Override
    protected String getFailMessage() {
        return "Documentation failed; see the ceylond error output for details.";
    }

}
