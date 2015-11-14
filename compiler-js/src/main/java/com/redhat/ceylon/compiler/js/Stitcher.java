package com.redhat.ceylon.compiler.js;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.impl.ShaSigner;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.compiler.js.loader.MetamodelVisitor;
import com.redhat.ceylon.compiler.js.loader.ModelEncoder;
import com.redhat.ceylon.compiler.js.util.JsIdentifierNames;
import com.redhat.ceylon.compiler.js.util.JsJULLogger;
import com.redhat.ceylon.compiler.js.util.JsOutput;
import com.redhat.ceylon.compiler.js.util.Options;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.model.typechecker.model.Module;

/** A simple program that takes the main JS module file and replaces #include markers with the contents of other files.
 * 
 * @author Enrique Zamudio
 */
public class Stitcher {

    private static TypeCheckerBuilder langmodtc;
    private static Path tmpDir;
    
    private static final File baseDir = new File("../language");
    private static final File clSrcDir = new File(baseDir, "src");
    private static final File clSrcFileDir = new File(clSrcDir, "ceylon/language/");
    public static final File LANGMOD_JS_SRC = new File(baseDir, "runtime-js");
    private static final File clJsFileDir = new File(LANGMOD_JS_SRC, "ceylon/language");
    private static JsIdentifierNames names;
    private static Module mod;
    private static final HashSet<File> compiledFiles = new HashSet<>(256);

    private static int compileLanguageModule(final String line, final JsOutput writer)
            throws IOException {
        File clsrcTmpDir = Files.createTempDirectory(tmpDir, "clsrc").toFile();
        final File tmpout = new File(clsrcTmpDir, Constants.DEFAULT_MODULE_DIR);
        tmpout.mkdir();
        final Options opts = new Options().addRepo("build/runtime").comment(false).optimize(true)
                .outRepo(tmpout.getAbsolutePath()).modulify(false).minify(true)
                .addSrcDir(clSrcDir).addSrcDir(LANGMOD_JS_SRC);

        //Typecheck the whole language module
        if (langmodtc == null) {
            langmodtc = new TypeCheckerBuilder()
                    .addSrcDirectory(clSrcDir)
                    .addSrcDirectory(LANGMOD_JS_SRC)
                    .encoding("UTF-8");
            langmodtc.setRepositoryManager(CeylonUtils.repoManager().systemRepo(opts.getSystemRepo())
                    .userRepos(opts.getRepos()).outRepo(opts.getOutRepo()).buildManager());
        }
        final File mod2 = new File(clJsFileDir, "module.ceylon");
        if (!mod2.exists()) {
            try (FileWriter w2 = new FileWriter(mod2);
                    FileReader r2 = new FileReader(new File(clSrcFileDir, "module.ceylon"))) {
                char[] c = new char[512];
                int r = r2.read(c);
                while (r != -1) {
                    w2.write(c, 0, r);
                    r = r2.read(c);
                }
                mod2.deleteOnExit();
            } finally {
            }
        }
        final TypeChecker tc = langmodtc.getTypeChecker();
        tc.process(true);
        if (tc.getErrors() > 0) {
            return 1;
        }

        //Compile these files
        final List<File> includes = new ArrayList<File>();
        for (String filename : line.split(",")) {
            filename = filename.trim();
            final boolean isJsSrc = filename.endsWith(".js");
            final boolean isDir = filename.endsWith("/");
            final boolean exclude = filename.charAt(0)=='-';
            if (exclude) {
                filename = filename.substring(1);
            }
            final File src = ".".equals(filename) ? clSrcFileDir : new File(isJsSrc ? LANGMOD_JS_SRC : clSrcFileDir,
                    isJsSrc || isDir ? filename :
                    String.format("%s.ceylon", filename));
            if (!addFiles(includes, src, exclude)) {
                final File src2 = new File(clJsFileDir, isDir ? filename : String.format("%s.ceylon", filename));
                if (!addFiles(includes, src2, exclude)) {
                    throw new IllegalArgumentException("Invalid Ceylon language module source " + src + " or " + src2);
                }
            }
        }
        if (includes.isEmpty()) {
            return 0;
        }
        //Compile only the files specified in the line
        //Set this before typechecking to share some decls that otherwise would be private
        JsCompiler jsc = new JsCompiler(tc, opts, true)
                .stopOnErrors(false)
                .setSourceFiles(includes);
        if (!jsc.generate()) {
            jsc.printErrorsAndCount(new OutputStreamWriter(System.out));
            return 1;
        } else {
            // We still call this here for any warning there might be
            jsc.printErrorsAndCount(new OutputStreamWriter(System.out));
        }
        if (names == null) {
            names = jsc.getNames();
        }
        File compsrc = new File(tmpout, "delete/me/delete-me.js");
        if (compsrc.exists() && compsrc.isFile() && compsrc.canRead()) {
            try {
                writer.outputFile(compsrc);
            } finally {
                compsrc.delete();
            }
        } else {
            System.out.println("Can't find generated js for language module in " + compsrc.getAbsolutePath());
            return 1;
        }
        return 0;
    }

    private static boolean addFiles(final List<File> includes, final File src, boolean exclude) {
        if (src.exists() && src.isFile() && src.canRead()) {
            if (exclude) {
                System.out.println("EXCLUDING " + src);
                compiledFiles.add(src);
            } else if (!compiledFiles.contains(src)) {
                includes.add(src);
                compiledFiles.add(src);
            }
        } else if (src.exists() && src.isDirectory()) {
            for (File sub : src.listFiles()) {
                if (!compiledFiles.contains(sub)) {
                    includes.add(sub);
                    compiledFiles.add(sub);
                }
            }
        } else {
            return false;
        }
        return true;
    }
    
    private static int encodeModel(final File moduleFile) throws IOException {
        final String name = moduleFile.getName();
        final File file = new File(moduleFile.getParentFile(),
                name.substring(0,name.length()-3)+ArtifactContext.JS_MODEL);
        System.out.println("Generating language module compile-time model in JSON...");
        TypeCheckerBuilder tcb = new TypeCheckerBuilder().usageWarnings(false);
        tcb.addSrcDirectory(clSrcDir);
        TypeChecker tc = tcb.getTypeChecker();
        tc.process(true);
        MetamodelVisitor mmg = null;
        final ErrorCollectingVisitor errVisitor = new ErrorCollectingVisitor(tc);
        for (PhasedUnit pu : tc.getPhasedUnits().getPhasedUnits()) {
            pu.getCompilationUnit().visit(errVisitor);
            if (errVisitor.getErrorCount() > 0) {
                errVisitor.printErrors(false, false);
                System.out.println("whoa, errors in the language module "
                        + pu.getCompilationUnit().getLocation());
                return 1;
            }
            if (mmg == null) {
                mmg = new MetamodelVisitor(pu.getPackage().getModule());
            }
            pu.getCompilationUnit().visit(mmg);
        }
        mod = tc.getPhasedUnits().getPhasedUnits().get(0).getPackage().getModule();
        try (FileWriter writer = new FileWriter(file)) {
            JsCompiler.beginWrapper(writer);
            writer.write("ex$.$CCMM$=");
            ModelEncoder.encodeModel(mmg.getModel(), writer);
            writer.write(";\n");
            final JsOutput jsout = new JsOutput(mod, "UTF-8", true) {
                @Override
                public Writer getWriter() throws IOException {
                    return writer;
                }
            };
            jsout.outputFile(new File(LANGMOD_JS_SRC, "MODEL.js"));
            JsCompiler.endWrapper(writer);
        } finally {
            ShaSigner.sign(file, new JsJULLogger(), true);
        }
        return 0;
    }

    private static int stitch(File infile, final JsOutput writer) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile), "UTF-8"));
        try {
            String line = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    if (line.startsWith("COMPILE ")) {
                        final String sourceFiles = line.substring(8);
                        System.out.println("Compiling language module sources: " + sourceFiles);
                        int exitCode = compileLanguageModule(sourceFiles, writer);
                        if (exitCode != 0) {
                            return exitCode;
                        }
                    }
                }
            }
        } finally {
            if (reader != null) reader.close();
        }
        return 0;
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("This program requires 2 arguments to run:");
            System.err.println("1. The path to the master file (the one with the list of files to compile)");
            System.err.println("2. The path of the resulting JS file");
            System.exit(1);
            return;
        }
        int exitCode = 0;
        tmpDir = Files.createTempDirectory("ceylon-jsstitcher-");
        try {
            File infile = new File(args[0]);
            if (infile.exists() && infile.isFile() && infile.canRead()) {
                File outfile = new File(args[1]);
                if (!outfile.getParentFile().exists()) {
                    outfile.getParentFile().mkdirs();
                }
                exitCode = encodeModel(outfile);
                if (exitCode == 0) {
                    final int p0 = args[1].indexOf(".language-");
                    final String version = args[1].substring(p0+10,args[1].length()-3);
                    try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outfile), "UTF-8")) {
                        final JsOutput jsout = new JsOutput(mod, "UTF-8", true) {
                            @Override
                            public Writer getWriter() throws IOException {
                                return writer;
                            }
                        };
                        //CommonJS wrapper
                        JsCompiler.beginWrapper(writer);
                        //Model
                        jsout.out("var _CTM$;function $CCMM$(){if (_CTM$===undefined)_CTM$=require('",
                                "ceylon/language/", version, "/ceylon.language-", version, "-model",
                                "').$CCMM$;return _CTM$;}\nex$.$CCMM$=$CCMM$;");
                        //Compile all the listed files
                        exitCode = stitch(infile, jsout);
                        //Unshared declarations
                        if (names != null) {
                            jsout.publishUnsharedDeclarations(names);
                        }
                        //Close the commonJS wrapper
                        JsCompiler.endWrapper(writer);
                    } finally {
                        ShaSigner.sign(outfile, new JsJULLogger(), true);
                    }
                }
            } else {
                System.err.println("Input file is invalid: " + infile);
                exitCode = 2;
            }
        } finally {
            FileUtil.deleteQuietly(tmpDir.toFile());
        }
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }
}
