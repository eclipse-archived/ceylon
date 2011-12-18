/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.ceylon.test.modules;

import ceylon.modules.spi.Argument;
import ceylon.modules.spi.Constants;
import com.redhat.ceylon.cmr.api.Repository;
import org.jboss.modules.Main;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.junit.Assert;

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Modules test helper.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class ModulesTest {
    private static final String RUNTIME_IMPL = "ceylon.modules.jboss.runtime.JBossRuntime";

    protected File getRepo() throws Throwable {
        return new File(getClass().getResource("/repo").toURI());
    }

    protected File createModuleFile(File tmpdir, Archive module) throws Exception {
        String fullName = module.getName();

        final boolean isDefault = (Constants.DEFAULT + ".car").equals(fullName);

        String name;
        String version;

        if (isDefault) {
            name = Constants.DEFAULT.toString();
            version = Repository.NO_VERSION;
        } else {
            int p = fullName.indexOf("-");
            if (p < 0)
                throw new IllegalArgumentException("No name and version split: " + fullName);

            name = fullName.substring(0, p);
            version = fullName.substring(p + 1, fullName.lastIndexOf("."));
        }

        File targetDir = new File(tmpdir, toPathString(name, version));
        if (targetDir.exists() == false)
            Assert.assertTrue(targetDir.mkdirs());
        File targetFile = new File(targetDir, fullName);

        ZipExporter exporter = module.as(ZipExporter.class);
        exporter.exportTo(targetFile, true);

        return targetDir;
    }

    protected void testArchive(Archive module, Archive... libs) throws Throwable {
        testArchive(module, null, libs);
    }

    protected void testArchive(Archive module, String run, Archive... libs) throws Throwable {
        File tmpdir = AccessController.doPrivileged(new PrivilegedAction<File>() {
            public File run() {
                return new File(System.getProperty("ceylon.repo", System.getProperty("java.io.tmpdir")));
            }
        });

        List<File> files = new ArrayList<File>();
        try {
            files.add(createModuleFile(tmpdir, module));
            for (Archive lib : libs)
                files.add(createModuleFile(tmpdir, lib));

            String name;
            String version;

            String fullName = module.getName();
            final boolean isDefault = (Constants.DEFAULT + ".car").equals(fullName);
            if (isDefault) {
                name = Constants.DEFAULT.toString();
                version = Repository.NO_VERSION;
            } else {
                int p = fullName.indexOf("-");
                if (p < 0)
                    throw new IllegalArgumentException("No name and version split: " + fullName);

                name = fullName.substring(0, p);
                version = fullName.substring(p + 1, fullName.lastIndexOf("."));
            }

            Map<String, String> args = new LinkedHashMap<String, String>();
            args.put(Constants.IMPL_ARGUMENT_PREFIX + Argument.EXECUTABLE.toString(), RUNTIME_IMPL);
            args.put(Constants.CEYLON_ARGUMENT_PREFIX + Argument.REPOSITORY.toString(), tmpdir.toString());
            if (run != null)
                args.put(Constants.CEYLON_ARGUMENT_PREFIX + Argument.RUN.toString(), run);

            execute(name + "/" + version, args);
        } finally {
            for (File file : files)
                delete(file);
        }
    }

    protected static void delete(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles())
                delete(f);
        }
        //noinspection ResultOfMethodCallIgnored
        file.delete();
    }

    protected void src(String module, String src) throws Throwable {
        src(module, src, Collections.<String, String>emptyMap());
    }

    protected void car(String module, Map<String, String> extra) throws Throwable {
        Map<String, String> args = new LinkedHashMap<String, String>();
        args.put(Constants.IMPL_ARGUMENT_PREFIX + Argument.EXECUTABLE.toString(), RUNTIME_IMPL);
        args.put(Constants.CEYLON_ARGUMENT_PREFIX + Argument.REPOSITORY.toString(), getRepo().getPath());
        args.putAll(extra);

        execute(module, args);
    }

    protected void src(String module, String src, Map<String, String> extra) throws Throwable {
        Map<String, String> args = new LinkedHashMap<String, String>();
        args.put(Constants.IMPL_ARGUMENT_PREFIX + Argument.EXECUTABLE.toString(), RUNTIME_IMPL);
        args.put(Constants.CEYLON_ARGUMENT_PREFIX + Argument.SOURCE.toString(), src);
        args.putAll(extra);

        execute(module, args);
    }

    protected String getBootstrapModules() {
        final String projectHome = System.getProperty("ceylon.runtime.home", System.getProperty("user.dir"));
        return projectHome + File.separator + "dist" + File.separator + "runtime-repo";
    }

    protected void execute(String module, Map<String, String> map) throws Throwable {

        List<String> args = new ArrayList<String>();
        // JBoss Modules args
        args.add(Constants.MODULE_PATH.toString());
        args.add(getBootstrapModules());
        args.add(Constants.CEYLON_RUNTIME_MODULE.toString());

        for (Map.Entry<String, String> entry : map.entrySet()) {
            args.add(entry.getKey());
            args.add(entry.getValue());
        }
        args.add(module);
        Main.main(args.toArray(new String[args.size()]));
    }

    protected static String toPathString(String name, String version) {
        final StringBuilder builder = new StringBuilder();
        builder.append(name.replace('.', File.separatorChar));
        if (Repository.NO_VERSION.equals(version) == false)
            builder.append(File.separatorChar).append(version);
        builder.append(File.separatorChar);
        return builder.toString();
    }
}

