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

import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.ModuleSpec;

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Load bootstrap modules from zipped ditribution repository.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class DistributionModuleLoader extends ModuleLoader {

    private static final String BOOTSTRAP_DISTRIBUTION_ZIP = "ceylon-runtime-bootstrap.zip";
    private final File distributionFile;

    public DistributionModuleLoader() {
        final String ceylonRepository = AccessController.doPrivileged(new PrivilegedAction<String>() {
            @Override
            public String run() {
                final String defaultCeylonRepository = System.getProperty("user.home") + File.separator + ".ceylon" + File.separator + "repo";
                return System.getProperty("ceylon.repo", defaultCeylonRepository);
            }
        });
        final File temp = new File(ceylonRepository + File.separator + BOOTSTRAP_DISTRIBUTION_ZIP);
        if (temp.exists() == false)
            throw new IllegalArgumentException("No such Ceylon Runtime Bootstrap distribution file: " + temp);
        distributionFile = temp;
    }

    @Override
    protected ModuleSpec findModule(ModuleIdentifier moduleIdentifier) throws ModuleLoadException {
        return null; // TODO
    }

    @Override
    public String toString() {
        return "Ceylon Bootstrap Module Loader: " + distributionFile;
    }
}
