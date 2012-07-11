package com.redhat.ceylon.ceylondoc;

import java.io.File;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.tools.Plugin;
import com.redhat.ceylon.tools.annotation.OptionArgument;
import com.redhat.ceylon.tools.annotation.Argument;
import com.redhat.ceylon.tools.annotation.Description;
import com.redhat.ceylon.tools.annotation.Option;
import com.redhat.ceylon.tools.annotation.Section;
import com.redhat.ceylon.tools.annotation.Sections;
import com.redhat.ceylon.tools.annotation.Summary;

@Summary("Generates Ceylon API documentation from Ceylon source files")
@Description("The default module repositories are `modules` and " +
		"http://modules.ceylon-lang.org, and the default source directory is `source`. " +
		"The default output module repository is `modules`." +
		"\n\n"+
		"The documentation compiler searches for compilation units belonging " +
		"to the specified modules in the specified source directories and in " +
		"source archives in the specified module repositories. For each " +
		"specified module, the compiler generates a set of XHTML pages in the " +
		"module documentation directory (the module-doc directory) of the " +
		"specified output module repository." +
		"\n\n" +
		"The compiler searches for source in the following locations:" +
		"\n\n" +
		"* source archives in the specified repositories, and\n" +
		"* module directories in the specified source directories." +
		"\n\n" +
        "If no version identifier is specified for a module, the module is " +
        "assumed to exist in a source directory.")
@Sections({
@Section(name="EXAMPLE",
text="The following would compile the org.hibernate module source code found in " +
	 "the ~/projects/hibernate/src directory to the " +
	 "repository ~/projects/hibernate/build:\n" +
	 "\n" +
	 "    ceylond org.hibernate/3.0.0.beta \\n"+
     "        -src ~/projects/hibernate/src \\n"+
     "        -out ~/projects/hibernate/build")
})
public class DocTool implements Plugin {

    private List<File> source = Collections.singletonList(new File("source"));
    private File out = new File("modules");
    private List<URI> repo;
    private List<String> module;
    private boolean d;
    private boolean sourceCode;
    private boolean nonShared;
    private String user;
    private String pass;
    
    public DocTool() {
    }

    @Option
    @Description("Includes source code in the generated documentation.")
    public void setSourceCode(boolean sourceCode) {
        this.sourceCode = sourceCode;
    }
    
    @Option
    @Description("Includes documentation for package-private declarations.")
    public void setNonShared(boolean nonShared) {
        this.nonShared = nonShared;
    }
    
    @OptionArgument(longName="src", argumentName="dir")
    @Description("A directory containing Ceylon and/or Java source code")
    public void setSource(List<File> source) {
        this.source = source;
    }

    @OptionArgument(longName="rep", argumentName="dir-or-url")
    @Description("The URL of a module repository containing dependencies")
    public void setRepo(List<URI> repo) {
        this.repo = repo;
    }

    @Option(longName="d")
    @Description("Disables the default module repositories and source directory.")
    public void setD(boolean d) {
        this.d = d;
    }
    
    @OptionArgument(argumentName="dir-or-url")
    @Description("The URL of the module repository where output should be published")
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
    @Description("The modules (with optional version) to compile the documentation of")
    public void setModule(List<String> module) {
        this.module = module;
    }

    @Override
    public int run() {
        // TODO Auto-generated method stub
        return 0;
    }

}
