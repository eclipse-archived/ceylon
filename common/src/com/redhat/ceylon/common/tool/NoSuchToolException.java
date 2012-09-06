package com.redhat.ceylon.common.tool;

public class NoSuchToolException extends ToolException {

    private String toolName;
    
    public NoSuchToolException() {
        super();
    }

    public NoSuchToolException(String toolName) {
        super(ToolMessages.msg("exception.no.such.tool", toolName));
        this.toolName = toolName;
    }
    
    public String getToolName() {
        return toolName;
    }


}
