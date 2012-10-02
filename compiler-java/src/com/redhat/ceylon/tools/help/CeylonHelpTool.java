package com.redhat.ceylon.tools.help;


import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tool;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tool.Tools;
import com.redhat.ceylon.common.tool.WordWrap;
import com.redhat.ceylon.tools.CeylonTool;
import com.redhat.ceylon.tools.help.Output.Synopsis;

/**
 * A plugin which provides help about other plugins 
 * @author tom
 */
@Summary("Display help information about other ceylon tools")
@Description(
"If a `<tool>` is given, displays help about that ceylon tool on the standard output.\n\n" +
"If no `<tool>` is given, displays the synopsis of the top level `ceylon` command. "
)
@RemainingSections(
"## SEE ALSO\n\n" +
"* `ceylon doc-tool` for generating documentation about ceylon tools\n"
)
public class CeylonHelpTool extends AbstractDoc implements Tool {

    private ToolModel<?> tool;
    private Appendable out = System.out;
    
    @Option
    public void setIncludeHidden(boolean includeHidden) {
        this.includeHidden = includeHidden;
    }
    
    @Argument(argumentName="tool", multiplicity="?")
    public void setTool(ToolModel<?> tool) {
        this.tool = tool;
    }
    
    public void setOut(Appendable out) {
        this.out = out;
    }
    
    @Override
    public void run() {
        final WordWrap wrap = new WordWrap(out);
        Output plain = new PlainOutput(wrap);
        if (tool == null) {
            printTopLevelHelp(plain, wrap, toolLoader.getToolNames());
        } else {
            final ToolModel<?> model = tool;
            if (model != null) {
                printToolHelp(plain, new ToolDocumentation<>(model));
            }/* else {
                Tools.printToolSuggestions(toolLoader, wrap, tool);
            }*/
        }
        wrap.flush();
    }
    
    protected final void printTopLevelHelp(Output out, WordWrap wrap, Iterable<String> toolNames) {
        final ToolModel<CeylonTool> root = toolLoader.loadToolModel("");
        final ToolDocumentation<CeylonTool> docModel = new ToolDocumentation<CeylonTool>(root);
        printToolSummary(out, docModel);
        
        Synopsis synopsis = out.startSynopsis(bundle.getString("section.SYNOPSIS"));
        synopsis.appendSynopsis(Tools.progName() + " ");
        synopsis.longOptionSynopsis("--version");
        synopsis.nextSynopsis();
        synopsis.appendSynopsis(Tools.progName() + " ");
        synopsis.argumentSynopsis("[<cey-options>]");
        synopsis.appendSynopsis(" ");
        synopsis.argumentSynopsis("<command>");
        synopsis.appendSynopsis(" ");
        synopsis.argumentSynopsis("[<command-options>]");
        synopsis.appendSynopsis(" ");
        synopsis.argumentSynopsis("[<command-args>]");
        synopsis.endSynopsis();
        
        printToolDescription(out, docModel);
        
        wrap.setIndent(8);
        int max = 0;
        for (String toolName : toolNames) {
            max = Math.max(max, toolName.length());
        }
        wrap.addTabStop(max + 12);
        wrap.setIndentRestLines(max + 12);
        for (String toolName : toolNames) {
            final ToolModel<Tool> model = toolLoader.loadToolModel(toolName);
            if (model == null) {
                throw new RuntimeException(toolName);
            }
            if (!model.isPorcelain()) {
                continue;
            }
            final Class<Tool> toolClass = model.getToolClass();
            final Summary summary = toolClass.getAnnotation(Summary.class);
            wrap.append(toolName);
            if (summary != null) {
                wrap.tab().append(summary.value());
            }
            wrap.newline();
        }
        wrap.removeTabStop(max + 12);
        wrap.setIndent(8);
        wrap.newline();
        wrap.append("See '" + Tools.progName() + " help <command>' for more information on a particular command");
        wrap.setIndent(0);
        wrap.newline();
    }

    

}
