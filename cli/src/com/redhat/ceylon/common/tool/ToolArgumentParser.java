package com.redhat.ceylon.common.tool;


/**
 * An {@link ArgumentParser} for {@link Tool}s. This is used for subtools
 * @author tom
 */
public class ToolArgumentParser implements EnumerableParser<Tool>{

    private ToolLoader loader;

    protected ToolArgumentParser(ToolLoader loader) {
        this.loader = loader;
    }
    
    ToolLoader getToolLoader() {
        return loader;
    }

    @Override
    public Tool parse(final String argument, Tool tool) {
        Tool instance = loader.instance(argument, tool);
        if (instance == null) {
            throw new IllegalArgumentException(argument);
        }
        return instance;
    }

    @Override
    public Iterable<String> possibilities() {
        return loader.getToolNames();
    }
    
}
