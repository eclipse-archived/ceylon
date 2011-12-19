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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Main {
    private static final String CEYLOND_VERSION = "0.1 'Newton'";

    public static void main(String[] args) throws IOException {
        String destDir = null;
        List<String> sourceDirs = new LinkedList<String>();
        boolean showPrivate = false;
        boolean omitSource = false;
        List<String> modules = new LinkedList<String>();
        List<String> repositories = new LinkedList<String>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("-h".equals(arg)
                    || "-help".equals(arg)
                    || "--help".equals(arg)) {
                printUsage();
            } else if ("-v".equals(arg)
                        || "-version".equals(arg)
                        || "--version".equals(arg)) {
                printVersion();
            } else if ("-d".equals(arg)) {
                System.err.println("-d: option not yet supported (though perhaps you meant -out?)");
                System.exit(1);
            } else if ("-out".equals(arg)) {
                destDir = args[++i];
            } else if ("-src".equals(arg)) {
                sourceDirs.addAll(readPath(args[++i]));
            } else if ("-rep".equals(arg)) {
                repositories.add(args[++i]);
            } else if ("-private".equals(arg)) {
                showPrivate = true;
            } else if ("-omit-source".equals(arg)) {
                omitSource = true;
            } else {
                modules.add(arg);
            }
            
        }
        
        if(modules.isEmpty()){
            printUsage();
        }
        if (destDir == null) {
            destDir = "modules";
        }
        repositories = com.redhat.ceylon.compiler.util.Util.addDefaultRepositories(repositories);

        List<File> sourceFolders = new LinkedList<File>();
        if (sourceDirs.isEmpty()) {
            File src = new File("source");
            if(src.isDirectory())
                sourceFolders.add(src);
        }else{
            for(String srcDir : sourceDirs){
                File src = new File(srcDir);
                if (!src.isDirectory()) {
                    System.err.println("No such source directory: " + srcDir);
                    System.exit(1);
                }
                sourceFolders.add(src);
            }
        }

        try{
            CeylonDocTool ceylonDocTool = new CeylonDocTool(sourceFolders, repositories, modules);
            ceylonDocTool.setShowPrivate(showPrivate);
            ceylonDocTool.setDestDir(destDir);
            ceylonDocTool.setOmitSource(omitSource);
            ceylonDocTool.makeDoc();
        }catch(Exception x){
            System.err.println("Error: "+x.getMessage());
            x.printStackTrace();
            System.exit(1);
        }
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
        System.out.println("Version: ceylond "+CEYLOND_VERSION);
        System.exit(0);
    }

    private static void printUsage() {
        List<String> defaultRepositories = com.redhat.ceylon.compiler.util.Util.addDefaultRepositories(Collections.<String>emptyList());
        System.err.print(
                "Usage: ceylon [options...] moduleName[/version]... :\n"
                +"\n"
                +"-out <path>:  Output module repository (default: 'modules')\n"
                +"-src <path>:  Source path (default: 'source')\n"
                +"              You can set this option multiple times\n"
                +"-rep <path>:  Module repository\n"
                +"              You can set this option multiple times\n"
                +"              Default:\n"
        );
        for(String repo : defaultRepositories)
            System.err.println("              "+repo);
        System.err.print(
                 "-private:     Document non-shared declarations\n"
                +"-omit-source: Do not include the source code\n"
                +"-d:           Not supported yet\n"
                +"-help:        Prints help usage\n"
                +"-version:     Prints version number\n"
        );
        System.exit(1);
    }
}
