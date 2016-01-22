package com.redhat.ceylon.tools.browse;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.api.ModuleVersionDetails;
import com.redhat.ceylon.cmr.ceylon.RepoUsingTool;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tools.CeylonTool;
import com.redhat.ceylon.common.tools.ModuleSpec;

@Summary("Open module documentation in the browser")
public class CeylonBrowseTool extends RepoUsingTool {

    private List<ModuleSpec> modules;

    public CeylonBrowseTool() {
        super(CeylonBrowseMessages.RESOURCE_BUNDLE);
    }

    @Argument(argumentName = "module", multiplicity = "+")
    public void setModules(List<String> modules) {
        this.modules = ModuleSpec.parseEachList(modules);
    }

    @Override
    public void initialize(CeylonTool tool) {
    }

    @Override
    public void run() throws Exception {
        for (ModuleSpec module : modules) {
            List<ModuleVersionDetails> versions = new ArrayList<ModuleVersionDetails>(
            		getModuleVersions(getRepositoryManager(), module.getName(), module.getVersion(), 
            				ModuleQuery.Type.ALL, null, null, null, null));
            Collections.sort(versions);

            if (versions.isEmpty()) {
                String err = getModuleNotFoundErrorMessage(getRepositoryManager(), module.getName(), module.getVersion());
                errorAppend(err);
                errorNewline();
                continue;
            }

            ModuleVersionDetails mvd = versions.get(versions.size() - 1);
            browseDoc(mvd);
        }
    }

    private void browseDoc(ModuleVersionDetails mvd) throws IOException {
        if (mvd.getOrigin() == null) {
            errorMsg("error.noOrigin", getModuleAndVersion(mvd));
            return;
        }

        URI uri;
        if (mvd.isRemote()) {
            uri = parseRemoteUri(mvd);
        } else {
            uri = parseLocalUri(mvd);
        }

        if (uri != null) {
            try {
                Desktop.getDesktop().browse(uri);
                msg("info.browseDoc", getModuleAndVersion(mvd), uri);
                newline();
            } catch (Exception e) {
                errorMsg("error.unableToOpenBrowser", getModuleAndVersion(mvd), uri);
            }
        }
    }

    private URI parseLocalUri(ModuleVersionDetails mvd) throws IOException {
        Path path = Paths.get(mvd.getOrigin(), mvd.getModule().replace('.', File.separatorChar), mvd.getVersion(), "module-doc", "api", "index.html");
        if (!Files.isRegularFile(path)) {
            errorMsg("error.noIndex", path);
            return null;
        }
        return path.toUri();
    }

    private URI parseRemoteUri(ModuleVersionDetails mvd) throws IOException {
        String origin = mvd.getOrigin();
        if (origin.startsWith("The Herd (") && origin.endsWith(")")) {
            origin = origin.substring(10, origin.length() - 1);
        }
        if (!origin.endsWith("/")) {
            origin = origin + "/";
        }
        try {
            URL url = new URL(new URL(origin), mvd.getModule().replace('.', '/') + "/" + mvd.getVersion() + "/module-doc/api/index.html");
            return url.toURI();
        } catch (Exception e) {
            errorMsg("error.unableToParseUri", getModuleAndVersion(mvd), e.getMessage());
            return null;
        }
    }

    private String getModuleAndVersion(ModuleVersionDetails mvd) {
        return mvd.getModule() + "/" + mvd.getVersion();
    }
    
}