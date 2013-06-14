/*
 * Copyright (c) 1999, 2006, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 */

package com.redhat.ceylon.compiler.java.launcher;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumSet;
import java.util.MissingResourceException;

import javax.annotation.processing.Processor;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardLocation;

import com.redhat.ceylon.compiler.java.tools.CeylonLog;
import com.redhat.ceylon.compiler.java.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.java.tools.LanguageCompiler;
import com.redhat.ceylon.compiler.java.util.Timer;
import com.sun.tools.javac.code.Source;
import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.jvm.Target;
import com.sun.tools.javac.main.CommandLine;
import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.main.JavacOption.Option;
import com.sun.tools.javac.main.RecognizedOptions;
import com.sun.tools.javac.main.RecognizedOptions.OptionHelper;
import com.sun.tools.javac.processing.AnnotationProcessingError;
import com.sun.tools.javac.util.ClientCodeException;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.FatalError;
import com.sun.tools.javac.util.JavacMessages;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javac.util.PropagatedException;

/**
 * This class provides a commandline interface to the GJC compiler.
 * 
 * <p>
 * <b>This is NOT part of any supported API. If you write code that depends on
 * this, you do so at your own risk. This code and its internal interfaces are
 * subject to change or deletion without notice.</b>
 */
public class Main extends com.sun.tools.javac.main.Main {

    /**
     * The name of the compiler, for use in diagnostics.
     */
    String ownName;

    /**
     * The writer to use for diagnostic output.
     */
    PrintWriter out;

    /**
     * If true, any command line arg errors will cause an exception.
     */
    boolean fatalErrors;

    /**
     * Result codes.
     */
    public static final int EXIT_OK = 0, // Compilation completed with no errors.
            EXIT_ERROR = 1, // Completed but reported errors.
            EXIT_CMDERR = 2, // Bad command-line arguments
            EXIT_SYSERR = 3, // System error or resource exhaustion.
            EXIT_ABNORMAL = 4; // Compiler terminated abnormally

    private Option[] recognizedOptions = RecognizedOptions.getJavaCompilerOptions(new OptionHelper() {

        public void setOut(PrintWriter out) {
            Main.this.out = out;
        }

        public void error(String key, Object... args) {
            Main.this.error(key, args);
        }

        public void printVersion() {
            Log.printLines(out, getLocalizedString("version", ownName, JavaCompiler.version()));
        }

        public void printFullVersion() {
            Log.printLines(out, getLocalizedString("fullVersion", ownName, JavaCompiler.fullVersion()));
        }

        public void printHelp() {
            help();
        }

        public void printXhelp() {
            xhelp();
        }

        public void addFile(File f) {
            if (!filenames.contains(f))
                filenames.append(f);
        }

        public void addClassName(String s) {
            classnames.append(s);
        }

    });

    /**
     * Construct a compiler instance.
     */
    public Main(String name) {
        this(name, new PrintWriter(System.err, true));
    }

    /**
     * Construct a compiler instance.
     */
    public Main(String name, PrintWriter out) {
        super(name, out);
        this.ownName = name;
        this.out = out;
    }

    /** A table of all options that's passed to the JavaCompiler constructor. */
    private Options options = null;

    /** A timer used to calculate task execution times times */
    private Timer timer = null;
    
    /**
     * The list of source files to process
     */
    public ListBuffer<File> filenames = null; // XXX sb protected

    /**
     * List of class files names passed on the command line
     */
    public ListBuffer<String> classnames = null; // XXX sb protected

    /**
     * Number of errors found during compilation
     */
    public int errorCount = 0;
    
    /**
     * The exception which caused the compilation to fail with 
     * {@link #EXIT_ABNORMAL}  
     */
    public Throwable abortingException = null;

    /**
     * Report a usage error.
     */
    void error(String key, Object... args) {
        if (fatalErrors) {
            String msg = getLocalizedString(key, args);
            throw new PropagatedException(new IllegalStateException(msg));
        }
        warning(key, args);
        Log.printLines(out, getLocalizedString("msg.usage", ownName));
    }

    /**
     * Report a warning.
     */
    void warning(String key, Object... args) {
        Log.printLines(out, ownName + ": " + getLocalizedString(key, args));
    }

    public Option getOption(String flag) {
        for (Option option : recognizedOptions) {
            if (option.matches(flag))
                return option;
        }
        return null;
    }

    public void setOptions(Options options) {
        if (options == null)
            throw new NullPointerException();
        this.options = options;
    }

    public void setFatalErrors(boolean fatalErrors) {
        this.fatalErrors = fatalErrors;
    }

    /**
     * Process command line arguments: store all command line options in
     * `options' table and return all source filenames.
     * @param flags The array of command line arguments.
     */
    public List<File> processArgs(String[] flags) { // XXX sb protected
        int ac = 0;
        while (ac < flags.length) {
            String flag = flags[ac];
            ac++;

            int j;
            // quick hack to speed up file processing:
            // if the option does not begin with '-', there is no need to check
            // most of the compiler options.
            int firstOptionToCheck = flag.charAt(0) == '-' ? 0 : recognizedOptions.length - 1;
            for (j = firstOptionToCheck; j < recognizedOptions.length; j++)
                if (recognizedOptions[j].matches(flag))
                    break;

            if (j == recognizedOptions.length) {
                error("err.invalid.flag", flag);
                return null;
            }

            Option option = recognizedOptions[j];
            if (option.hasArg()) {
                if (ac == flags.length) {
                    error("err.req.arg", flag);
                    return null;
                }
                String operand = flags[ac];
                ac++;
                if (option.process(options, flag, operand))
                    return null;
            } else {
                if (option.process(options, flag))
                    return null;
            }
        }

        if (!checkDirectoryOrURL("-d"))
            return null;
        if (!checkDirectory("-s"))
            return null;

        String sourceString = options.get("-source");
        Source source = (sourceString != null) ? Source.lookup(sourceString) : Source.DEFAULT;
        String targetString = options.get("-target");
        Target target = (targetString != null) ? Target.lookup(targetString) : Target.DEFAULT;
        // We don't check source/target consistency for CLDC, as J2ME
        // profiles are not aligned with J2SE targets; moreover, a
        // single CLDC target may have many profiles. In addition,
        // this is needed for the continued functioning of the JSR14
        // prototype.
        if (Character.isDigit(target.name.charAt(0))) {
            if (target.compareTo(source.requiredTarget()) < 0) {
                if (targetString != null) {
                    if (sourceString == null) {
                        warning("warn.target.default.source.conflict", targetString, source.requiredTarget().name);
                    } else {
                        warning("warn.source.target.conflict", sourceString, source.requiredTarget().name);
                    }
                    return null;
                } else {
                    options.put("-target", source.requiredTarget().name);
                }
            } else {
                if (targetString == null && !source.allowGenerics()) {
                    options.put("-target", Target.JDK1_4.name);
                }
            }
        }
        return filenames.toList();
    }

    // where
    private boolean checkDirectory(String optName) {
        String value = options.get(optName);
        if (value == null)
            return true;
        File file = new File(value);
        if (!file.exists()) {
            error("err.dir.not.found", value);
            return false;
        }
        if (!file.isDirectory()) {
            error("err.file.not.directory", value);
            return false;
        }
        return true;
    }

    /**
     * Checker whether optName is a valid URL or directory
     * @param optName The option name
     * @return
     */
    private boolean checkDirectoryOrURL(String optName) {
        String value = options.get(optName);
        if (value == null)
            return true;
        try{
            URL url = new URL(value);
            String scheme = url.getProtocol();
            if("http".equals(scheme) || "https".equals(scheme))
                return true;
            error("ceylon.err.output.repo.not.supported", value);
            return false;
        }catch(MalformedURLException x){
            // not a URL, perhaps a file?
        }
        File file = new File(value);
        if (file.exists() && !file.isDirectory()) {
            error("err.file.not.directory", value);
            return false;
        }
        return true;
    }

    /**
     * Programmatic interface for main function.
     * @param args The command line parameters.
     */
    public int compile(String[] args) {
        Context context = new Context();
        CeyloncFileManager.preRegister(context); // can't create it until Log
                                                 // has been set up
        CeylonLog.preRegister(context);
        int result = compile(args, context);
        if (fileManager instanceof JavacFileManager) {
            // A fresh context was created above, so jfm must be a
            // JavacFileManager
            ((JavacFileManager) fileManager).close();
        }
        return result;
    }

    public int compile(String[] args, Context context) {
        return compile(args, context, List.<JavaFileObject> nil(), null);
    }

    /**
     * Programmatic interface for main function.
     * @param args The command line parameters.
     */
    public int compile(String[] args, Context context, List<JavaFileObject> fileObjects, Iterable<? extends Processor> processors) {
        if (options == null) {
            options = Options.instance(context); // creates a new one
        }

        filenames = new ListBuffer<File>();
        classnames = new ListBuffer<String>();
        errorCount  = 0;
        abortingException = null;
        JavaCompiler comp = null;
        /* TODO: Logic below about what is an acceptable command line should be
         * updated to take annotation processing semantics into account. */
        try {
            if (args.length == 0 && fileObjects.isEmpty()) {
                help();
                return EXIT_CMDERR;
            }

            List<File> filenames;
            try {
                filenames = processArgs(CommandLine.parse(args));
                if (filenames == null) {
                    // null signals an error in options, abort
                    return EXIT_CMDERR;
                } else if (filenames.isEmpty() && fileObjects.isEmpty() && classnames.isEmpty()) {
                    // it is allowed to compile nothing if just asking for help
                    // or version info
                    if (options.get("-help") != null 
                            || options.get("-jhelp") != null 
                            || options.get("-X") != null 
                            || options.get("-version") != null 
                            || options.get("-fullversion") != null)
                        return EXIT_OK;
                    error("err.no.source.files");
                    return EXIT_CMDERR;
                }
            } catch (java.io.FileNotFoundException e) {
                Log.printLines(out, ownName + ": " + getLocalizedString("err.file.not.found", e.getMessage()));
                return EXIT_SYSERR;
            }

            // Set up the timer *after* we've processed to options
            // because it needs to know if we need logging or not
            timer = Timer.instance(context);
            timer.init();
            
            boolean forceStdOut = options.get("stdout") != null;
            if (forceStdOut) {
                out.flush();
                out = new PrintWriter(System.out, true);
            }

            context.put(Log.outKey, out);

            fileManager = context.get(JavaFileManager.class);

            comp = LanguageCompiler.instance(context);
            if (comp == null)
                return EXIT_SYSERR;

            if(!classnames.isEmpty())
                filenames = addModuleSources(filenames);
            
            if (!filenames.isEmpty()) {
                // add filenames to fileObjects
                comp = JavaCompiler.instance(context);
                List<JavaFileObject> otherFiles = List.nil();
                JavacFileManager dfm = (JavacFileManager) fileManager;
                for (JavaFileObject fo : dfm.getJavaFileObjectsFromFiles(filenames))
                    otherFiles = otherFiles.prepend(fo);
                for (JavaFileObject fo : otherFiles)
                    fileObjects = fileObjects.prepend(fo);
            }
            if(fileObjects.isEmpty()){
                error("err.no.source.files");
                return EXIT_CMDERR;
            }
            comp.compile(fileObjects, classnames.toList(), processors);

            errorCount = comp.errorCount();
            if (errorCount != 0)
                return EXIT_ERROR;
        } catch (IOException ex) {
            ioMessage(ex);
            return EXIT_SYSERR;
        } catch (OutOfMemoryError ex) {
            resourceMessage(ex);
            return EXIT_SYSERR;
        } catch (StackOverflowError ex) {
            resourceMessage(ex);
            return EXIT_SYSERR;
        } catch (FatalError ex) {
            feMessage(ex);
            return EXIT_SYSERR;
        } catch (AnnotationProcessingError ex) {
            apMessage(ex);
            return EXIT_SYSERR;
        } catch (ClientCodeException ex) {
            // as specified by javax.tools.JavaCompiler#getTask
            // and javax.tools.JavaCompiler.CompilationTask#call
            throw new RuntimeException(ex.getCause());
        } catch (PropagatedException ex) {
            throw ex.getCause();
        } catch (Throwable ex) {
            // Nasty. If we've already reported an error, compensate
            // for buggy compiler error recovery by swallowing thrown
            // exceptions.
            if (comp == null || comp.errorCount() == 0 || options == null || options.get("dev") != null)
                bugMessage(ex);
            this.abortingException = ex;
            return EXIT_ABNORMAL;
        } finally {
            if (comp != null)
                comp.close();
            filenames = null;
            options = null;
            if (timer != null) {
                timer.end();
            }
            timer = null;
        }
        return EXIT_OK;
    }

    private List<File> addModuleSources(List<File> filenames) throws IOException {
        for(String moduleName : classnames){
            String path = moduleName.equals("default") ? "" : moduleName;
            Iterable<JavaFileObject> files = fileManager.list(StandardLocation.SOURCE_PATH, path, EnumSet.of(Kind.SOURCE), true);
            boolean gotOne = false;
            for(JavaFileObject file : files){
                File f = new File(file.toUri().getPath());
                if(!filenames.contains(f))
                    filenames = filenames.prepend(f);
                gotOne = true;
            }
            if(!gotOne){
                warning("ceylon", "Could not find source files for module: "+moduleName);
            }
        }
        classnames.clear();
        return filenames;
    }

    /**
     * Print a message reporting an internal error.
     */
    void bugMessage(Throwable ex) {
        Log.printLines(out, getLocalizedString("msg.bug", JavaCompiler.version()));
        ex.printStackTrace(out);
    }

    /**
     * Print a message reporting an fatal error.
     */
    void feMessage(Throwable ex) {
        Log.printLines(out, ex.getMessage());
    }

    /**
     * Print a message reporting an input/output error.
     */
    void ioMessage(Throwable ex) {
        Log.printLines(out, getLocalizedString("msg.io"));
        ex.printStackTrace(out);
    }

    /**
     * Print a message reporting an out-of-resources error.
     */
    void resourceMessage(Throwable ex) {
        Log.printLines(out, getLocalizedString("msg.resource"));
        // System.out.println("(name buffer len = " + Name.names.length + " " +
        // Name.nc);//DEBUG
        ex.printStackTrace(out);
    }

    /**
     * Print a message reporting an uncaught exception from an annotation
     * processor.
     */
    void apMessage(AnnotationProcessingError ex) {
        Log.printLines(out, getLocalizedString("msg.proc.annotation.uncaught.exception"));
        ex.getCause().printStackTrace();
    }

    private JavaFileManager fileManager;

    /* ************************************************************************
     * Internationalization
     * *********************************************************************** */

    /**
     * Find a localized string in the resource bundle.
     * @param key The key for the localized string.
     */
    public static String getLocalizedString(String key, Object... args) { // FIXME
                                                                          // sb
                                                                          // private
        try {
            if (messages == null)
                messages = new JavacMessages(javacBundleName);
            return messages.getLocalizedString("javac." + key, args);
        } catch (MissingResourceException e) {
            throw new Error("Fatal Error: Resource for javac is missing", e);
        }
    }

    public static void useRawMessages(boolean enable) {
        if (enable) {
            messages = new JavacMessages(javacBundleName) {
                @Override
                public String getLocalizedString(String key, Object... args) {
                    return key;
                }
            };
        } else {
            messages = new JavacMessages(javacBundleName);
        }
    }

    private static final String javacBundleName = "com.sun.tools.javac.resources.ceylonc";

    private static JavacMessages messages;
    
    
}
