package com.redhat.ceylon.tools.help;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.OptionModel;
import com.redhat.ceylon.common.tool.Tool;
import com.redhat.ceylon.common.tool.ToolModel;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tools;

/**
 * Provides access to (possible localized) the tool documentation.
 * @param <T>
 */
class ToolDocumentation<T extends Tool> {

    private ToolModel<T> model;
    
    private ResourceBundle bundle;

    public ToolDocumentation(ToolModel<T> model) {
        this.model = model;
        try {
            bundle = ResourceBundle.getBundle(model.getClass().getName());
        } catch (MissingResourceException e) {
            bundle = null;
        }
    }
    
    public ToolModel<T> getPlugin() {
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
    
    public String getSummaryValue() {
        String msg = msg("summary");
        if (msg.isEmpty()) {
            Summary summary = getSummary();
            if (summary != null) {
                msg = summary.value();
            }
        }
        return msg;
    }

    private Summary getSummary() {
        return model.getToolClass().getAnnotation(Summary.class);
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

    public String getSections() {
        RemainingSections sections = model.getToolClass().getAnnotation(RemainingSections.class);
        if (sections != null) {
            return sections.value();
        }
        return "";
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
