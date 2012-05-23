package com.redhat.ceylon.compiler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** Represents all the options for compiling.
 * 
 * @author Enrique Zamudio
 */
public class Options {

    private List<String> repos = new ArrayList<String>();
    private String user;
    private String pass;
    private String srcDir = "source";
    private String outDir = "modules";
    private boolean optimize;
    private boolean modulify;
    private boolean indent = true;
    private boolean comment = true;
    private boolean verbose;
    private boolean profile;
    private boolean help;
    private boolean version;
    private boolean stdin;

    /** Creates and returns an Options object from a command-list argument list. The list itself
     * is modified, so at the end it only contains files to compile and module names. */
    public static Options parse(List<String> args) {
        Options opts = new Options();
        //Review all non-arg options
        if (args.contains("-version")) {
            opts.version=true;
            args.remove("-version");
        }
        if (args.contains("-help")) {
            opts.help=true;
            return opts;
        }
        if (args.contains("-optimize")) {
            opts.optimize=true;
            args.remove("-optimize");
        }
        if (args.contains("-module")) {
            opts.modulify=true;
            args.remove("-module");
        }
        if (args.contains("-noindent") || args.contains("-compact")) {
            opts.indent=false;
            args.remove("-noindent");
        }
        if (args.contains("-nocomments") || args.contains("-compact")) {
            opts.comment=false;
            args.remove("-nocomments");
            args.remove("-compact");
        }
        if (args.contains("-verbose")) {
            opts.verbose=true;
            args.remove("-verbose");
        }
        if (args.contains("-profile")) {
            opts.profile=true;
            args.remove("-profile");
        }
        if (args.contains("--")) {
            opts.stdin=true;
            args.remove("--");
        }
        //Review arg options
        for (Iterator<String> iter = args.iterator(); iter.hasNext();) {
            String s = iter.next();
            if (s.startsWith("-")) {
                iter.remove();
                //Get the option's value
                if (iter.hasNext()) {
                    String v = iter.next();
                    iter.remove();
                    if ("-rep".equals(s)) {
                        opts.addRepo(v);
                    } else if ("-user".equals(s)) {
                        opts.user=v;
                    } else if ("-pass".equals(s)) {
                        opts.pass=v;
                    } else if ("-src".equals(s)) {
                        opts.srcDir=v;
                    } else if ("-out".equals(s)) {
                        opts.outDir=v;
                    } else {
                        System.err.printf("Unrecognized option %s %s%n", s, v);
                    }
                }
            }
        }
        if (opts.repos.isEmpty()) {
            opts.repos.add("modules");
        }
        return opts;
    }

    public List<String> getRepos() {
        return repos;
    }
    public void addRepo(String repo) {
        repos.add(repo);
    }

    public String getUser() {
        return user;
    }
    public String getPass() {
        return pass;
    }
    public String getSrcDir() {
        return srcDir;
    }
    public String getOutDir() {
        return outDir;
    }
    public boolean isOptimize() {
        return optimize;
    }
    public boolean isModulify() {
        return modulify;
    }
    public boolean isIndent() {
        return indent;
    }
    public boolean isComment() {
        return comment;
    }
    public boolean isVerbose() {
        return verbose;
    }
    public boolean isProfile() {
        return profile;
    }
    public boolean isVersion() {
        return version;
    }
    public boolean isStdin() {
        return stdin;
    }
    public boolean isHelp() {
        return help;
    }

}
