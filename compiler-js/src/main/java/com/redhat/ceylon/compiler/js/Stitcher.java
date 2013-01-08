package com.redhat.ceylon.compiler.js;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.impl.JULLogger;
import com.redhat.ceylon.cmr.impl.ShaSigner;
import com.redhat.ceylon.compiler.Options;
import com.redhat.ceylon.compiler.loader.JsModuleManagerFactory;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Message;

/** A simple program that takes the main JS module file and replaces #include markers with the contents of other files.
 * 
 * @author Enrique Zamudio
 */
public class Stitcher {

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        FileInputStream fIn = null;
        FileOutputStream fOut = null;
        FileChannel source = null;
        FileChannel destination = null;
        try {
            fIn = new FileInputStream(sourceFile);
            source = fIn.getChannel();
            fOut = new FileOutputStream(destFile);
            destination = fOut.getChannel();
            long transfered = 0;
            long bytes = source.size();
            while (transfered < bytes) {
                transfered += destination.transferFrom(source, 0, source.size());
                destination.position(transfered);
            }
        } finally {
            if (source != null) {
                source.close();
            } else if (fIn != null) {
                fIn.close();
            }
            if (destination != null) {
                destination.close();
            } else if (fOut != null) {
                fOut.close();
            }
        }
    }

    private static void compileLanguageModule(PrintWriter writer, String clmod) throws IOException {
        File srcdir = new File("src/main/resources/source");
        srcdir.mkdirs();
        Options opts = Options.parse(new ArrayList<String>(Arrays.asList(
                "-rep", "build/runtime", "-nocomments", "-optimize",
                "-out", "src/main/resources/modules", "-nomodule")));

        //Read the list of files to copy
        BufferedReader listReader = new BufferedReader(new FileReader("src/main/resources/language-module.txt"));
        try {
            String line;
            //Copy the files to a temporary dir
            while ((line = listReader.readLine()) != null) {
                if (!line.startsWith("#") && line.length() > 0) {
                    //Erase all existing files
                    for (File f : srcdir.listFiles()) {
                        f.delete();
                    }
                    //Compile these files
                    System.out.println("Compiling language module " + line);
                    for (String filename : line.split(",")) {
                        File src = new File(String.format("../ceylon.language/src/ceylon/language/%s.ceylon", filename.trim()));
                        if (src.exists() && src.isFile() && src.canRead()) {
                            File dst = new File(srcdir, src.getName());
                            copyFile(src, dst);
                        } else {
                            throw new IllegalArgumentException("Invalid Ceylon language module source " + src);
                        }
                    }
                    //Compile all that stuff
                    TypeCheckerBuilder tcb = new TypeCheckerBuilder().addSrcDirectory(srcdir);
                    tcb.setRepositoryManager(CeylonUtils.repoManager().systemRepo(opts.getSystemRepo())
                            .userRepos(opts.getRepos()).outRepo(opts.getOutDir()).buildManager());
                    tcb.moduleManagerFactory(new JsModuleManagerFactory(
                            clmod == null ? null : (Map<String,Object>)JSONValue.parse(clmod)));
                    TypeChecker tc = tcb.getTypeChecker();
                    tc.process();
                    if (tc.getErrors() > 0) {
                        System.exit(1);
                    }
                    JsCompiler jsc = new JsCompiler(tc, opts).stopOnErrors(false);
                    JsCompiler.compilingLanguageModule=true;
                    jsc.generate();
                    File compsrc = new File("src/main/resources/modules/default/default.js");
                    if (compsrc.exists() && compsrc.isFile() && compsrc.canRead()) {
                        BufferedReader jsr = new BufferedReader(new FileReader(compsrc));
                        try {
                            String jsline = null;
                            while ((jsline = jsr.readLine()) != null) {
                                writer.println(jsline);
                            }
                        } finally {
                            jsr.close();
                            compsrc.delete();
                        }
                    } else {
                        System.out.println("WTF??? No generated default.js for language module!!!!");
                        System.exit(1);
                    }
                }
            }
        } finally {
            listReader.close();
        }
    }

    private static void stitch(File infile, PrintWriter writer) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile), "UTF-8"));
        try {
            String line = null;
            String clModel = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    if (line.startsWith("//#include ")) {
                        File auxfile = new File(infile.getParentFile(), line.substring(11).trim());
                        if (auxfile.exists() && auxfile.isFile() && auxfile.canRead()) {
                            stitch(auxfile, writer);
                        } else {
                            throw new IllegalArgumentException("Invalid included file " + auxfile);
                        }
                    } else if (line.equals("//#METAMODEL")) {
                        System.out.println("Generating language module metamodel in JSON...");
                        TypeCheckerBuilder tcb = new TypeCheckerBuilder().usageWarnings(false);
                        tcb.addSrcDirectory(new File("../ceylon.language/src"));
                        TypeChecker tc = tcb.getTypeChecker();
                        tc.process();
                        MetamodelVisitor mmg = null;
                        for (PhasedUnit pu : tc.getPhasedUnits().getPhasedUnits()) {
                            if (!pu.getCompilationUnit().getErrors().isEmpty()) {
                                System.out.println("whoa, errors in the language module " + pu.getCompilationUnit().getLocation());
                                for (Message err : pu.getCompilationUnit().getErrors()) {
                                    System.out.println(err.getMessage());
                                }
                            }
                            if (mmg == null) {
                                mmg = new MetamodelVisitor(pu.getPackage().getModule());
                            }
                            pu.getCompilationUnit().visit(mmg);
                        }
                        writer.print("$$metamodel$$=");
                        clModel = JSONObject.toJSONString(mmg.getModel());
                        writer.print(clModel);
                        writer.println(";");
                        writer.println("exports.$$metamodel$$=$$metamodel$$;");
                        writer.flush();
                    } else if (line.equals("//#COMPILED")) {
                        System.out.println("Compiling language module sources");
                        compileLanguageModule(writer, clModel);
                    } else if (!line.endsWith("//IGNORE")) {
                        writer.println(line);
                    }
                }
            }
        } finally {
            if (reader != null) reader.close();
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("This program requires 2 arguments to run:");
            System.err.println("1. The path to the main JS file");
            System.err.println("2. The destination directory for the processed file");
            System.err.println();
            System.err.println("You can specify a third parameter which is a version number,");
            System.err.println("which will be inserted between filename and extension.");
            System.exit(1);
            return;
        }
        File infile = new File(args[0]);
        if (infile.exists() && infile.isFile() && infile.canRead()) {
            File outdir = new File(args[1]);
            if (outdir.exists() && outdir.isDirectory() && outdir.canWrite()) {
                String outname = infile.getName();
                if (args.length>2) {
                    final int dotpos = outname.lastIndexOf('.');
                    outname = String.format("%s-%s%s", outname.substring(0, dotpos), args[2], outname.substring(dotpos));
                }
                File outfile = new File(outdir, outname);
                PrintWriter writer = new PrintWriter(outfile, "UTF-8");
                stitch(infile, writer);
                writer.close();
                ShaSigner.sign(outfile, new JULLogger(), true);
            } else {
                System.err.println("Output directory is invalid: " + outdir);
                System.exit(3);
            }
        } else {
            System.err.println("Input file is invalid: " + infile);
            System.exit(2);
        }
    }
}
