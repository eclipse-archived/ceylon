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
        long t0, t1, t2, t3, t4;
        boolean optimize = false;
        boolean modulify = false;
        boolean noindent = false;
        boolean nocomments = false;
        boolean verbose = false;
        boolean profile = false;
        List<String> path = new ArrayList<String>(args.length);
        if ( args.length==0 ) {
            System.err.println("Usage ceylonjs [options] [file|dir]...");
            System.err.println();
            System.err.println("Options:");
            System.err.println("-optimize    Create prototype-style JS code");
            System.err.println("-module      Wrap generated code as CommonJS module");
            System.err.println("-noindent    Do NOT indent code");
            System.err.println("-nocomments  Do not generate any comments");
            System.err.println("-compact     Same as -noindent -nocomments");
            System.err.println("-verbose     Tell the whole story");
            System.err.println("-profile     Time the compilation phases (results are printed to STDERR)");
            System.err.println();
            System.err.println("If no files are specified or '--' is used, STDIN is read.");
            System.exit(-1);
            return;
        }
        else {
            for (String arg : args) {
                if ("-optimize".equals(arg)) optimize=true;
                else if ("-module".equals(arg)) modulify=true;
                else if ("-compact".equals(arg)) {
                    noindent=true; nocomments=true;
                } else if ("-noindent".equals(arg)) noindent=true;
                else if ("-nocomments".equals(arg)) nocomments=true;
                else if ("-verbose".equals(arg)) verbose=true;
                else if ("-profile".equals(arg)) profile=true;
                else if ("--".equals(arg)) {
                    path.clear();
                    break;
                } else path.add(arg);
            }
        }

        TypeChecker typeChecker;
        if (path.isEmpty()) {
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
            t0 = System.nanoTime();
            typeChecker = new TypeCheckerBuilder()
                    .verbose(verbose)
                    .addSrcDirectory(src)
                    .getTypeChecker();
            t1=System.nanoTime();
        } else {
            t0=System.nanoTime();
            TypeCheckerBuilder tcb = new TypeCheckerBuilder()
                .verbose(verbose);
            for (String filedir : path) {
              tcb.addSrcDirectory(new File(filedir));
            }
            typeChecker = tcb.getTypeChecker();
            t1=System.nanoTime();
        }
        //getting the type checker does process all types in the source directory
        typeChecker.process();
        t2=System.nanoTime();
        JsCompiler jsc = new JsCompiler(typeChecker).optimize(optimize)
            .comment(!nocomments).indent(!noindent).modulify(modulify);
        t3=System.nanoTime();
        if (!jsc.generate()) {
            jsc.printErrors(System.out);
        }
        t4=System.nanoTime();
        if (profile) {
            System.err.println("PROFILING INFORMATION");
            System.err.printf("TypeChecker creation:   %6d nanos%n", t1-t0);
            System.err.printf("TypeChecker processing: %6d nanos%n", t2-t1);
            System.err.printf("JS compiler creation:   %6d nanos%n", t3-t2);
            System.err.printf("JS compilation:         %6d nanos%n", t4-t3);
        }
    }
}
