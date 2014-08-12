package com.redhat.ceylon.compiler.java.runtime.tools.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.java.runtime.tools.CompilationListener;
import com.redhat.ceylon.compiler.java.runtime.tools.Compiler;
import com.redhat.ceylon.compiler.java.runtime.tools.CompilerOptions;
import com.redhat.ceylon.compiler.js.CeylonCompileJsTool;
import com.redhat.ceylon.compiler.js.CompilerErrorException;
import com.redhat.ceylon.compiler.js.DiagnosticListener;

public class JavaScriptCompilerImpl implements Compiler {

    @Override
    public boolean compile(CompilerOptions options, 
                           CompilationListener listener) {
        CeylonCompileJsTool tool = new CeylonCompileJsTool();
        if(options.isVerbose())
            tool.setVerbose("");
        tool.setOffline(options.isOffline());
        try {
            tool.setRepositoryAsStrings(options.getUserRepositories());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tool.setSystemRepository(options.getSystemRepository());
        tool.setOut(options.getOutputRepository());
        tool.setSource(options.getSourcePath());
        // just mix them all
        List<String> moduleOrFile = new ArrayList<String>();
        moduleOrFile.addAll(options.getModules());
        moduleOrFile.addAll(fileToStringList(options.getFiles()));
        tool.setModule(moduleOrFile);
        tool.setDiagnosticListener(adapt(listener));
        tool.setThrowOnError(true);
        // FIXME: allow the user to capture stdout
        if(!options.isVerbose()){
            // make the tool shut the hell up
            tool.setOut(new NullWriter());
        }

        try {
            tool.run();
        } catch (CompilerErrorException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private DiagnosticListener adapt(final CompilationListener listener) {
        return new DiagnosticListener(){
            @Override
            public void moduleCompiled(String module, String version) {
                listener.moduleCompiled(module, version);
            }

            @Override
            public void error(File file, long line, long column, String message) {
                listener.error(file, line, column, message);
            }

            @Override
            public void warning(File file, long line, long column, String message) {
                listener.warning(file, line, column, message);
            }
            
        };
    }

    private List<String> fileToStringList(List<File> files) {
        // FIXME: this must be the same path as given as source folder list
        // for example, if source folder is "./source" then file must be "./source/.../file.ceylon"
        // if source folder is "/absolute/source" then file must be "/absolute/source/.../file.ceylon"
        // This sounds like a death trap, but appears to sorta be required by the typechecker API at this point
        // because it uses VirtualFile which doesn't have File but only a String path, which can be in a Jar
        // in theory (though it's never been used I guess)
        List<String> ret = new ArrayList<String>(files.size());
        for(File file : files){
            ret.add(file.getPath());
        }
        return ret;
    }

    /*private Options convertOptions(CompilerOptions options) {
        // FIXME: support other options
        Options jsCompilerOptions = new Options();
        for(File sourcePath : options.getSourcePath())
            jsCompilerOptions.addSrcDir(sourcePath.getPath());
        if(options.isVerbose())
            jsCompilerOptions.verbose("");
        if(options.getOutputRepository() != null)
            jsCompilerOptions.outRepo(options.getOutputRepository());
        return jsCompilerOptions;
    }*/
}
