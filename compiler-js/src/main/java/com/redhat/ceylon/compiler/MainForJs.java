package com.redhat.ceylon.compiler;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.js.JsCompiler;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;

/**
 * Entry point for the type checker
 * Pass the source diretory as parameter. The source directory is relative to
 * the startup directory.
 *
 * @author Gavin King <gavin@hibernate.org>
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class MainForJs {

    /**
     * Files that are not under a proper module structure are placed under a <nomodule> module.
     */
    public static void main(String[] args) throws Exception {
        String path;
        if ( args.length==0 ) {
            System.err.println("Usage Main <directoryName>");
            System.exit(-1);
            return;
        }
        else {
            path = args[0];
        }
        
        boolean noisy = "true".equals(System.getProperties().getProperty("verbose"));

        TypeChecker typeChecker;
        if ("--".equals(path)) {
            VirtualFile src = new VirtualFile() {
                @Override
                public boolean isFolder() {
                    return false;
                }
                @Override
                public String getName() {
                    return "SCRIPT.ceylon";
                }
                @Override
                public String getPath() {
                    return getName();
                }
                @Override
                public InputStream getInputStream() {
                    return System.in;
                }
                @Override
                public List<VirtualFile> getChildren() {
                    return new ArrayList<VirtualFile>(0);
                }
                @Override
                public int hashCode() {
                    return getPath().hashCode();
                }
                @Override
                public boolean equals(Object obj) {
                    if (obj instanceof VirtualFile) {
                        return ((VirtualFile) obj).getPath().equals(getPath());
                    }
                    else {
                        return super.equals(obj);
                    }
                }
            };
            typeChecker = new TypeCheckerBuilder()
                    .verbose(noisy)
                    .addSrcDirectory(src)
                    .getTypeChecker();
        } else {
            typeChecker = new TypeCheckerBuilder()
                    .verbose(noisy)
                    .addSrcDirectory(new File(path))
                    .getTypeChecker();
        }
        //getting the type checker does process all types in the source directory
        typeChecker.process();
        if (typeChecker.getErrors() > 0) {
            System.exit(1);
            return;
        }
        JsCompiler jsc = new JsCompiler(typeChecker).optimize(true);
        if (!jsc.generate()) {
            jsc.printErrors(System.out);
        }
    }
}
