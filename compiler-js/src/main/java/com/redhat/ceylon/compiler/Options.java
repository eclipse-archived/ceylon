package com.redhat.ceylon.compiler;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.config.DefaultToolOptions;

/** Represents all the options for compiling.
 * 
 * @author Enrique Zamudio
 */
public class Options {

    private File cwd;
    private List<String> repos = new ArrayList<String>();
    private String systemRepo;
    private String cacheRepo;
    private boolean noDefRepos;
    private String user;
    private String pass;
    private List<String> srcDirs = new ArrayList<String>();
    private String outDir = DefaultToolOptions.getCompilerOutDir().getPath();
    private boolean optimize;
    private boolean modulify = true;
    private boolean indent = true;
    private boolean comment = true;
    private String verbose;
    private boolean profile;
    private boolean help;
    private boolean version;
    private boolean stdin;
    private boolean gensrc = true;
    private String encoding = System.getProperty("file.encoding");
    private boolean offline;

    public Options() {}

    /** Creates and returns an Options object from a command-list argument list. The list itself
     * is modified, so at the end it only contains the files to compile. */
    public static Options parse(List<String> args) {
        Options opts = new Options();
        //Review all non-arg options
        opts.version = findOption("-version", args, true);
        opts.help = findOption("-help", args, true);
        opts.optimize = findOption("-optimize", args, true);
        opts.modulify = !findOption("-nomodule", args, true);
        if (args.contains("-noindent") || args.contains("-compact")) {
            opts.indent=false;
            args.remove("-noindent");
        }
        if (args.contains("-nocomments") || args.contains("-compact")) {
            opts.comment=false;
            args.remove("-nocomments");
            args.remove("-compact");
        }
        opts.verbose = (findOption("-verbose", args, true)) ? "" : null;
        opts.offline = findOption("-offline", args, true);
        opts.profile = findOption("-profile", args, true);
        opts.stdin = findOption("--", args, true);
        //Review arg options
        for (Iterator<String> iter = args.iterator(); iter.hasNext();) {
            String s = iter.next();
            if (s.startsWith("-")) {
                iter.remove();
                //Get the option's value
                if (iter.hasNext()) {
                    String v = iter.next();
                    iter.remove();
                    if ("-cwd".equals(s)) {
                        opts.cwd=new File(v);
                    } else if ("-rep".equals(s)) {
                        opts.addRepo(v);
                    } else if ("-sysrep".equals(s)) {
                        opts.systemRepo=v;
                    } else if ("-cacherep".equals(s)) {
                        opts.cacheRepo=v;
                    } else if ("-user".equals(s)) {
                        opts.user=v;
                    } else if ("-pass".equals(s)) {
                        opts.pass=v;
                    } else if ("-src".equals(s)) {
                        //Split the value
                        int pos;
                        while ((pos = v.indexOf(File.pathSeparator)) >= 0) {
                            opts.srcDirs.add(v.substring(0, pos));
                            v = v.substring(pos+File.pathSeparator.length());
                        }
                        opts.srcDirs.add(v);
                    } else if ("-out".equals(s)) {
                        opts.outDir=v;
                    } else if ("-encoding".equals(s)) {
                        opts.encoding = v;
                    } else {
                        System.err.printf("Unrecognized option %s %s%n", s, v);
                    }
                }
            }
        }
        if (opts.srcDirs.isEmpty()) {
            opts.srcDirs.addAll(FileUtil.filesToPathList(DefaultToolOptions.getCompilerSourceDirs()));
        }
        return opts;
    }

    /** Find all the repos specified in the argument list (pairs of "-rep x").
     * @param args The argument list from which to parse repositories
     * @param remove If true, removes found repos from arguments, otherwise leaves list intact.
     * @return The list of found repositories. */
    public static List<String> findRepos(List<String> args, boolean remove) {
        ArrayList<String> repos = new ArrayList<String>(args.size() / 2);
        for (Iterator<String> iter = args.iterator(); iter.hasNext();) {
            String s = iter.next();
            if ("-rep".equals(s)) {
                if (remove) {
                    iter.remove();
                }
                if (iter.hasNext()) {
                    s = iter.next();
                    repos.add(s);
                    if (remove) {
                        iter.remove();
                    }
                }
            }
        }
        return repos;
    }

    /** Finds the value for an option that requires value. Can remove it from the original list if needed.
     * @param optionName The name for the option (usually starts with "-")
     * @param args The list of arguments where to look for the option and its value
     * @param remove If true, removes the option and its value from the list.
     * @return The value for the specified option, or null if not found. */
    public static String findOptionValue(String optionName, List<String> args, boolean remove) {
        int idx = args.indexOf(optionName);
        if (idx >=0 && idx < args.size() - 2 && !args.get(idx+1).startsWith("-")) {
            if (remove) {
                args.remove(idx);
                return args.remove(idx);
            } else {
                return args.get(idx+1);
            }
        }
        return null;
    }

    /** Finds the specified option among the arguments, and removes it if needed.
     * @param name The option name (usually starts with "-")
     * @param args The list of arguments where to look for the option
     * @param remove If true, removes the option from the arguments.
     * @return true if the option was found, false otherwise. */
    public static boolean findOption(String name, List<String> args, boolean remove) {
        int idx = args.indexOf(name);
        if (idx >= 0) {
            if (remove) {
                args.remove(idx);
            }
            return true;
        }
        return false;
    }

    /** Returns the current working directory (default = null) */
    public File getCwd() {
        return cwd;
    }
    
    public Options cwd(File cwd) {
        this.cwd = cwd;
        return this;
    }

    /** Returns the list of repositories that were parsed from the command line. */
    public List<String> getRepos() {
        return repos;
    }
    
    public Options addRepo(String repo) {
        repos.add(repo);
        return this;
    }
    
    public Options repos(List<String> repos) {
        this.repos.addAll(repos);
        return this;
    }
    
    /** Returns the system repository (default = null) */
    public String getSystemRepo() {
        return systemRepo;
    }

    public Options systemRepo(String systemRepo) {
        this.systemRepo = systemRepo;
        return this;
    }

    /** Returns the cache folder (default = null) */
    public String getCacheRepo() {
        return cacheRepo;
    }
    
    public Options cacheRepo(String cacheRepo) {
        this.cacheRepo = cacheRepo;
        return this;
    }

    public boolean getNoDefaultRepos() {
        return noDefRepos;
    }

    public Options noDefaultRepos(boolean noDefRepos) {
        this.noDefRepos = noDefRepos;
        return this;
    }

    public String getUser() {
        return user;
    }
    
    public Options user(String user) {
        this.user = user;
        return this;
    }

    public String getPass() {
        return pass;
    }
    
    public Options pass(String pass) {
        this.pass = pass;
        return this;
    }

    /** Returns a list of the source directories. By default it's just one, "source". */
    public List<String> getSrcDirs() {
        return srcDirs;
    }
    
    public Options addSrc(String src) {
        srcDirs.add(src);
        return this;
    }

    public Options sources(List<String> srcs) {
        srcDirs.addAll(srcs);
        return this;
    }

    public String getOutDir() {
        return outDir;
    }
    
    public Options outDir(String outDir) {
        this.outDir = outDir;
        return this;
    }

    public boolean isOptimize() {
        return optimize;
    }
    
    public Options optimize(boolean optimize) {
        this.optimize = optimize;
        return this;
    }

    public boolean isModulify() {
        return modulify;
    }
    
    public Options modulify(boolean modulify) {
        this.modulify = modulify;
        return this;
    }

    public boolean isIndent() {
        return indent;
    }
    
    public Options indent(boolean indent) {
        this.indent = indent;
        return this;
    }

    public boolean isComment() {
        return comment;
    }
    
    public Options comment(boolean comment) {
        this.comment = comment;
        return this;
    }

    public String getVerbose() {
        return verbose;
    }
    
    public Options verbose(String verbose) {
        this.verbose = verbose;
        return this;
    }

    public boolean isProfile() {
        return profile;
    }
    
    public Options profile(boolean profile) {
        this.profile = profile;
        return this;
    }

    public boolean isVersion() {
        return version;
    }
    
    public Options version(boolean version) {
        this.version = version;
        return this;
    }

    public boolean isStdin() {
        return stdin;
    }
    
    public Options stdin(boolean stdin) {
        this.stdin = stdin;
        return this;
    }

    public boolean isHelp() {
        return help;
    }
    
    public Options help(boolean help) {
        this.help = help;
        return this;
    }

    /** The character encoding to use when reading source files. */
    public String getEncoding() {
        return encoding;
    }
    
    public Options encoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public boolean getOffline() {
        return offline;
    }

    public Options offline(boolean offline) {
        this.offline = offline;
        return this;
    }

    /** Sets the option to generate the source archive or skip it.
     * The default is to generate it, but it can be set to false
     * for example when doing joint compilation with another backend
     * that will generate .src archives. */
    public Options generateSourceArchive(boolean flag) {
        gensrc = flag;
        return this;
    }
    
    /** Tells whether to generate the .src archive (default true). */
    public boolean isGenerateSourceArchive() {
        return gensrc;
    }
    
    // Returns true if --verbose or --verbose=all has been passed on the command line
    public boolean isVerbose() {
        return hasVerboseFlag("");
    }

    // Returns true if one of the argument passed matches one of the flags given to
    // --verbose=... on the command line or if one of the flags is "all"
    public boolean hasVerboseFlag(String flag) {
        if (verbose == null) {
            return false;
        }
        if (verbose.isEmpty()) {
            return true;
        }
        List<String> lst = Arrays.asList(verbose.split(","));
        if (lst.contains("all")) {
            return true;
        }
        return lst.contains(flag);
    }

}
