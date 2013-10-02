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

package com.redhat.ceylon.importjar;

import java.io.IOException;

import com.redhat.ceylon.common.Versions;

public class ImportJarMain {
    private static final int SC_OK = 0;
    private static final int SC_ARGS = 1;
    private static final int SC_ERROR = 2;

    public static void main(String[] args) throws IOException {
        String destDir = null;
        String user = null,pass = null;
        String moduleSpec = null;
        String jarFile = null;
        String verbose = null;
        
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            int argsLeft = args.length - 1 - i;
            if ("-h".equals(arg)
                    || "-help".equals(arg)
                    || "--help".equals(arg)) {
                printUsage(SC_OK);
            } else if ("-v".equals(arg)
                        || "-version".equals(arg)
                        || "--version".equals(arg)) {
                printVersion();
            } else if ("-d".equals(arg)) {
                System.err.println(ImportJarMessages.msg("error.optionDnotSupported"));
                exit(SC_ARGS);
            } else if ("-debug".equals(arg)) {
                verbose = "all";
            } else if ("-out".equals(arg)) {
                if (argsLeft <= 0) {
                    optionMissingArgument(arg);
                }
                destDir = args[++i];
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
                System.err.println(ImportJarMessages.msg("error.optionUnknown", arg));
                exit(SC_ARGS);
            } else if(moduleSpec == null){
                moduleSpec = arg;
            } else if(jarFile == null){
                jarFile = arg;
            } else {
                System.err.println(ImportJarMessages.msg("error.tooManyArguments", arg));
                exit(SC_ARGS);
            }
            
        }
        
        if(moduleSpec == null || moduleSpec.isEmpty()){
            System.err.println(ImportJarMessages.msg("error.noModuleSpecified"));
            printUsage(SC_ARGS);
        }
        if(jarFile == null || jarFile.isEmpty()){
            System.err.println(ImportJarMessages.msg("error.noJarFileSpecified"));
            printUsage(SC_ARGS);
        }
        if (destDir == null) {
            destDir = "modules";
        }

        try{
            CeylonImportJarTool importJar = new CeylonImportJarTool(moduleSpec, destDir, user, pass, jarFile, verbose);
            importJar.publish();
        }catch(ImportJarException x){
            System.err.println(ImportJarMessages.msg("error", x.getLocalizedMessage()));
            // no need to print the stack trace
            exit(SC_ERROR);
        }catch(Exception x){
            System.err.println(ImportJarMessages.msg("error", x.getLocalizedMessage()));
            x.printStackTrace();
            exit(SC_ERROR);
        }
    }

    private static void exit(int statusCode) {
        System.exit(statusCode);
    }
    
    private static void optionMissingArgument(String arg) {
        System.err.println(ImportJarMessages.msg("error.optionMissing", arg));
        exit(SC_ARGS);
    }

    private static void printVersion() {
        System.out.println(ImportJarMessages.msg("info.version", Versions.CEYLON_VERSION));
        exit(SC_OK);
    }

    private static void printUsage(int statusCode) {
        System.err.print(ImportJarMessages.msg("info.usage"));
        exit(statusCode);
    }
}