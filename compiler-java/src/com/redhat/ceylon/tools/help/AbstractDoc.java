package com.redhat.ceylon.tools.help;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;

import com.redhat.ceylon.common.tool.ArgumentModel;
import com.redhat.ceylon.common.tool.OptionModel;
import com.redhat.ceylon.common.tool.Plugin;
import com.redhat.ceylon.common.tool.PluginLoader;
import com.redhat.ceylon.common.tool.PluginModel;
import com.redhat.ceylon.common.tool.Section;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tool;
import com.redhat.ceylon.common.tool.Tools;
import com.redhat.ceylon.common.tool.WordWrap;
import com.redhat.ceylon.tools.help.Output.Options;
import com.redhat.ceylon.tools.help.Output.Synopsis;

public class AbstractDoc {

    protected ResourceBundle bundle = ResourceBundle.getBundle("com.redhat.ceylon.tools.help.sections");
    protected PluginLoader toolLoader;
    
    public AbstractDoc() {
        super();
    }
    
    public final void setToolLoader(PluginLoader toolLoader) {
        this.toolLoader = toolLoader;
    }
    
    protected final void printTopLevelHelp(Output out, WordWrap wrap, Iterable<String> toolNames) {
        final PluginModel<Tool> root = toolLoader.loadToolModel("");
        final ToolDocumentation<Tool> docModel = new ToolDocumentation<Tool>(root);
        printToolSummary(out, docModel);
        
        /*
        out.print("SYNOPSIS").println();
        out.setIndent(8);
        out.print(Tools.progName() + " --version").println();
        out.print(Tools.progName() + " <command> [<args>]").println();
        out.setIndent(0);
        out.println();
        out.println();
        */
        // TODO Nasty hack here: Should be able to describe the top level tool's synopsis
        // through the Synopsis interface
        Synopsis synopsis = out.synopsis("SYNOPSIS");
        wrap.setIndent(8);
        wrap.append(Tools.progName() + " --version").newline();
        wrap.append(Tools.progName() + " <command> [<args>]").newline();
        wrap.setIndent(0);
        wrap.newline();
        wrap.newline();
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
            final PluginModel<Plugin> model = toolLoader.loadToolModel(toolName);
            if (model == null) {
                throw new RuntimeException(toolName);
            }
            final Class<Plugin> toolClass = model.getToolClass();
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

    protected void printToolHelp(Output out, ToolDocumentation<?> model) {
        printToolSummary(out, model);
        printToolSynopsis(out, model);
        printToolDescription(out, model);
        printToolOptions(out, model);
        printToolSections(out, model);
        out.end();
    }

    private void printToolSections(Output out, ToolDocumentation<?> model) {
        for (Section section : model.getSections()) {
            com.redhat.ceylon.tools.help.Output.Section s = out.section(model.getSectionName(section));
            s.paragraph(model.getSectionText(section));
            s.endSection();
        }
    
    }

    private void printToolOptions(Output out, ToolDocumentation<?> model) {
        Options options = out.options(bundle.getString("section.OPTIONS"));
        for (OptionModel opt : sortedOptions(model.getPlugin().getOptions())) {
            String shortName = opt.getShortName() != null ? "-" + opt.getShortName() : null;
            String longName = opt.getLongName() != null ? "--" + opt.getLongName() : null;
            String argumentName = opt.isPureOption() ? null : opt.getArgument().getName();
            String descriptionMd = model.getOptionDescription(opt);
            options.option(shortName, longName, argumentName, descriptionMd);
        }
        options.endOptions();
    }

    protected void printToolDescription(Output out, ToolDocumentation<?> model) {
        final String description = model.getDescription();
        if (!description.isEmpty()) {
            Output.Section s = out.section(bundle.getString("section.DESCRIPTION"));
            s.paragraph(description);
            s.endSection();
        }
    }

    private void printToolSynopsis(Output out, ToolDocumentation<?> model) {
        Synopsis synopsis = out.synopsis(bundle.getString("section.SYNOPSIS"));
        // TODO Make auto generated SYNOPSIS better -- we need to know which options
        // form groups, or should we just have a @Synopses({@Synopsis(""), ...})
        synopsis.appendSynopsis(model.getCeylonInvocation() + " ");
        ArrayList<OptionModel> options = sortedOptions(model.getPlugin().getOptions());
        if (!options.isEmpty()) {
            for (OptionModel option : options) {
                final ArgumentModel argument = option.getArgument();
                if (!argument.getMultiplicity().isRequired()) {
                    synopsis.appendSynopsis("[");
                }
                if (option.getLongName() != null) {
                    synopsis.longOptionSynopsis("--" + option.getLongName());
                    if (!option.isPureOption()) {
                        synopsis.appendSynopsis("=");
                        synopsis.appendSynopsis(multiplicity(argument, argument.getName()));
                    }
                } else {
                    synopsis.shortOptionSynopsis("-" + option.getShortName());
                    if (!option.isPureOption()) {
                        synopsis.appendSynopsis(" ");
                        synopsis.appendSynopsis(multiplicity(argument, argument.getName()));
                    }
                }
                if (!argument.getMultiplicity().isRequired()) {
                    synopsis.appendSynopsis("]");
                }
            }
        }
        if (!model.getPlugin().getArguments().isEmpty()) {
            synopsis.appendSynopsis(" [--] ");
            for (ArgumentModel argument : model.getPlugin().getArguments()) {
                String name = argument.getName();
                if (!argument.getMultiplicity().isRequired()) {
                    synopsis.appendSynopsis("[");
                }
                synopsis.appendSynopsis("<");
                name = multiplicity(argument, name);
                synopsis.argumentSynopsis(name);
                if (argument.getMultiplicity().isMultivalued()) {
                    synopsis.appendSynopsis("...");
                }
                synopsis.appendSynopsis(">");
                //sb.append(name);
                if (!argument.getMultiplicity().isRequired()) {
                    synopsis.appendSynopsis("]");
                }
                synopsis.appendSynopsis(" ");
            }
        }
        synopsis.endSynopsis();
    }

    private ArrayList<OptionModel> sortedOptions(final Collection<OptionModel> options2) {
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

    protected void printToolSummary(Output out, ToolDocumentation<?> model) {
        // XXX The whole dash between ceylon and the tool name is a bit of a 
        // man(1)-ism which we probably don't want to follow
        out.title(model.getCeylonInvocation());
        Output.Section s = out.section(bundle.getString("section.NAME"));
        s.paragraph(model.getCeylonInvocation() + " - " + model.getSummary());
        s.endSection();
        
    }

}