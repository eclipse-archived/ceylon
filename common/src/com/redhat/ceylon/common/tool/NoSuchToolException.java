package com.redhat.ceylon.common.tool;

@NonFatal
public class NoSuchToolException extends OptionArgumentException.InvalidArgumentValueException {

    private String toolName;

    public NoSuchToolException(ArgumentModel<?> argumentModel, String toolName) {
        super(argumentModel, toolName);
        this.toolName = toolName;
    }
    
    public String getToolName() {
        return toolName;
    }


}
