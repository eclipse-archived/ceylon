package com.redhat.ceylon.compiler.js;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.redhat.ceylon.common.tool.ArgumentParserFactory;
import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;

public class CompileJsToolTest {

    protected final ArgumentParserFactory apf = new ArgumentParserFactory();
    protected final ToolFactory pluginFactory = new ToolFactory(apf);
    protected final ToolLoader pluginLoader = new ToolLoader(apf){

        @Override
        protected String getToolName(String className) {
            return camelCaseToDashes(className.replaceAll("^(.*\\.)?Ceylon(.*)Tool$", "$2"));    
        }
    };

    private List<String> args(String... args) {
        return Arrays.asList(args);
    }
    
    @Test
    public void testLoad() {
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        pluginFactory.bindArguments(tool, args());
    }
    
}
