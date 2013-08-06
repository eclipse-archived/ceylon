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
import java.util.List;
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

    private static String VERSION="###";

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        try (FileInputStream fIn = new FileInputStream(sourceFile);
                FileChannel source = fIn.getChannel();
                FileOutputStream fOut = new FileOutputStream(destFile);
                FileChannel destination = fOut.getChannel()) {
            long transfered = 0;
            long bytes = source.size();
            while (transfered < bytes) {
                transfered += destination.transferFrom(source, 0, source.size());
                destination.position(transfered);
            }
        } finally {
        }
    }

    private static void compileLanguageModule(List<String> sources, PrintWriter writer, String clmod)
            throws IOException {
        final File clSrcDir = new File("../ceylon.language/src/ceylon/language/");
        final File clSrcDirJs = new File("../ceylon.language/runtime-js");
        File tmpdir = File.createTempFile("ceylonjs", "clsrc");
        tmpdir.delete();
        tmpdir = new File(tmpdir.getAbsolutePath());
        tmpdir.mkdir();
        tmpdir.deleteOnExit();
        final File tmpout = new File(tmpdir, "modules");
        tmpout.mkdir();
        tmpout.deleteOnExit();
        Options opts = Options.parse(new ArrayList<String>(Arrays.asList(
                "-rep", "build/runtime", "-nocomments", "-optimize",
                "-out", tmpout.getAbsolutePath(), "-nomodule")));

        //Typecheck the whole language module
        System.out.println("Compiling language module from Ceylon source");
        TypeCheckerBuilder tcb = new TypeCheckerBuilder().addSrcDirectory(clSrcDir.getParentFile().getParentFile())
                .addSrcDirectory(new File(clSrcDir.getParentFile().getParentFile().getParentFile(), "runtime-js"));
        tcb.setRepositoryManager(CeylonUtils.repoManager().systemRepo(opts.getSystemRepo())
                .userRepos(opts.getRepos()).outRepo(opts.getOutDir()).buildManager());
        //This is to use the JSON metamodel
        JsModuleManagerFactory.setVerbose(true);
        tcb.moduleManagerFactory(new JsModuleManagerFactory((Map<String,Object>)JSONValue.parse(clmod)));
        TypeChecker tc = tcb.getTypeChecker();
        tc.process();
        if (tc.getErrors() > 0) {
            System.exit(1);
        }

        for (String line : sources) {
            //Compile these files
            System.out.println("Compiling " + line);
            final List<String> includes = new ArrayList<String>();
            for (String filename : line.split(",")) {
                final boolean isJsSrc = filename.trim().endsWith(".js");
                final File src = new File(isJsSrc ? clSrcDirJs : clSrcDir,
                        isJsSrc ? filename.trim() :
                        String.format("%s.ceylon", filename.trim()));
                if (src.exists() && src.isFile() && src.canRead()) {
                    includes.add(src.getPath());
                } else {
                    throw new IllegalArgumentException("Invalid Ceylon language module source " + src);
                }
            }
            //Compile only the files specified in the line
            //Set this before typechecking to share some decls that otherwise would be private
            JsCompiler.compilingLanguageModule=true;
            JsCompiler jsc = new JsCompiler(tc, opts).stopOnErrors(false);
            jsc.setFiles(includes);
            jsc.generate();
            File compsrc = new File(tmpout, String.format("ceylon/language/%s/ceylon.language-%<s.js", VERSION));
            if (compsrc.exists() && compsrc.isFile() && compsrc.canRead()) {
                try (BufferedReader jsr = new BufferedReader(new FileReader(compsrc))) {
                    String jsline = null;
                    while ((jsline = jsr.readLine()) != null) {
                        if (!jsline.contains("=require('")) {
                            writer.println(jsline);
                        }
                    }
                } finally {
                    compsrc.delete();
                }
            } else {
                System.out.println("WTF??? No generated js for language module!!!!");
                System.exit(1);
            }
        }
    }

    private static void stitch(File infile, PrintWriter writer, List<String> sourceFiles) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile), "UTF-8"));
        try {
            String line = null;
            String clModel = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    if (line.equals("//#METAMODEL")) {
                        System.out.println("Generating language module metamodel in JSON...");
                        TypeCheckerBuilder tcb = new TypeCheckerBuilder().usageWarnings(false);
                        tcb.addSrcDirectory(new File("../ceylon.language/src"));
                        TypeChecker tc = tcb.getTypeChecker();
                        tc.process();
                        MetamodelVisitor mmg = null;
                        for (PhasedUnit pu : tc.getPhasedUnits().getPhasedUnits()) {
                            if (!pu.getCompilationUnit().getErrors().isEmpty()) {
                                System.out.println("whoa, errors in the language module "
                                        + pu.getCompilationUnit().getLocation());
                                for (Message err : pu.getCompilationUnit().getErrors()) {
                                    System.out.println(err.getMessage());
                                }
                            }
                            if (mmg == null) {
                                mmg = new MetamodelVisitor(pu.getPackage().getModule());
                            }
                            pu.getCompilationUnit().visit(mmg);
                        }
                        writer.print("var $$METAMODEL$$=");
                        clModel = JSONObject.toJSONString(mmg.getModel());
                        writer.print(clModel);
                        writer.println(";");
                        writer.println("exports.$$METAMODEL$$=function(){return $$METAMODEL$$;};");
                        writer.flush();
                    } else if (line.equals("//#COMPILED")) {
                        System.out.println("Compiling language module sources");
                        compileLanguageModule(sourceFiles, writer, clModel);
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
        if (args.length < 3) {
            System.err.println("This program requires 3 arguments to run:");
            System.err.println("1. The path to the main JS file");
            System.err.println("2. The path to the list of language module files to compile from Ceylon source");
            System.err.println("3. The path of the resulting JS file");
            System.exit(1);
            return;
        }
        File infile = new File(args[0]);
        if (infile.exists() && infile.isFile() && infile.canRead()) {
            File outfile = new File(args[2]);
            if (!outfile.getParentFile().exists()) {
                outfile.getParentFile().mkdirs();
            }
            VERSION=outfile.getParentFile().getName();
            ArrayList<String> clsrc = new ArrayList<String>();
            File clSourcesPath = new File(args[1]);
            if (!(clSourcesPath.exists() && clSourcesPath.isFile() && clSourcesPath.canRead())) {
                throw new IllegalArgumentException("Invalid language module sources list " + args[2]);
            }
            try (BufferedReader listReader = new BufferedReader(new FileReader(clSourcesPath))) {
                String line;
                //Copy the files to a temporary dir
                while ((line = listReader.readLine()) != null) {
                    if (!line.startsWith("#") && line.length() > 0) {
                        clsrc.add(line);
                    }
                }
            } finally {
            }
            try (PrintWriter writer = new PrintWriter(outfile, "UTF-8")) {
                stitch(infile, writer, clsrc);
            } finally {
                ShaSigner.sign(outfile, new JULLogger(), true);
            }
        } else {
            System.err.println("Input file is invalid: " + infile);
            System.exit(2);
        }
    }
}
