/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package ceylon.modules.bootstrap.loader;

import org.jboss.modules.LocalModuleLoader;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.ModuleSpec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

/**
 * A remote filesystem-backed module loader.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class RemoteModuleLoader extends BootstrapModuleLoader {

    private final static String DEFAULT_CEYLON_VERSION = "0.1";
    private final static String INDEX = ".index";
    private final static String XML = "module.xml";

    private static final Method findModule;

    static {
        findModule = AccessController.doPrivileged(new PrivilegedAction<Method>() {
            public Method run() {
                try {
                    final Method declaredMethod = ModuleLoader.class.getDeclaredMethod("findModule", ModuleIdentifier.class);
                    declaredMethod.setAccessible(true);
                    return declaredMethod;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
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
        this.rootURL = System.getProperty("modules.remote.root.url", "http://modules.ceylon.org/");
        this.ceylonVersion = System.getProperty("ceylon.modules.version", DEFAULT_CEYLON_VERSION);
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