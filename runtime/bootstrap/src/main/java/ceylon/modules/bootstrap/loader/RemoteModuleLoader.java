/*
 * Copyright 2011 Red Hat inc. and third party contributors as noted 
 * by the author tags.
 * 
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

package ceylon.modules.bootstrap.loader;

import org.jboss.modules.LocalModuleLoader;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.ModuleSpec;

import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.Versions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;

/**
 * A remote filesystem-backed module loader.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RemoteModuleLoader extends BootstrapModuleLoader {

    private final static String DEFAULT_CEYLON_VERSION = Versions.CEYLON_VERSION_NUMBER;
    private final static String INDEX = ".index";
    private final static String XML = "module.xml";

    private static final Method findModule;

    static {
        findModule = SecurityActions.findModule();
    }

    private final ModuleLoader delegate;
    private final String rootURL;
    private final String ceylonVersion;
    private final File repoRoot;

    public RemoteModuleLoader() {
        this(new LocalModuleLoader());
    }

    protected RemoteModuleLoader(ModuleLoader delegate) {
        if (delegate == null)
            throw new IllegalArgumentException("Null delegate");

        this.delegate = delegate;
        this.rootURL = SecurityActions.getProperty("modules.remote.root.url", Constants.REPO_URL_CEYLON);
        this.ceylonVersion = SecurityActions.getProperty("ceylon.modules.version", DEFAULT_CEYLON_VERSION);
        this.repoRoot = new File(getCeylonRepository());
    }

    protected ModuleSpec getModuleSpec(final ModuleIdentifier mi) throws ModuleLoadException {
        try {
            return (ModuleSpec) findModule.invoke(delegate, mi);
        } catch (Exception e) {
            throw new ModuleLoadException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ModuleSpec findModule(final ModuleIdentifier moduleIdentifier) throws ModuleLoadException {
        try {
            ModuleSpec spec = getModuleSpec(moduleIdentifier);
            if (spec != null)
                return spec;
        } catch (ModuleLoadException ignored) {
        }

        return fetchRemoteModule(moduleIdentifier) ? getModuleSpec(moduleIdentifier) : null;
    }

    protected boolean fetchRemoteModule(ModuleIdentifier moduleIdentifier) throws ModuleLoadException {
        try {
            String localPath = toPathString(moduleIdentifier, false);
            String remotePath = toPathString(moduleIdentifier, true);

            InputStream moduleXMLStream;
            File localModuleXML = getLocalFile(localPath + XML);
            boolean existsLocally = localModuleXML.exists();
            if (existsLocally)
                moduleXMLStream = new FileInputStream(localModuleXML);
            else
                moduleXMLStream = getInputStream(remotePath, XML);

            if (moduleXMLStream != null) {

                if (existsLocally == false) {
                    File moduleXML = writeStream(moduleXMLStream, localPath + XML);
                    moduleXMLStream = new FileInputStream(moduleXML);
                }

                List<String> resourcePaths = parseResourcePaths(moduleXMLStream, moduleIdentifier);
                for (String resource : resourcePaths) {
                    String fullName = localPath + resource;
                    if (getLocalFile(fullName).exists() == false) {
                        InputStream jarStream = getInputStream(remotePath, resource);
                        if (jarStream != null) {
                            writeStream(jarStream, fullName);
                        }
                    }
                    if (getLocalFile(fullName + INDEX).exists() == false) {
                        InputStream indexStream = getInputStream(remotePath, resource + INDEX);
                        if (indexStream != null) {
                            writeStream(indexStream, fullName + INDEX);
                        }
                    }
                }
                log("Module fetch completed OK: " + moduleIdentifier);
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ModuleLoadException("Cannot fetch remote resource: " + moduleIdentifier, e);
        }
    }

    protected List<String> parseResourcePaths(InputStream moduleXMLStream, ModuleIdentifier moduleIdentifier) throws ModuleLoadException {
        return ModuleXmlParser.parseResourcePaths(moduleXMLStream, moduleIdentifier);
    }

    protected File getLocalFile(String name) {
        return new File(repoRoot, name);
    }

    protected InputStream getInputStream(String path, String resource) {
        try {
            URL url = new URL(rootURL + path + resource);
            log("Fetching resource: " + url);
            return url.openStream();
        } catch (Exception e) {
            log("Cannot open stream: " + e);
            return null;
        }
    }

    protected File writeStream(InputStream inputStream, String name) throws IOException {
        try {
            File file = new File(repoRoot, name);
            File parent = file.getParentFile();
            if (parent.exists() == false && parent.mkdirs() == false)
                throw new IOException("Cannot create parent directories: " + parent);

            log("Saving resource: " + file);
            FileOutputStream out = new FileOutputStream(file);
            try {
                int b;
                while ((b = inputStream.read()) != -1) {
                    out.write(b);
                }
                out.flush();
                log("Resource saved: " + file);
                return file;
            } finally {
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e);
        } finally {
            inputStream.close();
        }
    }

    private String toPathString(ModuleIdentifier moduleIdentifier, boolean addVersion) {
        final StringBuilder builder = new StringBuilder();
        if (addVersion)
            builder.append(ceylonVersion).append(File.separatorChar);
        builder.append(moduleIdentifier.getName().replace('.', File.separatorChar));
        builder.append(File.separatorChar).append(moduleIdentifier.getSlot());
        builder.append(File.separatorChar);
        return builder.toString();
    }

    public String toString() {
        final StringBuilder b = new StringBuilder();
        b.append("Remote ModuleLoader @").append(Integer.toHexString(hashCode())).append(" (url: ").append(rootURL);
        b.append(')');
        return b.toString();
    }

    protected void log(String msg) {
        System.out.println(msg);
    }
}
