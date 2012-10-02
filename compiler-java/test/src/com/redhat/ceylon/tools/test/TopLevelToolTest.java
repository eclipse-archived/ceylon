package com.redhat.ceylon.tools.test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redhat.ceylon.common.tool.ToolFactory;
import com.redhat.ceylon.common.tool.ToolLoader;
import com.redhat.ceylon.tools.CeylonTool;
import com.redhat.ceylon.tools.TestingToolLoader;
import com.redhat.ceylon.tools.help.CeylonHelpTool;

public class TopLevelToolTest {

    protected final ToolFactory toolFactory = new ToolFactory();
    protected final ToolLoader toolLoader = new TestingToolLoader(null, true);
    
    private String[] args(String...args) {
        return args;
    }
    
    private CeylonTool tool;
    
    @Before
    public void setup() {
        tool = new CeylonTool();
        tool.setToolLoader(toolLoader);
    }
    
    class CapturingStdOut implements AutoCloseable {
        private PrintStream savedOut;
        private PrintStream savedErr;
        private ByteArrayOutputStream redirectedOut;
        private ByteArrayOutputStream redirectedErr;

        public CapturingStdOut() {
            open();
        }

        private void open() {
            this.redirectedOut = new ByteArrayOutputStream();
            this.redirectedErr= new ByteArrayOutputStream();
            this.savedOut = System.out;
            this.savedErr = System.err;
            System.setOut(new PrintStream(redirectedOut));
            System.setErr(new PrintStream(redirectedErr));
        }

        @Override
        public void close() throws Exception {
            System.out.flush();
            System.err.flush();
            System.setOut(savedOut);
            System.setErr(savedErr);
        }
        
        public String getOut() {
            return new String(redirectedOut.toByteArray());
        }
        
        public String getErr() {
            return new String(redirectedErr.toByteArray());
        }
    }
    
    private String getHelpOutput(String toolName) {
        StringWriter sw = new StringWriter();
        CeylonHelpTool helpTool = new CeylonHelpTool();
        helpTool.setToolLoader(toolLoader);
        if (toolName != null
                && !toolName.isEmpty()) {
            helpTool.setTool(toolLoader.loadToolModel(toolName));
        }
        helpTool.setOut(sw);
        helpTool.run();
        return sw.toString();
    }
    
    @Test
    public void testNoArgs()  throws Exception {
        try (CapturingStdOut out = new CapturingStdOut()) {
            Assert.assertEquals(CeylonTool.SC_OK, tool.bootstrap(args()));
            Assert.assertEquals(getHelpOutput(""), out.getOut());
            Assert.assertTrue(out.getErr().isEmpty());
        } 
        Assert.assertEquals("help", tool.getToolName());
    }

    @Test
    public void testVersionOption()  throws Exception {
        try (CapturingStdOut out = new CapturingStdOut()) {
            Assert.assertEquals(CeylonTool.SC_OK, tool.bootstrap(args("--version")));
            Assert.assertTrue(out.getOut(), out.getOut().startsWith("ceylon version "));
            Assert.assertTrue(out.getErr().isEmpty());
        }
        Assert.assertEquals(null, tool.getToolName());
    }
    
    @Test
    public void testVersionOptionHelp()  throws Exception {
        // --version beats everything
        try (CapturingStdOut out = new CapturingStdOut()) {
            Assert.assertEquals(CeylonTool.SC_OK, tool.bootstrap(args("--version", "help")));
            Assert.assertTrue(out.getOut(), out.getOut().startsWith("ceylon version "));
            Assert.assertTrue(out.getErr().isEmpty());
        }
        Assert.assertEquals(null, tool.getToolName());
    }
    
    @Test
    public void testHelpVersionOption()  throws Exception {
        // We expect NO_SUCH_TOOL in this case because the HelpTool 
        // isn't loadable by the toolLoader we're using
        try (CapturingStdOut out = new CapturingStdOut()) {
            Assert.assertEquals(CeylonTool.SC_ARGS, tool.bootstrap(args("help", "--version")));
            Assert.assertTrue(out.getOut().isEmpty());
            Assert.assertTrue(out.getErr(), out.getErr().contains("ceylon help: Fatal error: Unrecognised option(s): --version"));
        }
        Assert.assertEquals("help", tool.getToolName());
    }
    
    @Test
    public void testEmptyArg()  throws Exception {
        Assert.assertEquals(CeylonTool.SC_OK, tool.bootstrap(args("")));
        Assert.assertEquals("help", tool.getToolName());
    }
    
    @Test
    public void testHelp()  throws Exception {
        Assert.assertEquals(CeylonTool.SC_OK, tool.bootstrap(args("help")));    
        Assert.assertEquals("help", tool.getToolName());
    }
    
    @Test
    public void testHelpOption()  throws Exception {
        Assert.assertEquals(CeylonTool.SC_OK, tool.bootstrap(args("--help")));
        Assert.assertEquals("help", tool.getToolName());
    }
    
    @Test
    public void testExample()  throws Exception {
        try (CapturingStdOut out = new CapturingStdOut()) {
            Assert.assertEquals(CeylonTool.SC_OK, tool.bootstrap(args("example")));
            Assert.assertTrue(out.getErr(), out.getErr().isEmpty());
            Assert.assertTrue(out.getOut(), out.getOut().isEmpty());
        }
        Assert.assertEquals("example", tool.getToolName());
    }
    
    @Test
    public void testStacktracesOptionExample()  throws Exception {
        try (CapturingStdOut out = new CapturingStdOut()) {
            Assert.assertEquals(CeylonTool.SC_OK, tool.bootstrap(args("--stacktraces", "example")));
            Assert.assertTrue(out.getErr(), out.getErr().isEmpty());
            Assert.assertTrue(out.getOut(), out.getOut().isEmpty());
        }
        Assert.assertEquals("example", tool.getToolName());
        Assert.assertTrue(tool.getStacktraces());
    }
    
    @Test
    public void testFileOptionExample()  throws Exception {
        // the top level tool doesn't take a --file option
        try (CapturingStdOut out = new CapturingStdOut()) {
            Assert.assertEquals(CeylonTool.SC_ARGS, tool.bootstrap(args("--file=.", "example")));
            Assert.assertEquals(out.getErr().trim(), "ceylon example: Fatal error: Unrecognised option(s): --file=.");
            Assert.assertTrue(out.getOut(), out.getOut().isEmpty());
        }
    }
    
    @Test
    public void testExampleFileOption()  throws Exception {
        try (CapturingStdOut out = new CapturingStdOut()) {
            Assert.assertEquals(CeylonTool.SC_OK, tool.bootstrap(args("example", "--file=.")));
            Assert.assertTrue(out.getOut(), out.getOut().isEmpty());
            Assert.assertTrue(out.getErr(), out.getErr().isEmpty());
        }
    }
    
    @Test
    public void testBashCompletion()  throws Exception {
        Assert.assertEquals(CeylonTool.SC_OK, tool.bootstrap(
                args("bash-completion", "--cword=1", "--", "./cey")));
    }
    
    
}
