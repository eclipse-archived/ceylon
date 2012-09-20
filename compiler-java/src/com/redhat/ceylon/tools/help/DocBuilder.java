package com.redhat.ceylon.tools.help;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.tautua.markdownpapers.ast.Document;

import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Hidden;
import com.redhat.ceylon.common.tool.OptionModel;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tool.Tools;
import com.redhat.ceylon.tools.help.Markdown.Section;
import com.redhat.ceylon.tools.help.model.DescribedSection;
import com.redhat.ceylon.tools.help.model.DescribedSection.Role;
import com.redhat.ceylon.tools.help.model.Doc;
import com.redhat.ceylon.tools.help.model.Option;
import com.redhat.ceylon.tools.help.model.OptionsSection;
import com.redhat.ceylon.tools.help.model.SynopsesSection;

public class DocBuilder {

    protected final ResourceBundle sectionsBundle = ResourceBundle.getBundle("com.redhat.ceylon.tools.help.resources.sections");
    protected ToolLoader toolLoader;
    protected boolean includeHidden = false;

    
    public DocBuilder(ToolLoader toolLoader) {
        super();
        this.toolLoader = toolLoader;
    }
    
    public Doc buildDoc(ToolModel<?> model) {
        Doc doc = new Doc();
        //TODO doc.setVersion(version);
        doc.setToolModel(model);
        doc.setInvocation(getCeylonInvocation(model));
        doc.setSummary(buildSummary(model));
        doc.setSynopses(buildSynopsis(model));
        doc.setDescription(buildDescription(model));
        doc.setOptions(buildOptions(model));
        doc.setAdditionalSections(buildAdditionalSections(model));
        return doc;
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
                ds.setDescription(sectionDoc);
                additionalSections.add(ds);
            }
        }
        return additionalSections;
    }

    private OptionsSection buildOptions(ToolModel<?> model) {
        OptionsSection optionsSection = new OptionsSection();
        optionsSection.setTitle(sectionsBundle.getString("section.OPTIONS"));
        List<Option> options = new ArrayList<>();
        for (OptionModel<?> opt : sortedOptions(model.getOptions())) {
            Option option = new Option();
            option.setOption(opt);
            String descriptionMd = getOptionDescription(model, opt);
            if (descriptionMd == null || descriptionMd.isEmpty()) {
                descriptionMd = sectionsBundle.getString("option.undocumented");
            }
            option.setDescription(Markdown.markdown(descriptionMd));
            options.add(option);
        }
        optionsSection.setOptions(options);
        return optionsSection;
    }

    protected DescribedSection buildDescription(ToolModel<?> model) {
        DescribedSection section = null;
        final String description = getDescription(model);
        if (!description.isEmpty()) {
            section = new DescribedSection();
            section.setRole(Role.DESCRIPTION);
            section.setDescription(Markdown.markdown(
                    "##" + sectionsBundle.getString("section.DESCRIPTION") + "\n\n" +
                    description));
            
        }
        return section;
    }

    private SynopsesSection buildSynopsis(ToolModel<?> model) {
        //Synopsis synopsis = out.startSynopsis(bundle.getString("section.SYNOPSIS"));
        // TODO Make auto generated SYNOPSIS better -- we need to know which options
        // form groups, or should we just have a @Synopses({@Synopsis(""), ...})
        SynopsesSection synopsesSection = new SynopsesSection();
        synopsesSection.setTitle(sectionsBundle.getString("section.SYNOPSIS"));
        List<com.redhat.ceylon.tools.help.model.Synopsis> sy = new ArrayList<>();
        com.redhat.ceylon.tools.help.model.Synopsis s = new com.redhat.ceylon.tools.help.model.Synopsis();
        s.setInvocation(getCeylonInvocation(model));
        s.setOptions(sortedOptions(model.getOptions()));
        s.setArguments(model.getArguments());
        synopsesSection.setSynopses(sy);
        return synopsesSection;
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

    protected DescribedSection buildSummary(ToolModel<?> model) {
        // XXX The whole dash between ceylon and the tool name is a bit of a 
        // man(1)-ism which we probably don't want to follow
        DescribedSection summary = new DescribedSection();
        summary.setRole(Role.SUMMARY);
        summary.setDescription(Markdown.markdown(
                "##" + sectionsBundle.getString("section.NAME") + "\n\n" +
                "`" + getCeylonInvocation(model) + "` - " + getSummaryValue(model)));
        return summary;
    }
    
    
    private String getName(ToolModel<?> model) {
        return model.getName();
    }
    
    private String msg(ResourceBundle toolBundle, String key) {
        if (toolBundle != null) {
            String msg = toolBundle.getString(key);
            if (msg != null) {
                return msg;
            }
        }
        return "";
    }
    
    private String getSummaryValue(ToolModel<?> model) {
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

    private ResourceBundle getToolBundle(ToolModel<?> model) {
        ResourceBundle toolBundle;
        try {
            toolBundle = ResourceBundle.getBundle(model.getToolClass().getName());
        } catch (MissingResourceException e) {
            toolBundle = null;
        }
        return toolBundle;
    }

    private Summary getSummary(ToolModel<?> model) {
        return model.getToolClass().getAnnotation(Summary.class);
    }

    private String getDescription(ToolModel<?> model) {
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
        RemainingSections sections = model.getToolClass().getAnnotation(RemainingSections.class);
        if (sections != null) {
            return sections.value();
        }
        return "";
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


}