package com.redhat.ceylon.tools.test;

import org.junit.Test;

import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.tools.CeylonTool;
import com.redhat.ceylon.tools.CeylonToolLoader;

public class TopLevelToolTest {

    protected final ToolFactory pluginFactory = new ToolFactory();
    protected final ToolLoader pluginLoader = new CeylonToolLoader(null);
    
    private String[] args(String...args) {
        return args;
    }
    
    @Test
    public void testNoArgs()  throws Exception {
        new CeylonTool().bootstrap(args());
    }
    
    @Test
    public void testVersion()  throws Exception {
        new CeylonTool().bootstrap(args("--version"));
    }
    
    @Test
    public void testEmptyArg()  throws Exception {
        new CeylonTool().bootstrap(args(""));
    }
    
    @Test
    public void testHelp()  throws Exception {
        new CeylonTool().bootstrap(args("help"));
    }
    
    @Test
    public void testHelpOption()  throws Exception {
        new CeylonTool().bootstrap(args("--help"));
    }
    
}
