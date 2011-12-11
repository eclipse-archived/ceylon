/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.ModuleSpec;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Load bootstrap modules from zipped ditribution repository.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class DistributionModuleLoader extends ModuleLoader {

    private static final String BOOTSTRAP_DISTRIBUTION = "ceylon-runtime-bootstrap";
    private final ModuleLoader delegate;

    public DistributionModuleLoader() {
        final String ceylonRepository = AccessController.doPrivileged(new PrivilegedAction<String>() {
            @Override
            public String run() {
                final String defaultCeylonRepository = System.getProperty("user.home") + File.separator + ".ceylon" + File.separator + "repo";
                return System.getProperty("ceylon.repo", defaultCeylonRepository);
            }
        });
        final File dir = new File(ceylonRepository, BOOTSTRAP_DISTRIBUTION);
        final File zip = new File(dir, BOOTSTRAP_DISTRIBUTION + ".zip");
        if (zip.exists() == false)
            throw new IllegalArgumentException("No such Ceylon Runtime Bootstrap distribution file: " + zip);

        final File unzipped = unzipDistribution(dir, zip);
        delegate = new LocalModuleLoader(new File[]{unzipped});
    }

    /**
     * Unzip bootstrap distrubution if not already present.
     *
     * @param dir the unzip destination
     * @param zip the zipped bootstrap distribution
     * @return unziped root directory
     */
    protected File unzipDistribution(File dir, File zip) {
        File exploded = new File(dir, BOOTSTRAP_DISTRIBUTION + "-exploded");
        if (exploded.exists() == false) {
            try {
                final ZipFile zipFile = new ZipFile(zip);
                try {
                    final Enumeration<? extends ZipEntry> entries = zipFile.entries();
                    while (entries.hasMoreElements()) {
                        final ZipEntry ze = entries.nextElement();
                        final File file = new File(exploded, ze.getName());
                        if (ze.isDirectory()) {
                            if (file.mkdirs() == false)
                                throw new IllegalArgumentException("Cannot create dir: " + file);
                        } else {
                            FileOutputStream fos = new FileOutputStream(file);
                            copyStream(zipFile.getInputStream(ze), fos);
                        }
                    }
                } finally {
                    zipFile.close();
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return exploded;
    }

    protected static void copyStream(final InputStream in, final OutputStream out) throws IOException {
        final byte[] bytes = new byte[8192];
        int cnt;
        try {
            while ((cnt = in.read(bytes)) != -1) {
                out.write(bytes, 0, cnt);
            }
        } finally {
            safeClose(in);
            safeClose(out);
        }
    }

    protected static void safeClose(Closeable c) {
        try {
            c.close();
        } catch (Exception ignored) {
        }
    }

    @Override
    protected Module preloadModule(ModuleIdentifier identifier) throws ModuleLoadException {
        return delegate.loadModule(identifier);
    }

    @Override
    protected ModuleSpec findModule(ModuleIdentifier moduleIdentifier) throws ModuleLoadException {
        throw new ModuleLoadException("Should not be here, by-passing delegate loader?");
    }

    @Override
    public String toString() {
        return "Ceylon Bootstrap Module Loader: " + delegate;
    }
}
