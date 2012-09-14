package com.redhat.ceylon.tools;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.tools.CeylonTool;

public class TopLevelToolTest {

    protected final ToolFactory pluginFactory = new ToolFactory();
    protected final ToolLoader pluginLoader = new CeylonToolLoader(null);
    
    private CeylonTool tool;
    
    @Before
    public void setup() {
        tool = new CeylonTool();
    }
    
    private String[] args(String...args) {
        return args;
    }
    
    @Test
    public void testNoArgs()  throws Exception {
        Assert.assertEquals(CeylonTool.SC_NO_SUCH_TOOL, tool.bootstrap(args()));
        Assert.assertEquals("help", tool.getToolName());
    }
    
    @Test
    public void testVersionOption()  throws Exception {
        Assert.assertEquals(CeylonTool.SC_OK, tool.bootstrap(args("--version")));
        Assert.assertEquals(null, tool.getToolName());
    }
    
    @Test
    public void testVersionOptionHelp()  throws Exception {
        // --version beats everything
        Assert.assertEquals(CeylonTool.SC_OK, tool.bootstrap(args("--version", "help")));
        Assert.assertEquals(null, tool.getToolName());
    }
    
    @Test
    public void testHelpVersionOption()  throws Exception {
        // We expect NO_SUCH_TOOL in this case because the HelpTool doesn't 
        // exist in ceylon-common
        Assert.assertEquals(CeylonTool.SC_NO_SUCH_TOOL, tool.bootstrap(args("help", "--version")));
        Assert.assertEquals("help", tool.getToolName());
    }
    
    @Test
    public void testEmptyArg()  throws Exception {
        Assert.assertEquals(CeylonTool.SC_NO_SUCH_TOOL, tool.bootstrap(args("")));
        Assert.assertEquals("help", tool.getToolName());
    }
    
    @Test
    public void testHelp()  throws Exception {
        Assert.assertEquals(CeylonTool.SC_NO_SUCH_TOOL, tool.bootstrap(args("help")));
        Assert.assertEquals("help", tool.getToolName());
    }
    
    @Test
    public void testHelpOption()  throws Exception {
        Assert.assertEquals(CeylonTool.SC_NO_SUCH_TOOL, tool.bootstrap(args("--help")));
        Assert.assertEquals("help", tool.getToolName());
    }
    
    @Test
    public void testExample()  throws Exception {
        Assert.assertEquals(CeylonTool.SC_OK, tool.bootstrap(args("example")));
        Assert.assertEquals("example", tool.getToolName());
    }
    
    @Test
    public void testStacktracesOptionExample()  throws Exception {
        Assert.assertEquals(CeylonTool.SC_OK, tool.bootstrap(args("--stacktraces", "example")));
        Assert.assertEquals("example", tool.getToolName());
        Assert.assertTrue(tool.getStacktraces());
    }
    
    @Test
    public void testFileOptionExample()  throws Exception {
        // the top level tool doesn't take a --file option
        Assert.assertEquals(CeylonTool.SC_ARGS, tool.bootstrap(args("--file=.", "example")));
    }
    
    @Test
    public void testExampleFileOption()  throws Exception {
        // the top level tool doesn't take a --file option
        Assert.assertEquals(CeylonTool.SC_OK, tool.bootstrap(args("example", "--file=.")));
    }
    
    @Test
    public void testBashCompletion()  throws Exception {
        Assert.assertEquals(CeylonTool.SC_OK, tool.bootstrap(
                args("bash-completion", "--cword=1", "--", "./cey")));
    }
    
    
}
