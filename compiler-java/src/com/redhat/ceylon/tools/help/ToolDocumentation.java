package com.redhat.ceylon.tools.help;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.OptionModel;
import com.redhat.ceylon.common.tool.Plugin;
import com.redhat.ceylon.common.tool.PluginModel;
import com.redhat.ceylon.common.tool.Section;
import com.redhat.ceylon.common.tool.Sections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tools;

/**
 * Provides access to (possible localized) the tool documentation.
 * @param <T>
 */
class ToolDocumentation<T extends Plugin> {

    private PluginModel<T> model;
    
    private ResourceBundle bundle;

    public ToolDocumentation(PluginModel<T> model) {
        this.model = model;
        try {
            bundle = ResourceBundle.getBundle(model.getClass().getName());
        } catch (MissingResourceException e) {
            bundle = null;
        }
    }
    
    public PluginModel<T> getPlugin() {
        return model;
    }
    
    public String getName() {
        return model.getName();
    }
    
    private String msg(String key) {
        if (bundle != null) {
            String msg = bundle.getString(key);
            if (msg != null) {
                return msg;
            }
        }
        return "";
    }
    
    public String getSummary() {
        String msg = msg("summary");
        if (msg.isEmpty()) {
            Summary summary = model.getToolClass().getAnnotation(Summary.class);
            if (summary != null) {
                msg = summary.value();
            }
        }
        return msg;
    }

    public String getDescription() {
        String msg = msg("description");
        if (msg.isEmpty()) {
            Description description = model.getToolClass().getAnnotation(Description.class);
            if (description != null) {
                msg = description.value();
            }
        }
        return msg;
    }

    public List<Section> getSections() {
        List<Section> result = new ArrayList<Section>(3);
        Sections sections = model.getToolClass().getAnnotation(Sections.class);
        if (sections != null) {
            for (Section s : sections.value()) {
                result.add(s);
            }
        }
        return result;
    }
    
    public String getSectionName(Section section) {
        String msg = msg("section.name."+section.name());
        if (msg.isEmpty()) {
            msg = section.name();
        }
        return msg;
    }
    
    public String getSectionText(Section section) {
        String msg = msg("section.text."+section.name());
        if (msg.isEmpty()) {
            msg = section.text();
        }
        return msg;
    }

    public String getOptionDescription(OptionModel<?> opt) {
        String msg = msg("option."+opt.getLongName());
        if (msg.isEmpty()) {
            Description description = opt.getArgument().getSetter().getAnnotation(Description.class);
            if (description != null) {
                msg = description.value();
            }
        }
        return msg;
    }

    public String getCeylonInvocation() {
        return getName().isEmpty() ? Tools.progName(): Tools.progName() + " " + model.getName();
    }
    
}
