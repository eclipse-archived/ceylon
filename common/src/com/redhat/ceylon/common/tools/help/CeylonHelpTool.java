/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.common.tools.help;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.Arrays;
import java.util.HashSet;

import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.OSUtil;
import com.redhat.ceylon.common.config.CeylonConfig;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Hidden;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tool;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tool.ToolUsageError;
import com.redhat.ceylon.common.tool.WordWrap;
import com.redhat.ceylon.common.tools.CeylonTool;
import com.redhat.ceylon.common.tools.help.model.Doc;
import com.redhat.ceylon.common.tools.help.model.Visitor;

/**
 * A plugin which provides help about other plugins 
 * @author tom
 */
@Summary("Displays help information about other Ceylon tools")
@Description(
"If a `<tool>` is given, displays help about that ceylon tool on the standard output.\n\n" +
"If no `<tool>` is given, displays the synopsis of the top level `ceylon` command. "
)
@RemainingSections(
"## SEE ALSO\n\n" +
"* `ceylon doc-tool` for generating documentation about ceylon tools\n"
)
public class CeylonHelpTool implements Tool {

    private Appendable out;
    private boolean includeHidden;
    private ToolLoader toolLoader;
    private DocBuilder docBuilder;
    private ToolModel<?> tool;
    private boolean synopsis = false;
    private String options = null;
    private boolean wantsPager;
    
    public final void setToolLoader(ToolLoader toolLoader) {
        this.toolLoader = toolLoader;
        this.docBuilder = new DocBuilder(toolLoader);
    }
    
    @Option
    public void setIncludeHidden(boolean includeHidden) {
        this.includeHidden = includeHidden;
    }
    
    @Hidden
    @Option
    @Description("Used to generate a synopsis when another tool was invoked incorrectly")
    public void setSynopsis(boolean synopsis) {
        this.synopsis = synopsis;
    }
    
    @Hidden
    @Option
    @OptionArgument
    @Description("Used to generate doc on a given option (or options)")
    public void setOptions(String options) {
        this.options = options;
    }
    
    @Argument(argumentName="tool", multiplicity="?")
    public void setTool(ToolModel<?> tool) {
        this.tool = tool;
    }
    
    public void setOut(Appendable out) {
        this.out = out;
    }

    @Override
    public void initialize(CeylonTool mainTool) {
        Boolean wantsPager = mainTool.getWantsPager();
        // tool-specific option
        if(wantsPager == null){
            String option = CeylonConfig.get().getOption("help.pager");
            if(option != null){
                if(CeylonConfig.isFalsish(option))
                    wantsPager = Boolean.FALSE;
                else // any other value is either true or a pager command
                    wantsPager = Boolean.TRUE;
            }
        }
        // general option
        if(wantsPager == null){
            String option = CeylonConfig.get().getOption("defaults.pager");
            if(option != null){
                if(CeylonConfig.isFalsish(option))
                    wantsPager = Boolean.FALSE;
                else // any other value is either true or a pager command
                    wantsPager = Boolean.TRUE;
            }
        }
        // allow pager if it's unspecified (default) or if it's not explicitly disabled
        if((wantsPager == null 
                || wantsPager.booleanValue())
                // and if stdout is on a tty
                // on this we differ with git which only tests isatty(stdout) and System.console() will return null
                // if stdout is on a tty but stdin on a pipe, but I've to say that I can't understand what a pager
                // is good for if stdin is not on a tty...
                && System.console() != null){
            this.wantsPager = true;
        }
    }
    
    @Override
    public void run() {
        if(wantsPager && OSUtil.isWindows() && tool != null){
            if (openHelpInBrowser()){
                return;
            }
        }
        
        docBuilder.setIncludeHidden(includeHidden);
        Doc doc;
        if (tool != null) {
            doc = docBuilder.buildDoc(tool);
        } else {
            final ToolModel<CeylonTool> root = toolLoader.loadToolModel("");
            doc = docBuilder.buildDoc(root, true);
        }
        Process pagerProcess = null;
        if(wantsPager && !OSUtil.isWindows()){
            pagerProcess = startPager();
        }
        OutputStream pagerPipe = null;
        if(pagerProcess != null)
            pagerPipe = pagerProcess.getOutputStream();
        try{
            final WordWrap wrap = getWrap(pagerPipe);
            Visitor plain = new PlainVisitor(wrap);
            if (synopsis) {
                plain = new SynopsisOnlyVisitor(plain);
            } else if (options != null) {
                plain = new OptionsOnlyVisitor(plain, 
                        new HashSet<String>(Arrays.asList(options.trim().split("\\s*,\\s*"))));
            }
            doc.accept(plain);
            wrap.flush();
        }finally{
            // shutdown pager
            if(pagerPipe != null){
                try {
                    pagerPipe.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // wait for pager to be done, there's no point doing anything else meanwhile
                try {
                    int errorCode = pagerProcess.waitFor();
                    if(errorCode != 0){
                        throw new ToolUsageError("Pager process returned an error exit code: "+errorCode+". Try fixing your $CEYLON_PAGER or $PAGER environment variable or invoke with the --no-pager command-line option.");
                    }
                } catch (InterruptedException e) {
                    throw new ToolUsageError("Pager process interrupted. Try fixing your $CEYLON_PAGER or $PAGER environment variable or invoke with the --no-pager command-line option.");
                }
            }
        }
    }

    /** this is only for windows **/
    private boolean openHelpInBrowser() {
        String ceylonHome = System.getProperty(Constants.PROP_CEYLON_HOME_DIR);
        if(ceylonHome == null || ceylonHome.isEmpty())
            return false;
        String toolDoc = "ceylon";
        if(tool.getName() != null && !tool.getName().isEmpty())
            toolDoc += "-"+tool.getName();
        toolDoc += ".html";
        File docHome = new File(ceylonHome, "doc/en/toolset/");
        File toolDocFile = new File(docHome, toolDoc);
        if(toolDocFile.exists() && toolDocFile.isFile()){
            try {
                Desktop.getDesktop().browse(toolDocFile.toURI());
            } catch (Exception e) {
                throw new ToolUsageError("Could not open browser for uri '"+toolDocFile.toURI()+"'. Please invoke with the --no-pager command-line option.");
            }
            return true;
        }else
            return false;
    }

    private Process startPager() {
        // find out the pager process, same process as in git
        // first try ceylon-specific pager env var
        String pager = System.getenv("CEYLON_PAGER");
        if(pager == null){
            // first try tool-specific option
            String option = CeylonConfig.get().getOption("help.pager");
            // if it's neither true nor false it's a pager program
            if(option != null 
                    && !CeylonConfig.isTrueish(option)
                    && !CeylonConfig.isFalsish(option))
                pager = option;
        }
        if(pager == null){
            // then try general option
            String option = CeylonConfig.get().getOption("defaults.pager");
            // if it's neither true nor false it's a pager program
            if(option != null 
                    && !CeylonConfig.isTrueish(option)
                    && !CeylonConfig.isFalsish(option))
                pager = option;
        }
        // then try general pager env var
        if(pager == null)
            pager = System.getenv("PAGER");
        // default to less
        if(pager == null)
            pager = "less";
        // make sure we don't page for cat
        if(!pager.equals("cat")){
            // start the pager process
            ProcessBuilder pb = new ProcessBuilder(pager);
            pb.redirectError(Redirect.INHERIT);
            pb.redirectOutput(Redirect.INHERIT);
            try {
                return pb.start();
            } catch (IOException e) {
                throw new ToolUsageError("Could not start pager process '"+pager+"'. Try fixing your $CEYLON_PAGER or $PAGER environment variable or invoke with the --no-pager command-line option.", e);
            }
        }
        return null;
    }

    private WordWrap getWrap(OutputStream pagerPipe) {
        Appendable destination = null;
        if(pagerPipe != null)
            destination = new PrintStream(pagerPipe);
        if(destination == null)
            destination = out;
        if(destination == null)
            destination = synopsis || options != null ? System.err : System.out;
        return new WordWrap(destination);
    }
}
