package com.redhat.ceylon.common.tool;

public class NoSuchToolException extends OptionArgumentException.InvalidArgumentValueException {

    private static final long serialVersionUID = 4847884252620666903L;
    
    private String toolName;

    public NoSuchToolException(ArgumentModel<?> argumentModel, String toolName) {
        super(null, argumentModel, toolName);
        this.toolName = toolName;
    }
    
    public String getToolName() {
        return toolName;
    }

}
