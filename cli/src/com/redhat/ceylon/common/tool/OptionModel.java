package com.redhat.ceylon.common.tool;

/**
 * A command line option or option argument accepted by a plugin
 */
public class OptionModel<A> {
    public static enum ArgumentType {
        NOT_ALLOWED,
        OPTIONAL,
        REQUIRED
    }
    private ToolModel<?> toolModel;
    private String longName;
    private Character shortName;
    private ArgumentModel<A> argument;
    private ArgumentType argumentType;
    
    public ToolModel<?> getToolModel() {
        return toolModel;
    }
    public void setToolModel(ToolModel<?> toolModel) {
        this.toolModel = toolModel;
    }
    public String getLongName() {
        return longName;
    }
    public void setLongName(String name) {
        this.longName = name;
    }
    public Character getShortName() {
        return shortName;
    }
    public void setShortName(Character shortName) {
        this.shortName = shortName;
    }
    public ArgumentModel<A> getArgument() {
        return argument;
    }
    public void setArgument(ArgumentModel<A> argument) {
        this.argument = argument;
    }
    public void setArgumentType(ArgumentType argumentType) {
        this.argumentType = argumentType;
    }
    public ArgumentType getArgumentType() {
        return this.argumentType;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (longName != null) {
            sb.append("--").append(longName);
        }
        if (longName != null && shortName != null) {
            sb.append("/");
        }
        if (shortName != null) {
            sb.append("-").append(shortName);
        }
        return sb.toString();
    }
}
