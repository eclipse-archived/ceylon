package com.redhat.ceylon.common.tool;

public class ScriptToolModel<T extends Tool> extends ToolModel<T> {
    private final String scriptName;

    public ScriptToolModel(String name, String scriptName) {
        super(name);
        this.scriptName = scriptName;
    }

    public String getScriptName() {
        return scriptName;
    }
    
    @Override
    public boolean isPlumbing() {
        return false;
    }
}
