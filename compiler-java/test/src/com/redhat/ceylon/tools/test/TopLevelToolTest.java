package com.redhat.ceylon.tools.test;

import org.junit.Test;

import com.redhat.ceylon.common.tool.ArgumentParserFactory;
import com.redhat.ceylon.common.tool.PluginFactory;
import com.redhat.ceylon.common.tool.PluginLoader;
import com.redhat.ceylon.common.tool.Tool;

public class TopLevelToolTest {

    protected final ArgumentParserFactory apf = new ArgumentParserFactory();
    protected final PluginFactory pluginFactory = new PluginFactory(apf);
    protected final PluginLoader pluginLoader = new PluginLoader(apf);
    
    private String[] args(String...args) {
        return args;
    }
    
    @Test
    public void testNoArgs()  throws Exception {
        new Tool().bootstrap(args());
    }
    
    @Test
    public void testVersion()  throws Exception {
        new Tool().bootstrap(args("--version"));
    }
    
    @Test
    public void testEmptyArg()  throws Exception {
        new Tool().bootstrap(args(""));
    }
    
    @Test
    public void testHelp()  throws Exception {
        new Tool().bootstrap(args("help"));
    }
    
    @Test
    public void testHelpOption()  throws Exception {
        new Tool().bootstrap(args("--help"));
    }
    
}
