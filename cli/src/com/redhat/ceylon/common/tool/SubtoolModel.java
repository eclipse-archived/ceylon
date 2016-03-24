package com.redhat.ceylon.common.tool;

public class SubtoolModel<T extends Tool> extends ArgumentModel<T> {

    private ToolLoader toolLoader;
    
    public ToolLoader getToolLoader() {
        return toolLoader;
    }
    
    public void setParser(ArgumentParser<T> parser) {
        super.setParser(parser);
        this.toolLoader = ((ToolArgumentParser)parser).getToolLoader();
    }
    
}
