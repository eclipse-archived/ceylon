/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.ceylondoc;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.config.DefaultToolOptions;

public class Main {
    private static final int SC_OK = 0;
    private static final int SC_ARGS = 1;
    private static final int SC_ERROR = 2;

    public static void main(String[] args) throws IOException {
        String destDir = null;
        List<String> sourceDirs = new LinkedList<String>();
        boolean includeNonShared = false;
        boolean includeSourceCode = false;
        List<String> modules = new LinkedList<String>();
        List<String> repositories = new LinkedList<String>();
        File cwd = new File(".");
        String systemRepo = null;
        String cacheRepo = null;
        boolean noDefRepos = false;
        String user = null, pass = null;
        
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            int argsLeft = args.length - 1 - i;
            if ("-h".equals(arg)
                    || "-help".equals(arg)
                    || "--help".equals(arg)) {
                printUsage(SC_OK, cwd, systemRepo, cacheRepo, noDefRepos, repositories, destDir);
            } else if ("-v".equals(arg)
                        || "-version".equals(arg)
                        || "--version".equals(arg)) {
                printVersion();
            } else if ("-d".equals(arg)) {
                System.err.println(CeylondMessages.msg("error.optionDnotSupported"));
                exit(SC_ARGS);
            } else if ("-cwd".equals(arg)) {
                if (argsLeft <= 0) {
                    optionMissingArgument(arg);
                }
                cwd = new File(args[++i]);
            } else if ("-out".equals(arg)) {
                if (argsLeft <= 0) {
                    optionMissingArgument(arg);
                }
                destDir = args[++i];
            } else if ("-src".equals(arg)) {
                if (argsLeft <= 0) {
                    optionMissingArgument(arg);
                }
                sourceDirs.addAll(readPath(args[++i]));
            } else if ("-rep".equals(arg)) {
                if (argsLeft <= 0) {
                    optionMissingArgument(arg);
                }
                repositories.add(args[++i]);
            } else if ("-sysrep".equals(arg)) {
                if (argsLeft <= 0) {
                    optionMissingArgument(arg);
                }
                systemRepo = args[++i];
            } else if ("-cacherep".equals(arg)) {
                if (argsLeft <= 0) {
                    optionMissingArgument(arg);
                }
                systemRepo = args[++i];
            } else if ("-nodefreps".equals(arg)) {
                noDefRepos = true;
            } else if ("-non-shared".equals(arg)) {
                includeNonShared = true;
            } else if ("-source-code".equals(arg)) {
                includeSourceCode = true;
            } else if ("-user".equals(arg)) {
                if (argsLeft <= 0) {
                    optionMissingArgument(arg);
                }
                user = args[++i];
            } else if ("-pass".equals(arg)) {
                if (argsLeft <= 0) {
                    optionMissingArgument(arg);
                }
                pass = args[++i];
            } else if (arg.startsWith("-")) {
                System.err.println(CeylondMessages.msg("error.optionUnknown", arg));
                exit(SC_ARGS);
            } else {
                modules.add(arg);
            }
            
        }
        
        if(modules.isEmpty()){
            System.err.println(CeylondMessages.msg("error.noModulesSpecified"));
            printUsage(SC_ARGS, cwd, systemRepo, cacheRepo, noDefRepos, repositories, destDir);
        }
        if (destDir == null) {
            destDir = DefaultToolOptions.getCompilerOutDir().getPath();
        }

        List<File> sourceFolders = new LinkedList<File>();
        if (sourceDirs.isEmpty()) {
            List<File> srcs = DefaultToolOptions.getCompilerSourceDirs();
            for (File src : srcs) {
                if(src.isDirectory())
                    sourceFolders.add(src);
            }
        }else{
            for(String srcDir : sourceDirs){
                File src = new File(srcDir);
                if (!src.isDirectory()) {
                    System.err.println(CeylondMessages.msg("error.noSuchSourceDirectory", srcDir));
                    exit(SC_ARGS);
                }
                sourceFolders.add(src);
            }
        }

        try{
            CeylonDocTool ceylonDocTool = new CeylonDocTool();
            ceylonDocTool.setSourceFolders(sourceFolders);
            ceylonDocTool.setRepositoryAsStrings(repositories);
            ceylonDocTool.setModuleSpecs(modules);
            ceylonDocTool.setHaltOnError(false);
            ceylonDocTool.setOutputRepository(destDir, user, pass);
            ceylonDocTool.setIncludeNonShared(includeNonShared);
            ceylonDocTool.setIncludeSourceCode(includeSourceCode);
            ceylonDocTool.init();
            ceylonDocTool.makeDoc();
        }catch(CeylondException x){
            System.err.println(CeylondMessages.msg("error", x.getLocalizedMessage()));
            // no need to print the stack trace
            exit(SC_ERROR);
        }catch(Exception x){
            System.err.println(CeylondMessages.msg("error", x.getLocalizedMessage()));
            x.printStackTrace();
            exit(SC_ERROR);
        }
    }

    private static void exit(int statusCode) {
        System.exit(statusCode);
    }
    
    private static void optionMissingArgument(String arg) {
        System.err.println(CeylondMessages.msg("error.optionMissing", arg));
        exit(SC_ARGS);
    }

    private static List<String> readPath(String path) {
        List<String> ret = new LinkedList<String>();
        int start = 0;
        int sep;
        while((sep = path.indexOf(File.pathSeparatorChar, start)) != -1){
            String part = path.substring(start, sep);
            if(!part.isEmpty())
                ret.add(part);
            start = sep + 1;
        }
        // rest
        String part = path.substring(start);
        if(!part.isEmpty())
            ret.add(part);
        return ret;
    }

    private static void printVersion() {
        System.out.println(CeylondMessages.msg("info.version", Versions.CEYLON_VERSION));
        exit(SC_OK);
    }

    private static void printUsage(int statusCode, File cwd, String systemRepo, String cacheRepo, boolean noDefRepos, List<String> userRepos, String outputRepo) {
        List<String> defaultRepositories = addDefaultRepositories(cwd, systemRepo, cacheRepo, noDefRepos, userRepos, null);
        System.err.print(CeylondMessages.msg("info.usage1"));
        for(String repo : defaultRepositories) {
            System.err.println("                        "+repo);
        }
        System.err.print(CeylondMessages.msg("info.usage2"));
        exit(statusCode);
    }
    
    private static List<String> addDefaultRepositories(File cwd, String systemRepo, String cacheRepo, boolean noDefRepos, List<String> userRepos, String outputRepo){
        RepositoryManagerBuilder builder = CeylonUtils.repoManager()
                .cwd(cwd)
                .systemRepo(systemRepo)
                .cacheRepo(cacheRepo)
                .noDefaultRepos(noDefRepos)
                .userRepos(userRepos)
                .outRepo(outputRepo)
                .buildManagerBuilder();
        return builder.getRepositoriesDisplayString();
    }
}