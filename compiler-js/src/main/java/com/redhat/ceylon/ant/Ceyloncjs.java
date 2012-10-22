package com.redhat.ceylon.ant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;

import com.redhat.ceylon.common.ant.Module;
import com.redhat.ceylon.common.ant.Rep;
import com.redhat.ceylon.common.tool.Java7Checker;
import com.redhat.ceylon.compiler.Options;
import com.redhat.ceylon.compiler.js.Main;

/** A task to compile Ceylon code to JS from within Ant.
 *
 * @author Enrique Zamudio
 */
public class Ceyloncjs extends Task {

    private List<File> compileList = new ArrayList<File>(2);
    private List<Module> modules = new LinkedList<Module>();
    private FileSet files;
    private Boolean verbose;
    private Boolean optimize;
    private Boolean modulify = true;
    private Boolean gensrc = true;
    private String user;
    private String pass;
    private String out;
    private Path src;
    private Rep systemRepository;
    private List<Rep> repositories = new LinkedList<Rep>();
    //private ExitHandler exitHandler = new ExitHandler();

    /**
     * Sets the user name for the output module repository (HTTP only)
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Sets the password for the output module repository (HTTP only)
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /** Tells the JS compiler whether to wrap the generated code in CommonJS module format. */
    public void setWrapModule(Boolean flag){
        modulify = flag;
    }
    /** Tells the JS compiler whether to use prototype style or not. */
    public void setOptimize(Boolean flag){
        this.optimize = flag;
    }
    /** Tells the JS compiler whether to generate the .src archive; default is true, but can be turned off
     * to save some time when doing joint jvm/js compilation. */
    public void setSrcArchive(Boolean flag) {
        gensrc = flag;
    }
    public void setVerbose(Boolean verbose){
        this.verbose = verbose;
    }

    /**
     * Adds a module to compile
     * @param module the module name to compile
     */
    public void addModule(Module module){
        modules.add(module);
    }

    public void addFiles(FileSet fileset) {
        if (this.files != null) {
            throw new BuildException("<ceyloncjs> only supports a single <files> element");
        }
        this.files = fileset;
    }

    /**
     * Clear the list of files to be compiled and copied..
     */
    protected void resetFileLists() {
        compileList.clear();
    }

    /**
     * Check that all required attributes have been set and nothing silly has
     * been entered.
     *
     * @exception BuildException if an error occurs
     */
    protected void checkParameters() throws BuildException {
        if (this.modules.isEmpty()
                && this.files == null) {
            throw new BuildException("You must specify a <module> and/or <files>");
        }
    }

    /**
     * Set the source directories to find the source Java and Ceylon files.
     * @param src the source directories as a path
     */
    public void setSrc(Path src) {
        if (this.src == null) {
            this.src = src;
        } else {
            this.src.append(src);
        }
    }

    protected List<File> getSrc() {
        if (this.src == null) {
            return Collections.singletonList(getProject().resolveFile("source"));
        }
        String[] paths = this.src.list();
        ArrayList<File> result = new ArrayList<File>(paths.length);
        for (String path : paths) {
            result.add(getProject().resolveFile(path));
        }
        return result;
    }

    /**
     * Set the destination repository into which the Java source files should be
     * compiled.
     * @param out the destination repository
     */
    public void setOut(String out) {
        this.out = out;
    }

    protected String getOut() {
        if (this.out == null) {
            return new File(getProject().getBaseDir(), "modules").getPath();
        }
        return this.out;
    }

    /**
     * Adds a module repository
     * @param rep the new module repository
     */
    public void addRep(Rep rep) {
        repositories.add(rep);
    }

    protected Rep getSystemRepository() {
        return systemRepository;
    }

    /**
     * Sets the system repository
     * @param rep the new system repository
     */
    public void setSysRep(Rep rep) {
        systemRepository = rep;
    }

    protected List<Rep> getRepositories() {
        return repositories;
    }

    /**
     * Executes the task.
     * @exception BuildException if an error occurs
     */
    @Override
    public void execute() throws BuildException {
        Java7Checker.check();

        checkParameters();
        resetFileLists();

        if (files != null) {

            for (File srcDir : getSrc()) {
                FileSet fs = (FileSet)this.files.clone();
                fs.setDir(srcDir);
                if (!srcDir.exists()) {
                    throw new BuildException("source path \"" + srcDir.getPath() + "\" does not exist!", getLocation());
                }

                DirectoryScanner ds = fs.getDirectoryScanner(getProject());
                String[] files = ds.getIncludedFiles();

                for(String fileName : files)
                    compileList.add(new File(srcDir, fileName));
            }
        }

        compile();
    }

    /**
     * Perform the compilation.
     */
    private void compile() {
        if (compileList.size() == 0 && modules.size() == 0){
            log("Nothing to compile");
            return;
        }

        List<String> sourceDirs = new ArrayList<String>(getSrc().size());
        for (File f : getSrc()) {
            sourceDirs.add(f.getAbsolutePath());
        }
        List<String> repos = new ArrayList<String>(repositories.size());
        for (Rep r : repositories) {
            if(r.url == null || r.url.isEmpty())
                continue;
            repos.add(r.toString());
        }
        Options opts = new Options(repos, sourceDirs, systemRepository == null ? null : systemRepository.toString(),
                getOut(), user, pass, optimize != null && optimize.booleanValue(),
                modulify != null && modulify.booleanValue(), true, true, verbose != null && verbose.booleanValue(),
                false, false, gensrc != null && gensrc.booleanValue(), null);
        
        // files to compile
        List<String> files = new ArrayList<String>(compileList.size() + modules.size());
        for (File file : compileList) {
            log("Adding source file: "+file.getAbsolutePath(), Project.MSG_VERBOSE);
            files.add(file.getAbsolutePath());
        }
        // modules to compile
        for (Module module : modules) {
            log("Adding module: "+module, Project.MSG_VERBOSE);
            files.add(module.toDir().getAbsolutePath());
        }

        try {
            Main.run(opts, files);
        } catch (IOException e) {
            throw new BuildException("Error running Ceylon compiler", e, getLocation());
        }
    }

}
