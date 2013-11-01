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
package com.redhat.ceylon.tools.help;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.tautua.markdownpapers.ast.Document;

import com.redhat.ceylon.common.OSUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.tool.ArgumentModel;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Hidden;
import com.redhat.ceylon.common.tool.Multiplicity;
import com.redhat.ceylon.common.tool.OptionModel;
import com.redhat.ceylon.common.tool.OptionModel.ArgumentType;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.SubtoolModel;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tool.Tools;
import com.redhat.ceylon.tools.CeylonTool;
import com.redhat.ceylon.tools.help.Markdown.Section;
import com.redhat.ceylon.tools.help.model.DescribedSection;
import com.redhat.ceylon.tools.help.model.DescribedSection.Role;
import com.redhat.ceylon.tools.help.model.Doc;
import com.redhat.ceylon.tools.help.model.Option;
import com.redhat.ceylon.tools.help.model.OptionsSection;
import com.redhat.ceylon.tools.help.model.SubtoolVisitor;
import com.redhat.ceylon.tools.help.model.SummarySection;
import com.redhat.ceylon.tools.help.model.SynopsesSection;
import com.redhat.ceylon.tools.help.model.Synopsis;

public class DocBuilder {

    protected ToolLoader toolLoader;
    protected boolean includeHidden = false;

    public DocBuilder(ToolLoader toolLoader) {
        super();
        this.toolLoader = toolLoader;
    }
    
    public boolean isIncludeHidden() {
        return includeHidden;
    }

    public void setIncludeHidden(boolean includeHidden) {
        this.includeHidden = includeHidden;
    }

    public Doc buildDoc(ToolModel<?> model, boolean specialRoot) {
        checkModel(model);
        boolean rootHack = specialRoot && CeylonTool.class.isAssignableFrom(model.getToolClass());
        Doc doc = new Doc();
        doc.setVersion(Versions.CEYLON_VERSION);
        doc.setToolModel(model);
        doc.setInvocation(getCeylonInvocation(model));
        doc.setSummary(buildSummary(model));
        doc.setSynopses(rootHack ? buildRootSynopsis(model) : buildSynopsis(model));
        doc.setDescription(rootHack ? buildRootDescription(model) : buildDescription(model));
        doc.setOptions(buildOptions(model));
        if (model.getSubtoolModel() != null) {
            //doc.setSubcommands(buildSubcommands(model));
        }
        doc.setAdditionalSections(buildAdditionalSections(model));
        return doc;
    }
    
    private void checkModel(ToolModel<?> model) {
        new SubtoolVisitor(model) {            
            @Override
            protected void visit(ToolModel<?> model, SubtoolModel<?> subtoolModel) {
                if (model != root) {
                    if (getSummary(model) != null) {
                        System.err.println("@Summary not supported on subtools: " + model.getToolClass());
                    }
                    if (!getSections(model).isEmpty()) {
                        System.err.println("@RemainingSections not supported on subtools: " + model.getToolClass());
                    }
                }
            }
        }.accept();
    }

    private SynopsesSection buildRootSynopsis(ToolModel<?> model) {

        SynopsesSection synopsis = new SynopsesSection();
        synopsis.setTitle(HelpMessages.msg("section.SYNOPSIS"));
        List<Synopsis> synopsisList = new ArrayList<>();
        {
            Synopsis s1 = new Synopsis();
            s1.setInvocation(Tools.progName());
            OptionModel<Boolean> option = new OptionModel();
            option.setLongName("version");
            option.setArgumentType(ArgumentType.NOT_ALLOWED);
            ArgumentModel<Boolean> argument = new ArgumentModel<>();
            argument.setMultiplicity(Multiplicity._1);
            argument.setType(Boolean.TYPE);
            option.setArgument(argument);
            s1.setOptionsAndArguments(Collections.singletonList(option));//model.getOption("version")));
            synopsisList.add(s1);
        }
        {
            Synopsis s2 = new Synopsis();
            s2.setInvocation(Tools.progName());
            
            ArrayList args = new ArrayList(model.getOptions());
            args.remove(model.getOption("version"));
            /*ArgumentModel<?> options = new ArgumentModel();
            options.setMultiplicity(Multiplicity._0_OR_MORE);
            options.setName("cey\u2011options");
            args.add(options);*/
            
            ArgumentModel<?> command = new ArgumentModel();
            command.setMultiplicity(Multiplicity._1);
            command.setName("command");
            args.add(command);
            
            ArgumentModel<?> commandOptions = new ArgumentModel();
            commandOptions.setMultiplicity(Multiplicity._0_OR_MORE);
            commandOptions.setName("command\u2011options");
            args.add(commandOptions);
            
            ArgumentModel<?> commandArgs = new ArgumentModel();
            commandArgs.setMultiplicity(Multiplicity._0_OR_MORE);
            commandArgs.setName("command\u2011args");
            args.add(commandArgs);
            
            s2.setOptionsAndArguments(args);
            synopsisList.add(s2);
        }
        synopsis.setSynopses(synopsisList);
        
        return synopsis;
    }
    
    private DescribedSection buildRootDescription(
            ToolModel<?> rootModel) {
        
        StringBuilder sb = new StringBuilder();
        final String newline = "\n";
        sb.append(newline);
        sb.append(newline);
        for (String toolName : toolLoader.getToolNames()) {
            final ToolModel<?> model = toolLoader.loadToolModel(toolName);
            if (model == null) {
                throw new RuntimeException(toolName);
             }
            if (!model.isPorcelain() && !includeHidden) {
                continue;
            }
            sb.append("* `").append(toolName).append("` — ");
            String summary = getSummaryValue(model);
            if (summary != null) {
                sb.append(summary);
            }
            sb.append(newline);
            sb.append(newline);
        }
        sb.append(newline);
        sb.append(HelpMessages.getMoreInfo());
        sb.append(newline);
        sb.append(newline);
        
        String both = getDescription(rootModel) + sb.toString();
        
        DescribedSection description = buildDescription(rootModel, both);
        return description;
    }

    public Doc buildDoc(ToolModel<?> model) {
        return buildDoc(model, false);
    }

    private List<DescribedSection> buildAdditionalSections(ToolModel<?> model) {
        List<DescribedSection> additionalSections = new ArrayList<DescribedSection>();
        String sections = getSections(model);
        if (sections != null && !sections.isEmpty()) {
            Document doc = Markdown.markdown(sections);
            List<Section> markdownSections = Markdown.extractSections(doc);
            for (Markdown.Section sect : markdownSections) {
                DescribedSection ds = new DescribedSection();
                ds.setRole(Role.ADDITIONAL);
                Document sectionDoc = sect.getDoc();
                if (sect.getHeading() == null) {
                    // TODO Warn that there were no section headings
                    continue;
                } else {
                    // Adjust the heading levels, so that the most prominent 
                    // heading is H2
                    Markdown.adjustHeadings(sectionDoc, 2-sect.getHeading().getLevel());
                }
                ds.setTitle(sect.getHeading());
                ds.setDescription(sectionDoc);
                additionalSections.add(ds);
            }
        }
        return additionalSections;
    }

    private OptionsSection buildOptions(ToolModel<?> model) {
        if(model.isScript())
            return null;
        final HashMap<ToolModel<?>, OptionsSection> map = new HashMap<>();
        new SubtoolVisitor(model) {
            @Override
            protected void visit(ToolModel<?> model,
                    SubtoolModel<?> subtoolModel) {
                OptionsSection optionsSection = new OptionsSection();
                map.put(model, optionsSection);
                if (model==root) {
                    optionsSection.setTitle(
                            Markdown.markdown("##" + HelpMessages.msg("section.OPTIONS")));
                } else {
                    optionsSection.setTitle(
                            Markdown.markdown("###" + HelpMessages.msg("section.OPTIONS.sub", model.getName())));
                }
                List<Option> options = new ArrayList<>();
                for (OptionModel<?> opt : sortedOptions(model.getOptions())) {
                    Option option = new Option();
                    option.setOption(opt);
                    String descriptionMd = getOptionDescription(model, opt);
                    if (descriptionMd == null || descriptionMd.isEmpty()) {
                        descriptionMd = HelpMessages.msg("option.undocumented");
                    }
                    option.setDescription(Markdown.markdown(descriptionMd));
                    options.add(option);
                }
                optionsSection.setOptions(options);
                if (model != root && !options.isEmpty()) {
                    OptionsSection parent = map.get(ancestors.lastElement().getModel());
                    ArrayList<OptionsSection> parentSubsections = new ArrayList<OptionsSection>(parent.getSubsections());
                    parentSubsections.add(optionsSection);
                    parent.setSubsections(parentSubsections);
                }
            }
            
        }.accept();
        return map.get(model);
    }

    private DescribedSection buildDescription(ToolModel<?> model) {
        final HashMap<ToolModel<?>, DescribedSection> map = new HashMap<ToolModel<?>, DescribedSection>();
        new SubtoolVisitor(model) {            
            @Override
            protected void visit(ToolModel<?> model, SubtoolModel<?> subtoolModel) {
                if (model == root) {
                    map.put(model, buildDescription(model, getDescription(model)));
                } else if (model.getSubtoolModel() == null) {// leaf
                    
                    DescribedSection section = new DescribedSection();
                    section.setRole(Role.DESCRIPTION);
                    
                    StringBuilder sb = new StringBuilder();
                    for (SubtoolVisitor.ToolModelAndSubtoolModel subtool : ancestors.subList(1, ancestors.size())) {
                        sb.append(subtool.getModel().getName()).append(" ");
                    }
                    sb.append(model.getName());
                    section.setTitle(Markdown.markdown("###" + HelpMessages.msg("section.DESCRIPTION.sub", sb.toString())));
                    section.setDescription(Markdown.markdown(getDescription(model)));
                    section.setAbout(model);
                    
                    List<DescribedSection> rootSubsections = new ArrayList<>(map.get(root).getSubsections());
                    rootSubsections.add(section);
                    map.get(root).setSubsections(rootSubsections);
                }
            }
        }.accept();
        return map.get(model);
    }
    
    private DescribedSection buildDescription(ToolModel<?> model, String description) {
        DescribedSection section = null;
        if (!description.isEmpty()) {
            section = new DescribedSection();
            section.setRole(Role.DESCRIPTION);
            section.setTitle(
                    Markdown.markdown("## " + HelpMessages.msg("section.DESCRIPTION") + "\n"));
            section.setDescription(Markdown.markdown(description));
        }
        return section;
    }
    

    private DescribedSection buildSubcommands(ToolModel<?> model) {
        /*
        DescribedSection section = null;
        if (!description.isEmpty()) {
            SubtoolModel<?> subtool = model.getSubtoolModel();
            for (String toolName : subtool.getToolLoader().getToolNames()) {
                ToolModel<Tool> subtoolModel = subtool.getToolLoader().loadToolModel(toolName);
            }
            / *
             * Here I need to build up the markdown something like as follows
             * 
            The command `ceylon config` takes various subcommands
            
            ## SUBCOMMANDS
            
            ### `ceylon config foo`
            
            summary
            
            description
            
            options
            
            ### `ceylon config bar baz`
            
            summary
            
            description
            
            options
            
            * /
            section = new DescribedSection();
            section.setRole(Role.SUBCOMMANDS);
            section.setDescription(Markdown.markdown(
                    "##" + sectionsBundle.getString("section.SUBCOMMANDS") + "\n\n" +
                    description));
        }
        return section;*/
        return null;
    }

    private SynopsesSection buildSynopsis(ToolModel<?> model) {
        //Synopsis synopsis = out.startSynopsis(bundle.getString("section.SYNOPSIS"));
        // TODO Make auto generated SYNOPSIS better -- we need to know which options
        // form groups, or should we just have a @Synopses({@Synopsis(""), ...})
        SynopsesSection synopsesSection = new SynopsesSection();
        synopsesSection.setTitle(HelpMessages.msg("section.SYNOPSIS"));
        final List<Synopsis> synopsisList = new ArrayList<>();
        
        new SubtoolVisitor(model) {
            
            @Override
            protected void visit(ToolModel<?> model, 
                    SubtoolModel<?> subtoolModel) {
                if (model.getSubtoolModel() == null) {// a leaf
                    Synopsis synopsis = new Synopsis();
                    synopsis.setInvocation(getCeylonInvocationForSynopsis(root));
                    List<?> optionsAndArguments;
                    if (ancestors.isEmpty()) {
                        optionsAndArguments = optionsAndArguments(model);
                    } else {
                        optionsAndArguments = new ArrayList<>();
                        for (SubtoolVisitor.ToolModelAndSubtoolModel ancestor : ancestors) {
                            List subOptAndArgs = optionsAndArguments(ancestor.getModel());
                            if (ancestor.getModel() != root) {
                                // Don't treat the foo in `ceylon foo` as a subtool 
                                subOptAndArgs.add(0, ancestor);
                            }
                            optionsAndArguments.addAll((List)subOptAndArgs);
                        }
                        List subOptAndArgs = optionsAndArguments(model);
                        subOptAndArgs.add(0, new SubtoolVisitor.ToolModelAndSubtoolModel(model, subtoolModel));
                        optionsAndArguments.addAll((List)subOptAndArgs);
                    }
                    synopsis.setOptionsAndArguments(optionsAndArguments);
                    synopsisList.add(synopsis);
                }
                
            }
        }.accept();

        synopsesSection.setSynopses(synopsisList);
        return synopsesSection;
    }

    private <E> List<E> optionsAndArguments(ToolModel<?> model) {
        List<E> optionsAndArguments = (List)sortedOptions(model.getOptions());
        optionsAndArguments.addAll((List)model.getArguments());
        return optionsAndArguments;
    }

    private boolean skipHiddenOption(OptionModel<?> option) {
        return option.getArgument().getSetter().getAnnotation(Hidden.class) != null 
                && !includeHidden;
    }

    private ArrayList<OptionModel<?>> sortedOptions(final Collection<OptionModel<?>> options2) {
        ArrayList<OptionModel<?>> options = new ArrayList<OptionModel<?>>(options2);
        for (Iterator<OptionModel<?>> iter = options.iterator(); iter.hasNext(); ) {
            OptionModel<?> option = iter.next();
            if (skipHiddenOption(option)) {
                iter.remove();
            }
        }
        Collections.sort(options, new Comparator<OptionModel<?>>() {
            @Override
            public int compare(OptionModel<?> o1, OptionModel<?> o2) {
                return o1.getLongName().compareTo(o2.getLongName());
            }
        });
        return options;
    }

    private SummarySection buildSummary(ToolModel<?> model) {
        SummarySection summary = new SummarySection();
        summary.setTitle(
                Markdown.markdown("##" + HelpMessages.msg("section.NAME") + "\n"));
        summary.setSummary(getSummaryValue(model));
        return summary;
    }
    
    
    private String getName(ToolModel<?> model) {
        return model.getName();
    }
    
    private String msg(ResourceBundle toolBundle, String key) {
        if (toolBundle != null && toolBundle.containsKey(key)) {
            String msg = toolBundle.getString(key);
            if (msg != null) {
                // Pass through a message format so that translators don't have to guess 
                // which things need doubled '' and which not.
                return MessageFormat.format(msg, new Object[]{});
            }
        }
        return "";
    }
    
    private String getSummaryValue(ToolModel<?> model) {
        if(model.isScript()){
            return invokeScript(model, "--_print-summary");
        }
        ResourceBundle toolBundle = getToolBundle(model);
        String msg = msg(toolBundle, "summary");
        if (msg.isEmpty()) {
            Summary summary = getSummary(model);
            if (summary != null) {
                msg = summary.value();
            }
        }
        return msg;
    }

    private String invokeScript(ToolModel<?> model, String arg) {
        ProcessBuilder processBuilder;
        if (OSUtil.isWindows()) {
            processBuilder = new ProcessBuilder("cmd.exe", "/C", model.getScriptName(), arg);
        } else {
            processBuilder = new ProcessBuilder(model.getScriptName(), arg);
        }
        CeylonTool.setupScriptEnvironment(processBuilder);
        processBuilder.redirectError(Redirect.INHERIT);
        try {
            Process process = processBuilder.start();
            // no stdin to the tool
            process.getOutputStream().close();
            InputStream stream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer strbuf = new StringBuffer();
            String line;
            while((line = reader.readLine()) != null){
                strbuf.append(line+"\n");
            }
            reader.close();
            int exit = process.waitFor();
            if(exit != 0)
                return "";
            return strbuf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    private ResourceBundle getToolBundle(ToolModel<?> model) {
        if(model.isScript())
            return null;
        ResourceBundle toolBundle;
        try {
            toolBundle = ResourceBundle.getBundle(model.getToolClass().getName());
        } catch (MissingResourceException e) {
            toolBundle = null;
        }
        return toolBundle;
    }

    
    private Summary getSummary(ToolModel<?> model) {
        if(model.isScript())
            return null;
        return model.getToolClass().getAnnotation(Summary.class);
    }

    private String getDescription(ToolModel<?> model) {
        if(model.isScript()){
            return invokeScript(model, "--_print-description");
        }
        ResourceBundle toolBundle = getToolBundle(model);
        String msg = msg(toolBundle, "description");
        if (msg.isEmpty()) {
            Description description = model.getToolClass().getAnnotation(Description.class);
            if (description != null) {
                msg = description.value();
            }
        }
        return msg;
    }

    private String getSections(ToolModel<?> model) {
        if(model.isScript())
            return null;
        ResourceBundle toolBundle = getToolBundle(model);
        String msg = msg(toolBundle, "sections.remaining");
        if (msg.isEmpty()) {
            RemainingSections sections = model.getToolClass().getAnnotation(RemainingSections.class);
            if (sections != null) {
                msg = sections.value();
            }
        }
        return msg;
    }

    private String getOptionDescription(ToolModel<?> model, OptionModel<?> opt) {
        ResourceBundle toolBundle = getToolBundle(model);
        String msg = msg(toolBundle, "option."+opt.getLongName());
        if (msg.isEmpty()) {
            Description description = opt.getArgument().getSetter().getAnnotation(Description.class);
            if (description != null) {
                msg = description.value();
            }
        }
        return msg;
    }

    private String getCeylonInvocation(ToolModel<?> model) {
        return getName(model).isEmpty() ? Tools.progName(): Tools.progName() + " " + model.getName();
    }

    private String getCeylonInvocationForSynopsis(ToolModel<?> model) {
        String ret = getCeylonInvocation(model);
        if(model.isScript())
            return ret + " " + invokeScript(model, "--_print-usage");
        return ret;
    }

}