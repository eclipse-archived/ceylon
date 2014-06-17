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

package org.jboss.ceylon.test.modules;

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ceylon.modules.spi.Argument;
import ceylon.modules.spi.Constants;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.common.Versions;
import org.jboss.modules.Main;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.junit.Assert;

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

    protected File getAlternative() throws Throwable {
        return new File(getClass().getResource("/alternative").toURI());
    }

    protected File createModuleFile(File tmpdir, Archive module) throws Exception {
        String fullName = module.getName();

        final boolean isDefault = (Constants.DEFAULT + ".car").equals(fullName);

        String name;
        String version;

        if (isDefault) {
            name = Constants.DEFAULT.toString();
            version = null;
        } else {
            int p = fullName.indexOf("-");
            if (p < 0)
                throw new IllegalArgumentException("No name and version split: " + fullName);

            name = fullName.substring(0, p);
            version = fullName.substring(p + 1, fullName.lastIndexOf("."));
        }

        File targetDir = new File(tmpdir, toPathString(name, version));
        if (targetDir.exists() == false) {
            Assert.assertTrue(targetDir.mkdirs());
        }
        File targetFile = new File(targetDir, fullName);
        if (targetFile.exists()) {
            Assert.assertTrue(targetFile.delete());
        }

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
                return new File(System.getProperty("ceylon.user.repo", System.getProperty("java.io.tmpdir")));
            }
        });

        List<File> files = new ArrayList<File>();
        try {
            files.add(createModuleFile(tmpdir, module));
            for (Archive lib : libs) {
                files.add(createModuleFile(tmpdir, lib));
            }

            String name;
            String version;

            String fullName = module.getName();
            final boolean isDefault = (Constants.DEFAULT + ".car").equals(fullName);
            if (isDefault) {
                name = Constants.DEFAULT.toString();
                version = null;
            } else {
                int p = fullName.indexOf("-");
                if (p < 0)
                    throw new IllegalArgumentException("No name and version split: " + fullName);

                name = fullName.substring(0, p);
                version = fullName.substring(p + 1, fullName.lastIndexOf("."));
            }

            Map<String, String> args = new LinkedHashMap<String, String>();
            args.put(Constants.CEYLON_ARGUMENT_PREFIX + Argument.SYSTEM_REPOSITORY.toString(), getDistRepo());
            args.put(Constants.CEYLON_ARGUMENT_PREFIX + Argument.REPOSITORY.toString(), tmpdir.toString());
            if (run != null)
                args.put(Constants.CEYLON_ARGUMENT_PREFIX + Argument.RUN.toString(), run);

            execute(version != null ? name + "/" + version : name, args);
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
        args.put(Constants.CEYLON_ARGUMENT_PREFIX + Argument.REPOSITORY.toString(), getRepo().getPath());
        args.putAll(extra);

        execute(module, args);
    }

    protected void src(String module, String src, Map<String, String> extra) throws Throwable {
        Map<String, String> args = new LinkedHashMap<String, String>();
        args.put(Constants.CEYLON_ARGUMENT_PREFIX + Argument.SOURCE.toString(), src);
        args.putAll(extra);

        execute(module, args);
    }

    protected String getDistRepo() {
        final String projectHome = System.getProperty("ceylon.runtime.home", System.getProperty("user.dir"));
        String distRepo = projectHome + File.separator + ".." + File.separator + "ceylon-dist" + File.separator + "dist" + File.separator + "repo";
        return distRepo;
    }

    protected String getBootstrapModules() {
        final String projectHome = System.getProperty("ceylon.runtime.home", System.getProperty("user.dir"));
        String runtimeRepo = projectHome + File.separator + "build" + File.separator + "dist" + File.separator + "repo";
        return getDistRepo() + File.pathSeparator + runtimeRepo;
    }

    protected static String toPathString(String name, String version) {
        final StringBuilder builder = new StringBuilder();
        builder.append(name.replace('.', File.separatorChar));
        if (RepositoryManager.DEFAULT_MODULE.equals(name) == false)
            builder.append(File.separatorChar).append(version);
        builder.append(File.separatorChar);
        return builder.toString();
    }

    protected void execute(String module, Map<String, String> map) throws Throwable {
        List<String> args = new ArrayList<String>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            args.add(entry.getKey());
            args.add(entry.getValue());
        }
        execute(module, args);
    }

    protected void execute(String module, List<String> extra) throws Throwable {
        List<String> args = new ArrayList<String>();
        // JBoss Modules args
        args.add(Constants.MODULE_PATH.toString());
        args.add(getBootstrapModules());
        args.add(Constants.CEYLON_RUNTIME_MODULE + ":" + Versions.CEYLON_VERSION_NUMBER);
        // default Ceylon runtime args
        args.add(Constants.IMPL_ARGUMENT_PREFIX + Argument.EXECUTABLE.toString());
        args.add(RUNTIME_IMPL);
        // extra args
        args.addAll(extra);
        // module_ args
        args.add(module);
        // run JBoss Modules
        System.err.print("Command line: ");
        for (String arg : args) {
            System.err.print(arg + " ");
        }
        System.err.println("");
        Main.main(args.toArray(new String[args.size()]));
    }
}

