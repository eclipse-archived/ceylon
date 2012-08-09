package com.redhat.ceylon.compiler;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.ArgumentStyle;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.JavacStyle;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.Plugin;
import com.redhat.ceylon.common.tool.Rest;
import com.redhat.ceylon.common.tool.Section;
import com.redhat.ceylon.common.tool.Sections;
import com.redhat.ceylon.common.tool.Summary;

@Summary("Compiles Ceylon and Java source code and directly produces module " +
		"and source archives in a module repository.")
@Description("The default module repositories are `modules` and " +
		"http://modules.ceylon-lang.org, and the default source directory is `source`. " +
		"The default output module repository is `modules`." +
		"\n\n" +
		"The compiler searches for compilation units belonging to the specified " +
		"modules in the specified source directories. " +
		"For each specified module, the compiler generates a module archive, " +
		"source archive, and their checksum files in the specified output module " +
		"repository." +
		"\n\n"+
        "All program elements imported by a compilation unit must belong to the " +
        "same module as the compilation unit, or must belong to a module that " +
        "is explicitly imported in the module descriptor." +
        "\n\n" +
        "The compiler searches for dependencies in the following locations:" +
        "\n\n"+
        "* module archives in the specified repositories,"+
        "* source archives in the specified repositories, and"+
        "* module directories in the specified source directories.")
@Sections({
@Section(
    name="EXAMPLE",
    text="Blah blah"),
@Section(
    name="CONFIGURATION VARIABLES",
    text="Blah blah blah")
})
@ArgumentStyle(JavacStyle.class)
public class CompileTool implements Plugin{

    private List<File> source = Collections.singletonList(new File("source"));
    private File out = new File("modules");
    private List<URI> repo = Collections.emptyList();
    private List<String> module = Collections.emptyList();
    private boolean d;
    private List<String> rest = Collections.emptyList();
    private String user;
    private String pass;

    public CompileTool() {
    }
    
    @OptionArgument(longName="src", argumentName="dir")
    @Description("Specifies a source directory.")
    public void setSource(List<File> source) {
        this.source = source;
    }
    
    @OptionArgument(longName="repo", argumentName="url")
    @Description("Specifies a module repository containing dependencies.")
    public void setRepository(List<URI> repo) {
        this.repo = repo;
    }
    
    @Option(longName="d")
    @Description("Disables the default module repositories and source directory.")
    public void setDisableDefaultRepos(boolean d) {
        this.d = d;
    }
    
    @OptionArgument(argumentName="url")
    @Description("Specifies the output module repository (which must be publishable).")
    public void setOut(File out) {
        this.out = out;
    }
    
    @OptionArgument(argumentName="name")
    @Description("Sets the user name for use with an authenticated output repository.")
    public void setUser(String user) {
        this.user = user;
    }
    
    @OptionArgument(argumentName="secret")
    @Description("Sets the password for use with an authenticated output repository.")
    public void setPass(String pass) {
        this.pass = pass;
    }

    @Argument(argumentName="modules", multiplicity="+")
    @Description("A list of module names (without versions) or file paths " +
    		"specifying the source code to compile.")
    public void setModule(List<String> module) {
        this.module = module;
    }
    
    /** We collect any other arguments to pass to the underlying javac*/
    @Rest
    public void setRest(List<String> rest) {
        this.rest = rest;
    }

    @Override
    public int run() {
        List<String> arguments = new ArrayList<>();
        for (File source : this.source) {
            arguments.add("-src");
            arguments.add(source.getPath());
        }
        
        if (d) {
            arguments.add("-d");
        }
        
        arguments.add("-out");
        arguments.add(out.getPath());
        
        if (user != null) {
            arguments.add("-user");
            arguments.add(user);
        }
        if (pass != null) {
            arguments.add("-pass");
            arguments.add(pass);
        }
        
        for (URI uri : this.repo) {
            arguments.add("-rep");
            arguments.add(uri.toString());
        }
        
        arguments.addAll(rest);
        
        for (String moduleSpec : this.module) {
            arguments.add(moduleSpec);
        }
        
        try {
            System.out.println(arguments);
            com.redhat.ceylon.compiler.java.launcher.Main compiler = new com.redhat.ceylon.compiler.java.launcher.Main("ceylon compile");
            return compiler.compile(arguments.toArray(new String[arguments.size()]));
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }
    
    

}
