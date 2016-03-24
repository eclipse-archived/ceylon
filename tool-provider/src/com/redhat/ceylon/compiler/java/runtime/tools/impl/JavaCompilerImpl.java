package com.redhat.ceylon.compiler.java.runtime.tools.impl;

import java.io.File;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.redhat.ceylon.javax.tools.Diagnostic;
import com.redhat.ceylon.javax.tools.Diagnostic.Kind;
import com.redhat.ceylon.javax.tools.DiagnosticListener;
import com.redhat.ceylon.javax.tools.JavaFileObject;

import com.redhat.ceylon.compiler.java.launcher.Main.ExitState;
import com.redhat.ceylon.compiler.java.launcher.Main.ExitState.CeylonState;
import com.redhat.ceylon.compiler.java.runtime.tools.CompilationListener;
import com.redhat.ceylon.compiler.java.runtime.tools.Compiler;
import com.redhat.ceylon.compiler.java.runtime.tools.CompilerOptions;
import com.redhat.ceylon.compiler.java.runtime.tools.JavaCompilerOptions;
import com.redhat.ceylon.compiler.java.tools.CeylonTaskListener;
import com.redhat.ceylon.compiler.java.tools.CeyloncTaskImpl;
import com.redhat.ceylon.compiler.java.tools.CeyloncTool;
import com.redhat.ceylon.langtools.source.util.TaskEvent;
import com.redhat.ceylon.langtools.tools.javac.api.ClientCodeWrapper.Trusted;
import com.redhat.ceylon.langtools.tools.javac.file.JavacFileManager;

public class JavaCompilerImpl implements Compiler {

    // if this is not here, this task will be wrapped into a TaskListener that does not implement CeylonTaskListener
    @Trusted
    public static class CompilationListenerAdapter implements DiagnosticListener<JavaFileObject>, CeylonTaskListener {

        private CompilationListener listener;

        public CompilationListenerAdapter(CompilationListener listener) {
            this.listener = listener;
        }

        @Override
        public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
            Kind kind = diagnostic.getKind();
            String message = diagnostic.getMessage(Locale.getDefault());
            long line = diagnostic.getLineNumber();
            long column = diagnostic.getColumnNumber();
            JavaFileObject source = diagnostic.getSource();
            File file = null;
            if(source != null) {
                try {
                    file = new File(source.toUri());
                } catch (IllegalArgumentException ignore) {
                    // An entry in a zip file that is not hierarchical
                }
            }
            switch(kind){
            case ERROR:
                listener.error(file, line, column, message);
                break;
            case WARNING:
            case MANDATORY_WARNING:
                listener.warning(file, line, column, message);
                break;
            case NOTE:
            case OTHER:
            default:
                // ignore?
                break;
            }
        }

        @Override
        public void started(TaskEvent e) {
        }

        @Override
        public void finished(TaskEvent e) {
        }

        @Override
        public void moduleCompiled(String name, String version) {
            listener.moduleCompiled(name, version);
        }

    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean compile(CompilerOptions options, 
                           CompilationListener listener) {
        CeyloncTool compiler = new CeyloncTool();
        CompilationListenerAdapter diagnosticListener = new CompilationListenerAdapter(listener);
        Writer writer = null;
        // FIXME: allow the user to capture stdout
        if(!options.isVerbose()){
            // make the tool shut the hell up
            writer = new NullWriter();
        }

        JavacFileManager fileManager = compiler.getStandardFileManager(writer, diagnosticListener, null, null);

        
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(options.getFiles());

        CeyloncTaskImpl compilerTask = compiler.getTask(null, fileManager, diagnosticListener, 
                                                        translateOptions(options), options.getModules(), compilationUnits);
        compilerTask.setTaskListener(diagnosticListener);
        ExitState state = compilerTask.call2();
        // print any helpful info if required
        if(options.isVerbose() && state.abortingException != null)
            state.abortingException.printStackTrace();
        return state.ceylonState == CeylonState.OK;
    }

    private List<String> translateOptions(CompilerOptions options) {
        List<String> translatedOptions = new ArrayList<String>();
        // FIXME: translate every option
        if(options.isVerbose())
            translatedOptions.add("-verbose");
        for(File sourcePath : options.getSourcePath()){
            translatedOptions.add("-src");
            translatedOptions.add(sourcePath.getPath());
        }
        for(String rep : options.getUserRepositories()){
            translatedOptions.add("-rep");
            translatedOptions.add(rep);
        }
        if(options.getOutputRepository() != null){
            translatedOptions.add("-out");
            translatedOptions.add(options.getOutputRepository());
        }
        if(options.getSystemRepository() != null){
            translatedOptions.add("-sysrep");
            translatedOptions.add(options.getSystemRepository());
        }
        if(options.getOverrides() != null){
            translatedOptions.add("-overrides");
            translatedOptions.add(options.getOverrides());
        }
        if(options instanceof JavaCompilerOptions){
            JavaCompilerOptions javaOptions = (JavaCompilerOptions) options;
            if(javaOptions.isFlatClasspath())
                translatedOptions.add("-flat-classpath");
            if(javaOptions.isAutoExportMavenDependencies())
                translatedOptions.add("-auto-export-maven-dependencies");
        }
        return translatedOptions;
    }

}
