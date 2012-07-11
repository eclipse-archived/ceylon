package com.redhat.ceylon.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import com.redhat.ceylon.tools.annotation.Argument;
import com.redhat.ceylon.tools.annotation.Description;
import com.redhat.ceylon.tools.annotation.Section;
import com.redhat.ceylon.tools.annotation.Sections;
import com.redhat.ceylon.tools.annotation.Summary;

/**
 * A plugin which provides help about other plugins 
 * @author tom
 */
@Summary("Display help information about other ceylon tools")
@Description("With no <command>, displays the synopsis of the ceylon command. " +
		"If a <command> is given, displays help about that particular ceylon tool.")
public class HelpTool implements Plugin {

    private String command;
    private PluginLoader toolLoader;

    @Argument(argumentName="command", multiplicity="?")
    @Description("The command to get help about")
    public void setCommand(String command) {
        this.command = command;
    }
    
    public void setToolLoader(PluginLoader toolLoader) {
        this.toolLoader = toolLoader;
    }
    
    @Override
    public int run() {
        final WordWrap wrap = new WordWrap();
        if (command == null) {
            printTopLevelHelp(wrap, toolLoader.getToolNames());
        } else {
            final PluginModel<?> model = toolLoader.loadToolModel(command);
            if (model != null) {
                printToolHelp(wrap, model);
            } else {
                Tool.printToolSuggestions(toolLoader, wrap, command);
            }
        }
        wrap.flush();
        return 0;
    }

    private void printTopLevelHelp(WordWrap out, Iterable<String> toolNames) {
        final PluginModel<Tool> root = toolLoader.loadToolModel("");
        printToolSummary(out, root);
        
        out.print("SYNOPSIS").println();
        out.setIndent(8);
        out.print(Tool.progName() + " --version").println();
        out.print(Tool.progName() + " <command> [<args>]").println();
        out.setIndent(0);
        out.println();
        out.println();
        
        printToolDescription(out, root);
        
        out.setIndent(8);
        int max = 0;
        for (String toolName : toolNames) {
            max = Math.max(max, toolName.length());
        }
        out.addTabStop(max + 12);
        out.setIndentRestLines(max + 12);
        for (String toolName : toolNames) {
            final PluginModel<Plugin> model = toolLoader.loadToolModel(toolName);
            if (model == null) {
                throw new RuntimeException(toolName);
            }
            final Class<Plugin> toolClass = model.getToolClass();
            final Summary summary = toolClass.getAnnotation(Summary.class);
            out.print(toolName);
            if (summary != null) {
                out.tab().print(summary.value());
            }
            out.println();
        }
        out.removeTabStop(max + 12);
        out.setIndent(8);
        out.println();
        out.print("See '" + Tool.progName() + " help <command>' for more information on a particular command");
        out.setIndent(0);
        out.println();
    }

    private void printToolHelp(WordWrap out, PluginModel<?> model) {
        final String ceylonName = printToolSummary(out, model);
        printToolSynopsis(out, model);
        printToolDescription(out, model);
        printToolOptions(out, model, ceylonName);
        printToolSections(out, model);
    }

    private void printToolSections(WordWrap out, PluginModel<?> model) {
        Sections sections = model.getToolClass().getAnnotation(Sections.class);
        if (sections != null) {
            for (Section section : sections.value()) {
                out.print(section.name()).println();
                out.setIndent(8);
                out.print(section.text());
                out.setIndent(0);
                out.println();
                out.println();
            }
        }
    }

    private void printToolOptions(WordWrap out, PluginModel<?> model,
            final String ceylonName) {
        out.print("OPTIONS").println();
        out.setIndent(8);
        if (model.getOptions().isEmpty()) {
            out.print(ceylonName+" has no options").println();
        } else {
            for (OptionModel opt : sortedOptions(model.getOptions())) {
                if (opt.getShortName() != null) {
                    out.print("-").print(String.valueOf(opt.getShortName()));
                    if (!opt.getArgument().getMultiplicity().isNone()) {
                        out.print(" <" + opt.getArgument().getName() + ">");
                    }
                    out.print(", ");
                }
                out.print("--").print(opt.getLongName());
                if (!opt.getArgument().getMultiplicity().isNone()) {
                    out.print("=<" + opt.getArgument().getName() + ">");
                }
                out.setIndent(12);
                out.println();
                final Description description = opt.getArgument().getSetter().getAnnotation(Description.class);
                if (description != null) {
                    out.print(description.value());
                } else {
                    out.print("Not documented");
                }
                out.println();
                out.setIndent(8);
                out.println();
            }
        }
        out.setIndent(0);
        out.println();
    }

    private void printToolDescription(WordWrap out, PluginModel<?> model) {
        final Description description = model.getToolClass().getAnnotation(Description.class);
        if (description != null) {
            out.print("DESCRIPTION").println();
            out.setIndent(8);
            out.print(model.getToolClass().getAnnotation(Description.class).value());
            out.setIndent(0);
            out.println();
            out.println();
        }
    }

    private void printToolSynopsis(WordWrap out, PluginModel<?> model) {
        out.print("SYNOPSIS").println();
        out.setIndent(8);
        // TODO Make auto generated SYNOPSIS better -- we need to know which options
        // form groups, or should we just have a @Synopses({@Synopsis(""), ...})
        StringBuilder sb = new StringBuilder(Tool.progName()).append(' ').append(model.getName()).append(" ");
        ArrayList<OptionModel> options = sortedOptions(model.getOptions());
        if (!options.isEmpty()) {
            for (OptionModel option : options) {
                final ArgumentModel argument = option.getArgument();
                StringBuilder sb1 = new StringBuilder();
                if (option.getLongName() != null) {
                    sb1.append("--").append(option.getLongName());
                    if (!argument.getMultiplicity().isNone()) {
                        sb1.append("=").append(multiplicity(argument, argument.getName()));
                    }
                } else {
                    sb1.append("-").append(option.getShortName());
                    if (!argument.getMultiplicity().isNone()) {
                        sb1.append(" ").append(multiplicity(argument, argument.getName()));
                    }
                }
                sb.append(optionality(argument, sb1.toString())).append(' ');
            }
        }
        if (!model.getArguments().isEmpty()) {
            sb.append(" [--] ");
            for (ArgumentModel argument : model.getArguments()) {
                String name = argument.getName();
                name = optionality(argument, multiplicity(argument, name));
                sb.append(name).append(" ");
            }
        }
        out.print(sb.toString());
        out.setIndent(0);
        out.println();
        out.println();
    }

    private ArrayList<OptionModel> sortedOptions(
            final Collection<OptionModel> options2) {
        ArrayList<OptionModel> options = new ArrayList<OptionModel>(options2);
        Collections.sort(options, new Comparator<OptionModel>() {
            @Override
            public int compare(OptionModel o1, OptionModel o2) {
                return o1.getLongName().compareTo(o2.getLongName());
            }
        });
        return options;
    }

    private String multiplicity(ArgumentModel argument, String name) {
        name = "<" + name + ">";
        if (argument.getMultiplicity().isMultivalued()) {
            name += "...";
        }
        return name;
    }
    
    private String optionality(ArgumentModel argument, String name) {
        if (!argument.getMultiplicity().isRequired()) {
            name = "[" + name + "]";
        }
        return name;
    }

    private String printToolSummary(WordWrap out, PluginModel<?> model) {
        out.print("NAME").println();
        out.setIndent(8);
        // XXX The whole dash between ceylon and the tool name is a bit of a 
        // man(1)-ism which we probably don't want to follow
        final String ceylonName = model.getName().isEmpty() ? Tool.progName(): Tool.progName() + "-" + model.getName();
        out.print(ceylonName + " - " + model.getToolClass().getAnnotation(Summary.class).value());
        out.setIndent(0);
        out.println();
        out.println();
        return ceylonName;
    }

}
