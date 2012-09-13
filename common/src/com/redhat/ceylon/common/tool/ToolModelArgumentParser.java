package com.redhat.ceylon.common.tool;

/**
 * An {@link ArgumentParser} for {@link ToolModel}s. This is useful for tools 
 * which operate on other tool's models, such as help tools and tool 
 * documentation generators. 
 * @author tom
 */
public class ToolModelArgumentParser implements ArgumentParser<ToolModel<?>>{

    private ToolLoader loader;

    protected ToolModelArgumentParser(ToolLoader loader) {
        this.loader = loader;
    }
    
    ToolLoader getToolLoader() {
        return loader;
    }

    @Override
    public ToolModel<?> parse(String argument) {
        return loader.loadToolModel(argument);
    }
    
}
