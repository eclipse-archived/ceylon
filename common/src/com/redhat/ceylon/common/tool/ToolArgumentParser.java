package com.redhat.ceylon.common.tool;


/**
 * An {@link ArgumentParser} for {@link Tool}s. This is used for subtools
 * @author tom
 */
public class ToolArgumentParser implements ArgumentParser<Tool>{

    private ToolLoader loader;

    protected ToolArgumentParser(ToolLoader loader) {
        this.loader = loader;
    }
    
    ToolLoader getToolLoader() {
        return loader;
    }

    @Override
    public Tool parse(final String argument) {
        Tool instance = loader.instance(argument);
        if (instance == null) {
            throw new IllegalArgumentException(argument);
        }
        return instance;
    }
    
}
