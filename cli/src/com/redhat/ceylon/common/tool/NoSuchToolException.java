package com.redhat.ceylon.common.tool;

public class NoSuchToolException extends OptionArgumentException.InvalidArgumentValueException {

    private String toolName;

    public NoSuchToolException(ArgumentModel<?> argumentModel, String toolName) {
        super(null, argumentModel, toolName);
        this.toolName = toolName;
    }
    
    public String getToolName() {
        return toolName;
    }

}
