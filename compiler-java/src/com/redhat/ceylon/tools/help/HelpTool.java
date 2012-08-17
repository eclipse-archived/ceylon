package com.redhat.ceylon.tools.help;


import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Plugin;
import com.redhat.ceylon.common.tool.PluginModel;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tools;
import com.redhat.ceylon.common.tool.WordWrap;

/**
 * A plugin which provides help about other plugins 
 * @author tom
 */
@Summary("Display help information about other ceylon tools")
@Description(
"If a `<command>` is given, displays help about that ceylon tool on the standard output.\n\n" +
"If no `<command>` is given, displays the synopsis of the top level `ceylon` command. "
)
@RemainingSections(
"## SEE ALSO\n\n" +
"* `ceylon doc-tool` for generating documentation about ceylon tools\n"
)
public class HelpTool extends AbstractDoc implements Plugin {

    private String tool;

    @Argument(argumentName="tool", multiplicity="?")
    @Description("The tool to get help about")
    public void setTool(String tool) {
        this.tool = tool;
    }
    
    @Override
    public void run() {
        final WordWrap wrap = new WordWrap();
        Output plain = new PlainOutput(wrap);
        if (tool == null) {
            printTopLevelHelp(plain, wrap, toolLoader.getToolNames());
        } else {
            final PluginModel<?> model = toolLoader.loadToolModel(tool);
            if (model != null) {
                printToolHelp(plain, new ToolDocumentation<>(model));
            } else {
                Tools.printToolSuggestions(toolLoader, wrap, tool);
            }
        }
        wrap.flush();
    }
    

}
