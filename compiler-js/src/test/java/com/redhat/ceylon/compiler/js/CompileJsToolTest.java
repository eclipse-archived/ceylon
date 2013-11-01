package com.redhat.ceylon.compiler.js;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.common.tool.ServiceToolLoader;
import com.redhat.ceylon.common.tool.Tool;
import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.common.tool.ToolModel;

public class CompileJsToolTest {

    protected final ToolFactory pluginFactory = new ToolFactory();
    protected final ToolLoader pluginLoader = new ServiceToolLoader(Tool.class) {
        
        @Override
        public String getToolName(String className) {
            return classNameToToolName(className);
        }
        
        
    };

    private List<String> args(String... args) {
        return Arrays.asList(args);
    }
    
    @Test
    public void testLoad() {
        ToolModel<CeylonCompileJsTool> tool = pluginLoader.loadToolModel("compile-js");
        Assert.assertNotNull(tool);
        pluginFactory.bindArguments(tool, args());
    }
    
}
